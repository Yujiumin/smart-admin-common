package jiumin.yu.common.http;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yujiumin
 * @date 2022/11/23
 */
public class HttpResponse {

    private boolean successful;

    private int responseCode;

    private String message;

    private Map<String, String> headerMap;

    private String body;

    public HttpResponse() {
        headerMap = new LinkedHashMap<>();
    }

    public void putHeader(String name, String value) {
        if (headerMap.containsKey(name)) {
            headerMap.replace(name, value);
        } else {
            headerMap.put(name, value);
        }
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
