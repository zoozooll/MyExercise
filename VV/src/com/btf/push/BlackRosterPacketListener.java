package com.btf.push;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import android.os.Handler;

public class BlackRosterPacketListener implements PacketListener {
	Handler handler = null;

	public BlackRosterPacketListener(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}
	@Override
	public void processPacket(Packet arg0) {
		// TODO Auto-generated method stub
		System.out.println("--*blackListener" + arg0.toXML());
		if (arg0 instanceof BlackRosterPacket) {
			System.out.println("--jjjj*blackListener" + arg0.toXML());
		}
	}
}
