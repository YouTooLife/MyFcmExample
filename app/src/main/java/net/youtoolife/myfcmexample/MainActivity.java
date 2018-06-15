package net.youtoolife.myfcmexample;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //private TextView textView;

    //private static final String URL_SERVER = "http://ytl.96.lt/fcm_db/login.php";

    private ListView listView;
    //private BroadcastReceiver broadcastReceiver;
    private EditText loginText;
    private EditText inviteText;
    private Button loginBtn;

    private RelativeLayout authActivityLayout;
    private AnimationDrawable animationDrawable;


    private String invite, login;


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            /*
             mappings.put("sexypackage.SexyClass", buffer);
            XLoader xloa = new XLoader(mappings);
            Class sexy_cla = xloa.loadClass("sexypackage.SexyClass", decryptedBytes);

            System.out.println("class was loaded");
            System.out.println("begin object creation");
            Object sexy_ob = sexy_cla.newInstance();

            System.out.println("object was created");
            System.out.println("invoke: getFoo" + sexy_cla.getMethod("getSimpleFoo").invoke(sexy_ob));
             String modFile = "xaa.dex";
            FileOutputStream outputStream = openFileOutput(modFile, MODE_PRIVATE); //new FileOutputStream(modFile, MODE_PRIVATE);
            outputStream.write(decryptedBytes);
            outputStream.close();


            //String appDir = getApplicationInfo().dataDir;
            DexClassLoader classLoader = new DexClassLoader(modFile, appDir, null, getClass().getClassLoader());
            // Загружаем класс и создаем объект с интерфейсом ModuleInterface
            //ModuleInterface module;
            try {
                Class sexy_cla = classLoader.loadClass("sexypackage/SexyClass");

            System.out.println("class was loaded");
            System.out.println("begin object creation");
            Object sexy_ob = sexy_cla.newInstance();
            System.out.println("object was created");
            System.out.println("invoke: getFoo" + sexy_cla.getMethod("getSimpleFoo").invoke(sexy_ob));
            } catch (Exception e) {
                e.printStackTrace();
            }*/


        setContentView(R.layout.activity_main);

        //textView = findViewById(R.id.textViewToken);
        String token = SharedPrefManager.getInstance(this).getToken();
        if (token != null) {
            //textView.setText(token);
            Log.d("TOKEN:", token);
        }

        loginText = findViewById(R.id.editLogin);
        inviteText = findViewById(R.id.editInvite);

        loginBtn = (Button) findViewById(R.id.loginBtn);


        authActivityLayout = (RelativeLayout) findViewById(R.id.AuthActivityLayout);
        animationDrawable = (AnimationDrawable) authActivityLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(1500);
        animationDrawable.start();


        if (SharedPrefManager.getInstance(getApplicationContext()).getInvite() != null
                && SharedPrefManager.getInstance(getApplicationContext()).getLogin() != null) {
            invite = SharedPrefManager.getInstance(getApplicationContext()).getInvite();
            login = SharedPrefManager.getInstance(getApplicationContext()).getLogin();
            login();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("button:", "onClick");
                invite = inviteText.getText().toString().trim();
                login = loginText.getText().toString().trim();
                login();
                //Intent intent = new Intent(MainActivity.this, ContActivity.class);
                //startActivity(intent);
            }
        });

        /*
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseInstanceIdService.TOKEN_BROADCAST));*/

    }

    private void login() {

        Map<String, String> params0 = new HashMap<>();
        params0.put("dev", SharedPrefManager.getInstance(getApplicationContext()).getToken());
        params0.put("login", login);
        params0.put("invite", invite);
        params0.put("pwd",XA.b(XA.B));

        JSONObject jsonObject = new JSONObject(params0);
        Map<String, String> params = new HashMap<>();
        String json = jsonObject.toString();
        System.out.print("json "+json);
        params.put("d", RSAIsa.rsaEncrypt(jsonObject.toString()));


        RequestHandler requestHandler = new RequestHandler(XA.b(XA.A), params, getApplicationContext());
        requestHandler.request(new CallBack() {
            @Override
            public void callBackFunc(String response) {
                try {
                    Log.d("Main: Answ", response);
                    JSONObject obj = new JSONObject(response);
                    int id = obj.getInt("id");
                    if (id > -1) {

                        SharedPrefManager.getInstance(getApplicationContext()).storeMsgs(response);
                        SharedPrefManager.getInstance(getApplicationContext()).storeLogin(login, invite);
                /*
                JSONArray arr = obj.getJSONArray("msg");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.optJSONObject(i);
                    Log.d("MSGS:", o.getString("msg"));
                }*/
                        Log.d("Translate:", "goToCont");
                        Intent content = new Intent(MainActivity.this, ContActivity.class);
                        startActivity(content);
                    }
                    else {
                        if (id == -2 && SharedPrefManager.getInstance(getApplicationContext()).getMsgs() != null) {
                            Intent content = new Intent(MainActivity.this, ContActivity.class);
                            startActivity(content);
                        }
                        else
                            inviteText.setError("Wrong invite code!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }




}

