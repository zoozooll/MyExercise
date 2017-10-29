package com.iskyinfor.duoduo.ui.usercenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.pojo.User;
import com.iskinfor.servicedata.usercenter.service.IManagerUserInfor0300030001;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskinfor.servicedata.usercenter.serviceimpl.ManagerUserInfor0300030001Impl;
import com.iskinfor.servicedata.usercenter.serviceimpl.QuerryUserInfor0300020001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UIPublicConstant;
import com.iskyinfor.duoduo.ui.UiHelp;

public class UserInfoTask extends AsyncTask<Void, Integer, Void> {
	private Context mContext = null;
	private IManagerUserInfor0300030001 managerUserInfo;
	private IQuerryUserInfor0300020001 queryUserInfo;
	private boolean flag; // 修改密码的标示
	private boolean payFlag; // 支付的标示
	private boolean payStatus;// 状态
	private String userId, oldPassword, newPassword;
	private String accountId, accountPswd;
	private String blance = null; // 得到余额
	private int marked = 0;
	private Handler mHandler;
	User userInfo = null;

	// 查询用户
	public UserInfoTask(Context con, String userid, int signMarked,
			Handler handler) {
		mContext = con;
		userId = userid;
		marked = signMarked;
		mHandler = handler;
	}

	// 修改密码
	public UserInfoTask(Context con, String userid, String oldPswd,
			String newPswd, int signMarked) {
		mContext = con;
		userId = userid;
		oldPassword = oldPswd;
		newPassword = newPswd;
		marked = signMarked;
	}

	// 充值操作
	public UserInfoTask(Context con, String account, String password,
			boolean bool, int signMarked ,Handler handler) {
		mContext = con;
		accountId = account;
		accountPswd = password;
		payStatus = bool;
		marked = signMarked;
		mHandler = handler;
	}

	/**
	 * 预处理任务
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		queryUserInfo = new QuerryUserInfor0300020001Impl(); // 查询用户信息
		managerUserInfo = new ManagerUserInfor0300030001Impl();
	}

	/**
	 * 后台处理任务
	 */
	@Override
	protected Void doInBackground(Void... params) {
		String userid = mContext.getSharedPreferences(
				UIPublicConstant.UserInfo,
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE)
				.getString("account", "");

		try {
			if (marked == UIPublicConstant.UPADATE_PSWD_MARK) {
				// 修改密码
				flag = managerUserInfo.modifyPassword(userId, oldPassword,newPassword);
			
			} else if (marked == UIPublicConstant.PAYFOR_MONEY_MARK) {
				
				if (payStatus)
				{
					// 为别人充值
					managerUserInfo.rechargeOther(userid, "", accountId);
				}
				else 
				{
					// 为自己充值
					payFlag = managerUserInfo.rechargeSelf(userid, accountPswd);
				}

				// 得到余额
				blance = queryUserInfo.getBalance(userid);
				Message message = new Message();
				message.what = UIPublicConstant.USER_BALANCE;
				message.obj = blance;
				mHandler.sendMessage(message);
				
				Log.i("blance == >>>>>>>>>>>>", "得到余额是："+blance);
				
			} else if (marked == UIPublicConstant.QUERY_ALLUSER_MARK) {

				// 查询当前用户的所有信息
				userInfo = queryUserInfo.querryUserFullInfor(userid, null);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 取消任务
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	/**
	 * 任务中
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * 任务完成处理
	 */
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if (marked == UIPublicConstant.UPADATE_PSWD_MARK) {

			// 密码修改
			if (flag == true) {
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
						R.string.update_pw_success), Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
						R.string.update_pw_fail), Toast.LENGTH_SHORT)
						.show();
			}

		} else if (marked == UIPublicConstant.PAYFOR_MONEY_MARK) {

			// 支付
			if (payFlag == true) 
			{
				paySuccessDialog(mContext);
			}
			else
			{
				payFailDailog(mContext);
			}
		} else if (marked == UIPublicConstant.QUERY_ALLUSER_MARK) {
			// 查询用户
			sendMessage();
		}
	}

	// 发送消息
	private void sendMessage() {
		if (!userInfo.equals("") || userInfo != null) {
			Message message = new Message();
			message.obj = userInfo;
			message.what = UIPublicConstant.USERCENTER_BASERES;
			mHandler.sendMessage(message);

//			Log.i("yyj", "userInfo >>>>>userInfo>>>>>==" + userInfo);
		} else {
			Toast.makeText(mContext, "Faile ....", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 充值失败操作
	 * 
	 * @param context
	 */
	private void payFailDailog(Context context) {
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("充值失败")
				.setIcon(R.drawable.all).setMessage("无效的充值卡号！")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(mContext, "充值失败", 100).show();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		dialog.show();

	}

	/**
	 * 充值成功操作
	 * 
	 * @param context
	 */
	private void paySuccessDialog(Context context) {

		String message = "账号" + UiHelp.getUserShareID(context) + "已经充值成功";

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("充值成功")
				.setIcon(R.drawable.icon).setMessage(message)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(mContext, "充值完成", 100).show();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		dialog.show();
	}
}