package com.iskyinfor.duoduo.downloadManage.mime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.res.XmlResourceParser;

public class MimeTypeParser {

	public static final String TAG_MIMETYPES = "MimeTypes";
	public static final String TAG_TYPE = "type";

	public static final String ATTR_EXTENSION = "extension";
	public static final String ATTR_MIMETYPE = "mimetype";

	private XmlPullParser parser;
	private MimeTypes mimeTypes;

	public MimeTypeParser() {
	}

	public MimeTypes fromXml(InputStream in) throws XmlPullParserException,
			IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

		parser = factory.newPullParser();
		parser.setInput(new InputStreamReader(in));

		return parse();
	}

	public MimeTypes fromXmlResource(XmlResourceParser in)
			throws XmlPullParserException, IOException {
		parser = in;
		return parse();
	}

	public MimeTypes parse() throws XmlPullParserException, IOException {

		mimeTypes = new MimeTypes();

		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tag = parser.getName();

			if (eventType == XmlPullParser.START_TAG) {
				if (tag.equals(TAG_MIMETYPES)) {

				} else if (tag.equals(TAG_TYPE)) {
					addMimeTypeStart();
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (tag.equals(TAG_MIMETYPES)) {

				}
			}

			eventType = parser.next();
		}

		return mimeTypes;
	}

	private void addMimeTypeStart() {
		String extension = parser.getAttributeValue(null, ATTR_EXTENSION);
		String mimetype = parser.getAttributeValue(null, ATTR_MIMETYPE);

		mimeTypes.put(extension, mimetype);
	}

}
