package kr.co.roysoft.lotto649.view.custom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Lotto649DTO;
import kr.co.roysoft.lotto649.manager.AdManager;
import kr.co.roysoft.lotto649.manager.DataManager;

public class LottoItemView extends FrameLayout {

    private Context mContext;

    private TextView textLottoCount;
    private TextView textWinamnt;
    private TextView textWinCount;
    private NumberViewGroup numberItem;
    private FrameLayout frameAd;

    private Lotto649DTO lotto;

    public LottoItemView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public LottoItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public LottoItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public LottoItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_lotto_history, null);
        addView(view);

        textLottoCount = view.findViewById(R.id.text_lotto_count);
        textWinamnt = view.findViewById(R.id.text_winamnt);
        textWinamnt.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter_bold));
        textWinamnt.setTextColor(getResources().getColor(R.color.main_black, null));
        textWinCount = view.findViewById(R.id.text_win_count);
        textWinCount.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter_bold));
        textWinCount.setTextColor(getResources().getColor(R.color.main_black, null));
        numberItem = view.findViewById(R.id.number_item);
        frameAd = view.findViewById(R.id.frame_ad);
    }

    public void setLotto(Lotto649DTO lotto) {
        this.lotto = lotto;
        settingData();
    }

    private void settingData(){
        textLottoCount.setText(lotto.getDateString());
        if(lotto.winners <= 0){
            textWinCount.setText("NONE");
        }
        else{
            textWinCount.setText(lotto.winners + "");
        }
        textWinamnt.setText(lotto.gold_jackpot + " | " + lotto.gold_number);

        numberItem.setLotto649Numbers(lotto.n1, lotto.n2, lotto.n3, lotto.n4, lotto.n5, lotto.n6, lotto.bb);
    }

    public void setAd(boolean isVisible){
        if(frameAd == null) return;
        if(isVisible){
            frameAd.setVisibility(View.VISIBLE);
            String adUnitId = getResources().getString(R.string.ad_unit_test_banner);
            if(!DataManager.getInstance().isDev) adUnitId = getResources().getString(R.string.ad_unit_lotto_banner);
            AdView adView = AdManager.getInstance().createAdView((Activity) mContext, adUnitId);
            frameAd.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    if(frameAd != null) frameAd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            });
        }
        else{
            frameAd.setVisibility(View.GONE);
            frameAd.removeAllViews();
        }
    }
}
