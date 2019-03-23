package com.example.wp.resource.basic.model;

import com.example.wp.resource.basic.BasicApp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class BasicNetwork {
	private final String baseUrl;
	private final Retrofit retrofit;
	
	public BasicNetwork(String baseUrl) {
		if (baseUrl == null) {
			throw new NullPointerException("networkListener can't be null!");
		}
		this.baseUrl = baseUrl;
		
		Retrofit.Builder builder = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.client(createOkHttpClient())
				.addConverterFactory(CustomGsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
		
		this.retrofit = builder.build();
	}
	
	public final <T> T createService(Class<T> service) {
		return this.retrofit.create(service);
	}
	
	private OkHttpClient createOkHttpClient() {
		return new OkHttpClient.Builder()
				// .addInterceptor(new Interceptor() {
				// 	@Override
				// 	public Response intercept(@NonNull Chain chain) throws IOException {
				// 		Request request = chain.request();
				// 		String requestUrl = request.url().toString();
				// 		if (!needRebuildRequestUrl(requestUrl)) {
				// 			return convertResponse(chain.proceed(request));
				// 		}
				// 		requestUrl = buildRequestUrl(requestUrl);
				// 		HttpUrl httpUrl = buildHttpUrl(request.method(), request.url().newBuilder(requestUrl));
				//
				// 		request = convertRequest(request, httpUrl);
				//
				// 		return convertResponse(chain.proceed(request));
				// 	}
				// })
				.connectTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.build();
	}
	
	// protected String getReplaceUrlPrefix() {
	// 	return BasicConst.REPLACE_URL_PREFIX;
	// }
	
	/**
	 * 判断是否重新构建请求地址
	 *
	 * @param requestUrl 请求地址
	 * @return true重新构建
	 */
	// protected boolean needRebuildRequestUrl(String requestUrl) {
	// 	return requestUrl.startsWith(getReplaceUrlPrefix());
	// }
	
	/**
	 * 构建请求地址
	 *
	 * @param requestUrl 请求地址
	 * @return 请求地址
	 */
	// protected String buildRequestUrl(String requestUrl) {
	// 	return this.baseUrl + requestUrl.substring(getReplaceUrlPrefix().length());
	// }
	
	/**
	 * 构建请求地址
	 *
	 * @param method  请求方式
	 * @param builder 请求地址构建
	 * @return 请求地址
	 */
	protected HttpUrl buildHttpUrl(String method, HttpUrl.Builder builder) {
		return builder.build();
	}
	
	/**
	 * 转换请求实例
	 *
	 * @param request 请求实例
	 * @param httpUrl 请求地址
	 * @return 请求实例
	 */
	protected Request convertRequest(Request request, HttpUrl httpUrl) {
		if (request.method().equals("GET")) {
			return convertGetRequest(request, httpUrl);
		} else if (request.method().equals("POST")) {
			return convertPostRequest(request, httpUrl);
		}
		return request.newBuilder()
				.url(httpUrl)
				.build();
	}
	
	/**
	 * 转换GET请求实例
	 *
	 * @param request 请求实例
	 * @param httpUrl 请求地址
	 * @return 请求实例
	 */
	protected Request convertGetRequest(Request request, HttpUrl httpUrl) {
		return request.newBuilder()
				.url(httpUrl)
				.headers(buildHeaders(request.body()))
				.build();
	}
	
	/**
	 * 转换POST请求实例
	 *
	 * @param request 请求实例
	 * @param httpUrl 请求地址
	 * @return 请求实例
	 */
	protected Request convertPostRequest(Request request, HttpUrl httpUrl) {
		RequestBody requestBody = request.body();
		if (requestBody instanceof FormBody) {
			return request.newBuilder()
					.url(httpUrl)
					.headers(buildHeaders(requestBody))
					.method(request.method(), convertBody((FormBody) requestBody))
					.build();
		} else if (requestBody instanceof MultipartBody) {
			return request.newBuilder()
					.url(httpUrl)
					.headers(buildHeaders(requestBody))
					.method(request.method(), convertBody((MultipartBody) requestBody))
					.build();
		}
		return request.newBuilder()
				.url(httpUrl)
				.headers(buildHeaders(requestBody))
				.method(request.method(), convertBody(requestBody))
				.build();
	}
	
	/**
	 * 构建HEADER实例
	 *
	 * @param requestBody 请求实体
	 * @return HEADER实例
	 */
	protected Headers buildHeaders(RequestBody requestBody) {
		return new Headers.Builder().build();
	}
	
	/**
	 * 转换表单实体
	 *
	 * @param formBody 表单实体
	 * @return 表单实体
	 */
	protected RequestBody convertBody(FormBody formBody) {
		return formBody;
	}
	
	/**
	 * 转换多表单实体
	 *
	 * @param multipartBody 多表单实体
	 * @return 多表单实体
	 */
	protected RequestBody convertBody(MultipartBody multipartBody) {
		return multipartBody;
	}
	
	/**
	 * 转换请求实体
	 *
	 * @param requestBody 请求实体
	 * @return 请求实体
	 */
	protected RequestBody convertBody(RequestBody requestBody) {
		return requestBody;
	}
	
	/**
	 * 转换结果实例
	 *
	 * @param response 结果实例
	 * @return 结果实例
	 */
	protected Response convertResponse(Response response) throws IOException {
		if (BasicApp.isDebug()) {
			StringBuilder builder = new StringBuilder();
			
			builder.append("=========== Request ===========")
					.append("\n");
			builder.append(response.request().method())
					.append(" ")
					.append(response.request().url().encodedPath());
			String queryStr = response.request().url().encodedQuery();
			if (queryStr != null) {
				builder.append("?").append(queryStr);
			}
			builder.append(" ")
					.append(response.protocol())
					.append("\n");
			builder.append("Host: ")
					.append(response.request().url().host())
					.append(":")
					.append(response.request().url().port())
					.append("\n");
			Headers requestHeaders = response.request().headers();
			for (int index = 0; index < requestHeaders.size(); index++) {
				builder.append(requestHeaders.name(index))
						.append(": ")
						.append(requestHeaders.value(index))
						.append("\n");
			}
			builder.append("\n");
			
			RequestBody requestBody = response.request().body();
			if (requestBody instanceof FormBody) {
				FormBody formBody = (FormBody) requestBody;
				for (int index = 0; index < formBody.size(); index++) {
					builder.append(formBody.name(index))
							.append("=")
							.append(formBody.value(index))
							.append("&");
				}
				if (formBody.size() > 0) {
					builder.setLength(builder.length() - 1);
				}
			} else if (requestBody != null) {
				builder.append(requestBody.toString());
			}
			builder.append("\n")
					.append("=========== Request ===========");
			
			builder.append("\n")
					.append("=========== Response ===========")
					.append("\n");
			builder.append(response.code())
					.append(" ")
					.append(response.message())
					.append("\n");
			Headers responseHeaders = response.headers();
			for (int index = 0; index < responseHeaders.size(); index++) {
				builder.append(responseHeaders.name(index))
						.append(": ")
						.append(responseHeaders.value(index))
						.append("\n");
			}
			builder.append("\n");
			
			ResponseBody responseBody = response.body();
			if (responseBody != null) {
				String responseStr = responseBody.string();
				builder.append(responseStr).append("\n");
				response = response.newBuilder()
						.body(ResponseBody.create(responseBody.contentType(), responseStr))
						.build();
			}
			builder.append("=========== Response ===========");
			
			System.out.println(builder);
		}
		return response;
	}
}
