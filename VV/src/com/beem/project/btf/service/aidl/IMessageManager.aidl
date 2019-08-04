/**
* vv定制：消息管理类
*/
package com.beem.project.btf.service.aidl;
import com.btf.push.Item;
interface IMessageManager {
	//获取所有消息
	List<Item> getAllMessages();
}