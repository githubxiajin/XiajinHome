package com.xiajin.home.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.R;

public class Opinion extends SwipeBackActivity implements OnClickListener{
	private LinearLayout submit_submit;
	private ImageView Back_option;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opinion);
		initView();
	}
	public void initView() {
		submit_submit = (LinearLayout) findViewById(R.id.submit_submit);
		submit_submit.setOnClickListener((android.view.View.OnClickListener) this);
		initTopBarForLeft("意见反馈");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.submit_submit:
				toast("提交意见");
				break;
			default:
				break;
		}
	}
	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}
}
