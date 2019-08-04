package com.beem.project.btf.service.aidl;
import  com.beem.project.btf.service.aidl.IChat;
import  com.beem.project.btf.service.Message;
interface onNewChatListener {
	//
	void notifyNewChat(in IChat chat, in com.beem.project.btf.service.Message message);
}