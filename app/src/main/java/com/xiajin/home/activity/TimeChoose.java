package com.xiajin.home.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.CustomView.ToggleButton.ToggleButton;
import com.xiajin.home.CustomView.ToggleButton.ToggleButton.OnToggleChanged;
import com.xiajin.home.R;
import com.xiajin.home.R.id;
import com.xiajin.home.utils.MySharedPreferences;

public class TimeChoose extends SwipeBackActivity implements OnClickListener {
	private ToggleButton mtogglebutton;
	private ToggleButton mtogglebutton_vibrate;// 震动
	private ToggleButton mtogglebutton_voice;// 声音
	//private Boolean isViewShow = true;//

	private RelativeLayout rl_vibrate;
	private RelativeLayout rl_voice;
	boolean isAllowNotify;//允许通知
	boolean isAllowVoice;//允许声音
	boolean isAllowVibrate ;//允许震动
	
	
	MySharedPreferences mSharedUtil;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_time_choose);
	mSharedUtil = mApplication.getSpUtil();
	getViews();
	 initView();
	
}
	public void initView() {
		mtogglebutton.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {

				if (on) {
					mSharedUtil.setPushNotifyEnable(true);
					rl_vibrate.setVisibility(View.VISIBLE);
					rl_voice.setVisibility(View.VISIBLE);
					if (isAllowVibrate) {
						mtogglebutton_vibrate.setToggleOn();
					}
					if (isAllowVoice) {
						mtogglebutton_voice.setToggleOn();
					}
					
				} else {
					mSharedUtil.setPushNotifyEnable(false);
					rl_vibrate.setVisibility(View.GONE);
					rl_voice.setVisibility(View.GONE);
				}

			}
		});

		mtogglebutton_vibrate.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(boolean on) {
				if (on) {
					
					mSharedUtil.setAllowVibrateEnable(true);
				}else {
					mSharedUtil.setAllowVibrateEnable(false);
				}
				
			}
		});
		
		mtogglebutton_voice.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(boolean on) {
				if (on) {
					mSharedUtil.setAllowVoiceEnable(true);
				}else {
					mSharedUtil.setAllowVoiceEnable(false);
				}
				
				
			}
		});
		
	}

	public void getViews() {
		initTopBarForLeft("免打扰");
		// 初始化
		isAllowNotify = mSharedUtil.isAllowPushNotify();
		isAllowVoice = mSharedUtil.isAllowVoice();
		isAllowVibrate = mSharedUtil.isAllowVibrate();
		mtogglebutton = (ToggleButton) findViewById(id.mtogglebutton);
		mtogglebutton_vibrate = (ToggleButton) findViewById(id.mtogglebutton_vibrate);
		mtogglebutton_voice = (ToggleButton) findViewById(id.mtogglebutton_voice);
		rl_vibrate = (RelativeLayout) findViewById(id.rl_vibrate);
		rl_voice = (RelativeLayout) findViewById(id.rl_voice);

		if (isAllowNotify) {// 如果
			mtogglebutton.setToggleOn();
			rl_vibrate.setVisibility(View.VISIBLE);
			rl_voice.setVisibility(View.VISIBLE);
		}
		if (isAllowVibrate) {
			mtogglebutton_vibrate.setToggleOn();
		}
		if (isAllowVoice) {
			mtogglebutton_voice.setToggleOn();
		}

	}

	@Override
	public void onClick(View v) {

	}
	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}
}
