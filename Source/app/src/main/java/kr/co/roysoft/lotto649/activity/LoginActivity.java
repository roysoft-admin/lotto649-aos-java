package kr.co.roysoft.lotto649.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.UUID;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.manager.PreferenceManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class LoginActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btn_google){
                Log.d("GoogleLogin", "Google login button clicked");
                
                // 현재 로그인 상태 확인
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                if (account != null) {
                    Log.d("GoogleLogin", "Already signed in with account: " + account.getEmail());
                    // 이미 로그인된 경우 로그아웃 후 다시 로그인
                    mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this, task -> {
                        Log.d("GoogleLogin", "Previous account signed out");
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        googleSignInLauncher.launch(signInIntent);
                    });
                } else {
                    Log.d("GoogleLogin", "No previous sign-in account found");
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    googleSignInLauncher.launch(signInIntent);
                }
            }
            else if(view.getId() == R.id.btn_apple){

            }
            else if(view.getId() == R.id.btn_naver){

            }
            else if(view.getId() == R.id.btn_kakao){

            }
            else if(view.getId() == R.id.btn_guest){
                genarateUID();
                networkManager.request(LoginActivity.this, NetworkManager.REQUEST_POST_LOGIN, new HashMap<String, String>() {{
                    put("uid", preferenceManager.getString(PreferenceManager.KEY_UID));
                }}, NetworkManager.PARAM_TYPE_BODY);
            }
        }
    };

    @Override
    public void onComplete(JsonDTO dto) {
        Log.d("GoogleLogin", "onComplete called with type: " + dto.type);
        if(dto.type.equals(NetworkManager.REQUEST_POST_LOGIN)){
            Log.d("GoogleLogin", "POST /login successful, executing main");
            linkManager.executeMain();
        }
        else {
            Log.d("GoogleLogin", "Other request type: " + dto.type);
            super.onComplete(dto);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        initStatus();

        // Firebase Auth, GoogleSignIn 초기화
        String webClientId = getString(R.string.default_web_client_id);
        Log.d("GoogleLogin", "Web Client ID: " + webClientId);
        
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        
        Log.d("GoogleLogin", "GoogleSignIn client initialized");

        // registerForActivityResult로 구글 로그인 결과 처리
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("GoogleLogin", "Google sign-in result received. Result code: " + result.getResultCode());
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Log.d("GoogleLogin", "Google sign-in successful, processing account");
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                Log.d("GoogleLogin", "Google account retrieved successfully");
                                firebaseAuthWithGoogle(account.getIdToken());
                            } else {
                                Log.e("GoogleLogin", "Google account is null");
                                Toast.makeText(this, "Google login failed: No account returned", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ApiException e) {
                            Log.e("GoogleLogin", "Google sign-in failed: " + e.getMessage());
                            Toast.makeText(this, "Google login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("GoogleLogin", "Google sign-in failed. Result code: " + result.getResultCode());
                        if (result.getData() != null) {
                            Log.e("GoogleLogin", "Result data: " + result.getData().toString());
                            
                            // Intent extras 확인
                            if (result.getData().getExtras() != null) {
                                Log.e("GoogleLogin", "Result extras: " + result.getData().getExtras().toString());
                                
                                // Bundle의 모든 키와 값 출력
                                for (String key : result.getData().getExtras().keySet()) {
                                    Object value = result.getData().getExtras().get(key);
                                    Log.e("GoogleLogin", "Extra key: " + key + ", value: " + value + " (type: " + (value != null ? value.getClass().getSimpleName() : "null") + ")");
                                }
                                
                                // 구글 로그인 관련 에러 코드 확인
                                if (result.getData().getExtras().containsKey("errorCode")) {
                                    int errorCode = result.getData().getExtras().getInt("errorCode");
                                    Log.e("GoogleLogin", "Google error code: " + errorCode);
                                }
                                
                                // 에러 메시지 확인
                                if (result.getData().getExtras().containsKey("errorMessage")) {
                                    String errorMessage = result.getData().getExtras().getString("errorMessage");
                                    Log.e("GoogleLogin", "Google error message: " + errorMessage);
                                }
                                
                                // GoogleSignIn 관련 키들 확인
                                if (result.getData().getExtras().containsKey("com.google.android.gms.common.api.Status")) {
                                    Object status = result.getData().getExtras().get("com.google.android.gms.common.api.Status");
                                    Log.e("GoogleLogin", "Google Status: " + status);
                                }
                                
                                if (result.getData().getExtras().containsKey("com.google.android.gms.signin.internal.AuthAccountResultParcelable")) {
                                    Object authResult = result.getData().getExtras().get("com.google.android.gms.signin.internal.AuthAccountResultParcelable");
                                    Log.e("GoogleLogin", "Auth Account Result: " + authResult);
                                }
                            }
                        } else {
                            Log.e("GoogleLogin", "Result data is null");
                        }
                        
                        // Result code별 상세 메시지
                        String errorMessage = "Google login failed: ";
                        switch (result.getResultCode()) {
                            case RESULT_CANCELED:
                                errorMessage += "User canceled or sign-in was interrupted";
                                break;
                            case RESULT_FIRST_USER:
                                errorMessage += "First user result";
                                break;
                            default:
                                errorMessage += "Unknown error (code: " + result.getResultCode() + ")";
                                break;
                        }
                        
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
        );

        findViewById(R.id.btn_google).setOnClickListener(onClickListener);
        findViewById(R.id.btn_apple).setOnClickListener(onClickListener);
        findViewById(R.id.btn_kakao).setOnClickListener(onClickListener);
        findViewById(R.id.btn_naver).setOnClickListener(onClickListener);
        findViewById(R.id.btn_guest).setOnClickListener(onClickListener);
    }

    private void genarateUID(){
        String uid = UUID.randomUUID().toString();
        preferenceManager.setString(PreferenceManager.KEY_UID, uid);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d("GoogleLogin", "Starting Firebase authentication with Google");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("GoogleLogin", "Firebase authentication successful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            Log.d("GoogleLogin", "User UID: " + uid + ", Name: " + name);
                            genarateUID();
                            String localUid = preferenceManager.getString(PreferenceManager.KEY_UID);
                            Log.d("GoogleLogin", "Local UID: " + localUid);
                            
                            HashMap<String, String> loginParams = new HashMap<>();
                            loginParams.put("google_token", uid);
                            if(name != null) loginParams.put("nickname", name);
                            loginParams.put("uid", localUid);
                            
                            Log.d("GoogleLogin", "Sending POST /login request with params: " + loginParams.toString());
                            networkManager.request(LoginActivity.this, NetworkManager.REQUEST_POST_LOGIN, loginParams, NetworkManager.PARAM_TYPE_BODY);
                        } else {
                            Log.e("GoogleLogin", "Firebase user is null");
                            Toast.makeText(LoginActivity.this, "Google login failed: User is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("GoogleLogin", "Firebase authentication failed: " + task.getException().getMessage());
                        Toast.makeText(LoginActivity.this, "Firebase authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
