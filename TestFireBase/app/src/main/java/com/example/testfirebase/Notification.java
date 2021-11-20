package com.example.testfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.HmsMessaging;
import com.huawei.hms.push.RemoteMessage;
import com.huawei.hms.support.api.push.service.HmsMsgService;

public class Notification extends AppCompatActivity {
    private static final String TAG = "Notification";
    TextView TVtoken;
    Button btn;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        TVtoken = findViewById(R.id.token);
        btn = findViewById(R.id.btn);
        editText = findViewById(R.id.notification_fullname);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPushToken();
            }
        });

    }
    private void showToken(final String token){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TVtoken.setText(token);
                editText.setText(token);

            }
        });
    }
    private void getPushToken() {
        new Thread() {
            @Override
            public void run() {
                // If there is no local AAID, this method will automatically generate an AAID when it is called because the Huawei Push server needs to generate a token based on the AAID.
                // This method is a synchronous method, and you cannot call it in the main thread. Otherwise, the main thread may be blocked.
                try {
                    // read from agconnect-services.json.
                    String appId = AGConnectServicesConfig.fromContext(Notification.this).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(Notification.this).getToken(appId, "HCM");

                    Log.i(TAG,"get token"+ token);
                    if (!TextUtils.isEmpty(token)) {
                        // get token successful, send token to server.
                        // a new token will be returned from HmsMessageService's method onNewToken(String).
                        showToken(token);
                        sendRegTokenToServer(token);
                        String messageId = String.valueOf(System.currentTimeMillis());

// The input parameter of the RemoteMessage.Builder method is push.hcm.upstream, which cannot be changed.
                        RemoteMessage remoteMessage = new RemoteMessage.Builder("push.hcm.upstream")
                                .setMessageId(messageId)
                                .addData("key1", "data1")
                                .addData("key2", "data2")
                                .build();
                        try {
                            // Send an uplink message.
                           // HmsMessaging.getInstance(Notification.this).send(remoteMessage);
                            HmsMessaging.getInstance(Notification.this).a(remoteMessage);

                        //HmsMessaging.getInstance(Notification.this).subscribe(token);
                            Log.i(TAG, "send message successfully");
                        } catch (Exception e) {
                            Toast.makeText(Notification.this, "build Mess fail", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "send message catch exception: " + e);
                        }



                    }
                } catch (com.huawei.hms.common.ApiException e) {
                    Log.e(TAG,"get token failed" + e);
                }
            }
        }.start();
    }


    private void sendRegTokenToServer(String token){
        Log.i(TAG,"sendRegTokenToServer:"+token);
    }
}
