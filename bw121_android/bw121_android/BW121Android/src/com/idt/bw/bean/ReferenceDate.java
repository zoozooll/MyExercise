/**
 * 
 */
package com.idt.bw.bean;

import java.util.Date;

/**
 * @author aaronli
 *
 */
public class ReferenceDate {
	
	private Date period;
	private float value;
	
	
	/**
	 * @param period
	 * @param value
	 */
	public ReferenceDate(Date period, float value) {
		super();
		this.period = period;
		this.value = value;
	}
	/**
	 * 
	 */
	public ReferenceDate() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the period
	 */
	public Date getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(Date period) {
		this.period = period;
	}
	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}
	
}
