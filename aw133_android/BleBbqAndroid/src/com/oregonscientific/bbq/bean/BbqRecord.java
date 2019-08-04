/**
 * 
 */
package com.oregonscientific.bbq.bean;

import com.oregonscientific.bbq.bean.BBQDataSet.CookingStatus;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;

/**
 * @author aaronli
 *
 */
public class BbqRecord {

	private long id;
	
	private long finishedDate;
	
	private int channel;
	
	private Mode mode;
	
	private float finishedTemperature;
	
	private int costTime;
	
	private CookingStatus cookingState;
	
	private String graphyPath;
	
	private String temperaturesFilepath;
	
	private String memo;
	
	private int setMeattype;
	
	private DonenessLevel setDoneness;
	
	private float setTargeTemperature;
	
	private int setTotalTime;
	
	private float setDonenessR;
	
	private float setDonenessRM;
	
	private float setDonenessM;
	
	private float setDonenessMW;
	
	private float setDonenessW;
		
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the finishedDate
	 */
	public long getFinishedDate() {
		return finishedDate;
	}

	/**
	 * @param finishedDate the finishedDate to set
	 */
	public void setFinishedDate(long finishedDate) {
		this.finishedDate = finishedDate;
	}

	/**
	 * @return the finishedTemperature
	 */
	public float getFinishedTemperature() {
		return finishedTemperature;
	}

	/**
	 * @param finishedTemperature the finishedTemperature to set
	 */
	public void setFinishedTemperature(float finishedTemperature) {
		this.finishedTemperature = finishedTemperature;
	}

	/**
	 * @return the costTime
	 */
	public int getCostTime() {
		return costTime;
	}

	/**
	 * @param costTime the costTime to set
	 */
	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}

	/**
	
	public String getcook_temperatures_sub() {
		return cook_temperatures_sub;
	}
	public void setcook_temperatures_sub(String cook_temperatures_sub) {
		this.cook_temperatures_sub = cook_temperatures_sub;
	}
 */
	public String getGraphyPath() {
		return graphyPath;
	}

	public void setGraphyPath(String graphyPath) {
		this.graphyPath = graphyPath;
	}
	/**
	 * @return the temperaturesFilepath
	 */
	public String getTemperaturesFilepath() {
		return temperaturesFilepath;
	}

	/**
	 * @param temperaturesFilepath the temperaturesFilepath to set
	 */
	public void setTemperaturesFilepath(String temperaturesFilepath) {
		this.temperaturesFilepath = temperaturesFilepath;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the setMeattype
	 */
	public int getSetMeattype() {
		return setMeattype;
	}

	/**
	 * @param setMeattype the setMeattype to set
	 */
	public void setSetMeattype(int setMeattype) {
		this.setMeattype = setMeattype;
	}


	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}


	/**
	 * @return the setTargeTemperature
	 */
	public float getSetTargeTemperature() {
		return setTargeTemperature;
	}

	/**
	 * @param setTargeTemperature the setTargeTemperature to set
	 */
	public void setSetTargeTemperature(float setTargeTemperature) {
		this.setTargeTemperature = setTargeTemperature;
	}

	/**
	 * @return the setTotalTime
	 */
	public float getSetTotalTime() {
		return setTotalTime;
	}

	/**
	 * @param setTotalTime the setTotalTime to set
	 */
	public void setSetTotalTime(int setTotalTime) {
		this.setTotalTime = setTotalTime;
	}

	/**
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * @return the cookingState
	 */
	public CookingStatus getCookingState() {
		return cookingState;
	}

	/**
	 * @param cookingState the cookingState to set
	 */
	public void setCookingState(CookingStatus cookingState) {
		this.cookingState = cookingState;
	}

	/**
	 * @return the setDoneness
	 */
	public DonenessLevel getSetDoneness() {
		return setDoneness;
	}

	/**
	 * @param setDoneness the setDoneness to set
	 */
	public void setSetDoneness(DonenessLevel setDoneness) {
		this.setDoneness = setDoneness;
	}

	/**
	 * @return the setDonenessR
	 */
	public float getSetDonenessR() {
		return setDonenessR;
	}

	/**
	 * @param setDonenessR the setDonenessR to set
	 */
	public void setSetDonenessR(float setDonenessR) {
		this.setDonenessR = setDonenessR;
	}

	/**
	 * @return the setDonenessRM
	 */
	public float getSetDonenessRM() {
		return setDonenessRM;
	}

	/**
	 * @param setDonenessRM the setDonenessRM to set
	 */
	public void setSetDonenessRM(float setDonenessRM) {
		this.setDonenessRM = setDonenessRM;
	}

	/**
	 * @return the setDonenessM
	 */
	public float getSetDonenessM() {
		return setDonenessM;
	}

	/**
	 * @param setDonenessM the setDonenessM to set
	 */
	public void setSetDonenessM(float setDonenessM) {
		this.setDonenessM = setDonenessM;
	}

	/**
	 * @return the setDonenessMW
	 */
	public float getSetDonenessMW() {
		return setDonenessMW;
	}

	/**
	 * @param setDonenessMW the setDonenessMW to set
	 */
	public void setSetDonenessMW(float setDonenessMW) {
		this.setDonenessMW = setDonenessMW;
	}

	/**
	 * @return the setDonenessW
	 */
	public float getSetDonenessW() {
		return setDonenessW;
	}

	/**
	 * @param setDonenessW the setDonenessW to set
	 */
	public void setSetDonenessW(float setDonenessW) {
		this.setDonenessW = setDonenessW;
	}
	
	
}
