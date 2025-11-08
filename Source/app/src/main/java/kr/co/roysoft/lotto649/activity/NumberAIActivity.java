package kr.co.roysoft.lotto649.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.view.custom.MyNumberItemView;
import kr.co.roysoft.lotto649.view.custom.NumberViewGroup;

public class NumberAIActivity  extends BottomBaseAuthActivity {

    private TextView textLottoCount;
    private TextView textDrawingCount;

    private NumberViewGroup numberAi;

    private LinearLayout frameHistory;

    private ArrayList<int[]> listHistory = new ArrayList<>();

    private FrameLayout frameAd;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_ai);
        setupBottom();
        setBottomMenu(Enums.MENU_INPUT);
        initStatus();

        findViewById(R.id.btn_manual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.executeNumberManual();
            }
        });

        textLottoCount = findViewById(R.id.text_lotto_count);

        textDrawingCount = findViewById(R.id.text_drawing_count);

        numberAi = findViewById(R.id.number_ai);
        numberAi.setNumbersBlue(0, 0, 0, 0, 0, 0);
        numberAi.setSize(true);

        frameHistory = findViewById(R.id.frame_history);

        findViewById(R.id.btn_ai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAi();
            }
        });

        frameAd = findViewById(R.id.frame_ad);
        String adUnitId = getResources().getString(R.string.ad_unit_test_banner);
        if(!dataManager.isDev) adUnitId = getResources().getString(R.string.ad_unit_input_banner);
        AdView adView = adManager.createAdView(this, adUnitId);
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

    @Override
    protected void init() {
        super.init();
        // StateManager의 LOTTO_DATE 사용
        if(stateManager.LOTTO_DATE != null && !stateManager.LOTTO_DATE.equals("")) {
            textLottoCount.setText(stateManager.LOTTO_DATE);
        } else {
            textLottoCount.setText("Next Drawing");
        }
        textDrawingCount.setText(stateManager.USER.drawing_count + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(stateManager.USER == null) return;
        networkManager.request(this, NetworkManager.REQUEST_GET_USERS_USER_ID, new HashMap<String, String>(){{
            put("user_id", stateManager.USER.id + "");
        }}, NetworkManager.PARAM_TYPE_NORMAL);
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_POST_RECOMMEND_AI_NUMBER)){
            numberAi.setNumbersBlue(dataManager.number.n1,
                    dataManager.number.n2,
                    dataManager.number.n3,
                    dataManager.number.n4,
                    dataManager.number.n5,
                    dataManager.number.n6);
            textDrawingCount.setText(stateManager.USER.drawing_count + "");
        }
        else if(dto.type.equals(NetworkManager.REQUEST_PUT_USERS_USER_ID)){
            linkManager.executeToastMessage("Your available draw attempts have been added.");
            textDrawingCount.setText(stateManager.USER.drawing_count + "");
        }
        else if(dto.type.equals(NetworkManager.REQUEST_GET_USERS_USER_ID)){
            textDrawingCount.setText(stateManager.USER.drawing_count + "");
        }
        else {
            super.onComplete(dto);
        }
    }

    @Override
    public void onError(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_POST_RECOMMEND_AI_NUMBER) && dto.error.code == NetworkManager.ERROR_CODE_NO_MORE_COUNT){
            rewardPop();
        }
        else {
            super.onError(dto);
        }
    }

    private void postAi(){
        if(isNetworking) return;
        if(stateManager.USER.drawing_count == 0){
            rewardPop();
            return;
        }
        if(numberAi.isNumber()){
            listHistory.add(numberAi.getNumbers());
        }
        networkManager.request(this, NetworkManager.REQUEST_POST_RECOMMEND_AI_NUMBER, new HashMap<String, String>(){{
            put("user_id", stateManager.USER.id + "");
        }}, NetworkManager.PARAM_TYPE_BODY);

        refreshHistory();
    }

    private void rewardPop(){
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("No draw chances left.\nWould you like to watch an ad to recharge 5 chances?") // 메시지
                .setPositiveButton("Yes, I’ll watch the ad.", (dialog, which) -> {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    String adUnitId = getResources().getString(R.string.ad_unit_test_reward);
                    if(!dataManager.isDev) adUnitId = getResources().getString(R.string.ad_unit_ai_reward);
                    RewardedAd.load(this, adUnitId,
                            adRequest, new RewardedAdLoadCallback() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error.
                                    linkManager.executeToastMessage("Failed to load the ad.");
                                    rewardedAd = null;
                                }

                                @Override
                                public void onAdLoaded(@NonNull RewardedAd ad) {
                                    rewardedAd = ad;
                                    if (rewardedAd != null) {
                                        Activity activityContext = NumberAIActivity.this;
                                        rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                            @Override
                                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                                int rewardAmount = rewardItem.getAmount();
                                                networkManager.request(NumberAIActivity.this, NetworkManager.REQUEST_PUT_USERS_USER_ID, new HashMap<String, String>() {{
                                                    put("drawing_count", rewardAmount + "");
                                                    put("user_id", stateManager.USER.id + "");
                                                }}, NetworkManager.PARAM_TYPE_BODY);
                                            }
                                        });
                                    } else {
                                        linkManager.executeToastMessage("The ad is not ready.");
                                    }
                                }
                            });
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void refreshHistory(){
        frameHistory.removeAllViews();

        final int size = listHistory.size();
        for(int i = 0 ; i < size ; i++){
            int[] numbers = listHistory.get(size - (i + 1));
            NumberDTO number = new NumberDTO();
            number.n1 = numbers[0];
            number.n2 = numbers[1];
            number.n3 = numbers[2];
            number.n4 = numbers[3];
            number.n5 = numbers[4];
            number.n6 = numbers[5];
            number.created_at = new Date();
            if(stateManager.LOTTO_DATE != null && !stateManager.LOTTO_DATE.equals("")) {
                number.date = stateManager.LOTTO_DATE;
            }
            number.input_type = Enums.INPUT_TYPE_AUTO;
            MyNumberItemView view = new MyNumberItemView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            view.setNumber(number);
            frameHistory.addView(view);
        }
    }
}
