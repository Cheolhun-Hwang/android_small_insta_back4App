package com.hch.hooney.mysmallinstaproject.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hch.hooney.mysmallinstaproject.NetEasyPack.Get;
import com.hch.hooney.mysmallinstaproject.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private final String TAG = AccountFragment.class.getSimpleName();
    private final String WEATHER_URL = "https://api2.sktelecom.com/weather/current/minutely?lat=37.450744&lon=127.128803";

    private Thread weatherThread;
    private Handler mHandler;


    public AccountFragment() {
        // Required empty public constructor
        mHandler = initHandler();

    }

    private Handler initHandler(){
        return new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){

                }
                return true;
            }
        });
    }

    private Thread initWeatherThread(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                String res = null;
                try {
                    res = new Get(WEATHER_URL).sendToWeather(getString(R.string.weather_key));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, res);
            }
        });
    }

    private void startThread(){
        weatherThread = initWeatherThread();
        weatherThread.start();
    }

    private void stopThread(){
        if(weatherThread != null){
            if(weatherThread.isAlive()){
                weatherThread.interrupt();
            }
            weatherThread = null;
        }
    }

    @Override
    public void onDestroy() {
        stopThread();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        startThread();

        return view;
    }



}
