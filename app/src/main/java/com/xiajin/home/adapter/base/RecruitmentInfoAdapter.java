package com.xiajin.home.adapter.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiajin.home.R;
import com.xiajin.home.bean.RecruitmentInfo;
import com.xiajin.home.request.MyImageLoader;

import java.util.List;

/**
 * Created by liu on 2015/8/25.
 */
public class RecruitmentInfoAdapter extends BaseListAdapter<RecruitmentInfo> {

    public RecruitmentInfoAdapter(Context context, List<RecruitmentInfo> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_recruitment, null);
            hold = new ViewHold();
            hold.uesr_uesr_name = (TextView) convertView
                    .findViewById(R.id.uesr_uesr_name);
            hold.ask_title = (TextView) convertView
                    .findViewById(R.id.ask_title);
            hold.address = (TextView) convertView
                    .findViewById(R.id.address);
            hold.price = (TextView) convertView
                    .findViewById(R.id.price);
            hold.create_time = (TextView) convertView
                    .findViewById(R.id.create_time);
            hold.head_portrait = (ImageView) convertView
                    .findViewById(R.id.head_portrait);
            convertView.setTag(hold);

        } else {
            hold = (ViewHold) convertView.getTag();
        }
        hold.uesr_uesr_name.setText(list.get(position).getAuthor().getUsername());
        hold.ask_title.setText(list.get(position).getTitle());
        hold.create_time.setText(list.get(position).getCreatedAt());
        hold.address.setText("工作地址:"+list.get(position).getAddress());
        hold.price.setText(list.get(position).getPrice());
        if (!TextUtils.isEmpty(list.get(position).getAuthor().getAvatar())) {

            MyImageLoader.getInstance(mContext).display(hold.head_portrait, R.drawable.xianshi,
                    R.drawable.xianshi, list.get(position).getAuthor().getAvatar(), 0, 0);

        } else {
            hold.head_portrait.setImageResource(R.drawable.touxaingceshi2);
        }
        return convertView;
    }

    static class ViewHold {
        private TextView uesr_uesr_name;
        private TextView ask_title;
        private ImageView head_portrait;
        private TextView create_time;
        private TextView address;
        private TextView price;

    }
}
