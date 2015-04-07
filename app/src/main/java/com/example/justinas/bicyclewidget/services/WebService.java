package com.example.justinas.bicyclewidget.services;


import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class WebService{

    public String getString(String urlString) throws IOException {
        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet(urlString);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String response = "";
        try {
            response = client.execute(request, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
