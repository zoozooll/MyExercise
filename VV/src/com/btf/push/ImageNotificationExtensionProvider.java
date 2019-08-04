package com.btf.push;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class ImageNotificationExtensionProvider implements
		PacketExtensionProvider {
	@Override
	public PacketExtension parseExtension(XmlPullParser xp) throws Exception {
		// TODO Auto-generated method stub
		ImageNotificationExtension result = new ImageNotificationExtension();
		while (true) {
			int n = xp.next();
			if (n == XmlPullParser.START_TAG) {
				if ("imgcount".equals(xp.getName())) {
					result.setImgcount(xp.nextText());
				}
			} else if (n == XmlPullParser.END_TAG) {
				if (ImageNotificationExtension.ELEMENT.equals(xp.getName())) {
					break;
				}
			}
		}
		return result;
	}
}
