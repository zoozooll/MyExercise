
package com.iskyinfor.duoduo.downloadManage.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.iskyinfor.duoduo.downloadManage.Constants;

/**
 * @author pKF29007
 */
public abstract class DownloadBroadcastReceiverTemplate extends BroadcastReceiver {

    public DownloadBroadcastReceiverTemplate() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Bundle bundle = intent
                .getBundleExtra(Constants.INTENT_BROADCAST_DOWNLOADTASK_FLAG);

        DownloadBundle item = (DownloadBundle) bundle
                .getSerializable(Constants.TAG_DOWNLOADBUNDLE);

        if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_FINISH)) {
            this.OnDonwloadFinish(item);
        } else if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_PAUSE)) {
            this.OnDonwloadPause(item);
        } else if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_ERROR)) {
            this.OnDonwloadError(item);
        } else if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_RUNNING)) {
            this.OnDonwloadRunning(item);
        } else if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_WAIT)) {
            this.OnDonwloadWait(item);
        } else if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_READYSTART)) {
            this.OnDonwloadReadyStart(item);
        } else if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_CANCEL)) {
            this.OnDonwloadCancel(item);
        } else if (action.equals(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_FAIL)) {
            this.OnDonwloadFail(item);
        }

    }

    public abstract void OnDonwloadFinish(DownloadBundle item);

    public abstract void OnDonwloadPause(DownloadBundle item);

    public abstract void OnDonwloadError(DownloadBundle item);

    public abstract void OnDonwloadRunning(DownloadBundle item);

    public abstract void OnDonwloadReadyStart(DownloadBundle item);

    public abstract void OnDonwloadWait(DownloadBundle item);

    public abstract void OnDonwloadCancel(DownloadBundle item);

    public abstract void OnDonwloadFail(DownloadBundle item);
}
