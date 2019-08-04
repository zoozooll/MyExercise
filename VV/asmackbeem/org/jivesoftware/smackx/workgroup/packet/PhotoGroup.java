/**
 * 
 */
package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author hongbo ke
 */
public class PhotoGroup implements PacketExtension {
	public static final String ELEMENT_NAME = "photogroup";
	private String gid;
	private String jid;
	private String gidCreateTime;

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.packet.PacketExtension#getElementName()
	 */
	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}
	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.packet.PacketExtension#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return "";
	}
	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.packet.PacketExtension#toXML()
	 */
	/*
	 * 	<photogroup>
			<gid>6</gid>
			<jid>951227@vv</jid>
			<gid_create_time>2016-03-18 09:35:47</gid_create_time>
		</photogroup>

	 * */
	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(ELEMENT_NAME).append(">").append("<gid>")
				.append(gid).append("</gid>").append("<jid>").append(jid)
				.append("</jid>").append("<gid_create_time>")
				.append(gidCreateTime).append("</gid_create_time>").append("<")
				.append(ELEMENT_NAME).append("/>");
		return sb.toString();
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getGidCreateTime() {
		return gidCreateTime;
	}
	public void setGidCreateTime(String gidCreateTime) {
		this.gidCreateTime = gidCreateTime;
	}
	
	public static class Provider implements PacketExtensionProvider {

		@Override
		public PacketExtension parseExtension(XmlPullParser parser)
				throws Exception {
			final PhotoGroup invitation = new PhotoGroup();
			boolean done = false;
			while (!done) {
				parser.next();
				String elementName = parser.getName();
				if (parser.getEventType() == XmlPullParser.START_TAG) {
					if ("gid".equals(elementName)) {
						invitation.gid = parser.getAttributeValue("",
								"gid");
					} else if ("jid".equals(elementName)) {
						invitation.jid = parser.nextText();
					} else if ("gid_create_time".equals(elementName)) {
						invitation.gidCreateTime = parser.nextText();
					} 
				} else if (parser.getEventType() == XmlPullParser.END_TAG
						&& ELEMENT_NAME.equals(elementName)) {
					done = true;
				}
			}
			return invitation;
		}
		
	}
}
