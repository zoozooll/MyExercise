package com.iskinfor.servicedata.pojo;

public class StepLesson {

	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getBigImgPath() {
		return bigImgPath;
	}
	public void setBigImgPath(String bigImgPath) {
		this.bigImgPath = bigImgPath;
	}
	public String getCourseContent() {
		return courseContent;
	}
	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
	}
	public String getFamilTeacherName() {
		return familTeacherName;
	}
	public void setFamilTeacherName(String familTeacherName) {
		this.familTeacherName = familTeacherName;
	}
	public String getContentIntro() {
		return contentIntro;
	}
	public void setContentIntro(String contentIntro) {
		this.contentIntro = contentIntro;
	}
	public String getFamilTeacherId() {
		return familTeacherId;
	}
	public void setFamilTeacherId(String familTeacherId) {
		this.familTeacherId = familTeacherId;
	}
	public String getJobsTitele() {
		return jobsTitele;
	}
	public void setJobsTitele(String jobsTitele) {
		this.jobsTitele = jobsTitele;
	}
	public String getAther() {
		return ather;
	}
	public void setAther(String ather) {
		this.ather = ather;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 课程名称
	 */
	private String courseName;
	/**
	 * 商品大图路径
	 */
	private String bigImgPath;
	/**
	 * 课程标题
	 */
	private String courseContent;
	/**
	 * 名师姓名
	 */
	private String familTeacherName;
	/**
	 * 内容简介
	 */
	private String contentIntro;
	/**
	 * 名师ID
	 */
	private String familTeacherId;
	/**
	 * 职称
	 */
	private String jobsTitele;
	/**
	 * 作者
	 */
	private String ather;
	/**
	 * 名师辅导内容
	 */
	private String content;
	/**
	 * 课程ID
	 */
	private String proId;
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	
}
