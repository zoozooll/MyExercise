package com.btf.push;

import com.btf.push.base.BaseIQ;

/**
 * @ClassName: IQAckPacket
 * @Description: IQ packet的回复包，表示客户端已经成功接受到消息
 * @author: yuedong bao
 * @date: 2015-3-17 上午11:27:06
 */
public class IQAckPacket extends BaseIQ {
	private String element;
	private String xmlns;

	public IQAckPacket(String element, String xmlns) {
		super();
		this.element = element;
		this.xmlns = xmlns;
		setType(Type.RESULT);
	}
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<" + element + " xmlns=\"" + xmlns + "\"");
		buf.append("/>");
		return buf.toString();
	}
}
