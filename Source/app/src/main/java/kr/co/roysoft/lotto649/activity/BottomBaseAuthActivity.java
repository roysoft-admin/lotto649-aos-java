package kr.co.roysoft.lotto649.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import java.util.HashMap;

import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.manager.PreferenceManager;

public class BottomBaseAuthActivity extends BottomBaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(stateManager.USER == null || stateManager.USER.id == 0){
                    if(preferenceManager.getString(PreferenceManager.KEY_UID) == null || preferenceManager.getString(PreferenceManager.KEY_UID).equals("")){
                        linkManager.executeLogin();
                    }
                    else {
                        networkManager.request(BottomBaseAuthActivity.this, NetworkManager.REQUEST_POST_LOGIN, new HashMap<String, String>() {{
                            put("uid", preferenceManager.getString(PreferenceManager.KEY_UID));
                        }}, NetworkManager.PARAM_TYPE_BODY);
                    }
                }
                else{
                    init();
                }
            }
        }, 100);
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_POST_LOGIN)){
            init();
        }
        else {
            super.onComplete(dto);
        }
    }

    protected void init(){

    }
}
