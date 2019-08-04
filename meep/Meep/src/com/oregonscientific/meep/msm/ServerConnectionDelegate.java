/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.oregonscientific.meep.Build;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

/**
 * Represents a connection to MEEP server.
 * 
 * @author Stanley Lam
 */
public class ServerConnectionDelegate {
	
	private final String TAG = getClass().getSimpleName();
	
	private final String MESSAGE_PING = "ping";
	private final String MESSAGE_PONG = "pong";
	
	/**
	 * The number of milliseconds the delegate can be idle
	 */
	public static final long MIN_IDLE_TIME = 10000;
	public static final long MAX_IDLE_TIME = 60000;
	public static final long MAX_RECONNECT_TIME = 500;
	
	public static enum State {
		DISCONNECTED,
		CONNECTING,
		CONNECTED
	};
	
	private final int MAX_RETRY_COUNT = 5;
	
	/**
	 * The listener that is used to notify when a server event occur
	 */
	private ServerEventListener mListener;
	
	/**
	 * The URI to a MEEP server
	 */
	private final URI mUri;
	
	/**
	 * A boolean that indicates whether or not the delegate should automatically reconnect
	 * to the MEEP server
	 */
	private boolean mAutoReconnect = false;
	private final ReconnectTask mReconnectTask = new ReconnectTask();
	
	/**
	 * The underlying WebSocket used to communicate with the MEEP server
	 */
	private WebSocket mSocket = null;
	private State mState = State.DISCONNECTED;
	private final ExecutorService mSenderService = Executors.newCachedThreadPool();
	
	/**
	 * A timer used to maintain connection with the MEEP server
	 */
	private final MEEPServerHandshakeTask mHandshakeTask = new MEEPServerHandshakeTask();
	private long idleTime = MIN_IDLE_TIME;
	private int idleRetryCount = 0;
	
	/**
	 * For starting/initiating connection with WebSocket server
	 */
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	private Future<?> mFuture;
	
	/**
	 * Constructs a new MEEPServerConnection
	 * 
	 * @paam url URI to the MEEP server
	 */
	public ServerConnectionDelegate(URI url) {
		this.mUri = url;
	}
	
	/**
	 * Disconnect with the MEEP server if a connection was established previously
	 */
	public synchronized void disconnect() {
		mAutoReconnect = false;
		mHandshakeTask.cancel();
		mReconnectTask.cancel();
		mSenderService.shutdownNow();
		
		try {
			mSocket.close();
		} catch (Exception ex) {
			mState = State.DISCONNECTED;
			
			if (mListener != null) {
				mListener.onError(ex);
			}
		} finally {
			mSocket = null;
		}
	}
	
	/**
	 * Connects to a MEEP server at the given URI
	 * 
	 * @param url URI to the MEEP server
	 */
	public synchronized void connect() {
		connect(false);
	}
	
	/**
	 * Reconnects to the MEEP server
	 */
	private void reconnect() {
		mReconnectTask.schedule(MAX_RECONNECT_TIME);
	}
	
	/**
	 * Connects to the MEEP server
	 * 
	 * @param autoReconnect A boolean that indicates whether or not the delegate should
	 * automatically reconnect to the server if connection was lost
	 */
	public synchronized void connect(final boolean autoReconnect) {
		// Do not connect if the delegate is already trying to establish connection
		if (isConnecting()) {
			Log.v(TAG, "Delegate is already trying to establish connection..");
			return;
		}
		
		if (isConnected()) {
			Log.e(TAG, "A connection is already established!");
			if (mListener != null) {
				mListener.onError(new ServerConnectionException("A connection is already established!"));
			}
			return;
		}
		
		mAutoReconnect = autoReconnect;
		mState = State.CONNECTING;
		
		try {
			mFuture = mExecutor.submit(new Runnable() {

				@Override
				public void run() {
					try {
						mSocket = new WebSocketConnection(mUri);
						mSocket.setEventHandler(new MEEPServerEventHandler());
						mSocket.connect();
						Log.d(TAG, "Connecting to " + mUri.toString());
					} catch (Exception ex) {
						Log.e(TAG, "Cannot establish connection with server because " + ex);
						mState = State.DISCONNECTED;
						if (autoReconnect) {
							reconnect();
						} else {
							if (mListener != null) {
								mListener.onError(ex);
							}
						}
					} finally {
						// Invalidate the task
						mFuture = null;
					}
				}
				
			});
		} catch (Exception ex) {
			// Failed to establish connection with server
			mState = State.DISCONNECTED;
			mFuture = null;
			
			Log.e(TAG, "Delegate cannot establish connection with server because " + ex);
		}
	}
	
	/**
	 * Indicates whether or not connection is established with a MEEP server  
	 * 
	 * @return true if a connection is established, false otherwise
	 */
	public synchronized boolean isConnected() {
		return mSocket != null && State.CONNECTED.equals(mState);
	}
	
	/**
	 * Returns whether or not the delegate is in the process of connecting to server
	 * 
	 * @return {@code true} if the delegate is connecting, {@code false} otherwise
	 */
	public synchronized boolean isConnecting() {
		return State.CONNECTING.equals(mState) || (mFuture != null && (!mFuture.isDone() || !mFuture.isCancelled()));
	}
	
	/**
	 * Returns whether or not the delegate will automatically reconnect to server
	 * when connection is lost
	 * 
	 * @return true if the delegate will automatically reconnect, false otherwise
	 */
	public boolean willAutoReconnect() {
		return mAutoReconnect;
	}
	
	/**
	 * Sends the given server message to the MEEP server
	 * 
	 * @param message The message to send to the MEEP server
	 */
	public synchronized void send(Message message) {
		if (message == null) {
			return;
		}
		
		try {
			send(message.toJson());
		} catch (Exception ex) {
			// The message could not be written into JSON
			Log.e(TAG, message.toString() + " cannot be written into a JSON representation");
		}
	}
	
	/**
	 * Sends the given data to the MEEP server 
	 * 
	 * @param data The data to send to the MEEP server
	 */
	public synchronized void send(final String data) {
		if (data == null) {
			return;
		}
		
		try {
			if (!mSenderService.isTerminated() && !mSenderService.isShutdown()) {
				mSenderService.execute(new Runnable() {

					@Override
					public void run() {
						try {
							Log.i(TAG, "WebSocket sending: " + data);
							mSocket.send(data);
							if (!data.trim().equalsIgnoreCase(MESSAGE_PING) 
									&& !data.trim().equalsIgnoreCase(MESSAGE_PONG)) {

								if (mListener != null) {
									mListener.onSend(data);
								}
							}
						} catch (Exception ex) {
							Log.e(TAG, data + " cannot be sent because " + ex + " occurred");
							if (mListener != null) {
								mListener.onError(new ServerMessageException(data, ex));
							}
						}
					}

				});
			}
		} catch (RejectedExecutionException ex) {
			Log.e(TAG, data + " cannot be sent because " + ex + " occurred");
			if (mListener != null) {
				mListener.onError(new ServerMessageException(data, ex));
			}
		}
	}
	
	/**
	 * Sets the maximum amount time the connection can remain idle before the delegate
	 * tries to reconnect with the MEEP server. The new idle time will not take effect 
	 * immediately. It will only come into effect the next time the handshake task is 
	 * rescheduled
	 * 
	 * @param idleTime The maximum amount of time (in milliseconds) the connection can remain idle 
	 */
	public void setAllowedIdleTime(long idleTime) throws ServerConnectionException {
		if (idleTime <= 0) {
			throw new ServerConnectionException(new IllegalArgumentException(
					"The maximum amount of time the connection can remain idle cannot be less than 1"));
		}
		
		this.idleTime = idleTime;
	}
	
	/**
	 * Register a callback to be invoked when a notification is received from
	 * a MEEP server
	 * 
	 * @param listener The notification listener that will be invoked
	 */
	public void setServerEventListener(ServerEventListener listener) {
		this.mListener = listener;
	}
	
	/**
	 * Reconnects with MEEP server if the connection was dropped
	 * 
	 * @author Stanley Lam
	 */
	private final class ReconnectTask {
		private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		private ScheduledFuture<?> handle = null;
		
		private final Runnable reconnectTask = new Runnable() {

			@Override
			public void run() {
				// The task is running, do not allow rescheduling
				handle = null;
				
				Log.d(TAG, "Trying to reconnect...");
				connect(mAutoReconnect);
			}
			
		};
		
		/**
		 * Schedule a reconnection with MEEP server with the given delay
		 * 
		 * @param delay The time from now to delay execution
		 */
		public void schedule(long delay) {
			if (handle != null) {
				// A reconnection has already been scheduled.
				Log.d(TAG, "A reconnection is already scheduled");
				return;
			}
			
			try {
				handle = scheduler.schedule(reconnectTask, delay, TimeUnit.MILLISECONDS);
				Log.d(TAG, "Scheduled to reconnect in " + delay + " milliseconds");
			} catch (Exception ex) {
				// The task cannot be scheduled
				Log.e(TAG, "Reconnect with server cannot be scheduled because " + ex.getMessage());
			}
		}
		
		/**
		 * Cancel trying to reestablish connection with MEEP server
		 */
		public void cancel() {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
		}
	}
	
	/**
	 * Perform handshake with the MEEP server. If the connection remains idle, tries
	 * to initiate a reconnection.
	 * 
	 * @author Stanley Lam
	 */
	private final class MEEPServerHandshakeTask {
		private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		private ScheduledFuture<?> handle;
		
		private final Runnable handshakeTask = new Runnable() {

			@Override
			public void run() {
				if (idleRetryCount < MAX_RETRY_COUNT) {
					// Sends a "ping" request to server
					send(MESSAGE_PING);
					idleRetryCount ++;
					
					schedule(idleTime);
				} else {
					// Try to reconnect with the MEEP server
					try {
						Log.d(TAG, "Connection with server remained idle for too long, closing connection...");
						mSocket.close();
					} catch (Exception ex) {
						// Could not close the socket properly.
						if (mAutoReconnect) {
							mState = State.DISCONNECTED;
							reconnect();
						}
					}
				}
			}
			
		};
		
		/**
		 * Schedule a handshake with MEEP server with the given delay
		 * 
		 * @param delay The time from now to delay execution
		 */
		public void schedule(long delay) {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
			
			try {
				handle = scheduler.schedule(handshakeTask, delay, TimeUnit.MILLISECONDS);
			} catch (Exception ex) {
				// The task cannot be scheduled
				Log.e(TAG, "Handshake with server cannot be scheduled because " + ex.getMessage());
			}
		}
		
		public void cancel() {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
		}
	}
	
	private final class MEEPServerEventHandler implements WebSocketEventHandler {

		@Override
		public void onOpen() {
			Log.i(TAG, "WebSocket opened");
			idleRetryCount = 0;
			mState = State.CONNECTED;
			
			// Only schedule the handshake task if the running environment
			// is production
			if (mAutoReconnect && Build.environment.equals(Build.Environment.PRODUCTION)) {
				mHandshakeTask.schedule(idleTime);
			}
			
			if (mListener != null) {
				mListener.onOpen();
			}
		}

		@Override
		public void onMessage(WebSocketMessage message) {
			// Quick return if there is no message to process
			if (message == null) {
				return;
			}
			
			Log.i(TAG, "WebSocket received message: " + message.getText());
			idleRetryCount = 0;
			
			// Only schedule the handshake task if the running environment
			// is production
			if (mAutoReconnect && Build.environment.equals(Build.Environment.PRODUCTION)) {
				mHandshakeTask.schedule(idleTime);
			}
			
			String textMessage = message.getText();
			if ( textMessage.trim().equalsIgnoreCase(MESSAGE_PING)) {
				// TODO: Respond to server's "ping" request
					send(MESSAGE_PONG);
			} else if (textMessage.trim().equalsIgnoreCase(MESSAGE_PONG)) {
				// Ignore the handshake message
			} else {
				if (mListener != null) {
					mListener.onReceive(textMessage);
				}
			}
		}

		@Override
		public void onClose() {
			Log.i(TAG, "WebSocket closed");
			idleRetryCount = 0;
			mState = State.DISCONNECTED;
			
			if (mListener != null) {
				mListener.onClose();
			}
			
			if (mAutoReconnect) {
				reconnect();
			}
		}
		
	}

}
