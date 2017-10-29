package com.iskinfor.servicedata.bookshopdataserviceimpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.ISearchQuery0200020003;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.pojo.Product;

public class SearchQuery0200020003 implements ISearchQuery0200020003{
	
    /*
     * String showLineNum显示信息条数 必传项       String scern 01,String rspTag PRO_ARRAY
     * String couse科目,科目包括00:语文01:英语02:数学03:物理04:化学05:科学(所有ID以0200020004中的查询结果为准)
     * 00: chinese 01: english 02: mathematical 03: physical 04: chemical 05: science 
     * String grade年级,年级包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10:初中通用(所有ID以0200020004中的查询结果为准)
     * 00:onegrade 01:secondgrade 02:threegrade 03:fourgrade 04:fivegrade 05:sixgrade 06:elementaryschool 07:onejunior 08:secondjunior 09:threejunior 10:juniorhigh 
     * String space篇幅,篇幅包括00:上册01:下册02:全册(所有ID以0200020004中的查询结果为准)
     * 00:upvolume 01:downvolume 02:allvolume
     * int use用途,用途包括00:教材01:写作02:词汇语法03:口语听力04:阅读05:电影原声06:歌曲07:竞赛08:智力(所有ID以0200020004中的查询结果为准) 
     * 00: teaching 01:writing 02:vocabulary 03:listening 04:read 05:movie 06:sing 07:competition  08:intelligence
     * String type类别,类别包括00:同步课程01:名师辅导02:名校课件03:单元考04:期中考05:期末考06:中考历年真题07:模拟考08:巩固篇09:提高篇10:进阶篇(所有ID以0200020004中的查询结果为准)
     * 00:synchronous 01:teacher 02:famous 03:unit 04:midtermexam 05:finalexam 06:midexamsimulation 07:simulation 08:consolidate 09:improve 10:advanced
	 * String source来源,来源包括00:学校01:出版社02:个人03:教育机构(所有ID以0200020004中的查询结果为准) 
	 * 00:school 01:press 02:individual 03:education
    */
	@Override
	public Map<String, Object> getAdvancedSearch(String scern,String rspTag ,String userId,
			String proId,Map<String, Object> couse, Map<String, Object> grade,Map<String, Object> space,Map<String, Object> use, 
			Map<String, Object> type,Map<String, Object> source, String courseName, String teacherName,
			String showLineNum, int pagNum, String proType, String inputInfo,
			String orderBy ) throws TimeoutException, Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ArrayList<Product> proList = new ArrayList<Product>();
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0200020003", scern);
		// 参数
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(userId) && null != userId) {
			args.put("USER_ID", userId);
		}
		if (!"".equals(proId) && null != proId) {
			args.put("PRO_ID", proId);
		}
//		 * String couse科目,科目包括00:语文01:英语02:数学03:物理04:化学05:科学(所有ID以0200020004中的查询结果为准)
//	     * 00: chinese 01: english 02: mathematical 03: physical 04: chemical 05: science 
		if (couse.size()>0 && null != couse) {
			List<Map<String, Object>> TeachList = new ArrayList<Map<String, Object>>();
			Map<String, Object> Teach;
			for(int i=0;i<couse.size();i++)
			{
				Teach = new HashMap<String, Object>();
				if(!couse.get("chinese").toString().equals(""))
				{Teach.put("COURSE", couse.get("chinese").toString()); }
				if(!couse.get("english").toString().equals(""))
				{Teach.put("COURSE", couse.get("english").toString()); }
				if(!couse.get("mathematical").toString().equals(""))
				{Teach.put("COURSE", couse.get("mathematical").toString()); }
				if(!couse.get("physical").toString().equals(""))
				{Teach.put("COURSE", couse.get("physical").toString()); }
				if(!couse.get("chemical").toString().equals(""))
				{Teach.put("COURSE", couse.get("chemical").toString()); }
				if(!couse.get("science").toString().equals(""))
				{Teach.put("COURSE", couse.get("science").toString()); }
				TeachList.add(Teach);
			}
			
			args.put("COURSE", couse);
		}
		//grade年级
		if (grade.size()>0 && null != grade) {
			args.put("GRADE", grade);
		}
		//space篇幅
		if (space.size()>0 && null != space) {
			args.put("SPACE", space);
		}
		//use用途
		if (use.size()>0 && use!=null) {
			args.put("USE", use);
		}
		//type类别
		if (type.size()>0 && null != type) {
			args.put("TYPE", type);
		}
		//source来源
		if (source.size()>0 && null != source) {
			args.put("SOURCE", source);
		}
		// 组装同步教学Array
		List<Map<String, Object>> coutTeachArray = new ArrayList<Map<String, Object>>();
		Map<String, Object> coutTeacherMap = new HashMap<String, Object>();
		if (!"".equals(courseName) && null != courseName) {
			coutTeacherMap.put("COURSE_NAME", courseName);
		}
		if (!"".equals(teacherName) && null != teacherName) {
			coutTeacherMap.put("USER_NAME", teacherName);
		}
		if (coutTeacherMap.size()>0) {
			coutTeachArray.add(coutTeacherMap);
			args.put("COU_TEACH_ARRAY", coutTeachArray);
		}
		
		if (!"".equals(showLineNum) && null != showLineNum) {
			args.put("SHOW_LINE_NUM", showLineNum);
		}
		else {
			args.put("SHOW_LINE_NUM", "12");
		}
		if(pagNum>0)
		{
			args.put("PAGE_NUM", pagNum);
		}
		if (!"".equals(proType) && null != proType) {
			args.put("PRO_TYPE", proType);
		}
		if (!"".equals(inputInfo) && null != inputInfo) {
			args.put("INPUT_INFOR", inputInfo);
		}
		if (!"".equals(orderBy) && null != orderBy) {
			args.put("ORDER_BY", orderBy);
		}
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		try {
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request02(reqData);
				System.out.print("rspData==>"+rspData);
			} catch (Exception e) {
				throw e;
			}
			CompositeData cbody = CdUtil.getBody(rspData);
			Array uArray = cbody.getArray(rspTag);
			if(scern.equals("01"))
			{
			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
				Product productDto = new Product();
				productDto.setProId((String)cData.getField("PRO_ID").getValue());
				productDto.setProName((String) cData.getField("PRO_NAME").getValue());
				//productDto.setIntroContent((String)cData.getField("PRO_INTRO").getValue());
				//productDto.setBigImgPath((String) cData.getField("BIG_IMG_PATH").getValue());
				productDto.setSmallImgPath((String) cData.getField("SMALL_IMG_PATH").getValue());
				proList.add(productDto);
			}}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将列表压入到返回结果中
		result.put(DataConstant.LIST, proList);
		System.out.println("PLJ"+result);
		return result;

	}

	@Override
	public Map<String, Object> getSearchBookShelf(String scern, String rspTag,
			String userId, String showLineNum, String proId, String orderBy,
			int pagNum, String couse, String grade, int use, String source,
			String inputInfo) throws TimeoutException, Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ArrayList<Product> proList = new ArrayList<Product>();
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0100020001", scern);
		// 参数
		Map<String, Object> args = new HashMap<String, Object>();
		if (!"".equals(userId) && null != userId) {
			args.put("USER_ID", userId);
		}
		if (!"".equals(showLineNum) && null != showLineNum) {
			args.put("SHOW_LINE_NUM", showLineNum);
		}
		if (!"".equals(proId) && null != proId) {
			args.put("PRO_ID", proId);
		}
		if (!"".equals(orderBy) && null != orderBy) {
			args.put("ORDER_BY", orderBy);
		}
		if(pagNum>0)
		{
			args.put("PAGE_NUM", pagNum);
		}
		if (!"".equals(couse) && null != couse) {
			args.put("COURSE", couse);
		}
		if (!"".equals(grade) && null != grade) {
			args.put("GRADE", grade);
		}
		if (use>0) {
			args.put("USE", use);
		}
		if (!"".equals(source) && null != source) {
			args.put("SOURCE", source);
		}
		else {
			args.put("SHOW_LINE_NUM", "12");
		}
		if (!"".equals(inputInfo) && null != inputInfo) {
			args.put("INPUT_INFOR", inputInfo);
		}
		
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		try {
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request01(reqData);
				System.out.print("rspData==>"+rspData);
			} catch (Exception e) {
				throw e;
			}
			CompositeData cbody = CdUtil.getBody(rspData);
			Array uArray = cbody.getArray(rspTag);
			if(scern.equals("01"))
			{
			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
				Product productDto = new Product();
				productDto.setProId((String)cData.getField("PRO_ID").getValue());
				productDto.setProName((String) cData.getField("PRO_NAME").getValue());
				productDto.setIntroContent((String)cData.getField("PRO_INTRO").getValue());
				productDto.setBigImgPath((String) cData.getField("BIG_IMG_PATH").getValue());
				productDto.setSmallImgPath((String) cData.getField("SMALL_IMG_PATH").getValue());
				proList.add(productDto);
			}}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将列表压入到返回结果中
		result.put(DataConstant.LIST, proList);
		System.out.println("PLJ"+result);
		return result;
	}

	@Override
	public Map<String, Object> getSearchShop(String scern, String rspTag,
			String userId, String showLineNum, String proId, String orderBy,
			int pagNum, String couse, String grade, int use, String source,
			String inputInfo) throws TimeoutException, Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
