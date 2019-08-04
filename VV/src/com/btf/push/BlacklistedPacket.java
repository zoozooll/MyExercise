package com.btf.push;

/**
 * @func 他人拉黑自己的Packet
 * @author yuedong bao
 * @time 2014-11-26 下午3:25:02
 */
public class BlacklistedPacket extends BlackRosterPacket {
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"blacklisted\" ");
		if (doaction != null) {
			buf.append(" action=\"").append(doaction).append("\"");
		}
		buf.append(">");
		synchronized (rosterItems) {
			for (Item entry : rosterItems) {
				buf.append(entry.toXML());
			}
		}
		buf.append("</query>");
		return buf.toString();
	}
}
