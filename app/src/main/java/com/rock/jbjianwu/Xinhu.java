/**
 * 定义一下方法常量
 * */

package com.rock.jbjianwu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.lib.Jiami;
import com.lib.Rock;
import com.lib.RockFile;
import com.lib.RockHttp;
import java.util.ArrayList;
import java.util.Map;


public class Xinhu {

    public static final String CHAT_USERVAL   = "user";
    public static final String CHAT_GROUPVAL  = "group";
    public static final String CHAT_AGENTVAL  = "agent";


    public static final String CACAHE_DIRNAME  = "dzjcy"; //缓存目录

    //public static String  APIURL 	    = "http://192.168.1.102/app/xinhu/"; //api地址
    public static String  APIURL 	    = "http://dzjcy.dezhou.name/"; //api地址
    public static String  device 	    = "";   //设备号

    //当前用户id和姓名
    public static String adminid        = "";
    public static String adminname      = "";
    public static String admintoken     = "";
    public static Map<String, String> adminmap       = null;

    //打开会话人员信息
    public static String recechatid     = "";
    public static String recechatsend   = "";
    public static String recechatname   = "";
    public static String recechattype   = ""; //会话类型

    //打开的应用
    public static String nowagentnum    = "";
    public static String nowagentname   = "";

    public static String PUSH_MESSAGE_TYPE   = "push_message_type";
    public static String PUSH_MESSAGE_TYPE_JPUSH   = "jpush";
//    public static String PUSH_MESSAGE_TYPE_XIAOMI   = "xiaomi";

    public static String ACTION_MESSAGE      = "com.rock.jbjianwu.MESSAGE";  //接收到消息

    public static String ACTION_CONNECTION   = "com.rock.jbjianwu.CONNECTION"; //连接推送成功时

    public static String ACTION_CHATRECEMESS = "com.rock.jbjianwu.CHATRECEMESS"; //推送到对话列表时

    public static String ACTION_ALLCLOSE    = "com.rock.jbjianwu.ALLCLOSE"; //打开主页面时全部子页面必须关闭

    public static String ACTION_DOWNBACK    = "com.rock.jbjianwu.DOWNRECREDBACK"; //下载聊天记录回传

    public static String ACTION_OPENSERVER  = "com.rock.jbjianwu.OPENSERVER"; //打开服务


    public static String ACTION_CLICKNOTI   = "com.rock.jbjianwu.CLICKNotification"; //打开通知


    public static final String XIAOMI_APP_ID = "2882303761517610798";
    public static final String XIAOMI_APP_KEY = "5821761079798";



    public static String SERVICETYPE        = "servicetype";
    public static String SERVICETYPE_DOWN   = "downrecord"; //下载聊天记录
    public static Map<String, String> DWONLASTMAP       = null;


    /**
     * 清空会话记录信息
     * */
    public static void clearchat()
    {
        recechatid     = "";
        recechatname   = "";
        recechattype   = "";
        recechatsend   = "";
        nowagentnum    = "";
        nowagentname   = "";
    }

    /**
     * 清空登录信息
     * */
    public static void clearlogin()
    {
        adminid     = "";
        adminname   = "";
        admintoken  = "";
        adminmap    = null;
        clearchat();
    }

    /**
     * 获取一个访问的Url
     * */
    public static String geturl(String url)
    {
        if(Rock.isEmpt(url))return "";
        url = url.replace("{url}", APIURL);
        if(!Rock.equals(url.substring(0,4), "http")){
            url = APIURL+url;
        }
        return url;
    }

    /**
     * 获取api地址
     * */
    public static String getapiurl(String m, String a)
    {
        String url = ""+APIURL+"api.php?m="+m+"&a="+a+"&cfrom=appandroid&device="+device+"&token="+admintoken+"&adminid="+adminid+"";
        return url;
    }


    /**
     * post请求
     * */
    public static void ajaxpost(String m, String a, String[] param,Handler myhandlers, int getcode)
    {
        ArrayList<String[]> params = new ArrayList<String[]>();
        int len = param.length,i;
        for (i = 0; i < len; i=i+2) {
            params.add(new String[]{param[i], param[i+1]});
        }
        RockHttp.post(getapiurl(m,a), myhandlers, params, getcode);
    }

    /**
     * get请求
     * */
    public static void ajaxget(String m, String a, String params,Handler myhandlers, int getcode)
    {
        String url = getapiurl(m,a);
        if(!Rock.isEmpt(params))url=url+"&"+params+"";
        RockHttp.get(url, myhandlers, getcode);
    }
    public static void ajaxget(String m, String a,Handler myhandlers, int getcode)
    {
        ajaxget(m,a,"",myhandlers,getcode);
    }

    /**
     * 下载头像
     * */
    public static Bitmap downface(String face, Handler myhandlers, int getcode)
    {
        String nface= RockFile.existsFile(face);
        if(Rock.isEmpt(nface)) {
            String path = Jiami.base64encode(face);
            Xinhu.ajaxget("login", "downimg", "path=" + path + "",myhandlers, getcode);
            return null;
        }else{
            return BitmapFactory.decodeFile(nface);
        }
    }

    /**
     * 发通知
     * */
    public static void Notification(Context context, int icon, String title, String cont)
    {
        int id = 99; //消息Id
        NotificationManager notifmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder1 = new Notification.Builder(context);
        builder1.setSmallIcon(R.drawable.icons); //设置图标
        builder1.setTicker("显示第二个通知");
        builder1.setContentTitle(title); //设置标题
        builder1.setContentText(cont); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent =new Intent (context,IndexActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        intent.setAction(ACTION_CLICKNOTI);
        builder1.setContentIntent(pendingIntent); Notification notification1 = builder1.build();
        notifmanager.notify(id, notification1); // 通过通知管理器发送通知



/*        NotificationManager notifmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id              = 99; //消息Id
//        if(icon == 0)
        icon   = R.drawable.icons;
        //打开主页
        Intent intent_to 	= new Intent(context, IndexActivity.class);
        PendingIntent pi 	= PendingIntent.getActivity(context, id, intent_to, PendingIntent.FLAG_UPDATE_CURRENT);
        intent_to.setAction(ACTION_CLICKNOTI);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent_to, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notif 	= new Notification();

        notif.icon          = icon;//图标
        notif.tickerText    = title;
        notif.when          = System.currentTimeMillis();  //显示时间
        //notif.defaults      |= Notification.DEFAULT_SOUND; //添加声音
        notif.sound         = Uri.parse("android.resource://"+ context.getPackageName()+ "/" + R.raw.todo);//自定义声音
        notif.flags         = Notification.FLAG_AUTO_CANCEL; //点击自动取消
        notif.setLatestEventInfo(context, title, cont, pi);
        notifmanager.notify(id, notif); //发出通知*/

    }

    public static void NotificationcancelAll(Context context)
    {
        NotificationManager notifmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifmanager.cancelAll();
    }


    //用服务的启动
    public static void startService(Context context, String type) {
        Intent it	 	= new Intent(context, XinhuService.class);
        it.putExtra(SERVICETYPE, type);
        context.startService(it);
    }

    //停止服务
    public static void stopService(Context context) {
        Intent it	 	= new Intent(context, XinhuService.class);
        context.stopService(it);
    }


    //发广播
    public static void sendBroadcast(Context context, String action)
    {
        Intent ent = new Intent();
        ent.setAction(action);
        context.sendBroadcast(ent);
    }

    public static void startActivity(Context context, Class<?> target, String name, String url)
    {
        Intent intent = new Intent();
        intent.putExtra("url",url);
        intent.putExtra("name", name);
        intent.setClass(context, target);
        context.startActivity(intent);
    }

    /**
     * 是否为demo站点
     * */
    public static boolean isdemo()
    {
        return Rock.contain(APIURL, "dzjcy.dezhou.name");
    }
}