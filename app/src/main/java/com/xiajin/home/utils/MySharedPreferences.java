package com.xiajin.home.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class MySharedPreferences {
	private SharedPreferences mSharedPreferences;
	private static Editor editor;

	public MySharedPreferences(Context context, String name) {
		mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}
	private String SHARED_KEY_NOTIFY = "shared_key_notify";
	private String SHARED_KEY_VOICE = "shared_key_sound";
	private String SHARED_KEY_VIBRATE = "shared_key_vibrate";

	public static final String PREFERNCE_FILE_NAME = "obj"; // 缓存文件名


	// 是否允许推送通知
		public boolean isAllowPushNotify() {
			return mSharedPreferences.getBoolean(SHARED_KEY_NOTIFY, true);
		}

		public void setPushNotifyEnable(boolean isChecked) {
			editor.putBoolean(SHARED_KEY_NOTIFY, isChecked);
			editor.commit();
		}

		// 允许声音
		public boolean isAllowVoice() {
			return mSharedPreferences.getBoolean(SHARED_KEY_VOICE, true);
		}

		public void setAllowVoiceEnable(boolean isChecked) {
			editor.putBoolean(SHARED_KEY_VOICE, isChecked);
			editor.commit();
		}

		// 允许震动
		public boolean isAllowVibrate() {
			return mSharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
		}

		public void setAllowVibrateEnable(boolean isChecked) {
			editor.putBoolean(SHARED_KEY_VIBRATE, isChecked);
			editor.commit();
		}
	

	public static void save(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public  String getValueByKey( String key) {
		String value = mSharedPreferences.getString(key, null);
		return value;

	}

	public static void remove(String key) {
		editor.remove(key);
		editor.commit();
	}
	/**
	 * @param context
	 * @return
	 */
	public static Object readObj(Context context, String key) {
		Object obj = null;
		SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
		String replysBase64 = prefe.getString(key, "");
		if (TextUtils.isEmpty(replysBase64)) {
			return obj;
		}
		// 读取字节
		byte[] base64 = Base64.decodeBase64(replysBase64.getBytes());
		// 封装到字节读取流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 封装到对象读取流
			ObjectInputStream ois = new ObjectInputStream(bais);
			try {
				// 读取对象
				obj = ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * 存储一个对象
	 *
	 * @param context
	 * @param
	 * @param key
	 */
	public static <T> void saveObj(Context context, T obj, String key) {
		T _obj = obj;

		SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流,封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(_obj);
			// 将字节流编码成base64的字符串
			String list_base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = prefe.edit();
			editor.putString(key, list_base64);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
