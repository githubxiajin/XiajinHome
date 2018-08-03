package com.xiajin.home.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * 常用工具类（如：网络是否可用等）
 * 
 * 
 * 
 */
public class CommonUtils {

	private static final String TAG = "CommonUtils";
	private static String timeStamp;

	// 获取AppKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	
	/**
	 * 判断是否�?3G网络
	 * @param context
	 * @return
	 */
	public static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		    switch (telephonyManager.getNetworkType()) {
		        case TelephonyManager.NETWORK_TYPE_1xRTT:
		            return false; // ~ 50-100 kbps
		        case TelephonyManager.NETWORK_TYPE_CDMA:
		            return false; // ~ 14-64 kbps
		        case TelephonyManager.NETWORK_TYPE_EDGE:
		            return false; // ~ 50-100 kbps
		        case TelephonyManager.NETWORK_TYPE_EVDO_0:
		            return true; // ~ 400-1000 kbps
		        case TelephonyManager.NETWORK_TYPE_EVDO_A:
		            return true; // ~ 600-1400 kbps
		        case TelephonyManager.NETWORK_TYPE_GPRS:
		            return false; // ~ 100 kbps
		        case TelephonyManager.NETWORK_TYPE_HSDPA:
		            return true; // ~ 2-14 Mbps
		        case TelephonyManager.NETWORK_TYPE_HSPA:
		            return true; // ~ 700-1700 kbps
		        case TelephonyManager.NETWORK_TYPE_HSUPA:
		            return true; // ~ 1-23 Mbps
		        case TelephonyManager.NETWORK_TYPE_UMTS:
		            return true; // ~ 400-7000 kbps
		        case TelephonyManager.NETWORK_TYPE_EHRPD:
		            return true; // ~ 1-2 Mbps
		        case TelephonyManager.NETWORK_TYPE_EVDO_B:
		            return true; // ~ 5 Mbps
		        case TelephonyManager.NETWORK_TYPE_HSPAP:
		            return true; // ~ 10-20 Mbps
		        case TelephonyManager.NETWORK_TYPE_IDEN:
		            return false; // ~25 kbps
		        case TelephonyManager.NETWORK_TYPE_LTE:
		            return true; // ~ 10+ Mbps
		        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
		            return false;
		        default:
		            return false;
		        }
		    }
	
	/**
     * 获取网络连接方式
     * @param context 上下�?
     */
    public static String getNetWorkType(Context context) {
    	String netType = "";
       try {
    	   ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo networkInfo = manager.getActiveNetworkInfo();
           if (networkInfo != null && networkInfo.isConnected()) {
               String type = networkInfo.getTypeName();
               if (type.equalsIgnoreCase("WIFI")) {
               	netType = "WIFI";
               } else if (type.equalsIgnoreCase("MOBILE")) {
               	if(isFastMobileNetwork(context)){
               		netType = "M-3G";
               	}else{
               		netType = "M-2G";
               	}
               }
           } else {
           	
           }
		} catch (Exception e) {
			e.printStackTrace();
		}
        LogUtils.i("网络连接方式�?"+netType);
        return netType;
    }
    public static int dp2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    } 
    public static String getIEME(Context context) {
		String str = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return str;
		
	}
	public static long GetTimeStamp(){
		return System.currentTimeMillis();
		}
	
	
	


	/**
	 * long类型时间格式�?
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * 判断是否为合法的json
	 * 
	 * @param jsonContent
	 *            �?判断的字�?
	 */
	public static boolean isJsonFormat(String jsonContent) {
		try {
			new JsonParser().parse(jsonContent);
			return true;
		} catch (JsonParseException e) {
			return false;
		}
	}

	/**
	 * 格式化时�?
	 * 
	 * @param seconds
	 * @return
	 */
	public static String formatTime(int seconds) {
		StringBuilder time = new StringBuilder();

		int day = seconds / (24 * 60 * 60);
		int remainder = seconds - day * 24 * 60 * 60;

		int hours = remainder / (60 * 60);
		remainder = remainder - hours * 60 * 60;

		int minutes = remainder / 60;

		remainder = remainder - minutes * 60;

		int second = remainder;

		if (seconds == 0) {
			time.append("0�?0�?0�?");
			return time.toString();
		}

		if (day > 0) {
			time.append(day + "�?");
		}
		if (hours > 0) {
			time.append(hours + "�?");
		}
		if (minutes > 0) {
			time.append(minutes + "�?");
		}

		return time.toString();
	}

	/**
	 * 格式化距�?
	 * 
	 * @param distance
	 * @return
	 */
	public static String formatDistance(double distance) {
		DecimalFormat df = new DecimalFormat("0.00");
		String d = "";
		if (distance >= 1000.0) {
			d = df.format(distance / 1000.0) + "km";
		} else {
			d = distance + "m";
		}
		return d;
	}

	/**
	 * 显示进度
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param indeterminate
	 * @param cancelable
	 * @return
	 */
	public static ProgressDialog showProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);
		// dialog.setDefaultButton(false);
		dialog.show();
		return dialog;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView,
			boolean addHeight, float density) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (null != listItem) {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		if (addHeight) {
			if (density < 1.0f) {
				params.height += (int) (density * 30 + 0.5f);
			} else {
				params.height += (int) (density * 8 + 0.5f);
			}
		} else {
			if (density >= 1.0f && density <= 1.5f) {
				params.height += (int) (density * 10 + 0.5f);
			}
			if (density < 1.0f) {
				params.height += (int) (density * 30 + 0.5f);
			}
			if (density >= 2.0f) {
				params.height += (int) (density * 6 + 0.5f);
			}
		}

		listView.setLayoutParams(params);
	}

	/**
	 * 测量ListView的高�?
	 * 
	 * @param listView
	 * @return
	 */
	public static int measureListViewHeight(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽�?
			totalHeight += listItem.getMeasuredHeight(); // 统计�?有子项的总高�?
		}
		int height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高�?
		// params.height�?后得到整个ListView完整显示�?要的高度
		return height;
	}

	/**
	 * 根据手机的分辨率�? dp 的单�? 转成�? px(像素)
	 */
	public static int dip2px(float density, float dpValue) {
		return (int) (dpValue * density + 0.5f);
	}

	/**
	 * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取格式化的时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateFormatString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(new Date(time));
	}

	/**
	 * 字符串反�?
	 */
	public static String reverse(String s) {
		int length = s.length();
		if (length <= 1)
			return s;
		String left = s.substring(0, length / 2);
		String right = s.substring(length / 2, length);
		return reverse(right) + reverse(left);
	}

	

	/**
	 * �?启activity
	 */
	public static void launchActivity(Context context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}

	public static void launchActivityForResult(Activity context,
			Class<?> activity, int requestCode) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivityForResult(intent, requestCode);
	}


	/**
	 * 格式化金�?
	 */
	public static String formatMoney(String money) {
		if (TextUtils.isEmpty(money)) {
			return "";
		}
		int dotIndex = 0;
		// �?3.0 或�??3.00
		if (money.endsWith(".0") || money.endsWith(".00")) {
			dotIndex = money.indexOf(".");
			money = money.substring(0, dotIndex);
		}
		// �?3.30
		if (money.endsWith("0") && money.contains(".")) {
			money = money.substring(0, money.lastIndexOf("0"));
		}
		return money;
	}

	
	/**
	 * 处，每隔四位来一个空格显�?
	 * @param code
	 * @return
	 */
	public static String dealLashouCode(String code){
		if(TextUtils.isEmpty(code)){
			return "";
		}else{
			StringBuffer sb = new StringBuffer();
			char[] charArray = code.toCharArray();
			for(int index=0;index<charArray.length;index++){
				if(index%4==0 && index!=0)
					sb.append(" ");
				sb.append(charArray[index]);
			}
			return sb.toString();
		}
		
	}
	/**根据字符串获取时间戳
	 * @param time
	 * @return
	 */
	public static String getTimeStamp(String time){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		Date d;
		try{
			d=sdf.parse(time);
			long l=d.getTime();
			String strTime=String.valueOf(l);
			timeStamp = strTime.substring(0,10);
		}catch(ParseException e){
			e.printStackTrace();
		}
		return timeStamp;
	} 
	/**获取时间标签数字
	 * @param timeStamp
	 * @return
	 */
	public static char timeSignNum(String timeStamp){
		
		/*char d1=timeStamp.charAt(timeStamp.length()-1);
		char d2=timeStamp.charAt(timeStamp.length()-2);
		char d3=timeStamp.charAt(timeStamp.length()-3);
		char d4=timeStamp.charAt(timeStamp.length()-4);*/
	//	int index=(Integer.parseInt(d1)+Integer.parseInt(d2)+Integer.parseInt(d3)+Integer.parseInt(d4))%10;
		
		long time=Long.parseLong(timeStamp);
		long d1=time%10;
		long d2=time/10%10;
		long d3=time/100%10;
		long d4=time/1000%10 ;
		
		int index=(int) ((d1+d2+d3+d4)%10);
		char str=timeStamp.charAt(index);
		return str;
	}
	/** 检查是否有网络 */
	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			return info.isAvailable();
		}
		return false;
	}

	/** 检查是否是WIFI */
	public static boolean isWifi(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;
	}

	/** 检查是否是移动网络 */
	public static boolean isMobile(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}

	private static NetworkInfo getNetworkInfo(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/** 检查SD卡是否存在 */
	public static boolean checkSdCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
}
