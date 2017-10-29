package com.iskinfor.servicedata.datahelp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.iskinfor.servicedata.datahelp.SysHeader.Ret;
import com.iskinfor.servicedata.datahelp.SysHeader.STATUS;



public class CdUtil {
	public static final String SERVICE_CODE = "SERVICE_CODE";	// 服务代码
	public static final String SERVICE_SCENE = "SERVICE_SCENE";	// 服务场景
	public static final String SYS_HEAD = "SYS_HEAD";	// 报文头
	public static final String BODY = "BODY";	// 报文主体
	public static final String CONSUMER_ID = "CONSUMER_ID"; // 渠道代码
	public static final String SEQ_NO = "SEQ_NO";	// 流水号
	private static Date lastTime = new Date(); // 线程同步
	public static final String RET_STATUS = "RET_STATUS";
	public static final String RET = "RET";
	public static final String RET_MSG="RET_MSG";
	public static final String RET_CODE = "RET_CODE";
	private static final SimpleDateFormat seqFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");	
	
	public static CompositeData sysHeaderInstance(String code, String scene){
		CompositeData sysData = new CompositeData();
		//服务代码
		Field fieldCode = new Field(new FieldAttr(FieldType.FIELD_STRING, 11, 0));
		fieldCode.setValue(code);
		sysData.addField(SERVICE_CODE, fieldCode);
		
		//服务场景
		Field fieldScene = new Field(new FieldAttr(FieldType.FIELD_STRING, 2, 0));
		fieldScene.setValue(scene);
		sysData.addField(SERVICE_SCENE, fieldScene);
		
		//请求发送方流水号
		Field fieldSeq = new Field(new FieldAttr(FieldType.FIELD_STRING,17, 0));
		fieldSeq.setValue(getSeqNum());
		sysData.addField(SEQ_NO, fieldSeq);
        
		 
        //渠道代码 
        Field fieldConsumerId = new Field(new FieldAttr(FieldType.FIELD_STRING, 4, 0)); 
        fieldConsumerId.setValue("0101"); 
        sysData.addField(CONSUMER_ID, fieldConsumerId); 
		
		return sysData;
	}
	
	/** 获取流水号 */
	private static String getSeqNum(){
		String strNow =null;
		synchronized (lastTime) {
			String strLast = seqFormat.format(lastTime);
			Date now = new Date();
			strNow = seqFormat.format(now);
			while(strLast.equals(strNow)){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				now = new Date();
				strNow = seqFormat.format(now);
			}
			lastTime = now;
		}
		return strNow;
	}
	/**
	 * 生成请求Body实例
	 * @param args
	 * @author WuNan2
	 * @return
	 */
	public static CompositeData bodyInstance(Map<String, Object> args){
		CompositeData body = new CompositeData();
		for(String key : args.keySet()){
			Object obj = args.get(key);
			if(obj instanceof Map){
				Map<String, Object> map = (Map<String, Object>) obj;
				CompositeData data = bodyInstance(map);
				body.addStruct(key, data);
			}else if(obj instanceof List){
				List<Map<String, Object>> ls = (List<Map<String, Object>>) obj;
				Array array = new Array();
				for(Map<String, Object> map: ls){
					array.addStruct(bodyInstance(map));
				}
				body.addArray(key, array);
			}
			else if (obj instanceof Integer) {
				Field field = new Field(new FieldAttr(FieldType.FIELD_INT, 20, 0));
				field.setValue((Integer) obj);
				body.addField(key, field);
			}else if(obj instanceof Double){
				Field field = new Field(new FieldAttr(FieldType.FIELD_DOUBLE, 20, 0));
				field.setValue((Double) obj);
				body.addField(key, field);
			}else{
				Field field= new Field(new FieldAttr(FieldType.FIELD_STRING, null != obj ? obj.toString().length() : 20, 0));
				field.setValue(null != obj ? obj.toString() : null);
				body.addField(key, field);
			}
		}
		return body;
	}
	
	/**
	 * 校验Header
	 * @param data
	 * @return
	 */
	public static SysHeader verifyHeader(CompositeData data){
		
		if(null == data){
			return null;
		}
		
		SysHeader sys = new SysHeader();
		
		CompositeData header = data.getStruct(SYS_HEAD);
		Field status = header.getField(RET_STATUS);
		Field scene = header.getField(SERVICE_SCENE);
		Field code = header.getField(SERVICE_CODE);
		Array array = header.getArray("BIZRESULT_ARRAY");
		if(array.size()==0){
			array = header.getArray("ret");
		}
		if(array.size()==0){
			array = header.getArray(RET);
		}
		
		List<Ret> lsr = null;
		if(null != array){
			List<CompositeData> ret = CdUtil.array2List(array);
			lsr = new ArrayList<Ret>();
			for(CompositeData com : ret){
				Map<String, Object> map = CdUtil.data2Map(com, new String[]{RET_MSG, RET_CODE});
				Ret e = sys.new Ret();
				e.setCode((String)map.get(RET_CODE));
				e.setMsg((String)map.get(RET_MSG));
				lsr.add(e);
			}
		}
		sys.setServiceCode(null != code ? code.strValue() : null);
		sys.setServiceScene(null != scene ? scene.strValue() : null);
		sys.setStatu(null != status && "S".equals(status.strValue()) ? STATUS.SUCCESS : STATUS.FAIL);
		sys.setRet(lsr);
		
		return sys;
	}
	/**
	 * 将CompositeData对象转成Map
	 * @param data
	 * @param itmes
	 * @return
	 */
	public static Map<String,Object> data2Map(CompositeData data, String[] items){
		Map<String,Object> map = null;
		if(null != data && null != items){
			map = new HashMap<String,Object>();
			for (String key : items) {
				Field field = data.getField(key);
				map.put(key, null != field ? field.getValue() : null);
			}
		}
		return map;
	}
	/**
	 * Array转成List
	 * @param array
	 * @return
	 */
	public static List<CompositeData> array2List(Array array){
		List<CompositeData> ls =null;
		if(null != array){
			ls = new ArrayList<CompositeData>();
			for(int i=0; i<array.size(); i++){
				ls.add(array.getStruct(i));
			}
		}
		return ls;
	}
	/**
	 * 生成请求报文
	 * @param header
	 * @param body
	 * @return
	 */
	public static CompositeData reqData(CompositeData header, CompositeData body){
		CompositeData reqData = new CompositeData();
		
		reqData.addStruct(SYS_HEAD, header);
		reqData.addStruct(BODY, body);
		reqData.addStruct("APP_HEAD", new CompositeData());
		
		return reqData;
	}
	
	/**
	 * 获取Body
	 * @param rspData
	 * @return
	 */
	public static CompositeData getBody(CompositeData rspData){
		CompositeData body = rspData.getStruct(BODY);
		return body;
	}
	
	/**
	 * 获取数组
	 * @param body
	 * @param key
	 * @return
	 */
	public static Array  getArray(CompositeData body, String key){
		Array array = null;
		if(body != null && null != key){
			array = body.getArray(key);
		}
		return array;
	}
	/**
	 * 获取数组
	 * @param body
	 * @param key
	 * @return
	 */
	public static Map<String,Array>  getArray(CompositeData body, String[] items){
		Map<String,Array> array = null;
		if(body != null && null != items && items.length>0){
			array = new HashMap<String,Array>();
			for(String item : items){
				Array arr = body.getArray(item);
				array.put(item, arr);
			}
		}
		return array;
	}
}
