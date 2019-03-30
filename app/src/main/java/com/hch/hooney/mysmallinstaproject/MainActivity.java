package com.hch.hooney.mysmallinstaproject;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hch.hooney.mysmallinstaproject.fragments.AccountFragment;
import com.hch.hooney.mysmallinstaproject.fragments.CameraFragment;
import com.hch.hooney.mysmallinstaproject.fragments.FavoriteFragment;
import com.hch.hooney.mysmallinstaproject.fragments.HomeFragment;
import com.hch.hooney.mysmallinstaproject.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private final int SIG_CAMERA = 1001;

    enum Tabs{
        home, search, camera, favorite, account
    }

    private ImageButton tab_home, tab_search, tab_camera, tab_favorite, tab_account;
    private Tabs nowTab;

    private Fragment home, search, camera, favorite, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        switchFragments(nowTab);
    }

    private void init(){
        nowTab = Tabs.home;

        home = new HomeFragment();
        search = new SearchFragment();
        camera = new CameraFragment();
        favorite = new FavoriteFragment();
        account = new AccountFragment();

        tab_home = (ImageButton) findViewById(R.id.tab_home);
        tab_search = (ImageButton) findViewById(R.id.tab_search);
        tab_camera = (ImageButton) findViewById(R.id.tab_camera);
        tab_favorite = (ImageButton) findViewById(R.id.tab_favorite);
        tab_account = (ImageButton) findViewById(R.id.tab_user);

        setEvents();
    }

    private void setEvents(){
        tab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowTab != Tabs.home){
                    switchFragments(Tabs.home);
                }
            }
        });
        tab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowTab != Tabs.search){
                    switchFragments(Tabs.search);
                }
            }
        });
        tab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowTab != Tabs.camera){
                    switchFragments(Tabs.camera);
                }
            }
        });
        tab_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowTab != Tabs.favorite){
                    switchFragments(Tabs.favorite);
                }
            }
        });
        tab_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowTab != Tabs.account){
                    switchFragments(Tabs.account);
                }
            }
        });
    }

    private void switchFragments(Tabs tab){
        focusTab(tab);
        switch (tab){
            case home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, home).commit();
                break;
            case search:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, search).commit();
                break;
            case camera:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, camera).commit();
                break;
            case favorite:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, favorite).commit();
                break;
            case account:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, account).commit();
                break;
        }
        nowTab = tab;
    }

    private void focusTab(Tabs tab){
        switch (tab){
            case home:
                tab_home.setColorFilter(getResources().getColor(R.color.black));
                tab_search.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_camera.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_favorite.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_account.setColorFilter(getResources().getColor(R.color.grey_400));
                break;
            case search:
                tab_home.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_search.setColorFilter(getResources().getColor(R.color.black));
                tab_camera.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_favorite.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_account.setColorFilter(getResources().getColor(R.color.grey_400));
                break;
            case camera:
                tab_home.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_search.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_camera.setColorFilter(getResources().getColor(R.color.black));
                tab_favorite.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_account.setColorFilter(getResources().getColor(R.color.grey_400));
                break;
            case favorite:
                tab_home.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_search.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_camera.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_favorite.setColorFilter(getResources().getColor(R.color.black));
                tab_account.setColorFilter(getResources().getColor(R.color.grey_400));
                break;
            case account:
                tab_home.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_search.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_camera.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_favorite.setColorFilter(getResources().getColor(R.color.grey_400));
                tab_account.setColorFilter(getResources().getColor(R.color.black));
                break;
        }
    }

}
