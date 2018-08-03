package com.xiajin.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.xiajin.home.R;
import com.xiajin.home.base.BaseActivity;
import com.xiajin.home.config.Config;
import com.xiajin.home.config.CustomApplcation;
import com.xiajin.home.updata.VersionCheck;

import cn.bmob.im.BmobChat;

public class Splashctivity extends BaseActivity {
	private ImageView imageView_pic;
	private TextView textView_desc;
	private ImageView loadingimage;
	private FrameLayout load;
	/**
	 * 三个切换的动画
	 */
	private Animation mFadeIn;
	private Animation mFadeInScale;
	private Animation mFadeOut;

	/**
	 * 三个图片
	 */
	private Drawable mPicture_1;
	private Drawable mPicture_2;
	private Drawable mPicture_3;
	/*
	private static final int GO_HOME = 100;
	private static final int GO_LOGIN = 200;
*/
	// 定位获取当前用户的地理位置
	private LocationClient mLocationClient;

	private BaiduReceiver mReceiver;// 注册广播接收器，用于监听网络以及验证key
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_lens_focus);
		initView();
	}
	public void initView() {

		//checkAppVersion();// 版本升级
		if (CustomApplcation.getInstance().getSpUtil().getValueByKey("first") != null) {
			initTwo();

		} else {
			imageView_pic = (ImageView) findViewById(R.id.imageView_pic);
			textView_desc = (TextView) findViewById(R.id.textView_desc);
			init();

			setListener();
		}

	}

	private void initTwo() {
		loadingimage = (ImageView) findViewById(R.id.loadingimage);
		load = (FrameLayout) findViewById(R.id.load);
		// new MyImageLoader().display(this,loadingimage,
		// R.drawable.default_image, R.drawable.failed_image,
		// "http://www.huabian.com/uploadfile/2014/1229/20141229033639624.jpg",
		// 999, 999);  //加载网络图片资源
		loadingimage.setBackgroundResource(R.drawable.default_image);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				initbmob();
				load.setVisibility(View.GONE);

			}
		}, 2000);


	}	private void initbmob() {
		//可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试
		BmobChat.DEBUG_MODE = true;
		//BmobIM SDK初始化--只需要这一段代码即可完成初始化
		//请到Bmob官网(http://www.bmob.cn/)申请ApplicationId,具体地址:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
		BmobChat.getInstance(this).init(Config.applicationId);
		// 开启定位
		initLocClient();
		// 注册地图 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new BaiduReceiver();
		registerReceiver(mReceiver, iFilter);

		openActivity(HomeActivity.class);
		this.finish();
	/*	if (userManager.getCurrentUser() != null) {
			// 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
			updateUserInfos();
			mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
		}*/



	}@Override
	 protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	/*if (userManager.getCurrentUser() != null) {
		// 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
		updateUserInfos();
		mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
	} else {
		mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
	}*/
	}
	/**
	 * 开启定位，更新当前用户的经纬度坐标
	 * @Title: initLocClient
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initLocClient() {
		mLocationClient = CustomApplcation.getInstance().mLocationClient;
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式:高精度模式
		option.setCoorType("bd09ll"); // 设置坐标类型:百度经纬度
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms:低于1000为手动定位一次，大于或等于1000则为定时定位
		option.setIsNeedAddress(false);// 不需要包含地址信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}


	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class BaiduReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				toast("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				toast("当前网络连接不稳定，请检查您的网络设置!");
			}
		}
	}


	private void init() {
		initAnim();
		initPicture();
		/**
		 * 界面刚开始显示的内容
		 */
		imageView_pic.setImageDrawable(mPicture_1);
		textView_desc.setText("儿时的我们...");
		imageView_pic.startAnimation(mFadeIn);
	}

	/**
	 * 初始化动画
	 */
	private void initAnim() {
		mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
		mFadeIn.setDuration(1000);
		mFadeInScale = AnimationUtils.loadAnimation(this,
				R.anim.welcome_fade_in_scale);
		mFadeInScale.setDuration(4000);
		mFadeOut = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_out);
		mFadeOut.setDuration(1000);
	}

	/**
	 * 初始化图片
	 */

	private void initPicture() {
		mPicture_1 = getResources().getDrawable(R.drawable.pic_01);
		mPicture_2 = getResources().getDrawable(R.drawable.pic_02);
		mPicture_3 = getResources().getDrawable(R.drawable.pic_03);
	}

	/**
	 * 监听事件
	 */
	public void setListener() {
		/**
		 * 动画切换原理:开始时是用第一个渐现动画,当第一个动画结束时开始第二个放大动画,当第二个动画结束时调用第三个渐隐动画,
		 * 第三个动画结束时修改显示的内容并且重新调用第一个动画,从而达到循环效果
		 */
		mFadeIn.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				imageView_pic.startAnimation(mFadeInScale);
			}
		});
		mFadeInScale.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				imageView_pic.startAnimation(mFadeOut);
			}
		});
		mFadeOut.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				if (imageView_pic.getDrawable().equals(mPicture_1)) {
					textView_desc.setText("懵懂的我们...");
					imageView_pic.setImageDrawable(mPicture_2);
				} else if (imageView_pic.getDrawable().equals(mPicture_2)) {
					textView_desc.setText("少年的我们...");
					imageView_pic.setImageDrawable(mPicture_3);

					CustomApplcation.getInstance().getSpUtil().save("first", "first");
					// 第一次进入保存

					new  Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							//	checkAppVersion();// 版本升级
							Intent intent = new Intent(getApplicationContext(),
									HomeActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.fade, R.anim.hold);
							initbmob();
							Splashctivity.this.finish();
						}
					}, 3000);

				} else if (imageView_pic.getDrawable().equals(mPicture_3)) {
					textView_desc.setText("儿时的我们...");
					imageView_pic.setImageDrawable(mPicture_1);
				}
				imageView_pic.startAnimation(mFadeIn);
			}
		});

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		mPicture_1 = null;
		mPicture_2 = null;
		mPicture_3 = null;
		imageView_pic = null;
		loadingimage = null;


	}
	private void checkAppVersion() {
		Intent intent = new Intent(this, VersionCheck.class);
		startService(intent);
		Log.d("SplashActivity", "启动VersionCheck");
	}
	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}
}
