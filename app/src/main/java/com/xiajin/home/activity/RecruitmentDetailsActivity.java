package com.xiajin.home.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.R;
import com.xiajin.home.bean.RecruitmentInfo;
import com.xiajin.home.bean.User;
import com.xiajin.home.request.MyImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by liu on 2015/8/25.
 */
public class RecruitmentDetailsActivity extends SwipeBackActivity implements View.OnClickListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private ImageView head_portrait2;
    private TextView delete;
    private TextView uesr_uesr_name2;
    private TextView content;
    private TextView title, tv_address, price;
    public User intentUser;

    private RecruitmentInfo recruitmentInfo;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_details);
        initTopBarForLeft("招聘");
        initView();
        initRfaButton();
    }

    public void initRfaButton() {
  /*
        // 可通过代码设置属性
        rfaLayout.setFrameColor(Color.RED);
        rfaLayout.setFrameAlpha(0.4f);

        rfaButton.setNormalColor(0xff37474f);
        rfaButton.setPressedColor(0xff263238);
        rfaButton.getRfabProperties().setShadowDx(ABTextUtil.dip2px(context, 3));
        rfaButton.getRfabProperties().setShadowDy(ABTextUtil.dip2px(context, 3));
        rfaButton.getRfabProperties().setShadowRadius(ABTextUtil.dip2px(context, 5));
        rfaButton.getRfabProperties().setShadowColor(0xffcccccc);
        rfaButton.getRfabProperties().setStandardSize(RFABSize.MINI);
        rfaButton.build();
        */

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Phone: 拨打电话")
                        .setResId(R.drawable.ico_test_d)
                        .setIconNormalColor(0xffd84315)
                        .setIconPressedColor(0xffbf360c)
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("应用内联系")
                        .setDrawable(getResources().getDrawable(R.drawable.ico_test_c))
                        .setIconNormalColor(0xff4e342e)
                        .setIconPressedColor(0xff3e2723)
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(this, 4)))
                        .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("分享转发")
                        .setResId(R.drawable.ico_test_b)
                        .setIconNormalColor(0xff056f00)
                        .setIconPressedColor(0xff0d5302)
                        .setLabelColor(0xff056f00)
                        .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Compose")
                        .setResId(R.drawable.ico_test_a)
                        .setIconNormalColor(0xff283593)
                        .setIconPressedColor(0xff1a237e)
                        .setLabelColor(0xff283593)
                        .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(this, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();


    }

    public void initView() {
        head_portrait2 = (ImageView) findViewById(R.id.head_portrait2);
        uesr_uesr_name2 = (TextView) findViewById(R.id.uesr_uesr_name2);
        content = (TextView) findViewById(R.id.content);
        tv_address = (TextView) findViewById(R.id.tv_address);
        price = (TextView) findViewById(R.id.price);
        title = (TextView) findViewById(R.id.title);
        delete = (TextView) findViewById(R.id.delete);

        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.label_list_sample_rfal);
        rfaButton = (RapidFloatingActionButton) findViewById(R.id.label_list_sample_rfab);
        setData();

    }

    public void setData() {
        if (user.getUsername().equals("admin")) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(this);
        }
        Intent intent = this.getIntent();
        recruitmentInfo = (RecruitmentInfo) intent.getSerializableExtra("details");
        content.setText(recruitmentInfo.getContent());// 内容
        tv_address.setText(recruitmentInfo.getAddress());
        price.setText(recruitmentInfo.getPrice());
        title.setText(recruitmentInfo.getTitle());
        uesr_uesr_name2.setText(recruitmentInfo.getAuthor().getUsername());// 用户名
        intentUser = new User();
        intentUser.setObjectId(recruitmentInfo.getAuthor().getObjectId());
        intentUser.setUsername(recruitmentInfo.getAuthor().getUsername());// 点击图像聊天所带参数
        if (!TextUtils.isEmpty(recruitmentInfo.getAuthor().getAvatar())) {
            MyImageLoader.getInstance(this).display(head_portrait2, R.drawable.xianshi,
                    R.drawable.xianshi, recruitmentInfo.getAuthor().getAvatar(), 0, 0);//头像

        }
        head_portrait2.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.head_portrait2:
                intent = new Intent(this,
                        ChatActivity.class);
                intent.putExtra("user", intentUser);
                startAnimActivity(intent);
                break;
            case R.id.delete:

                deleteData();

                break;

        }

    }

    public void deleteData() {
        RecruitmentInfo recruitment = new RecruitmentInfo();
        recruitment.setObjectId(recruitmentInfo.getObjectId());
        recruitment.delete(this, new DeleteListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("删除成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("删除失败：" + msg);
            }
        });

    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        toast("clicked label: " + position);
        rfabHelper.toggleContent();
        switch (position) {

            case 0:
                dialog(recruitmentInfo.getPhone());
                break;
            case 1:
                OpenChat();
                break;
            case 2:
                share();
                break;
            case 3:
                break;


        }

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        toast("clicked icon: " + position);
        rfabHelper.toggleContent();
        switch (position) {

            case 0:
                dialog(recruitmentInfo.getPhone());
                break;
            case 1:
                OpenChat();
                break;
            case 2:
                share();
                break;
            case 3:
                break;


        }
    }

    private void OpenChat() {
        Intent intent = new Intent(RecruitmentDetailsActivity.this,
                ChatActivity.class);
        intent.putExtra("user", intentUser);
        startAnimActivity(intent);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "[夏津之家]" + recruitmentInfo.getContent());
        startActivity(Intent.createChooser(intent, getTitle()));

    }

    private void call(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);//ACTION_DIAL
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);

    }

    public void dialog(final String number) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater
                .inflate(R.layout.diolog_call, null);
        TextView phone = (TextView) timepickerview.findViewById(R.id.phone_number);
        phone.setText(number);
        new AlertDialog.Builder(this).setView(timepickerview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        call(number);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void retry() {

    }

    @Override
    public void netError() {

    }
}
