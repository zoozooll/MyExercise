package com.oregonscientific.meep.together.bean;

public class LoginUser {
	
	private String email;
	private String password;
	private String challenge;
	public LoginUser()
	{
	}
	
	public LoginUser(String email,String password)
	{
		this.email = email;
		this.password = password;
	}
	
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
	public String getChallenge() {
		return challenge;
	}
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
}
