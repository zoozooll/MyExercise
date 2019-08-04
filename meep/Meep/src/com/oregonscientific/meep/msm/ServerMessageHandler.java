/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.net.http.AndroidHttpClient;
import android.os.SystemClock;
import android.util.Log;

import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.util.NetworkUtils;

import de.roderick.weberknecht.WebSocketException;

/**
 * A handler that handles messaging between client and the MEEP server
 * 
 * @author Stanley Lam
 */
public class ServerMessageHandler implements ServerEventListener {
	
	private final String TAG = "ServerMessageHandler";
	private final String CLIENT_AGENT = "MEEP/3.0";
	
	private final String PROD_SERVER_URI = "wss://portal.meeptablet.com:443/websocket";
	private final String DEV_SERVER_URI = "ws://localhost:8887";
	private final String TIMESTAMP_URI = "http://portal.meeptablet.com/timestamp";
	
	/**
	 * A message is only timed out if it was older than 1 hour
	 */
	private final long MESSAGE_TIMEOUT = 3600000;
	private final long GB_RUN_PERIOD = 30000;
	
	private final int MAX_RETRIES = 10;
	private final long MAX_RESEND_TIME = 1000;
	private final int MAX_THREAD_COUNT = 10;
	
	/**
	 * The minimum device timestamp
	 */
	private final long TIME_SYNC = 1346457600;
	
	/**
	 * The delegate that represents the MEEP server
	 */
	private ServerConnectionDelegate delegate;
	
	/**
	 * The context the handler runs in
	 */
	private final Context mContext;
	
	private ScheduledExecutorService messageSenderService =
		Executors.newScheduledThreadPool(MAX_THREAD_COUNT);
	
	private final GarbageMessageCollectionTask garbageCollectionTask = 
		new GarbageMessageCollectionTask();
	
	private final ConcurrentHashMap<UUID, MEEPMessage> sentMessages = 
		new ConcurrentHashMap<UUID, MEEPMessage>();
	
	private final Object mLock = new Object();
	private final List<ServerMessageReceiver> mMessageReceivers = 
		new ArrayList<ServerMessageReceiver>();
	
	/**
	 * For starting/initiating server connection
	 */
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	
	/**
	 * Construct a new server message handler
	 * 
	 * @param context The context the handler runs in, through which it can access other 
	 * system services
	 */
	public ServerMessageHandler(Context context) {
		mContext = context;
	}
	
	/**
	 * Determines whether the a user is authenticated with MEEP server 
	 * 
	 * @return true if the user has been authenticated, false otherwise
	 */
	public boolean isLoggedIn() {
		AccountManager manager = (AccountManager) ServiceManager.getService(mContext, ServiceManager.ACCOUNT_SERVICE);
		return (isConnected() && manager.getLoggedInAccount() != null);
	}
	
	/**
	 * Determines whether or not the handler is connected with the delegate
	 * @return true if the handler is connected with the delegate, false otherwise
	 */
	public boolean isConnected() {
		return (delegate != null && delegate.isConnected());
	}
	
	/**
	 * Determines whether or not the handler is in the process of connecting to
	 * the remote server 
	 * 
	 * @return {@code true} if the handler is trying to establish connection, {@code false} otherwise
	 */
	public boolean isConnecting() {
		return (delegate != null && delegate.isConnecting());
	}
	
	/**
	 * Synchronize device time with server
	 */
	private void syncDeviceTimeWithServer() {
		if (!NetworkUtils.hasConnection(mContext)) {
			return;
		}
		
		AndroidHttpClient client = AndroidHttpClient.newInstance(CLIENT_AGENT);
		HttpGet get = new HttpGet(TIMESTAMP_URI);
		
		try {
			StringBuilder sb = new StringBuilder();
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				InputStream is = entity.getContent();
				if (is != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					is.close();
				}
				entity.consumeContent();
			}
			
			String timestamp = sb.toString();
			if (timestamp != null && !timestamp.isEmpty()) {
				long time = Long.parseLong(timestamp.trim());
				SystemClock.setCurrentTimeMillis(time);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot synchronize device time with server because " + ex + " occurred");
		} finally {
			client.close();
		}
	}
	
	/**
	 * Register a component to be run when a Message received matches the {@code filter} 
	 * 
	 * @param component Name of the component to handle the message
	 * @param filter The filter the message to match against
	 */
	public void registerReceiver(ComponentName component, MessageFilter filter) {
		synchronized (mLock) {
			ServerMessageReceiver receiver = new ServerMessageReceiver(component, filter);
			if (!mMessageReceivers.contains(receiver)) {
				mMessageReceivers.add(receiver);
			}
		}
	}
	
	/**
	 * Unregister a previously registered component. All filters that have been registered
	 * for the component will be removed
	 * 
	 * @param component The component to unregister
	 */
	public void unregisterReceiver(ComponentName component) {
		// Quick return if there is nothing to process
		if (component == null) {
			return;
		}
		
		synchronized (mLock) {
			Iterator<ServerMessageReceiver> iterator = mMessageReceivers.iterator();
			while (iterator.hasNext()) {
				ServerMessageReceiver receiver = iterator.next();
				if (receiver.componentName.equals(component)) {
					iterator.remove();
				}
			}
		}
	}
	
	/**
	 * Restart the message service with given environment
	 * 
	 * @param environmentInt 0 is for Production, 1 is for Sandbox
	 */
	public void reconnect(int environmentInt) {
		Build.environment = Build.Environment.fromInteger(environmentInt);
		start(true);
	}
	
	/**
	 * Starts to handle messages between the MEEP and server
	 * 
	 * @param autoReconnect If true, the handler will automatically try to reconnect with the server
	 * if connection was lost. Otherwise, the MEEP will remain disconnected with server if connection
	 * was lost.
	 */
	public void start(final boolean autoReconnect) {
		startThreaded(autoReconnect);
	}
	
	private void startThreaded(final boolean autoReconnect) {
		// Quick return if handler is already trying to establish connection
		// with server
		if (delegate != null && delegate.isConnecting()) {
			return;
		}
		
		final ServerEventListener listener = this;
		Runnable start = new Runnable() {

			@Override
			public void run() {
				try {
					// Disconnect any previously established connection
					stop();
					
					boolean sandboxing = Build.Environment.SANDBOX.equals(Build.environment);
					String url = sandboxing ? DEV_SERVER_URI : PROD_SERVER_URI;
					
					// Synchronize device time with server time if the device timestamp
					// is set to earlier than 2012-09-01
					if (System.currentTimeMillis() / 1000 < TIME_SYNC) {
						syncDeviceTimeWithServer();
					}
					
					URI uri = new URI(url);
					delegate = new ServerConnectionDelegate(uri);
					delegate.setServerEventListener(listener);
					
					// Do not auto-reconnect with server if debugging
					delegate.connect(Build.DEBUG ? false : autoReconnect);
					Log.i(TAG, "Starting WebSocket server delegate...");
					
					// Schedule the garbage message collector to run
					garbageCollectionTask.schedule(GB_RUN_PERIOD);
				} catch (Exception ex) {
					// Ignored
					Log.e(TAG, "Cannot start delegte because " + ex + " occurred");
				}
			}
			
		};
		
		try {
			mExecutor.submit(start);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot start WebSocket delegate because " + ex);
		}
	}
	
	/**
	 * Stops the handler from handling messages. All unsent messages will remain and the
	 * handler will try to resend them upon restart
	 */
	public void stop() {
		garbageCollectionTask.cancel();
		
		if (delegate != null) {
			delegate.disconnect();
			delegate = null;
		}
	}
	
	/**
	 * Sets the connection timeout period 
	 * 
	 * @param timeout The new timeout period
	 * @throws ServerConnectionException Thrown if {@code} timeout is less than 1
	 */
	public void setTimeout(long timeout) throws ServerConnectionException {
		if (delegate != null) {
			delegate.setAllowedIdleTime(timeout);
		}
	}
	
	/**
	 * Send notification to registered receivers of the  
	 * 
	 * @param message The message received from server
	 */
	private synchronized void notifyReceivers(String message) {
		try {
			List<ComponentName> components = new ArrayList<ComponentName>();
			
			// Deserializes the message into a Message object
			Message serverMessage = deserializeMessage(message);
			synchronized (mLock) {
				// Iterator through the list of registered receivers to find the one(s)
				// that are registered to handle the message. 
				Iterator<ServerMessageReceiver> iterator = mMessageReceivers.iterator();
				while (iterator.hasNext()) {
					ServerMessageReceiver receiver = iterator.next();
					if (receiver.shouldReceive(serverMessage) 
							&& receiver.componentName != null
							&& !components.contains(receiver.componentName)) {
						
						components.add(receiver.componentName);
					}
				}
			}
			
			// A message is only sent to the receiver once even if it was registered
			// multiple times
			notifyReceiver(components, serverMessage);
		} catch (Exception ex) {
			// Either the message was invalid or the receiver cannot be started. Ignore the message
			Log.e(TAG, message + " cannot be sent to receivers because " + ex + " occurred");
		}
	}
	
	/**
	 * Notify {@code receivers} a {@code message} is received from MEEP server
	 * 
	 * @param receivers A list of receivers to notify
	 * @param message The message received from MEEP server
	 */
	private synchronized void notifyReceiver(List<ComponentName> receivers, Message message) {
		// Quick return if there is nothing to process
		if (receivers == null || message == null || mContext == null) {
			return;
		}
		
		for (ComponentName component : receivers) {
			try {
				if (isServiceExist(component)) {
					Intent intent = new Intent();
					intent.setComponent(component);
					intent.setAction(MessageManager.MESSAGE_RECEIVED_ACTION);
					intent.putExtra(MessageManager.EXTRA_MESSAGE, message);
					mContext.startService(intent);
				}
			} catch (Exception ex) {
				// Ignore. Continue to notify the remaining receivers.
				Log.e(TAG, message + " cannot be delivered to " + component + " because " + ex + " occurred");
			}
		}
	}
	
	/**
	 * Determines whether the given class name is a service on the system
	 * 
	 * @param component
	 *            the full component name (i.e. com.oregonscientific.meep.home/com.oregonscientific.meep.account.AccountService} of a
	 *            {@link Service} class
	 * @return {@code true} if the {@code component} is a {@link Service},
	 *         {@code false} otherwise
	 */
	private boolean isServiceExist(ComponentName component) {
		ServiceInfo info = null;
		try {
			Log.i(TAG, "Retrieving information for " + component);
			info = mContext.getPackageManager().getServiceInfo(component, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException ex) {
			Log.e(TAG, component + " is not a service");
		}
		return info != null;
	}
	
	/**
	 * Determine whether or not the given message is a sign-in request
	 * 
	 * @param message The message to determine whether or not it was a sign-in request
	 * @return true if the message is a sign-in message, false otherwise
	 */
	protected boolean isSigninMessage(Message message) {
		return (message != null
				&& message.getProcess() != null 
				&& message.getOperation() != null 
				&& message.getProcess().equals(Message.PROCESS_ACCOUNT)
				&& message.getOperation().equals(Message.OPERATION_CODE_SIGN_IN));
	}

	/**
	 * Retrieves the sign-in message that was sent previously but is still
	 * awaiting for response from remote server
	 * 
	 * @return the sign-in message or {@code null} if there is no pending sign-in message
	 */
	protected Message getPendingSigninMessage() {
		Collection<MEEPMessage> messages = sentMessages.values();
		for (MEEPMessage meepMessage : messages) {
			if (isSigninMessage(meepMessage.message)) {
				return meepMessage.message;
			}
		}
		return null;
	}
	
	/**
	 * Determine whether or not the given message is a chat text message
	 * 
	 * @param message The message to determine whether or not it was a sign-in request
	 * @return true if the message is a text message, false otherwise
	 */
	protected boolean isIMConversationMessage(Message message) {
		return (message != null
				&& message.getProcess() != null 
				&& message.getOperation() != null 
				&& message.getProcess().equals(Message.PROCESS_INSTANT_MESSAGING)
				&& message.getOperation().equals(Message.OPERATION_CODE_TEXT_MSG));
	}
	
	/**
	 * Determines whether or not an acknowledgment should be sent to server
	 * for the given message
	 * 
	 * @param message The message to determine whether an acknowlegment should be sent
	 * @return true if an acknowlegment should be sent, false otherwise
	 */
	protected boolean shouldSendAcknowledgment(Message message) {
		return (message != null
				&& message.getMessageID() != null
				&& !message.isReceived() 
				&& !isSigninMessage(message));
	}
	
	/**
	 * Determines whether or not the hanlder should wait for an acknowledgment from server
	 * for the given message
	 * 
	 * @param message The message to determine whether the handler should wait for an acknowledgment
	 * @return true if an acknowledgment is expected, false otherwise
	 */
	private boolean shouldWaitForAcknowledgment(Message message) {
		return (message != null 
				&& message.getMessageID() != null 
				&& !message.isReceived() 
				&& !isIMConversationMessage(message));
	}
	
	/**
	 * Sends the given message to the MEEP server
	 * 
	 * @param message the message to send to the MEEP server
	 */
	public synchronized void sendMessage(Message message) {
		if (message == null) {
			return;
		}
		
		try {
			sendMessage(message.toJson());
		} catch (Exception ex) {
			// Cannot send the message. Ignore
			Log.e(TAG, message + " cannot be sent because " + ex + " occurred");
		}
	}
	
	/**
	 * Sends the given message to the MEEP server
	 * 
	 * @param message the message to send to the MEEP server
	 */
	public synchronized void sendMessage(String message) {
		// Quick return if there is nothing to send
		if (message == null) {
			return;
		}
		
		Message serverMessage = deserializeMessage(message);
		if (isConnected()) {
			// Removes a pending sign-in request if the current request is a sign-in request
			if (isSigninMessage(serverMessage)) {
				Message pending = getPendingSigninMessage();
				if (pending != null) {
					sentMessages.remove(pending.getMessageID());
				}
			}
			
			// Ignore sign-in request if the device is already logged in
			if (isLoggedIn() && isSigninMessage(serverMessage)) {
				return;
			}
			
			// Send the message
			delegate.send(message);
		} else {
			// Put the message into sent-queue. Garbage message collector will try to
			// send the message in its next run
			if (serverMessage != null 
					&& serverMessage.getMessageID() != null 
					&& sentMessages.get(serverMessage.getMessageID()) == null) {
				
				sentMessages.put(serverMessage.getMessageID(), new MEEPMessage(message));
			}
		}
	}
	
	/**
	 * Resend the given message after a delay period
	 * 
	 * @param message The message to deliver
	 */
	private void resendMessage(final String message) {
		// Quick return if there is nothing to process
		if (message == null || delegate == null) {
			return;
		}
		
		try {
			// Try to resend the message if a connection with server is established
			Runnable resendTask = new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, "Resending message " + message);
					sendMessage(message);
				}
				
			};
			// Resend the message
			messageSenderService.schedule(resendTask, MAX_RESEND_TIME, TimeUnit.MILLISECONDS);
		} catch (Exception ex) {
			// The task cannot be scheduled for execution. Put the message into
			// sent-queue. Garbage message collector will try to send the message in
			// its next run
			Message serverMessage = deserializeMessage(message);
			if (serverMessage != null 
					&& serverMessage.getMessageID() != null 
					&& sentMessages.get(serverMessage.getMessageID()) == null) {
				
				sentMessages.put(serverMessage.getMessageID(), new MEEPMessage(message));
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
			Log.e(TAG, "Fail to deserialize message: " + message);
			return null;
		}
	}
	
	@Override
	public void onOpen() {
		// Broadcast action: a connection with MEEP server is established
		if (mContext != null) {
			Intent intent = new Intent();
			intent.setAction(MessageManager.SERVER_CONNECTION_STATE_CHANGED_ACTION);
			intent.putExtra(MessageManager.EXTRA_SERVER_CONNECTED, true);
			mContext.sendBroadcast(intent);
		}
	}

	@Override
	public void onSend(String data) {
		if (data == null) {
			return;
		}
		
		// Add the message to the sent messages queue, ignoring acknowledgment messages
		try {
			Message serverMessage = deserializeMessage(data);
			if (serverMessage != null 
					&& shouldWaitForAcknowledgment(serverMessage) 
					&& sentMessages.get(serverMessage.getMessageID()) == null) {
				
				sentMessages.put(serverMessage.getMessageID(), new MEEPMessage(data));
			}
		} catch (Exception ex) {
			// The message was not a valid representation of Message. IGNORE
			Log.e(TAG, "Data: " + data + " is not a valid representation of Message: " + ex);
		}
	}

	@Override
	public void onReceive(String message) {
		try {
			Message serverMessage = deserializeMessage(message);
			if (serverMessage != null 
					&& serverMessage.getMessageID() != null 
					&& serverMessage.isReceived()) {
				
				// Remove the message from sent messages queue
				sentMessages.remove(serverMessage.getMessageID());
			}
			
			// Notify receivers of receipt of the message
			notifyReceivers(message);
			
			// Send receive acknowledgement
			if (shouldSendAcknowledgment(serverMessage)) {
				serverMessage.setReceived(true);
				delegate.send(serverMessage);
			}
		} catch (Exception ex) {
			// The message was not a valid representation of Message. IGNORE
			Log.e(TAG, "Message: " + message + " is not a valid representation of Message: " + ex);
		}
	}

	@Override
	public void onClose() {
		// TODO: Connection with the MEEP server is closed. Perform cleanup tasks
		Log.i(TAG, "WebSocket is closed");
		
		// Broadcast action: a connection with MEEP server is lost
		if (mContext != null) {
			Intent intent = new Intent();
			intent.setAction(MessageManager.SERVER_CONNECTION_STATE_CHANGED_ACTION);
			intent.putExtra(MessageManager.EXTRA_SERVER_CONNECTED, false);
			mContext.sendBroadcast(intent);
		}
		
		// Manually restart delegate if debugging
		if (Build.DEBUG) {
			start(false);
		}
	}

	@Override
	public void onError(Throwable cause) {
		if (cause instanceof ServerMessageException) {
			// The message was not sent. Resend it!
			String message = ((ServerMessageException) cause).getMessage();
			resendMessage(message);
		} else if (cause instanceof WebSocketException) {
			// Restart delegate
			if (Build.DEBUG) {
				start(false);
			}
		}
	}
	
	/**
	 * The receiver that will receive message sent by MEEP server
	 */
	private final class ServerMessageReceiver {
		
		ComponentName componentName;
		MessageFilter filter;
		
		ServerMessageReceiver(ComponentName componentName, MessageFilter filter) {
			this.componentName = componentName;
			this.filter = filter;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other == null || other.getClass() != getClass()) {
				return false;
			}
			
			ServerMessageReceiver otherReceiver = (ServerMessageReceiver) other;
			boolean sameComponent = false;
			if (componentName != null && otherReceiver.componentName != null) {
				sameComponent = componentName.equals(otherReceiver.componentName);
			} else {
				sameComponent = componentName == null && otherReceiver.componentName == null;
			}
			
			boolean sameFilter = false;
			if (filter != null && otherReceiver.filter != null) {
				sameFilter = filter.equals(otherReceiver.filter);
			} else {
				sameFilter = filter == null && otherReceiver.filter == null;
			}
			
			return sameComponent && sameFilter;
		}
		
		public boolean shouldReceive(Message message) {
			boolean result = false;
			if (filter != null) {
				result = filter.match(message) > 0;
			}
			return result;
		}
	}
	
	private final class MEEPMessage {
		
		private final Message message;
		private final long createdDate;
		private long retries;
		
		public MEEPMessage(String message) {
			this.message = deserializeMessage(message);
			this.createdDate = System.currentTimeMillis();
			retries = 0;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other == null || other.getClass() != getClass()) {
				return false;
			}
			
			boolean result = false;
			if (other != null && message != null) {
				try {
					MEEPMessage otherMessage = (MEEPMessage) other;
					result = otherMessage.message.getMessageID().equals(message.getMessageID());
				} catch (Exception ex) {
					// The message is not a valid JSON
					result = false;
				}
			} else {
				result = other == null && message == null;
			}
			return result;
		}
		
	}
	
	/**
	 * Garbage message collection is the process that the handler uses to remove messages
	 * that are timed out from the queue
	 * 
	 * @author Stanley Lam
	 */
	private final class GarbageMessageCollectionTask {
		private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		private ScheduledFuture<?> handle;
		
		private final Runnable garbageCollectionTask = new Runnable() {

			@Override
			public void run() {
				long currentTime = System.currentTimeMillis();
				Iterator<Entry<UUID, MEEPMessage>> it = sentMessages.entrySet().iterator();
				while (it.hasNext()) {
					Entry<UUID, MEEPMessage> entry = it.next();
					Message message = entry.getValue().message;
					
					// Removes messages that exceeded the timeout period
					if (!isSigninMessage(message) 
							&& (currentTime - entry.getValue().createdDate > MESSAGE_TIMEOUT 
									|| entry.getValue().retries > MAX_RETRIES)) {
						
						it.remove();
					} else {
						if (delegate != null && delegate.isConnected()) {
							
							if (isSigninMessage(message)) {
								// No response from the last sign-in request, remove it from
								// queue and resend it
								it.remove();
								sendMessage(entry.getValue().message);
							} else if (isLoggedIn()) {
								// No acknowledgement was received since collector was last ran.
								// Resend the message
								entry.getValue().retries++;
								sendMessage(entry.getValue().message);
							}
						}
					}
				}
			}
			
		};
		
		/**
		 * Schedule a garbage collection
		 * 
		 * @param period The time from now to delay execution
		 */
		public void schedule(long period) {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
			
			try {
				handle = scheduler.scheduleWithFixedDelay(garbageCollectionTask, period, period, TimeUnit.MILLISECONDS);
			} catch (Exception ex) {
				// The task cannot be scheduled
				Log.e(TAG, "Garbage message collection cannot be scheduled because " + ex.getMessage());
			}
		}
		
		/**
		 * Canel execution of the task. If the task was already running, it will be interrupted
		 */
		public void cancel() {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
		}
	}

}
