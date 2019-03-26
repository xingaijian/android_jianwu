package com.rock.jbjianwu;

/**
 * XingAijian 2019-02-22
 * 更新apk服务器返回的Json串
 */

public class UpdateBean {
    /**
     * versionCode : 1
     * versionName : 2.0
     * apkurl : http://oa.dzjcy.gov.cn/dzjcy.apk
     * message : 1.增加聊天提醒功能 2.修改部分视图
     */

    private String versionCode;
    private String versionName;
    private String apkurl;
    private String message;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
