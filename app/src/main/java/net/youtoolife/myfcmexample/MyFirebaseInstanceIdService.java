package net.youtoolife.myfcmexample;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by youtoolife on 4/7/18.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    public static final  String TOKEN_BROADCAST = "mybroadcast";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myfirebase", "Refreshed token: " + refreshedToken);

        //sendRegistrationToServer(refreshedToken);
        //getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }
}
