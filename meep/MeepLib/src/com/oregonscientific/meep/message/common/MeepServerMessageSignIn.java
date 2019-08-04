package com.oregonscientific.meep.message.common;

//import com.google.gson.JsonObject;

public class MeepServerMessageSignIn extends MeepServerMessage{

	/*private String STR_TOKEN = "token";
	private String STR_CHECK_SUM = "checksum";*/
	
	private String token = null;
	private String checksum	= null;
	private String first_name = null;
	private String last_name = null;
	private String meeptag = null;
	private String version = null;
	
	
	public MeepServerMessageSignIn(String proc, String opcode) {
		super(proc, opcode);
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	/*public String toJsonString() {
		JsonObject json = new JsonObject();
		json.addProperty(STR_PROCESS, getProc());
		json.addProperty(STR_OPCODE, getOpcode());
		json.addProperty(STR_TOKEN, getToken());
		json.addProperty(STR_CHECK_SUM, getChecksum());
		return json.toString();
	}*/

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

	public String getMeeptag() {
		return meeptag;
	}

	public void setMeeptag(String meeptag) {
		this.meeptag = meeptag;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
