package se.rejjd.taskmanager.http;

import java.io.ByteArrayInputStream;
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

    private HttpResponse doRequest(String url, String requestType, String body) {
        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            if(body != null) {
                connection.setDoOutput(true);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
                writeToOutputStream(inputStream, connection.getOutputStream());
            }

            InputStream in = connection.getInputStream();
            return getAsHttpResponse(in, connection);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    private HttpResponse getAsHttpResponse(InputStream inputStream, HttpURLConnection connection) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeToOutputStream(inputStream, outputStream);
        final int statusCode = connection.getResponseCode();
        final String responseMessage = connection.getResponseMessage();
        final Map<String, List<String>> headers = connection.getHeaderFields();
        final byte[] response = outputStream.toByteArray();

        outputStream.close();

        return new HttpResponse(responseMessage, statusCode, headers, response);
    }

    private void writeToOutputStream(InputStream is, OutputStream os) throws IOException {
        int bytesRead;
        byte[] buffer = new byte[1024];

        try {
            while((bytesRead = is.read(buffer)) > 0) {
                os.write(buffer, 0, bytesRead);
            }
        } finally {
            os.close();
        }
    }

}