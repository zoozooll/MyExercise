package com.btf.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GPSPacketListener implements PacketListener {
	Handler handler = null;

	public GPSPacketListener(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}
	@Override
	public void processPacket(Packet arg0) {
		// TODO Auto-generated method stub
		System.out.println("--*" + arg0.toXML());
		if (arg0 instanceof GPSPacket) {
			GPSPacket gpsPacket = (GPSPacket) arg0;
			// System.err.println(""+gpsPacket.getRosterItemCount());
			Collection<com.btf.push.Item> items = gpsPacket.getRosterItems();
			ArrayList<com.beem.project.btf.service.Contact> contacts = new ArrayList<com.beem.project.btf.service.Contact>();
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				com.btf.push.Item item = (com.btf.push.Item) iterator.next();
				com.beem.project.btf.service.Contact contact = new com.beem.project.btf.service.Contact(
						item.getJid());
				contact.setDistance(item.getDistance());
				contacts.add(contact);
				// Debug.getDebugInstance().log("llll "+item.getUser());
			}
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putParcelableArrayList("gpsitems", contacts);
			// data.putParcelableArrayList("gpsitems", arrayList);
			// data.putParcelableArrayList("gpsitems", (ArrayList<? extends
			// Parcelable>) items);
			msg.setData(data);
			// msg.what = gpsPacket.getRosterItemCount();
			msg.what = 1;
			handler.sendMessage(msg);
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				com.btf.push.Item item = (com.btf.push.Item) iterator.next();
				// System.out.println(item.getUser()+item.getDistance());
			}
		}
	}
}
