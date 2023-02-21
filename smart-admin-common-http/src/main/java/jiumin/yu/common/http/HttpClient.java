package jiumin.yu.common.http;

import okhttp3.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Yujiumin
 * @date 2022/11/20
 */
public class HttpClient extends OkHttpClient {

    private static OkHttpClient okHttpClient;

    private static final long DEFAULT_READ_TIMEOUT_SECONDS = 3;
    private static final long DEFAULT_WRITE_TIMEOUT_SECONDS = 3;
    private static final long DEFAULT_CONNECT_TIMEOUT_SECONDS = 3;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * GET请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static HttpResponse get(String url, Map<String, ? extends Serializable> headers, Map<String, ? extends Serializable> params) {
        final HttpUrl.Builder httpUrlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((name, value) -> httpUrlBuilder.addQueryParameter(name, value.toString()));
        }

        final HttpUrl httpUrl = httpUrlBuilder.build();

        final Request.Builder requestBuilder = new Request.Builder();
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((name, value) -> requestBuilder.addHeader(name, value.toString()));
        }

        final Request request = requestBuilder.url(httpUrl).build();
        return syncExecute(request);
    }

    /**
     * POST请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static HttpResponse post(String url, Map<String, ? extends Serializable> headers, String body) {
        Request.Builder requestBuilder = new Request.Builder();

        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((name, value) -> requestBuilder.addHeader(name, value.toString()));
        }

        final Request request = requestBuilder.url(Objects.requireNonNull(HttpUrl.parse(url)))
                .post(RequestBody.create(MediaType.parse("application/json?charset=utf-8"), body))
                .build();
        return syncExecute(request);
    }

    /**
     * 同步执行请求
     *
     * @param request
     * @return
     */
    public static HttpResponse syncExecute(Request request) {
        HttpResponse httpResponse = new HttpResponse();
        try {
            final Call call = okHttpClient.newCall(request);
            final Response response = call.execute();
            response.headers().names().forEach(name -> httpResponse.putHeader(name, response.header(name)));
            httpResponse.setResponseCode(response.code());
            httpResponse.setSuccessful(response.isSuccessful());
            if (response.isSuccessful()) {
                final ResponseBody responseBody = response.body();
                if (Objects.nonNull(responseBody)) {
                    final String responseBodyString = responseBody.string();
                    httpResponse.setBody(responseBodyString);
                }
            } else {
                final String message = response.message();
                httpResponse.setMessage(message);
            }
        } catch (IOException e) {
            httpResponse.setSuccessful(false);
            httpResponse.setMessage("请求异常: " + e.getMessage());
        }
        return httpResponse;
    }
}
