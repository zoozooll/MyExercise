package org.jivesoftware.smack.readerwriter;

/**
 * @ClassName: VVXmppStreamHandler
 * @Description: Xmpp流处理器，将原有的数据附加：start(2个字节),len(4个字节)，end(2个字节)
 * @author: yuedong bao
 * @date: 2015-5-5 下午5:46:48
 */
public class VVXmppStreamHandler {
	public static final byte[] startBytes = new byte[] { 0x1A, 0x7E };
	public static final byte[] endBytes = new byte[] { 0x7E, 0x1A };
	public static final byte leftBracket = 0x3C;// 字符<的编码
}
