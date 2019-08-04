package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class BlackRosterPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		BlackRosterPacket packet = new BlackRosterPacket();
		boolean done = false;
		BlackRosterPacket.Item item = null;
		//LogUtils.i("BlackRosterPacket parseRoster");
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("item")) {
					String jid = parser.getAttributeValue("", "jid");
					String action = parser.getAttributeValue("", "action");
					item = new BlackRosterPacket.Item(jid, null, action);
					String note = parser.getAttributeValue("", "note");
					String role = parser.getAttributeValue("", "sex");
					String bday = parser.getAttributeValue("", "bday");
					String nickname = parser.getAttributeValue("", "nickname");
					String photo = parser.getAttributeValue("",
							"portrait_small");
					item.setNote(note);
					item.setRole(role);
					item.setBday(bday);
					item.setNickname(nickname);
					item.setPhoto(photo);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("item")) {
					packet.addRosterItem(item);
				}
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return packet;
	}
}
