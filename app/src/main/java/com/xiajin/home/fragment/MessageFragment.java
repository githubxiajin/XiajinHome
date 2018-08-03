package com.xiajin.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiajin.home.R;
import com.xiajin.home.base.BaseFragment;
import com.xiajin.home.config.Config;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.bean.BmobSmsState;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.QuerySMSStateListener;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class MessageFragment extends BaseFragment {

    public Integer msmsId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BmobSMS.initialize(context, Config.applicationId);

        view = inflater.inflate(R.layout.frag_home, null);
        getViews(view);
        return view;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTopBarForOnlyTitle("夏津");
    }

    private void getViews(View view) {

        TextView aaaaaaa = (TextView) view.findViewById(R.id.aaaaaaa);
        aaaaaaa.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               /* Intent
                        intent = new Intent(getActivity(),DialogDemoActivity.class);*/


            }
        });


        TextView send = (TextView) view.findViewById(R.id.send);
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //send();

            }
        });



        TextView Verified = (TextView) view.findViewById(R.id.Verified);
        Verified.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //	Verified();

            }


        });


        TextView querySmsState = (TextView) view.findViewById(R.id.querySmsState);
        querySmsState.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                querySmsState();

            }

            private void querySmsState() {
                BmobSMS.querySmsState(context, msmsId, new QuerySMSStateListener() {


                    @Override
                    public void done(BmobSmsState state, BmobException ex) {
                        if(ex==null){
                            Log.i("bmob","短信状态："+state.getSmsState()+",验证状态："+state.getVerifyState());
                        }

                    }
                });

            }


        });

    }

    public void send(){
        BmobSMS.requestSMSCode(context, "17091089715", "夏津之家mode1",new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId,BmobException ex) {
                if(ex==null){//验证码发送成功
                    Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                    toast("bmob"+ "短信id："+smsId);


                    msmsId = smsId;
                }
            }
        });}
    private void Verified() {
        BmobSMS.verifySmsCode(context,"11位手机号码", "验证码", new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                if(ex==null){//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                    toast("bmob验证通过");
                }else{
                    Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                }
            }
        });}

}
