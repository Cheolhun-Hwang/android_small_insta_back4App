package com.hch.hooney.mysmallinstaproject.fragments;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hch.hooney.mysmallinstaproject.R;
import com.hch.hooney.mysmallinstaproject.Utils.PermissionPack;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private final String TAG = FavoriteFragment.class.getSimpleName();
    private final int SIGNAL_PERMISSION_GALLERY = 401;
    private final int SIGNAL_PERMISSION_CAMERA = 402;

    private ImageView imageView;
    private Button gellary, camera;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_camera, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        imageView = (ImageView) view.findViewById(R.id.camera_show_image);
        gellary = (Button) view.findViewById(R.id.camera_gallery_btn);
        gellary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(SIGNAL_PERMISSION_GALLERY, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                });
            }
        });
        camera = (Button) view.findViewById(R.id.camera_camera_btn);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(SIGNAL_PERMISSION_CAMERA, new String[]{
                        Manifest.permission.CAMERA
                });
            }
        });
    }

    private void getImage(int signal, String[] ps) {
        boolean ispm = new PermissionPack().checkPermission(getContext(), ps);
        if(ispm){
            //To Do
            if(signal == SIGNAL_PERMISSION_GALLERY){
                //G
            }else{
                //C
            }
        }else{
            //Commit Permission
            new PermissionPack().commitPermission(FavoriteFragment.this, signal, ps);
        }
    }

}
