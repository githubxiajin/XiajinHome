package com.xiajin.home.updata;

import java.io.File;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class AppInstallActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finish();
		/*
		 * Intent intent=new Intent("closeNotification"); sendBroadcast(intent);
		 * installApk();
		 */
	}

	public void installApk() {
		File filePath = new File("/sdcard/xiajin/apk/");//public static final String APK_PATH = "/sdcard/groupLeader/apk/";
		String[] files = filePath.list();
		if (files == null || files.length != 1) {
			Toast.makeText(this, "文件错误,请从新下载安装!", Toast.LENGTH_SHORT);
		} else {
			String fileName = null;
			for (String name : files) {
				fileName = name;
			}
			File appFile = new File("/sdcard/xiajin/apk/" + fileName);
			if (appFile.exists()) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(appFile),
						"application/vnd.android.package-archive");
				startActivity(intent);
			}
		}
		finish();
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}
}
