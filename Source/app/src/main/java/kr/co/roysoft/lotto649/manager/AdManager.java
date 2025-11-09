package kr.co.roysoft.lotto649.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowMetrics;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdManager {
    private static AdManager instance = new AdManager();

    private AdManager() {

    }

    public static AdManager getInstance() {
        return instance;
    }

    private AdSize getAdSize(Activity activity){
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int adWidthPixels = displayMetrics.widthPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            adWidthPixels = windowMetrics.getBounds().width();
        }

        float density = displayMetrics.density;
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public AdView createAdView(Activity activity, String adUnitId){
        AdView adView = new AdView(activity);
        adView.setAdUnitId(adUnitId);
        adView.setAdSize(getAdSize(activity));
        return adView;
    }
}
