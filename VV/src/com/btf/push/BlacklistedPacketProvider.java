package com.btf.push;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class BlacklistedPacketProvider implements IQProvider {
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		BlacklistedPacket blp = new BlacklistedPacket();
		boolean done = false;
		BlackRosterPacket.Item item = null;
		Log.i("kk", "BlacklistedPacket parse");
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("item")) {
					String jid = parser.getAttributeValue("", "jid");
					String action = parser.getAttributeValue("", "action");
					item = new BlackRosterPacket.Item(jid, null, action);
					String note = parser.getAttributeValue("", "note");
					String role = parser.getAttributeValue("", "role");
					String bday = parser.getAttributeValue("", "bday");
					String nickname = parser.getAttributeValue("", "nickname");
					String photo = parser.getAttributeValue("", "photo");
					item.setNote(note);
					item.setRole(role);
					item.setBday(bday);
					item.setNickname(nickname);
					item.setPhoto(photo);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("item")) {
					blp.addRosterItem(item);
				}
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return blp;
	}
}
