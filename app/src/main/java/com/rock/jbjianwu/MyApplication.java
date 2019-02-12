package com.rock.jbjianwu;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        XGPushConfig.enableDebug(this,true);
//        XGPushManager.bindAccount(getApplicationContext(), "XINGE");
//        XGPushManager.setTag(this,"XINGE");
        //打开第三方推送
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
