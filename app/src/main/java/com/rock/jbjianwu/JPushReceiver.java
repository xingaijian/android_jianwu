package com.rock.jbjianwu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.lib.Rock;

import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;

import static android.content.ContentValues.TAG;
import static com.lib.Rock.printBundle;

public class JPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action   = intent.getAction(),title,content,json,extras;
        Bundle bundle   = intent.getExtras();
        //extras          = Rock.printBundle(bundle);

        if(Xinhu.isdemo()) {
            //Rock.Toast(context, "JPushaction："+action+"");
        }
        //网络发送变化
        if(action.equals(JPushInterface.ACTION_CONNECTION_CHANGE)){

        }

        try {
            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

//                //打开自定义的Activity
                Intent i = new Intent(context, MainActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){

        }

        //推送是消息时
        if(action.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
            Intent ent = new Intent();
            ent.setAction(Xinhu.ACTION_MESSAGE);
            ent.putExtra(JPushInterface.EXTRA_TITLE, bundle.getString(JPushInterface.EXTRA_TITLE));
            ent.putExtra(JPushInterface.EXTRA_MESSAGE, bundle.getString(JPushInterface.EXTRA_MESSAGE));
            ent.putExtra(Xinhu.PUSH_MESSAGE_TYPE, Xinhu.PUSH_MESSAGE_TYPE_JPUSH);
            context.sendBroadcast(ent);
        }

        //推送来的是通知时
        if(action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            title    = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            content  = bundle.getString(JPushInterface.EXTRA_ALERT);
            json     = bundle.getString(JPushInterface.EXTRA_EXTRA);
           // Rock.Toast(context, "通知标题："+title+"内容："+content+"extras："+extras+"");
        }

        //电池量改变
        if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
            Rock.Toast(context, "电池？");
        }

    }
}
