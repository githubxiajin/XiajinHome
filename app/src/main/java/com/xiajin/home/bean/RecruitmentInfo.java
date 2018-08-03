package com.xiajin.home.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by liu on 2015/8/25.
 */
public class RecruitmentInfo extends BmobObject implements Serializable {
    private String title;//帖子标题
    private String content;// 帖子内容
    public String address;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String phone;
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String price;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    private User author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

}
