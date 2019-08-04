package com.tcl.manager.view;

import com.tcl.mie.manager.R;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @Description:自定义对话框
 * @author jiaquan.huang
 * @Date 2014-8-7
 * 
 */
public class RewriteDialog {

    public RewriteDialog() {

    }

    /**
     * 
     * @param context
     *            上下文对象
     * @param message
     *            提示信息
     * @param messageIsCenter
     *            提示信息是否居中
     * @param button1_text
     *            第一个按钮显示文字
     * @param button2_text
     *            第二个按钮显示文字
     * @param listener
     *            按钮点击事件
     * @return 对话框
     */
    public static Dialog alertDialog(Context context, String message, int gravity, String button1_text, String button2_text, final OnButtonClickListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_notice_dialog, null);

        final Button button1 = (Button) view.findViewById(R.id.dialog_button_ok);
        final Button button2 = (Button) view.findViewById(R.id.dialog_button_cancel);
        TextView text = (TextView) view.findViewById(R.id.dec_txt);

        final Dialog dialog = new Dialog(context, R.style.FullHeightDialog);
        dialog.setContentView(view);

        if (message != null) {
            text.setVisibility(View.VISIBLE);
            text.setText(Html.fromHtml(message));
            text.setGravity(gravity);
        }
        if (button1_text != null) {
            button1.setVisibility(View.VISIBLE);
            button1.setText(button1_text);
            button1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onButton1Clicked();
                    }
                }
            });
        }
        if (button2_text != null) {
            button2.setVisibility(View.VISIBLE);
            button2.setText(button2_text);
            button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onButton2Clicked();
                    }
                }
            });
        }
        dialog.show();
        return dialog;
    }

    public interface OnButtonClickListener {
        void onButton1Clicked();

        void onButton2Clicked();
    }

}
