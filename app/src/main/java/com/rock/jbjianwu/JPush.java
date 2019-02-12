/**
 * 初始化极光推送
 * */

package com.rock.jbjianwu;
import android.content.Context;
import android.content.Intent;

import com.lib.Rock;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JPush {

    private static Context mContext;
    private static String mAlias;

    public static String AliasStr   = "alias";

    //直接启动
    public static void init(Context context, String alias) {
        mContext = context;
        if(!Rock.isEmpt(alias))mAlias   = alias;
        JPushInterface.init(context);
        if (JPushInterface.isPushStopped(context)) {
            JPushInterface.resumePush(context);
        }
        if(!Rock.isEmpt(mAlias)) initJpushsetAlias();
    }

    //设置别名和标签
    private static void initJpushsetAlias() {
        JPushInterface.setAliasAndTags(mContext, mAlias, null, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias, Set<String> tags) {
                if(code==0) {
                    //Rock.Toast(mActivity, "OK:"+alias+"");
                }else if(code==6002){
                    initJpushsetAlias();
                }else{

                }
            }
        });
    }

    //用服务的启动
    public static void initService(Context context, String alias) {
        mContext = context;
        if(!Rock.isEmpt(alias))mAlias   = alias;
        Intent it	 	= new Intent("com.rock.jbjianwu.XinhuService");
        it.putExtra(AliasStr, mAlias);
        mContext.startService(it);
    }
}