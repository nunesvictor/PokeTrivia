package br.edu.ifto.pdmii.avaliacao02.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    public String get(String requestUrl) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(getRequestInputStream(requestUrl));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String responseText;

        while ((responseText = bufferedReader.readLine()) != null) {
            stringBuilder.append(responseText).append("\n");
        }

        return stringBuilder.toString();
    }

    private InputStream getRequestInputStream(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return new BufferedInputStream(connection.getInputStream());
    }
}