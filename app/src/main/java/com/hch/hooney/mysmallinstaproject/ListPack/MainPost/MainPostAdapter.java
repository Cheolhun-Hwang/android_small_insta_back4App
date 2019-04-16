package com.hch.hooney.mysmallinstaproject.ListPack.MainPost;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hch.hooney.mysmallinstaproject.CommentActivity;
import com.hch.hooney.mysmallinstaproject.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainPostAdapter extends RecyclerView.Adapter {
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    private final String TAG = MainPostAdapter.class.getSimpleName();
    private Context context;
    private List<ParseObject> list;
    private int lastPostion = -1;


    public MainPostAdapter(Context context, List<ParseObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_main_post, viewGroup, false);
        MainPostHolder holder = new MainPostHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final ParseObject object = list.get((list.size()-1) - position); //length-1-position
        if(object != null){
            final MainPostHolder hold = (MainPostHolder) viewHolder;

            hold.userName.setText(object.getString("username"));
            hold.bodyMessage.setText(
                    mDateFormat.format(
                            object.getCreatedAt())
                            +"\n"
                            +object.getString("title")
                            + ( object.getString("tags") == null ? "" : "\n\n" + object.getString("tags") ) );

            ParseQuery<ParseObject> like_query = ParseQuery.getQuery("likes");
            like_query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null){
                        int count = 0;
                        for(ParseObject like : objects){
                            if(like.getString("to").equals(object.getString("uuid"))){
                                count++;
                            }
                        }
                        hold.likeCount.setText(count+"");
                    }else{
                        hold.likeCount.setText(0+"");
                    }
                }
            });
            ParseQuery<ParseObject> comment_query = ParseQuery.getQuery("comments");
            comment_query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null){
                        int count = 0;
                        for(ParseObject com : objects){
                            if(com.getString("to").equals(object.getString("uuid"))){
                                count++;
                            }
                        }
                        hold.commentCount.setText(count+"");
                    }else{
                        hold.commentCount.setText(0+"");
                    }
                }
            });

            hold.commentCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("post_id", object.getObjectId());
                    intent.putExtra("post_body", hold.bodyMessage.getText());
                    intent.putExtra("post_username", object.getString("username"));
                    intent.putExtra("post_ava_url", object.getParseFile("ava").getUrl());
                    intent.putExtra("post_uuid", object.getString("uuid"));
                    context.startActivity(intent);
                }
            });

            hold.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, hold.menu);
                    popupMenu.getMenuInflater().inflate(R.menu.item_post_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.item_main_post_warning:
                                    Toast.makeText(context, "신고 : "+ position, Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });

            hold.likeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Like BTN index : "+ position, Toast.LENGTH_SHORT).show();
                }
            });
             /*
            Add Glide
             */
            ParseFile pic_url = object.getParseFile("pic");
            if(pic_url != null){
                //존재!
                Glide.with(context).load(pic_url.getUrl()).into(hold.mainImage);
            }else{
                hold.mainImage.setVisibility(View.GONE);
            }

            ParseFile ava_url = object.getParseFile("ava");
            if(ava_url != null){
                //존재!
                Glide.with(context).load(ava_url.getUrl()).into(hold.userImage);
            }else{

            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MainPostHolder extends RecyclerView.ViewHolder{
        public TextView userName, commentCount, likeCount, bodyMessage;
        public ImageButton likeBTN, menu;
        public ImageView mainImage, userImage;

        public MainPostHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.item_main_post_nickname);
            commentCount = (TextView) itemView.findViewById(R.id.item_main_post_comment_count);
            likeCount = (TextView) itemView.findViewById(R.id.item_main_post_like_count);
            likeBTN = (ImageButton) itemView.findViewById(R.id.item_main_post_like_btn);
            userImage = (ImageView) itemView.findViewById(R.id.item_main_post_ava);
            mainImage = (ImageView) itemView.findViewById(R.id.item_main_post_image);
            bodyMessage = (TextView) itemView.findViewById(R.id.item_main_post_text);
            menu = (ImageButton) itemView.findViewById(R.id.item_main_post_more_btn);
        }
    }
}
