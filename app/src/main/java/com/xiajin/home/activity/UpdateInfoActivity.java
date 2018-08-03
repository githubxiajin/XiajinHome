package com.xiajin.home.activity;

import android.widget.EditText;

import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.R;
import com.xiajin.home.bean.User;
import com.xiajin.home.CustomView.myView.HeaderLayout.onRightImageButtonClickListener;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 设置昵称和性别
 *
 * @ClassName: SetNickAndSexActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 下午4:03:40
 */
public class UpdateInfoActivity extends SwipeBackActivity {

	EditText edit_nick;

	public void initView() {
		setContentView(R.layout.activity_set_updateinfo);
		initTopBarForBoth("修改昵称", R.drawable.base_action_bar_true_bg_selector,
				new onRightImageButtonClickListener() {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						String nick = edit_nick.getText().toString();
						if (nick.equals("")) {
							toast("请填写昵称!");
							return;
						}
						updateInfo(nick);
					}
				});
		edit_nick = (EditText) findViewById(R.id.edit_nick);
	}

	/** 修改资料
	 * updateInfo
	 * @Title: updateInfo
	 * @return void
	 * @throws
	 */
	private void updateInfo(String nick) {
		final User user = userManager.getCurrentUser(User.class);
		User u = new User();
		u.setNick(nick);
		u.setHight(110);
		u.setObjectId(user.getObjectId());
		u.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				final User c = userManager.getCurrentUser(User.class);
				toast("修改成功:"+c.getNick()+",height = "+c.getHight());
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				toast("onFailure:" + arg1);
			}
		});
	}
	@Override
	public void retry() {

	}

	@Override
	public void netError() {

	}
}

