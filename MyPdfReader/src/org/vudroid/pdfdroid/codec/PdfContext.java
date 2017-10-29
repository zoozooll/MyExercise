package org.vudroid.pdfdroid.codec;

import android.content.ContentResolver;
import org.vudroid.core.codec.CodecContext;
import org.vudroid.core.codec.CodecDocument;

public class PdfContext implements CodecContext {
	
	private static boolean alreadyLoaded;

	static {
		if (!alreadyLoaded) {
			System.loadLibrary("vudroid");
			alreadyLoaded = true;
		}
	}

	public CodecDocument openDocument(String fileName) {
		return PdfDocument.openDocument(fileName, "");
	}

	public void setContentResolver(ContentResolver contentResolver) {
		// TODO
	}

	public void recycle() {
	}
}
