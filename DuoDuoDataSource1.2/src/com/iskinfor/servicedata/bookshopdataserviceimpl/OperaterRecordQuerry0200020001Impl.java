package com.iskinfor.servicedata.bookshopdataserviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.client.ESBClient;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterRecordQuerry0200020001;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.pojo.ShopCar;


/**
 *	操作记录查询	
 * @author Administrator
 */
public class OperaterRecordQuerry0200020001Impl implements IOperaterRecordQuerry0200020001 {
	/**
	 * 查询用户购物车
	 */
	public Map<String, Object> querryShopingCat(String userId) throws Exception {
		return operate0200020001("01", userId, null, null, 0, null, null, null, null);
	}
	/**
	 * 收藏信息查询
	 */
	public Map<String, Object> querryStoreInfo(String userId,String colletType) throws Exception {
		return operate0200020001("02", userId, null, colletType, 0, null, null, null, null);
	}
	
    /**
     * 查询订单信息
     */
	public Map<String, Object> querryOrderInfor(String userId,
			String start_date, String end_data,int pag,String showLinNum,String orderId) throws Exception {
		return operate0200020001("05", userId, showLinNum, null, pag, null, start_date, end_data, orderId);
	}
	/**
	 * 查看订单明细
	 * @param userId
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> querryOrderProduct(String userId,String orderId,String orderyby)
			 throws Exception {
		return operate0200020001("06", userId, null, null, 0, orderyby, null, null, orderId);
	}
	
	/**
	 * 用户购买或浏览商品的时浏览去其他的商品信息（商品名称，价格，评分）以及购物车和收藏信息的查询。
	 * @param scern 场景
	 * @param userId 用户id
	 * @param show_lin_num 每页显示条目
	 * @param collect_type 收藏类型
	 * @param pagenum 页数
	 * @param oderyby 排序
	 * @param start_data 起止日期
	 * @param end_date 终止日期
	 * @param order_id 订单id
	 * @return
	 */
	private Map<String,Object> operate0200020001(String scern,String userId,String show_lin_num,String collect_type,int pagenum,String oderyby,String start_data,String end_date,String order_id){
//		将要返回的结果
		Map<String, Object> result = new HashMap<String, Object>();
//		产品集合
		ArrayList<Product> proList=new ArrayList<Product>();
//		订单结合
		ArrayList<Order> orderList=new ArrayList<Order>();

//		商品总金额
		float totalSum = 0 ;
//		请求头
		CompositeData header = CdUtil.sysHeaderInstance("0200020001", scern);
//		参数
		Map<String, Object> args = new HashMap<String, Object>();
		if(userId!=null&&!"".equals(userId)){
			args.put("USER_ID", userId);
		}
		if(show_lin_num!=null&&!"".equals(show_lin_num)){
			args.put("SHOW_LINE_NUM", show_lin_num);
		}else{
			args.put("SHOW_LINE_NUM", "15");
		}
		if(collect_type!=null&&!"".equals(collect_type)){
			args.put("COLLECT_TYPE", collect_type);
		}
		if(pagenum>0){
			args.put("PAGE_NUM", pagenum);
		}
		if(null!=oderyby&&!"".equals(oderyby)){
			args.put("ORDER_BY", oderyby);
		}else{
			args.put("ORDER_BY", "01");
		}
		if(start_data!=null&&!"".equals(start_data)){
			args.put("START_DATE", start_data);
		}
		if(end_date!=null&&!"".equals(end_date)){
			args.put("END_DATE", end_date);	
		}
		if(order_id!=null&&!"".equals(order_id)){
			args.put("ORDER_ID", order_id);	
		}
		//组装请求体
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		
		try {
//			返回的报文
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request02(reqData);
				System.out.println("rspData==>"+rspData);
			} catch (Exception e) {
				throw e;
			}

			CompositeData cbody = CdUtil.getBody(rspData);
			Array uArray;
//			根据文档只有05场景下返回的是“ORDER_ARRAY”，其他场景返回的都是“PRO_ARRAY”
//			if(scern.equals("05")){
//				uArray=cbody.getArray("ORDER_ARRAY");
//			}else{
			 uArray = cbody.getArray("PRO_ARRAY");
//			}
			
//			获得totalNum并将totalNum放入请求结果map中
			Field totalNum = cbody.getField("TOTAL_NUM");
			
			
			int showLine;
			if(!"".equals(show_lin_num)&&null!=show_lin_num){
				showLine=Integer.valueOf(show_lin_num);
			}else{
				showLine=15;
			}
			
			int all = (Integer) cbody.getField("TOTAL_NUM").getValue();
			// 将总记录数加到返回结果中
			result.put(DataConstant.TOTAL_NUM, all);
			
			int pags;
			if(all%showLine>0){
				pags=all/showLine+1;
			}else{
				pags=all/showLine;
			}
			System.out.print("showLine==>"+showLine);
			System.out.print("all==>"+all);
			System.out.print("pags==>"+pags);
			result.put(DataConstant.TOTAL_PAGS, pags);

			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
//				场景5的结果处理
				if(scern.equals("05")){
					Order order=new Order();
//					得到订单ID
					String orderId=(String) cData.getField("ORDER_ID").getValue();
					if(null!=orderId&&!"".equals(orderId)){
						order.setOrderId(orderId);
					}
//					得到订单金额
					String orderAccount=(String)cData.getField("ORDER_ACOUNT").getValue();
					if(null!=orderAccount&&!"".equals(orderAccount)){
						order.setOrderAccount(orderAccount);
					}
//					得到下单时间
					String orderData=(String)cData.getField("ORDER_DATE").getValue();
					if(null!=orderData&&!"".equals(orderData)){
						order.setOrderDate(orderData);
					}
//					得到订单状态
					String orderState=(String) cData.getField("ORDER_STATE").getValue();
					if(null!=orderState&&!"".equals(orderState)){
						order.setOrdeState(orderState);
					}
					
//					得到交易状态
					String tranState=(String) cData.getField("TRAN_STATE").getValue();
					if(null!=tranState&&!"".equals(tranState)){
						order.setTranState(tranState);
					}
					
					
//					将订单放入订单列表中
					orderList.add(order);
					
				}else{
					Product productDto = new Product();
					if("01".equals(scern)){
//						得到产品的ID
						String proId=(String) cData.getField("PRO_ID").getValue();
						if(null!=proId&&!"".equals(proId)){
							productDto.setProId(proId);
						}
						String proName=(String) cData.getField("PRO_NAME").getValue();
//						得到产品名
						if(null!=proName&&!"".equals(proName)){
							productDto.setProName(proName);
						}
//						得到小图路径
						String small_path=(String) cData.getField("SMALL_IMG_PATH").getValue();
						if(null!=small_path&&!"".equals(small_path)){
							productDto.setSmallImgPath(small_path);
						}
//						得到产品价格
						String proPrice=(String)cData.getField("PRO_PRICE").getValue();
						if(null!=proPrice&&!"".equals(proPrice)){
							productDto.setProPrice(proPrice);
						}
//						得到此商品的数目	
						String proNum1=(String)cData.getField("PRO_NUM").getValue();
						int proNum;
						proNum=Integer.valueOf(proNum1);
						if(!(proNum<0)){
							productDto.setProNum(proNum);
						}
//						设置商品小计
						float subTotal=Float.parseFloat(proPrice)*proNum;
						productDto.setSubtotal(subTotal);
//						商品总价
						totalSum=totalSum+subTotal;
//						得到优惠金额
						String offer_num=(String) cData.getField("OFFER_NUM").getValue();
						if(null!=offer_num&&!"".equals(offer_num)){
							productDto.setOfferNum(offer_num);
						}
						//积分
						int integral;
						String integral1=(String)cData.getField("INTEGRAL").getValue();
						integral=Integer.valueOf(integral1);
						if(!(integral<0)){
							productDto.setIntegral(integral);
						}
					}else if("02".equals(scern)){
						//得到产品ID
						String proId = (String) cData.getField("PRO_ID").getValue();
						productDto.setProId(proId);
//						得到产品名
						String proName=(String) cData.getField("PRO_NAME").getValue();
						if(null!=proName&&!"".equals(proName)){
							productDto.setProName(proName);
						}
//						得到产品价格
						String proPrice=(String)cData.getField("PRO_PRICE").getValue();
						if(null!=proPrice&&!"".equals(proPrice)){
							productDto.setProPrice(proPrice);
						}
//						得到小图路径
						String small_path=(String) cData.getField("SMALL_IMG_PATH").getValue();
						if(null!=small_path&&!"".equals(small_path)){
							productDto.setSmallImgPath(small_path);
						}

					}else if("03".equals(scern)){
						String proName=(String) cData.getField("PRO_NAME").getValue();
//						得到产品名
						if(null!=proName&&!"".equals(proName)){
							productDto.setProName(proName);
						}
//						得到产品价格
						String proPrice=(String)cData.getField("PRO_PRICE").getValue();
						if(null!=proPrice&&!"".equals(proPrice)){
							productDto.setProPrice(proPrice);
						}
//						得到小图路径
						String small_path=(String) cData.getField("SMALL_IMG_PATH").getValue();
						if(null!=small_path&&!"".equals(small_path)){
							productDto.setSmallImgPath(small_path);
						}

					}else if("04".equals(scern)){
						String proName=(String) cData.getField("PRO_NAME").getValue();
//						得到产品名
						if(null!=proName&&!"".equals(proName)){
							productDto.setProName(proName);
						}
//						得到产品价格
						String proPrice=(String)cData.getField("PRO_PRICE").getValue();
						if(null!=proPrice&&!"".equals(proPrice)){
							productDto.setProPrice(proPrice);
						}
//						得到小图路径
						String small_path=(String) cData.getField("SMALL_IMG_PATH").getValue();
						if(null!=small_path&&!"".equals(small_path)){
							productDto.setSmallImgPath(small_path);
						}

						
					}
					else if("06".equals(scern)){
//						   SPEAKER_ID=(string,4,0)=0101
					     
					         
//					         PRO_TYPE=(string,2,0)=00
					     String smallPath=(String) cData.getField("SMALL_IMG_PATH").getValue();
						   if(null!=smallPath&&!"".equals(smallPath)){
								productDto.setSmallImgPath(smallPath);
							} 
//						  String proType=(String) cData.getField("PRO_TYPE").getValue();
//						   if(null!=proType&&!"".equals(proType)){
//								productDto.setPro(smallPath);
//							}
						
//						得到产品的ID
						String proId=(String) cData.getField("PRO_ID").getValue();
						if(null!=proId&&!"".equals(proId)){
							productDto.setProId(proId);
						}
						String proName=(String) cData.getField("PRO_NAME").getValue();
//						得到产品名
						if(null!=proName&&!"".equals(proName)){
							productDto.setProName(proName);
						}
//						得到产品价格
						String proPrice=(String)cData.getField("PRO_PRICE").getValue();
						if(null!=proPrice&&!"".equals(proPrice)){
							productDto.setProPrice(proPrice);
						}
//						TRAN_VOLUME	INT	购买数量
						String proNum=(String)cData.getField("PRO_NUM").getValue();
						if(null!=proNum&&!"".equals(proNum)){
							productDto.setProNum(Integer.valueOf(proNum));
						}
						
					
						//购买金额
						String proAccout=(String)cData.getField("PRO_ACOUNT").getValue();
						if(null!=proAccout&&!"".equals(proAccout)){
							productDto.setProAccoutn(proAccout);
						}
//						
					}
//				将产品封装到list中
				proList.add(productDto);
				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将列表压入到返回结果中
		
//		如果是01场景将要把购物车对象放入map中返回
		if(scern.equals("01")){
		  ShopCar shopCar=new ShopCar();
//		  将商品总价放入购物车中
		  shopCar.setTotalSum(totalSum);
//		  将商品列表放入购物车中
		  shopCar.setProductList(proList);
//		 将购物车加入到结果中
		  result.put(DataConstant.SHOPCAR_KEY, shopCar);
		}
//		如果是05场景将订单列表放入map中返回
		else if(scern.equals("05")){
			result.put(DataConstant.ORDER_KEY, orderList);
		}
		else{
//			将产品列表加入到结果中
		result.put(DataConstant.LIST, proList);
		}
		return result;
	}


}
