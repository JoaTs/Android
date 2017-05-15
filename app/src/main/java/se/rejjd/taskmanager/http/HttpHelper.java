package se.rejjd.taskmanager.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HttpHelper {

    public static String get(String url) {
        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            writeToOutput(inputStream, outputStream);

            return new String(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    private static void writeToOutput(InputStream inputStream, ByteArrayOutputStream outputStream) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[1024];

        while ((bytesRead = inputStream.read()) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }
}