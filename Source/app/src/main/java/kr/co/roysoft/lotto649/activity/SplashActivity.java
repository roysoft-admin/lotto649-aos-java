package kr.co.roysoft.lotto649.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.UUID;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.manager.PreferenceManager;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferenceManager.getString(PreferenceManager.KEY_UID) == null || preferenceManager.getString(PreferenceManager.KEY_UID).equals("")){
                    linkManager.executeLogin();
                }
                else {
                    networkManager.request(SplashActivity.this, NetworkManager.REQUEST_POST_LOGIN, new HashMap<String, String>() {{
                        put("uid", preferenceManager.getString(PreferenceManager.KEY_UID));
                    }}, NetworkManager.PARAM_TYPE_BODY);
                }
            }
        }, 1000);
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_POST_LOGIN)){
            linkManager.executeMain();
        }
        else {
            super.onComplete(dto);
        }
    }
}
