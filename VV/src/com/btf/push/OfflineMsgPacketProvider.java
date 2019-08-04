package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.btf.push.base.BaseIQ;

/**
 * @ClassName: OfflineMsgPacketProvider
 * @Description: 解析OfflineMsgPacket
 * @author: yuedong bao
 * @date: 2015-3-17 上午11:49:34
 */
public class OfflineMsgPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		BaseIQ retVal = new OfflineMsgPacket();
		retVal.toBaseIQ(parser, OfflineMsgPacket.element,
				OfflineMsgPacket.xmlns);
		return retVal;
	}
}
