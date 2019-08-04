package com.oregonscientific.meep.junit.test;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.oregonscientific.meep.msm.Message;

import android.util.Log;

public class MockWebSocketServer extends WebSocketServer {

	public final static String TEST_SERVER_ADDRESS = "localhost";
	public final static int    TEST_SERVER_PORT	= 8887;
	
	private final String TAG = MockWebSocketServer.class.getSimpleName();
	
	/**
	 * The responses should be synchronized
	 */
	private final Object mLock = new Object();
	private final List<MessageResponder> mResponders = new ArrayList<MessageResponder>();
	
	public MockWebSocketServer() throws UnknownHostException {
		
		super(new InetSocketAddress(TEST_SERVER_ADDRESS, TEST_SERVER_PORT));
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Adds a {@line MessageResponder} to run when a {@link Message} matching the
	 * given {@code filter} is received  
	 * 
	 * @param responder the {@link MessageResponder} to run
	 */
	public void addResponder(MessageResponder responder) {
		synchronized (mLock) {
			if (responder != null && !mResponders.contains(responder)) {
				mResponders.add(responder);
			}
		}
	}
	
	/**
	 * Retrieves a {@link Message} representation of the given {@code message} 
	 * 
	 * @param message the text string to convert into a {@link Message}
	 * @return the {@link Message} representation of {@code message}
	 */
	private Message deserializeMessage(String message) {
		// Quick return if there is nothing to process
		if (message == null) {
			return null;
		}
		
		// Deserializes the message into a Message object
		try {
			return Message.fromJson(message);
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Remove the given {@link MessageResponder} from responding to messages received 
	 * 
	 * @param responder the {@link MessageResponder} to remove
	 */
	public void removeResponder(MessageResponder responder) {
		synchronized (mLock) {
			mResponders.remove(responder);
		}
	}
	
	/**
	 * Removes all previously added responders
	 */
	public void removeAllResponders() {
		synchronized (mLock) {
			mResponders.clear();
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		Log.i(TAG, "A connection is opened");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		Log.i(TAG, "Server is closing");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		Log.d(TAG, "Server received message: " + message);
		Message msg = deserializeMessage(message);
		synchronized (mLock) {
			for (MessageResponder responder : mResponders) {
				if (responder.shouldRespond(msg) && !msg.isReceived()) {
					String data = responder.onRespond(msg);
					conn.send(data);
					
					Log.d(TAG, "Server sending: " + data);
				}
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		Log.e(TAG, "Server error because " + ex);
	}

	
	public void sendToAll(String text) {
		Collection<WebSocket> con = connections();
		synchronized (con) {
			for (WebSocket c : con) {
				c.send( text );
			}
		}
	}
	

}
