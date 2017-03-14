package com.cof.model;

public class Args {
	
	private java.lang.Number uid;
	private String token;
	private String api_token;
	private boolean json_strict_mode;
	private boolean json_verbose_response;
	
	public java.lang.Number getUid() {
		return uid;
	}

	public void setUid(java.lang.Number uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getApi_token() {
		return api_token;
	}

	public void setApi_token(String api_token) {
		this.api_token = api_token;
	}

	public boolean isJson_strict_mode() {
		return json_strict_mode;
	}

	public void setJson_strict_mode(boolean json_strict_mode) {
		this.json_strict_mode = json_strict_mode;
	}

	public boolean isJson_verbose_response() {
		return json_verbose_response;
	}

	public void setJson_verbose_response(boolean json_verbose_response) {
		this.json_verbose_response = json_verbose_response;
	}

	@Override
	public String toString(){
		return getUid()+","+getToken()+","+getApi_token()+","+isJson_strict_mode()+","+isJson_verbose_response();
	}
}
