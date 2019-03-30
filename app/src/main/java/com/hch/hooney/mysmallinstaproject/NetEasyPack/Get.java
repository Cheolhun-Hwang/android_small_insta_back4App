package com.hch.hooney.mysmallinstaproject.NetEasyPack;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Get {
    private final String TAG = "GET";
    private String url;
    private OkHttpClient client;

    public Get(String u){
        this.url = u;
        client = new OkHttpClient();
    }

    public String sendToWeather(String key) throws IOException {
        Request request = new Request.Builder()
                .url(this.url)
                .header("Accept", "application/json")
                .addHeader("appKey", key)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "GET Call back Code : "+response.isSuccessful());
        Log.d(TAG, "RES : " + response );
        return response.body().string();
    }
}
