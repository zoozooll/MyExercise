package com.tcl.manager.activity;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 父类 完全退出
 * 
 * @author hui.zhu
 * 
 */
public abstract class BaseActivity extends Activity {

    /**
     * 进入live运行为true, back退出fasle）
     */
    public static boolean isRun = false;
    private int exitValue = 1;
    protected Toast mToast;

    private long navigateTime = 0;
    private long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        exitValue = 1;
        isRun = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // 防止被主动停止服务 而发送不了命令
        /*
         * if (!OnetouchLiveService.isAlive) { startService(new Intent(this,
         * OnetouchLiveService.class)); }
         */
        ActivityManager.setTopActivity(this);

        startTime = System.currentTimeMillis();

        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        ActivityManager.setTopActivity(null);
        navigateTime += System.currentTimeMillis() - startTime;
        super.onPause();
    }

    public long getNavigateTime() {
        return navigateTime;
    }

    public void safeUnRegisterReceiver(BroadcastReceiver receiver) {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
        }
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                exitValue = 1;
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Toast 消息显示
     * 
     * @param text
     */
    public void showTip(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * Toast 消息显示
     * 
     * @param resId
     */
    public void showTip(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

}
