package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author chaohui liu
 */
public class GPSPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		// TODO Auto-generated method stub
		return parseRoster(parser);
	}
	private static GPSPacket parseRoster(XmlPullParser parser) throws Exception {
		GPSPacket gpsPacket = new GPSPacket();
		boolean done = false;
		Item item = null;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("item")) {
					String jid = parser.getAttributeValue("", "jid");
					String name = parser.getAttributeValue("", "nickname");
					item = new Item(jid, name);
					item.setNickname(name);
					String distance = parser.getAttributeValue("", "distance");
					item.setDistance(distance);
					// String photo = parser.getAttributeValue("", "photo");
					// item.setPhoto(StringUtils.decodeBase64(photo));
					String onlineTime = parser.getAttributeValue("",
							"onlineTime");
					item.setLogintime(onlineTime);
					// nickname
					String bday = parser.getAttributeValue("", "bday");
					item.setBday(bday);
					String role = parser.getAttributeValue("", "role");
					item.setSex(role);
					String note = parser.getAttributeValue("", "note");
					item.setSignature(note);
					String photo = parser.getAttributeValue("", "photo");
					item.setBinval(photo);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("item")) {
					gpsPacket.addRosterItem(item);
				}
				if (parser.getName().equals("vCard")) {
					done = true;
				}
			}
		}
		return gpsPacket;
	}
}
