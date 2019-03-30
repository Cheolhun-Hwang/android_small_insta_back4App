package com.hch.hooney.mysmallinstaproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hch.hooney.mysmallinstaproject.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();


    public HomeFragment() {
        // Required empty public constructor
        readObject();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void readObject(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("posts");

        query.getInBackground("puYCtU1Dct", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null){
                    Log.d(TAG, object.toString());
                    Log.d(TAG, object.getObjectId());
                    Log.d(TAG, object.get("title").toString());
                }else{
                    Log.d(TAG, "Get Post Data error....");
                }
            }
        });
    }

}
