package com.tcl.manager.protocol.data;

import java.util.ArrayList;

/**
 * @Description:白名单列表
 * 
 * @author pengcheng.zhang
 * @date 2014-12-23 下午7:30:20
 * @copyright TCL-MIE
 */
public class WhiteListPojo
{
	public String code;
	public Data data;
	
	public WhiteListPojo()
	{
		;
	}

	public static class Data
	{
		public ArrayList<WhiteInfo> infos;
	}

	public static class WhiteInfo
	{
		public int id;
		public String pkn;
		public String createTime;
		public String modifyTime;
	}
}
