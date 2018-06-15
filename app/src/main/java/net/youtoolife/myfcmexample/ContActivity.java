package net.youtoolife.myfcmexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContActivity extends AppCompatActivity {


    private BroadcastReceiver broadcastReceiver;

    //private ArrayAdapter<String> adapter = null;
    //private List<String> list = null;
    //private ListView msgList;

    private AlertDialog.Builder ad;
    RVAdapter adapter;


    private volatile List<Message> msgs;
    private RecyclerView rv;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData(){
        msgs = new ArrayList<>();

        msgs.add(new Message(0, "Title", "Body", "07.08.1970", "nop"));
        msgs.add(new Message(1, "Title", "Body", "07.08.1970", "nop"));
        //msgs.add(new Person("Lillie Watts", "35 years old", R.mipmap.ic_launcher0));

    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(ContActivity.this);
        builder.setTitle("Вы действительно хотите выйти?")
                .setMessage("Если выйти из системы, уведомления приходить не будут!")
                //.setIcon(R.drawable.ic_launcher_background)
                .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();

                        Map<String, String> params0 = new HashMap<>();
                        params0.put("login", SharedPrefManager.getInstance(getApplicationContext()).getLogin());
                        params0.put("invite", SharedPrefManager.getInstance(getApplicationContext()).getInvite());
                        params0.put("pwd",XA.b(XA.B));

                        JSONObject jsonObject = new JSONObject(params0);
                        Map<String, String> params = new HashMap<>();
                        String json = jsonObject.toString();
                        System.out.print("json "+json);
                        params.put("d", RSAIsa.rsaEncrypt(jsonObject.toString()));

                        String URL_SERVER = XA.b(XA.AA);

                        RequestHandler requestHandler = new RequestHandler(URL_SERVER, params, getApplicationContext());
                        //String response = requestHandler.request();
                        requestHandler.request(new CallBack() {
                            @Override
                            public void callBackFunc(String response) {
                                responseLogOut(response);
                            }
                        });
                    }
                })
                .setCancelable(false)
                .setNegativeButton("Остаться в системе",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont);


        Log.d("ACTIVITY", "OPEN");


        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);



        initializeData();
        adapter = new RVAdapter(msgs);
        rv.setAdapter(adapter);




/*
        msgList = (ListView) findViewById(R.id.msgList);
        String[] msgs = {"NO DATA"};
        list = new ArrayList<>();
        Collections.addAll(list, msgs);

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        msgList.setAdapter(adapter);

 */
        responseHandler(SharedPrefManager.getInstance(getApplicationContext()).getMsgs());

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("breceive:", "yes");
                responseHandler(null);
                //list.clear();
                //list.add("test");

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseMessagingService.TOKEN_BROADCAST));

//*/

    }


    private void readMsgs(JSONObject obj) {
        try {

            msgs.clear();

        JSONArray arr = obj.getJSONArray("msg");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            int index = o.getInt("index");
            String msg = o.getString("msg");
            String hash = o.getString("h");
            String date = o.getString("date");

            Message msg0 = new Message(index, "SMS-INFO", msg, date, hash);
            msgs.add(msg0);

            if (!hash.isEmpty()) {
                if (!adapter.bitmaps.containsKey(hash)) {
                    byte[] blob = getImg(hash);
                    if (blob != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                        adapter.bitmaps.put(hash, bitmap);
                    }
                    else
                        loadImg(hash);

                }
            }
            Log.d("MSGS:", msg);
            //adapter.add(msg);
            rv.getAdapter().notifyDataSetChanged();
        }
        /*
            adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            msgList.setAdapter(adapter);
            */
        //rv.setAdapter(new RVAdapter(msgs));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseLogOut(String response) {
        SharedPrefManager.getInstance(getApplicationContext()).storeLogin(null,null);
        //SharedPrefManager.getInstance(getApplicationContext()).storeToken(null);

        Intent content = new Intent(ContActivity.this, MainActivity.class);
        startActivity(content);
        Log.d("out: ", response);
    }

    public void update(String invite, String login) {

        /*
        Map<String, String> params = new HashMap<>();
        params.put("dev", SharedPrefManager.getInstance(getApplicationContext()).getToken());
        params.put("login", login);
        params.put("invite", invite);
        */
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

        String URL_SERVER = XA.b(XA.A);

        RequestHandler requestHandler = new RequestHandler(URL_SERVER, params, getApplicationContext());
        //String response = requestHandler.request();
        requestHandler.request(new CallBack() {
            @Override
            public void callBackFunc(String response) {
                responseHandler(response);
            }
        });
    }

    private void responseHandler(String response) {

        try {
            if (response == null) {
                if (SharedPrefManager.getInstance(getApplicationContext()).getInvite() != null
                        && SharedPrefManager.getInstance(getApplicationContext()).getLogin() != null) {
                    String invite = SharedPrefManager.getInstance(getApplicationContext()).getInvite();
                    String login = SharedPrefManager.getInstance(getApplicationContext()).getLogin();
                    update(invite, login);
                }
                else {
                    Intent content = new Intent(ContActivity.this, MainActivity.class);
                    startActivity(content);
                    //return;
                }
            } else {
                Log.d("Cont: Answ", response);
                JSONObject obj = new JSONObject(response);
                int id = obj.getInt("id");
                if (id > -1) {
                    SharedPrefManager.getInstance(getApplicationContext()).storeMsgs(response);
                    //SharedPrefManager.getInstance(getApplicationContext()).storeLogin(login, invite);
                    readMsgs(obj);
                } else {
                    if (id != -2 || SharedPrefManager.getInstance(getApplicationContext()).getMsgs() == null) {
                        Intent content = new Intent(ContActivity.this, MainActivity.class);
                        startActivity(content);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void loadImg(String hash) {

        Map<String, String> params0 = new HashMap<>();
        params0.put("h", hash);
        params0.put("pwd",XA.b(XA.B));

        JSONObject jsonObject = new JSONObject(params0);
        Map<String, String> params = new HashMap<>();
        String json = jsonObject.toString();
        System.out.print("json "+json);
        params.put("d", RSAIsa.rsaEncrypt(jsonObject.toString()));

        String URL_SERVER = XA.b(XA.C);

        RequestHandler requestHandler = new RequestHandler(URL_SERVER, params, getApplicationContext());
        //String response = requestHandler.request();
        requestHandler.request(new CallBack() {
            @Override
            public void callBackFunc(String response) {
                imgResponseHandler(response);
            }
        });
    }


    public void imgResponseHandler(String response) {

        if (response == null)
            return;

        Log.d("Cont: Answ", response);
        try {
            JSONObject obj = new JSONObject(response);

            byte[] blob = null;

            int id = obj.getInt("id");
            if (id > -1) {

                JSONArray img = obj.getJSONArray("blob");

                String hash = obj.getString("hash");

                ByteBuffer byteBuffer = ByteBuffer.allocate(img.length() * 316*1024);
                //blob = new byte[img.length()];
                for (int j = 0; j < img.length(); j++) {
                    //JSONArray img = imgs.getJSONArray(j);
                    /*byte[] encbyte = new byte[img.length()];
                    for (int jx = 0; jx < img.length(); jx++)
                        encbyte[jx] = (byte) img.getInt(jx);
                    try {
                       // Log.d("BLOB_PUT: ", new String(encbyte, "UTF-8"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    byte[] dec = Base64.decode(img.getString(j), Base64.DEFAULT);
                    byteBuffer.put(dec);
                }

                blob = byteBuffer.array();

                try {
                    byte[] hash0 = MessageDigest.getInstance("MD5").digest(blob);
                    Formatter formatter = new Formatter();
                    for (byte b : hash0) {
                        formatter.format("%02x", b);
                    }
                    String hashhex = formatter.toString();
                    formatter.close();
                    Log.d("BLOB_PUT0: ", hashhex+" : "+hash);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                storeImg(hash, blob);

                /*
                for (Message msg:msgs)
                    if (msg.hash.equalsIgnoreCase(hash))
                        msg.setBlob(blob);
                        */
                if (!adapter.bitmaps.containsKey(hash)) {
                    if (blob != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                        adapter.bitmaps.put(hash, bitmap);
                    }
                }
                //Message msg0 = new Message(777, "SMS-INFO", "msg", "dadte", hash, blob);
                //msgs.add(msg0);
                //rv.setAdapter(new RVAdapter(msgs));
                rv.getAdapter().notifyDataSetChanged();
            }
        } catch (JSONException e) {
                e.printStackTrace();
            }
    }





    public void storeImg(String hash, byte[] data) {
        String filename = hash + ".bin";
        FileOutputStream outputStream;
        try {
            File file = new File(getApplicationContext().getFilesDir(), filename);
            outputStream = new FileOutputStream(file); //openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] getImg(String hash) {
        String filename = hash + ".bin";
        FileInputStream inputStream;

        byte[] output = null;
        try {

            File file = new File(getApplicationContext().getFilesDir(), filename);
            if (file.exists()) {
                inputStream = new FileInputStream(file); //openFileOutput(filename, Context.MODE_PRIVATE);
                output = new byte[(int) file.length()];
                inputStream.read(output);
                inputStream.close();

                Formatter formatter = new Formatter();
                for (int i = 0; i < 300; i++) {
                    formatter.format("%02x ", output[i]);
                }
                String sd = formatter.toString();
                formatter.close();
                Log.d("getSTORE+", sd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  output;
    }


}
