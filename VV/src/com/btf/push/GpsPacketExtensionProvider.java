package com.btf.push;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class GpsPacketExtensionProvider implements PacketExtensionProvider {
	@Override
	public PacketExtension parseExtension(XmlPullParser xp) throws Exception {
		// TODO Auto-generated method stub
		GpsPacketExtension result = new GpsPacketExtension();
		while (true) {
			int n = xp.next();
			if (n == XmlPullParser.START_TAG) {
				if ("content".equals(xp.getName())) {
					result.setContent(xp.nextText());// Note!
				} else if ("longitude".equals(xp.getName())) {
					result.setLongitude(xp.nextText());
				} else if ("latitude".equals(xp.getName())) {
					result.setLatitude(xp.nextText());
				}
			} else if (n == XmlPullParser.END_TAG) {
				if (GpsPacketExtension.ELEMENT.equals(xp.getName())) {
					break;
				}
			}
		}
		return result;
	}
}
