package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseFeedback;


public interface OnUploadAvatarParentListener {
	public void onUploadAvatarParentSuccess(ResponseFeedback r);
	public void onUploadAvatarParentFailure(ResponseBasic r);
}
