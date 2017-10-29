package com.iskinfor.servicedata.usercenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.pojo.Teacher;
import com.iskinfor.servicedata.pojo.User;

public interface IQuerryUserInfor0300020001 {
	/**
	 * 用户登录
	 * @param userName 用户名
	 * @param password 用户密码
	 * @return true登录成功 false登录失败
	 * @throws Exception
	 */
	public boolean Login(String userName,String password) throws TimeoutException, Exception;
	/**
	 * 根据课程id号查询名师列表
	 * @param cursor_id
	 * 00:语文01:英语02:数学03:物理04:化学05:科学
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Teacher> getFamlesTeachers(String cursor_id) throws TimeoutException ,Exception;
	/**
	 * 得到交易记录
	 * @param userid 用户的id
	 * @param startDate 起止日期YYYY-MM-DD
	 * @param endDate 终止日期YYYY-MM-DD
	 * @param showLineNum 每页显示的条数
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public Map<String,Object> querryBusynessRecord(String userid,String startDate,String endDate,String showLineNum) throws TimeoutException ,Exception;
	/**
	 * 得到余额
	 * @param userId 用户的ID
	 * @return 
	 * @throws TimeoutException 
	 * @throws Exception
	 */
	public String getBalance(String userId)throws TimeoutException,Exception;
	/**
	 * 获的用户的基本信息
	 * 可以获得以下信息
	 *  用户ID
	 *	生日
	 *	学校名称
	 *	用户姓名
	 *	班级名称
	 *	年级名称
	 *	用户图片路径
	 *	用户级别
	 *	年级ID
	 *	班级ID
	 *	学校ID
	 *	座右铭
	 *	职位
	 *	EMAIL
	 *	QQ
	 *	电话
	 *	用户类别		00学生 01老师 02家长
	 *	性别			0 男 1 女
	 *	职称ID		0101 三级教师  0102 二级教师  0103 一级教师  0104 高级教师 0105 正高级教师 0106 特级教师 
	 * @param userId  用户的ID必传
	 * @param userName 用户名   非必传 没有可以传空或者“”；
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public User querryUserBaseInfor(String userId,String userName) throws  TimeoutException,Exception;
	/**
	 * 获得用户的详细信息
	 *  用户名
	 *	用户图片路径
	 *	用户级别
	 *	学校名称
	 *	班级名称
	 *	班级ID
	 *	学校ID
	 *	年级名称
	 *	年级ID
	 *	城市ID
	 * 	地区ID
	 *	省ID
	 *	城市名称
	 *	地区名称
	 *	省名称
	 *	用户类别   00学生 01老师 02家长
	 *	座右铭
	 *  如果用户类别是02家长 还获得以下数据
	 *  子女ID
	 *	班级名称
	 *	年级名称
	 *	学校名称
	 *  班级ID
	 *	子女名称
	 *	级别
	 *	生日
	 *  如果用户类别是01老师  还可获得以下数据
	 *  班级ID
	 *	班级名称
	 *	年级名称
	 *	年级ID
	 *	EMAIL
	 *	QQ
	 *	电话
	 *	职称
	 *	性别		0 男 1 女
	 *	职称ID	0101 三级教师 0102 二级教师  0103 一级教师 0104 高级教师 0105 正高级教师  0106 特级教师
	 *	授课科目ID 只有老师输出此字段000 全部 001 语文 002 思想品德 003 数学 004 英语 005 政治 006 美术
	 * @param userId
	 * @param userName
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public  User querryUserFullInfor(String userId,String userName)throws  TimeoutException,Exception;
	
	/**	
	 * SERVICE_SCENE=07:分组查询（如果用户是学生使用此场景）
	 * USER_ID	STRING	用户ID
	 * 
	 */
   public Map<String, Object> groupQuery(String scern,String userid)throws TimeoutException,Exception;
	
	
	/**
	 * SERVICE_SCENE=08:批量用户查询
	 * 
	 * USERS_ARRAY	ARRAY	存放用户ID数组
	 * OTHER_ID	STRING	用户ID
	 */
	public Map<String, Object> BatchUserQuery(String scern, String rspTag,String userid,String[] userArray); 

	
}
