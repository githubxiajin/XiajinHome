package com.xiajin.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiajin.home.R;
import com.xiajin.home.adapter.base.BaseListAdapter;
import com.xiajin.home.bean.TwoPost;
import com.xiajin.home.request.MyImageLoader;

import java.util.List;

public class MyAskQuestionAdapter extends BaseListAdapter<TwoPost> {


	public MyAskQuestionAdapter(Context mContext, List<TwoPost> list) {
		super(mContext, list);

	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {

		ViewHold hold = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.myaskquestion_item, null);
			hold = new ViewHold();
			hold.uesr_uesr_name = (TextView) convertView
					.findViewById(R.id.uesr_uesr_name);
			hold.come_from = (TextView) convertView
					.findViewById(R.id.come_from);
			hold.ask_title = (TextView) convertView
					.findViewById(R.id.ask_title);
			hold.create_time = (TextView) convertView
					.findViewById(R.id.create_time);
			hold.head_portrait = (ImageView) convertView
					.findViewById(R.id.head_portrait);

			hold.all_image_li = (LinearLayout) convertView
					.findViewById(R.id.all_image_li);
			ImageView imageView  = new ImageView(mContext);
			hold.image1 = (ImageView) convertView.findViewById(R.id.image1);
			hold.image2 = (ImageView) convertView.findViewById(R.id.image2);
			hold.image3 = (ImageView) convertView.findViewById(R.id.image3);

			convertView.setTag(hold);

		} else {
			hold = (ViewHold) convertView.getTag();
		}

		hold.uesr_uesr_name.setText(list.get(position).getAuthor().getUsername());
		hold.come_from.setText(list.get(position).come_from);
		hold.ask_title.setText(list.get(position).getTitle());
		hold.create_time.setText(list.get(position).getCreatedAt());
		if (!TextUtils.isEmpty(list.get(position).getAuthor().getAvatar())) {

			MyImageLoader.getInstance(mContext).display(hold.head_portrait, R.drawable.xianshi,
					R.drawable.xianshi, list.get(position).getAuthor().getAvatar(), 0, 0);

		}else {
			hold.head_portrait.setImageResource(R.drawable.touxaingceshi2);
		}
		final String[] str1 = list.get(position).getImage().split(",");

		if (str1.length >= 2) {
			MyImageLoader.getInstance(mContext).display( hold.image1, R.drawable.xianshi,
					R.drawable.xianshi, str1[0], 0, 0);
			MyImageLoader.getInstance(mContext).display( hold.image2, R.drawable.xianshi,
					R.drawable.xianshi, str1[1], 0, 0);
			MyImageLoader.getInstance(mContext).display( hold.image3, R.drawable.xianshi,
					R.drawable.xianshi, str1[2], 0, 0);
		}
		return convertView;
	}

	static class ViewHold {
		private TextView uesr_uesr_name;
		private TextView come_from;
		private TextView ask_title;
		private TextView  create_time;
		private ImageView head_portrait;
		private ImageView image1;
		private ImageView image2;
		private ImageView image3;
		private LinearLayout all_image_li;
	}
}
