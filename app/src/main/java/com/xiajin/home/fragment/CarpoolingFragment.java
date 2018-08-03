package com.xiajin.home.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiajin.home.CustomView.myView.ErrorHintView;
import com.xiajin.home.R;
import com.xiajin.home.activity.CarpoolingDetailsActivity;
import com.xiajin.home.adapter.CarpoolingAdapter;
import com.xiajin.home.base.BaseFragment;
import com.xiajin.home.bean.CarpoolingInfo;
import com.xiajin.home.config.BmobConstants;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by liu on 2015/8/30.
 */
public class CarpoolingFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private String channelId;   // 0 我是车主 1 ：我是乘客
    private ErrorHintView mErrorHintView;
    public RelativeLayout loadView;
    public CarpoolingFragment(String channelId) {
        this.channelId = channelId;
    }
    private int Limit = 20;
    private int Skip = 0;
    private int number = 1;
    private int SUCCESS = 1; // 0 假 1 真
    private Boolean first = true;
    private PullToRefreshListView listView;
    private  List<CarpoolingInfo> list =new ArrayList<CarpoolingInfo>();
    private RelativeLayout rl_mycontent;
    private CarpoolingAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_carpooling, null);
        initView(view);
        return view;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }
    private void initView(View view) {
        loadView = (RelativeLayout)view.findViewById(R.id.loadView);
       listView= (PullToRefreshListView) view.findViewById(R.id.listview_Carpooling);
        rl_mycontent = (RelativeLayout) view.findViewById(R.id.rl_mycontent);
        mErrorHintView = (ErrorHintView) view.findViewById(R.id.hintView);
        listView.setOnItemClickListener(this);
        adapter = new CarpoolingAdapter(context,list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

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
                    listView.onRefreshComplete();
                }

            }
        });


    }
    private void initData() {
        if (0 == SUCCESS) {
            return;
        }
        SUCCESS = 0;
        BmobQuery<CarpoolingInfo> query = new BmobQuery<CarpoolingInfo>();
        query.include("author");
        query.order("-createdAt");
        query.setLimit(Limit);
        query.addWhereEqualTo("type", channelId);
        if (first) {
            query.setSkip(0);
        } else {
            query.setSkip(Skip);
        }
        first = false;
        // 执行查询方法
        query.findObjects(context, new FindListener<CarpoolingInfo>() {
            @Override
            public void onSuccess(List<CarpoolingInfo> object) {
                // TODO Auto-generated method stub

                if (object.size() == 0) {
                    listView.onRefreshComplete();
                    toast("没有最新数据");
                    SUCCESS = 1;
                    return;
                }
                list.addAll(object);
                adapter.notifyDataSetChanged();
                listView.onRefreshComplete();

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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        toast(position + "--position");
        Intent intent = new Intent(context, CarpoolingDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("details", list.get(position - 1));
        intent.putExtras(bundle);
        startActivity(intent);

    }
    private void initErro(int code) {
        mErrorHintView.setVisibility(View.GONE);
        loadView.setVisibility(View.GONE);
        rl_mycontent.setVisibility(View.GONE);
        listView.onRefreshComplete();
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
}
