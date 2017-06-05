package se.rejjd.taskmanager.http;

import java.util.List;
import java.util.Map;

public final class HttpResponse {
    private final String responseMessage;
    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final byte[] response;

    public HttpResponse(String responseMessage, int statusCode, Map<String, List<String>> headers, byte[] response) {
        this.responseMessage = responseMessage;
        this.statusCode = statusCode;
        this.headers = headers;
        this.response = response;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public byte[] getRawResponse() {
        return response;
    }

    public String getResponseAsString() {
        return new String(response);
    }

}


