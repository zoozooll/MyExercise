package com.oregonscientific.meep.together.bean;

public class RegisterUser 
{
	private String first_name;
	private String last_name;
	private String email;
	private String password;
	private String gender;
	private String tel;
	private String address;
	private String store_country;
	private String dob;
	private String avatar;
	
	
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	private boolean promotion_optin;
	private boolean tns_acceptance;
	
	private String serial_no;
	
	public String getSerial_no() {
		return serial_no;
	}
	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}
	public static String MALE = "M";
	public static String FEMALE = "F";
	
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStore_country() {
		return store_country;
	}
	public void setStore_country(String store_country) {
		this.store_country = store_country;
	}
	public boolean getPromotion_optin() {
		return promotion_optin;
	}
	public void setPromotion_optin(boolean promotion_optin) {
		this.promotion_optin = promotion_optin;
	}
	public boolean getTns_acceptance() {
		return tns_acceptance;
	}
	public void setTns_acceptance(boolean tns_acceptance) {
		this.tns_acceptance = tns_acceptance;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
}

