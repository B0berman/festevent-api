package com.visitcardpro.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.mongodb.morphia.annotations.Entity;

import java.util.UUID;

@Entity
public class Media {

	public enum TYPE {
		IMAGE_PNG,
		IMAGE_JPG,
		VIDEO,
		GIF
	}

	protected TYPE			type;
	protected String		url;
	protected String		id = UUID.randomUUID().toString();

	protected byte[]		bytes;

	public byte[] getBytes() {
		return bytes;
	}
	
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String title) {
		this.id = title;
	}
}
