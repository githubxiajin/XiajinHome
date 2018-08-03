package com.xiajin.home.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.xiajin.home.R;
import com.xiajin.home.activity.AddFriendActivity;
import com.xiajin.home.activity.NewFriendActivity;
import com.xiajin.home.adapter.MyFragmentPagerAdapter;
import com.xiajin.home.base.BaseFragment;
import com.xiajin.home.config.CustomApplcation;
import com.xiajin.home.receiver.MyMessageReceiver;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.inteface.EventListener;

public class MineFragment extends BaseFragment implements EventListener,OnClickListener {
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView barText;
	private TextView view1, view2;
	private int currIndex;// 当前页卡编号
	private ContactFragment contactFragment;
	private RecentFragment recentFragment;
  private ImageView addfriend;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_setting, null);

		return view;

	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		mPager = (ViewPager) this.findViewById(R.id.my_setting_viewpage);
		view1 = (TextView) this.findViewById(R.id.tv_guid1);
		view2 = (TextView) this.findViewById(R.id.tv_guid2);
		addfriend = (ImageView) this.findViewById(R.id.addfriend);
		addfriend.setOnClickListener(this);
		view1.setOnClickListener(new txListener(0));
		view2.setOnClickListener(new txListener(1));

	}

	public class txListener implements OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化图片的位移像素
	 */
	public void InitTextBar() {
		barText = (TextView) super.findViewById(R.id.cursor);
		Display display = getActivity().getWindow().getWindowManager()
				.getDefaultDisplay();
		// 得到显示屏宽度
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		// 1/3屏幕宽度
		int tabLineLength = metrics.widthPixels / 2;
		LayoutParams lp = (LayoutParams) barText.getLayoutParams();
		lp.width = tabLineLength;
		barText.setLayoutParams(lp);

	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {

	

		// setRetainInstance(true); 不能添加
if (!(null==fragmentList)&&fragmentList.size()>=2) {
	
}else {
	fragmentList = new ArrayList<Fragment>();
	 contactFragment = new ContactFragment();
	 recentFragment = new RecentFragment();
	 fragmentList.add(recentFragment);
		fragmentList.add(contactFragment);
}
		

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			// 取得该控件的实例
			LayoutParams ll = (LayoutParams) barText
					.getLayoutParams();

			if (currIndex == arg0) {
				ll.leftMargin = (int) (currIndex * barText.getWidth() + arg1
						* barText.getWidth());
			} else if (currIndex > arg0) {
				ll.leftMargin = (int) (currIndex * barText.getWidth() - (1 - arg1)
						* barText.getWidth());
			}
			barText.setLayoutParams(ll);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			currIndex = arg0;
			int i = currIndex + 1;
			// ShowToast("您选择了第"+i+"个页卡");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initNewMessageBroadCast();
		initTagMessageBroadCast();
		InitTextView();
		InitTextBar();
		InitViewPager();

	}
	@Override
	public void onAddUser(BmobInvitation message) {
		refreshInvite(message);

	}

	@Override
	public void onMessage(BmobMsg message) {
		refreshNewMsg(message);

	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		if (isNetConnected) {
			toast("当前网络不可用,请检查您的网络!");
		}
	}

	@Override
	public void onOffline() {
		showOfflineDialog(context);

	}

	@Override
	public void onReaded(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	private void refreshNewMsg(BmobMsg message) {
		// 声音提示
		boolean isAllow = CustomApplcation.getInstance().getSpUtil()
				.isAllowVoice();
		if (isAllow) {
			CustomApplcation.getInstance().getMediaPlayer().start();
		}
		// iv_recent_tips.setVisibility(View.VISIBLE);
		// 也要存储起来
		if (message != null) {
			BmobChatManager.getInstance(context).saveReceiveMessage(true,
					message);
		}
		if (currIndex == 0) {
			// 当前页面如果为会话页面，刷新此页面
			if (recentFragment != null) {
				 recentFragment.refresh();
			}
		}
	}

	private void refreshInvite(BmobInvitation message) {
		boolean isAllow = CustomApplcation.getInstance().getSpUtil()
				.isAllowVoice();
		if (isAllow) {
			CustomApplcation.getInstance().getMediaPlayer().start();
		}
		// iv_contact_tips.setVisibility(View.VISIBLE);
		if (currIndex == 1) {
			if (contactFragment != null) {
				contactFragment.refresh();
			}
		} else {
			// 同时提醒通知
			String tickerText = message.getFromname() + "请求添加好友";
			boolean isAllowVibrate = CustomApplcation.getInstance().getSpUtil()
					.isAllowVibrate();
			BmobNotifyManager.getInstance(context).showNotify(isAllow,
					isAllowVibrate, R.drawable.ic_launcher, tickerText,
					message.getFromname(), tickerText.toString(),
					NewFriendActivity.class);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MyMessageReceiver.ehList.add(this);// 监听推送的消息
		// 清空
		MyMessageReceiver.mNewNum = 0;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyMessageReceiver.ehList.remove(this);// 取消监听推送的消息
		//mPager.setCurrentItem(0);//foucebaocuo  zheshi bug
	}

	NewBroadcastReceiver newReceiver;

	private void initNewMessageBroadCast() {
		// 注册接收消息广播
		newReceiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				BmobConfig.BROADCAST_NEW_MESSAGE);
		// 优先级要低于ChatActivity
		intentFilter.setPriority(3);
		context.registerReceiver(newReceiver, intentFilter);
	}

	/**
	 * 新消息广播接收者
	 * 
	 */
	private class NewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 刷新界面
			refreshNewMsg(null);
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}

	TagBroadcastReceiver userReceiver;

	private void initTagMessageBroadCast() {
		// 注册接收消息广播
		userReceiver = new TagBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				BmobConfig.BROADCAST_ADD_USER_MESSAGE);
		// 优先级要低于ChatActivity
		intentFilter.setPriority(3);
		context.registerReceiver(userReceiver, intentFilter);
	}

	/**
	 * 标签消息广播接收者
	 */
	private class TagBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			BmobInvitation message = (BmobInvitation) intent
					.getSerializableExtra("invite");
			refreshInvite(message);
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addfriend:
			startAnimActivity(AddFriendActivity.class);
			break;

		default:
			break;
		}
		
	}

}
