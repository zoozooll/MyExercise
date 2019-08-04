package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * @func 附近的人包解析类
 * @author yuedong bao
 * @time 2015-1-13 上午10:36:33
 */
public class NeighborHoodPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		NeighborHoodPacket retVal = new NeighborHoodPacket();
		retVal.toBaseIQ(parser, NeighborHoodPacket.element,
				NeighborHoodPacket.xmlns);
		return retVal;
	}
}
