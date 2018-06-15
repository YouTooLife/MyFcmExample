package net.youtoolife.myfcmexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Formatter;

/**
 * Created by youtoolife on 4/7/18.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "fcmsharedprefdemo";
    private static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_ACCESS_LOGIN = "login";
    private static final String KEY_ACCESS_INVITE = "invite";
    private static final String KEY_ACCESS_MSGS= "msgs";

    private static Context mCtx;
    private static SharedPrefManager mInstance;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }

    public boolean storeToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        return true;
    }

    public boolean storeMsgs(String msgs) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_MSGS, msgs);
        editor.apply();
        return true;
    }

    public boolean storeLogin(String login, String invite) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_LOGIN, login);
        String a = XA.a(invite);
        Log.d("STORE_LOGIN", a);
        editor.putString(KEY_ACCESS_INVITE, a);
        editor.apply();
        return true;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getMsgs() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_MSGS, null);
    }

    public String getLogin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_LOGIN, null);
    }

    public String getInvite() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String b = null;
        String aa = sharedPreferences.getString(KEY_ACCESS_INVITE, null);
            if (aa != null) {
                Log.d("GET_LOGIN", (aa != null) ? aa : "null");
                byte[] a = Base64.decode(aa, Base64.DEFAULT);
                b = XA.b(a);
                Log.d("GET_LOGIN", (b != null) ? b : "null");
            }
        return b;
    }




}
