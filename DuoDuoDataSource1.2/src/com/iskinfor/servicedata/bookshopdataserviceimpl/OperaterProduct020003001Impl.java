package com.iskinfor.servicedata.bookshopdataserviceimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.client.ESBClient;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.datahelp.SysHeader;
import com.iskinfor.servicedata.pojo.DuoDuoRecod;
import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;

public class OperaterProduct020003001Impl implements IOperaterProduct0200030001 {
	
	
	/**
	 * 根据商品ID删除购物车内的商品
	 * 
	 * @param userId
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsById(String userId, String[] goodsId)
			throws Exception {
		boolean tag = false;
		Map Mbody = new HashMap();
		Mbody.put("USER_ID", userId); // 用户id
		List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();
		if (null == goodsId) {
			return false;
		} else {
			for (int i = 0; i < goodsId.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PRO_ID", goodsId[i]);
				ids.add(map);
			}
		}
		Mbody.put("PRO_ARRAY", ids);
		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "01"); // header
		CompositeData body = CdUtil.bodyInstance(Mbody); // body
		CompositeData reqData = CdUtil.reqData(header, body); // 请求报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request02(reqData); // 返回的报文
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
				// 成功操作
				tag = true;
			} else {
				// 失败操作
				tag = false;
			}
		} catch (Exception e) {
			throw e;
		}
		return tag;
	}

	/**
	 * 将商品移入收藏夹
	 * 
	 * @param userId
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public boolean saveGoods(String userId, String[] goodsId) throws Exception {
		boolean msg = false; // 商品移入收藏夹返回值
		Map body = new HashMap();
		body.put("USER_ID", userId);
		List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < goodsId.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PRO_ID", goodsId[i]);
			ids.add(map);
		}
		body.put("PRO_ARRAY", ids);
		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "02"); // header
		CompositeData Hbody = CdUtil.bodyInstance(body); // body
		CompositeData reqData = CdUtil.reqData(header, Hbody); // 请求报文
		try {
			CompositeData rspData = ESBClient.request02(reqData); // 返回的报文
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& ("000000".equals(sysHeader.getRet().get(0).getCode()) || "000003"
							.equals(sysHeader.getRet().get(0).getCode()))) {
				// 成功操作
				msg = true;
			} else {
				msg = false;
			}
		} catch (Exception e) {
		}
		return msg;
	}

	/**
	 * 将商品添加到购物车中
	 * 
	 * @param usrId
	 * @param proIds
	 * @param proNums
	 * @return
	 * @throws Exception
	 */

	public boolean addShopCart(String usrId, String proid, String price,
			String proNum) throws Exception {
		boolean tag = false;
		// 参数
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PRO_ID", proid);
		map.put("PRO_NUM", proNum);
		map.put("PRO_PRICE", price);
		ls.add(map);

		// 语法参数
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("USER_ID", usrId);
		args.put("PRO_ARRAY", ls);

		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "03");

		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);

		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request02(reqData);
		} catch (Exception e) {
			throw e;
		}
		SysHeader sysHeader = CdUtil.verifyHeader(rspData);
		if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
				&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
			tag = true;
		} else if (SysHeader.STATUS.FAIL == sysHeader.getStatu()
				&& "000005".equals(sysHeader.getRet().get(0).getCode())) {
			tag = false;
		} else {
			tag = true;
		}
		return tag;
	}

	/**
	 * 删除订单
	 * 
	 * @param orderIds
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean delOrders(String[] orderIds, String userId) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (null == orderIds) {
			return false;
		} else {
			for (int i = 0; i < orderIds.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ORDER_ID", orderIds[i]);
				list.add(map);
			}
		}
		args.put("USER_ID", userId);
		args.put("ORDER_ARRAY", list);

		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "06");

		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		CompositeData data = CdUtil.reqData(header, body);
		try {
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request02(data);
			} catch (Exception e) {
				throw e;
			}
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode()))
				return true;
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	/**
	 * 清空购物车
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean clearShopCart(String userId) throws Exception {
		boolean tag = false;
		Map Mbody = new HashMap();
		Mbody.put("USER_ID", userId); // 用户id

		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "08"); // header
		CompositeData body = CdUtil.bodyInstance(Mbody); // body
		CompositeData reqData = CdUtil.reqData(header, body); // 请求报文
		try {
			CompositeData rspData = ESBClient.request02(reqData); // 返回的报文
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
				// 成功操作
				tag = true;
			} else {
				// 失败操作
				tag = false;
			}
		} catch (Exception e) {
			throw e;
		}
		return tag;

	}

	/**
	 * 取消收藏
	 * 
	 * @param ids
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean delFavorate(String userId, String[] ids) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < ids.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PRO_ID", ids[i]);
			list.add(map);
		}
		args.put("PRO_ARRAY", list);
		args.put("USER_ID", userId);
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "11");

		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		CompositeData data = CdUtil.reqData(header, body);
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request02(data); // 返回给你的报文
		} catch (Exception e) {
			e.printStackTrace();
			//throw e;
		}
		SysHeader sysHeader = CdUtil.verifyHeader(rspData);
		if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
				&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 *  更新订单状态
	 *  (non-Javadoc)
	 * @see com.iskyinfor.duoduo.servicedata.bookshopdataservice.IOperaterProduct0200030001#updateorderType(java.lang.String[])
	 */
	public String updateorderType(String[] orderIds) throws Exception {
		String msg = null;
		List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();
		Map<String, Object> mbody = new HashMap<String, Object>();
		for (int i = 0; i < orderIds.length; i++) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("ORDER_ID", orderIds[i]); // 订单id
			newMap.put("TRAN_STATE", "01"); // 订单状态
			ids.add(newMap);
		}
		mbody.put("ORDER_ARRAY", ids);
		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "12"); // header
		CompositeData body = CdUtil.bodyInstance(mbody); // body
		CompositeData reqData = CdUtil.reqData(header, body); // 请求报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request02(reqData); // 返回的报文
		} catch (Exception e) {
			throw e;
		}
		if (null != rspData) {
			CompositeData cd = rspData.getStruct("BODY");
			int totalnum = cd.getField("TOTAL_NUM").intValue();
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
				msg = DataConstant.UPDATE_ORDERTYPE_SUCCESS;
			} else if (SysHeader.STATUS.FAIL == sysHeader.getStatu()
					&& "000005".equals(sysHeader.getRet().get(0).getCode())) {
				msg = DataConstant.UPDATA_ORDERTYPE_FAIL;
			}
		} else {
		}
		return msg;
	}

	@Override
	public String addOrders(String userId, ArrayList<Product> list)
			throws Exception {
		DecimalFormat myformat = new java.text.DecimalFormat("#0.00"); // 保留2位小数
		Order order = new Order();

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("USER_ID", userId);
		double order_acount = 0;
		List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Product product = list.get(i);
			// Map<String, Object> map = list.get(i);
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("PRO_ID", product.getProId());
			newMap.put("PRO_PRICE", product.getProPrice());
			int proNo= product.getProNum();
			if(!(proNo>0)){
				proNo=1;
			}
			newMap.put("PRO_NUM",proNo);
			double total = Double.valueOf(product.getProPrice())
					* product.getProNum();
			order_acount = order_acount + total;
			newMap.put("PRO_ACOUNT", order_acount);
			ids.add(newMap);
		}
		// 添加订单
		order.setOrderAccount(myformat.format(order_acount));
		body.put("ORDER_ACOUNT", Double.parseDouble(myformat
				.format(order_acount))
				+ "");
		body.put("PRO_ARRAY", ids);

		CompositeData header = CdUtil.sysHeaderInstance("0200030001", "07"); // header
		CompositeData Hbody = CdUtil.bodyInstance(body); // body
		CompositeData reqData = CdUtil.reqData(header, Hbody); // 请求报文
		String orderNumber = null;
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request02(reqData); // 返回的报文
			System.out.println("rspData==>" + rspData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != rspData) {
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
				CompositeData cd = rspData.getStruct("BODY");
				Field ordernum = cd.getField("ORDER_ID"); // 去订单编号
				orderNumber = (String) ordernum.getValue();
			}
		} 
		return orderNumber;
	}

	@Override
	public boolean putBuyedProducetToShelf(String userId,
			ArrayList<Product> plist) throws TimeoutException, Exception {
		boolean tag = false;
		Map Mbody = new HashMap();
		Mbody.put("USER_ID", userId); // 用户id
		List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();
		if (null == plist) {
			return false;
		} else {
			for (int i = 0; i < plist.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Product product = plist.get(i);
				map.put("PRO_ID", product.getProId());
				int nums = product.getProNum();
				if (nums > 0) {
					map.put("PRO_NUM", product.getProNum());
				} else {
					map.put("PRO_NUM", 1);
				}
				ids.add(map);
			}
		}
		Mbody.put("PRO_ARRAY", ids);
		CompositeData header = CdUtil.sysHeaderInstance("0100030001", "11"); // header
		CompositeData body = CdUtil.bodyInstance(Mbody); // body
		CompositeData reqData = CdUtil.reqData(header, body); // 请求报文
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request01(reqData); // 返回的报文
			System.out.println("rspData==>" + rspData);
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if (SysHeader.STATUS.SUCCESS == sysHeader.getStatu()
					&& "000000".equals(sysHeader.getRet().get(0).getCode())) {
				// 成功操作
				tag = true;
			} else {
				// 失败操作
				tag = false;
			}
		} catch (Exception e) {
			throw e;
		}
		return tag;
	}

}
