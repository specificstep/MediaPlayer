package com.cute.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import pl.droidsonroids.gif.GifImageView;

public class LandingActivity extends AppCompatActivity {
    private static int splash_screen_timeout = 3000;
    String intVar;
    Boolean showAds = false;
    InterstitialAd interstitialAd ;
    ProgressDialog progressDialog;
    GifImageView gifview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        gifview=findViewById(R.id.gifview);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        FirebaseInit("https://cute-5b4b3-default-rtdb.firebaseio.com/interstitial");
        AdRequest adRequest = new AdRequest.Builder().build();
       InterstitialAd.load(LandingActivity.this, "/419163168/com.cute.mediaplayer.Interstitial", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                if (interstitialAd != null) {
                    interstitialAd.show(LandingActivity.this);
                    gifview.setVisibility(View.VISIBLE);
                } else {
                    gifview.setVisibility(View.GONE);

                }

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showAds) {
                    showLoadingDialog();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent splash_intent = new Intent(LandingActivity.this, LaunchScreen.class);
                        startActivity(splash_intent);
                        finish();
//                        if (showAds && interstitialAd.isLoaded()) {
//                            interstitialAd.show();
//                        }
                        dismissLoadingDialog();
                    }
                }, 6000);
            }
        },splash_screen_timeout);
    }
    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
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
//    public void FirebaseInit(String url){
//        Firebase.setAndroidContext(this);
//        Firebase firebase = new Firebase(url);
//        firebase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                intVar = dataSnapshot.getValue(String.class);
//                if(intVar != null){
//                    Log.i("msg", "-----------------------------------------------------------------------------------");
//                    showAds = true;
//                    interstitialAd.setAdUnitId(intVar);
//                    interstitialAd.loadAd(new AdRequest.Builder().build());
//                    interstitialAd.setAdListener(new AdListener() {
//                        @Override
//                        public void onAdClosed() {
//                            interstitialAd.loadAd(new AdRequest.Builder().build());
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//            }
//        });
//    }
}
