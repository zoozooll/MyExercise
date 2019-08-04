/**
 * 
 */
package com.oregonscientific.bbq.ble;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

/**
 * @author aaronli
 *
 */
public class CommandPool implements Runnable {
	
	private static final String TAG = "CommandPool";
	
	private boolean running;
	private Deque<Command> commandQueue = new ArrayDeque<Command>();
	private CommandPoolControll client;
	private Command currentUsingCommand;
	
	public void setCommandPoolControll(CommandPoolControll cpc) {
		client = cpc;
	}

	@Override
	public void run() {
		while (running) {
			synchronized (commandQueue) {
				Command c = commandQueue.peek();
				if (c != null) {
					c.setUsing(true);
					Log.d(TAG, "running command "+ Arrays.toString(c.getValue()));
					client.startUsingCommand(c);
					currentUsingCommand = c;
					while (c.isUsing()) {
						// checking is the command was in using per 10 ms
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					currentUsingCommand = null;
					commandQueue.poll();
				}
				
			}
			
			// if the current command turn to not-in-using, wait 50 ms and then go to next command.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void putCommand(Command c) {
		//if (!c.equals(commandQueue.peekLast())) {
//			Log.d("aaron", "putCommand "+ String.valueOf(c.getValue()));
			commandQueue.offer(c);
		//}
	}
	
	public void setCurrentCommandEnd(BluetoothGattCharacteristic c) {
		if (c != null && c.equals(currentUsingCommand.getCharacteristic())) {
//			Log.d(TAG, "stop command " + Arrays.toString(currentUsingCommand.getValue()));
			currentUsingCommand.setUsing(false);
		}
	}
	
	public void setStarting() {
//		Log.d(TAG, "setStarting");
		running = true;
	}
	
	public void setStopping() {
//		Log.d(TAG, "setStopping");
		running = false;
	}
	
	public interface CommandPoolControll {
    	public void startUsingCommand(Command c);
    }
}
