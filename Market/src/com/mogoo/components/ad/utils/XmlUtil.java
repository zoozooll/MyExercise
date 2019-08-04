package com.mogoo.components.ad.utils;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class XmlUtil
{
	private static class XmlParserInstanceHolder
	{
		public static final XmlPullParser xmlParser = Xml.newPullParser();
	}

	public static XmlPullParser getXmlPullParser()
	{
		return XmlParserInstanceHolder.xmlParser;
	}

}
