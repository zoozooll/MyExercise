package com.iskinfor.servicedata.bookshopdataservice;


import java.util.Map;

/**
 * 操作记录查询
 * @author Administrator
 *
 */
public interface IOperaterRecordQuerry0200020001 {
	
	/**
	 * 购物车查询
	 * @param userId 用户的ID
	 * @return
	 * 返回的是是一个MAp值中，map中包含着，总条目数，和购物车对象，购物车对象中含有价格总数和商品列表
	 * 
	 * 取条目数方法：getDataConstant
	 * @throws Exception
	 */
	public Map<String, Object>  querryShopingCat(String userId)throws Exception;
	/**
	 * 收藏信息查询
	 * @param userId 用户ID
	 *  @param colletType 收藏类型
	 *  00：所有收藏
	 *  01：书籍收藏
	 *  02：课件收藏
	 *  03：练习收藏
	 *  04：考卷收藏；  
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object>  querryStoreInfo(String userId,String colletType)throws Exception;
	/**
	 * 查询订单信息
	 * @param userId
	 * @param start_date 时间格式为20100701表示2010年七月一日
	 * @param end_data
	 * @param pag  显示第几条数据
	 * @param showLineNum 每页显示的条目数
	 * @return 订单列表 ，总记录书
	 * @throws Exception
	 */
	public Map<String, Object>  querryOrderInfor(String userId,String start_date,String end_data,int pag,String showLineNum,String orderId)throws Exception;
	/**
	 * 查询订单明细
	 * @param userId 用户的id
	 * @param orderId 订单号
	 * @param orderyby 排序方式
	 * 00：商品名称升序 01：商品名称降序 02：商品单价升序 03：商品单价升序 04：商品金额升序 05：商品金额降序
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> querryOrderProduct(String userId,String orderId,String orderyby)throws Exception; 
	
	
}
