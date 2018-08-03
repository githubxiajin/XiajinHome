package com.xiajin.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.CustomView.myView.HeaderLayout.onRightImageButtonClickListener;
import com.xiajin.home.R;
import com.xiajin.home.bean.TwoPost;
import com.xiajin.home.service.ReleaseService;
import com.xiajin.home.utils.Bimp;
import com.xiajin.home.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;

public class ReleaseActivity extends SwipeBackActivity implements
        OnItemClickListener {
    private GridView gridview;
    private EditText comment_content;// 发布内容
    private EditText title_content;// 标题内容
    private EditText come_from_content;
    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    private GridAdapter adapter;
    private float dp;
    private String comment;
    private String title;
    private String come_from;
    private String FROM;
    public String fileName = "";
    private LinearLayout pic_add;
    private String myurl = "";
    private  Button next_bt;
    // private ScrollView activity_selectimg_scrollView;
    private HorizontalScrollView selectimg_horizontalScrollView;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                myurl = (String) msg.obj;
                saveData();
            }
        }

        private void saveData() {
            TwoPost post = new TwoPost();
            post.setTitle(title);// biaoti
            post.setFileName(fileName);
            post.setImage(myurl);// tupian
            post.setContent(comment);// neirong
            post.setCome_from(come_from);//laizi
            post.setMifrom(FROM);
            post.setAuthor(user);
            post.save(ReleaseActivity.this, new SaveListener() {

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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_release);
        initView();
        next_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent service = new Intent(ReleaseActivity.this, ReleaseService.class);
                String[] files = drr.toArray(new String[drr.size()]);
                service.putExtra("files",files);
                startService(service);
                setResult(RESULT_OK);
                ReleaseActivity.this.finish();

            }
        });
















        initTopBarForBoth(FROM, R.drawable.btn_login_n, "提交",
                new onRightImageButtonClickListener() {

                    @Override
                    public void onClick() {
                        come_from = come_from_content.getText().toString();
                        comment = comment_content.getText().toString();
                        title = title_content.getText().toString();
                        if (TextUtils.isEmpty(title)) {
                            toast("请输入标题");
                            return;
                        }
                        if (TextUtils.isEmpty(comment)) {
                            toast("请输入内容");
                            return;
                        }
                        if (TextUtils.isEmpty(come_from)) {
                            toast("请输入乡镇");
                            return;
                        }

                        if (bmp.size() != 3) {
                            toast("每次发布三张图片");
                            return;
                        } else {
                       /*     dialog.show();
                            String[] files = drr.toArray(new String[drr.size()]);
                            BmobProFile.getInstance(ReleaseActivity.this)
                                    .uploadBatch(files,
                                            new UploadBatchListener() {

                                                @Override
                                                public void onError(int arg0,
                                                                    String arg1) {
                                                    toast(arg1);

                                                }

                                                @Override
                                                public void onSuccess(
                                                        boolean isFinish,
                                                        String[] fileNames,
                                                        String[] urls, BmobFile[] files) {
                                                    Log.e("String[] fileNames",
                                                            fileNames
                                                                    .toString()
                                                                    + ""
                                                                    + urls.toString());
                                                    if (isFinish) {
                                                        toast("finish");
                                                        StringBuffer sb = new StringBuffer();
                                                        for (int i = 0; i < fileNames.length; i++) {
                                                            sb.append(fileNames[i] + ",");
                                                        }
                                                        fileName = sb
                                                                .toString();
                                                        String urlss = "";
                                                        for (int i = 0; i < drr
                                                                .size(); i++) {

                                                            if (i == 0) {
                                                                urlss = urlss
                                                                        + BmobProFile
                                                                        .getInstance(
                                                                                ReleaseActivity.this)
                                                                        .signURL(
                                                                                fileNames[i],
                                                                                urls[i],
                                                                                Config.AccessKey,
                                                                                Config.effectTime,
                                                                                Config.SecretKey);
                                                            } else {
                                                                urlss = urlss
                                                                        + ","
                                                                        + BmobProFile
                                                                        .getInstance(
                                                                                ReleaseActivity.this)
                                                                        .signURL(
                                                                                fileNames[i],
                                                                                urls[i],
                                                                                Config.AccessKey,
                                                                                Config.effectTime,
                                                                                Config.SecretKey);
                                                            }

                                                        }
                                                        Message message = new Message();
                                                        message.obj = urlss;
                                                        message.what = 1;
                                                        handler.sendMessage(message);
                                                    }
                                                }

                                                @Override
                                                public void onProgress(
                                                        int arg0, int arg1,
                                                        int arg2, int arg3) { // 进度

                                                }
                                            });*/

                        }

                    }

                });


    }

    public void initView() {
        dp = getResources().getDimension(R.dimen.dp);
        FROM = getIntent().getExtras().getString("FROM");
        next_bt  = (Button) findViewById(R.id.next_bt);
        title_content = (EditText) findViewById(R.id.title_content);
        comment_content = (EditText) findViewById(R.id.comment_content);
        come_from_content = (EditText) findViewById(R.id.come_from);
        title_content.setFocusable(true);
        title_content.setFocusableInTouchMode(true);

        selectimg_horizontalScrollView = (HorizontalScrollView) findViewById(R.id.selectimg_horizontalScrollView);
        gridview = (GridView) findViewById(R.id.noScrollgridview);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridviewInit();
    }

    public void gridviewInit() {
        adapter = new GridAdapter(this);
        adapter.setSelectedPosition(0);
        int size = 0;
        if (bmp.size() < 6) {
            size = bmp.size() + 1;
        } else {
            size = bmp.size();
        }
        LayoutParams params = gridview.getLayoutParams();
        final int width = size * (int) (dp * 9.4f);
        params.width = width;
        gridview.setLayoutParams(params);
        gridview.setColumnWidth((int) (dp * 9.4f));
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        selectimg_horizontalScrollView.getViewTreeObserver()
                .addOnPreDrawListener(// 绘制完毕
                        new OnPreDrawListener() {
                            public boolean onPreDraw() {
                                selectimg_horizontalScrollView.scrollTo(width,
                                        0);
                                selectimg_horizontalScrollView
                                        .getViewTreeObserver()
                                        .removeOnPreDrawListener(this);
                                return false;
                            }
                        });
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater listContainer;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public class ViewHolder {
            public ImageView image;
            public Button bt;
        }

        public GridAdapter(Context context) {
            listContainer = LayoutInflater.from(context);
        }

        public int getCount() {
            if (bmp.size() < 6) {
                return bmp.size() + 1;
            } else {
                return bmp.size();
            }
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int sign = position;
            // 自定义视图
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                // 获取list_item布局文件的视图

                convertView = listContainer.inflate(
                        R.layout.item_published_grida, null);

                // 获取控件对象
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.bt = (Button) convertView
                        .findViewById(R.id.item_grida_bt);
                // 设置控件集到convertView
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                holder.bt.setVisibility(View.GONE);
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(bmp.get(position));
                holder.bt.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        PhotoActivity.bitmap.remove(sign);
                        bmp.get(sign).recycle();
                        bmp.remove(sign);
                        drr.remove(sign);

                        gridviewInit();
                    }
                });
            }

            return convertView;
        }
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.select_popupwindows,
                    null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_2));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    photo();// 拍照
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(
                            // 相册
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    private static final int SELECTIMG_SEARCH = 3;
    private String path = "";
    private Uri photoUri;

    // 进入相机拍照
    public void photo() {
        try {
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);

            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = android.os.Environment
                    .getExternalStorageDirectory().getPath()
                    + "/xiajin/"
                    + "/photo/";
            // String sdcardPathDir = FileUtil.SDPATH1;
            File file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                // 有sd卡，是否有myImage文件夹
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                // 是否有headImg文件
                file = new File(sdcardPathDir + System.currentTimeMillis()
                        + ".jpg");
            }
            if (file != null) {
                path = file.getPath();
                photoUri = Uri.fromFile(file);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (drr.size() < 6 && resultCode == -1) {// 拍照

                    startPhotoZoom(photoUri);
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (drr.size() < 6 && resultCode == RESULT_OK && null != data) {// 相册返回
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case CUT_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                    Bitmap bitmap = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
                    PhotoActivity.bitmap.add(bitmap);
                    bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
                            (int) (dp * 1.6f));
                    bmp.add(bitmap);
                    gridviewInit();
                }
                break;
            case SELECTIMG_SEARCH:
                if (resultCode == RESULT_OK && null != data) {
                    String text = "#" + data.getStringExtra("topic") + "#";
                    text = comment_content.getText().toString() + text;
                    comment_content.setText(text);

                    comment_content.getViewTreeObserver().addOnPreDrawListener(
                            new OnPreDrawListener() {
                                public boolean onPreDraw() {
                                    comment_content.setEnabled(true);
                                    // 设置光标为末尾
                                    CharSequence cs = comment_content.getText();
                                    if (cs instanceof Spannable) {
                                        Spannable spanText = (Spannable) cs;
                                        Selection.setSelection(spanText,
                                                cs.length());
                                    }
                                    comment_content.getViewTreeObserver()
                                            .removeOnPreDrawListener(this);
                                    return false;
                                }
                            });

                }

                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        try {
            /**** 判断目录是否已存在 ****/
            String sdcardPathDir = android.os.Environment
                    .getExternalStorageDirectory().getPath()
                    + "/xiajin/"
                    + "/photo/";
            // String sdcardPathDir = FileUtil.SDPATH1;
            File file = null;
            // 有sd卡，是否有myImage文件夹
            File fileDir = new File(sdcardPathDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            /**** 判断目录是否已存在end ****/

            // 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyyMMddhhmmss");
            String address = sDateFormat.format(new java.util.Date());
            if (!FileUtil.isFileExist("")) {
                FileUtil.createSDDir("");

            }
            drr.add(FileUtil.SDPATH + address + ".jpg");

            // Uri imageUri = Uri.parse("file:///sdcard/formats/" + address
            // + ".jpg");
            // 这里需要和FileUtil.SDPATH 一致，而且在之前要创建文件夹
            Uri imageUri = Uri.parse("file:///sdcard/" + "/xiajin/"
                    + "/photo/xiajin" + address + ".jpg");
            System.out.println("uri====" + FileUtil.SDPATH + address + ".jpg");

            final Intent intent = new Intent("com.android.camera.action.CROP");
            // 照片URL地址
            intent.setDataAndType(uri, "image/*");

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 720);
            intent.putExtra("outputY", 720);
            // 输出路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 输出格式
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            // 不启用人脸识别
            intent.putExtra("noFaceDetection", false);
            intent.putExtra("return-data", false);
            startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {

        FileUtil.deleteDir(FileUtil.SDPATH);
        FileUtil.deleteDir(FileUtil.SDPATH1);
        // 清理图片缓存
        for (int i = 0; i < bmp.size(); i++) {
            bmp.get(i).recycle();
        }
        for (int i = 0; i < PhotoActivity.bitmap.size(); i++) {
            PhotoActivity.bitmap.get(i).recycle();
        }
        PhotoActivity.bitmap.clear();
        bmp.clear();
        drr.clear();
        super.onDestroy();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ReleaseActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (arg2 == bmp.size()) {
            String sdcardState = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                new PopupWindows(ReleaseActivity.this, gridview);
            } else {
                Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(ReleaseActivity.this,
                    PhotoActivity.class);

            intent.putExtra("ID", arg2);
            startActivity(intent);
        }
    }

    @Override
    public void retry() {

    }

    @Override
    public void netError() {

    }
}
