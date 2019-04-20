package com.hch.hooney.mysmallinstaproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hch.hooney.mysmallinstaproject.ListPack.AutoSearch.SearchAdapter;
import com.hch.hooney.mysmallinstaproject.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditText searchEdit;
    private SearchAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);
        loadData();
        return view;
    }

    private void loadData() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(getContext(),
                            "데이터를 가져올 수 없습니다.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    for (ParseUser item : objects){
                        Log.d("Search", "name : " + item.getString("username"));
                    }
                    adapter = new SearchAdapter(getContext(), objects);
                    recyclerView.setAdapter(adapter);
                    setEvent();
                }
            }
        });
    }

    private void setEvent() {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = searchEdit.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(search);
            }
        });
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.search_frame_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        searchEdit = view.findViewById(R.id.search_frame_edit_text);
    }

}
