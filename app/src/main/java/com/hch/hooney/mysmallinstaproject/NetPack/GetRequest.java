package com.hch.hooney.mysmallinstaproject.NetPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRequest {
    public String send(String link, String key){
        try{
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("appKey", key);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // read the output from the server
            BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder=new StringBuilder();

            String line = null;
            while ((line=reader.readLine()) !=null){
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
