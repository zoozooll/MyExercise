package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class SchoolMatePacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		SchoolMatePacket schoolPacket = new SchoolMatePacket();
		schoolPacket.toBaseIQ(parser, SchoolMatePacket.element,
				SchoolMatePacket.xmlns);
		return schoolPacket;
	}
}
