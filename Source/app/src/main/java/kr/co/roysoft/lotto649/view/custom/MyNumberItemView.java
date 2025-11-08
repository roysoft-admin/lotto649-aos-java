package kr.co.roysoft.lotto649.view.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.HashMap;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.manager.AdManager;
import kr.co.roysoft.lotto649.manager.DataManager;
import kr.co.roysoft.lotto649.manager.NetworkListener;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.manager.StateManager;
import kr.co.roysoft.lotto649.view.chip.NormalChip;

public class MyNumberItemView extends FrameLayout {

    private Context mContext;

    private TextView textDate;
    private TextView textLottoCount;
    private TextView textIng;
    private NormalChip chipType;
    private View frameResult;
    private TextView textResult;
    private NumberViewGroup numberItem;

    private View btnMore;

    private NumberDTO number;
    private FrameLayout frameAd;

    public MyNumberItemView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public MyNumberItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public MyNumberItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public MyNumberItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_my_number, null);
        addView(view);

        textDate = view.findViewById(R.id.text_date);
        textLottoCount = view.findViewById(R.id.text_lotto_count);
        textIng = view.findViewById(R.id.text_ing);
        frameResult = view.findViewById(R.id.frame_result);
        textResult = view.findViewById(R.id.text_result);
        textResult.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter_bold));
        textResult.setTextColor(getResources().getColor(R.color.main_black, null));
        numberItem = view.findViewById(R.id.number_item);
        chipType = view.findViewById(R.id.chip_type);
        frameAd = view.findViewById(R.id.frame_ad);
        btnMore = view.findViewById(R.id.btn_more);
    }

    public void setNumber(NumberDTO number) {
        this.number = number;
        settingData();
    }

    private void settingData(){
        textDate.setText(number.getCreatedAtString());
        if(number.date != null && !number.date.equals("")) {
            textLottoCount.setText(number.date);
        }
        textIng.setVisibility(View.GONE);
        frameResult.setVisibility(View.VISIBLE);
        if(number.result == 0){
            textIng.setVisibility(View.VISIBLE);
            frameResult.setVisibility(View.GONE);
        }
        else{
            textResult.setText(number.getResult());
        }
        
        chipType.setSelected(number.input_type == Enums.INPUT_TYPE_AUTO);
        if(number.input_type == Enums.INPUT_TYPE_AUTO){
            chipType.setText("QUICK PICK");
        }
        else{
            chipType.setText("INPUT");
        }
        numberItem.setNumbersBlue(number.n1, number.n2, number.n3, number.n4, number.n5, number.n6);
    }

    public void setMore(){
        if(btnMore != null){
            btnMore.setVisibility(View.VISIBLE);
            btnMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Alert")
                            .setMessage("Do you want to delete the entered number?")
                            .setPositiveButton("YES", (dialog, which) -> {
                                NetworkManager.getInstance().request((NetworkListener) mContext, NetworkManager.REQUEST_DELETE_NUMBERS_NUMBER_ID, new HashMap<String, String>(){{
                                    put("number_id", number.id + "");
                                }}, NetworkManager.PARAM_TYPE_BODY);
                                dialog.dismiss();
                            })
                            .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                            .show();
                }
            });
        }
    }

    public void setAd(boolean isVisible){
        if(frameAd == null) return;
        if(isVisible){
            frameAd.setVisibility(View.VISIBLE);
            String adUnitId = getResources().getString(R.string.ad_unit_test_banner);
            if(!DataManager.getInstance().isDev) adUnitId = getResources().getString(R.string.ad_unit_my_number_banner);
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
