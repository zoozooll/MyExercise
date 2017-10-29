package com.iskinfor.servicedata.bookshopdataservice;

import java.util.ArrayList;
import java.util.Map;

import com.iskinfor.servicedata.pojo.Product;

/**
 * 商品信息查询
 * @author Administrator
 *
 */
public interface IQurryProductInfor0200020002 {
	/**
	 * 根据商品ID查询商品信息
	 * @param proId 商品id数组
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getProductById(String[] proIds) throws Exception;
	/**
	 * 商品集合
	 * @param plist
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  getBatchProductById(ArrayList<Product> plist) throws Exception;
	/**
	 * 查看商品基本信息
	 * @param proId 商品id
	 * @return
	 * @throws Exception
	 */
	public Map<String ,Object> getProuductBaseInforById(String proId) throws Exception;
	/**
	 * TODO
	 * 商品的交易量
	 * @param proId
	 * @return
	 * @throws Exception
	 */
	public Map<String ,Object> getTranVolumById(String proId) throws Exception;
	/**
	 * 交易明细
	 * @param proId
	 * @return
	 */
	public Map<String ,Object> getTransaction(String proId)throws Exception;
	/**
	 * 得到商品的状态
	 * @param proId 商品的id
	 * @param userId 用户的密码
	 * @return
	 * @throws Exception
	 */
	public Map<String ,Object> getProducetStart(String proId,String userId)throws Exception;

//	高级搜索
//	订单管理
//	充值
//	加入购物车
//	加入收藏夹
//	从购物车删除
//	从收藏夹删除
//	余额查询
//	购买
//	查询商品的详细信息
//	书架信息查询1.书架中的所有，2.书架中的课件，3书架中的试卷，4
//	赠送，推荐，笔记，书签
//	书架管理
//	学习，显示所有课件，显示同步学习，显示名师辅导
//	登入，验证id 和密码
}
