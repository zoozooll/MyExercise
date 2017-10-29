package com.iskinfor.servicedata.bookshopdataservice;

import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 学习资料信息查询
 * @author Administrator
 *
 */
public interface IQuerryStudyInfor0200020003 {
	/**
	 * 查询所有的同步课程
	 */
	public Map<String,Object> getStepLession(String pags,String showLineNum) throws TimeoutException,Exception;
	/**
	 * 查询所有的名师指导
	 */
	public Map<String,Object> getFamilesLession(String pags,String showLineNum) throws TimeoutException,Exception;	
}
