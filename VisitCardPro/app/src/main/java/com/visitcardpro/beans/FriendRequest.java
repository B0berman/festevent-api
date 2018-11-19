package com.visitcardpro.beans;

import java.util.Date;
import java.util.List;


public class FriendRequest {

	protected String sender;
	protected String target;
	protected Date   date = new Date();
	protected List<User> commonFriends;
	
	public FriendRequest(String s, String t, List<User> common) {
		sender = s;
		target = t;
		commonFriends = common;
	}

	public FriendRequest() {
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<User> getCommonFriends() {
		return commonFriends;
	}

	public void setCommonFriends(List<User> commonFriends) {
		this.commonFriends = commonFriends;
	}
	
	
}
