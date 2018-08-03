package com.xiajin.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiajin.home.CustomView.CircleImageView;
import com.xiajin.home.CustomView.PullLayout;
import com.xiajin.home.R;
import com.xiajin.home.activity.AboutOus;
import com.xiajin.home.activity.BlackListActivity;
import com.xiajin.home.activity.LoginActivity;
import com.xiajin.home.activity.MyAskQuestion;
import com.xiajin.home.activity.Opinion;
import com.xiajin.home.activity.TimeChoose;
import com.xiajin.home.base.BaseFragment;
import com.xiajin.home.bean.Bdweather;
import com.xiajin.home.bean.Bdweather.RetData;
import com.xiajin.home.bean.User;
import com.xiajin.home.config.BmobConstants;
import com.xiajin.home.config.CustomApplcation;
import com.xiajin.home.request.RequestData;
import com.xiajin.home.request.RequestData.Action;
import com.xiajin.home.request.RequestListener;
import com.xiajin.home.request.MyImageLoader;
import com.xiajin.home.utils.PhotoUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class SettingFragment extends BaseFragment implements OnClickListener,
		RequestListener {
	private TextView nick_name;
	private TextView tv_set_name;
	private LinearLayout my_questions;
	private LinearLayout choose_time;
	private LinearLayout About_us;
	private LinearLayout opinion;
	private LinearLayout exit_login;
	private LinearLayout check_the_update;
	private LinearLayout blacklist;
	private PullLayout mine_layout_all;
	private ImageView icon_sex;
	private ImageView weather_ima;//天气图片
	private ImageView sun_img;
	private TextView weather_tex;
	private TextView sun_tex;
	private RelativeLayout ll_weather;
	private TextView tv;

	private Boolean isLogin = true;// test 登入状态
	private CircleImageView Head_portrait_information;
	private User user;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_mine, null);
		
		getViews(view);
		setViews();
		setListeners();
		return view;
	}

	private void setListeners() {
		my_questions.setOnClickListener(this);
		choose_time.setOnClickListener(this);
		About_us.setOnClickListener(this);
		opinion.setOnClickListener(this);
		exit_login.setOnClickListener(this);
		nick_name.setOnClickListener(this);
		check_the_update.setOnClickListener(this);
		blacklist.setOnClickListener(this);
		Head_portrait_information.setOnClickListener(this);
	}

	private void setViews() {
		// 因为魅族手机下面有三个虚拟的导航按钮，需要将其隐藏掉，不然会遮掉拍照和相册两个按钮，且在setContentView之前调用才能生效
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= 14) {
					getActivity().getWindow().getDecorView().setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
				}
		
		
		if (!isLogin) {
			my_questions.setVisibility(View.GONE);
			choose_time.setVisibility(View.GONE);
			exit_login.setVisibility(View.GONE);
			tv_set_name.setVisibility(View.GONE);
			check_the_update.setVisibility(View.GONE);
			Head_portrait_information.setImageResource(R.drawable.un_login);
			nick_name.setText("点击登入");
		} else {
			getData();// 如果用户登入请求用户信息
			my_questions.setVisibility(View.VISIBLE);
			choose_time.setVisibility(View.VISIBLE);
			exit_login.setVisibility(View.VISIBLE);
			tv_set_name.setVisibility(View.VISIBLE);
			check_the_update.setVisibility(View.VISIBLE);

		}

	}

	private void getData() {
		user = userManager.getCurrentUser(User.class);
		String user_id = "1";
		String start_time = "11:55";
		String end_time = "18:96";

		nick_name.setText(user.getNick());
		tv_set_name.setText(user.getUsername());

		
		icon_sex.setVisibility(View.VISIBLE);
		// 设置sex图标
		if (null!=user.getSex()) {
			if (user.getSex()) {// true是男
				icon_sex.setBackgroundResource(R.drawable.boy);
				Head_portrait_information.setImageResource(R.drawable.touxaingceshi2);
			} else {
				Head_portrait_information.setImageResource(R.drawable.touxaingceshi);
				icon_sex.setBackgroundResource(R.drawable.icon_girl);
			}
		}if (null!=user.getAvatar()) {
			MyImageLoader.getInstance(getActivity()).display(Head_portrait_information, 0, 0, user.getAvatar(), 80, 80);
		}
		
		
		
	}
@Override
public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	
}


	private void getViews(View view) {
		tv =(TextView) view.findViewById(R.id.tv);
		nick_name = (TextView) view.findViewById(R.id.nick_name);
		tv_set_name = (TextView) view.findViewById(R.id.tv_set_name);
		my_questions = (LinearLayout) view.findViewById(R.id.my_questions);
		choose_time = (LinearLayout) view.findViewById(R.id.choose_time);
		About_us = (LinearLayout) view.findViewById(R.id.About_us);
		opinion = (LinearLayout) view.findViewById(R.id.opinion);
		check_the_update = (LinearLayout) view
				.findViewById(R.id.check_the_update);
		mine_layout_all = (PullLayout)view. findViewById(R.id.mine_layout_all);
		blacklist = (LinearLayout) view.findViewById(R.id.blacklist);
		exit_login = (LinearLayout) view.findViewById(R.id.exit_login);
		Head_portrait_information = (CircleImageView) view
				.findViewById(R.id.Head_portrait_information);
		icon_sex = (ImageView) view.findViewById(R.id.icon_sex);

		weather_ima = (ImageView) view.findViewById(R.id.weather_ima);
		sun_img = (ImageView) view.findViewById(R.id.sun_img);
		weather_tex = (TextView) view.findViewById(R.id.weather_tex);
		sun_tex = (TextView) view.findViewById(R.id.sun_tex);
		ll_weather = (RelativeLayout) view.findViewById(R.id.ll_weather);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	
		RequestData.getInstance().getweather(context, this); // 百度天气请求
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.my_questions:
			intent = new Intent(context, MyAskQuestion.class);
			intent.putExtra("from","MYPOST");
			startActivity(intent);
			break;
		case R.id.choose_time:
			toast("TimeChoose");
			intent = new Intent(context, TimeChoose.class);
			startActivity(intent);

			break;
		case R.id.About_us:
			intent = new Intent(context, AboutOus.class);
			startActivity(intent);

			break;
		case R.id.opinion:
			intent = new Intent(context, Opinion.class);
			startActivity(intent);

			break;
		case R.id.Head_portrait_information:
			showAvatarPop();
			break;
		case R.id.blacklist:// 黑名单
			startAnimActivity(new Intent(getActivity(), BlackListActivity.class));
			break;
		case R.id.exit_login:
			toast("退出登入");
			CustomApplcation.getInstance().logout();
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			if (isLogin) {
				isLogin = false;
				setViews();// test 登出
			}
			break;
		default:
			break;
		}

	}
	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;

	public String filePath = "";

	private void showAvatarPop() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_showavator,
				null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowLog("点击拍照");
				// TODO Auto-generated method stub
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				File dir = new File(BmobConstants.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// 获取相片的保存路径
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShowLog("点击相册");
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION);
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(mine_layout_all, Gravity.BOTTOM, 0, 0);
		
	}
	/**
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照旋转
	int degree = 0;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改头像
			if (resultCode == getActivity().RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					toast("SD不可用");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "拍照后的角度：" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			}
			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改头像
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == getActivity().RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					toast("SD不可用");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			} else {
				toast("照片获取失败");
			}

			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
			// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				// Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropAvator(data);
			}
			// 初始化文件路径
			filePath = "";
			// 上传头像
			uploadAvatar();
			break;
		default:
			break;

		}
	}

	private void uploadAvatar() {
		BmobLog.i("头像地址：" + path);
		final BmobFile bmobFile = new BmobFile(new File(path));
		bmobFile.upload(getActivity(), new UploadFileListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				String url = bmobFile.getFileUrl(getActivity());
				// 更新BmobUser对象
				updateUserAvatar(url);
			}

			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				toast("头像上传失败：" + msg);
			}
		});
	}

	private void updateUserAvatar(final String url) {
		User  u =new User();
		u.setAvatar(url);
		updateUserData(u,new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("头像更新成功！");
				// 更新头像
				//refreshAvatar(url);
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("头像更新失败：" + msg);
			}
		});
	}
	/**
	 * 更新头像 refreshAvatar
	 * 
	 * @return void
	 * @throws
	 */
	private void refreshAvatar(String avatar) {
		if (avatar != null && !avatar.equals("")) {
			MyImageLoader.getInstance(getActivity()).
			display(Head_portrait_information, 0, 0, user.getAvatar(), 80, 80);

		} 
	}
	String path;

	/**
	 * 保存裁剪的头像
	 * 
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				Head_portrait_information.setImageBitmap(bitmap);
				// 保存图片
				String filename = new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date())+".png";
				path = BmobConstants.MyAvatarDir + filename;
				PhotoUtil.saveBitmap(BmobConstants.MyAvatarDir, filename,
						bitmap, true);
				// 上传头像
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}
	
	/** 测试关联关系是否可用
	  * @Title: addBlog
	  * @Description: TODO
	  * @param  
	  * @return void
	  * @throws
	  */
	
	
	private void updateUserData(User user,UpdateListener listener){
		User current = (User) userManager.getCurrentUser(User.class);
		user.setObjectId(current.getObjectId());
		user.update(getActivity(), listener);
	}

	@Override
	public void onSuccess(Action method, Object obj) {
		switch (method) {
		case WEATHER:
			if (obj instanceof Bdweather) {
				Bdweather weather = (Bdweather) obj;
				if ("0".equals(weather.getErrNum())) {
					ll_weather.setVisibility(View.VISIBLE);
					RetData retData = weather.getRetData();
					initWeather(retData);
				}
			}

			break;

		default:
			break;
		}

	}

	private void initWeather(RetData retData) {
		String weather = retData.getWeather();
		weather_tex.setText(retData.getL_tmp() + "°~" + retData.getH_tmp()
				+ "°\r\n" + weather);
		sun_tex.setText("日出 日落  \r\n" + retData.getSunrise() + "~"
				+ retData.getSunset());
		if ("多云".equals(weather)) {
			weather_ima.setBackgroundResource(R.drawable.ww0);
		}
		if ("晴".equals(weather)) {
			weather_ima.setBackgroundResource(R.drawable.ww1);
		}
		if ("阵雨".equals(weather)) {
			weather_ima.setBackgroundResource(R.drawable.ww3);
		}

		if (weather.contains("阴")) {
			weather_ima.setBackgroundResource(R.drawable.ww2);
		}

		if (weather.contains("小雨")) {
			weather_ima.setBackgroundResource(R.drawable.ww7);
		}
		if (weather.contains("中雨")) {
			weather_ima.setBackgroundResource(R.drawable.ww8);
		}
		if (weather.contains("大雨")) {
			weather_ima.setBackgroundResource(R.drawable.ww10);
		}
		if (weather.contains("小雪")) {
			weather_ima.setBackgroundResource(R.drawable.ww14);
		}
		if (weather.contains("中雪")) {
			weather_ima.setBackgroundResource(R.drawable.ww15);
		}

		if (weather.contains("大雪")) {
			weather_ima.setBackgroundResource(R.drawable.ww17);
		}

	}

	@Override
	public void onError(Action method, Object statusCode) {
		switch (method) {
		case WEATHER:
			break;

		}
	}
	
}
