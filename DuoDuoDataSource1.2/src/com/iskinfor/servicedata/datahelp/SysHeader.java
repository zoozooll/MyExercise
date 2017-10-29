package com.iskinfor.servicedata.datahelp;

import java.util.List;

/**
 * 响应报文Header
 * 
 * @author WuNan2
 * @date Apr 15, 2011
 */
public class SysHeader {
	/** 状态 */
	public enum STATUS {
		SUCCESS, FAIL
	};
	/** 详细信息 */
	public class Ret{
		private String msg;
		private String code;
		
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
	}
	
	private STATUS statu;	// 状态
	private String serviceScene;	// 服务代码
	private String serviceCode;	// 场景代码
	private List<Ret> ret;	// 详细信息
	
	public STATUS getStatu() {
		return statu;
	}
	public void setStatu(STATUS statu) {
		this.statu = statu;
	}
	public String getServiceScene() {
		return serviceScene;
	}
	public void setServiceScene(String serviceScene) {
		this.serviceScene = serviceScene;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public List<Ret> getRet() {
		return ret;
	}
	public void setRet(List<Ret> ret) {
		this.ret = ret;
	}
	
}
