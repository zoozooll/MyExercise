package org.jivesoftware.smack.readerwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import com.beem.project.btf.utils.BBSUtils;

public class VVOutputstream extends OutputStream {
	private final OutputStream out;

	public VVOutputstream(OutputStream out) {
		super();
		this.out = out;
	}
	@Override
	public void write(int b) throws IOException {
	}
	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
	}
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		byte[] bytes = BBSUtils.byteArrayCat(VVXmppStreamHandler.startBytes,
				BBSUtils.intToByteArray(len),
				Arrays.copyOfRange(b, off, off + len),
				VVXmppStreamHandler.endBytes);
		out.write(bytes);
	}
}
