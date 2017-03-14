package com.cof.model;

public class User {
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Args getArgs() {
		return args;
	}

	public void setArgs(Args args) {
		this.args = args;
	}

	private String email;
	private String password;
	private Args args;
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("***** User Details *****\n");
		sb.append("email="+getEmail()+"\n");
		sb.append("password="+getPassword()+"\n");
		sb.append("args="+getArgs()+"\n");
		sb.append("\n*****************************");
		
		return sb.toString();
	}
}
