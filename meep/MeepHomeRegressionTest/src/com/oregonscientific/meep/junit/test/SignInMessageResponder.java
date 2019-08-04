package com.oregonscientific.meep.junit.test;

import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;

public class SignInMessageResponder extends MessageResponder {

	public SignInMessageResponder() {
		super(new MessageFilter(Message.PROCESS_ACCOUNT, Message.OPERATION_CODE_SIGN_IN));
	}

	@Override
	public String onRespond(Message message) {
		// TODO Auto-generated method stub
	
		String json = "{\"status\": \"Authenticated successfully.\", \"received\": true, \"first_name\": \"" +
				TestData.TEST_USER_NAME + 
				"\", \"code\": 200, \"opcode\": \"sign-in\", \"userid\": \"50e5825c04f77a1c945f0354\", \"meeptag\": \"mikael#8\", \"requestid\": 1, \"messageid\": \"" +
				message.getMessageID() + "\", \"is_guest\": false, \"proc\": \"account\"}";

		return json;

	}

	
}
