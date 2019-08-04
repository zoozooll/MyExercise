package com.oregonscientific.meep.together.bean;

public class RegisterUserCombined extends RegisterUser {
	private String kid_first_name;
	private String kid_last_name;
	private String kid_dob;
	public String getKid_first_name() {
		return kid_first_name;
	}
	public void setKid_first_name(String kid_first_name) {
		this.kid_first_name = kid_first_name;
	}
	public String getKid_last_name() {
		return kid_last_name;
	}
	public void setKid_last_name(String kid_last_name) {
		this.kid_last_name = kid_last_name;
	}
	public String getKid_dob() {
		return kid_dob;
	}
	public void setKid_dob(String kid_dob) {
		this.kid_dob = kid_dob;
	}
}
