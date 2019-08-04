package com.btf.push;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;

public class GpsPacketExtensionPacketListener implements PacketListener {
	@Override
	public void processPacket(Packet arg0) {
		// TODO Auto-generated method stub
		//		Log.i("SMACK", "GpsPacketExtensionPacketListener --" + arg0.toXML());
		PacketExtension packetExtension = arg0
				.getExtension(GpsPacketExtension.NAMESPACE);
		if (packetExtension instanceof GpsPacketExtension) {
			/*GpsPacketExtension gpsPacketExtension = (GpsPacketExtension) packetExtension;
			Log.i("SMACK",
					"GpsPacketExtension --" + gpsPacketExtension.getLatitude() + " "
							+ gpsPacketExtension.getLongitude());
			Log.i("SMACK", "GpsPacketExtension --" + gpsPacketExtension.toXML());*/
		}
	}
}
