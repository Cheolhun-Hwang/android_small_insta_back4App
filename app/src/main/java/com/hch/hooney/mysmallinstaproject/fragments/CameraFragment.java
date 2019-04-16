package com.hch.hooney.mysmallinstaproject.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CameraFragment extends Fragment {
    private final String TAG = CameraFragment.class.getSimpleName();
    private final int SIGNAL_PERMISSION_CAMERA = 201;
    private final int SIGNAL_PERMISSION_GALLERY = 202;
    private final int SIGNAL_STORE_CAMERA = 301;
    private final int SIGNAL_STORE_GALLERY = 302;

    private Button gallery, camera;
    private ImageView showImage;

    private boolean isGallery, isCamera;
    private File photo; //Bitmap


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
        isGallery = false;
        isCamera = false;

        gallery = (Button) view.findViewById(R.id.camera_gallery_btn);
        camera = (Button) view.findViewById(R.id.camera_camera_btn);
        showImage = (ImageView) view.findViewById(R.id.camera_show_image);

        setEvents();
    }

    private void setEvents() {
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGallery){
                    isGallery = true;
                    getImage(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, SIGNAL_PERMISSION_GALLERY);

                    isGallery = false;
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCamera){
                    isCamera = true;
                    //new String[]{Manifest.permission.CAMERA}
                    getImage(new String[]{Manifest.permission.CAMERA}, SIGNAL_PERMISSION_CAMERA);
                    isCamera = false;
                }
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
//                boolean isAll = true;
//                for(int permit : grantResults){
//                    if(permit == PackageManager.PERMISSION_DENIED){
//                        isAll = false;
//                        break;
//                    }
//                }
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
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SIGNAL_STORE_GALLERY:
                if(resultCode == Activity.RESULT_OK){
                    Uri photoUri = data.getData();

                    if(startCursor(photoUri)){
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
//                    Uri photoUri = data.getData();
//
//                    if(startCursor(photoUri)){
//                        showImage.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
//                    }else{
//                        Toast.makeText(getContext(), "이미지를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
        }
    }

    private boolean startCursor(Uri uri) {
        Cursor cursor = null;
        boolean isOk = true;
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            assert uri != null;
            cursor = getContext().getContentResolver().query(uri, proj, null, null, null);

            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            photo = new File(cursor.getString(column_index));
        }catch (Exception e){
            e.printStackTrace();
            isOk = false;
        }finally {
            if(cursor != null){
                cursor.close();
            }
            return isOk;
        }
    }

    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }
}
