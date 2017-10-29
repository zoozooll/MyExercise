package com.iskinfor.servicedata.bookshopdataserviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IFilePathTranslate0200040001;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.pojo.Product;

public class FilePathTranslate0200040001Impl implements
		IFilePathTranslate0200040001 {

	@Override
	public Map<String, String> getProPath(String proId)
			throws TimeoutException, Exception {
		Map<String, String> result = new HashMap<String, String>();

		ArrayList<Product> proList = new ArrayList<Product>();
		// SysHeader
		CompositeData header = CdUtil.sysHeaderInstance("0200040001", "01");
		// 参数
		Map<String, Object> args = new HashMap<String, Object>();
		List<Map<String, Object>> ids = new ArrayList<Map<String, Object>>();
		Map<String, Object> proArrayArgs = new HashMap<String, Object>();
		proArrayArgs.put("PRO_ID", proId);
		ids.add(proArrayArgs);
		args.put("PRO_ARRAY", ids);
		// 根据Map生成Body
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);

		try {
			CompositeData rspData = null;
			try {
				rspData = ESBClient.request02(reqData);
				System.out.println("rspData==>" + rspData);
			} catch (Exception e) {
				System.out.println("SERVICE_CODE=0200010001,SERVICE_SCENE="
						+ "01" + e.getMessage());
				throw e;
			}
			CompositeData cbody = CdUtil.getBody(rspData);
			                               
			Array uArray = cbody.getArray("RESOURSE_ARRAY");
//			for (int i = 0; i < uArray.size(); i++) {
				CompositeData cData = uArray.getStruct(0);
				try{
				String proviewpath = (String) cData.getField("PRO_VIEW_PATH")
						.getValue();
				if(null!=proviewpath&&!"".equals(proviewpath)){
					result.put(DataConstant.PRO_VIEW_PATH_KEY, proviewpath);
				}}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
				String proRealPath=(String) cData.getField("PRO_REAL_PATH").getValue();
				if(null!=proRealPath&&!"".equals(proRealPath)){
					result.put(DataConstant.PRO_REAL_PATH_KEY, proRealPath);
				}}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
				String fileType=(String) cData.getField("FILE_TYPE").getValue();
				if(null!=fileType&&!"".equals(fileType)){
					result.put(DataConstant.FILE_TYPE_KEY, fileType);
				}}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
				String fileBit=(String) cData.getField("FILE_BIT").getValue();
				if(null!=fileBit&&!"".equals(fileBit)){
					result.put(DataConstant.FILE_BIT_KEY, fileBit);
				}}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
				String fileSite= (String) cData.getField("FILE_SITE").getValue();
				if(null!=fileSite&&!"".equals(fileSite)){
					result.put(DataConstant.File_SITE_KEY, fileSite);
				}}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
				String fileTime=(String) cData.getField("FILE_TIME").getValue();
				if(null!=fileTime&&!"".equals(fileTime)){
					result.put(DataConstant.File_TIME_KEY, fileTime);
				}}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
				String fileHshKey=(String) cData.getField("FILE_HASH").getValue();
				if(null!=fileHshKey&&!"".equals(fileHshKey)){
					result.put(DataConstant.File_HASH_KEY, fileHshKey);
				}}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
				String serverId=(String) cData.getField("SERVER_ID").getValue();
				if(null!=serverId&&!"".equals(serverId)){
					result.put(DataConstant.SERVER_ID_KEY, serverId);
				}}catch(Exception e){
					e.printStackTrace();
				}
			
				
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
