package com.xiajin.home.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.R;
import com.xiajin.home.bean.TwoPost;
import com.xiajin.home.bean.User;
import com.xiajin.home.request.MyImageLoader;
import com.xiajin.home.request.RequestData;
import com.xiajin.home.request.RequestData.Action;
import com.xiajin.home.request.RequestListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.DeleteListener;

public class DetailsActivity extends SwipeBackActivity implements OnClickListener, RequestListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private ImageView head_portrait2;
    public LinearLayout li_image_all;
    public LinearLayout ImageLayout;
    public LayoutInflater inflater;
    private TextView come_from2;
    private TextView uesr_uesr_name2;
    private TextView details_content;
    private TwoPost twoPost;
    public User intentUser;
    private TextView delete;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;
    private int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_details);

        inflater = LayoutInflater.from(DetailsActivity.this);

        initTopBarForLeft("内容");

        initView();
        initRfaButton();
    }

    public void initView() {
        li_image_all = (LinearLayout) findViewById(R.id.li_image_all);
        head_portrait2 = (ImageView) findViewById(R.id.head_portrait2);
        uesr_uesr_name2 = (TextView) findViewById(R.id.uesr_uesr_name2);
        details_content = (TextView) findViewById(R.id.details_content);
        delete = (TextView) findViewById(R.id.delete);
        come_from2 = (TextView) findViewById(R.id.come_from2);
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
        twoPost = (TwoPost) intent.getSerializableExtra("details");
        details_content.setText(twoPost.getContent());// 内容
        uesr_uesr_name2.setText(twoPost.getAuthor().getUsername());// 用户名
        intentUser = new User();
        intentUser.setObjectId(twoPost.getAuthor().getObjectId());
        intentUser.setUsername(twoPost.getAuthor().getUsername());// 点击图像聊天所带参数
        come_from2.setText(twoPost.getCome_from());
        if (!TextUtils.isEmpty(twoPost.getAuthor().getAvatar())) {
            MyImageLoader.getInstance(this).display(head_portrait2, R.drawable.xianshi,
                    R.drawable.xianshi, twoPost.getAuthor().getAvatar(), 0, 0);//头像

        }

        head_portrait2.setOnClickListener(this);
        initImage();//初始化图片
    }

    private void initImage() {
        final String[] str1 = twoPost.getImage().split(",");

        for (int i = 0; i < str1.length; i++) {
            ImageLayout = (LinearLayout) inflater.inflate(
                    R.layout.item_imageview, null);

            ImageView imageView = (ImageView) ImageLayout
                    .findViewById(R.id.image);
            imageView.setId(i);
            imageView.setOnClickListener(this);
            MyImageLoader.getInstance(this).display(imageView, R.drawable.xianshi, R.drawable.xianshi,
                    str1[i], 0, 0);
            li_image_all.addView(ImageLayout);


        }
        openPhoto(str1);//照片初始化

    }

    private void openPhoto(String[] str1) {

        PhotoActivity.bitmap.clear();
        for (int i = 0; i < str1.length; i++) {
            // RequestData.getInstance().image(this, str1[i], this);
            Bitmap bitmap = MyImageLoader.getInstance(this).getBitmapCache().getBitmap(getCacheKey(str1[i], 0, 0));
            if (null != bitmap) { //从缓存che取出 如果没有请求
                PhotoActivity.bitmap.add(bitmap);
            } else {
                RequestData.getInstance().image(this, str1[i], this);

            }


        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_portrait2:
                OpenChat();
                break;
            case R.id.delete:
                deleteData();
                break;
            default:
                Intent intent = new Intent(this, PhotoActivity.class);
                intent.putExtra("ID", v.getId());
                startActivity(intent);
                break;
        }

    }

    public void deleteData() {
        final String[] fileNames = twoPost.getFileName().split(",");

        for (int i = 0; i < fileNames.length; i++) {
            number = i;
            BmobProFile.getInstance(DetailsActivity.this).deleteFile(fileNames[i], new DeleteFileListener() {

                @Override
                public void onError(int errorcode, String errormsg) {
                    // TODO Auto-generated method stub
                    Log.i("bmob", "删除文件失败：" + errormsg + "(" + errorcode + ")");
                }

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Log.i("bmob", "删除文件成功");
                    if (number == 2) {
                        deleteData2();

                    }
                }
            });

        }


    }

    private void deleteData2() {

        TwoPost two = new TwoPost();
        two.setObjectId(twoPost.getObjectId());
        two.delete(this, new DeleteListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("删除数据成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("删除数据失败：" + msg);
            }
        });

    }

    private void OpenChat() {
        Intent intent = new Intent(DetailsActivity.this,
                ChatActivity.class);
        intent.putExtra("user", intentUser);
        startAnimActivity(intent);
    }

    // volley cache 名字
    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W")
                .append(maxWidth).append("#H").append(maxHeight).append(url)
                .toString();
    }

    @Override
    public void onSuccess(Action method, Object obj) {
        if (obj instanceof Bitmap) {
            Bitmap map = (Bitmap) obj;
            PhotoActivity.bitmap.add(map);
        }

    }

    @Override
    public void onError(Action method, Object statusCode) {
        // TODO Auto-generated method stub

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


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        toast("clicked label: " + position);
        rfabHelper.toggleContent();
        switch (position) {

            case 0:
                dialog("17091089715");
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
                dialog("17091089715");
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

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "[夏津之家]" + twoPost.getContent());
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
