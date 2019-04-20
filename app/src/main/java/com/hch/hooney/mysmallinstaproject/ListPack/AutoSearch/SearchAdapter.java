package com.hch.hooney.mysmallinstaproject.ListPack.AutoSearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hch.hooney.mysmallinstaproject.R;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {
    private Context context;
    private List<ParseUser> list;
    private ArrayList<ParseUser> searchList;

    public SearchAdapter(Context context, List<ParseUser> list) {
        this.context = context;
        this.list = list;
        searchList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search, viewGroup, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder searchHolder, int i) {
        ParseObject item = searchList.get(i);
        searchHolder.user_name.setText(((ParseUser) item).getUsername());

        ParseFile pic_url = item.getParseFile("ava");
        if(pic_url != null){
            //존재!
            Glide.with(context).load(pic_url.getUrl()).into(searchHolder.user_icon);
        }else{
            searchHolder.user_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.no_user, null));
        }

        searchHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Detail Info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    class SearchHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        ImageView user_icon;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            user_icon = itemView.findViewById(R.id.item_search_image);
            user_name = itemView.findViewById(R.id.item_search_user_name);
        }
    }

    public void filter(String search){
        search = search.toLowerCase(Locale.getDefault());
        searchList.clear();
        if(search.length() == 0){
            searchList.addAll(list);
        }else{
            for(ParseUser item : list){
                String s_name = item.getUsername();
                if(s_name.toLowerCase().contains(search)){
                    searchList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
