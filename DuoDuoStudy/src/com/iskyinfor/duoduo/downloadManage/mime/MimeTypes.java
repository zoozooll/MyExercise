package com.iskyinfor.duoduo.downloadManage.mime;

import java.util.HashMap;
import java.util.Map;

import android.webkit.MimeTypeMap;

public class MimeTypes {

	private Map<String, String> mimeTypes;

	public MimeTypes() {
		mimeTypes = new HashMap<String, String>();
	}

	public void put(String type, String extension) {
		extension = extension.toLowerCase();
		mimeTypes.put(type, extension);
	}

	public String getMimeType(String filename) {

		String extension = MimeUtils.getExtension(filename);
		if (extension.length() > 0) {
			String webkitMimeType = MimeTypeMap.getSingleton()
					.getMimeTypeFromExtension(extension.substring(1));

			if (webkitMimeType != null) {
				return webkitMimeType;
			}
		}

		extension = extension.toLowerCase();
		String mimetype = mimeTypes.get(extension);
		return mimetype;
	}

	public String getMimeTypeByExtension(String extension) {

		if (extension.length() > 0) {
			String webkitMimeType = MimeTypeMap.getSingleton()
					.getMimeTypeFromExtension(extension.substring(1));

			if (webkitMimeType != null) {
				return webkitMimeType;
			}
		}

		extension = extension.toLowerCase();
		String mimetype = mimeTypes.get(extension);
		return mimetype;
	}

}
