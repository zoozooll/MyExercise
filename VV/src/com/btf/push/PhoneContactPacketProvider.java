package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class PhoneContactPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		PhoneContactPacket packet = new PhoneContactPacket();
		return packet.toBaseIQ(parser, PhoneContactPacket.element,
				PhoneContactPacket.xmlns);
	}
}
