package com.example.wp.resource.basic.model;

import android.text.TextUtils;
import android.util.Log;

import com.example.wp.resource.basic.BasicApp;
import com.example.wp.resource.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by wp on 2018/4/11.
 */

public class CustomGsonConverterFactory extends Converter.Factory {
	
	private final Gson gson;
	private String TAG = CustomGsonConverterFactory.class.getSimpleName();
	
	private CustomGsonConverterFactory(Gson gson) {
		if (gson == null) throw new NullPointerException("gson == null");
		this.gson = gson;
	}
	
	public static CustomGsonConverterFactory create() {
		return create(new Gson());
	}
	
	public static CustomGsonConverterFactory create(Gson gson) {
		return new CustomGsonConverterFactory(gson);
	}
	
	@Override
	public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
		return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
	}
	
	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
		TypeToken typeToken = TypeToken.get(type);
		TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
		if (typeToken.getRawType() == BasicBean.class) {
			return new BaseResponseBodyConverter<>();
		}
		if (BasicBean.class.isAssignableFrom(typeToken.getRawType())) {
			if (ArrayBean.class.isAssignableFrom(typeToken.getRawType())) {
				return new ArrayResponseBodyConverter<>(type);
			} else {
				return new ObjectResponseBodyConverter<>(type);
			}
		}
		return new OtherResponseBodyConverter<>(gson, adapter);
	}
	
	private class BaseResponseBodyConverter<T> implements Converter<ResponseBody, BasicBean> {
		@Override
		public BasicBean convert(@NonNull ResponseBody value) throws IOException {
			BaseValueBean basicBean = gson.fromJson(value.charStream(), BaseValueBean.class);
			// if (BasicApp.isDebug()) {
			// 	LogUtils.json(value.string());
			// 	LogUtils.i(TAG, "code = " + basicBean.statusCode
			// 			+ ", message = " + basicBean.statusMessage
			// 			+ ", data = " + basicBean.resultData);
			// }
			
			BasicBean resultBean = new BasicBean();
			resultBean.statusInfo.statusCode = basicBean.statusCode;
			resultBean.statusInfo.statusMessage = basicBean.statusMessage;
			resultBean.resultData = basicBean.resultData;
			
			return resultBean;
		}
	}
	
	private class ObjectResponseBodyConverter<T extends BasicBean> implements Converter<ResponseBody, T> {
		private Type type;
		
		ObjectResponseBodyConverter(Type type) {
			this.type = type;
		}
		
		@Override
		public T convert(@NonNull ResponseBody value) throws IOException {
			// BaseBean basicBean = gson.fromJson(value.charStream(), BaseBean.class);
			// if (BasicApp.isDebug()) {
			// 	LogUtils.i(TAG, "code = " + basicBean.statusCode
			// 			+ ", message = " + basicBean.statusMessage
			// 			+ ", data = " + basicBean.resultData.toString());
			// }
			T resultBean = null;
			JsonParser parse = new JsonParser();
			try {
				JsonObject json = (JsonObject) parse.parse(value.string());
				JsonElement jsonElement = json.get("data");
				// LogUtils.d("-----jsonElement = " + jsonElement);
				String jsonStr;
				if (jsonElement != null && jsonElement.isJsonObject()) {
					jsonStr = jsonElement.getAsJsonObject().toString();
				} else {
					jsonStr = "{}";
				}
				// LogUtils.d("-----jsonStr = " + jsonStr);
				resultBean = gson.fromJson(jsonStr, this.type);
				resultBean.statusInfo.statusCode = json.get("respCode").getAsInt();
				resultBean.statusInfo.statusMessage = json.get("respMsg").getAsString();
				resultBean.resultData = jsonElement != null ? jsonElement.toString() : "";
				
				LogUtils.i(TAG, "statusCode = " + resultBean.statusInfo.statusCode
						+ ", statusMessage = " + resultBean.statusInfo.statusMessage
						+ ", resultData = " + resultBean.resultData);
			} catch (JsonIOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			return resultBean;
		}
	}
	
	private class ArrayResponseBodyConverter<T extends ArrayBean> implements Converter<ResponseBody, T> {
		private Type type;
		
		ArrayResponseBodyConverter(Type type) {
			this.type = type;
		}
		
		@Override
		public T convert(@NonNull ResponseBody value) throws IOException {
			// BaseListBean basicBean = gson.fromJson(value.charStream(), BaseListBean.class);
			// if (BasicApp.isDebug()) {
			// 	// LogUtils.json(value.string());
			// 	LogUtils.i(TAG, "code = " + basicBean.statusCode
			// 			+ ", message = " + basicBean.statusMessage
			// 			+ ", data = " + basicBean.resultData);
			// }
			// T resultBean = gson.fromJson(
			// 		String.format("{\"result\":%s}", basicBean.resultData == null ? "[]" : gson.toJson(basicBean.resultData)),
			// 		this.type);
			// resultBean.totalCount = basicBean.totalCount;
			// resultBean.statusInfo.statusCode = basicBean.statusCode;
			// resultBean.statusInfo.statusMessage = basicBean.statusMessage;
			
			T resultBean = null;
			JsonParser parse = new JsonParser();
			try {
				JsonObject json = (JsonObject) parse.parse(value.string());
				JsonElement jsonElement = json.get("data");
				String jsonStr = "[]";
				if (jsonElement != null && jsonElement.isJsonArray()) {
					jsonStr = jsonElement.getAsJsonArray().toString();
				}
				resultBean = gson.fromJson(String.format("{\"result\":%s}", jsonStr), this.type);
				resultBean.statusInfo.statusCode = json.get("respCode").getAsInt();
				resultBean.statusInfo.statusMessage = json.get("respMsg").getAsString();
				JsonElement count = json.get("count");
				resultBean.totalCount = count != null && !TextUtils.equals("null", count.toString()) ? count.getAsInt() : 0;
				resultBean.resultData = jsonElement != null ? jsonElement.toString() : "";
				
				LogUtils.i(TAG, "statusCode = " + resultBean.statusInfo.statusCode
						+ ", statusMessage = " + resultBean.statusInfo.statusMessage
						+ ", resultData = " + resultBean.resultData);
			} catch (JsonIOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			return resultBean;
		}
	}
	
	private class OtherResponseBodyConverter<T> implements Converter<ResponseBody, T> {
		private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
		private final Charset UTF_8 = Charset.forName("UTF-8");
		private final Gson gson;
		private final TypeAdapter<T> adapter;
		
		OtherResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
			this.gson = gson;
			this.adapter = adapter;
		}
		
		@Override
		public T convert(@NonNull ResponseBody value) throws IOException {
			String response = value.string();
			// BasicBean baseBean = gson.fromJson(response, BasicBean.class);
			// // if (httpStatus.isCodeInvalid()) {
			// // 	value.close();
			// // 	throw new ApiException(httpStatus.getCode(), httpStatus.getMessage());
			// // }
			Log.d(TAG, "isDebug : " + BasicApp.isDebug());
			if (BasicApp.isDebug()) {
				LogUtils.json(response);
			}
			
			MediaType contentType = value.contentType();
			Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
			InputStream inputStream = new ByteArrayInputStream(response.getBytes());
			Reader reader = null;
			JsonReader jsonReader = null;
			if (charset != null) {
				reader = new InputStreamReader(inputStream, charset);
				jsonReader = gson.newJsonReader(reader);
			}
			
			try {
				return adapter.read(jsonReader);
			} finally {
				value.close();
			}
		}
	}
	
	private static class BaseBean {
		@SerializedName("respCode")
		int statusCode;
		@SerializedName("respMsg")
		String statusMessage;
		@SerializedName("data")
		JsonObject resultData;
	}
	
	private static class BaseListBean {
		@SerializedName("respCode")
		int statusCode;
		@SerializedName("respMsg")
		String statusMessage;
		@SerializedName("data")
		JsonArray resultData;
		@SerializedName("count")
		int totalCount;
	}
	
	private static class BaseValueBean {
		@SerializedName("respCode")
		int statusCode;
		@SerializedName("respMsg")
		String statusMessage;
		@SerializedName("data")
		String resultData;
	}
}
