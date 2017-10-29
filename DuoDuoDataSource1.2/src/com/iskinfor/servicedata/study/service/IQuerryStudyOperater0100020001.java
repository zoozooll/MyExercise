package com.iskinfor.servicedata.study.service;

import java.util.ArrayList;
import java.util.Map;

import com.iskinfor.servicedata.pojo.BookReaded;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.pojo.ReadingNotes;
import com.iskinfor.servicedata.pojo.RecommentInfor;
import com.iskinfor.servicedata.pojo.StudyAssessment;



public interface IQuerryStudyOperater0100020001 {
	/**
	 * 我看过的书
	 * 
	 * @param userid
	 *            用户的ID 必传
	 * @param showLineNum
	 *            每页显示的条数 默认为12条
	 * @param proId
	 *            书的id
	 * @param orderby
	 *            排序方式 00：按照日期倒序排序；所有场景使用
	 * @param PageNum
	 *            显示页码
	 * @param courser
	 *            课程科目包括00:语文01:英语02:数学03:物理04:化学05:科学
	 * @param grade
	 *            年级
	 *            包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10
	 *            :初中通用
	 * @param user
	 *            用途 00：课件01：书籍02：习题03：考题
	 * @param resource
	 *            来源 来源包括00：学校01：个人02：出版社03：培训机构
	 * @param proType
	 *            商品类型 00:书籍01:课件02:习题03：考卷
	 * @return
	 * @throws Exception
	 */
	public ArrayList<BookReaded> getReadedBook(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType) throws Exception;
	/**
	 * 看书做的书签
	 * 
	 * @param userid
	 *            用户的ID 必传
	 * @param showLineNum
	 *            每页显示的条数 默认为12条
	 * @param proId
	 *            书的id 必传
	 * @param orderby
	 *            排序方式 00：按照日期倒序排序；所有场景使用
	 * @param PageNum
	 *            显示页码
	 * @param courser
	 *            课程科目包括00:语文01:英语02:数学03:物理04:化学05:科学
	 * @param grade
	 *            年级
	 *            包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10
	 *            :初中通用
	 * @param user
	 *            用途 00：课件01：书籍02：习题03：考题
	 * @param resource
	 *            来源 来源包括00：学校01：个人02：出版社03：培训机构
	 * @param proType
	 *            商品类型 00:书籍01:课件02:习题03：考卷
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getMyBookMark(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType) throws Exception;
	
	/**
	 * 看书做的笔记
	 * 
	 * @param userid
	 *            用户的ID 必传
	 * @param showLineNum
	 *            每页显示的条数 默认为12条
	 * @param proId
	 *            书的id 必传
	 * @param orderby
	 *            排序方式 00：按照日期倒序排序；所有场景使用
	 * @param PageNum
	 *            显示页码
	 * @param courser
	 *            课程科目包括00:语文01:英语02:数学03:物理04:化学05:科学
	 * @param grade
	 *            年级
	 *            包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10
	 *            :初中通用
	 * @param user
	 *            用途 00：课件01：书籍02：习题03：考题
	 * @param resource
	 *            来源 来源包括00：学校01：个人02：出版社03：培训机构
	 * @param proType
	 *            商品类型 00:书籍01:课件02:习题03：考卷
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ReadingNotes> getReadingNotes(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType) throws Exception;
	
	/**
	 * 学习评价
	 * 
	 * @param userid
	 *            用户的ID 必传
	 * @param showLineNum
	 *            每页显示的条数 默认为12条
	 * @param proId
	 *            书的id 必传
	 * @param orderby
	 *            排序方式 00：按照日期倒序排序；所有场景使用
	 * @param PageNum
	 *            显示页码
	 * @param courser
	 *            课程科目包括00:语文01:英语02:数学03:物理04:化学05:科学
	 * @param grade
	 *            年级
	 *            包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10
	 *            :初中通用
	 * @param user
	 *            用途 00：课件01：书籍02：习题03：考题
	 * @param resource
	 *            来源 来源包括00：学校01：个人02：出版社03：培训机构
	 * @param proType
	 *            商品类型 00:书籍01:课件02:习题03：考卷
	 * @return
	 * @throws Exception
	 */
	public ArrayList<StudyAssessment> getStudyAssessment(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType) throws Exception;
	
	/**
	 * 书架信息
	 * 
	 * @param userid
	 *            用户的ID 必传
	 * @param showLineNum
	 *            每页显示的条数 默认为12条
	 * @param proId
	 *            书的id 
	 * @param orderby
	 *            排序方式 00：按照日期倒序排序；所有场景使用
	 * @param PageNum
	 *            显示页码
	 * @param courser
	 *            课程科目包括00:语文01:英语02:数学03:物理04:化学05:科学
	 * @param grade
	 *            年级
	 *            包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10
	 *            :初中通用
	 * @param user
	 *            用途 00：课件01：书籍02：习题03：考题
	 * @param resource
	 *            来源 来源包括00：学校01：个人02：出版社03：培训机构
	 * @param proType
	 *            商品类型 00:书籍01:课件02:习题03：考卷
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getBookShelf(String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType,String inputInfo) throws Exception;
	
	/**
	 * 赠送信息查询和推荐信息查询
	 *  @param scern
	 *            场景 必传  传"06"为赠送信息查询  传“07”为推荐信息查询
	 * @param userid
	 *            用户的ID 必传
	 * @param showLineNum
	 *            每页显示的条数 默认为12条
	 * @param proId
	 *            书的id 
	 * @param orderby
	 *            排序方式 00：按照日期倒序排序；所有场景使用
	 * @param PageNum
	 *            显示页码
	 * @param operatetype 
	 *            00推荐 01赠送  0600推荐 0700被推荐   0601赠送 0701被赠送
	 * @param courser
	 *            课程科目包括00:语文01:英语02:数学03:物理04:化学05:科学
	 * @param grade
	 *            年级
	 *            包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10
	 *            :初中通用
	 * @param user
	 *            用途 00：课件01：书籍02：习题03：考题
	 * @param resource
	 *            来源 来源包括00：学校01：个人02：出版社03：培训机构
	 * @param proType
	 *            商品类型 00:书籍01:课件02:习题03：考卷
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getRecommentInfor(String scern,String userid,
			String showLineNum, String proId, String orderby, int PageNum,String operatetype,
			String courser, String grade, String user, String resource,
			String proType,String inputInfo) throws Exception;
}
