package com.vo;

import java.util.Set;

/**
 * ÇþµÀ
 */

public class Channel implements java.io.Serializable {

	// Fields

	private Integer chaId;
	private String channelcode;
	private String channelname;
	private Set<Admin> admins; 
	// Constructors

	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	/** default constructor */
	public Channel() {
	}

	/** full constructor */
	public Channel(String channelcode, String channelname) {
		this.channelcode = channelcode;
		this.channelname = channelname;
	}

	// Property accessors

	public Integer getChaId() {
		return this.chaId;
	}

	public void setChaId(Integer chaId) {
		this.chaId = chaId;
	}

	public String getChannelcode() {
		return this.channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getChannelname() {
		return this.channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

}