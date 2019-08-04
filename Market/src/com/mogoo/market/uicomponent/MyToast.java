package com.mogoo.market.uicomponent;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mogoo.market.R;

/**
 * 
 * @author fdl
 *
 */
public class MyToast extends Toast {

	public MyToast(Context context) {
		super(context);
		setView(createMyToastView(context));
	}
	
	public static Toast makeText(Context context, CharSequence text, int duration)
	{
		Toast toast = Toast.makeText(context, text, duration);
		View toastview = createMyToastView(context);
		TextView tv = (TextView) toastview.findViewById(R.id.toast_tip);
		tv.setText(text);
		toast.setView(toastview);
		return toast;
	}
	

	public static Toast makeText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		Toast toast = makeText(context, context.getResources().getText(resId),
				duration);
		View toastview = createMyToastView(context);;
		TextView tv = (TextView) toastview.findViewById(R.id.toast_tip);
		tv.setText(resId);
		toast.setView(toastview);
		return toast;
	}
	
	private static View createMyToastView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View toastView = inflater.inflate(R.layout.common_toast, null);
		return toastView;
	}

}
