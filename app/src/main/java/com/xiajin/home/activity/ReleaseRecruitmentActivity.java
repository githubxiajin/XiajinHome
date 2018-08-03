package com.xiajin.home.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiajin.home.CustomView.DateTimeSelector.pickerview.OptionsPopupWindow;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.R;
import com.xiajin.home.bean.RecruitmentInfo;
import com.xiajin.home.dialog.dialog_choose_price.ActionSheetDialog;
import com.xiajin.home.dialog.dialog_choose_price.ActionSheetDialog.SheetItemColor;

import java.util.ArrayList;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by liu on 2015/8/25.
 */
public class ReleaseRecruitmentActivity extends SwipeBackActivity implements View.OnClickListener {
    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
    private OptionsPopupWindow pwOptions;
    private EditText content;// 发布内容
    private EditText title;// 标题内容
    private EditText come_from;
    private TextView price, tv_address;
    private EditText phone;
    private Button next_bt;
    private LinearLayout l_l_title, l_l_content, l_l_address, l_l_price, l_l_phone;
    public static String[] prices = {"1-1000/元", "1000-2000/元", "2000-3000/元", "3000-4000/元",
            "4000-5000/元", "5000-6000/元", "6000-7000/元", "7000-8000/元", "8000以上", "面议"};
    private String jiage = "", address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recruitment_release);
        initView();
        addressSelector();
        initTopBarForLeft("兼职招聘");
    }

    private void addressSelector() {
//选项选择器
        pwOptions = new OptionsPopupWindow(this);
        //选项1
        options1Items.add("山东");
        options1Items.add("湖南");
        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<String>();
        options2Items_01.add("济南");
        options2Items_01.add("德州");
        options2Items_01.add("东营");
        ArrayList<String> options2Items_02 = new ArrayList<String>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);

        //选项3
        ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
        ArrayList<String> options3Items_01_01 = new ArrayList<String>();
        options3Items_01_01.add("历下");
        options3Items_01_01.add("槐荫");
        options3Items_01_01.add("趵突");
        options3Items_01_01.add("市中");
        options3Items_01.add(options3Items_01_01);
        ArrayList<String> options3Items_01_02 = new ArrayList<String>();
        options3Items_01_02.add("夏津");
        options3Items_01_02.add("武城");
        options3Items_01.add(options3Items_01_02);
        ArrayList<String> options3Items_01_03 = new ArrayList<String>();
        options3Items_01_03.add("孤岛");
        options3Items_01_03.add("垦利");
        options3Items_01.add(options3Items_01_03);

        ArrayList<String> options3Items_02_01 = new ArrayList<String>();
        options3Items_02_01.add("长沙");
        options3Items_02.add(options3Items_02_01);
        ArrayList<String> options3Items_02_02 = new ArrayList<String>();
        options3Items_02_02.add("岳样");
        options3Items_02.add(options3Items_02_02);

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        //三级联动效果
        pwOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
        pwOptions.setLabels("省", "市", "区");
        //设置默认选中的三级项目
        pwOptions.setSelectOptions(0, 0, 0);
        //监听确定选择按钮
        pwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);
                tv_address.setText(tx);
                address = tx;

            }
        });

    }

    public void initView() {
        l_l_title = (LinearLayout) findViewById(R.id.l_l_title);
        l_l_content = (LinearLayout) findViewById(R.id.l_l_content);
        l_l_address = (LinearLayout) findViewById(R.id.l_l_address);
        l_l_price = (LinearLayout) findViewById(R.id.l_l_price);
        l_l_phone = (LinearLayout) findViewById(R.id.l_l_phone);
        next_bt = (Button) findViewById(R.id.next_bt);
        content = (EditText) findViewById(R.id.content);
        title = (EditText) findViewById(R.id.title);
        come_from = (EditText) findViewById(R.id.come_from);
        tv_address = (TextView) findViewById(R.id.tv_address);
        price = (TextView) findViewById(R.id.price);
        phone = (EditText) findViewById(R.id.phone);
        setListener();


    }

    public void setListener() {
        l_l_price.setOnClickListener(this);
        l_l_title.setOnClickListener(this);
        l_l_content.setOnClickListener(this);
        l_l_address.setOnClickListener(this);
        l_l_phone.setOnClickListener(this);
        next_bt.setOnClickListener(this);
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l_l_price:
                new ActionSheetDialog(ReleaseRecruitmentActivity.this)
                        .builder()
                        .setTitle("请选择操作")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("1-1000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("1000-2000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("2000-3000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("3000-4000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("4000-5000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("5000-6000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("6000-7000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("7000-8000/元", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("8000以上", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                })
                        .addSheetItem("面议", SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        jiage = prices[which - 1];
                                        price.setText(jiage);
                                    }
                                }).show();
                break;
            case R.id.l_l_title:
                title.setFocusable(true);
                title.setFocusableInTouchMode(true);

                break;
            case R.id.l_l_content:
                content.setFocusable(true);
                content.setFocusableInTouchMode(true);
                break;
            case R.id.l_l_address:
                pwOptions.showAtLocation(tv_address, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.l_l_phone:
                phone.setFocusable(true);
                phone.setFocusableInTouchMode(true);
                break;
            case R.id.next_bt:
                if (TextUtils.isEmpty(content.getText().toString())) {
                    toast("请输入标题");
                } else if ("".equals(address)) {
                    toast("请选择工作地址");
                } else if ("".equals("jiage")) {
                    toast("请选择工资待遇");
                } else if (TextUtils.isEmpty(phone.getText().toString())) {
                    toast("请输入手机号码");
                } else if (TextUtils.isEmpty(content.getText().toString())) {
                    toast("请输入招聘内容");
                } else {
                    saveData();
                }

                toast("jiage" + jiage + "title" + title.getText().toString() + "content" + content.getText().toString() + "phone" + phone.getText().toString() + "address" + address);
                break;

        }
    }

    private void saveData() {
        RecruitmentInfo recruitmentInfo = new RecruitmentInfo();
        recruitmentInfo.setTitle(title.getText().toString().trim());// biaoti
        recruitmentInfo.setAddress(address);
        recruitmentInfo.setContent(content.getText().toString().trim());// neirong
        recruitmentInfo.setPrice(jiage);
        recruitmentInfo.setPhone(phone.getText().toString().trim());
        recruitmentInfo.setAuthor(user);
        recruitmentInfo.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                dialog.dismiss();
                finish();

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    public void retry() {

    }

    @Override
    public void netError() {

    }

}
