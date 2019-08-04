package com.oregonscientific.meep.together.bean;

public class ResponseLogin extends ResponseBasic{
	
	//login
	public static String S_TOKEN ="token";
	public static String S_FIRST_NAME ="first_name";
	public static String S_LAST_NAME ="last_name";
	public static String S_AVATAR ="avatar";
	public static String S_COINS ="coins";
	public static String S_STAGE ="stage";
	public static String STAGE_NORMAL ="normal";
	public static String STAGE_VERIFY_EMAIL ="verify_email";
	public static String STAGE_VERIFY_CRIDITCARD ="verify_creditcard";
	public static String STAGE_CREATE_ACCOUNT ="create_account";
	public static String STAGE_OFFLINE ="offline";
	
	private String token;
	private String first_name;
	private String last_name;
	private String avatar;
	private String coins;
	private String stage;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getCoins() {
		return coins;
	}
	public void setCoins(String coins) {
		this.coins = coins;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	
	
	
}
