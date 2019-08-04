package org.jivesoftware.smack.readerwriter;

import java.io.IOException;
import java.io.InputStream;
import com.beem.project.btf.utils.BBSUtils;

public class VVInputstream extends InputStream {
	private InputStream in;
	private int totalLen = 0;
	private int hasReadLen = 0;

	public VVInputstream(InputStream in) {
		this.in = in;
	}
	@Override
	public int read() throws IOException {
		return 0;
	}
	@Override
	public int read(byte[] b) throws IOException {
		return in.read(b);
	}
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int avaliable = 0;
		int count = 0;
		// 每一段流读取完之后，in.available()就会等于0.
		// 用了以下代码当读到空流，或者读完一段流之后，会进入死循环。但不会丢失数据或者抛异常。
		// 此bug留日后解决。
		while (count == 0) {
			count = in.available();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (totalLen == hasReadLen) {
			byte[] prevTwoBytes = new byte[2];
			boolean isCatchStartOrEnd = false;
			byte[] ignoreBytes = new byte[len];
			int avaliable_ignore = 0;
			while (!isCatchStartOrEnd) {
				in.read(prevTwoBytes, 0, prevTwoBytes.length);
				if (prevTwoBytes[0] == VVXmppStreamHandler.startBytes[0]
						&& prevTwoBytes[1] == VVXmppStreamHandler.startBytes[1]) {
					byte[] lenBytes = new byte[4];
					in.read(lenBytes, 0, lenBytes.length);
					totalLen = BBSUtils.byteArrayToInt(lenBytes);
					hasReadLen = 0;
					//LogUtils.i("catch the start..totalLen:" + totalLen + " len:" + len + " off:" + off);
					isCatchStartOrEnd = true;
				} else if (prevTwoBytes[0] == VVXmppStreamHandler.endBytes[0]
						&& prevTwoBytes[1] == VVXmppStreamHandler.endBytes[1]) {
					//LogUtils.i("catch the end..");
					isCatchStartOrEnd = true;
				} else {
					if (avaliable_ignore < ignoreBytes.length) {
						ignoreBytes[avaliable_ignore++] = prevTwoBytes[0];
						ignoreBytes[avaliable_ignore++] = prevTwoBytes[1];
					} else {
						//LogUtils.i("ignoreBytes has been full,cannot fill");
					}
				}
			}
			if (avaliable_ignore > 0) {
				//LogUtils.e("this cannot be happen!:prevTwoBytes:"
				//						+ BBSUtils.Bytes2HexString(Arrays.copyOfRange(ignoreBytes, 0, avaliable_ignore)) + " len:"
				//						+ len);
			}
			return read(b, off, len);
		} else if (totalLen < hasReadLen) {
			//LogUtils.e("totalLen bigger than the hasReadLen,totalLen:" + totalLen + " hasReadLen:" + hasReadLen);
		} else {
			avaliable = Math.min(len, totalLen - hasReadLen);
			avaliable = in.read(b, off, avaliable);
			hasReadLen += avaliable;
		}
		return avaliable;
	}
}
