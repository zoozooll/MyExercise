package com.oregonscientific.meep.message.common;

//import com.google.gson.JsonObject;

public class MeepServerMessageSendText extends MeepServerMessage{

	/*private final String STR_RECIPIENT= "recipient";
	private final String STR_SENDER= "sender";
	private final String STR_MESSAGE= "message";*/
		
	
	private String recipient = null;
	private String sender = null;
	private String message = null;
	
	public MeepServerMessageSendText(String proc, String opcode) {
		super(proc, opcode);
		// TODO Auto-generated constructor stub
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/*public String toJsonString() {
		JsonObject json = new JsonObject();
		json.addProperty(STR_PROCESS, getProc());
		json.addProperty(STR_OPCODE, getOpcode());
		json.addProperty(STR_SENDER, getSender());
		json.addProperty(STR_RECIPIENT, getRecipient());
		json.addProperty(STR_MESSAGE, getMessage());
		return json.toString();
	}*/
	

}
