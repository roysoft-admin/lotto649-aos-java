package kr.co.roysoft.lotto649.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import kr.co.roysoft.lotto649.activity.LoginActivity;
import kr.co.roysoft.lotto649.activity.MainActivity;

public class PreferenceManager {
    private SharedPreferences pre = null;

    private static PreferenceManager instance = new PreferenceManager();

    private PreferenceManager() {}

    public void init(Context context) {
        pre = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static PreferenceManager getInstance() {
        return instance;
    }

    public static final String KEY_UID = "key_uid";

    public String getString(String key){
        if(pre == null) return null;
        Log.d("test", "preference get key: " + key + ", value: " + pre.getString(key, null));
        return pre.getString(key, null);
    }
    public void setString(String key, String value){
        Log.d("test", "preference set key: " + key + ", value: " + value);
        pre.edit().putString(key, value).apply();
    }
}
