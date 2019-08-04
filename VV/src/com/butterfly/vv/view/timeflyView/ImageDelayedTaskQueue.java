package com.butterfly.vv.view.timeflyView;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.butterfly.vv.db.ImageDelayedTask;

public class ImageDelayedTaskQueue {
	BlockingQueue<ImageDelayedTask> blockingQueue = new LinkedBlockingQueue<ImageDelayedTask>();

	public void push(ImageDelayedTask imageDelayedTask)
			throws InterruptedException {
		blockingQueue.put(imageDelayedTask);
	}
	public ImageDelayedTask pop() throws InterruptedException {
		return blockingQueue.take();
	}
}
