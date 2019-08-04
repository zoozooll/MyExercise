package com.btf.push;

import org.jivesoftware.smack.packet.PacketExtension;

public class ImageNotificationExtension implements PacketExtension {
	public static final String ELEMENT = "imgnotification";
	public static final String NAMESPACE = "jabber:client:imgnotification";
	private String imgcount;

	@Override
	public String getElementName() {
		// TODO Auto-generated method stub
		return ELEMENT;
	}
	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		String sb = createContinueString("<", ELEMENT, " xmlns=\"", NAMESPACE,
				"\"", ">", "<imgcount>", getImgcount(), "</imgcount>", "</",
				ELEMENT, ">");
		return sb;
	}
	public static String createContinueString(String... strings) {
		StringBuffer sb = new StringBuffer();
		for (String string : strings) {
			sb.append(string);
		}
		return sb.toString();
	}
	public String getImgcount() {
		return imgcount;
	}
	public void setImgcount(String imgcount) {
		this.imgcount = imgcount;
	}
}
