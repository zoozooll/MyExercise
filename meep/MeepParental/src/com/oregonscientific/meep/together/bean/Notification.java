package com.oregonscientific.meep.together.bean;

public class Notification {
	public static String S_TYPE_STORE_PURCHASE="store-purchase";
	public static String S_TYPE_FRIEND_REQUEST="friend-request";
	public static String S_TYPE_GOOGLEPLAY_DOWNLOAD="googleplay-download";
	public static String S_TYPE_COIN_REQUEST="coin-request";
	public static String S_APPROVAL_APPROVE="APPROVE";
	public static String S_APPROVAL_REJECT="REJECT";
	public static String S_APPROVAL_PENDING="PENDING";

	private String id;
	private String read_details;
	private String message;
	private String approval;
	private String event_time;
	private String type;
	private String log_time;
	private String details;
	private String requester;
	private String requestee;
	
	
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEvent_time() {
		return event_time;
	}
	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}
	
	public String getRead_details() {
		return read_details;
	}
	public void setRead_details(String read_details) {
		this.read_details = read_details;
	}
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getRequestee() {
		return requestee;
	}
	public void setRequestee(String requestee) {
		this.requestee = requestee;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLog_time() {
		return log_time;
	}
	public void setLog_time(String log_time) {
		this.log_time = log_time;
	}
	
}
