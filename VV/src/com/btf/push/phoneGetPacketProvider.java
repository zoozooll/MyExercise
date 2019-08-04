package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class phoneGetPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		PhoneGetPacket phoneGetPacket = new PhoneGetPacket();
		return phoneGetPacket.toBaseIQ(parser, PhoneGetPacket.element,
				PhoneGetPacket.xmlns);
	}
}
