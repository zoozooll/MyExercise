package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.btf.push.base.BaseIQ;

/**
 * @func 对方好友请求处理类
 * @author yuedong bao
 * @time 2014-11-28 上午11:40:58
 */
public class AddRosterPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		AddRosterPacket rstValue = new AddRosterPacket();
		BaseIQ retIQ = rstValue.toBaseIQ(parser, AddRosterPacket.element,
				AddRosterPacket.xmlns);
		return retIQ;
	}
}
