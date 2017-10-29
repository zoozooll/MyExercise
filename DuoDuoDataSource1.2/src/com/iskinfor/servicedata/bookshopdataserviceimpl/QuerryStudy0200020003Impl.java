package com.iskinfor.servicedata.bookshopdataserviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.client.ESBClient;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IQuerryStudyInfor0200020003;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.pojo.StepLesson;


public class QuerryStudy0200020003Impl implements IQuerryStudyInfor0200020003{
	@Override
	public Map<String, Object> getStepLession(String pags,String showLineNum)
			throws TimeoutException, Exception {
		Map<String, Object> result=new HashMap<String, Object>();
		int linNums;
		if(!"".equals(showLineNum)&&null!=showLineNum){
			linNums=Integer.valueOf(showLineNum);
		}else{
			linNums=12;
		}
		
		CompositeData rspData=querryStepLesson("02",pags,showLineNum);
		CompositeData cbody = CdUtil.getBody(rspData);
		Field totalNum = cbody.getField("TOTAL_NUM");
		int allNum=(Integer)totalNum.getValue();
		
		int totalPag;
		if(allNum%linNums>0){
			totalPag=allNum/linNums+1;
		}else{
			totalPag=allNum/linNums;
		}
		result.put(DataConstant.TOTAL_PAGS, totalPag);
	
		
		// 将总记录数加到返回结果中
		result.put(DataConstant.TOTAL_NUM, allNum);
		Array uArray=cbody.getArray("SYNCHRONOU_TEACH_ARRAY");;
		ArrayList<StepLesson>  stelLessionList=new ArrayList<StepLesson>();
		if(uArray!=null&&uArray.size()>0){
			for (int i = 0; i < uArray.size(); i++) {
				StepLesson st=new StepLesson();
				CompositeData cData = uArray.getStruct(i);
				st.setCourseName((String)cData.getField("COURSE_NAME").getValue());
				st.setBigImgPath((String)cData.getField("BIG_IMG_PATH").getValue());
				st.setCourseContent((String)cData.getField("COURSE_CONTENT").getValue());
//				st.setFamilTeacherName((String)cData.getField("USER_NAME").getValue());
				st.setContentIntro((String)cData.getField("CONTENT_INTRO").getValue());
				st.setFamilTeacherId((String)cData.getField("USER_ID").getValue());
				st.setJobsTitele((String)cData.getField("JOBS_TITLE").getValue());
				st.setAther((String)cData.getField("ATHER").getValue());
				st.setProId((String)cData.getField("PRO_ID").getValue());
				stelLessionList.add(st);
			}
			result.put(DataConstant.LIST, stelLessionList);
			}
		return result;
	}

	@Override
	public Map<String, Object> getFamilesLession(String pags,String showLineNum)
			throws TimeoutException, Exception {
		Map<String, Object> result=new HashMap<String, Object>();
		CompositeData rspData=querryStepLesson("03",pags,showLineNum);
		
		int linNums;
		if(!"".equals(showLineNum)&&null!=showLineNum){
			linNums=Integer.valueOf(showLineNum);
		}else{
			linNums=12;
		}
		
		CompositeData cbody = CdUtil.getBody(rspData);
		Field totalNum = cbody.getField("TOTAL_NUM");
		int allNum=(Integer)totalNum.getValue();
		
		int totalPag;
		if(allNum%linNums>0){
			totalPag=allNum/linNums+1;
		}else{
			totalPag=allNum/linNums;
		}
		result.put(DataConstant.TOTAL_PAGS, totalPag);
		// 将总记录数加到返回结果中
		result.put(DataConstant.TOTAL_NUM, allNum);
		Array uArray=cbody.getArray("TEACHER_ARRAY");;
		ArrayList<StepLesson>  stelLessionList=new ArrayList<StepLesson>();
		if(uArray!=null&&uArray.size()>0){
			for (int i = 0; i < uArray.size(); i++) {
				StepLesson st=new StepLesson();
				CompositeData cData = uArray.getStruct(i);
		     	st.setAther((String)cData.getField("ATHER").getValue());
		 		st.setCourseContent((String)cData.getField("COURSE_CONTENT").getValue());
				st.setCourseName((String)cData.getField("COURSE_NAME").getValue());
				st.setFamilTeacherId((String)cData.getField("USER_ID").getValue());
				st.setContent((String)cData.getField("CONTENT").getValue());
				st.setContentIntro((String)cData.getField("CONTENT_INTRO").getValue());
				st.setJobsTitele((String)cData.getField("JOBS_TITLE").getValue());
				st.setBigImgPath((String)cData.getField("BIG_IMG_PATH").getValue());
				st.setProId((String)cData.getField("PRO_ID").getValue());
				stelLessionList.add(st);
			}
			result.put(DataConstant.LIST, stelLessionList);
			}
		return result;
	}
	
	
	
	private CompositeData  querryStepLesson(String scern,String pag,String showLineNum){
		Map<String, Object> args = new HashMap<String, Object>();
//		args.put("USER_ID", userId); // 名师姓名20006
		if(showLineNum!=null&&!"".equals(showLineNum)){
		args.put("SHOW_LINE_NUM", showLineNum); // 显示信息条数
		}else{
			args.put("SHOW_LINE_NUM", "12");
		}
		if(pag!=null&&!"".equals(pag)&&Integer.valueOf(pag)>0){
			args.put("PAGE_NUM", pag); // 当前显示页
		}else{
			args.put("PAGE_NUM", "1");
		}
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0200020003", scern);

		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);

		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		// 返回的报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request02(reqData);
			System.out.println("rspData==>"+rspData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rspData;
	}







	

}
