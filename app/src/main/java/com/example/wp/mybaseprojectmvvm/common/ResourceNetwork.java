package com.example.wp.mybaseprojectmvvm.common;

import com.example.wp.resource.basic.model.BasicNetwork;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ResourceNetwork extends BasicNetwork {
	private String token;
	
	public ResourceNetwork(String baseUrl, String token) {
		super(baseUrl);
		this.token = token;
	}
	
	@Override
	protected Headers buildHeaders(RequestBody requestBody) {
		// return new Headers.Builder()
		// 		.add("system", "Android")
		// 		.add("systemVersion", "4.0.0")
		// 		.add("device", "test")
		// 		.add("versionName", "1.0.0")
		// 		.add("versionCode", "1")
		// 		.add("IMEI", "imei")
		// 		.add("platformId", "1")
		// 		.build();
		
		return new Headers.Builder().build();
	}
	
	@Override
	protected Request convertGetRequest(Request request, HttpUrl httpUrl) {
		HttpUrl.Builder builder = httpUrl.newBuilder();
		// builder.addQueryParameter("token", this.token);
		// builder.addQueryParameter("accountId", this.token);
		
		return request.newBuilder()
				.url(builder.build())
				.headers(buildHeaders(request.body()))
				.build();
	}
	
	@Override
	protected RequestBody convertBody(FormBody formBody) {
		FormBody.Builder builder = new FormBody.Builder();
		// builder.add("token", this.token);
		
		int size = formBody.size();
		for (int index = 0; index < size; index++) {
			builder.add(formBody.name(index), formBody.value(index));
		}
		
		return builder.build();
	}
	
	@Override
	protected RequestBody convertBody(MultipartBody multipartBody) {
		MultipartBody.Builder builder = new MultipartBody.Builder();
		// builder.addFormDataPart("token", this.token);
		// builder.addFormDataPart("accountId", this.token);
		
		for (MultipartBody.Part part : multipartBody.parts()) {
			builder.addPart(part);
		}
		return builder.build();
	}
	
	@Override
	protected RequestBody convertBody(RequestBody requestBody) {
		if (requestBody.contentType() != null) {
			return requestBody;
		}
		
		FormBody.Builder builder = new FormBody.Builder();
		// builder.add("token", this.token);
		// builder.add("accountId", this.token);
		
		return builder.build();
	}
}
