package com.xiajin.home.request;

import android.content.Context;

import java.util.HashMap;

public class RequestData {
	private static RequestData RequestData;
	private RequestData (){}
	public static  RequestData getInstance(){
		if (RequestData==null){
			RequestData = new RequestData();
		}
		return  RequestData;
	}
	public static enum Action {
		/**
		 * 品牌列表
		 * */

		LOANS, TESTIMAGE, WEATHER

	}

	public static final String WEATHERKEY = "bdb45751e18f7063c4a7f7dee7d10452";
	public static final HashMap<Action, String> API_URLS = new HashMap<Action, String>() {
		private static final long serialVersionUID = -8469661978245513712L;
		{
			put(Action.WEATHER,
					"http://apis.baidu.com/apistore/weatherservice/weather?+citypinyin=dezhou");
			put(Action.LOANS, "http://www.vobank.com/mobileLoanList");
			put(Action.TESTIMAGE,
					"http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");

		}
	};

	public static void getloans(Context context, String loanProductType,
								String currentPage, RequestListener handler) {
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("loanProductType", loanProductType);
		params.put("currentPage", currentPage);
		RequestService.getInstance(context).post(Action.LOANS, params, handler);
	}

	public static void image(Context context,String url, RequestListener handler) {
		RequestService.getInstance(context).ImageRequest(Action.TESTIMAGE, url, handler);
	}

	public static void getweather(Context context, RequestListener handler) {
		final HashMap<String, String> Headers = new HashMap<String, String>();
		Headers.put("apikey", WEATHERKEY);
		RequestService.getInstance(context).getAddHeaders(Action.WEATHER, Headers,
				handler);
	}

}
