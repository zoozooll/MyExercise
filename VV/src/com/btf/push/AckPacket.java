package com.btf.push;

import org.jivesoftware.smack.packet.Packet;

public class AckPacket extends Packet {
	
	private String element;
	private String xmlns;
	
	public AckPacket(String packetId, String element, String xmlns) {
		super();
		setPacketID(packetId);
		this.element = element;
		this.xmlns = xmlns;
	}
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuilder buf = new StringBuilder();
		buf.append("<" + element + " xmlns=\"" + xmlns + "\" ");
		buf.append("id=\"" + getPacketID() + "\" ");
		buf.append("type=\"result\">");
		buf.append("</" + element +">");
		return buf.toString();
	}
}
