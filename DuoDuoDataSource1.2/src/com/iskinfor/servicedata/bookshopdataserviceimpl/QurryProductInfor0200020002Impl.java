package com.iskinfor.servicedata.bookshopdataserviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.client.ESBClient;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IQurryProductInfor0200020002;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.pojo.DuoDuoRecod;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.pojo.TranVolum;

/**
 * 商品信息查询
 * 
 * @author Administrator
 */
public class QurryProductInfor0200020002Impl implements
		IQurryProductInfor0200020002 {
	/**
	 * 根据商品ID查询商品详细信息
	 * 
	 * @param proId
	 * @return
	 */
	public Map<String, Object> getProductById(String[] proIds) {
		ArrayList<Product> pList = new ArrayList<Product>();
		for (int i = 0; i < proIds.length; i++) {
			Product product = new Product();
			product.setProId(proIds[i]);
			pList.add(product);
		}
		return operate0200020002("01", pList, null);
	}

	/**
	 * 批量查看商品信息
	 */
	public Map<String, Object> getBatchProductById(ArrayList<Product> plist)
			throws Exception {
		return operate0200020002("01", plist, null);
	}

	/**
	 * 查看应用基本信息
	 */
	public Map<String, Object> getProuductBaseInforById(String proId)
			throws Exception {
		ArrayList<Product> pList = new ArrayList<Product>();
		Product product = new Product();
		product.setProId(proId);
		pList.add(product);
		return operate0200020002("02", pList, null);
	}

	/**
	 * 根据ID查看交易量
	 */
	public Map<String, Object> getTranVolumById(String proId) throws Exception {
		ArrayList<Product> pList = new ArrayList<Product>();
		Product product = new Product();
		product.setProId(proId);
		pList.add(product);
		return operate0200020002("03", pList, null);
	}

	/**
	 * 根据ID得到交易明细
	 */
	public Map<String, Object> getTransaction(String proId) throws Exception {
		ArrayList<Product> pList = new ArrayList<Product>();
		Product product = new Product();
		product.setProId(proId);
		pList.add(product);
		return operate0200020002("04", pList, null);
	}

	/**
	 * 得到商品的状态
	 */
	public Map<String, Object> getProducetStart(String proId, String userId)
			throws Exception {
		ArrayList<Product> pList = new ArrayList<Product>();
		Product product = new Product();
		product.setProId(proId);
		pList.add(product);
		return operate0200020002("05", pList, userId);
	}

	/**
	 * 查看商品的信息(评分，价格，销售量，作者等)
	 * 
	 * @param scern
	 *            商品场景
	 * @param plist
	 *            商品集合
	 * @param userID
	 *            用户ID
	 * @return
	 */
	private Map<String, Object> operate0200020002(String scern,
			ArrayList<Product> plist, String userID) {
		// 返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 产品集合
		ArrayList<Product> proList = new ArrayList<Product>();
		// 交易量
		ArrayList<TranVolum> tranList = new ArrayList<TranVolum>();
		// 朵朵记录集合
		ArrayList<DuoDuoRecod> duoList = new ArrayList<DuoDuoRecod>();

		// 请求头
		CompositeData header = CdUtil.sysHeaderInstance("0200020002", scern);

		// 参数
		Map<String, Object> args = new HashMap<String, Object>();
		if (userID != null && !"".equals(userID)) {
			args.put("USER_ID", userID);
		}

		List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();

		if (plist != null && plist.size() != 0) {
			for (int i = 0; i < plist.size(); i++) {
				Map<String, Object> idMaps = new HashMap<String, Object>();
				idMaps.put("PRO_ID", plist.get(i).getProId());
				ids.add(idMaps);
			}
		}
		args.put("PRO_ARRAY", ids);

		// 组装请求体
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		try {
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request02(reqData);
			} catch (Exception e) {
				throw e;
			}
			CompositeData cbody = CdUtil.getBody(rspData);

			Array uArray;
			// 只有在场景3时返回的是NUM_ARRAY，04场景返回的是RECORD_ARRAY 其他场景返回的是PRO_ARRAY
			if ("03".equals(scern)) {
				uArray = cbody.getArray("NUM_ARRAY");
			} else if ("04".equals(scern)) {
				uArray = cbody.getArray("RECORD_ARRAY");
				Field totalNum = cbody.getField("TOTAL_NUM");
				// 将总记录数加到返回结果中
				result.put(DataConstant.TOTAL_NUM, totalNum.getValue());
			} else {
				uArray = cbody.getArray("PRO_ARRAY");
			}

			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
				if ("03".equals(scern)) {
					TranVolum tanVolmObject = new TranVolum();
					// 商品ID
					String tranProId = (String) cData.getField("PRO_ID")
							.getValue();
					if (null != tranProId && !"".equals(tranProId)) {
						tanVolmObject.setProId(tranProId);
					}
					// 商品成交量
					String taanNum = (String) cData.getField("TRAN_VOLUM")
							.getValue();
					if (null != taanNum && !"".equals(taanNum)) {
						tanVolmObject.setTranVolum(taanNum);
					}
					tranList.add(tanVolmObject);

				} else if ("04".equals(scern)) {
					DuoDuoRecod duoduo = new DuoDuoRecod();
					// 买家
					String duoUserId = (String) cData.getField("USER_ID")
							.getValue();
					if (null != duoUserId && !"".equals(duoUserId)) {
						duoduo.setUserId(duoUserId);
					}
					// 书名
					String duoProName = (String) cData.getField("PRO_NAME")
							.getValue();
					if (null != duoProName && !"".equals(duoProName)) {
						duoduo.setProName(duoProName);
					}
					// 成交金额
					String duoOrderAccount = (String) cData.getField(
							"ORDER_ACOUNT").getValue();
					if (null != duoOrderAccount && !"".equals(duoOrderAccount)) {
						duoduo.setOrderAccount(duoOrderAccount);
					}
					// 数量
					String duoTranVolume = (String) cData.getField(
							"TRAN_VOLUME").getValue();
					if (null != duoTranVolume && !"".equals(duoTranVolume)) {
						duoduo.setTanVolum(duoTranVolume);
					}
					// 交易时间 ORDER_DATE STRING(10)
					String duoOrderDate = (String) cData.getField("ORDER_DATE")
							.getValue();
					if (null != duoOrderDate && !"".equals(duoOrderDate)) {
						duoduo.setOrderData(duoOrderDate);
					}
					// ORDER_STATE STRING 交易状态
					String duoOrderState = (String) cData.getField(
							"ORDER_STATE").getValue();
					if (null != duoOrderState && !"".equals(duoOrderState)) {
						duoduo.setOrderState(duoOrderState);
					}
					duoList.add(duoduo);

				} else {
					Product productDto = new Product();
					String proId = (String) cData.getField("PRO_ID").getValue();
					// 得到产品的ID
					if (null != proId && !"".equals(proId)) {
						productDto.setProId(proId);
					}

					if ("01".equals(scern) || "02".equals(scern)) {
						// 得到产品名
						String proName = (String) cData.getField("PRO_NAME")
								.getValue();
						if (null != proName && !"".equals(proName)) {
							productDto.setProName(proName);
						}

						// 得到小图的路径
						String small_path = (String) cData.getField(
								"SMALL_IMG_PATH").getValue();
						if (null != small_path && !"".equals(small_path)) {
							productDto.setSmallImgPath(small_path);
						}

						// 得到大图路径
						String big_path = (String) cData.getField(
								"BIG_IMG_PATH").getValue();
						if (null != big_path && !"".equals(big_path)) {
							productDto.setBigImgPath(big_path);
						}
						// 得到产品价格
						String proPrice = (String) cData.getField("PRO_PRICE")
								.getValue();
						if (null != proPrice && !"".equals(proPrice)) {
							productDto.setProPrice(proPrice);
						}

						// 得到作者ATHER
						String proAther = (String) cData.getField("ATHER")
								.getValue();
						if (null != proAther && !"".equals(proAther)) {
							productDto.setAther(proAther);
						}
						// 得到出版社
						String publisher = (String) cData.getField("PUBLISHER")
								.getValue();
						if (null != publisher && !"".equals(publisher)) {
							productDto.setPublisher(publisher);
						}
						if ("01".equals(scern)) {
							// 得到商品评分
							String priRating = (String) cData.getField(
									"PRO_RATING").getValue();
							if (null != priRating && !"".equals(priRating)) {
								productDto.setProRating(priRating);
							}
							// 得到商品的销售量
							String tranNum = (String) cData
									.getField("TRAN_NUM").getValue();
							if (null != tranNum && !"".equals(tranNum)) {
								productDto.setTranNum(tranNum);
							}
							// 得到购买数量
							String quantity = (String) cData.getField(
									"QUANTITY").getValue();
							if (null != quantity && !"".equals(quantity)) {
								productDto.setQuantity(quantity);
							}
							// 得到编辑推荐
							String editorChoice = (String) cData.getField(
									"EDITOR_CHOICE").getValue();
							if (null != editorChoice
									&& !"".equals(editorChoice)) {
								productDto.setEditorChoice(editorChoice);
							}
							// 得到内容简介
							String introContent = (String) cData.getField(
									"INTRO_CONTENT").getValue();
							if (null != introContent
									&& !"".equals(introContent)) {
								productDto.setIntroContent(introContent);
							}
							// 得到评价的人数
							String ratingRum = (String) cData.getField(
									"RATING_NUM").getValue();
							if (null != ratingRum && !"".equals(ratingRum)) {
								int intRatingRum = Integer.valueOf(ratingRum);
								if (intRatingRum < 1) {
									productDto.setIntroContent("0");
								} else {
									productDto.setIntroContent(ratingRum);
								}

							}
							// 得到收藏人数
							String collectonNum = (String) cData.getField(
									"COLLECTION_NUM").getValue();
							if (null != collectonNum
									&& !"".equals(collectonNum)) {
								productDto.setCollectionNum(collectonNum);
							}

							// TODO 以PRO_ID为条件，通过与0100020001+07服务组合取得推荐人数

						}
					}
					if ("02".equals(scern)) {
						// 得到商品评分
						int priRating02 = (Integer) cData
								.getField("PRO_RATING").getValue();
						if (priRating02 > 0) {
							productDto.setProPrice(String.valueOf(priRating02));
						}
						// 得到商品的销售量
						int tranNum02 = (Integer) cData.getField("TRAN_NUM")
								.getValue();
						if (tranNum02 > 0) {
							productDto.setTranNum(String.valueOf(tranNum02));
						}
					}

					if ("05".equals(scern)) {
						// PRO_STATUS STRING 商品状态
						String proStatus = (String) cData
								.getField("PRO_STATUS").getValue();
						if (null != proStatus && !"".equals(proStatus)) {
							productDto.setProStatus(proStatus);
						}
					}
					// 将产品封装到list中

					proList.add(productDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将列表压入到返回结果中
		if ("03".equals(scern)) {
			result.put(DataConstant.TRAN_KEY, tranList);
		} else if ("04".equals(scern)) {
			result.put(DataConstant.DUO_LIST_KEY, duoList);
		} else {
			result.put(DataConstant.LIST, proList);
		}
		return result;
	}

}
