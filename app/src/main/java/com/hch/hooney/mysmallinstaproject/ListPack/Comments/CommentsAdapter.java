package com.hch.hooney.mysmallinstaproject.ListPack.Comments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hch.hooney.mysmallinstaproject.R;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    private ArrayList<ParseObject> list;
    private Context context;

    public CommentsAdapter(ArrayList<ParseObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_comments, viewGroup, false);
        CommentsHolder holder = new CommentsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ParseObject item = list.get((list.size()-1) - position);
        if(item!= null){
            CommentsHolder hold = (CommentsHolder) viewHolder;
            hold.user_name.setText(item.getString("username"));

            ParseFile pic_ava = item.getParseFile("ava");
            if(pic_ava != null){
                Glide.with(context).load(pic_ava.getUrl()).into(hold.user_image);
            }else{
                hold.user_image.setImageDrawable(context.getResources().getDrawable(R.drawable.no_user, null));
            }

            hold.msg.setText(item.getString("comment"));
            hold.date.setText(format.format(item.getCreatedAt()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentsHolder extends RecyclerView.ViewHolder{
        public ImageView user_image;
        public TextView user_name, msg, date;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);

            user_image = (ImageView) itemView.findViewById(R.id.item_comment_user_icon);
            user_name = (TextView) itemView.findViewById(R.id.item_comment_user_name);
            date = (TextView) itemView.findViewById(R.id.item_comment_user_date);
            msg = (TextView) itemView.findViewById(R.id.item_comment_user_msg);
        }
    }
}
