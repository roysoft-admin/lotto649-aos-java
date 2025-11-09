package kr.co.roysoft.lotto649.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.data.Lotto649DTO;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.view.custom.LottoItemView;
import kr.co.roysoft.lotto649.view.custom.MainAiNumberItemView;
import kr.co.roysoft.lotto649.view.custom.MyNumberItemView;

public class MainActivity extends BottomBaseAuthActivity {

    private TextView textNextPowerballDate;
    private TextView textNextPowerballJackpot;
    private TextView textUserName;
    private TextView textUserNameSuffix;
    private TextView textWelcome;

    private View btnMyNumberMore;
    private LinearLayout frameMyNumber;
    private TextView textMyNumberEmpty;

    private View btnAiNumber;
    private MainAiNumberItemView itemAiNumber1;
    private MainAiNumberItemView itemAiNumber2;
    private MainAiNumberItemView itemAiNumber3;
    private MainAiNumberItemView itemAiNumber4;
    private TextView textNoResult;

    private View btnLottoMore;
    private LinearLayout frameLotto;

    private ArrayList<NumberDTO> aiNumbers;
    private int position = 0;
    private Timer timerAi;

    private FrameLayout frameAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottom();
        setBottomMenu(Enums.MENU_HOME);
        initStatus();

        textNextPowerballDate = findViewById(R.id.text_lotto_count);
        textNextPowerballDate.setTextColor(getColor(R.color.main_black));
        textNextPowerballJackpot = findViewById(R.id.text_lotto_jackpot);
        textNextPowerballJackpot.setTextColor(getColor(R.color.main_black));
        textUserName = findViewById(R.id.text_user_name);
        textUserNameSuffix = findViewById(R.id.text_user_name_suffix);
        textWelcome = findViewById(R.id.text_welcome);

        btnMyNumberMore = findViewById(R.id.btn_my_number_more);
        btnMyNumberMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.executeMy();
            }
        });
        textMyNumberEmpty = findViewById(R.id.text_my_number_empty);
        frameMyNumber = findViewById(R.id.frame_my_number);

        itemAiNumber1 = findViewById(R.id.item_ai_number_1);
        itemAiNumber2 = findViewById(R.id.item_ai_number_2);
        itemAiNumber3 = findViewById(R.id.item_ai_number_3);
        itemAiNumber4 = findViewById(R.id.item_ai_number_4);
        itemAiNumber1.setVisibility(View.INVISIBLE);
        itemAiNumber2.setVisibility(View.INVISIBLE);
        itemAiNumber3.setVisibility(View.INVISIBLE);
        itemAiNumber4.setVisibility(View.INVISIBLE);

        textNoResult = findViewById(R.id.text_no_result);

        btnAiNumber = findViewById(R.id.btn_ai_number);
        btnAiNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.executeNumberAI();
            }
        });

        btnLottoMore = findViewById(R.id.btn_lotto_more);
        btnLottoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.executeHistory();
            }
        });

        frameLotto = findViewById(R.id.frame_lotto);

        frameAd = findViewById(R.id.frame_ad);
        String adUnitId = getResources().getString(R.string.ad_unit_test_banner);
        if(!dataManager.isDev) adUnitId = getResources().getString(R.string.ad_unit_main_banner);
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
    protected void onPause() {
        super.onPause();
        stopTimerAi();
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_GET_TEXTS)){
            settingWelcome(dataManager.Texts.size() > 0 ? dataManager.Texts.get(0).message : "");
            networkManager.request(MainActivity.this, NetworkManager.REQUEST_GET_LOTTO649S, new HashMap<String, String>(){{
                put("limit", "3");
                put("offset", "0");
            }}, NetworkManager.PARAM_TYPE_NORMAL);
        }
        else if(dto.type.equals(NetworkManager.REQUEST_GET_LOTTO649S)){
            Log.d("test", "size : " + (dataManager.Lottos != null ? dataManager.Lottos.size() : 0));
            // 최근 로또의 next_date를 StateManager에 저장
            if(dataManager.Lottos != null && dataManager.Lottos.size() > 0) {
                Lotto649DTO latestLotto = dataManager.Lottos.get(0);
                stateManager.LOTTO_DATE = latestLotto.next_date;
            }
            settingLotto(dataManager.Lottos);
            networkManager.request(MainActivity.this, NetworkManager.REQUEST_GET_NUMBERS_MY, new HashMap<String, String>(){{
                put("limit", "3");
                put("offset", "0");
                put("user_id", stateManager.USER.id + "");
            }}, NetworkManager.PARAM_TYPE_NORMAL);
        }
        else if(dto.type.equals(NetworkManager.REQUEST_GET_NUMBERS_MY)){
            settingMyNumber(dataManager.Numbers);
            networkManager.request(MainActivity.this, NetworkManager.REQUEST_GET_NUMBERS_AI, new HashMap<String, String>(){{
                put("type", "winner");
                put("limit", "20");
            }}, NetworkManager.PARAM_TYPE_NORMAL);
        }
        else if(dto.type.equals(NetworkManager.REQUEST_GET_NUMBERS_AI)){
            aiNumbers = new ArrayList<>();
            aiNumbers.addAll(dataManager.Numbers);
            settingAiNumber();
        }
        else {
            super.onComplete(dto);
        }
    }

    @Override
    protected void init() {
        super.init();

        settingUser();
        networkManager.request(this, NetworkManager.REQUEST_GET_TEXTS, new HashMap<String, String>() {{
            put("type", "random");
        }}, NetworkManager.PARAM_TYPE_NORMAL);

        networkManager.request(MainActivity.this, NetworkManager.REQUEST_GET_NUMBERS_AI, new HashMap<String, String>(){{
            put("type", "winner");
            put("limit", "20");
        }}, NetworkManager.PARAM_TYPE_NORMAL);
    }

    private void settingUser(){
        if(stateManager.USER == null) return;
        String name = stateManager.USER.name;
        if(stateManager.USER.nickname != null && !stateManager.USER.nickname.equals("")){
            name = stateManager.USER.nickname;
        }
        if(name != null && !name.equals("")){
            textUserName.setVisibility(View.VISIBLE);
            textUserNameSuffix.setVisibility(View.VISIBLE);
            textUserName.setText(name);
        }
    }

    private void settingWelcome(String str){
        textWelcome.setText(str);
    }

    private void settingLotto(ArrayList<Lotto649DTO> lottomaxs){
        // null 체크 추가
        if(lottomaxs == null || lottomaxs.isEmpty()) {
            textNextPowerballDate.setText("-");
            textNextPowerballJackpot.setText("-");
            frameLotto.removeAllViews();
            return;
        }
        
        // StateManager의 LOTTO_DATE 사용
        if(stateManager.LOTTO_DATE != null && !stateManager.LOTTO_DATE.equals("")) {
            textNextPowerballDate.setText(stateManager.LOTTO_DATE);
            textNextPowerballJackpot.setText(lottomaxs.get(0).next_jackpot);
        } else {
            textNextPowerballDate.setText("-");
            textNextPowerballJackpot.setText("-");
        }
        frameLotto.removeAllViews();
        for(int i = 0 ; i < lottomaxs.size() ; i++){
            Lotto649DTO lotto = lottomaxs.get(i);
            LottoItemView view = new LottoItemView(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            frameLotto.addView(view);
            view.setLotto(lotto);
        }
    }

    private void settingMyNumber(final ArrayList<NumberDTO> numbers){
        textMyNumberEmpty.setVisibility(View.GONE);
        if(numbers == null || numbers.size() == 0){
            textMyNumberEmpty.setVisibility(View.VISIBLE);
            return;
        }
        frameMyNumber.removeAllViews();
        frameMyNumber.addView(textMyNumberEmpty);
        for(int i = 0 ; i < numbers.size() ; i++){
            NumberDTO number = numbers.get(i);
            MyNumberItemView view = new MyNumberItemView(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            frameMyNumber.addView(view);
            view.setNumber(number);
        }
    }

    private void settingAiNumber(){
        if(aiNumbers != null && !aiNumbers.isEmpty()){
            position = 0;
            startTimerAi();
        }

        if(aiNumbers == null || aiNumbers.isEmpty()){
            textNoResult.setVisibility(View.VISIBLE);
        }
        else{
            textNoResult.setVisibility(View.GONE);
        }
    }

    private void startTimerAi(){
        stopTimerAi();
        displayAiNumber();
        timerAi = new Timer();
        timerAi.schedule(new TimerTask() {
            @Override
            public void run() {
                position++;
                if(position * 4 >= aiNumbers.size()) position = 0;
                runOnUiThread(() -> {
                    displayAiNumber();
                });
            }
        }, 5000, 5000);
    }

    private void stopTimerAi(){
        if(timerAi != null){
            timerAi.cancel();
            timerAi = null;
        }
    }

    private void displayAiNumber(){
        int size = aiNumbers.size();
        int position1 = (position * 4);
        int position2 = (position * 4 + 1);
        int position3 = (position * 4 + 2);
        int position4 = (position * 4 + 3);
        if(size > position1){
            Log.d("test", "po1 vi");
            NumberDTO number = aiNumbers.get(position1);
            itemAiNumber1.setVisibility(View.VISIBLE);
            itemAiNumber1.setNumber(number);
        }
        else{
            itemAiNumber1.setVisibility(View.INVISIBLE);
        }

        if(size > position2){
            Log.d("test", "po2 vi");
            NumberDTO number = aiNumbers.get(position2);
            itemAiNumber2.setVisibility(View.VISIBLE);
            itemAiNumber2.setNumber(number);
        }
        else{
            itemAiNumber2.setVisibility(View.INVISIBLE);
        }

        if(size > position3){
            Log.d("test", "po3 vi");
            NumberDTO number = aiNumbers.get(position3);
            itemAiNumber3.setVisibility(View.VISIBLE);
            itemAiNumber3.setNumber(number);
        }
        else{
            itemAiNumber3.setVisibility(View.INVISIBLE);
        }

        if(size > position4){
            Log.d("test", "po4 vi");
            NumberDTO number = aiNumbers.get(position4);
            itemAiNumber4.setVisibility(View.VISIBLE);
            itemAiNumber4.setNumber(number);
        }
        else{
            itemAiNumber4.setVisibility(View.INVISIBLE);
        }

        if(aiNumbers == null || aiNumbers.isEmpty()){
            textNoResult.setVisibility(View.VISIBLE);
        }
        else{
            textNoResult.setVisibility(View.GONE);
        }
    }
}
