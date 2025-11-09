package kr.co.roysoft.lotto649.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.manager.PreferenceManager;

public class NameChangeActivity extends BaseActivity {

    private EditText editNickname;

    public boolean isChange = false;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_skip){
                networkManager.request(NameChangeActivity.this, NetworkManager.REQUEST_PUT_USERS_USER_ID, new HashMap<String, String>() {{
                    put("nickname", "GUEST" + preferenceManager.getString(PreferenceManager.KEY_UID).substring(0, 3));
                    put("user_id", dataManager.User.id + "");
                }}, NetworkManager.PARAM_TYPE_BODY);
            }
            else if(v.getId() == R.id.btn_done){
                if(editNickname.getText().toString().equals("")){
                    linkManager.executeToastMessage("Nickname Here");
                }
                else {
                    networkManager.request(NameChangeActivity.this, NetworkManager.REQUEST_PUT_USERS_USER_ID, new HashMap<String, String>() {{
                        put("nickname", editNickname.getText().toString());
                        put("user_id", dataManager.User.id + "");
                    }}, NetworkManager.PARAM_TYPE_BODY);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);
        initStatus();

        isChange = getIntent().getBooleanExtra("isChanged", false);

        editNickname = findViewById(R.id.edit_nickname);

        findViewById(R.id.btn_done).setOnClickListener(onClickListener);
        findViewById(R.id.btn_skip).setOnClickListener(onClickListener);

        if(isChange){
            findViewById(R.id.btn_skip).setVisibility(View.GONE);
            editNickname.setText(stateManager.USER.nickname);
        }
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_PUT_USERS_USER_ID)){
            if(isChange){
                linkManager.executeToastMessage("Changed.");
                finish();
            }
            else {
                linkManager.executeMain();
            }
        }
        else {
            super.onComplete(dto);
        }
    }
}
