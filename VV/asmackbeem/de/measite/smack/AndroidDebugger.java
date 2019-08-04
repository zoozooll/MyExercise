package de.measite.smack;

import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.*;

import android.os.Environment;
import android.util.Log;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Very simple debugger that prints to the android log the sent and received stanzas. Use this
 * debugger with caution since printing to the console is an expensive operation that may even block
 * the thread since only one thread may print at a time.
 * <p>
 * <p/>
 * It is possible to not only print the raw sent and received stanzas but also the interpreted
 * packets by Smack. By default interpreted packets won't be printed. To enable this feature just
 * change the <tt>printInterpreted</tt> static variable to <tt>true</tt>.
 * @author Gaston Dombiak
 */
public class AndroidDebugger implements SmackDebugger {
	private static final boolean IS_SHOW_DEBUG = true && BuildConfig.DEBUG;
	private static final boolean IS_SAVE_DEBUG = true && BuildConfig.DEBUG;
	public static boolean printInterpreted = false;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"hh:mm:ss aaa");
	private Connection connection = null;
	private PacketListener listener = null;
	private ConnectionListener connListener = null;
	private Writer writer;
	private Reader reader;
	private ReaderListener readerListener;
	private WriterListener writerListener;
	private Writer smackWriter;
	private static final String SMACK_LOG_FILE = Environment
			.getExternalStorageDirectory().getPath()
			+ File.separator
			+ "smacklog"
			+ File.separator
			+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
			+".txt";

	public AndroidDebugger(Connection connection, Writer writer, Reader reader) {
		this.connection = connection;
		this.writer = writer;
		this.reader = reader;
		createDebug();
	}
	/**
	 * Creates the listeners that will print in the console when new activity is detected.
	 */
	private void createDebug() {
		// Create a special Reader that wraps the main Reader and logs data to
		// the GUI.
		if (IS_SAVE_DEBUG) {
			try {
				File logfile = new File(SMACK_LOG_FILE);
				if (!logfile.getParentFile().exists()) {
					logfile.getParentFile().mkdirs();
				}
				smackWriter = new FileWriter(logfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		ObservableReader debugReader = new ObservableReader(reader);
		readerListener = new ReaderListener() {
			@Override
			public void read(String str) {
				if (IS_SHOW_DEBUG)
					Log.d("SMACK", dateFormatter.format(new Date()) + " RCV  ("
							+ connection.hashCode() + "): " + str);
				try {
					if (IS_SAVE_DEBUG) {
						smackWriter.write(dateFormatter.format(new Date())
								+ " RCV  (" + connection.hashCode() + "): "
								+ str + "\r\n");
						smackWriter.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		debugReader.addReaderListener(readerListener);
		// Create a special Writer that wraps the main Writer and logs data to
		// the GUI.
		ObservableWriter debugWriter = new ObservableWriter(writer);
		writerListener = new WriterListener() {
			@Override
			public void write(String str) {
				if (IS_SHOW_DEBUG)
					Log.d("SMACK", dateFormatter.format(new Date()) + " SENT ("
							+ connection.hashCode() + "): " + str);
				try {
					if (IS_SAVE_DEBUG) {
						smackWriter.write(dateFormatter.format(new Date()) + " SENT ("
								+ connection.hashCode() + "): " + str + "\r\n");
						smackWriter.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		debugWriter.addWriterListener(writerListener);
		// Assign the reader/writer objects to use the debug versions. The
		// packet reader
		// and writer will use the debug versions when they are created.
		reader = debugReader;
		writer = debugWriter;
		// Create a thread that will listen for all incoming packets and write
		// them to
		// the GUI. This is what we call "interpreted" packet data, since it's
		// the packet
		// data as Smack sees it and not as it's coming in as raw XML.
		listener = new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				if (printInterpreted) {
					if (IS_SHOW_DEBUG)
						Log.d("SMACK", dateFormatter.format(new Date())
								+ " RCV PKT (" + connection.hashCode() + "): "
								+ packet.toXML());
					try {
						if (IS_SAVE_DEBUG) {
							smackWriter.write(dateFormatter.format(new Date())
									+ " RCV PKT (" + connection.hashCode()
									+ "): " + packet.toXML() + "\r\n");
							smackWriter.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		connListener = new ConnectionListener() {
			@Override
			public void connectionClosed() {
				if (IS_SHOW_DEBUG)
					Log.d("SMACK", dateFormatter.format(new Date())
							+ " Connection closed (" + connection.hashCode()
							+ ")");
				try {
					if (IS_SAVE_DEBUG) {
						smackWriter.write(dateFormatter.format(new Date())
								+ " Connection closed ("
								+ connection.hashCode() + ")" + "\r\n");
						smackWriter.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void connectionClosedOnError(Exception e) {
				if (IS_SHOW_DEBUG)
					Log.d("SMACK", dateFormatter.format(new Date())
							+ " Connection closed due to an exception ("
							+ connection.hashCode() + "|||" + e + ")");
				try {
					if (IS_SAVE_DEBUG) {
						smackWriter.write(dateFormatter.format(new Date())
								+ " Connection closed due to an exception ("
								+ connection.hashCode()+ "|||" + e  + ")" + "\r\n");
						smackWriter.flush();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			@Override
			public void reconnectionFailed(Exception e) {
				if (IS_SHOW_DEBUG)
					Log.d("SMACK", dateFormatter.format(new Date())
							+ " Reconnection failed due to an exception ("
							+ connection.hashCode()+ "|||" + e  + ")");
				try {
					if (IS_SAVE_DEBUG) {
						smackWriter.write(dateFormatter.format(new Date())
								+ " Reconnection failed due to an exception ("
								+ connection.hashCode()+ "|||" + e  + ")" + "\r\n");
						smackWriter.flush();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			@Override
			public void reconnectionSuccessful() {
				if (IS_SHOW_DEBUG)
					Log.d("SMACK",
							dateFormatter.format(new Date())
									+ " Connection reconnected ("
									+ connection.hashCode() + ")");
				try {
					if (IS_SAVE_DEBUG) {
						smackWriter.write(dateFormatter.format(new Date())
								+ " Connection reconnected ("
								+ connection.hashCode() + ")" + "\r\n");
						smackWriter.flush();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			@Override
			public void reconnectingIn(int seconds) {
				if (IS_SHOW_DEBUG)
					Log.d("SMACK", dateFormatter.format(new Date())
							+ " Connection (" + connection.hashCode()
							+ ") will reconnect in " + seconds);
				try {
					if (IS_SAVE_DEBUG) {
						smackWriter.write(dateFormatter.format(new Date())
								+ " Connection (" + connection.hashCode()
								+ ") will reconnect in " + seconds + "\r\n");
						smackWriter.flush();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
	}
	@Override
	public Reader newConnectionReader(Reader newReader) {
		((ObservableReader) reader).removeReaderListener(readerListener);
		ObservableReader debugReader = new ObservableReader(newReader);
		debugReader.addReaderListener(readerListener);
		reader = debugReader;
		return reader;
	}
	@Override
	public Writer newConnectionWriter(Writer newWriter) {
		((ObservableWriter) writer).removeWriterListener(writerListener);
		ObservableWriter debugWriter = new ObservableWriter(newWriter);
		debugWriter.addWriterListener(writerListener);
		writer = debugWriter;
		return writer;
	}
	@Override
	public void userHasLogged(String user) {
		boolean isAnonymous = "".equals(StringUtils.parseName(user));
		String title = "User logged (" + connection.hashCode() + "): "
				+ (isAnonymous ? "" : StringUtils.parseBareAddress(user)) + "@"
				+ connection.getServiceName() + ":" + connection.getPort();
		title += "/" + StringUtils.parseResource(user);
		// Add the connection listener to the connection so that the debugger
		// can be notified
		// whenever the connection is closed.
		connection.addConnectionListener(connListener);
	}
	@Override
	public Reader getReader() {
		return reader;
	}
	@Override
	public Writer getWriter() {
		return writer;
	}
	@Override
	public PacketListener getReaderListener() {
		return listener;
	}
	@Override
	public PacketListener getWriterListener() {
		return null;
	}
}