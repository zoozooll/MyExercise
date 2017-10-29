package com.iskinfor.servicedata.usercenter.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.datahelp.SysHeader;
import com.iskinfor.servicedata.usercenter.service.IManagerUserInfor0300030001;

public class ManagerUserInfor0300030001Impl implements IManagerUserInfor0300030001{

	
	/**
	 * 充值
	 * @param userid   用户id
	 * @param objectId 充值对象的id
	 * @param cardNo	充值卡号
	 * @param objectType 对象类型00:本人 01:他人
	 * @return
	 */
	private boolean recharge(String userid,String objectId, String cardNo, String objectType) {
		//组头
		CompositeData header = CdUtil.sysHeaderInstance("0300030001", "02"); // header
		// 传参数
		Map<String,Object> args=new HashMap<String, Object>();
		if(null!=userid&&!"".equals(userid)){
			args.put("USER_ID", userid);
		}
		if(null!=objectId&&!"".equals(objectId)){
			args.put("OBJECT_ID", objectId);
		}
		if(null!=cardNo&&!"".equals(cardNo)){
			args.put("CARD_NO", cardNo);
		}
		if(null!=objectType&&!"".equals(objectType)){
			args.put("OBJECT_TYPE", objectType);
		}
	
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request03(reqData);
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		SysHeader sysHeader = CdUtil.verifyHeader(rspData);
		if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
				&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean rechargeOther(String userid, String objectId, String cardNo)
			throws Exception {
		return recharge(userid,objectId,cardNo,"01");
	}
	@Override
	public boolean rechargeSelf(String userid, String cardNo) throws Exception {
		return  recharge(userid,null,cardNo,"00");
	}
	@Override
	public boolean business(String userId, String money, String payword)
			throws Exception {
		// 组头
		CompositeData header = CdUtil.sysHeaderInstance("0300030001", "01"); // header
		// 传参数
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("USER_ID", userId);
		args.put("OPR_NUM", money);
		if (!"".equals(payword) && null != payword) {
			args.put("PAY_PWD", payword);
		}
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request03(reqData);
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		SysHeader sysHeader = CdUtil.verifyHeader(rspData);
		if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
				&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean updateUseInfor(String useid, String nickNmae,
			String schoolid, String motto, String classid, String qq,
			String email, String phone, String gradeId, String cityId,
			String arealId, String provinceId, String birthdate, String sex,
			String designation, String courseId) throws Exception {
		// 组头
		CompositeData header = CdUtil.sysHeaderInstance("0300030001", "03"); // header
		// 传参数
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(useid) && null != useid) {
			args.put("USER_ID", useid);
		}
		if (!"".equals(nickNmae) && null != nickNmae) {
			args.put("NICK_NAME", nickNmae);
		}
		if (!"".equals(schoolid) && null != schoolid) {
			args.put("SCHOOL_ID", schoolid);
		}
		if (!"".equals(motto) && null != motto) {
			args.put("MOTTO", motto);
		}
		if (!"".equals(classid) && null != classid) {
			args.put("CLASS_ID", classid);
		}
		if (!"".equals(qq) && null != qq) {
			args.put("QQ", qq);
		}
		if (!"".equals(email) && null != email) {
			args.put("EMAIL", email);
		}
		if (!"".equals(phone) && null != phone) {
			args.put("PHONE", phone);
		}
		if (!"".equals(gradeId) && null != gradeId) {
			args.put("GRADE_ID", gradeId);
		}
		if (!"".equals(cityId) && null != cityId) {
			args.put("CITY_ID", cityId);
		}
		if (!"".equals(arealId) && null != arealId) {
			args.put("AREAL_ID", arealId);
		}
		if (!"".equals(provinceId) && null != provinceId) {
			args.put("PROVINCE_ID", provinceId);
		}
		if (!"".equals(birthdate) && null != birthdate) {
			args.put("BIRTH_DATE", birthdate);
		}
		if (!"".equals(sex) && null != sex) {
			args.put("SEX", sex);
		}
		if (!"".equals(designation) && null != designation) {
			args.put("DESIGNATION_ID", designation);
		}
		if (!"".equals(courseId) && null != courseId) {
			args.put("COURSE_ID", courseId);
		}
		
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request03(reqData);
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		SysHeader sysHeader = CdUtil.verifyHeader(rspData);
		if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
				&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean modifyPassword(String userId, String oldPassword,
			String newPassWord) throws Exception {
		// 组头
		CompositeData header = CdUtil.sysHeaderInstance("0300030001", "05"); // header
		// 传参数
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("USER_ID", "userId");
		args.put("PASSWORD", "oldPassword");
		args.put("PASSWORD1", "newPassWord");
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request03(reqData);
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		SysHeader sysHeader = CdUtil.verifyHeader(rspData);
		if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
				&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
			return true;
		} else {
			return false;
		}
	}
	
}
