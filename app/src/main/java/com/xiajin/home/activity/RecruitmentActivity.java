package com.xiajin.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.CustomView.myView.ErrorHintView;
import com.xiajin.home.R;
import com.xiajin.home.adapter.base.RecruitmentInfoAdapter;
import com.xiajin.home.bean.RecruitmentInfo;
import com.xiajin.home.CustomView.myView.HeaderLayout;
import com.xiajin.home.config.BmobConstants;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by liu on 2015/8/25.
 */
public class RecruitmentActivity extends SwipeBackActivity implements
        AdapterView.OnItemClickListener {
    private int Limit = 20;
    private int Skip = 0;
    private int number = 1;
    private int SUCCESS = 1; // 0 假 1 真
    private Boolean first = true;
    private PullToRefreshListView my_recruitment_listview;
    private RelativeLayout rl_mycontent;
    private RecruitmentInfoAdapter adapter;
    private ErrorHintView mErrorHintView;
    public RelativeLayout loadView;
    List<RecruitmentInfo> list = new ArrayList<RecruitmentInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recruitment);
        initTopBarForBoth("兼职招聘", R.drawable.btn_login_n, "发布",
                new HeaderLayout.onRightImageButtonClickListener() {

                    @Override
                    public void onClick() {
                         openActivity(ReleaseRecruitmentActivity.class);

                    }
                });
        initView();


        initData();

    }


    private void initView() {
        loadView = (RelativeLayout) findViewById(R.id.loadView);
        mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
        my_recruitment_listview = (PullToRefreshListView) findViewById(R.id.my_recruitment_listview);
        rl_mycontent = (RelativeLayout) findViewById(R.id.rl_mycontent);
        adapter = new RecruitmentInfoAdapter(RecruitmentActivity.this, list);
        my_recruitment_listview.setAdapter(adapter);
        my_recruitment_listview.setOnItemClickListener(this);
        my_recruitment_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                first = true;
                number = 1;
                list.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                if (SUCCESS == 1) {
                    Skip = Limit * number;
                    number++;

                    initData();
                } else {
                    my_recruitment_listview.onRefreshComplete();
                }

            }
        });

    }

    private void initData() {
        if (0 == SUCCESS) {
            return;
        }
        SUCCESS = 0;
        BmobQuery<RecruitmentInfo> query = new BmobQuery<RecruitmentInfo>();
        query.include("author");
        query.order("-createdAt");
        query.setLimit(Limit);
        if (first) {
            query.setSkip(0);
        } else {
            query.setSkip(Skip);
        }
        first = false;
        // 执行查询方法
        query.findObjects(this, new FindListener<RecruitmentInfo>() {
            @Override
            public void onSuccess(List<RecruitmentInfo> object) {
                // TODO Auto-generated method stub

                if (object.size() == 0) {
                    my_recruitment_listview.onRefreshComplete();
                    toast("没有最新数据");
                    SUCCESS = 1;
                    return;
                }
                list.addAll(object);
                adapter.notifyDataSetChanged();
                my_recruitment_listview.onRefreshComplete();

                SUCCESS = 1;
                toast("查询成功：共" + object.size() + "条数据。");

                rl_mycontent.setVisibility(View.VISIBLE);// 顯示內容
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                toast("查询失败：" + msg);
                SUCCESS = 1;
                initErro(code);
            }
        });


    }

    private void initErro(int code) {
        mErrorHintView.setVisibility(View.GONE);
        loadView.setVisibility(View.GONE);
        rl_mycontent.setVisibility(View.GONE);
        my_recruitment_listview.onRefreshComplete();
        switch (code){
            case BmobConstants.NETWORKNORMA:
                first = true;
                mErrorHintView.hideLoading();
                mErrorHintView.netError(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                        loadView.setVisibility(View.VISIBLE);
                        initData();

                    }
                });
                break;
            case 2:
                mErrorHintView.hideLoading();
                mErrorHintView.loadFailure(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                    }
                });
                break;


        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        toast(position + "--position");
        Intent intent = new Intent(this, RecruitmentDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("details", list.get(position - 1));
        intent.putExtras(bundle);
        startActivity(intent);

    }
    @Override
    public void retry() {

    }

    @Override
    public void netError() {

    }
}

