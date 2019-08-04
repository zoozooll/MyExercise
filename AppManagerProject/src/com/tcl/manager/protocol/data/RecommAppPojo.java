package com.tcl.manager.protocol.data;

import java.util.ArrayList;

/**
 * @Description:
 * @author pengcheng.zhang
 * @date 2014-12-22 下午4:16:20
 * @copyright TCL-MIE
 */
public class RecommAppPojo
{
	public String status;
	public Data data;
	
	public RecommAppPojo()
	{
		;
	}

	public static class Data
	{
		public ArrayList<BaseAppInfo> apps;
		/** 下次获取 APP 分页数据的第一条数据的index */
		public int ntof;
	}
}
