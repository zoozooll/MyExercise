<?php
/** 
*
* memberlist [Chinese Simplified]
*
* @package language
* @version $Id: memberlist.php 8574 2008-05-29 13:59:47Z Kellanved $
* @copyright (c) 2007 phpbbchina.com
* @license http://opensource.org/licenses/gpl-license.php GNU Public License
*
*/

/**
* DO NOT CHANGE
*/
if (!defined('IN_PHPBB'))
{
	exit;
}

if (empty($lang) || !is_array($lang))
{
	$lang = array();
}

// DEVELOPERS PLEASE NOTE
//
// All language files should use UTF-8 as their encoding and the files must not contain a BOM.
//
// Placeholders can now contain order information, e.g. instead of
// 'Page %s of %s' you can (and should) write 'Page %1$s of %2$s', this allows
// translators to re-order the output of data while ensuring it remains correct
//
// You do not need this where single placeholders are used, e.g. 'Message %d' is fine
// equally where a string contains only two placeholders which are used to wrap text
// in a url you again do not need to specify an order e.g., 'Click %sHERE%s' is fine

$lang = array_merge($lang, array(
	'ABOUT_USER'			=> '其它相关',
	'ACTIVE_IN_FORUM'		=> '活跃于版面',
	'ACTIVE_IN_TOPIC'		=> '活跃于话题',
	'ADD_FOE'				=> '添加坏蛋',
	'ADD_FRIEND'			=> '添加好友',
	'AFTER'					=> '后于',

	'ALL'					=> '全部',

	'BEFORE'				=> '早于',

	'CC_EMAIL'				=> '给您自己发送一份这个 Email 的拷贝',
	'CONTACT_USER'			=> '联系方式',

	'DEST_LANG'				=> '语言',
	'DEST_LANG_EXPLAIN'		=> '选择一个合适的语言（如果可用）。',

	'EMAIL_BODY_EXPLAIN'	=> '邮件将以纯文本的形式发送，请不要添加任何 HTML 或 BBCode。这个邮件的返回地址将设置为您的 Email 地址。',
	'EMAIL_DISABLED'		=> '对不起 Email 相关的功能都已经被关闭。',
	'EMAIL_SENT'			=> 'Email 已经发送。',
	'EMAIL_TOPIC_EXPLAIN'	=> '内容将以纯文本的形式发送，请不要包含任何 HTML 或 BBCode。请注意主题的信息已经包含在邮件内。这个邮件的返回地址将设置为您的 Email 地址。',
	'EMPTY_ADDRESS_EMAIL'	=> '您必须提供一个有效的邮件接收地址。',
	'EMPTY_MESSAGE_EMAIL'	=> '您必须填入邮件内容。', 
	'EMPTY_MESSAGE_IM'		=> '您必须输入发送的消息.',
	'EMPTY_NAME_EMAIL'		=> '您必须填入真实的收信人名。',
	'EMPTY_SUBJECT_EMAIL'	=> '您必须给邮件定个题目。',
	'EQUAL_TO'				=> '等于',

	'FIND_USERNAME_EXPLAIN'	=> '用这个表单查找特定的成员。您不必填写所有的表格。匹配部分字符可以使用 * 作为通配符。当输入日期时请使用格式 <kbd>YYYY-MM-DD</kbd>，e.g. <samp>2004-02-29</samp>。使用多选框选择一个或多个用户名（是否接受多个用户名取决于表单本身）并点击选择选中的按钮回到原先的表单。',
	'FLOOD_EMAIL_LIMIT'		=> '您现在不能再次发送 Email。请稍后再试。',

	'GROUP_LEADER'			=> '组领导者',

	'HIDE_MEMBER_SEARCH'	=> '隐藏用户搜索',

	'IM_ADD_CONTACT'		=> '添加联系方式',
	'IM_AIM'				=> '请注意您需要安装 AOL Instant Messenger 后才能使用这个功能。',
	'IM_AIM_EXPRESS'		=> 'AIM Express',
	'IM_DOWNLOAD_APP'		=> '下载程序',
	'IM_ICQ'				=> '请注意用户可能不会接收到未被请求的即时消息。',
	'IM_JABBER'				=> '请注意用户可能不会接收到未被请求的即时消息。',
	'IM_JABBER_SUBJECT'		=> '这是一个自动消息，请不要回复！消息由用户 %1$s 于 %2$s 发出。',
	'IM_MESSAGE'			=> '您的消息',
	'IM_MSNM'				=> '请注意您需要安装 MSN Messenger 后才能使用这个功能。',
	'IM_MSNM_BROWSER'		=> '您的浏览器不支持这个功能。',
	'IM_MSNM_CONNECT'		=> 'MSNM 未连接。\n您需要连上 MSNM 后才能继续。',		
	'IM_NAME'				=> '您的名字',
	'IM_NO_DATA'			=> '没有关于这个用户的联系方式信息。',
	'IM_NO_JABBER'			=> '对不起，这个论坛不支持对 Jabber 用户的直接消息发送。您需要安装一个 Jabber 客户端来联系上面的联系人。',
	'IM_RECIPIENT'			=> '收信人',
	'IM_SEND'				=> '发送消息',
	'IM_SEND_MESSAGE'		=> '发送消息',
	'IM_SENT_JABBER'		=> '您发往 %1$s 的消息已经发送。',
	'IM_USER'				=> '发送一个即时消息',
	
	'LAST_ACTIVE'				=> '最后活动',
	'LESS_THAN'					=> '少于',
	'LIST_USER'					=> '1 位用户',
	'LIST_USERS'				=> '%d 位用户',
	'LOGIN_EXPLAIN_LEADERS'		=> '您需要注册并登录后才能浏览用户组列表。',
	'LOGIN_EXPLAIN_MEMBERLIST'	=> '您需要注册并登录后才能浏览用户列表。',
	'LOGIN_EXPLAIN_SEARCHUSER'	=> '您需要注册并登录后才能搜索用户。',
	'LOGIN_EXPLAIN_VIEWPROFILE'	=> '您需要注册并登录后才能查看用户资料。',

	'MORE_THAN'				=> '多于',

	'NO_EMAIL'				=> '您不允许给这个用户发 Email。',
	'NO_VIEW_USERS'			=> '您未被授权查看用户列表或用户资料。',

	'ORDER'					=> '顺序',
	'OTHER'					=> '其他',

	'POST_IP'				=> '发文 IP/域名',

	'RANK'					=> '等级',
	'REAL_NAME'				=> '收信人名',
	'RECIPIENT'				=> '收信人',
	'REMOVE_FOE'			=> '删除坏蛋',
	'REMOVE_FRIEND'			=> '删除好友',

	'SEARCH_USER_POSTS'		=> '列出用户文章',
	'SELECT_MARKED'			=> '选择已标记',
	'SELECT_SORT_METHOD'	=> '用户排列方式 ',
	'SEND_AIM_MESSAGE'		=> '发送 AIM 消息',
	'SEND_ICQ_MESSAGE'		=> '发送 ICQ 消息',
	'SEND_IM'				=> '即时消息',
	'SEND_JABBER_MESSAGE'	=> '发送 Jabber 消息',
	'SEND_MESSAGE'			=> '消息',
	'SEND_MSNM_MESSAGE'		=> '发送 MSNM/WLM 消息',
	'SEND_YIM_MESSAGE'		=> '发送 YIM 消息',
	'SORT_EMAIL'			=> 'Email',
	'SORT_LAST_ACTIVE'		=> '最后活动',
	'SORT_POST_COUNT'		=> '发贴总数',

	'USERNAME_BEGINS_WITH'	=> '用户名首字母 ',
	'USER_ADMIN'			=> '管理用户',
	'USER_BAN'				=> '封禁',
	'USER_FORUM'			=> '用户统计',
	'USER_LAST_REMINDED'	=> array(
		0		=> '尚未发送提醒',
		1		=> '发送过 %1$d 次提醒<br />» %2$s',
	),
	'USER_ONLINE'			=> '在线',
	'USER_PRESENCE'			=> '论坛形象',

	'VIEWING_PROFILE'		=> '查看资料 - %s',
	'VISITED'				=> '最后浏览',

	'WWW'					=> '主页',
));

?>