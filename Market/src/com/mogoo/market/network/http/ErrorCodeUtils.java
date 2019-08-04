package com.mogoo.market.network.http;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.mogoo.market.R;
import com.mogoo.market.uicomponent.MyToast;

public class ErrorCodeUtils {
	/**
	 * 根据服务器返回的错误代码显示错误信息
	 * 
	 * @author 张永辉
	 * @date 2011-10-27
	 * @param errorCode
	 */
	public static void showErrorToast(Context context, String errorCode) {
		String tip = null;
		Resources res = context.getResources();

		int code = ErrorCode.ERROR_UNKNOW;

		try {
			code = Integer.parseInt(errorCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

		switch (code) {
		case ErrorCode.SUCCESS:
			return;
		case ErrorCode.ERROR_AKEY_WRONG:
			tip = res.getString(R.string.tip_error_akey_wrong);
			break;
		case ErrorCode.ERROR_ARGS_MISMATCH:
			tip = res.getString(R.string.tip_error_args_mismatch);
			break;
		case ErrorCode.ERROR_SESSION_INVALID:
			tip = res.getString(R.string.tip_error_session_invalid);
			break;
		case ErrorCode.ERROR_AID_INVALID:
			tip = res.getString(R.string.tip_error_aid_invalid);
			break;
		case ErrorCode.ERROR_ATTENTION_EXIST:
			tip = res.getString(R.string.tip_error_attention_exist);
			break;
		case ErrorCode.ERROR_ATTENTION_FAIL:
			tip = res.getString(R.string.tip_error_attention_fail);
			break;
		case ErrorCode.ERROR_ATTENTION_NULL:
			tip = res.getString(R.string.tip_error_attention_null);
			break;
		case ErrorCode.ERROR_SEX_EMPTY:
			tip = res.getString(R.string.tip_error_sex_empty);
			break;
		case ErrorCode.ERROR_SYSTEM_WRONG:
			tip = res.getString(R.string.tip_error_system);
			break;
		case ErrorCode.ERROR_BIRTHDAY_EMPTY:
			tip = res.getString(R.string.tip_error_birthday_empty);
			break;
		case ErrorCode.ERROR_NAME_EMPTY:
			tip = res.getString(R.string.tip_error_username_empty);
			break;

		// 蘑菇商城
		case ErrorCode.ERROR_APP_EMPTY:
			tip = res.getString(R.string.tip_app_empty);
			break;
		// 评分时间为空
		case ErrorCode.ERROR_GRADE_TIME_EMPTY:
			tip = res.getString(R.string.tip_grade_time_empty);
			break;
		// 评分时间为空
		case ErrorCode.ERROR_GRADE_EMPTY:
			tip = res.getString(R.string.tip_grade_empty);
			break;
		default:
			tip = res.getString(R.string.tip_error_default);
			break;

		}

		if (tip != null) {
			//Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
                          MyToast.makeText(context, tip, MyToast.LENGTH_SHORT).show();
		}
	}
}
