package com.btf.push;

import org.jivesoftware.smack.packet.PacketExtension;

public class GpsPacketExtension implements PacketExtension {
	public static final String ELEMENT = "gps";
	public static final String NAMESPACE = "jabber:client:gps";
	private String content;
	private String longitude;
	private String latitude;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
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
				"\"", ">", "<content>", getContent(), "</content>",
				"<longitude>", getLongitude(), "</longitude>", "<latitude>",
				getLatitude(), "</latitude>", "</", ELEMENT, ">");
		// <MyPackExtension
		// xmlns="MyPackExtension.com"><content>hahahahahahaha!...</content></MyPackExtension>
		return sb;
	}
	public static String createContinueString(String... strings) {
		StringBuffer sb = new StringBuffer();
		for (String string : strings) {
			sb.append(string);
		}
		return sb.toString();
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
