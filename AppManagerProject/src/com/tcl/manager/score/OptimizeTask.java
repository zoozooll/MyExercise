package com.tcl.manager.score;

import com.tcl.manager.activity.entity.OptimizeChildItem;

/** 
 * @Description: 优化任务
 * @author wenchao.zhang 
 * @date 2015年1月3日 下午8:37:01 
 * @copyright TCL-MIE
 */

public class OptimizeTask implements Runnable {
	private int type;
	
	private String pkgName;
	

	public OptimizeTask(int type, String pkgName) {
		super();
		this.type = type;
		this.pkgName = pkgName;
	}
	/**
	 * 启动
	 */
	public void start(){
		new Thread(this).start();
	}


	@Override
	public void run() {
		
		switch (type) {
		case OptimizeChildItem.TYPE_BATTERY://电量
			PageFunctionProvider.setAutoStart(pkgName, false);
			break;
		case OptimizeChildItem.TYPE_DATA://流量
			PageFunctionProvider.setDataAccess(pkgName, false);
			break;
		default:
			break;
		}
		
	}

}
