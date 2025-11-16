package com.ytload.dto;

public class RequestDTO {

	private String url;
	private String type;
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RequestDTO(String url) {
		super();
		this.url = url;
	}

	public RequestDTO() {
		super();
	}

}
