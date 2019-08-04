/**
 * 
 */
package com.beem.project.btf.receiver;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.ShareRankingActivity;
import com.beem.project.btf.ui.activity.StartActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.TimeflyDueRemindView;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.service.TimeflyService.Valid;

import de.greenrobot.event.EventBus;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

/**
 * @author hongbo ke
 *
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, Intent intent) {
		final ImageFolder folder = (ImageFolder) intent.getSerializableExtra("folder");
		final String time = intent.getStringExtra("time");
		final BBSCustomerDialog blurDlg = BBSCustomerDialog
				.newInstance(context, R.style.blurdialog);
		TimeflyDueRemindView remindview = new TimeflyDueRemindView(
				context, true);
		remindview.setText(time);
		remindview.setBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				blurDlg.dismiss();
				Intent i = new Intent(context, ShareRankingActivity.class);
				i.putExtra("jid", folder.getJid());
				i.putExtra("gid", folder.getGid());
				i.putExtra("gidCreatTime", folder.getCreateTime());
				context.startActivity(i);
				/*ShareRankingActivity.launch(context, LoginManager
						.getInstance().getJidParsed(), folder.getGid(),
						folder.getCreateTime());*/
				ImageFolderNotify notifyDB = new ImageFolderNotify();
				notifyDB.setField(DBKey.jid, LoginManager.getInstance()
						.getJidParsed());
				notifyDB.setField(DBKey.gid, folder.getGid());
				notifyDB.setField(DBKey.createTime,
						folder.getCreateTime());
				notifyDB.setField(DBKey.notify_valid, Valid.close.val);
				notifyDB.saveToDatabaseAsync();
				// 更新界面数据
				EventBusData data = new EventBusData(
						EventAction.CheckTimeflyNotify,
						new Object[] { folder.getCreateTime(),
								Valid.close.val });
				EventBus.getDefault().post(data);
			}
		});
		blurDlg.setContentView(remindview.getmView());
		blurDlg.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		blurDlg.setCancelable(true);
		blurDlg.show();
    }
}
