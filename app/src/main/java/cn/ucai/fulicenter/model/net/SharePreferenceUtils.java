package cn.ucai.fulicenter.model.net;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public class SharePreferenceUtils {
    private static final String SHARE_PREFERENCE_NAME="saveUserInfo";
    private static SharePreferenceUtils instance;
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;
    public static final String SHARE_PREFERENCE_USER_NAME="share_key_user_name";

    public SharePreferenceUtils(Context context){
       mSharedPreference=context.getSharedPreferences(SHARE_PREFERENCE_NAME,Context.MODE_PRIVATE);
        mEditor=mSharedPreference.edit();
    }

    public static SharePreferenceUtils getInstance(Context context) {
        if(instance==null){
            instance=new SharePreferenceUtils(context);
        }
        return instance;
    }

    public void saveUser(String username){
        mEditor.putString(SHARE_PREFERENCE_USER_NAME,username);
        mEditor.commit();
    }
    public String getUser(){
        return mSharedPreference.getString(SHARE_PREFERENCE_USER_NAME,null);
    }
    public void removeUser(){
        mEditor.remove(SHARE_PREFERENCE_USER_NAME);
        mEditor.commit();
    }
}
