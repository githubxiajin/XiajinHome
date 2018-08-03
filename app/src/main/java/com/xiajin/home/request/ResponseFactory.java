package com.xiajin.home.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiajin.home.bean.Bdweather;
import com.xiajin.home.request.RequestData.Action;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 这个类是获取API请求内容的工厂方
 */
public class ResponseFactory {
	Object result = null;

	public static Object parseResponse(Action action, String info) {
		Object result = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		if (info == null) {
			return result;
		}
		switch (action) {
			case LOANS: // example
				try {
					JSONObject json = new JSONObject(info);
					result = json;

				} catch (JSONException e) {
					result = "";
					e.printStackTrace();
				}

			/*
			 * try {ASKQuestionsCommitResult aSKQuestionsCommitResult =
			 * gson.fromJson( info.toString(), ASKQuestionsCommitResult.class);
			 * 
			 * result = aSKQuestionsCommitResult; } catch (Exception e) {
			 * System.out.println("数据解析失败"); }
			 */
				break;
			case WEATHER:
				try {
					Bdweather weather = gson.fromJson(info.toString(),
							Bdweather.class);
					result = weather;
				} catch (Exception e) {
					System.out.println("数据解析失败");
				}

				break;
			default:
				break;
		}

		return result;
	}
}
