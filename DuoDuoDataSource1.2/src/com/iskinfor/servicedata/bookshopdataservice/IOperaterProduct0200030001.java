package com.iskinfor.servicedata.bookshopdataservice;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;


/**
 * 
 * @author Administrator
 *	商品操作
 */
public interface IOperaterProduct0200030001 {
	/**
	 * 删除商品
	 * @param useID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsById(String userId, String[] goodsId)throws Exception ;
	/**
	 * 将商品添加到购物车中
	 * @param usrId  用户的id
	 * @param proIds 商品的id
	 * @param price  商品的价格
	 * @param proNums 商品的数量
	 * @return
	 * @throws Exception
	 */
	public boolean addShopCart(String usrId, String proid, String price,String proNum)
			throws Exception ;
	/**
	 * 删除订单
	 * @param orderIds
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean delOrders(String[] orderIds, String userId) throws Exception ;
	/**
	 * 添加订单的方法
	 * @param userId 用户的id
	 * @param list	  商品列表
	 * @return
	 * @throws Exception
	 */
	public String addOrders(String userId, ArrayList<Product> list)
			throws Exception ;
	/**
	 * 清空购物车
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean clearShopCart(String userId) throws Exception ;

	/**
	 * 取消收藏
	 * @param ids
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean delFavorate(String userId,String[] ids) throws Exception ;
	/**
	 * 更新订单状态
	 * @param orderIds
	 * @return
	 * @throws Exception
	 */
	public String updateorderType(String[] orderIds) throws Exception ;
	/**
	 * 将商品添加到收藏夹中
	 * @param userId
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public boolean saveGoods(String userId, String[] goodsId) throws Exception;
	
	/**
	 * 将购买成功的商品放入我的书架中
	 * @param userId 用户的id
	 * @param productList 商品集合
	 * 每个商品中必须的有 商品名称和商品的数量
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public boolean putBuyedProducetToShelf(String userId,ArrayList<Product> productList)throws TimeoutException,Exception; 
}
