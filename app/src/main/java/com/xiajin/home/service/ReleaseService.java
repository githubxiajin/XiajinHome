package com.xiajin.home.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.xiajin.home.bean.TwoPost;
import com.xiajin.home.bean.User;
import com.xiajin.home.config.Config;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by liu on 2015/10/12.
 */
public class ReleaseService extends Service {
    public String fileName = "";
    private String myurl = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override

    public void onCreate() {
        super.onCreate();
    }

    @Override

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] files = intent.getStringArrayExtra("files");
        BmobProFile.getInstance(ReleaseService.this)
                .uploadBatch(files,
                        new UploadBatchListener() {

                            @Override
                            public void onError(int arg0,
                                                String arg1) {
                                Toast.makeText(ReleaseService.this, arg1, Toast.LENGTH_LONG).show();
                                Intent mIntent = new Intent(Config.ACTION_NAME);
                                mIntent.putExtra("result", "fail");

                                //发送广播
                                sendBroadcast(mIntent);
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
                                    Toast.makeText(ReleaseService.this, "finish", Toast.LENGTH_LONG).show();
                                    StringBuffer sb = new StringBuffer();
                                    for (int i = 0; i < fileNames.length; i++) {
                                        sb.append(fileNames[i] + ",");
                                    }
                                    fileName = sb
                                            .toString();
                                    String urlss = "";
                                    for (int i = 0; i < files
                                            .length; i++) {

                                        if (i == 0) {
                                            urlss = urlss
                                                    + BmobProFile
                                                    .getInstance(
                                                            ReleaseService.this)
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
                                                            ReleaseService.this)
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
                        });


        return super.onStartCommand(intent, flags, startId);
    }

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
            post.setTitle("title");// biaoti
            post.setFileName(fileName);
            post.setImage(myurl);// tupian
            post.setContent("aaaaaaaaaaa");// neirong
            post.setCome_from("bbbbbbbbbbb");//laizi
            post.setMifrom("DERIVATIVE");
            post.setAuthor(new User());
            post.save(ReleaseService.this, new SaveListener() {

                @Override
                public void onSuccess() {
                    Intent mIntent = new Intent(Config.ACTION_NAME);
                    mIntent.putExtra("result", "success");

                    //发送广播
                    sendBroadcast(mIntent);

                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    Intent mIntent = new Intent(Config.ACTION_NAME);
                    mIntent.putExtra("result", "fail");

                    //发送广播
                    sendBroadcast(mIntent);

                }
            });

        }
    };
}




