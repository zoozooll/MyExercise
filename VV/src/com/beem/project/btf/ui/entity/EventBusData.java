package com.beem.project.btf.ui.entity;

/**
 * @ClassName: VVEventBus
 * @Description: VV定制的EventBus
 * @author: yuedong bao
 * @date: 2015-5-13 下午7:57:25
 * @use:使用示例： 发送：VVEventBus.getDefaut.post(EventBusData data);
 *            接受：实现IEventBusAction中onEventMainThread方法
 */
public class EventBusData {
	private EventAction action;
	private Object msg;
	private Object msg1;

	public EventBusData() {
		super();
	}
	public EventBusData(EventAction action, Object msg) {
		super();
		this.action = action;
		this.msg = msg;
	}
	public EventBusData(EventAction action, Object msg, Object msg1) {
		super();
		this.action = action;
		this.msg = msg;
		this.msg1 = msg1;
	}
	public EventAction getAction() {
		return action;
	}
	public void setAction(EventAction action) {
		this.action = action;
	}
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	public Object getMsgList() {
		return msg1;
	}
	public void setMsgList(Object msg1) {
		this.msg1 = msg1;
	}

	public enum EventAction {
		//登录超时
		LOGIN_TIMEOUT,
		//登录失败
		LOGIN_FAILED,
		//登录成功
		LOGIN_SUCCESS,
		//退出登录
		LOGOUT,
		//网络连接状态改变
		NETWORK_ACTIVE,
		//备注修改
		AliasChange,
		//个人资料修改操作
		ModifyContactInfo,
		//素材管理图片添加操作
		ImageAdd,
		//素材管理图片删除操作
		ImageDelete,
		//时光模块图片删除操作
		TimeflyImageDelete,
		//时光相机背景图片文字附加信息，选择修改操作
		TimeCameraNoteTextChange,
		//分享点赞更改
		ShareSupportChange,
		//分享评论修改
		ShareCommentChange,
		//上传时光图片增加
		UploadTimeflyPhotoAdd,
		//上传时光图片创建
		UploadTimeflyPhotoCreate,
		//时光模块图片上传操作
		UploadTimeflyPhoto,
		//
		ShowImageTitleChange,
		//时光提醒
		TimeflyNotify,
		//
		DOWNLOADSUCCESS,
		//注册成功
		RegisterSuccess,
		//注册时本地缓冲头像
		RegisterCacheImage,
		//打开消息模块聊天信息
		MessageChatOpen,
		//打开消息模块好友请求信息
		MessageFriendAskOpen,
		//评论消息
		MessageComment,
		//注册成功时，提醒上传头像到服务器
		RegisterUploadImage,
		//上传头像到服务器成功时的提示
		UploadImageSuccess,
		//登陆页面传递数据到时光页面
		SendUsersInfoPacket,
		//发送相册图片到头像
		SendResultFAlbum,
		//发送相册图片到聊天中
		SendPathTChat,
		//发送相册图片到时光中
		SendPathFGarrery,
		//通过销毁相册界面
		NoticeDestroyGarray,
		//
		LoadImageNumberChanged,
		//点赞重新排序排名列表
		SupportToSort,
		//关闭第一个修改密码的界面
		CloseFrontActivity,
		//被评论的位置，传个消息界面
		UpdateCommentCount,
		//分享页面调用时光页面数据来刷新二级评论
		SecondSupportToTimely,
		//
		XmppConnectState,
		//通知关闭页面
		FinishActivity,
		//被评论的位置，传个消息界面
		CommentedPosition,
		//头像check界面发送通知edit删除头像
		SendBroadCastDel,
		//移除黑名单
		RemoveBlacklist,
		//增加到黑名单
		AddBlacklist,
		//
		ForceExit,
		//
		CheckTimeflyNotify,
		//正在下载新版本
		MsgDowning,
		//传递系统剪裁的结果
		SendClipPhoto,
		//新闻头条获取图片成功
		SendUnClipPhoto,
		//时光提醒
		TimeflyAlert,
		//时光提醒本地
		TimeflyAlertLocal;
	}

	public interface IEventBusAction {
		public void onEventMainThread(EventBusData data);
	}

	@Override
	public String toString() {
		return "EventBusData [action=" + action + ", msg=" + msg + "]";
	}
}
