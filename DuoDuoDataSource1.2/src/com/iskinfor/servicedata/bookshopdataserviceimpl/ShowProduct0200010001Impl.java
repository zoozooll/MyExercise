package com.iskinfor.servicedata.bookshopdataserviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IShowProduct0200010001;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.pojo.Product;

public class ShowProduct0200010001Impl implements IShowProduct0200010001{

	/**
	 * 获取书店的所有的商品信息
	 */
	public Map<String, Object> getAllProduct(int pag) throws TimeoutException,Exception {
		return getShops("01",pag, "ALL_ARRAY");
	}

	/**
	 * 获取书店中所有的书籍
	 */
	public Map<String, Object> getAllBook(int pag) throws TimeoutException,Exception {
		return getShops("03",pag, "BOOK_SHOW");
	}
	/**
	 * 得到所有的课件
	 */
	@Override
	public Map<String, Object> getAllCourseware(int pag)
			throws TimeoutException, Exception {
		return getShops("02",pag, "COURSEWARE_SHOW");
	}

	
	/**
	 * 获取书店中所有的习题
	 */
	public Map<String, Object> getAllExercise(int pag) throws TimeoutException, Exception {
		return getShops("04",pag, "EXERCISE_SHOW");
	}
	/**
	 * 获取所有的考卷
	 */
	public Map<String, Object> getAllExam(int pag)  throws TimeoutException, Exception {
		return getShops("05",pag, "EXAM_SHOW");
	}
	/**
	 * 
	 * @param scen
	 * @param rspTag
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getShops(String scen,int page,String rspTag) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();

		ArrayList<Product> proList = new ArrayList<Product>();
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0200010001", scen);
		// 参数
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("SHOW_LINE_NUM", "12");
		if(page>0){
		args.put("PAGE_NUM", page);
		}
		// 根据Map生成Body
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
			Array uArray = cbody.getArray(rspTag);

			Field totalNum = cbody.getField("TOTAL_NUM");
		

			// 将总记录数加到返回结果中
			result.put(DataConstant.TOTAL_NUM, totalNum.getValue().toString());
			System.out.println(totalNum.getValue().toString() + "=======================>");

			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(i);
				Product productDto = new Product();
				productDto.setProName((String) cData.getField("PRO_NAME")
						.getValue());
				productDto.setBigImgPath((String) cData
						.getField("BIG_IMG_PATH").getValue());
				productDto.setSmallImgPath((String) cData.getField(
						"SMALL_IMG_PATH").getValue());
				productDto.setProPrice((String) cData.getField("PRO_PRICE")
						.getValue());
				productDto.setProVolume((String) cData.getField("PRO_VOLUME")
						.getValue());
				productDto.setProId((String) cData.getField("PRO_ID")
						.getValue());
				proList.add(productDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将列表压入到返回结果中
		result.put(DataConstant.LIST, proList);
		return result;
	}

	

}
