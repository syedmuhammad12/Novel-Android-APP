package com.ishtiaq.tehkhanaykaraaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kidoz.sdk.api.KidozInterstitial;
import com.kidoz.sdk.api.KidozSDK;
import com.kidoz.sdk.api.interfaces.SDKEventListener;

import com.tapdaq.sdk.STATUS;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;
import com.tapdaq.sdk.model.rewards.TDReward;

import java.util.Locale;


public class MainNovel extends AppCompatActivity {

    PDFView pdfView;
    private BroadcastReceiver receiver;
    private String mAppId = "6235c17708fe6c2d735d7ad3";
    private String mClientKey = "da9c738f-833d-4a3f-9d0a-68ef65bd01da";
    private String mPlacementTag_interstitial = "interstitial";
    private String  mPlacementTag_banner= "interstitial";
    private String  mPlacementTag_rewarded= "interstitial";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_novel);


        // Tapdaq config interstitial
        TapdaqConfig config = Tapdaq.getInstance().config();

        config.setUserSubjectToGdprStatus(STATUS.TRUE); //GDPR declare if user is in EU
        config.setConsentStatus(STATUS.TRUE); //GDPR consent must be obtained from the user
        config.setAgeRestrictedUserStatus(STATUS.FALSE); //Is user subject to COPPA or GDPR age restrictions

        Tapdaq.getInstance().initialize(this, mAppId, mClientKey, config, new TapdaqInitListener());
        TapdaqConfig configure = new TapdaqConfig();
        configure.setAutoReloadAds(true);
        // **********************************************************



        // Tapdaq banner initializer

        TMBannerAdView ad = (TMBannerAdView) findViewById(R.id.adBanner);
        ad.load (this, mPlacementTag_banner, TMBannerAdSizes.STANDARD, new BannerListener());

        // **************************************************************


        // Main content of screen

        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("Teh Khany Ka Razz.pdf").load();
        // *******************************************************************


        //Broadcasting ads

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Tapdaq.getInstance().isInterstitialReady(MainNovel.this, mPlacementTag_interstitial)){
                    Tapdaq.getInstance().showInterstitial(MainNovel.this, mPlacementTag_interstitial, new InterstitialListener());}
            }
        };
        registerReceiver(receiver, filter);
        // ***************************************************************


    }

    // Tapdaq class for interstitial checker

    public class TapdaqInitListener extends TMInitListener {

        public void didInitialise() {
            super.didInitialise();
            // Ads may now be requested
            Tapdaq.getInstance().loadInterstitial(MainNovel.this,  mPlacementTag_interstitial, new InterstitialListener());
            Tapdaq.getInstance().loadRewardedVideo(MainNovel.this,  mPlacementTag_rewarded, new RewardedVideoListener());
        }

        @Override
        public void didFailToInitialise(TMAdError error) {
            super.didFailToInitialise(error);
            //Tapdaq failed to initialise
        }
    }
    // *************************************************

    //Interstitial ad listner for Tapdaq

    public class InterstitialListener extends TMAdListener {

        @Override
        public void didLoad() {
            // Ready to display the interstitial
            if (Tapdaq.getInstance().isInterstitialReady(MainNovel.this, mPlacementTag_interstitial)){
                Tapdaq.getInstance().showInterstitial(MainNovel.this, mPlacementTag_interstitial, new InterstitialListener());}
            }
        }
    // ******************************************************************

    //Banner ad listner for Tapdaq

    public class BannerListener extends TMAdListener {
        @Override
        public void didLoad() {
            // First banner loaded into view
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            // No banners available. View will stop refreshing
        }

        @Override
        public void didRefresh() {
            // Subequent banner loaded, this view will refresh every 30 seconds
        }
        @Override
        public void didFailToRefresh(TMAdError error) {
            // Banner could not load, this view will attempt another refresh every 30 seconds
        }

        @Override
        public void didClick() {
            // User clicked on banner
        }
    }

    // ******************************************************************

    // Rewarded ad listner for Tapdaq

    public class RewardedVideoListener extends TMAdListener {

        @Override
        public void didVerify(TDReward reward) {
            Log.i("MEDIATION-SAMPLE", String.format(Locale.ENGLISH, "didVerify: ID: %s, Tag: %s. Reward: %s. Value: %d. Valid: %b. Custom Json: %s", reward.getRewardId(), reward.getTag(), reward.getName(), reward.getValue(), reward.isValid(), reward.getCustom_json().toString()));
        }
    }





    // *****************************

    // Screen Functions

    // On back press
    @Override
    public void onBackPressed() {

        boolean isReady = Tapdaq.getInstance().isRewardedVideoReady(MainNovel.this, mPlacementTag_rewarded);

        if (isReady) {
            Tapdaq.getInstance().showRewardedVideo(MainNovel.this, mPlacementTag_rewarded, new RewardedVideoListener());
        }
        super.onBackPressed();
    }
    // On Destroy
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        TMBannerAdView ad = (TMBannerAdView) findViewById(R.id.adBanner);
        if (ad != null) {
            ad.destroy(MainNovel.this);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    // *************************************************************

}

