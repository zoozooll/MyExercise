package com.btf.push;

import com.btf.push.base.BaseIQ;

public class SchoolMatePacket extends BaseIQ {
	public static final String element = "query";
	public static final String xmlns = "school:fellow";

	@Override
	public String getChildElementXML() {
		return toXml(new StringBuilder(), element, xmlns);
	}
}