package com.beem.project.btf.ui.dialog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.ThreadUtils;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.vv.vv.utils.CToast;

/**
 * @ClassName: VVBaseLoadingDlg
 * @Description: VV取数据等待框
 * @author: yuedong bao
 * @date: 2014-12-17 上午10:43:47
 * @param <Result>
 */
public abstract class VVBaseLoadingDlg<Result> {
	private InnerAsyncTask asyncTask = new InnerAsyncTask();
	private boolean isTimeOut;
	private BeemServiceHelper helper;
	protected IXmppFacade mDlgXmppFacade;
	protected final VVBaseLoadingDlgCfg config;
	private StackTraceElement[] currentThread;
	private int curentIndex;
	private Handler handler = new Handler();

	/**
	 * @ClassName: VVBaseLoadingDlgCfg
	 * @Description:
	 * @author: yuedong bao
	 * @date: 2015-8-14 上午10:38:06
	 */
	public static class VVBaseLoadingDlgCfg {
		// 上下文:用于绑定service
		public Context context;
		// 是否绑定Service
		private boolean isBindXmpp = false;
		// 超时时间
		private long timeOut = 20 * 1000;
		// 最小加载时间
		private long leastLoadingtime = 0;
		// 是否显示等待界面
		private boolean isShowWaitingView = false;
		// 是否显示超时提示
		private boolean isShowTimeOutPromp = true;
		// 是否可取消等待框
		private boolean isCancelable = true;
		public Object[] params;

		public VVBaseLoadingDlgCfg(Context ctx) {
			this.context = ctx;
		}
		public boolean isBindXmpp() {
			return isBindXmpp;
		}
		public VVBaseLoadingDlgCfg setBindXmpp(boolean isBindXmpp) {
			this.isBindXmpp = isBindXmpp;
			return this;
		}
		public long getTimeOut() {
			return timeOut;
		}
		public VVBaseLoadingDlgCfg setParams(Object... params) {
			this.params = params;
			return this;
		}
		public VVBaseLoadingDlgCfg setTimeOut(long timeOut) {
			this.timeOut = timeOut;
			return this;
		}
		public boolean isShowWaitingView() {
			return isShowWaitingView;
		}
		public VVBaseLoadingDlgCfg setShowWaitingView(boolean isShowWaitingView) {
			this.isShowWaitingView = isShowWaitingView;
			return this;
		}
		public VVBaseLoadingDlgCfg setLeastLoadingtime(long leastLoadingtime) {
			this.leastLoadingtime = leastLoadingtime;
			return this;
		}
		public VVBaseLoadingDlgCfg setCancelable(Boolean isCancelable) {
			this.isCancelable = isCancelable;
			return this;
		}
		public VVBaseLoadingDlgCfg setShowTimeOutPromp(
				boolean isShowTimeOutPromp) {
			this.isShowTimeOutPromp = isShowTimeOutPromp;
			return this;
		}
		@Override
		public String toString() {
			return "VVBaseLoadingDlgCfg [context=" + context + ", isBindXmpp="
					+ isBindXmpp + ", timeOut=" + timeOut
					+ ", leastLoadingtime=" + leastLoadingtime
					+ ", isShowWaitingView=" + isShowWaitingView
					+ ", isShowTimeOutPromp=" + isShowTimeOutPromp
					+ ", isCancelable=" + isCancelable + "]";
		}
	}

	public VVBaseLoadingDlg(VVBaseLoadingDlgCfg config) {
		this.config = config;
	}
	protected void onCancelled() {
		UIHelper.hideDialogForLoading();
	}
	protected void timeOut() {
		//LogUtils.i("Loadign dialog time out:" + (System.currentTimeMillis() - asyncTask.startTime) + " "
		//				+ //LogUtils.getCallBackStr(currentThread, curentIndex, 100));
		if (config.isShowTimeOutPromp) {
			CToast.showToast(BeemApplication.getContext(), "获取数据超时",
					Toast.LENGTH_SHORT);
		}
		UIHelper.hideDialogForLoading();
		onTimeOut();
	}
	// 超时回调
	protected void onTimeOut() {
	};
	public void setManulaTimeOut(boolean isTimeOut) {
		this.isTimeOut = isTimeOut;
		handler.removeCallbacks(asyncTask.timeOutRunnable);
		handler.post(asyncTask.timeOutRunnable);
	}
	abstract protected Result doInBackground();
	public final void execute() {
		currentThread = Thread.currentThread().getStackTrace();
		curentIndex = LogUtils.getCallStackPos(currentThread);
		if (config.isBindXmpp) {
			/*helper = new BeemServiceHelper(config.context.getApplicationContext(),
					new BeemServiceHelper.IBeemServiceConnection() {
						@Override
						public void onServiceDisconnectAct(IXmppFacade xmppFacade, ComponentName name) {
							mDlgXmppFacade = xmppFacade;
						}
						@Override
						public void onServiceConnectAct(IXmppFacade xmppFacade, ComponentName name, IBinder service) {
							mDlgXmppFacade = xmppFacade;
						}
					});
			helper.bindBeemService();*/
			helper = BeemServiceHelper.getInstance(config.context
					.getApplicationContext());
			mDlgXmppFacade = helper.getXmppFacade();
			asyncTask.execute();
		} else {
			asyncTask.execute();
		}
	}
	protected void onPostExecute(Result result) {
	}
	// 重写此方法可以去掉等待框，自定义加载View
	protected void onPreExecute() {
	}

	private class InnerAsyncTask extends AsyncTask<Object, Integer, Result> {
		protected long startTime;
		private Runnable timeOutRunnable;

		@Override
		protected Result doInBackground(Object... params) {
			sendTimeOutMsg(config.timeOut);
			startTime = System.currentTimeMillis();
			if (config.isBindXmpp) {
				try {
					if (!mDlgXmppFacade.isAuthentificated()) {
						helper.xmppLoginSilently();
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			Result result = VVBaseLoadingDlg.this.doInBackground();
			long sleepTime = config.leastLoadingtime
					- (System.currentTimeMillis() - startTime);
			if (sleepTime > 0) {
				// 停止一段时间
				//LogUtils.i(" sleep cost time(ms):" + (sleepTime) + " ms.");
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(final Result result) {
			if (!isTimeOut) {
				super.onPostExecute(result);
				handler.removeCallbacks(timeOutRunnable);
				onPostExecuteInner(result);
			} else {
				//LogUtils.i("this should not be happened");
			}
		}
		private void onPostExecuteInner(Result result) {
			if (System.currentTimeMillis() - startTime > 1500) {
				//LogUtils.i(" dialog cost time (ms) too much:" + (System.currentTimeMillis() - startTime) + " "
				//						+ //LogUtils.getCallBackStr(currentThread, curentIndex, 100));
			}
			if (config.isShowWaitingView) {
				UIHelper.hideDialogForLoading();
			}
			VVBaseLoadingDlg.this.onPostExecute(result);
			/*if (config.isBindXmpp) {
				helper.unBindBeemSerivice();
			}*/
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (config.isShowWaitingView) {
				UIHelper.showDialogForLoading(config.context, "请稍候",
						config.isCancelable);
			}
			VVBaseLoadingDlg.this.onPreExecute();
		}
		private void sendTimeOutMsg(long delayMillis) {
			timeOutRunnable = new Runnable() {
				@Override
				public void run() {
					if (asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
						isTimeOut = true;
						asyncTask.cancel(false);
					}
					timeOut();
				}
			};
//			handler.postDelayed(timeOutRunnable, delayMillis);
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (!isTimeOut) {
				VVBaseLoadingDlg.this.onCancelled();
			} else {
				handler.removeCallbacks(timeOutRunnable);
			}
		}
	}

	/**
	 * @Title: isCancled
	 * @Description: 异步任务是否取消
	 * @param: @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean isCancled() {
		return asyncTask.isCancelled();
	}
	public void cancel(boolean mayInterruptIfRunning) {
		asyncTask.cancel(mayInterruptIfRunning);
	}
}
