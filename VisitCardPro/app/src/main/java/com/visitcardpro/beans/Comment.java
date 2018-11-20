package com.visitcardpro.beans;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

	protected User			commenter;
	protected String		content;
	protected Date			date = new Date();

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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
