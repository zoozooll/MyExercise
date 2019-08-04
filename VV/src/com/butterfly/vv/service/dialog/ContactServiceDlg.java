package com.butterfly.vv.service.dialog;

import java.util.List;

import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.EditNameDialogView;
import com.beem.project.btf.bbs.view.EditNameDialogView.EditNameDialogType;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.btf.push.AddRosterPacket.Operation;
import com.btf.push.NeighborHoodPacket.NeighborHoodType;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.vv.utils.CToast;
import com.teleca.jamendo.api.WSError;

/**
 * @ClassName: ContactServiceDlg
 * @Description:
 * @author: yuedong bao
 * @date: 2015-4-3 上午10:50:37
 */
public class ContactServiceDlg {
	protected static final String tag = ContactServiceDlg.class.getSimpleName();

	private ContactServiceDlg() {
	};
	// 弹出添加好友对话框
	public static void showAddContactDlg(final Context context,
			final Contact c, final View toggleView) {
		if (LoginManager.getInstance().getJidParsed().equals(c.getJIDParsed())) {
			CToast.showToast(context, "别闹，不能加自己为好友！", Toast.LENGTH_SHORT);
			return;
		}
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				context, R.style.blurdialog);
		EditNameDialogView editNameView = new EditNameDialogView(context,
				EditNameDialogType.addfriend);
		editNameView.setForText(c.getNickName());
		editNameView.setHint("请输入验证消息");
		editNameView.setText("加个好友呗！");
		SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(context);
		dilaogView.setTitle("添加好友 :");
		dilaogView.setContentView(editNameView.getView());
		dilaogView.setBottomMargin();
		dilaogView.setPositiveButton("申请", new BtnListener() {
			@Override
			public void ensure(View contentView) {
				String content = ((EditText) contentView
						.findViewById(R.id.nickNameEdit)).getText().toString();
				ContactServiceDlg.executeContactOprt(context, c.getJid(),
						content, Operation.require, null);
				blurDlg.dismiss();
			}
		});
		blurDlg.setContentView(dilaogView.getmView());
		blurDlg.show();
	}
	// 执行添加好友操作
	public static void executeContactOprt(final Context mContext,
			final String jid, final String content, final Operation operation,
			final ContactService.onPacketResult<PacketResult> resultPactLis) {
		new VVBaseLoadingDlg<PacketResult>(
				new VVBaseLoadingDlgCfg(mContext).setShowWaitingView(true)) {
			@Override
			protected PacketResult doInBackground() {
				return ContactService.getInstance().addFriend(jid, content,
						operation.toString());
			}
			@Override
			protected void onPostExecute(PacketResult result) {
				super.onPreExecute();
				if (resultPactLis != null) {
					resultPactLis.onResult(result, false, null);
				} else {
					if (result.isOk()) {
					} else {
						CToast.showToast(mContext, "请求发送失败", Toast.LENGTH_SHORT);
					}
				}
			}
			@Override
			protected void onTimeOut() {
				if (resultPactLis != null) {
					resultPactLis.onResult(null, true, null);
				}
			}
		}.execute();
	}
	// 一个创建对话框的方法
	public static void CreateTipdialog(Context mContext, String content,
			final OnClickListener... lis) {
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				mContext, R.style.blurdialog);
		// 简单标示内容填充
		SimpleDilaogView simpleDilaogView = new SimpleDilaogView(mContext);
		// 设置标题
		simpleDilaogView.setTitle(content);
		// 定义按钮功能
		for (int i = 0; i < lis.length; i++) {
			if (i == 0) {
				simpleDilaogView.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						blurDlg.dismiss();
						lis[0].onClick(v);
					}
				});
			}
			if (i == 1) {
				simpleDilaogView.setPositiveButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						blurDlg.dismiss();
						lis[1].onClick(v);
					}
				});
			}
		}
		blurDlg.setContentView(simpleDilaogView.getmView());
		blurDlg.show();
	}
	public static void executeGetNeighborHood(Context mContext,
			final NeighborHoodType neighborType, final Start start,
			final int limit,
			final ContactService.onPacketResult<List<Contact>> rstLis) {
		new VVBaseLoadingDlg<List<Contact>>(new VVBaseLoadingDlgCfg(mContext)) {
			@Override
			protected List<Contact> doInBackground() {
				List<Contact> retVal = null;
				try {
					retVal = ContactService.getInstance()
							.getNeighborHoodList(BDLocator.getInstance().getLon(),
									BDLocator.getInstance().getLat(), neighborType,
									start, String.valueOf(limit));
				} catch (XMPPException e) {
					e.printStackTrace();
					setManulaTimeOut(true);
				} catch (WSError e) {
					e.printStackTrace();
					setManulaTimeOut(true);
				}
				return retVal;
			}
			@Override
			protected void onPostExecute(List<Contact> result) {
				if (rstLis != null) {
					rstLis.onResult(result, false, start);
				}
			}
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				//				ContactService.getInstance().synGeoInfo();
			}
			@Override
			protected void onTimeOut() {
				if (rstLis != null) {
					rstLis.onResult(null, true, start);
				}
			}
		}.execute();
	}
}
