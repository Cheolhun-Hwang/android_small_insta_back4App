package com.hch.hooney.mysmallinstaproject.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hch.hooney.mysmallinstaproject.ListPack.MainPost.MainPostAdapter;
import com.hch.hooney.mysmallinstaproject.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();
    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        readObject();
        return view;
    }

    private void readObject(){
        // Configure Query
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("posts");

        // Query Parameters
//        query.whereEqualTo("userId", ParseUser.getCurrentUser());

        // Sorts the results in ascending order by the itemName field
//        query.orderByAscending("itemName");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    Log.d(TAG, "Get .List");

                    recyclerView.setAdapter(new MainPostAdapter(getContext(), objects));
                }
            }
        });

//          // Get Detail Item to use object_ID
//        query.getInBackground("puYCtU1Dct", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if(e == null){
//                    Log.d(TAG, object.toString());
//                    Log.d(TAG, object.getObjectId());
//                    Log.d(TAG, object.get("title").toString());
//                }else{
//                    Log.d(TAG, "Get Post Data error....");
//                }
//            }
//        });
    }

}
