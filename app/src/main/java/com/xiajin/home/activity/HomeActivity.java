package com.xiajin.home.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.xiajin.home.R;
import com.xiajin.home.base.BaseActivity;
import com.xiajin.home.fragment.HomeFragment;
import com.xiajin.home.fragment.MessageFragment;
import com.xiajin.home.fragment.SettingFragment;
import com.xiajin.home.fragment.MineFragment;

public class HomeActivity extends BaseActivity implements
		OnCheckedChangeListener {
	// tags
	private String tag;
	private RadioGroup radioGroup;
	private RadioButton rb_home;
	private RadioButton message;
	private RadioButton rb_borrow;
	private RadioButton rb_mine;
	private FragmentManager mManager;
	private HomeFragment homeFragment;// 首页
	private MessageFragment messageFragment;// 信息
	private SettingFragment settingFragment;// 我的mineFragment
	private MineFragment mineFragment;// 设置
	private int investNum = 0;
	private long mExitToastTime;




	private static final int GO_HOME = 100;
	private static final int GO_LOGIN = 200;
	/*
	// 定位获取当前用户的地理位置
	private LocationClient mLocationClient;

	private BaiduReceiver mReceiver;// 注册广播接收器，用于监听网络以及验证key
*/
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GO_HOME:

					startAnimActivity(HomeActivity.class);
					break;
				case GO_LOGIN:
					startAnimActivity(LoginActivity.class);
					//toast("GO_LOGIN");
					//finish();
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hometext);
		mManager = getSupportFragmentManager();
		initView();
	}


	public void initView() {
		//initbmob();
		radioGroup = (RadioGroup) this.findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
		rb_home = (RadioButton) this.findViewById(R.id.rb_home);
		message = (RadioButton) this.findViewById(R.id.message);
		rb_borrow = (RadioButton) this.findViewById(R.id.rb_borrow);
		rb_mine = (RadioButton) this.findViewById(R.id.rb_mine);
		rb_home.setChecked(true);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction transaction = mManager.beginTransaction();

		switch (checkedId) {
			case R.id.rb_home:// 首页
				if (homeFragment == null) {
					homeFragment = new HomeFragment();
				}
				transaction.replace(R.id.container, homeFragment);
				break;
			case R.id.message:// 信息
				messageFragment = new MessageFragment();
				Bundle args = new Bundle();
				args.putInt("investNum", investNum);
				messageFragment.setArguments(args);
				transaction.replace(R.id.container, messageFragment);
				break;
			case R.id.rb_mine:// 我的
				if (mineFragment == null) {
					mineFragment = new MineFragment();
				}
				transaction.replace(R.id.container, mineFragment);
				break;
			case R.id.rb_setting:// 设置
				if (settingFragment == null) {
					settingFragment = new SettingFragment();
				}
				transaction.replace(R.id.container, settingFragment);


				break;
		}
		transaction.commit();

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (userManager.getCurrentUser() != null) {
			// 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
			updateUserInfos();
			//mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_LOGIN, 1200);
		}
	}
	/**//**
	 * 开启定位，更新当前用户的经纬度坐标
	 * @Title: initLocClient
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private static long firstTime;

	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			toast("再按一次退出程序");
		}
		firstTime = System.currentTimeMillis();
	}
	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}
}
