package com.rock.jbjianwu;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.CDate;
import com.lib.Rock;
import com.lib.RockActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.view.ImageViewXinhu;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends RockActivity implements EasyPermissions.PermissionCallbacks {

	private String versionCode = "";
	private String versionName = "";
	private String versionUrl = "";
	private String versionMessage = "";
	private String[] permissions = {Manifest.permission.CALL_PHONE,Manifest.permission.INTERNET,Manifest.permission.WAKE_LOCK,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.VIBRATE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.WRITE_SETTINGS,Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.VIBRATE,Manifest.permission.SYSTEM_ALERT_WINDOW,
			Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,Manifest.permission.GET_TASKS,Manifest.permission.REQUEST_INSTALL_PACKAGES,
			Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.CHANGE_NETWORK_STATE};
	/**
	 * 初始化
	 * */
	protected void initCreate() {
		setContentView(R.layout.activity_main);
		Xinhu.APIURL    = Sqlite.getOption("apiurl", Xinhu.APIURL);

		String appname	= Rock.getstring(this, R.string.appname);

		String apptitle = Sqlite.getOption("apptitle", appname);

		TextView at = (TextView) findViewById(R.id.appnameval);
		at.setText(apptitle);
		setface();


		String footer = "Copyright ©"+ CDate.now("Y","")+" "+Rock.getstring(this, R.string.appname)+" "+Rock.getstring(this, R.string.appgwurl)+"";
		at = (TextView) findViewById(R.id.banquan);
		at.setText(footer);

		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (EasyPermissions.hasPermissions(this, permissions)) {
			//拥有权限进行操作
			upapk();
		} else {
			//没有权限进行提示且申请权限
			EasyPermissions.requestPermissions(this,"应用需要权限，请允许", 0, permissions);
		}
	}

	protected  void handleCallback(Message msg, String bstr){
		if(msg.what==15){
			openmain();
		}
	}

	private void openmain()
	{
		String uid = Sqlite.getOption("adminid");
		if(!Rock.isEmpt(uid)){
			startSimpleActivity(IndexActivity.class, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}else {
			startSimpleActivity(LoginActivity.class);
		}
		finish();
	}

	private void setface()
	{
		ImageViewXinhu imga = (ImageViewXinhu)findViewById(R.id.myicons);
		imga.setPath("images/logo.png");
	}

	/**
	 * 1.获取版本信息
	 */
	private void upapk(){
		OkGo.<String>get(UrlUtil.UPAPK)
				.tag(this)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(Response<String> response) {
						UpdateBean updateBean = FastJsonUtils.getObject(response.body(),UpdateBean.class);
						versionCode = updateBean.getVersionCode();
						versionName = updateBean.getVersionName();
						versionUrl = updateBean.getApkurl();
						versionMessage = updateBean.getMessage();

						if (updateJudge()){
							dialogUp();
						}else {
							RunTimer(15, 2000);//启动页面1秒就好了
						}
					}
				});
	}

	/**
	 * 2.判断是否更新
	 */
	private boolean updateJudge(){
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(),0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(versionCode) > packInfo.versionCode ){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 3.弹窗更新提示
	 */
	private void dialogUp(){
		AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
//		alertDialogBuilder.setIcon(R.drawable.)
		alertDialogBuilder.setTitle("版本更新");
		alertDialogBuilder.setMessage(versionMessage);
		alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				//更新应用提醒
				ProgressDialog progressDialog;
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("软件更新中，请稍等~");
				progressDialog.setCancelable(true);
				progressDialog.show();

				getFile();
			}
		});

		alertDialogBuilder.setCancelable(false);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();//将dialog显示出来
	}

	/**
	 * 下载apk
	 */
	private void getFile(){

		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download";
		final String fileName = "dzjcy.apk";
		final File file = new File(filePath + "/" + fileName);
		if (file.exists()){
			file.delete();
		}
		OkGo.<File>get(versionUrl)
				.tag(this)
				.execute(new FileCallback(filePath, fileName) {
					@Override
					public void onStart(Request<File, ? extends Request> request) {
						super.onStart(request);
						Log.e("MainActivity", "开始下载" );
					}

					@Override
					public void onSuccess(Response<File> response) {
						Log.e("MainActivity", "file--" + file);
						openFile(file);
					}

					@Override
					public void onError(Response<File> response) {
						super.onError(response);
						Log.e("MainActivity", "onError: "+response.message());
					}

					@Override
					public void downloadProgress(Progress progress) {
						super.downloadProgress(progress);
						Log.e("MainActivity", "progress" + progress.fraction * 100);
					}
				});
	}

	//打开APK程序代码
	private void openFile(File file) {

		//Android 7.0及以上
		if (Build.VERSION.SDK_INT >= 24) {
            boolean hasInstallPermission = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                hasInstallPermission = this.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6666);
                }
            }
			Log.e("MainActivity", ">=24" );
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri apkUri = FileProvider.getUriForFile(this, "com.dzjcy.provider", file);
			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
			this.startActivity(intent);
			finish();
		} else {
			Log.e("MainActivity", "小于24" );
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			this.startActivity(intent);
			finish();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		//把申请权限的回调交由EasyPermissions处理
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
		//申请成功进行的操作
		upapk();
	}

	@Override
	public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
		//申请失败进行的操作
		EasyPermissions.requestPermissions(this,"应用需要权限，请允许", 0, permissions);
	}
}
