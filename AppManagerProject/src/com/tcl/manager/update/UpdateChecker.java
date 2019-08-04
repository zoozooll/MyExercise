package com.tcl.manager.update;

import java.io.File;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.update.UpdateManager.OnUpadateConfirmListener;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.ObjectSharedPreference;
import com.tcl.manager.view.UIHelper;
import com.tcl.mie.manager.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class UpdateChecker implements OnUpadateConfirmListener
{
	public final static String	VISION_INFO	= "vision_info";
	Context					mContext;
	Dialog					dialog;
	
	public UpdateChecker(Context context)
	{
		mContext = context;
	}
	
	public void check()
	{
		
		UpdateInfo info = new ObjectSharedPreference(mContext).get(VISION_INFO, UpdateInfo.class);
		
		if (info != null && info.versionCode > AndroidUtil.getVersionCode(mContext))
		{
			// 有新的包
			File file = new File(info.filePath);
			// 并且已经下载好了
			if (file.exists() && file.canRead())
			{
				// 可以弹框了
				show(info, false, null, null);
				return;
			}
		}
		
		checkVersion();
	}
	
	private void show(final UpdateInfo info, final boolean isConfirm, final String savePath, final String fileName)
	{
		/** 更新信息 **/
		StringBuffer message = new StringBuffer();
		message.append(mContext.getString(R.string.update_vision) + info.versionName);
		message.append("\n");
		message.append(mContext.getString(R.string.update_size) + info.size + "MB");
		message.append("\n");
		message.append("\n");
		message.append(mContext.getString(R.string.update_contents) + ":");
		message.append("\n");
		message.append(info.description);
		
		UIHelper.showCustomDialog(mContext, message.toString(), "", mContext.getString(R.string.update_cancle), mContext.getString(R.string.update_update_now), null, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!isConfirm)
				{
					Intent intent = AndroidUtil.getInstallIntent(mContext, new File(info.filePath));
					mContext.startActivity(intent);
				}
				else
				{
					UpdateManager.getInstance().download(savePath, fileName);
				}
			}
		});
	}
	
	private void checkVersion()
	{
		try
		{
			UpdateManager um = UpdateManager.getInstance();
			um.init(ManagerApplication.sApplication);
			if (AndroidUtil.isWifiConnect(mContext))
			{
				um.check(null);
			}
			else if (AndroidUtil.isNetConnect(mContext))
			{
				um.check(this);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onConfirm(UpdateInfo info, final String savePath, final String fileName)
	{ 
		show(info, true, savePath, fileName);
	}
}
