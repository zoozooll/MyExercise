package com.beem.project.btf.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.utils.AppFileDownUtils;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.service.CommonService;
import com.teleca.jamendo.api.WSError;

public class UpdateManager {
	public static UpdateMessage update(String url) throws WSError,
			JSONException {
		String update = CommonService.update(url);
		JSONObject jsonObject = new JSONObject(update);
		UpdateMessage msg = new UpdateMessage();
		msg.setVersion(jsonObject.getString("version"));
		msg.setUrl(jsonObject.getString("url"));
		msg.setInfo(jsonObject.getString("info"));
		msg.setForce(jsonObject.getString("force"));
		return msg;
	}
	public static void showUpdateDialog(final Context context,
			final String url, final int flag, final String content) {
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				context, R.style.blurdialog);
		SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(context);
		dilaogView.setTitle("发现新版本");
		TextView textView = new TextView(context);
		textView.setText(content);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		textView.setPadding((int) BBSUtils.toPixel(context,
				TypedValue.COMPLEX_UNIT_DIP, 10), 0, (int) BBSUtils.toPixel(
				context, TypedValue.COMPLEX_UNIT_DIP, 10), 0);
		dilaogView.setContentView(textView);
		dilaogView.setMargin();
		if (flag == Constants.SHOW_FORCEUPDATE) {
			blurDlg.setCancelable(false);
		}
		dilaogView.setPositiveButton("确定", new BtnListener() {
			@Override
			public void ensure(View contentView) {
				new AppFileDownUtils(context, url,
						AppProperty.getInstance().UPDATE_FILENAME).start();
				blurDlg.dismiss();
			}
		});
		dilaogView.setNegativeButton("取消", new BtnListener() {
			@Override
			public void ensure(View contentView) {
				if (flag == Constants.SHOW_CHOOSEUPDATE) {
					blurDlg.dismiss();
				} else {
					blurDlg.dismiss();
					ActivityController.getInstance().finishAllActivity();
				}
			}
		});
		blurDlg.setContentView(dilaogView.getmView());
		blurDlg.show();
	}
	/*// 安装
	public static void install(Context context, String saveFile) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(new File(saveFile)), "application/vnd.android.package-archive");
		((Activity) context).startActivityForResult(intent, 0);
		// 升级删除Cartoon文件
		DataCleanManager.deleteFolderFile(new File(BeemApplication.getContext().getFilesDir(), "Cartoon").getPath(),
				true);
	}*/
}
