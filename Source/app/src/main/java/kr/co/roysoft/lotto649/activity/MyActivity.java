package kr.co.roysoft.lotto649.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.manager.PreferenceManager;

public class MyActivity extends BottomBaseAuthActivity {

    private TextView textUserName;
    private TextView textUserNameSuffix;
    private TextView textWelcome;

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btn_google){
                if(stateManager.USER.google_token != null){
                    linkManager.executeToastMessage("Already connected.");
                    return;
                }
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(signInIntent);
            }
            else if(view.getId() == R.id.btn_apple){
                if(stateManager.USER.apple_token != null){
                    linkManager.executeToastMessage("Already connected.");
                    return;
                }
            }
            else if(view.getId() == R.id.btn_naver){
                if(stateManager.USER.name != null){
                    linkManager.executeToastMessage("Already connected.");
                    return;
                }
            }
            else if(view.getId() == R.id.btn_kakao){
                if(stateManager.USER.kakao_token != null){
                    linkManager.executeToastMessage("Already connected.");
                    return;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setupBottom();
        setBottomMenu(Enums.MENU_SETTING);
        initStatus();

        textUserName = findViewById(R.id.text_user_name);
        textUserNameSuffix = findViewById(R.id.text_user_name_suffix);
        textWelcome = findViewById(R.id.text_welcome);

        findViewById(R.id.btn_name_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkManager.executeNameChange(true);
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                builder.setTitle("Alert");
                builder.setMessage("Do you want to sign-out?\n* Guest users’ past records may be deleted.");

                builder.setPositiveButton("SIGN-OUT", (DialogInterface dialog, int which) -> {
                    preferenceManager.setString(PreferenceManager.KEY_UID, null);
                    linkManager.executeSplash();
                });

                builder.setNegativeButton("CANCEL", (DialogInterface dialog, int which) -> {
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // registerForActivityResult로 구글 로그인 결과 처리
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                firebaseAuthWithGoogle(account.getIdToken());
                            } else {
                                Toast.makeText(this, "Google login failed: No account returned", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ApiException e) {
                            Toast.makeText(this, "Google login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        findViewById(R.id.btn_google).setOnClickListener(onClickListener);
        findViewById(R.id.btn_apple).setOnClickListener(onClickListener);
        findViewById(R.id.btn_kakao).setOnClickListener(onClickListener);
        findViewById(R.id.btn_naver).setOnClickListener(onClickListener);
    }

    @Override
    protected void init() {
        super.init();
        settingUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingUser();
        networkManager.request(this, NetworkManager.REQUEST_GET_TEXTS, new HashMap<String, String>() {{
            put("type", "random");
        }}, NetworkManager.PARAM_TYPE_NORMAL);
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_GET_TEXTS)){
            settingWelcome(dataManager.Texts.size() > 0 ? dataManager.Texts.get(0).message : "");
        }
        else if(dto.type.equals(NetworkManager.REQUEST_PUT_USERS_USER_ID)){
            linkManager.executeToastMessage("Connected.");
        }
        else {
            super.onComplete(dto);
        }
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

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            networkManager.request(MyActivity.this, NetworkManager.REQUEST_POST_LOGIN, new HashMap<String, String>() {{
                                put("google_token", uid);
                                if(name != null) put("nickname", name);
                                put("uid", preferenceManager.getString(PreferenceManager.KEY_UID));
                                put("user_id", dataManager.User.id + "");
                            }}, NetworkManager.PARAM_TYPE_BODY);
                        }
                    } else {
                        Toast.makeText(MyActivity.this, "Firebase authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
