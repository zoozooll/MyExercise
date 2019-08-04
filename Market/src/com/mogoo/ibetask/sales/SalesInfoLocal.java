package com.mogoo.ibetask.sales;

import android.text.TextUtils;

public class SalesInfoLocal {
	private String uid;
	private String sim;
	private String imei;
	private String smsc;
	private String ver;
	private String pid;
	private String phone_number;
	private String resolution;
	private String phonetype;

	public SalesInfoLocal(String salesinfo) {

		if (!TextUtils.isEmpty(salesinfo)) {
			String[] stringInfo = salesinfo.split(";");
			String[] strResult;
			if (stringInfo != null) {
				if (stringInfo[0] != null && !TextUtils.isEmpty(stringInfo[0])) {
					strResult = stringInfo[0].split("=");
					if(strResult.length==2){
						uid = strResult[1];
					}
				}

				if (stringInfo[1] != null && !TextUtils.isEmpty(stringInfo[1])) {
					strResult = stringInfo[1].split("=");
					if(strResult.length==2){
						sim = strResult[1];
					}
				}

				if (stringInfo[2] != null && !TextUtils.isEmpty(stringInfo[2])) {
					strResult = stringInfo[2].split("=");
					if(strResult.length==2){
						imei = strResult[1];
					}
				}

				if (stringInfo[3] != null && !TextUtils.isEmpty(stringInfo[3])) {
					strResult = stringInfo[3].split("=");
					if(strResult.length==2){
						smsc = strResult[1];
					}
				}

				if (stringInfo[4] != null && !TextUtils.isEmpty(stringInfo[4])) {
					strResult = stringInfo[4].split("=");
					if(strResult.length==2){
						ver = strResult[1];
					}
				}

				if (stringInfo[5] != null && !TextUtils.isEmpty(stringInfo[5])) {
					strResult = stringInfo[5].split("=");
					if(strResult.length==2){
						pid = strResult[1];
					}
				}

				if (stringInfo[6] != null && !TextUtils.isEmpty(stringInfo[6])) {
					strResult = stringInfo[6].split("=");
					if(strResult.length==2){
						phone_number = strResult[1];
					}
				}

				if (stringInfo[7] != null && !TextUtils.isEmpty(stringInfo[7])) {
					strResult = stringInfo[7].split("=");
					if(strResult.length==2){
						resolution = strResult[1];
					}
				}

				if (stringInfo[8] != null && !TextUtils.isEmpty(stringInfo[8])) {
					strResult = stringInfo[8].split("=");
					if(strResult.length==2){
						phonetype = strResult[1];
					}
				}
			}
		}
	}

	public String getUIDLocal() {

		return uid;
	}

	/**
	 * 获取sim卡号
	 * 
	 * @return
	 */
	public String getSimLocal() {

		return sim;
	}

	/**
	 * 获取imei号
	 * 
	 * @return
	 */
	public String getImeiLocal() {

		return imei;

	}

	/**
	 * 获取smsc
	 * 
	 * @return
	 */
	public String getSmscLocal() {

		return smsc;
	}

	/**
	 * 获取软件版本
	 * 
	 * @return
	 */
	public String getVersionLocal() {

		return ver;
	}

	/**
	 * 获取项目号
	 * 
	 * @return
	 */
	public String getProjectIdLocal() {

		return pid;
	}

	/**
	 * 获取手机号码
	 * 
	 * @return
	 */
	public String getPhoneNumberLocal() {

		return phone_number;
	}

	/**
	 * 获取分辨率
	 * 
	 * @return
	 */
	public String getResolutionLocal() {

		return resolution;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public String getPhoneTypeLocal() {

		return phonetype;
	}

}
