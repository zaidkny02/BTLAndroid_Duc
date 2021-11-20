package com.example.testfirebase;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.huawei.hms.push.SendException;


import java.util.Arrays;


public class DemoHmsMessageService extends HmsMessageService {
    private static final String TAG = "DemoHmsMessageService";
    private final static String CODELABS_ACTION = "com.huawei.codelabpush.action";
    // This method callback must be completed in 10 seconds. Otherwise,     you need to start a new Job for callback processing.
    // extends HmsMessageService super class
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.i(TAG, "received refresh token:" + token);
        // send the token to your app server.
      //  Toast.makeText(super.getApplicationContext(), "New Token", Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
        Intent intent = new Intent();
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onNewToken");
        intent.putExtra("msg", "onNewToken called, token: " + token);

        sendBroadcast(intent);

    }
    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        Log.i(TAG, "onMessageReceived is called");
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            Toast.makeText(super.getApplicationContext(), "Mess null", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(super.getApplicationContext(), "Mess not null", Toast.LENGTH_SHORT).show();
        // Obtain the message content.
        Log.i(TAG, "getCollapseKey: " + message.getCollapseKey()
                + "\n getData: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getTtl: " + message.getTtl()
                + "\n getSendMode: " + message.getSendMode()
                + "\n getReceiptMode: " + message.getReceiptMode()
                + "\n getOriginalUrgency: " + message.getOriginalUrgency()
                + "\n getUrgency: " + message.getUrgency()
                + "\n getToken: " + message.getToken());
        RemoteMessage.Notification notification = message.getNotification();
        Boolean judgeWhetherIn10s = false;
        if (notification != null) {
            Log.i(TAG, "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getImageUrl: " + notification.getImageUrl()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getIntentUri: " + notification.getIntentUri()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId()
                    + "\n isDefaultLight: " + notification.isDefaultLight()
                    + "\n isDefaultSound: " + notification.isDefaultSound()
                    + "\n isDefaultVibrate: " + notification.isDefaultVibrate()
                    + "\n getWhen: " + notification.getWhen()
                    + "\n getLightSettings: " + Arrays.toString(notification.getLightSettings())
                    + "\n isLocalOnly: " + notification.isLocalOnly()
                    + "\n getBadgeNumber: " + notification.getBadgeNumber()
                    + "\n isAutoCancel: " + notification.isAutoCancel()
                    + "\n getImportance: " + notification.getImportance()
                    + "\n getTicker: " + notification.getTicker()
                    + "\n getVibrateConfig: " + Arrays.toString(notification.getVibrateConfig())
                    + "\n getVisibility: " + notification.getVisibility());
        }
        Intent intent = new Intent();
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onMessageReceived");
        intent.putExtra("msg", "onMessageReceived called, message id:" + message.getMessageId() + ", payload data:"
                + message.getData());

        sendBroadcast(intent);
        // If the message is not processed within 10 seconds, create a job to process it.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message);
        } else {
            // Process the message within 10 seconds.
            processWithin10s(message);
        }
    }
    private void startWorkManagerJob(RemoteMessage message) {
        Log.d(TAG, "Start new job processing.");
    }
    private void processWithin10s(RemoteMessage message) {
        Log.d(TAG, "Processing now.");
    }
    @Override
    public void onMessageSent(String msgId) {
        Log.i(TAG, "onMessageSent called, Message id:" + msgId);
        Toast.makeText(super.getApplicationContext(), "Sent Mess", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onMessageSent");
        intent.putExtra("msg", "onMessageSent called, Message id:" + msgId);

        sendBroadcast(intent);
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        Log.i(TAG, "onSendError called, message id:" + msgId + ", ErrCode:"
                + ((SendException) exception).getErrorCode() + ", description:" + exception.getMessage());

        Intent intent = new Intent();
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onSendError");
        intent.putExtra("msg", "onSendError called, message id:" + msgId + ", ErrCode:"
                + ((SendException) exception).getErrorCode() + ", description:" + exception.getMessage());

        sendBroadcast(intent);
    }

    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
    }


}
