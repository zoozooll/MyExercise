package com.mogoo.market.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.mogoo.network.http.HttpUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * 查询任务，这里主要是用来发送消息后弹出一个进度对话框等待返回结果 ，点进度对话框的取消按钮后可以取消任务的执行。
 * 
 * @author fdl
 */
public class HttpSendAndRecvTask extends
		AsyncTask<HttpGetURIBuilder, Void, InputStream> {
	private static final boolean ISGZIP = true;
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private boolean mShowProgress;
	private IStreamRspProcessor mProcessor;
	private String mProgressMessage;
	private boolean mProgressCancelable;
	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			mProcessor.onError((Exception) msg.obj);
		}
	};

	/**
	 * 构造一个查询任务
	 * 
	 * @param showProgressDialog
	 *            是否显示进度对话框
	 */
	public HttpSendAndRecvTask(boolean showProgressDialog,
			IStreamRspProcessor processor, Context ctx) {
		super();
		mShowProgress = showProgressDialog;
		mContext = ctx;
		mProcessor = processor;
	}

	@Override
	protected InputStream doInBackground(HttpGetURIBuilder... params) {
		// return mContext.getClassLoader().getResourceAsStream("hot.xml");
		// AndroidHttpClient is not allowed to be used from the main thread
		System.out.println("lcq:" + params[0].generateURI());
		// HttpParams httpParameters = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);//
		// 设置连接超时时间为15秒
		// HttpConnectionParams.setSoTimeout(httpParameters, 15000);//
		// 设置socket超时时间为15秒
		// HttpClient client = new DefaultHttpClient(httpParameters);
		StringBuilder builder = new StringBuilder();
		//
		// HttpGet get = new HttpGet(params[0].generateURI());
		try {
			// HttpResponse response = client.execute(get);
			// StatusLine statusLine = response.getStatusLine();
			// if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
			// throw new IllegalAccessException("");
			// }
			InputStream is = HttpUtils.get(params[0].generateURI().toString(),
					ISGZIP);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			} // jsonArray = new JSONArray(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
			uiHandler.sendMessage(uiHandler.obtainMessage(0, e));
		}

		return new ByteArrayInputStream(builder.toString().getBytes());

	}

	@Override
	protected void onPreExecute() {
		if (mShowProgress) {
			mProgressDialog = new ProgressDialog(mContext);
			// 设置ProgressDialog 标题
			mProgressDialog.setTitle("");
			// 设置ProgressDialog 提示信息
			mProgressMessage = "loading...";
			mProgressDialog.setMessage(mProgressMessage);

			// 设置进度条风格，风格为圆形，旋转的
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			// 设置ProgressDialog 标题图标
			// progressDialog.setIcon(R.drawable.img1);

			// 设置ProgressDialog 的进度条是否不明确
			mProgressDialog.setIndeterminate(false);

			// 设置ProgressDialog 是否可以按退回按键取消
			mProgressDialog.setCancelable(true);

			// 设置ProgressDialog 的取消按钮
			if (mProgressCancelable) {
				mProgressDialog.setButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int i) {
								dialog.cancel();
								// 点击“确定按钮”取消对话框
								boolean canceled = cancel(true);
								Log.i("info", "QueryTask's result is "
										+ canceled);
							}
						});
			}
			mProgressDialog.show();
		}
	}

	/**
	 * 设置进度对话框的消息文字
	 * 
	 * @param text
	 */
	public void setProgressText(String text) {
		mProgressMessage = text;
	}

	/**
	 * 设置进度对话框是否可以被取消
	 * 
	 * @param cancelable
	 */
	public void setTaskCancelable(boolean cancelable) {
		mProgressCancelable = cancelable;
	}

	/**
	 * 子类覆盖此方法需要调用super.onPostExecute(result)
	 */
	@Override
	protected void onPostExecute(InputStream result) {
		if (mShowProgress && (null != mProgressDialog)) {
			mProgressDialog.dismiss();
		}
		if (result != null) {
			mProcessor.onQueryResulted(result);
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onCancelled() {
		mProcessor.onCancelled();
	}

}
