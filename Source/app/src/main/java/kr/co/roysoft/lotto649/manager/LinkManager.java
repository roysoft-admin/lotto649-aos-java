package kr.co.roysoft.lotto649.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.activity.LoginActivity;
import kr.co.roysoft.lotto649.activity.LottoHistoryActivity;
import kr.co.roysoft.lotto649.activity.MainActivity;
import kr.co.roysoft.lotto649.activity.MyNumberActivity;
import kr.co.roysoft.lotto649.activity.NameChangeActivity;
import kr.co.roysoft.lotto649.activity.NumberAIActivity;
import kr.co.roysoft.lotto649.activity.NumberMenualActivity;
import kr.co.roysoft.lotto649.activity.MyActivity;
import kr.co.roysoft.lotto649.activity.SplashActivity;

public class LinkManager {
    private static LinkManager instance = new LinkManager();
    private Context mContext;
    private Toast mToast;
    private LinkManager() {
    }
    public static LinkManager getInstance(){
        return instance;
    }
    public void init(Context context) {
        mContext = context;
    }

    public void executeToastMessage(String text) {
        if(mToast != null) mToast.cancel();
        mToast = new Toast(mContext);
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_toast, null);
        ((TextView) view.findViewById(R.id.text_toast)).setText(text);
        mToast.setView(view);
        mToast.show();
    }

    public void executeLogin(){
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeNameChange(boolean isChanged){
        Intent intent = new Intent(mContext, NameChangeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isChanged", isChanged);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeMain(){
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeNumberManual(){
        Intent intent = new Intent(mContext, NumberMenualActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeNumberAI(){
        Intent intent = new Intent(mContext, NumberAIActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeMy(){
        Intent intent = new Intent(mContext, MyNumberActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeHistory(){
        Intent intent = new Intent(mContext, LottoHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeSetting(){
        Intent intent = new Intent(mContext, MyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    public void executeSplash(){
        Intent intent = new Intent(mContext, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }
}
