package com.xiajin.home.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiajin.home.CustomView.DateTimeSelector.pickerview.OptionsPopupWindow;
import com.xiajin.home.CustomView.DateTimeSelector.pickerview.TimePopupWindow;
import com.xiajin.home.CustomView.DateTimeSelector.pickerview.TimePopupWindow.Type;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.R;
import com.xiajin.home.bean.CarpoolingInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by liu on 2015/9/2.
 */
public class ReleaseCarpoolingCarActivity extends SwipeBackActivity implements View.OnClickListener {
    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
    private TimePopupWindow pwTime;
    private OptionsPopupWindow pwOptions;
    private String type_address = "0";
    private String  successively = "0";
    private EditText phone,content;
    private Button next_bt;
    private TextView tvTime, start_address, end_address, chezhu, chengke;
    private LinearLayout l_l_time, l_l_start_address, l_l_end_address, l_l_phone,l_l_content;
    private String startAddress = "", endAddress = "", Time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpoolingcar_release);
        initView();
        initTopBarForLeft("拼车");
        timeselector();
        setListener();

    }

    public void setListener() {

        //弹出时间选择器
        l_l_time.setOnClickListener(this);
        //点击弹出选项选择器
        l_l_start_address.setOnClickListener(this);
        l_l_end_address.setOnClickListener(this);
        l_l_phone.setOnClickListener(this);
        chezhu.setOnClickListener(this);
        chengke.setOnClickListener(this);
        next_bt.setOnClickListener(this);
        l_l_content.setOnClickListener(this);
    }

    public void timeselector() {

        //时间选择器
        pwTime = new TimePopupWindow(this, Type.ALL);
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tvTime.setText(getTime(date));
                Time = getTime(date);
            }
        });

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
                if ("0".equals(successively)) {
                    start_address.setText(tx);
                    startAddress = tx;
                } else if ("1".equals(successively)) {
                    end_address.setText(tx);
                    endAddress = tx;
                }

            }
        });

    }


    public void initView() {
        next_bt = (Button) findViewById(R.id.next_bt);
        l_l_start_address = (LinearLayout) findViewById(R.id.l_l_start_address);
        l_l_end_address = (LinearLayout) findViewById(R.id.l_l_end_address);
        l_l_phone = (LinearLayout) findViewById(R.id.l_l_phone);
        l_l_content= (LinearLayout) findViewById(R.id.l_l_content);
        l_l_time = (LinearLayout) findViewById(R.id.l_l_time);
        tvTime = (TextView) findViewById(R.id.time);
        phone = (EditText) findViewById(R.id.phone);
        content= (EditText) findViewById(R.id.content);
        start_address = (TextView) findViewById(R.id.start_address);
        chezhu = (TextView) findViewById(R.id.chezhu);
        chengke = (TextView) findViewById(R.id.chengke);
        end_address = (TextView) findViewById(R.id.end_address);
        chezhu.setBackgroundColor(0xff2eb2ff);


    }


    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }


    private void saveData() {
        CarpoolingInfo carpoolingInfo = new CarpoolingInfo();
        carpoolingInfo.setPhone(phone.getText().toString().trim());
        carpoolingInfo.setContent(content.getText().toString().trim());
        carpoolingInfo.setStart(startAddress.substring(2));
        carpoolingInfo.setEnd(endAddress.substring(2));
        carpoolingInfo.setType(type_address);
        carpoolingInfo.setTime(Time);
        carpoolingInfo.setAuthor(user);
        carpoolingInfo.save(this, new SaveListener() {

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l_l_time:
                pwTime.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.l_l_start_address: //点击弹出选项选择器
                successively="0";
                pwOptions.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.l_l_end_address:
                successively="1";
                pwOptions.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.chezhu:
                type_address = "0";
                chezhu.setBackgroundColor(0xff2eb2ff);
                chengke.setBackgroundColor(0xffffffff);
                break;
            case R.id.chengke:
                type_address = "1";
                chengke.setBackgroundColor(0xff2eb2ff);
                chezhu.setBackgroundColor(0xffffffff);
                break;
            case R.id.l_l_phone:
               phone.setFocusable(true);
                phone.setFocusableInTouchMode(true);
                break;
            case R.id.l_l_content:
                content.setFocusable(true);
                content.setFocusableInTouchMode(true);
                break;
            case R.id.next_bt:
                if ("".equals("Time")){
                    toast("请选择出发时间");
                }else if ("".equals(startAddress)){
                    toast("请选择出发地址");
                }else if ("".equals(endAddress)){
                    toast("请选择到达地址");
                }else if("".equals(phone.getText().toString().trim())){
                    toast("请填写手机号码");
                }  else if ("".equals(content.getText().toString().trim())){
                    toast("请填写描述内容");

                } else {

                    saveData();
                }
                toast("time=" + Time + "----" + "startadrress=" + startAddress + "------" + "endAddress" + endAddress + "-----" + "phone=" + phone.getText().toString().trim() + "-----" + "type=" + type_address);

                break;

        }
    }

    @Override
    public void retry() {

    }

    @Override
    public void netError() {

    }

}

