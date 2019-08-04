package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.butterfly.vv.vv.utils.Debug;

public class SchoolPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		// TODO Auto-generated method stub
		return parseRoster(parser);
	}
	private static SchoolVCard parseRoster(XmlPullParser parser)
			throws Exception {
		SchoolVCard gpsPacket = new SchoolVCard();
		boolean done = false;
		SchoolVCard.Item item = null;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("item")) {
					if (parser.getAttributeValue("", "error") != null) {
						item = new SchoolVCard.Item("", "");
						item.setError(parser.getAttributeValue("", "error"));
					} else {
						String jid = parser.getAttributeValue("", "jid");
						String role = parser.getAttributeValue("", "role");
						String bday = parser.getAttributeValue("", "bday");
						// String photo = parser.getAttributeValue("", "photo");
						// String name = parser.getAttributeValue("", "name");
						String note = parser.getAttributeValue("", "note");
						String nickname = parser.getAttributeValue("",
								"nickname");
						String onlineTime = parser.getAttributeValue("",
								"onlineTime");
						item = new SchoolVCard.Item(jid, "");
						item.setRole(role);
						item.setBday(bday);
						item.setNickname(nickname);
						item.setNote(note);
						item.setOnlineTime(onlineTime);
						// item.setPhoto(StringUtils.decodeBase64(photo));
						// String distance = parser.getAttributeValue("",
						// "distance");
						// item.setDistance(distance);
					}
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("item")) {
					gpsPacket.addRosterItem(item);
					Debug.getDebugInstance().log("222==" + gpsPacket.toXML());
				}
				if (parser.getName().equals("vCard")) {
					done = true;
				}
			}
		}
		Debug.getDebugInstance().log("==" + gpsPacket.toXML());
		return gpsPacket;
	}
}
