package com.hch.hooney.mysmallinstaproject.MyApp;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.hch.hooney.mysmallinstaproject.R;
import com.parse.Parse;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;

public class MyApplication extends Application {
    public static String userName;
    public static ParseFile userAva;

    @Override
    public void onCreate() {
        super.onCreate();

        //SDK Init
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("CHiF4GUlnzFgl3RmAUJeVOyOTQX1Ui4hBWGkkGRy")
                .clientKey("TCbkL4gDRLirY5ayeD5Ii6WeAwmpQaSNi9kl3GcU")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

//    public static void login(){
//        userName = "Temp User";
////        userAva = null;
//        userAva = new ParseFile("guest_ava_photo.jpeg", BitmapFactory.decodeResource());
////    }
//
//    public static byte[] BitmapToByteArray( Bitmap bitmap ) {
//
//        Bitmap bitmap = ((BitmapFactory)drawable).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
//        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
//        byte[] byteArray = stream.toByteArray() ;
//        return byteArray ;
//    }
}
