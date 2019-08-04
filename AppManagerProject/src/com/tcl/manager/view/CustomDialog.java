package com.tcl.manager.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tcl.framework.util.DeviceManager;
import com.tcl.mie.manager.R;

import org.w3c.dom.Text;

/**
 * @Description: 自定义对话框,提供给弹出确认框使用
 * @author wenchao.zhang
 * @date 2014年12月30日 下午8:35:25
 * @copyright TCL-MIE
 */


// 2015-01-12 by Jeff 修改为Metrail Design 样式
public class CustomDialog extends AlertDialog implements
        android.view.View.OnClickListener {

    public CustomDialog(Context context, String title, String msg,
            String confirmTips, final String leftButton, String rightButton,
            final View.OnClickListener leftListener,
            View.OnClickListener rightListener) {
        super(context);
        this.title = title;
        this.msg = msg;
        this.confirmTips = confirmTips;
        this.leftButton = leftButton;
        this.rightButton = rightButton;
        this.leftListener = leftListener;
        this.rightListener = rightListener;

        setCanceledOnTouchOutside(true);
    }

    private TextView tvTitle;
    private TextView tvRight;
    private TextView tvLeft;

    private TextView tvMsg;
    private TextView tvConfirmMsg;

    private String title;
    private String msg;
    private String confirmTips;
    private String leftButton;
    private String rightButton;
    private View.OnClickListener rightListener;
    private View.OnClickListener leftListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//      setContentView(R.layout.dialog_custom);
        

//      Window dialogWindow = this.getWindow();
//      DisplayMetrics dm = new DisplayMetrics();
//      getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
//      WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//      p.width = dm.widthPixels - DeviceManager.dip2px(getContext(), 50);
//      dialogWindow.setAttributes(p);
        
        setupView();
        setupData();
    }

    private void setupView() {
        View view = View.inflate(getContext(), R.layout.dialog_custom, null);
        tvRight = (TextView) view.findViewById(R.id.dialog_right);
        tvLeft = (TextView) view.findViewById(R.id.dialog_left);

        tvTitle = (TextView) view.findViewById(R.id.dialogTitle);
        tvMsg = (TextView) view.findViewById(R.id.dialog_message);
        tvConfirmMsg = (TextView) view.findViewById(R.id.dialog_tips);

        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
    }

    private void setupData() {
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
            tvTitle.setText("");
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        if (msg != null) {
            tvMsg.setText(msg);
        }
        if (confirmTips != null) {
            tvConfirmMsg.setText(confirmTips);
        } else {
            tvConfirmMsg.setText(R.string.dialog_default_confirm_again);
        }
        if (leftButton != null) {
            tvLeft.setText(leftButton);
        } else {
            tvLeft.setText(R.string.dialog_default_left_button);
        }

        if (rightButton != null) {
            tvRight.setText(rightButton);
        } else {
            tvRight.setText(R.string.dialog_default_right_button);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dialog_right:
            if (rightListener != null) {
                rightListener.onClick(v);
            }
            dismiss();
            break;
        case R.id.dialog_left:
            if (leftListener != null) {
                leftListener.onClick(v);
            }
            dismiss();
            break;

        default:
            break;
        }
    }

    @Override
    public void show() {
        if (!TextUtils.isEmpty(confirmTips))
            setTitle(confirmTips);

        if (TextUtils.isEmpty(leftButton)) {
            this.leftButton = getContext().getResources().getString(R.string.dialog_default_left_button);
        }
        if (TextUtils.isEmpty(rightButton)) {
            this.rightButton = getContext().getResources().getString(R.string.dialog_default_right_button);
        }
        setMessage(msg);
        setButton(DialogInterface.BUTTON_NEGATIVE, leftButton, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvLeft.performClick();
            }

        });
        setButton(DialogInterface.BUTTON_POSITIVE, rightButton, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvRight.performClick();
            }

        });

        super.show();
    }
}
