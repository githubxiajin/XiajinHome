package com.xiajin.home.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by wangning on 15/8/24.
 */
public class CarpoolingInfo extends BmobObject implements Serializable {
    private String start;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private  String end;
    private String type;
    private String phone;
    private User author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户
    private String time;// 帖子内容

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnd() {
        return end;

    }



    public void setEnd(String end) {
        this.end = end;

    }


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }



    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }


}
