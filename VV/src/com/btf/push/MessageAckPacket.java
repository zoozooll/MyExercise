package com.btf.push;

import org.jivesoftware.smack.packet.Packet;

public class MessageAckPacket extends Packet {
	public MessageAckPacket(String packetId) {
		super();
		setPacketID(packetId);
	}
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuilder buf = new StringBuilder();
		buf.append("<message xmlns=\"jabber:client\" ");
		buf.append("id=\"" + getPacketID() + "\" ");
		buf.append("type=\"result\">");
		buf.append("</message>");
		return buf.toString();
	}
}
