package com.xiajin.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.CustomView.myView.ErrorHintView;
import com.xiajin.home.CustomView.myView.HeaderLayout.onRightImageButtonClickListener;
import com.xiajin.home.R;
import com.xiajin.home.adapter.MyAskQuestionAdapter;
import com.xiajin.home.bean.TwoPost;
import com.xiajin.home.config.BmobConstants;
import com.xiajin.home.config.Config;
import com.xiajin.home.service.ReleaseService;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MyAskQuestion extends SwipeBackActivity implements
        OnItemClickListener {
    private MyAskQuestionAdapter adapter;
    private PullToRefreshListView my_askquestion_list;
    private RelativeLayout rl_mycontent;
    private int Limit = 20;
    private int Skip = 20;
    private int number = 1;
    private int SUCCESS = 1; // 0 假 1 真
    private Boolean first = true;
    public List<TwoPost> list = new ArrayList<TwoPost>();
    public String MYPOST = "MYPOST";//我的帖子
    public String SECOND = "SECOND";//二手
    public String DERIVATIVE = "DERIVATIVE";//微商
    public String FROM;
    public RelativeLayout loadView;
    private ErrorHintView mErrorHintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myaskquestion);
        FROM = getIntent().getStringExtra("from");
        initView();
        initData();
        if (FROM.equals(SECOND)) {
            initTopBarForBoth(SECOND, R.drawable.btn_login_n, "发布",
                    new onRightImageButtonClickListener() {

                        @Override
                        public void onClick() {
                            Bundle mBundle = new Bundle();
                            mBundle.putString("FROM", "SECOND");
                            openActivity(ReleaseActivity.class, mBundle);

                        }
                    });
        } else if (FROM.equals(MYPOST)) {
            initTopBarForLeft("我的帖子");
        } else if (FROM.equals(DERIVATIVE)) {
            initTopBarForBoth("DERIVATIVE", R.drawable.btn_login_n, "发布",
                    new onRightImageButtonClickListener() {

                        @Override
                        public void onClick() {
                            Intent intent = new Intent(MyAskQuestion.this, ReleaseActivity.class);

                            Bundle mBundle = new Bundle();
                            mBundle.putString("FROM", "DERIVATIVE");
                            intent.putExtras(mBundle);
                            startActivityForResult(intent, 0);
                            registerBoradcastReceiver();

                        }
                    });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            initTopBarForLeft("发布中");
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Config.ACTION_NAME)) {
                toast(action + intent.getStringExtra("result"));
                unregisterReceiver(mBroadcastReceiver);
                Intent service = new Intent(MyAskQuestion.this, ReleaseService.class);
                stopService(service);
                initTopBarForLeft(intent.getStringExtra("result"));
            }
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Config.ACTION_NAME);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void initView() {
        loadView = (RelativeLayout) findViewById(R.id.loadView);
        mErrorHintView = (ErrorHintView) this.findViewById(R.id.hintView);
        my_askquestion_list = (PullToRefreshListView) findViewById(R.id.xiajin_new);
        rl_mycontent = (RelativeLayout) findViewById(R.id.rl_mycontent);
        adapter = new MyAskQuestionAdapter(MyAskQuestion.this, list);
        my_askquestion_list.setAdapter(adapter);
        my_askquestion_list.setOnItemClickListener(this);
    }

    private void initData() {
        if (0 == SUCCESS) {
            return;
        }
        SUCCESS = 0;
        BmobQuery<TwoPost> query = new BmobQuery<TwoPost>();
        if (FROM.equals(MYPOST)) {//我的帖子
            query.addWhereEqualTo("author", user);
        }
        if (FROM.equals(SECOND)) {
            query.addWhereEqualTo("mifrom", SECOND);
        }
        if (FROM.equals(DERIVATIVE)) {
            query.addWhereEqualTo("mifrom", DERIVATIVE);
        }

        query.include("author");
        query.setLimit(Limit);
        query.order("-createdAt");
        if (first) {
            query.setSkip(0);
        } else {
            query.setSkip(Skip);
        }
        first = false;
        // 执行查询方法
        query.findObjects(this, new FindListener<TwoPost>() {
            @Override
            public void onSuccess(List<TwoPost> object) {
                // TODO Auto-generated method stub
                if (object.size() == 0) {
                    my_askquestion_list.onRefreshComplete();
                    toast("没有最新数据");
                    SUCCESS = 1;
                    return;
                }
                list.addAll(object);

                adapter.notifyDataSetChanged();
                my_askquestion_list.onRefreshComplete();

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

        my_askquestion_list.setOnRefreshListener(new OnRefreshListener2() {

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
                    my_askquestion_list.onRefreshComplete();
                }

            }
        });

    }

    private void initErro(int code) {
        mErrorHintView.setVisibility(View.GONE);
        loadView.setVisibility(View.GONE);
        rl_mycontent.setVisibility(View.GONE);
        my_askquestion_list.onRefreshComplete();
        switch (code) {
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
        Intent intent = new Intent(MyAskQuestion.this, DetailsActivity.class);
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
