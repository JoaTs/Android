package se.rejjd.taskmanager.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpHelper {

    public HttpResponse get(String url) {
        return doRequest(url, "GET", null);
    }

    public HttpResponse post(String url, String body) {
        return doRequest(url, "POST", body);
    }

    public HttpResponse put(String url, String body) {
        return doRequest(url, "PUT", body);
    }

    public HttpResponse delete(String url) {
        return doRequest(url, "DELETE", null);
    }


    public HttpResponse doRequest(String url, String requestTyp, String body) {
        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod(requestTyp);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            return getAsHttpResponse(connection);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    private HttpResponse getAsHttpResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeToOutputStream(inputStream, outputStream);

        final int statusCode = connection.getResponseCode();
        final String responseMessage = connection.getResponseMessage();
        final Map<String, List<String>> headers = connection.getHeaderFields();
        final byte[] response = outputStream.toByteArray();

        outputStream.close();

        return new HttpResponse(responseMessage, statusCode, headers, response);
    }

    private static void writeToOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[1024];

        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

}