package com.xiajin.home.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

	private String content;//评论内容

	private User user;//评论的用户，Pointer类型，一对一关系

	private TwoPost post; //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个帖子

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TwoPost getPost() {
		return post;
	}

	public void setPost(TwoPost post) {
		this.post = post;
	}

	//自行实现getter和setter方法
}