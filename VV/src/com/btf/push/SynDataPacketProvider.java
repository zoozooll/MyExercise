package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class SynDataPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		return new SynDataPacket().toBaseIQ(parser, SynDataPacket.element,
				SynDataPacket.xmlns);
	}
}
