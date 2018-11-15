package com.eip.festevent.beans;

import com.google.common.collect.Lists;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;
import java.util.List;


@Entity
public class FriendRequest {

	protected String sender;
	protected String target;
	protected Date   date = new Date();
	protected List<User> commonFriends = Lists.newArrayList();
	
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
