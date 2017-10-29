package com.iskinfor.servicedata.usercenter.service;

import java.util.Map;

public interface IManagerUserInfor0300030001 {
	/**
	 * 用户支付
	 * 
	 * @param userId
	 *            用户的ID
	 * @param money
	 *            交易的金额
	 * @return true支付成功 false支付失败
	 */
	public boolean business(String userId, String money, String payword) throws Exception;
	/**
	 * 为自己充值
	 * @param userid 用户id
	 * @param cardNo 充值卡卡号
	 * @return true支付成功 false支付失败
	 * @throws Exception
	 */
	public boolean rechargeSelf(String userid,String cardNo)throws Exception;
	/**
	 * 为他人充值
	 * @param userid 用户id
	 * @param objectId 充值对象的id
	 * @param cardNo 充值卡号
	 * @return true 支付成功false支付失败
	 * @throws Exception
	 */
	public boolean rechargeOther(String userid,String objectId,String cardNo)throws Exception;
	/**
	 * 修改用户信息
	 * @param useid
	 * @param nickNmae
	 * @param schoolid
	 * @param motto
	 * @param classid
	 * @param qq
	 * @param email
	 * @param phone
	 * @param gradeId
	 * @param cityId
	 * @param arealId
	 * @param provinceId
	 * @param birthdate
	 * @param sex
	 * @param designation
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public boolean updateUseInfor(String useid,String nickNmae,String schoolid,String motto,String classid,String qq,String email,String phone,String gradeId,String cityId,String arealId,String provinceId,String birthdate,String sex,String designation,String courseId)throws Exception;
	/**
	 * 修改密码
	 * @param user_id 用户id
	 * @param oldPassword 目前的密码
	 * @param newPassWord 新密码
	 * @return 修改成功返回true 修改该失败返回false
	 * @throws Exception
	 */
	public boolean modifyPassword(String user_id,String oldPassword,String newPassWord) throws Exception;
}
