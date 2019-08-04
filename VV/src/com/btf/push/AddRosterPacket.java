package com.btf.push;

import com.btf.push.base.BaseIQ;

/**
 * @func 添加好友Packet
 * @author yuedong bao
 * @time 2014-11-27 下午7:40:51
 */
public class AddRosterPacket extends BaseIQ {
	static final public String element = "query";
	static final public String xmlns = "friendreq";

	public enum Operation {
		// 请求- 同意- 拒绝
		require, agree, refuse;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<" + element + " xmlns=\"" + xmlns + "\" ");
		buf.append(">");
		buf.append("<item");
		for (String name : fields.keySet()) {
			Object value = fields.get(name);
			buf.append(" ").append(name.toString()).append("=")
					.append("\"" + value + "\"");
		}
		buf.append("/>");
		buf.append("</" + element + ">");
		return buf.toString();
	}
}
