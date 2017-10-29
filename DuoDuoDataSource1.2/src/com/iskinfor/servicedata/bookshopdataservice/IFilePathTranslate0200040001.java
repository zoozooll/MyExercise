package com.iskinfor.servicedata.bookshopdataservice;

import java.util.Map;
import java.util.concurrent.TimeoutException;


/**
 * 文件路径传输
 * @author Administrator
 *
 */
public interface IFilePathTranslate0200040001 {
	/**
	 * 根据ID获取文件的以下路径
	 *      PRO_VIEW_PATH	STRING	商品浏览路径	
	 *		PRO_REAL_PATH	STRING	商品真实路径	
	 *		FILE_TYPE	STRING	文件类型	
	 *		FILE_BIT	STRING	文件规格	
	 *		FILE_SITE	STRING	文件大小	
	 *		FILE_TIME	STRING	文件播放时间	
	 *		FILE_HASH	STRING	文件HASH代码	
	 *		SERVER_ID	STRING	文件所在服务器ID	
	 * 获取数据的方法 假设返回的MAP 为rusult
	 *    	商品浏览路径	=result.get(DataConstant.PRO_VIEW_PATH_KEY);
	 *		商品真实路径	=result.get(DataConstant.PRO_REAL_PATH_KEY);
	 *		文件类型	=result.get(DataConstant.FILE_TYPE_KEY);
	 *		文件规格	=result.get(DataConstant.FILE_BIT_KEY);
	 *		文件大小		=result.get(DataConstant.File_SITE_KEY);
	 *		文件播放时间	=result.get(DataConstant.File_TIME_KEY);
	 *		文件HASH代码		=result.get(DataConstant.File_HASH_KEY);
	 *		文件所在服务器ID	=result.get(DataConstant.SERVER_ID_KEY);
	 */
	public Map<String,String> getProPath(String proId)throws TimeoutException,Exception;
}
