package com.iskinfor.servicedata.pojo;

import java.util.ArrayList;

/**
 * 用户对象
 * @author Administrator
 */
public  class User {
	private String userId; //用户的id
	private String userName;//用户的名字
	private String userPhone;//用户电话号码
	private String userEmail;//用户email号
	private String userPassword;//用户密码
	private String userUserRuler;//用户角色
	private int  userMoney;//用户余额
	private String payPassword;//支付密码
	private String useImgPath;//用户图片路径
	private String leval;//用户级别
	private String schoollName;//学校名称
	private String className;//班级名称
	private String classId;//班级ID
	private String schoollId;//学校ID
	private String gradeName;//年级名称
	private String gradeId;//年级ID
	private String cityId;//城市ID
	private String arrealId;//地区ID
	private String provinceId;//省ID
    private String cityName;//城市名称	
	private String arrealName;//地区名称
	private String provinceName;//省名称
	private String userType;//用户类别 00学生 01老师 02家长  
	private String motto;//座右铭	
	private String birthday;//生日
	private String designation;//职位
	private String qq;//用户的QQ
	private String sex;//0 男 1 女
	private String designationId;//职称ID
	private ArrayList<Child> childList; //子女
	private ArrayList<ClassesArray> classesArrayList;//班级数组
	/**
	 * 用户对象是否被选中
	 */
	private boolean isUserChecked=false;
	
	
	public boolean isUserChecked() {
		return isUserChecked;
	}
	public void setUserChecked(boolean isUserChecked) {
		this.isUserChecked = isUserChecked;
	}
	public ArrayList<ClassesArray> getClassesArrayList() {
		return classesArrayList;
	}
	public void setClassesArrayList(ArrayList<ClassesArray> classesArrayList) {
		this.classesArrayList = classesArrayList;
	}
	public ArrayList<Child> getChildList() {
		return childList;
	}
	public void setChildList(ArrayList<Child> childList) {
		this.childList = childList;
	}
	public String getUseImgPath() {
		return useImgPath;
	}
	public void setUseImgPath(String useImgPath) {
		this.useImgPath = useImgPath;
	}
	public String getLeval() {
		return leval;
	}
	public void setLeval(String leval) {
		this.leval = leval;
	}
	public String getSchoollName() {
		return schoollName;
	}
	public void setSchoollName(String schoollName) {
		this.schoollName = schoollName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSchoollId() {
		return schoollId;
	}
	public void setSchoollId(String schoollId) {
		this.schoollId = schoollId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getArrealId() {
		return arrealId;
	}
	public void setArrealId(String arrealId) {
		this.arrealId = arrealId;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getArrealName() {
		return arrealName;
	}
	public void setArrealName(String arrealName) {
		this.arrealName = arrealName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDesignationId() {
		return designationId;
	}
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	
//	0101 三级教师 
//	0102 二级教师 
//	0103 一级教师 
//	0104 高级教师
//	0105 正高级教师 
//	0106 特级教师 

	
	
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public int getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(int userMoney) {
		this.userMoney = userMoney;
	}
	public String getUserUserRuler() {
		return userUserRuler;
	}
	public void setUserUserRuler(String userUserRuler) {
		this.userUserRuler = userUserRuler;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	


}
