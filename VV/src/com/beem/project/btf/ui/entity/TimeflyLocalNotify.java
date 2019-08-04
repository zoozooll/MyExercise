/**
 * 
 */
package com.beem.project.btf.ui.entity;

import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.service.TimeflyService.Valid;

/**
 * @author hongbo ke
 *
 */
public class TimeflyLocalNotify extends EventBusData {
	
	public TimeflyLocalNotify(ImageFolder folder, String time, Valid valid) {
		super(EventAction.TimeflyAlertLocal, folder);
		this.folder = folder;
		this.time = time;
		this.valid = valid;
	}

	public ImageFolder folder;
	
	public String time;
	
	public Valid valid;
}
