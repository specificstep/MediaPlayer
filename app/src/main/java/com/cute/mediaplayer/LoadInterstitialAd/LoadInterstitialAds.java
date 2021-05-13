package com.cute.mediaplayer.LoadInterstitialAd;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import androidx.annotation.NonNull;

import com.cute.mediaplayer.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class LoadInterstitialAds {

    public static void showinterstitialAd(Activity activity, AdRequest adRequest) {


        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, "/419163168/com.cute.mediaplayer.Interstitial", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                if (interstitialAd != null) {
                    interstitialAd.show(activity);
                    showDialog(activity);
                } else {
//                    Toast.makeText(activity, "Ad did not load", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

            }
        });

    }

     public static void showDialog(Activity activity){
         Dialog dialog = new Dialog( activity, android.R.style.Theme_Light);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setContentView(R.layout.gifdialog);
         dialog.show();
    }
}
