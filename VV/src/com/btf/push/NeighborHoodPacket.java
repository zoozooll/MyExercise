package com.btf.push;

import com.btf.push.base.BaseIQ;

;
/**
 * @func 附近的人的Packet
 * @author yuedong bao
 * @time 2015-1-12 上午11:01:35
 */
public class NeighborHoodPacket extends BaseIQ {
	public static final String element = "query";
	public static final String xmlns = "neighborhood";

	public enum NeighborHoodType {
		all("2"), male("1"), female("0");
		NeighborHoodType(String val) {
			this.val = val;
		}
		public static NeighborHoodType getTypeByValue(String val) {
			NeighborHoodType type = null;
			for (NeighborHoodType typeOne : NeighborHoodType.values()) {
				if (typeOne.val.equals(val)) {
					type = typeOne;
					break;
				}
			}
			return type;
		}

		public String val;
	}

	@Override
	public String getChildElementXML() {
		return toXml(new StringBuilder(), element, xmlns);
	}
}
