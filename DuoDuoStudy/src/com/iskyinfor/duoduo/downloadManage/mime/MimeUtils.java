package com.iskyinfor.duoduo.downloadManage.mime;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.iskyinfor.duoduo.R;

public class MimeUtils {

	public static MimeTypes getMimeTypes(Context context) {
		MimeTypes mimeTypes;

		MimeTypeParser mtp = new MimeTypeParser();
		XmlResourceParser in = context.getResources().getXml(R.xml.mimetypes);
		try {
			mimeTypes = mtp.fromXmlResource(in);
		} catch (XmlPullParserException e) {
			throw new RuntimeException(
					"PreselectedChannelsActivity: XmlPullParserException");
		} catch (IOException e) {
			throw new RuntimeException(
					"PreselectedChannelsActivity: IOException");
		}

		return mimeTypes;
	}

	public static String getExtension(String fileName) {
		if (fileName == null) {
			return null;
		}

		int dot = fileName.lastIndexOf(".");
		if (dot >= 0) {
			return fileName.substring(dot);
		} else {
			return "";
		}
	} 
	


}
