package com.xiajin.home.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.xiajin.home.CustomView.PagerSlidingTabStrip.PagerSlidingTabStrip;
import com.xiajin.home.CustomView.PagerSlidingTabStrip.ZoomOutPageTransformer;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.CustomView.myView.HeaderLayout;
import com.xiajin.home.R;
import com.xiajin.home.base.BaseFragment;
import com.xiajin.home.fragment.CarpoolingFragment;

/**
 * Created by wangning on 15/8/24.
 */
public class CarpoolingCarActivity extends SwipeBackActivity {


    private ViewPager contentPager;
    private mPagerAdapter adapter;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling_car);
        initTopBarForBoth("拼车", R.drawable.btn_login_n, "发布",
                new HeaderLayout.onRightImageButtonClickListener() {

                    @Override
                    public void onClick() {
                        openActivity(ReleaseCarpoolingCarActivity.class);
                    }
                });
        initView();

    }

    private void initView() {
        contentPager = (ViewPager) this.findViewById(R.id.content_pager);
        adapter = new mPagerAdapter(getSupportFragmentManager());
        contentPager.setAdapter(adapter);
        contentPager.setOffscreenPageLimit(2);
        contentPager.setPageTransformer(true, new ZoomOutPageTransformer());

        tabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);
        tabs.setTextColorResource(R.color.light_gray_text);
        tabs.setDividerColorResource(R.color.common_list_divider);
        // tabs.setUnderlineColorResource(R.color.common_list_divider);
        tabs.setIndicatorColorResource(R.color.red);
        tabs.setSelectedTextColorResource(R.color.red);
        // tabs.setIndicatorHeight(5);
        tabs.setViewPager(contentPager);

    }
    private class mPagerAdapter extends FragmentStatePagerAdapter {

        private String Title[] = { "我是车主", "我是乘客" };

        public mPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int arg0) {
            String[] id = { "0", "1" }; 	// 0 我是车主 1 ：我是乘客

            return new CarpoolingFragment(id[arg0]);
        }

        @Override
        public int getCount() {
            return Title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Title[position];
        }

    }

    @Override
    public void retry() {

    }

    @Override
    public void netError() {

    }
}
