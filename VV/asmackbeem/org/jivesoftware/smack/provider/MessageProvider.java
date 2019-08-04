package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.Message;
import org.xmlpull.v1.XmlPullParser;

public interface MessageProvider {
	
	public Message parseIQ(XmlPullParser parser) throws Exception;
}
