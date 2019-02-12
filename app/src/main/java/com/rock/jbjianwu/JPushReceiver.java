package com.rock.jbjianwu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.lib.Rock;

import cn.jpush.android.api.JPushInterface;

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
