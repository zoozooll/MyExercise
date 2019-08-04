package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseFeedback;


public interface OnUploadAvatarKidListener {
	public void onUploadAvatarKidSuccess(ResponseFeedback r);
	public void onUploadAvatarKidFailure(ResponseBasic r);
}
