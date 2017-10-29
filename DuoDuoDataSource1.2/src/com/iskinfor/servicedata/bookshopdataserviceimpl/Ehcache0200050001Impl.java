package com.iskinfor.servicedata.bookshopdataserviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.iskinfor.servicedata.bookshopdataservice.IEhcache0200050001;
import com.iskinfor.servicedata.datahelp.CdUtil;
import com.iskinfor.servicedata.datahelp.SysHeader;

/**
 * 动态提示服务类
 * @author minge
 *
 */
public class Ehcache0200050001Impl implements IEhcache0200050001 {
	
	public List<String> getKeyWord(String keyWord) throws Exception {
		
		List<String> keyList = new ArrayList<String>();
		Map<String,Object> args = new HashMap<String,Object>();
		args.put("KEY_WORD", keyWord);
		args.put("SHOW_LINE_NUM", 5);
		CompositeData header = CdUtil.sysHeaderInstance("0200050001", "01");
		
		CompositeData body = CdUtil.bodyInstance(args);
		// 封装请求报文
		CompositeData reqData = CdUtil.reqData(header, body);
		CompositeData rspData = null;
		try {
			rspData = ESBClient.request02(reqData);
			//验证返回报文
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if(SysHeader.STATUS.SUCCESS == sysHeader.getStatu() && "000000".equals(sysHeader.getRet().get(0).getCode())){
				Array rspArray = rspData.getStruct("BODY").getArray("KEYWORD_ARRAY");
				if(rspArray != null){
					for (int i = 0; i < rspArray.size(); i++) {
						CompositeData compositeData = rspArray.getStruct(i);
						keyList.add(compositeData.getField("KEY_WORD") != null ? compositeData.getField("KEY_WORD").getValue().toString() :"");
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return keyList;
	}

	public boolean putKeyWord(String keyWord) throws Exception {
		Map<String,Object> args = new HashMap<String,Object>();
		
		args.put("KEY_WORD", keyWord);
		args.put("SHOW_LINE_NUM", 5);
	
		CompositeData header = CdUtil.sysHeaderInstance("0200050001", "02");
		CompositeData body = CdUtil.bodyInstance(args);
		CompositeData reqData = CdUtil.reqData(header, body); 
	
		try {
			CompositeData rspData = ESBClient.request02(reqData);
			SysHeader sysHeader = CdUtil.verifyHeader(rspData);
			if("000000".equals(sysHeader.getRet().get(0).getCode())){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
