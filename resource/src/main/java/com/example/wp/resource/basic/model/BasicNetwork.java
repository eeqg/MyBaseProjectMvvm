package com.example.wp.resource.basic.model;

import android.util.Log;

import com.example.wp.resource.basic.BasicApp;
import com.example.wp.resource.utils.LogUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class BasicNetwork {
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private final String TAG = getClass().getSimpleName();

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
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        String requestUrl = request.url().toString();
                        // requestUrl = buildRequestUrl(requestUrl);
                        HttpUrl httpUrl = buildHttpUrl(request.method(), request.url().newBuilder(requestUrl));
                        request = convertRequest(request, httpUrl);

                        return convertResponse(chain.proceed(request));
                    }
                })
                // .addInterceptor(LogInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

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
                    // .append(response.request().url().encodedPath())
                    .append(response.request().url());
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
                //print params
                // Buffer buffer = new Buffer();
                // requestBody.writeTo(buffer);
                // Charset charset = Charset.forName("UTF-8");
                // MediaType contentType = requestBody.contentType();
                // if (contentType != null) {
                // 	charset = contentType.charset(UTF_8);
                // }
                // String paramsStr = buffer.readString(charset);
                String paramsStr = bodyToString(requestBody);
                builder.append("\nparams: ").append(paramsStr);
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

    private final Interceptor LogInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            // Request request = chain.request();
            // long t1 = System.nanoTime();
            // printLog(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            // Response response = chain.proceed(request);
            // long t2 = System.nanoTime();
            // printLog(String.format(Locale.CHINA, "Received response for %s in %.1fms%n%s",
            // 		response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            // return response;

            Request oldRequest = chain.request();
            Request.Builder newRequestBuild = null;
            String method = oldRequest.method();
            String postBodyString = "";
            if ("POST".equals(method)) {
                RequestBody oldBody = oldRequest.body();
                if (oldBody instanceof FormBody) {
                    FormBody.Builder formBodyBuilder = new FormBody.Builder();
                    // formBodyBuilder.add("deviceOs", iCommon.DEVICE_OS);
                    // formBodyBuilder.add("appVersion", Utils.instance().getAppVersionName());
                    // formBodyBuilder.add("appName", Utils.instance().getAppNameNew());
                    newRequestBuild = oldRequest.newBuilder();

                    RequestBody formBody = formBodyBuilder.build();
                    postBodyString = bodyToString(oldRequest.body());
                    postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
                    newRequestBuild.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
                } else if (oldBody instanceof MultipartBody) {
                    MultipartBody oldBodyMultipart = (MultipartBody) oldBody;
                    List<MultipartBody.Part> oldPartList = oldBodyMultipart.parts();
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);
                    // RequestBody requestBody1 = RequestBody.create(MediaType.parse("text/plain"), iCommon.DEVICE_OS);
                    // RequestBody requestBody2 = RequestBody.create(MediaType.parse("text/plain"), Utils.instance().getAppNameNew());
                    // RequestBody requestBody3 = RequestBody.create(MediaType.parse("text/plain"), Utils.instance().getAppVersionName());
                    for (MultipartBody.Part part : oldPartList) {
                        builder.addPart(part);
                        postBodyString += (bodyToString(part.body()) + "\n");
                    }
                    // postBodyString += (bodyToString(requestBody1) + "\n");
                    // postBodyString += (bodyToString(requestBody2) + "\n");
                    // postBodyString += (bodyToString(requestBody3) + "\n");
                    //              builder.addPart(oldBody);  //不能用这个方法，因为不知道oldBody的类型，可能是PartMap过来的，也可能是多个Part过来的，所以需要重新逐个加载进去
                    // builder.addPart(requestBody1);
                    // builder.addPart(requestBody2);
                    // builder.addPart(requestBody3);
                    newRequestBuild = oldRequest.newBuilder();
                    newRequestBuild.post(builder.build());
                    Log.e(TAG, "MultipartBody," + oldRequest.url());
                } else {
                    newRequestBuild = oldRequest.newBuilder();
                }
            } else {
                // 添加新的参数
                HttpUrl.Builder commonParamsUrlBuilder = oldRequest.url()
                        .newBuilder()
                        .scheme(oldRequest.url().scheme())
                        .host(oldRequest.url().host())
                        // .addQueryParameter("deviceOs", iCommon.DEVICE_OS)
                        // .addQueryParameter("appVersion", Utils.instance().getAppVersionName())
                        // .addQueryParameter("appName", Utils.instance().getAppNameNew())
                        ;
                newRequestBuild = oldRequest.newBuilder()
                        .method(oldRequest.method(), oldRequest.body())
                        .url(commonParamsUrlBuilder.build());
            }
            Request newRequest = newRequestBuild
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", "zh")
                    .build();

            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(newRequest);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            int httpStatus = response.code();
            StringBuilder logSB = new StringBuilder();
            logSB.append("-------start:" + method + "|");
            logSB.append(newRequest.toString() + "\n|");
            logSB.append(method.equalsIgnoreCase("POST") ? "post参数{" + postBodyString + "}\n|" : "");
            logSB.append("httpCode=" + httpStatus + ";Response:" + content + "\n|");
            logSB.append("----------End:" + duration + "毫秒----------");
            Log.d(TAG, logSB.toString());
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    protected String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static RequestBody mapToRequestBody(Object params) {
        String paramsStr = new Gson().toJson(params);
        // if (BasicApp.isDebug()) {
        // 	LogUtils.d("BasicNetwork-----" + paramsStr);
        // }
        return RequestBody.create(BasicNetwork.MEDIA_TYPE, paramsStr);
    }

    private void printLog(String msg) {
        LogUtils.i(TAG, "-----isDebug : " + BasicApp.isDebug());
        if (BasicApp.isDebug()) {
            LogUtils.i(TAG, msg);
        }
    }
}
