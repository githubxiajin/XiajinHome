package com.xiajin.home.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;

import android.util.Log;

public class MD5Utils {
	public static String getToken(List<NameValuePair> listp) {
		String ss = "";
		if (listp != null && !listp.isEmpty()) {
			// 参数排序
			Collections.sort(listp, new Comparator<NameValuePair>() {
				@Override
				public int compare(NameValuePair o1, NameValuePair o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			for (int i = 0; i < listp.size(); i++) {
				if ("".equals(ss)) {
					ss += listp.get(i).getName() + "=" + (listp.get(i).getValue() == null ? "" : listp.get(i).getValue());
				} else {
					ss += "&" + listp.get(i).getName() + "=" + (listp.get(i).getValue() == null ? "" : listp.get(i).getValue());
				}
			}
		}
		return toMD5(ss + "@#@#$#)$#(@(#$#@");
	}

	public static String toMD5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		md.update(str.getBytes());
		byte[] encodedPassword = md.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < encodedPassword.length; i++) {

			if ((encodedPassword[i] & 0xff) < 0x10) {
				sb.append("0");
			}
			sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return sb.toString();
	}

	public static String getMD5(String plainText) {
		String md5 = null;
			try {
				if (plainText != null) {
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(plainText.getBytes());
					byte b[] = md.digest();
					StringBuffer buf = new StringBuffer("");
					for (int offset = 0; offset < b.length; offset++) {
						int i = b[offset];
						if (i < 0)
							i += 256;
						if (i < 16)
							buf.append("0");
						buf.append(Integer.toHexString(i));
					}
					md5 = buf.toString();
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			Log.i("jianian", md5);
		return md5;
	}

}
