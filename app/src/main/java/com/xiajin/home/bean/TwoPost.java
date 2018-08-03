package com.xiajin.home.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class TwoPost extends BmobObject implements Serializable {



	public String getCome_from() {
		return come_from;
	}

	public void setCome_from(String come_from) {
		this.come_from = come_from;
	}

	private String title;//帖子标题
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String content;// 帖子内容

	private  String mifrom;

	public String getMifrom() {
		return mifrom;
	}

	public void setMifrom(String mifrom) {
		this.mifrom = mifrom;
	}

	public String come_from;
	private User author;//帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

	private String image;//帖子图片

	private BmobRelation likes;//多对多关系：用于存储喜欢该帖子的所有用户


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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}


	//自行实现getter和setter方法

}