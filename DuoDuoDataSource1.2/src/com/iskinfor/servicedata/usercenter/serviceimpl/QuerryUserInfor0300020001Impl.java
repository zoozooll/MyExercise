package com.iskinfor.servicedata.usercenter.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.datahelp.SysHeader;
import com.iskinfor.servicedata.pojo.BusinesRecord;
import com.iskinfor.servicedata.pojo.Child;
import com.iskinfor.servicedata.pojo.ClassesArray;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.pojo.Teacher;
import com.iskinfor.servicedata.pojo.User;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;

public class QuerryUserInfor0300020001Impl implements
		IQuerryUserInfor0300020001 {

	/**
	 * 用户信息查询
	 * 
	 * @param proId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getTranListById2(String proId)
			throws Exception {
		// 参数
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PRO_ID", proId);
		ls.add(map);

		// 语法参数
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("UM1_ARRAY", ls);
		args.put("SHOW_LINE_NUM", "15");

		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "01");

		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);

		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		CompositeData rspData = ESBClient.request03(reqData);

		List<Map<String, Object>> lt = new ArrayList<Map<String, Object>>();

		// 验证返回报文
		SysHeader sysHeader = CdUtil.verifyHeader(rspData);
		if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
				&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
			String[] rspColumn = { "PRO_ID", "VOLUME", "TRAN_DATE",
					"USER_NAME", "PGS" };

			Array rspArray = rspData.getStruct("BODY").getArray("STATE_ARRAY");

			for (int i = 0; i < rspArray.size(); i++) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				CompositeData compositeData = rspArray.getStruct(i);
				for (String key : rspColumn) {
					Field cdField = compositeData.getField(key);
					if (cdField != null) {
						if (cdField.getAttr().getType() == FieldType.FIELD_STRING) {
							resultMap.put(key, cdField.strValue());
						} else if (cdField.getAttr().getType() == FieldType.FIELD_INT) {
							resultMap.put(key, cdField.intValue());
						}
					}
				}
				lt.add(resultMap);
			}

		}
		return lt;
	}

	/*
	 * 验证用户输入的Email/phone
	 */
	public String validateEP(Map map) throws Exception {
		String msg = "";
		String emailV = map.get("email_v").toString();
		String phoneV = map.get("phone_v").toString();
		String userId = map.get("userId").toString();
		// if(null == emailV && "".equals(emailV) &&
		// StringUtils.isEmpty(emailV)){
		// emailV = "";
		// }
		// if(null == phoneV && "".equals(phoneV) &&
		// StringUtils.isEmpty(emailV)){
		// phoneV = "";
		// }
		Map mapV = new HashMap();
		mapV.put("USER_ID", userId);
		mapV.put("EMAIL", emailV);
		mapV.put("PHONE", phoneV);

		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "11"); // 11:用户信息验证
		CompositeData body = CdUtil.bodyInstance(mapV);
		CompositeData reqData = CdUtil.reqData(header, body);
		// //System.out.println("validate=="+reqData);

		CompositeData rspData;
		try {
			rspData = ESBClient.request03(reqData);
			// //System.out.println("rspData:"+rspData);

			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
				msg = "S";
			} else {
				msg = sysHeader.getRet().get(0).getMsg();
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 查询名师列表
	 */
	public ArrayList<Teacher> getFamlesTeachers(String cursorId)
			throws Exception {

		ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
		// 组装请求头
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "01");
		// 组装请求体
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(cursorId) && null != cursorId) {
			args.put("COURSE_ID", cursorId);
		}
		CompositeData body = CdUtil.bodyInstance(args);
		CompositeData reqData = CdUtil.reqData(header, body);
		// 得到返回
		CompositeData rspData;
		try {
			rspData = ESBClient.request03(reqData);
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			Array uArray = body.getArray("TEACHER_ARRAY");
			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
				Teacher teacher = new Teacher();
				String teacherName = (String) cData.getField("TEACHER_NAME")
						.getValue();
				if (null != teacherName && "".equals(teacherName)) {
					teacher.setUserName(teacherName);
				}
				teacherList.add(teacher);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		// 得到结果
		return teacherList;
	}

	@Override
	public Map<String, Object> querryBusynessRecord(String userid,
			String startDate, String endDate, String showLineNum)
			throws TimeoutException, Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		// 组装请求头
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "02");
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(userid) && null != userid) {
			args.put("USER_ID", userid);
		}
		if (!"".equals(startDate) && null != startDate) {
			args.put("START_DATE", startDate);
		}
		if (!"".equals(endDate) && null != endDate) {
			args.put("END_DATE", endDate);
		}
		if (!"".equals(showLineNum) && null != showLineNum) {
			args.put("END_DATE", showLineNum);
		} else {
			args.put("SHOW_LINE_NUM", "10");
		}
		CompositeData body = CdUtil.bodyInstance(args);
		CompositeData reqData = CdUtil.reqData(header, body);
		// 得到返回
		CompositeData rspData;
		try {
			rspData = ESBClient.request03(reqData);
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			CompositeData cbody = CdUtil.getBody(rspData);

			Array uArray = cbody.getArray("ACCOUNTLOG_ARRAY");
			ArrayList<BusinesRecord> bussList = new ArrayList<BusinesRecord>();
			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
				BusinesRecord businesRecourd = new BusinesRecord();
				String type = (String) cData.getField("TYPE").getValue();
				if (!"".equals(type) && null != type) {
					businesRecourd.setType(type);
				}
				String money = (String) cData.getField("OPR_NUM").getValue();
				if (!"".equals(money) && null != money) {
					businesRecourd.setOprNum(money);
				}
				String desc = (String) cData.getField("OPR_DESC").getValue();
				if (!"".equals(desc) && null != desc) {
					businesRecourd.setPrrDesc(desc);
				}
				String createData = (String) cData.getField("CREATE_DATE")
						.getValue();
				if (!"".equals(createData) && null != createData) {
					businesRecourd.setPrrDesc(createData);
				}
				String balance = (String) cData.getField("BALANCE").getValue();
				if (!"".equals(balance) && null != balance) {
					businesRecourd.setBalance(balance);
				}
				String userName = (String) cData.getField("USER_NAME")
						.getValue();
				if (!"".equals(userName) && null != userName) {
					businesRecourd.setBalance(userName);
				}
				bussList.add(businesRecourd);
			}
			result.put(DataConstant.BUSINESS_RECORD_LIST, bussList);
			int pgs = (Integer) cbody.getField("PGS").getValue();
			if (!(pgs < 0)) {
				result.put(DataConstant.BUSINESS_RECORD_PGS, pgs);
			}
			String outChage = (String) cbody.getField("OUTCHARGE").getValue();
			if (null != outChage && !"".equals(outChage)) {
				result.put(DataConstant.OUT_CHARGE, outChage);
			}
			String reChage = (String) cbody.getField("RECHARGE").getValue();
			if (null != reChage && !"".equals(reChage)) {
				result.put(DataConstant.RECHARGE, reChage);
			}

		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean Login(String userName, String passWord)
			throws TimeoutException, Exception {
		// 语法参数
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("USER_ID", userName);
		args.put("PASSWORD", passWord);
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "05");
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request03(reqData);
			// //System.out.println(rspData);
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
	public String getBalance(String userId) throws TimeoutException, Exception {
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "03");
		String balance = "0.00";
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(userId) && null != userId) {
			args.put("USER_ID", userId);
		}
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request03(reqData);
			CompositeData resBody = CdUtil.getBody(rspData);
			balance = (String) resBody.getField("BALANCE").getValue();
			// //System.out.println(rspData);
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return balance;
	}

	@Override
	public User querryUserBaseInfor(String userId, String userName)
			throws TimeoutException, Exception {
		User user = new User();
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "04");
		// 传送参数
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(userId) && null != userId) {
			args.put("USER_ID", userId);
		}
		if (!"".equals(userName) && null != userName) {
			args.put("USER_NAME", userName);
		}
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;

		try {
			rspData = ESBClient.request03(reqData);
			CompositeData resBody = CdUtil.getBody(rspData);
			Array resArray = resBody.getArray("USER_ARRAY");
			CompositeData cd = resArray.getStruct(0);
			
			//用户ID
			try {
			String resUserId = (String) cd.getField("USER_ID").getValue();
			if (!"".equals(resUserId) && null != resUserId) {
				user.setUserId(resUserId);
			}
			}catch(Exception e){}
			//生日
			try{
			String resBirthday = (String) cd.getField("BIRTH_DATE").getValue();
			if (!"".equals(resBirthday) && null != resBirthday) {
				user.setBirthday(resBirthday);
			}}catch(Exception e){}
			// 学校名称
			try{
			String resSchoolName = (String) cd.getField("SCHOOL_NAME")
					.getValue();
			if (!"".equals(resSchoolName) && null != resSchoolName) {
				user.setSchoollName(resSchoolName);
			}}catch(Exception e){}
			// 用户姓名
			try{
			String resUserName = (String) cd.getField("USER_NAME").getValue();
			if (!"".equals(resUserName) && null != resUserName) {
				user.setUserName(resUserName);
			}}catch(Exception e){}
			// 班级名称
			try{
			String resClassName = (String) cd.getField("CLASS_NAME").getValue();
			if (!"".equals(resClassName) && null != resClassName) {
				user.setClassName(resClassName);
			}}catch(Exception e){}
			// 年级名称
            try{
			String resGradeName = (String) cd.getField("GRADE_NAME").getValue();
			if (!"".equals(resGradeName) && null != resGradeName) {
				user.setGradeName(resGradeName);
			}}catch(Exception e){}
			// 用户图片路径
			try{
			String resUserImgPath = (String) cd.getField("USER_IMG_PATH")
					.getValue();
			if (!"".equals(resUserImgPath) && null != resUserImgPath) {
				user.setUseImgPath(resUserImgPath);
			}}catch(Exception e){}
			// 用户级别
			try{
			String resUserLeval = (String) cd.getField("LEVAL").getValue();
			if (!"".equals(resUserLeval) && null != resUserLeval) {
				user.setLeval(resUserLeval);
			}}catch(Exception e){}
			// 年级ID
			try{
			String resGreadeId = (String) cd.getField("GRADE_ID").getValue();
			if (!"".equals(resGreadeId) && null != resGreadeId) {
				user.setGradeId(resGreadeId);
			}}catch(Exception e){}
			// 班级ID
			try{
			String classId = (String) cd.getField("CLASS_ID").getValue();
			if (!"".equals(classId) && null != classId) {
				user.setClassId(classId);
			}}catch(Exception e){}
			// 学校ID
			try{
			String schoolId = (String) cd.getField("SCHOOL_ID").getValue();
			if (!"".equals(schoolId) && null != schoolId) {
				user.setSchoollId(schoolId);
			}}catch(Exception e){}
			// 座右铭
			try{
			String motto = (String) cd.getField("MOTTO").getValue();
			if (!"".equals(motto) && null != motto) {
				user.setMotto(motto);
			}}catch(Exception e){}
			// 职位
			try{
			String designation = (String) cd.getField("DESIGNATION").getValue();
			if (!"".equals(designation) && null != designation) {
				user.setDesignation(designation);
			}}catch(Exception e){}
			// EMAIL
			try{
			String email = (String) cd.getField("EMAIL").getValue();
			if (!"".equals(email) && null != email) {
				user.setDesignation(email);
			}}catch(Exception e){}
			// QQ
			try{
			String qq = (String) cd.getField("QQ").getValue();
			if (!"".equals(qq) && null != qq) {
				user.setQq(qq);
			}}catch(Exception e){}
			// 电话
			try{
			String phone = (String) cd.getField("PHONE").getValue();
			if (!"".equals(phone) && null != phone) {
				user.setUserPhone(phone);
			}}catch(Exception e){}
			// 用户类别 00学生 01老师 02家长
			try{
			String userType = (String) cd.getField("USER_TYPE").getValue();
			if (!"".equals(userType) && null != userType) {
				user.setUserType(userType);
			}}catch(Exception e){}
			// 性别
			try{
			String sex = (String) cd.getField("SEX").getValue();
			if (!"".equals(sex) && null != sex) {
				user.setSex(sex);
			}}catch(Exception e){}
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return user;
	}

	/**
	 * 查询用户详细信息
	 */
	@Override
	public User querryUserFullInfor(String userId, String userName)
			throws TimeoutException, Exception {

		System.out.println("querryUserFullInfor>>>>>>====" + userId);

		User user = new User();
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", "12");

		// 传送参数
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(userId) && null != userId) {
			args.put("USER_ID", userId);
		}
		if (!"".equals(userName) && null != userName) {
			args.put("USER_NAME", userName);
		}
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		// 返回的报文
		CompositeData rspData = null;

		try {
			rspData = ESBClient.request03(reqData);
//			System.out.print("rspData==============>" + rspData);
			
			CompositeData resBody = CdUtil.getBody(rspData);
//			Array resArray = resBody.getArray("USER_ARRAY");
//			CompositeData cd = resArray.getStruct(0);
			
			// 用户姓名
			String resUserName = (String) resBody.getField("USER_NAME").getValue();
			if (!"".equals(resUserName) && null != resUserName) {
				user.setUserName(resUserName);
			}

			// 用户图片路径
			String resUserImgPath = (String) resBody.getField("USER_IMG_PATH")
					.getValue();
			if (!"".equals(resUserImgPath) && null != resUserImgPath) {
				user.setUseImgPath(resUserImgPath);
			}
			// 用户级别
			String resUserLeval = (String) resBody.getField("LEVAL").getValue();
			if (!"".equals(resUserLeval) && null != resUserLeval) {
				user.setLeval(resUserLeval);
			}
			// 学校名称
			String resSchoolName = (String) resBody.getField("SCHOOL_NAME")
					.getValue();
			if (!"".equals(resSchoolName) && null != resSchoolName) {
				user.setSchoollName(resSchoolName);
			}
			// 班级名称
			String resClassName = (String) resBody.getField("CLASS_NAME").getValue();
			if (!"".equals(resClassName) && null != resClassName) {
				user.setClassName(resClassName);
			}
			// 班级ID
			String classId = (String) resBody.getField("CLASS_ID").getValue();
			if (!"".equals(classId) && null != classId) {
				user.setClassId(classId);
			}
			// 学校ID
			String schoolId = (String) resBody.getField("SCHOOL_ID").getValue();
			if (!"".equals(schoolId) && null != schoolId) {
				user.setSchoollId(schoolId);
			}
			// 年级名称
			String resGradeName = (String) resBody.getField("GRADE_NAME").getValue();
			if (!"".equals(resGradeName) && null != resGradeName) {
				user.setGradeName(resGradeName);
			}
			// 年级ID
			String resGreadeId = (String) resBody.getField("GRADE_ID").getValue();
			if (!"".equals(resGreadeId) && null != resGreadeId) {
				user.setGradeId(resGreadeId);
			}
			// 城市ID
			String cityIdString = (String) resBody.getField("CITY_ID").getValue();
			if (!"".equals(cityIdString) && null != cityIdString) {
				user.setCityId(cityIdString);
			}
			// 地区ID
			String arrealIdString = (String) resBody.getField("AREAL_ID").getValue();
			if (!"".equals(arrealIdString) && null != arrealIdString) {
				user.setArrealId(cityIdString);
			}
			// 省份ID
			String provinceIdString = (String) resBody.getField("PROVINCE_ID")
					.getValue();
			if (!"".equals(provinceIdString) && null != provinceIdString) {
				user.setProvinceId(provinceIdString);
			}
			// 城市名
			String cityNameString = (String) resBody.getField("CITY_NAME")
					.getValue();
			if (!"".equals(cityNameString) && null != cityNameString) {
				user.setCityName(cityNameString);
			}
			// 地区名
			String arealNameString = (String) resBody.getField("AREAL_NAME")
					.getValue();
			if (!"".equals(arealNameString) && null != arealNameString) {
				user.setArrealName(arealNameString);
			}
			// 省份名
			String provinceName = (String) resBody.getField("PROVINCE_NAME")
					.getValue();
			if (!"".equals(provinceName) && null != provinceName) {
				user.setProvinceName(provinceName);
			}
			// 用户座右铭
			String motto = (String) resBody.getField("MOTTO").getValue();
			if (!"".equals(motto) && null != motto) {
				user.setProvinceName(provinceName);
			}
			// 用户类型
			String userType = (String) resBody.getField("USER_TYPE").getValue();
			if (!"".equals(userType) && null != userType) {
				user.setUserType(userType);
			}
			// 如果是家长
			if ("02".equals(userType)) {
				Array childArray = resBody.getArray("TCHILD_ARRAY");
				ArrayList<Child> childList = new ArrayList<Child>();
				for (int i = 0; i < childArray.size(); i++) {
					CompositeData childCd = childArray.getStruct(i);
					Child child = new Child();
					String childId = (String) childCd.getField("CHILD_ID")
							.getValue();
					if (!"".equals(childId) && null != childId) {
						child.setChildId(childId);
					}
					String childClassName = (String) childCd.getField(
							"CLASS_NAME").getValue();
					if (!"".equals(childClassName) && null != childClassName) {
						child.setClassName(childClassName);
					}
					String childGradeName = (String) childCd.getField(
							"GRADE_NAME").getValue();
					if (!"".equals(childGradeName) && null != childGradeName) {
						child.setGradeName(childGradeName);
					}
					String childSchoolName = (String) childCd.getField(
							"SCHOOL_NAME").getValue();
					if (!"".equals(childSchoolName) && null != childSchoolName) {
						child.setSchoolName(childSchoolName);
					}
					String childClassId = (String) childCd.getField("CLASS_ID")
							.getValue();
					if (!"".equals(childClassId) && null != childClassId) {
						child.setClassId(childClassId);
					}
					String childName = (String) childCd.getField("CHILD_NAME")
							.getValue();
					if (!"".equals(childName) && null != childName) {
						child.setChildName(childName);
					}
					String childLeaval = (String) childCd.getField("LEVAL")
							.getValue();
					if (!"".equals(childLeaval) && null != childLeaval) {
						child.setLeaval(childLeaval);
					}
					String childBirthDate = (String) childCd.getField(
							"BIRTH_DATE").getValue();
					if (!"".equals(childBirthDate) && null != childBirthDate) {
						child.setBirthDate(childBirthDate);
					}
					childList.add(child);
				}
				user.setChildList(childList);

			}// 如果是老师
			else if ("01".equals(userType)) {
				Array classArray = resBody.getArray("CLASS_ARRAY");
				ArrayList<ClassesArray> classArrayList = new ArrayList<ClassesArray>();
				for (int i = 0; i < classArray.size(); i++) {
					ClassesArray getClass = new ClassesArray();

					CompositeData classCd = classArray.getStruct(i);

					String resClassID = (String) classCd.getField("CLASS_ID")
							.getValue();
					if (!"".equals(resClassID) && null != resClassID) {
						getClass.setClassID(resClassID);
					}
					String resClassName1 = (String) classCd.getField(
							"CLASS_NAME").getValue();
					if (!"".equals(resClassName1) && null != resClassName1) {
						getClass.setClassName(resClassName1);
					}
					String resGradeId = (String) classCd.getField("GRADE_ID")
							.getValue();
					if (!"".equals(resGradeId) && null != resGradeId) {
						getClass.setGradeId(resGradeId);
					}
					String resEmail = (String) classCd.getField("EMAIL")
							.getValue();
					if (!"".equals(resEmail) && null != resEmail) {
						getClass.setEmail(resEmail);
					}
					String resQQ = (String) classCd.getField("QQ").getValue();
					if (!"".equals(resQQ) && null != resQQ) {
						getClass.setQq(resQQ);
					}
					String phone = (String) classCd.getField("PHONE")
							.getValue();
					if (!"".equals(phone) && null != phone) {
						getClass.setPhone(phone);
					}
					String resDesignation = (String) classCd.getField(
							"DESIGNATION").getValue();
					if (!"".equals(resDesignation) && null != resDesignation) {
						getClass.setDesignation(resDesignation);
					}
					String sex = (String) classCd.getField("SEX").getValue();
					if (!"".equals(sex) && null != sex) {
						getClass.setSex(sex);
					}
					String desigNationId = (String) classCd.getField(
							"DESIGNATION_ID").getValue();
					if (!"".equals(desigNationId) && null != desigNationId) {
						getClass.setDesignationId(desigNationId);
					}
					String courseId = (String) classCd.getField("COURSE_ID")
							.getValue();
					if (!"".equals(courseId) && null != courseId) {
						getClass.setCourseId(courseId);
					}
					classArrayList.add(getClass);
				}
				user.setClassesArrayList(classArrayList);
			}

		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public Map<String, Object> groupQuery(String scern,String userid)
			throws TimeoutException, Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ArrayList<User> userList = new ArrayList<User>();
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0300020001", scern);
		// 参数
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(userid) && null != userid) {
			
			args.put("USER_ID", userid);
		}
		
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		try {
			CompositeData rspData = null;
			try {
				 rspData = ESBClient.request03(reqData);
				
			} catch (Exception e) {
				throw e;
			}
			CompositeData cbody = CdUtil.getBody(rspData);
			Array classArray = cbody.getArray("CLASSNAMTES_ARRAY");
			ArrayList<User>  classesList=new ArrayList<User>();
			for(int i=0;i<classArray.size();i++){
				CompositeData cData = classArray.getStruct(i);
				User user=new User();
				user.setUserId((String)cData.getField("USER_ID").getValue());
				user.setUserName((String)cData.getField("USER_NAME").getValue());
				classesList.add(user);
			}
			result.put("class_array", classesList);
			
			Array homeArray = cbody.getArray("HOMEMMB_ARRAY");
			ArrayList<User>  homeList=new ArrayList<User>();
			for(int i=0;i<homeArray.size();i++){
				CompositeData cData = homeArray.getStruct(i);
				User user=new User();
				user.setUserId((String)cData.getField("USER_ID").getValue());
				user.setUserName((String)cData.getField("USER_NAME").getValue());
				homeList.add(user);
			}
			result.put("home_array", homeList);
			
			Array teacherArray = cbody.getArray("TEACHERORSTU_ARRAY");
			ArrayList<User>  teacherList=new ArrayList<User>();
			for(int i=0;i<teacherArray.size();i++){
				CompositeData cData = teacherArray.getStruct(i);
				User user=new User();
				user.setUserId((String)cData.getField("USER_ID").getValue());
				user.setUserName((String)cData.getField("USER_NAME").getValue());
				teacherList.add(user);
			}
			result.put("teacher_array", teacherList);
			
			
			
			Array friendArray = cbody.getArray("FRIEND_ARRAY");
			ArrayList<User>  friendList=new ArrayList<User>();
			for(int i=0;i<friendArray.size();i++){
				CompositeData cData = friendArray.getStruct(i);
				User user=new User();
				user.setUserId((String)cData.getField("USER_ID").getValue());
				user.setUserName((String)cData.getField("USER_NAME").getValue());
				friendList.add(user);
			}
			result.put("friend_array", friendList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return result;
	}

	@Override
	public Map<String, Object> BatchUserQuery(String scern, String rspTag,
			String userid, String[] userArray) {
		  
		Map<String,Object> result =new HashMap<String,Object>();
		CompositeData header=CdUtil.sysHeaderInstance("0300020001", scern);
		Map<String,Object> args=new HashMap<String,Object>();
        if(null!=userid && !"".equals(userid))
        {
        	args.put("USER_ID",userid);
        }
        if(null!=userArray && userArray.length>0)
        {
        	List<Map<String,Object>> mapUserArrayList =new ArrayList<Map<String,Object>>();
        	for(int i=0;i<userArray.length;i++)
        	{
        		Map<String,Object> mapUserArray=new HashMap<String,Object>();
        		mapUserArray.put("OTHER_ID", userArray[i]);
        		mapUserArrayList.add(mapUserArray);
        	}
            args.put("USERS_ARRAY", mapUserArrayList); 
        }
        CompositeData body = CdUtil.bodyInstance(args);
		CompositeData reqData = CdUtil.reqData(header, body);
		// 得到返回
		CompositeData rspData;
		try {
			rspData = ESBClient.request03(reqData);
			System.out.print("PLJ==rspData:>"+rspData); 
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			CompositeData cbody = CdUtil.getBody(rspData);

			Array uArray = cbody.getArray(rspTag);
			ArrayList<User> userList = new ArrayList<User>();
			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
				User user = new User();
				
				String otherid = (String) cData.getField("OTHER_ID").getValue();
				if (!"".equals(otherid) && null != otherid) {
					user.setUserId(otherid); }
				
				String userName = (String) cData.getField("USER_NAME").getValue();
				if (!"".equals(userName) && null != userName) {
					user.setUserName(userName);}
				
				String userImgPath= (String) cData.getField("USER_IMG_PATH").getValue();
				if (!"".equals(userImgPath) && null != userImgPath) {
					user.setUseImgPath(userImgPath); }
				
				String designation= (String) cData.getField("DESIGNATION").getValue();
				if (!"".equals(designation) && null != designation) {
					user.setDesignation(designation); }
				
				userList.add(user);
			   }
			result.put(DataConstant.USER_ARRAY_KEY, userList);
		     		
		}
		 catch (Exception e) {
		  e.printStackTrace();
		}
        
        
		
		return result;
	}

	

	

}
