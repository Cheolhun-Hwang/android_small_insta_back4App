package com.hch.hooney.mysmallinstaproject.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hch.hooney.mysmallinstaproject.R;
import com.hch.hooney.mysmallinstaproject.Utils.PermissionPack;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CameraFragment extends Fragment {
    private final String TAG = CameraFragment.class.getSimpleName();
    private final int SIGNAL_PERMISSION_CAMERA = 201;
    private final int SIGNAL_PERMISSION_GALLERY = 202;
    private final int SIGNAL_STORE_CAMERA = 301;
    private final int SIGNAL_STORE_GALLERY = 302;

    private Button gallery, camera, sendPost;
    private ImageView showImage;
    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        gallery = (Button) view.findViewById(R.id.camera_gallery_btn);
        camera = (Button) view.findViewById(R.id.camera_camera_btn);
        showImage = (ImageView) view.findViewById(R.id.camera_show_image);
        sendPost = (Button) view.findViewById(R.id.send_post);

        setEvents();
    }

    private void setEvents() {
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, SIGNAL_PERMISSION_GALLERY);

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new String[]{Manifest.permission.CAMERA}
                getImage(new String[]{Manifest.permission.CAMERA}, SIGNAL_PERMISSION_CAMERA);

            }
        });

        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject entity = new ParseObject("posts");
                entity.put("username", "Temp User");
                entity.put("title", "테스트해보기 ㅎ");
                entity.put("ava", new ParseFile("guest_ava_img.jpg", drawableToByteArrayAVA()));
                entity.put("pic", new ParseFile("post_img.jpg", bitmapToByteArray()));
                entity.put("uuid", "temp_user_01554");
                entity.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Toast.makeText(getContext(), "제대로 작동하지 못했습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void getImage(String[] ps, int SIGNAL){
        boolean isPermission = new PermissionPack().checkPermission(getContext(), ps);
        if(isPermission == true){
            if(SIGNAL == SIGNAL_PERMISSION_CAMERA){
                loadCamera();
            }else{
                loadGallery();
            }
        }else{
            new PermissionPack().commitPermission(CameraFragment.this,
                    SIGNAL,
                    ps);
        }
    }

    private void loadGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, SIGNAL_STORE_GALLERY);
    }

    private void loadCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), SIGNAL_STORE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case SIGNAL_PERMISSION_GALLERY:
                if(grantsAll(grantResults)){

                }else{
                    //Notify Get All Permission
                    Toast.makeText(getContext(), "권한이 반드시 필요한 기능입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case SIGNAL_PERMISSION_CAMERA:
                if(grantsAll(grantResults)){

                }else{
                    //Notify Get All Permission
                    Toast.makeText(getContext(), "권한이 반드시 필요한 기능입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean grantsAll(int[] grants){
        for(int permit : grants){
            if(permit == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SIGNAL_STORE_GALLERY:
                if(resultCode == Activity.RESULT_OK){
                    Uri photoUri = data.getData();
                    File photo = startCursor(photoUri);

                    if(photo != null){
                        showImage.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
                    }else{
                        Toast.makeText(getContext(), "이미지를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case SIGNAL_STORE_CAMERA:
                if(resultCode == Activity.RESULT_OK){
                    Log.i(TAG, "Signal Camera is OK");
                    Bitmap pt = (Bitmap) data.getExtras().get("data");

                    if (pt != null){
                        showImage.setImageBitmap(pt);
                    }else{
                        Log.i(TAG, "Image Bitmap is Null...");
                    }
                }
                break;
        }
    }

    private File startCursor(Uri uri) {
        Cursor cursor = null;
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            assert uri != null;
            cursor = getContext().getContentResolver().query(uri, proj, null, null, null);

            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return new File(cursor.getString(column_index));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private byte[] bitmapToByteArray( ) {
        Bitmap bitmap = null;
        Drawable drawable = showImage.getDrawable();
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
}
