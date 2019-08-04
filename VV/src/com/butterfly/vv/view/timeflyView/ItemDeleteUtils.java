package com.butterfly.vv.view.timeflyView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.butterfly.vv.db.ImageDelayedTask;
import com.butterfly.vv.view.grid.ImageDelayedGridAdapter;

import android.os.Handler;

public class ItemDeleteUtils {
	ExecutorService executorService = Executors.newFixedThreadPool(5);
	CompleteCallBack callBack;
	ImageDelayedGridAdapter delayedGridAdapter;
	ImageDelayedTaskQueue delayedTaskQueue = new ImageDelayedTaskQueue();
	Handler handler = new Handler();

	public ItemDeleteUtils(ImageDelayedGridAdapter delayedGridAdapter) {
		super();
		this.delayedGridAdapter = delayedGridAdapter;
	}

	interface CompleteCallBack {
		void updateView(int pos);
	}

	public void setCallBack(CompleteCallBack callBack) {
		this.callBack = callBack;
	}
	public void uploadImage(int pos) throws InterruptedException {
		// ImageDelayedTask delayedTask = (ImageDelayedTask)
		// delayedGridAdapter.getItem(pos);
		// delayedTaskQueue.push(delayedTask);
		// executorService.submit( new
		// DeleteItemRunnable(delayedTaskQueue,pos));
	}
	public void deleteOneImage(int pos) throws InterruptedException {
		ImageDelayedTask delayedTask = (ImageDelayedTask) delayedGridAdapter
				.getItem(pos);
		delayedTaskQueue.push(delayedTask);
		executorService.submit(new DeleteItemRunnable(delayedTaskQueue, pos));
		Thread.sleep(500);
	}
	public void deleteOneImage(ImageDelayedTask delayedTask)
			throws InterruptedException {
		delayedTaskQueue.push(delayedTask);
		executorService.submit(new DeleteItemRunnableTask(delayedTaskQueue));
		Thread.sleep(500);
	}
	public void deleteAllImage(ArrayList<ImageDelayedTask> arrayList)
			throws InterruptedException {
		executorService.submit(new DeleteAllItemRunnable(arrayList));
	}

	class DeleteItemRunnable implements Runnable {
		ImageDelayedTaskQueue queue;
		int pos;

		public DeleteItemRunnable(ImageDelayedTaskQueue imageDelayedTaskQueue,
				int pos) {
			queue = imageDelayedTaskQueue;
			this.pos = pos;
		}
		@Override
		public void run() {
			try {
				ImageDelayedTask delayedTask = queue.pop();
				String url = delayedTask.getUri();
				// pos = delayedTask.pos;
				// handler.sendEmptyMessage(pos);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					delayedGridAdapter.getValues().remove(pos);
					delayedGridAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	class DeleteItemRunnableTask implements Runnable {
		ImageDelayedTaskQueue queue;
		int pos;

		public DeleteItemRunnableTask(
				ImageDelayedTaskQueue imageDelayedTaskQueue) {
			queue = imageDelayedTaskQueue;
		}
		@Override
		public void run() {
			try {
				ImageDelayedTask delayedTask = queue.pop();
				String url = delayedTask.getUri();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class DeleteAllItemRunnable implements Runnable {
		private ArrayList<ImageDelayedTask> tasks;

		public DeleteAllItemRunnable(ArrayList<ImageDelayedTask> arrayList) {
			tasks = arrayList;
		}
		@Override
		public void run() {
			try {
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					delayedGridAdapter.getValues().clear();
					delayedGridAdapter.notifyDataSetChanged();
				}
			});
		}
	}
}
