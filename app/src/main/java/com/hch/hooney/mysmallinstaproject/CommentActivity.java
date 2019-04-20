package com.hch.hooney.mysmallinstaproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hch.hooney.mysmallinstaproject.ListPack.Comments.CommentsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CommentActivity extends AppCompatActivity {
    /*
    intent.putExtra("post_id", object.getObjectId());
                    intent.putExtra("post_body", hold.bodyMessage.getText());
                    intent.putExtra("post_username", object.getString("username"));
                    intent.putExtra("post_ava_url", object.getParseFile("ava").getUrl());
                    intent.putExtra("post_uuid", object.getString("uuid"));
     */
    private final String TAG = CommentActivity.class.getSimpleName();

    private String post_id, post_msg, post_username, uuid;

    private RecyclerView recyclerView;
    private EditText editText;
    private Button button;
    private boolean isSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        post_id = getIntent().getStringExtra("post_id");
        if(post_id == null){
            Toast.makeText(getApplicationContext(), "잘못된 요청입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            post_msg = getIntent().getStringExtra("post_body");
            post_username = getIntent().getStringExtra("post_username");
            uuid = (getIntent().getStringExtra("post_uuid"));
            setBar(getSupportActionBar());

            init();
            getCommentsData();
        }
    }

    private void init() {
        isSend = false;
        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        editText = (EditText) findViewById(R.id.comment_edit);
        button = (Button) findViewById(R.id.comment_send_btn);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSend){
                    isSend = true;

                    ParseObject entity = new ParseObject("comments");
                    entity.put("username", "Temp User");
                    entity.put("to", uuid);
                    entity.put("ava", new ParseFile("guest_ava_img.jpg", drawableToByteArrayAVA()));
                    entity.put("comment", editText.getText().toString());
                    editText.setText("");
                    entity.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Toast.makeText(getApplicationContext(), "제대로 작동하지 못했습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                getCommentsData();
                            }
                            isSend = false;
                        }
                    });
                }
            }
        });
    }

    private byte[] drawableToByteArrayAVA( ) {
        Bitmap bitmap = null;
        Drawable drawable = getResources().getDrawable(R.drawable.no_user, null);
        if(drawable instanceof BitmapDrawable){
            bitmap = ((BitmapDrawable)drawable).getBitmap();
        }else{
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    private void getCommentsData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("comments");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ArrayList<ParseObject> tempList = new ArrayList<>();

                for(ParseObject object : objects){
                    if(object.getString("to").equals(uuid)){
                        tempList.add(object);
                    }
                }

                recyclerView.setAdapter(new CommentsAdapter(tempList, getApplicationContext()));
                recyclerView.scrollToPosition(0);
            }
        });
    }

    private void setBar(ActionBar actionBar) {
        actionBar.setTitle("댓글");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


}
