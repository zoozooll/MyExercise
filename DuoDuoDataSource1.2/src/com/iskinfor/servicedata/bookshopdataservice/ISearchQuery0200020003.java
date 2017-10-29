package com.iskinfor.servicedata.bookshopdataservice;

import java.util.Map;
import com.dcfs.esb.client.exception.TimeoutException;

public interface ISearchQuery0200020003 {

	/**
	 * 功能描述：获取搜索商品的基本信息
	 * 参数描述：
	 * String scern情景, 
	 * String rspTag
	 * String userId用户ID,
	 * String proId商品编号,
	 * String couse科目,科目包括00:语文01:英语02:数学03:物理04:化学05:科学(所有ID以0200020004中的查询结果为准)
     * String grade年级,年级包括00:一年级01:二年级02:三年级03:四年级04:五年级05:六年级06:小学通用07:初一08:初二09:初三10:初中通用(所有ID以0200020004中的查询结果为准)
     * String space篇幅,篇幅包括00:上册01:下册02:全册(所有ID以0200020004中的查询结果为准)
     * int use用途,用途包括00:教材01:写作02:词汇语法03:口语听力04:阅读05:电影原声06:歌曲07:竞赛08:智力(所有ID以0200020004中的查询结果为准) 
     * String type类别,类别包括00:同步课程01:名师辅导02:名校课件03:单元考04:期中考05:期末考06:中考历年真题07:模拟考08:巩固篇09:提高篇10:进阶篇(所有ID以0200020004中的查询结果为准)
	 * String source来源,来源包括00:学校01:出版社02:个人03:教育机构(所有ID以0200020004中的查询结果为准) 
	 * String courseName课程名称, 02场景使用
	 * String teacherName名师名称,03场景使用
	 * String showLineNum显示信息条数,
	 * int pagNum页码, 
	 * String proType商品类别,00:课件01:书籍02:习题03:考卷 
	 * String inputInfo搜索信息,按照需求规则输入对应的信息
	 * String orderBy排序方式 00:成交量升序 01:成交量降序02:价格升序 03:价格降序04:学习量升序 05:学习量降序06:添加日期升序 07:添加日期降序08:浏览量升序 09:浏览量降序10:推荐人气升序 11:推荐人气降序12:收藏人气升序 13:收藏人气降序
	 * String showLineNum显示信息条数 必传项
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getAdvancedSearch(String scern,String rspTag ,String userId,
			String proId,Map<String, Object> couse, Map<String, Object> grade,Map<String, Object> space,Map<String, Object> use, 
			Map<String, Object> type,Map<String, Object> source, String courseName, String teacherName,
			String showLineNum, int pagNum, String proType, String inputInfo,
			String orderBy )throws TimeoutException,Exception;
	
	public Map<String, Object>  getSearchBookShelf(String scern,String rspTag ,String userId,
			String showLineNum,String proId,String orderBy,int pagNum,String couse, 
			String grade,int use,String source,  String inputInfo
			 )throws TimeoutException,Exception;
	
	public Map<String, Object>  getSearchShop(String scern,String rspTag ,String userId,
			String showLineNum,String proId,String orderBy,int pagNum,String couse, 
			String grade,int use,String source,  String inputInfo
			 )throws TimeoutException,Exception;
}
