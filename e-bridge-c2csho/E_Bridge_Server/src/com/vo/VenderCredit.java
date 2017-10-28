package com.vo;

/** 
 * ¬Ùº“–≈”√
 */

public class VenderCredit implements java.io.Serializable {

	// Fields

	private Integer venCreId;
	private Integer venId;
	private String venCreRank;
	private String venCreTime;
	private Double venCreMoney;
	private Double venCreBalance;
	private Integer venCreReturnpoint;

	// Constructors

	/** default constructor */
	public VenderCredit() {
	}

	/** minimal constructor */
	public VenderCredit(Integer venId) {
		this.venId = venId;
	}

	/** full constructor */
	public VenderCredit(Integer venId, String venCreRank, String venCreTime,
			Double venCreMoney, Double venCreBalance, Integer venCreReturnpoint) {
		this.venId = venId;
		this.venCreRank = venCreRank;
		this.venCreTime = venCreTime;
		this.venCreMoney = venCreMoney;
		this.venCreBalance = venCreBalance;
		this.venCreReturnpoint = venCreReturnpoint;
	}

	// Property accessors

	public Integer getVenCreId() {
		return this.venCreId;
	}

	public void setVenCreId(Integer venCreId) {
		this.venCreId = venCreId;
	}

	public Integer getVenId() {
		return this.venId;
	}

	public void setVenId(Integer venId) {
		this.venId = venId;
	}

	public String getVenCreRank() {
		return this.venCreRank;
	}

	public void setVenCreRank(String venCreRank) {
		this.venCreRank = venCreRank;
	}

	public String getVenCreTime() {
		return this.venCreTime;
	}

	public void setVenCreTime(String venCreTime) {
		this.venCreTime = venCreTime;
	}

	public Double getVenCreMoney() {
		return this.venCreMoney;
	}

	public void setVenCreMoney(Double venCreMoney) {
		this.venCreMoney = venCreMoney;
	}

	public Double getVenCreBalance() {
		return this.venCreBalance;
	}

	public void setVenCreBalance(Double venCreBalance) {
		this.venCreBalance = venCreBalance;
	}

	public Integer getVenCreReturnpoint() {
		return this.venCreReturnpoint;
	}

	public void setVenCreReturnpoint(Integer venCreReturnpoint) {
		this.venCreReturnpoint = venCreReturnpoint;
	}

}