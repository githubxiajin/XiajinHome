package com.xiajin.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xiajin.home.CustomView.HomeButton;
import com.xiajin.home.CustomView.HomeButton.HomeClickListener;
import com.xiajin.home.R;
import com.xiajin.home.activity.CarpoolingCarActivity;
import com.xiajin.home.activity.MyAskQuestion;
import com.xiajin.home.activity.RecruitmentActivity;
import com.xiajin.home.adapter.MyPagerAdapter;
import com.xiajin.home.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private ViewPager viewpager;
    /**
     * 图片列表
     */
    public List<ImageView> imageViews;
    private TextView tv_title;
    private LinearLayout ll_point;
    private HomeButton hotel;
    private HomeButton hb_carpooling;
    private HomeButton hb_recruitment;
    private HomeButton derivative;
    private int lastPosition;
    // 图片资源ID
    private int[] ids = {R.drawable.huimeng, R.drawable.b, R.drawable.c,
            R.drawable.d};
    // 图片标题集合
    private final String[] imageDescriptions = {"xxxxx", "xxxxxx",
            "xxxxxxx", "xxxxxxxxx"};

    private boolean isRunning = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
// 得到当前的的选择的位置
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            if (isRunning) {
                handler.sendEmptyMessageDelayed(0, 2000);
            }

        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_message, null);
        initViewPage(view);
        setlistenr();
        return view;
    }


    private void setlistenr() {

        hotel.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                Intent intent = new Intent(context, MyAskQuestion.class);
                intent.putExtra("from", "SECOND");
                startActivity(intent);

            }
        });
        derivative.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                Intent intent = new Intent(context, MyAskQuestion.class);
                intent.putExtra("from", "DERIVATIVE");
                startActivity(intent);

            }
        });
        hb_carpooling.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                Intent intent = new Intent(context, CarpoolingCarActivity.class);
                startActivity(intent);

            }
        });
        hb_recruitment.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                Intent intent = new Intent(context, RecruitmentActivity.class);
                startActivity(intent);

            }
        });

    }

    private void initViewPage(View view) {
        hotel = (HomeButton) view.findViewById(R.id.hotel);
        hb_carpooling = (HomeButton) view.findViewById(R.id.hb_carpooling);
        hb_recruitment = (HomeButton) view.findViewById(R.id.hb_recruitment);
        derivative = (HomeButton) view.findViewById(R.id.derivative);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ll_point = (LinearLayout) view.findViewById(R.id.ll_point);
// 实例化数据集合
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            ImageView point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_selector);
// 设置参数的时候，主要了要设置控件的父类是什么，就导入谁的包
            LayoutParams params = new LayoutParams(10, 10);
            params.leftMargin = 10;
            point.setLayoutParams(params);
            ll_point.addView(point);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
        }

        viewpager.setAdapter(new MyPagerAdapter(imageViews));
// 默认设置第0个的文本
        String msg = imageDescriptions[0];
        tv_title.setText(msg);
// 设置中间值
        int indext = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % imageViews.size();
// 22-11-10
// 66-33-
        viewpager.setCurrentItem(indext);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            // 当选择了某个页面的时候回调
// 就是选择了那个页面：position
            @Override
            public void onPageSelected(int position) {
                int myindex = position % imageViews.size();
                String msg = imageDescriptions[myindex];
                tv_title.setText(msg);
// 当前的指示点设置高亮
                ll_point.getChildAt(myindex).setEnabled(true);
// 上一次的高亮了的，设置为默认
                ll_point.getChildAt(lastPosition).setEnabled(false);
                lastPosition = myindex;

            }

            // 当页面滚动了的时候回调
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            // 当页面状态发送变化的时候回调
// 静止-滑动
// 滑动-静止
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        isRunning = true;
        handler.sendEmptyMessageDelayed(0, 2000);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
// 停止自动播放动画了
        isRunning = false;
        imageViews.retainAll(imageViews);

    }

}
