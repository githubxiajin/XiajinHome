package com.xiajin.home.updata;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;


public class VersionCheck extends Service {
	private int newVerCode;
	private String newVerName;
	public static final String VERSION_TEXT = "http://download.tuanche.com/app/android/groupLeader/groupLeader.txt";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Task task = new Task();
		Log.e("service start", "start");
		task.execute();
	}

	class Task extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			return getServerVerCode();

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				int vercode = getVerCode();
				Log.d("VersionCheck", "vercode:" + vercode + ",newVerCode:"
						+ newVerCode);
				if (newVerCode > vercode) {
					updateApp();
				} else {
					VersionCheck.this.stopSelf();
				}
			} else {
				VersionCheck.this.stopSelf();
			}
		}

	}


	private void checkVersion() {
		// 版本控制
		if (getServerVerCode()) {
			int vercode = getVerCode();
			Log.d("VersionCheck", "vercode:" + vercode + ",newVerCode:"
					+ newVerCode);
			if (newVerCode > vercode) {
				updateApp();
			} else {
				this.stopSelf();
			}
		} else {
			this.stopSelf();
		}
	}

	private boolean getServerVerCode() {
		try {
			String json = excute(VERSION_TEXT); //版本升级的地址  URL.VERSION_TEXT
			Log.d("VersionCheck", "获取数据" + json);
			JSONObject obj = new JSONObject(json);
			try {
				newVerCode = obj.getInt("verCode");
				newVerName = obj.getString("verName");
				Log.d("VersionCheck", "获取数据newVerCode：" + newVerCode
						+ ",newVerName:" + newVerName);
			} catch (Exception e) {
				newVerCode = -1;
				newVerName = "";
				Log.d("VersionCheck", "获取数据错误");
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public int getVerCode() {
		int verCode = -1;
		PackageManager pm = this.getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(this.getPackageName(), 0);
			verCode = pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return verCode;
	}

	private void updateApp() {
		Intent intent = new Intent(this, UpdateVersionActivity.class);
		intent.putExtra("newVerName", newVerName);
		intent.putExtra("newVerCode", newVerCode);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		this.stopSelf();
	}

	public static String excute(String url) {
		HttpGet httpRequest = new HttpGet(url);
		StringBuffer result = new StringBuffer();
		try {
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			HttpParams httpParams = defaultClient.getParams();
			// 设置网络超时参数
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			HttpResponse response = defaultClient.execute(httpRequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"),
							8192);
					String line = null;
					while ((line = reader.readLine()) != null) {
						result.append(line);
					}
					reader.close();

				}
			}
		} catch (Exception e) {
			httpRequest.abort();
			e.printStackTrace();
		} finally {

			httpRequest.abort();
		}
		return result.toString();
	}
}
