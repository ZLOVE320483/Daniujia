package com.xiaojia.daniujia.domain;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo {
	@JSONField(name = "id")
	private int userId;
	@JSONField(name = "username")
	private String username;
	private String name;
	@JSONField(name = "imgUrl")
	private String head;
	private int identity;
	@JSONField(name = "password")
	private String password;
	private int applyStep;
	private int redirectToRegister = -1;
	private int hashId;

	public int getHashId() {
		return hashId;
	}

	public void setHashId(int hashId) {
		this.hashId = hashId;
	}

	public int getApplyStep() {
		return applyStep;
	}

	public void setApplyStep(int applyStep) {
		this.applyStep = applyStep;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return TextUtils.isEmpty(name)?username:name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRedirectToRegister() {
		return redirectToRegister;
	}

	public void setRedirectToRegister(int redirectToRegister) {
		this.redirectToRegister = redirectToRegister;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", name='" + name + '\'' +
				", head='" + head + '\'' +
				", identity=" + identity +
				", password='" + password +
				'}';
	}

	public String getUserIdStr() {
		return String.valueOf(userId);
	}
}
