package com.btf.push;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;

public class ImageNotificationExtensionPacketListener implements PacketListener {
	@Override
	public void processPacket(Packet arg0) {
		// TODO Auto-generated method stub
		//		Log.i("SMACK", "GpsPacketExtensionPacketListener --" + arg0.toXML());
		PacketExtension packetExtension = arg0
				.getExtension(ImageNotificationExtension.NAMESPACE);
		if (packetExtension instanceof ImageNotificationExtension) {
			//			ImageNotificationExtension gpsPacketExtension = (ImageNotificationExtension) packetExtension;
			//			Log.i("SMACK", "GpsPacketExtension --" + gpsPacketExtension.getImgcount() + " ");
			//			Log.i("SMACK", "GpsPacketExtension --" + gpsPacketExtension.toXML());
		}
	}
}
