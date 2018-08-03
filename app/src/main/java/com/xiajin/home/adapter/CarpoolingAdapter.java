package com.xiajin.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiajin.home.R;
import com.xiajin.home.adapter.base.BaseListAdapter;
import com.xiajin.home.bean.CarpoolingInfo;
import com.xiajin.home.bean.User;

import java.util.List;

/**
 * Created by wangning on 15/8/24.
 */
public class CarpoolingAdapter extends BaseListAdapter<CarpoolingInfo>{

    public CarpoolingAdapter(Context context, List<CarpoolingInfo> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_carpooling_car, null);
            hold = new ViewHold();
            hold.tv_start = (TextView) convertView
                    .findViewById(R.id.tv_start);
            hold.tv_end = (TextView) convertView
                    .findViewById(R.id.tv_end);
            hold.tv_start_time = (TextView) convertView
                    .findViewById(R.id.tv_start_time);
            hold.tv_release_time = (TextView) convertView
                    .findViewById(R.id.tv_release_time);
            convertView.setTag(hold);

        } else {
            hold = (ViewHold) convertView.getTag();
        }
        hold.tv_start.setText(list.get(position).getStart());
        hold.tv_end.setText(list.get(position).getEnd());
        hold.tv_start_time.setText(list.get(position).getTime());
        hold.tv_release_time.setText(list.get(position).getCreatedAt().substring(0,10)+"发布");


        return convertView;
    }
    static class ViewHold {
        private TextView tv_start;
        private  TextView tv_end;
        private TextView type;
        private User author;
        private TextView tv_start_time;
        private TextView tv_release_time;


    }
}
