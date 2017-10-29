package com.iskinfor.servicedata.bookshopdataservice;

import java.util.Map;

import com.dcfs.esb.client.exception.TimeoutException;
/**
 * 1.1	商品展示（0200010001）
 * @author Administrator
 *
 */
public interface IShowProduct0200010001 {
	/**
	 * 功能描述：获取书店中所有产品
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getAllProduct(int pag )throws TimeoutException,Exception;
	/**
	 * 获取书店中所有的书籍
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getAllBook(int pag )throws TimeoutException,Exception;
	/**
	 * 获取书店中所有的课件
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getAllCourseware(int pag )throws TimeoutException,Exception;
	/**
	 * 获取书店中所有的习题
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getAllExercise(int pag )throws TimeoutException,Exception;
	/**
	 * 得到所有的考卷
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getAllExam( int pag)throws TimeoutException,Exception;
}
