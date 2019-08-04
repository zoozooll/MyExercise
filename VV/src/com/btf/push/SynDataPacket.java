package com.btf.push;

import com.btf.push.base.BaseIQ;

/**
 * @func 同步服务器Packet
 * @author yuedong bao
 * @time 2015-1-23 上午10:23:10
 */
public class SynDataPacket extends BaseIQ {
	public static String xmlns = "syndata";
	public static String element = "query";

	@Override
	public String getChildElementXML() {
		return toXml(new StringBuilder(), element, xmlns);
	}
}
