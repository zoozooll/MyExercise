package com.iskyinfor.duoduo.ui.shop.task;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.pojo.Product;

public class AddToShellCtrler {
	
	private static final int DELAY_TIME =  10 * 60 * 1000;

	/**
	 * 加入书架方法。当产品没有加入书架时候，本方法定时执行加入书架的方法
	 * @param opearter 产品接口
	 * @param userId 用户名
	 * @param products 产品列表
	 * @return
	 * @Author Aaron Lee
	 * @date 2011-7-14 上午10:04:29
	 */
	public static boolean addTOShell(final IOperaterProduct0200030001 opearter,
			final String userId, final ArrayList<Product> products) {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			public void run() {
				try {
					if (opearter.putBuyedProducetToShelf(userId, products)) {
						timer.cancel();
					}
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		timer.schedule(task, 0, DELAY_TIME );
		return false;
	}
}
