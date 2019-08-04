/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teleca.jamendo.dialog;

import com.teleca.jamendo.api.WSError;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Wrapper around UserTask & ProgressDialog
 * @author Lukasz Wisniewski
 */
public abstract class LoadingDialog<Input, Result> extends
		AsyncTask<Input, WSError, Result> {
	// private ProgressDialog mProgressDialog;
	protected Activity activity;
	private String mLoadingMsg;
	private String mFailMsg;

	public LoadingDialog(Activity activity, String loadingMsg, String failMsg) {
		this.activity = activity;
		this.mLoadingMsg = loadingMsg;
		this.mFailMsg = failMsg;
	}
	public LoadingDialog(Activity activity, String... params) {
		this.activity = activity;
		this.mLoadingMsg = "任务执行中...";
		this.mFailMsg = "任务执行失败!";
		if (params.length > 1) {
			this.mLoadingMsg = params[0];
			this.mFailMsg = params[1];
		}
	}
	@Override
	public void onCancelled() {
		failMsg();
		super.onCancelled();
	}
	@Override
	public void onPreExecute() {
	}
	@Override
	public abstract Result doInBackground(Input... params);
	@Override
	public void onPostExecute(Result result) {
		super.onPostExecute(result);
		// mProgressDialog.dismiss();
		doStuffWithResult(result);
		if (result == null) {
			failMsg();
		}
	}
	protected void failMsg() {
		Toast.makeText(activity, mFailMsg, 2000).show();
	}
	/**
	 * Very abstract function hopefully very meaningful name, executed when result is other than
	 * null
	 * @param result
	 * @return
	 */
	public abstract void doStuffWithResult(Result result);
	@Override
	protected void onProgressUpdate(WSError... values) {
		// Toast.makeText(mActivity, values[0].getMessage(),
		// Toast.LENGTH_LONG).show();
		// this.cancel(true);
		// // mProgressDialog.dismiss();
		// super.onProgressUpdate(values);
	}
	public void doCancel() {
		// mProgressDialog.dismiss();
	}
}
