package com.festevent.beans;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

	protected User			commenter;
	protected String		content;
	protected String		date;

	public User getCommenter() {
		return commenter;
	}
	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
