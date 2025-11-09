package kr.co.roysoft.lotto649.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.manager.AdManager;
import kr.co.roysoft.lotto649.manager.DataManager;
import kr.co.roysoft.lotto649.manager.LinkManager;
import kr.co.roysoft.lotto649.manager.NetworkListener;
import kr.co.roysoft.lotto649.manager.NetworkManager;
import kr.co.roysoft.lotto649.manager.PreferenceManager;
import kr.co.roysoft.lotto649.manager.StateManager;

public class BaseActivity extends AppCompatActivity implements NetworkListener {
    protected LinkManager linkManager = LinkManager.getInstance();
    protected PreferenceManager preferenceManager = PreferenceManager.getInstance();
    protected NetworkManager networkManager = NetworkManager.getInstance();
    protected DataManager dataManager = DataManager.getInstance();
    protected StateManager stateManager = StateManager.getInstance();
    protected AdManager adManager = AdManager.getInstance();

    private View viewLoading;

    protected boolean isNetworking = false;

    protected int offsetList = 0;
    protected int prevLastItemForEndOfScroll = -1;

    protected OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linkManager.init(this);
        preferenceManager.init(this);
        networkManager.init(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        viewLoading = inflater.inflate(R.layout.view_loading, null);

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Override
    public void onComplete(JsonDTO dto) {
        Log.d("test", "onComplete");
    }

    @Override
    public void onError(JsonDTO dto) {
        Log.d("test", "onError");
        if(dto.error != null){
            linkManager.executeToastMessage(dto.error.message);
        }
        else {
            linkManager.executeToastMessage(dto.message);
        }
    }

    @Override
    public void networkStart() {
        isNetworking = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FrameLayout viewGroup = (FrameLayout) ((ViewGroup) BaseActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                if(viewGroup != null && viewLoading != null) {
                    viewGroup.removeView(viewLoading);
                    viewGroup.addView(viewLoading);
                }
            }
        });
    }

    @Override
    public void networkEnd() {
        isNetworking = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FrameLayout viewGroup = (FrameLayout) ((ViewGroup) BaseActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                if(viewGroup != null && viewLoading != null) {
                    viewGroup.removeView(viewLoading);
                }
            }
        });
    }

    protected void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void initPage(){
        offsetList = 0;
        prevLastItemForEndOfScroll = -1;
    }

    protected void initStatus(){
        View rootView = findViewById(R.id.view_root);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                int topInset = 0;
                int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

                // 상태바/네비게이션바 영역만큼 패딩 적용
                v.setPadding(0, topInset, 0, bottomInset);

                return insets;
            }
        });
    }
}
