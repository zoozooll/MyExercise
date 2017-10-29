package com.iskinfor.servicedata.pojo;
/**
 * 学习评价
 */
public class StudyAssessment {
	private String assessment_info;//评价信息
	private String assessment_data;//评价时间
	private int assessment_num;//评论条数
	private int pgs;//总条数
	public String getAssessment_info() {
		return assessment_info;
	}
	public void setAssessment_info(String assessmentInfo) {
		assessment_info = assessmentInfo;
	}
	public String getAssessment_data() {
		return assessment_data;
	}
	public void setAssessment_data(String assessmentData) {
		assessment_data = assessmentData;
	}
	public int getAssessment_num() {
		return assessment_num;
	}
	public void setAssessment_num(int assessmentNum) {
		assessment_num = assessmentNum;
	}
	public int getPgs() {
		return pgs;
	}
	public void setPgs(int pgs) {
		this.pgs = pgs;
	}
	
}
