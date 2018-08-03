package com.xiajin.home.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xiajin.home.request.RequestData.Action;

import java.util.HashMap;
import java.util.Map;

public class RequestService {

	private static RequestService RequestService;
	private static  RequestQueue mQueue;
	public static  Context mcontext;
	private RequestService() {}
	public static RequestService getInstance(Context context){

		if (RequestService==null){
			RequestService = new RequestService();
			mQueue = Volley.newRequestQueue(context);
			mcontext = context;

		}
		return  RequestService;

	}

	public void post(final Action action, final HashMap<String, String> params,
					 final RequestListener handler) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				RequestData.API_URLS.get(action),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Object result = ResponseFactory.parseResponse(action,
								response);
						handler.onSuccess(action, result);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
		};

		mQueue.add(stringRequest);

	}

	public void get(final Action action, final RequestListener handler) {
		StringRequest stringRequest = new StringRequest(
				RequestData.API_URLS.get(action),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Object result = ResponseFactory.parseResponse(action,
								response);
						handler.onSuccess(action, result);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}

		});
		mQueue.add(stringRequest);
	}

	public void ImageRequest(final Action action, final String url,final RequestListener handler) {
		com.android.volley.toolbox.ImageRequest imageRequest = new com.android.volley.toolbox.ImageRequest(
				url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						handler.onSuccess(action, response);
					}
				}, 200, 200, Config.RGB_565, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// imageView.setImageResource(R.drawable.default_image);
			}
		});
		mQueue.add(imageRequest);
	}

	public void getAddHeaders(final Action action,final HashMap<String, String> Headers,
							  final RequestListener handler) {
		StringRequest stringRequest = new StringRequest(
				Request.Method.GET,
				RequestData.API_URLS.get(action),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Object result = ResponseFactory.parseResponse(action,
								response);
						handler.onSuccess(action, result);
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return Headers;
			}
		};
		mQueue.add(stringRequest);

	}

}
