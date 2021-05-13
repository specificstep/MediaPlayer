package com.cute.mediaplayer;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.InterstitialAd;
import com.firebase.client.Firebase;

import java.util.ArrayList;

import static com.cute.mediaplayer.LoadInterstitialAd.LoadInterstitialAds.showinterstitialAd;

public class MainActivity extends AppCompatActivity {
    Tracker mTracker;
    String intVar;
    int adCount = 2;
    int currentCount = 1;
    ProgressDialog progressDialog;
    private static final int REQUEST_CODE_PERMISSON = 123 ;
    Boolean showAds = false;
    InterstitialAd interstitialAd = new InterstitialAd(this);
    BottomNavigationView bottomView;
    static ArrayList<VideoFiles> videoFiles = new ArrayList<>();
    static ArrayList<String> folderList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        MainActivity.super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        FirebaseInit("https://cute-5b4b3-default-rtdb.firebaseio.com/interstitial");
        AdRequest adRequest = new AdRequest.Builder().build();
        showinterstitialAd(MainActivity.this, adRequest);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        bottomView = findViewById(R.id.bottom_nav);
        permission();
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                currentCount++;
                switch (menuItem.getItemId())
                {
                    case R.id.folders_list:
                        if(currentCount == adCount) {
                            currentCount = 0;
                            if (showAds) {
                                showLoadingDialog();
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (showAds && interstitialAd.isLoaded()) {
                                        interstitialAd.show();
                                        dismissLoadingDialog();
                                        menuItem.setChecked(true);
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                                        fragmentTransaction.commit();
                                    } else {
                                        dismissLoadingDialog();
                                        menuItem.setChecked(true);
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                                        fragmentTransaction.commit();
                                    }
                                }
                            }, 1800);
                        }
                        else{
                            menuItem.setChecked(true);
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.mainFragment, new FolderFragment());
                            fragmentTransaction.commit();
                        }
                        break;

                    case R.id.files_list:
                        if(currentCount == adCount) {
                            currentCount = 0;
                            if (showAds) {
                                showLoadingDialog();
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (showAds && interstitialAd.isLoaded()) {
                                        interstitialAd.show();
                                        dismissLoadingDialog();
                                        menuItem.setChecked(true);
                                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction2.replace(R.id.mainFragment, new FileFragment());
                                        fragmentTransaction2.commit();
                                    } else {
                                        dismissLoadingDialog();
                                        menuItem.setChecked(true);
                                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction2.replace(R.id.mainFragment, new FileFragment());
                                        fragmentTransaction2.commit();
                                    }
                                }
                            }, 1800);
                        }else{
                            menuItem.setChecked(true);
                            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction2.replace(R.id.mainFragment, new FileFragment());
                            fragmentTransaction2.commit();
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
    public void dismissLoadingDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.dismiss();
        }
    }
    public void FirebaseInit(String url){
        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase(url);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                intVar = dataSnapshot.getValue(String.class);
                if(intVar != null){
                    showAds = true;
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                    interstitialAd.setAdUnitId(intVar);
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = "Main Screen";
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_PERMISSON);
        }
        else{
            videoFiles = getAllVideo(this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment,new FolderFragment());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSON)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                videoFiles = getAllVideo(this);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment,new FolderFragment());
                fragmentTransaction.commit();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_PERMISSON);
            }
        }
    }

    public static ArrayList<VideoFiles> getAllVideo(Context context) {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null,null,null);
        if (cursor != null){
            while (cursor.moveToNext())
            {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);

                VideoFiles videoFiles = new VideoFiles(id,path,title,fileName,size,dateAdded,duration);
                Log.e("Path",path);
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0,slashFirstIndex);

                if (!folderList.contains(subString))
                {
                    folderList.add(subString);
                }
                tempVideoFiles.add(videoFiles);
            }
            cursor.close();
        }
        return tempVideoFiles;
    }
}