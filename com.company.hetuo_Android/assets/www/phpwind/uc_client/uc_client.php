<?php

// uc_client 包的根目录
define('UC_CLIENT_ROOT', dirname(__FILE__) . '/');
// uc_client 包使用的 lib 库所在的目录
// define('UC_LIB_ROOT', dirname(__FILE__) . '/../lib/');
// uc_client 包的版本
define('UC_CLIENT_VERSION', '0.1.0');
// uc_client 使用的API规范号
define('UC_CLIENT_API', '20090609');

/**
 用户登录
  @param  string $username   - 用户名
  @param  string $pwd        - 密码(md5)
  @param  int $logintype     - 登录类型 0,1,2分别为 用户名,uid,邮箱登录
  @param  boolean $checkques - 是否要验证安全问题
  @param  string $question   - 安全问题
  @param  string $answer     - 安全回答
  @return array 同步登录的代码
*/
function uc_user_login($username, $password, $logintype, $checkques = 0, $question = '', $answer = '') {
	return uc_data_request('user', 'login', array($username, $password, $logintype, $checkques, $question, $answer));
}

/**
 同步退出
  @return string 同步退出的代码
*/
function uc_user_synlogout() {
	return uc_data_request('user', 'synlogout');
}

/**
 注册
  @param  string $username - 注册用户名
  @param  string $password - 注册密码(md5)
  @param  string $email	   - 邮箱
  @return int 注册用户uid
*/
function uc_user_register($username, $password, $email) {
	$args = func_get_args();
	return uc_data_request('user', 'register', $args);
}
/**
 获取用户信息
  @param  string $username - 用户名
  @param  int $bytype - 获取方式 0,1,2分别为 用户名,uid,邮箱
  @return array uid,用户名,邮箱
*/
function uc_user_get($username, $bytype = 0) {
	return uc_data_request('user', 'get', array($username, $bytype));
}

/**
 验证
  @param  string $uid - 用户名
  @checkstr string password - uc_key+passwrord
  @return array uid,用户名,密码,邮箱
*/
function uc_user_check($uid, $checkstr) {
	$args = func_get_args();
	return uc_data_request('user', 'check', $args);
}

/**
 编辑用户资料
  @param  int $uid - 用户uid
  @param  string $oldname - 原用户名
  @param  string $newname - 新用户名
  @param  string $pwd - 新密码
  @param  string $email - 新邮箱
*/
function uc_user_edit($uid, $oldname, $newname, $pwd, $email) {
	return uc_data_request('user', 'edit', array($uid, $oldname, $newname, $pwd, $email));
}

/**
 删除指定 uid 的用户
  @param  mixed $uids - 用户uid序列，支持单个uid,多个uid数组或者用“,”隔开的字符串序列
  @param  int $del
*/
function uc_user_delete($uids) {
	return uc_data_request('user', 'delete', array($uids));
}

/**
 * 添加好友
 *
 * @param  int    $uid      - 用户id
 * @param  int    $friendid - 好友id
 * @param  string $descrip  - 好友描述
 * @return boolean          - 成功返回 true，失败返回 false
 */
function uc_friend_add($uid, $friendid, $descrip) {
    $args = func_get_args();
    return uc_data_request('friend', 'add', $args);
}

/**
 * 删除好友
 *
 * @param  int   $uid       - 用户id
 * @param  array $friendids - 要删除的好友id列表，多个用“,”隔开
 * @return boolean          - 成功返回 true，失败返回 false
 */
function uc_friend_delete($uid, $friendids) {
    $args = func_get_args();
    return uc_data_request('friend', 'delete', $args);
}

/**
 * 获取好友个数
 *
 * @param  int $uid    - 用户id
 * @param  int $status - 好友状态（-1为全部，0为未验证，1为已验证）
 * @return int         - 返回好友个数
 */
function uc_friend_totalnum($uid, $status = -1) {
    return uc_data_request('friend', 'totalnum', array($uid, $status));
}

/**
 * 获取好友列表
 *
 * @param  int $uid     - 用户id
 * @param  int $page    - 页号
 * @param  int $perpage - 每页显示个数
 * @param  int $status  - 好友状态（-1为全部，0为未验证，1为已验证）
 * @return array
 */
function uc_friend_list($uid, $page, $perpage, $status) {
    $args = func_get_args();
    return uc_data_request('friend', 'get_list', $args);
}

/**
 * 添加好友到某个分组
 *
 * @param  int $ftid
 * @param  int $uid
 * @param  int $friendid
 * @return boolean
 */
function uc_friend_add_type($ftid, $uid, $friendid) {
    $args = func_get_args();
    return uc_data_request('friend', 'add_type', $args);
}

/**
 * 新建好友分组
 *
 * @param  int $uid
 * @param  string $name
 * @return boolean
 */
function uc_friend_type_create($uid, $name) {
    $args = func_get_args();
    return uc_data_request('friend_type', 'create', $args);
}

/**
 * 删除好友分组
 *
 * @param  int $uid
 * @param  int $ftid
 * @return boolean
 */
function uc_friend_type_delete($uid, $ftid) {
    $args = func_get_args();
    return uc_data_request('friend_type', 'delete', $args);
}

/***********
 * 消息部分 *
 ***********/

/**
 * 发送消息
 *
 * @param  int    $fromuid  - 发送者id
 * @param  string $username - 发送者
 * @param  int    $touid    - 接收者id
 * @param  string $title    - 标题
 * @param  string $content  - 内容
 * @param  bool   $save_to_sebox - 是否保存到发件箱
 * @return boolean          - 成功返回 true，失败返回 false
 */
function uc_msg_send($fromuid, $username, $touid, $title, $content, $save_to_sebox) {
    $args = func_get_args();
    return uc_data_request('message', 'send', $args);
}

/**
 * 删除收件箱短消息
 *
 * @param  string $id  - 消息id，多条id使用“,”隔开，如：3 或者 1,2,3,4
 * @param  int $uid    - 用户id
 * @return boolean     - 成功返回 true，失败返回 false
 */
function uc_msg_delete_rebox($ids, $uid) {
    $args = func_get_args();
    return uc_data_request('message', 'delete_rebox', $args);
}

/**
 * 删除发件箱短消息
 *
 * @param  string $id  - 消息id，多条id使用“,”隔开，如：3 或者 1,2,3,4
 * @param  int $uid    - 用户id
 * @return boolean     - 成功返回 true，失败返回 false
 */
function uc_msg_delete_sebox($ids, $uid) {
    $args = func_get_args();
    return uc_data_request('message', 'delete_sebox', $args);
}

/**
 * 获取收件箱短消息总数
 *
 * @param  int $uid - 用户id
 * @return int      - 收件箱短消息总数
 */
function uc_msg_count_rebox($uid) {
    $args = func_get_args();
    return uc_data_request('message', 'count_rebox', $args);
}

/**
 * 获取发件箱短消息总数
 *
 * @param  int $uid - 用户id
 * @return int      - 发件箱短消息总数
 */
function uc_msg_count_sebox($uid) {
    $args = func_get_args();
    return uc_data_request('message', 'count_sebox', $args);
}

/**
 * 获取收件箱短消息列表
 *
 * @param  int $uid          - 用户id
 * @param  int $page         - 页号
 * @param  int $num_per_page - 每页记录数
 * @return array|null        - 返回对应页的二维数组，该页没有数据则返回 null
 */
function uc_msg_rebox_list($uid, $page, $num_per_page) {
    $args = func_get_args();
    return uc_data_request('message', 'rebox_list', $args);
}

/**
 * 获取发件箱短消息列表
 *
 * @param  int $uid          - 用户id
 * @param  int $page         - 页号
 * @param  int $num_per_page - 每页记录数
 * @return array|null        - 返回对应页的二维数组，该页没有数据则返回 null
 */
function uc_msg_sebox_list($uid, $page, $num_per_page) {
    $args = func_get_args();
    return uc_data_request('message', 'sebox_list', $args);
}

/**
 * 提取收件箱短消息内容
 *
 * @param  int $id    - 消息id
 * @param  int $uid   - 用户id
 * @return array|null - 返回数组，如果消息不存在，则返回 null
 */
function uc_msg_get_rebox($id, $uid) {
    $args = func_get_args();
    return uc_data_request('message', 'get_rebox', $args);
}

/**
 * 提取发件箱短消息内容
 *
 * @param  int $id    - 消息id
 * @param  int $uid   - 用户id
 * @return array|null - 返回数组，如果消息不存在，则返回 null
 */
function uc_msg_get_sebox($id, $uid) {
    $args = func_get_args();
    return uc_data_request('message', 'get_sebox', $args);
}

/**
 * 群发消息
 *
 * @param  string $title   - 标题
 * @param  string $content - 内容
 * @return boolean         - 成功返回 true，失败返回 false
 */
function uc_msg_send_public($title, $content) {
    $args = func_get_args();
    return uc_data_request('message', 'send_public', $args);
}

/**
 * 删除群发消息
 *
 * @param  string $ids
 * @param  int    $uid
 * @return boolean
 */
function uc_msg_delete_public($ids, $uid) {
    $args = func_get_args();
    return uc_data_request('message', 'delete_public', $args);
}

/**
 * 获取公共消息列表
 *
 * @param  int $uid
 * @param  int $gid
 * @param  int $regdate
 * @return array|null
 */
function uc_msg_public_list($uid, $gid, $regdate) {
    $args = func_get_args();
    return uc_data_request('message', 'public_list', $args);
}

/**
 * 获取公共消息内容
 *
 * @param  int $mid
 * @return array|null
 */
function uc_msg_get_public($mid) {
    $args = func_get_args();
    return uc_data_request('message', 'get_public', $args);
}

/**
 设置用户积分增减
  @param  array $credit array($uid1 => array($ctype1 => $point1, $ctype2 => $point2), $uid2 => array())
  return array
 */
function uc_credit_add($credit, $isAdd = true) {
	return uc_data_request('credit', 'add', array($credit, $isAdd));
}

function uc_credit_get($uid) {
	return uc_data_request('credit', 'get', array($uid));
}

function uc_data_request($class,$method,$args = array()) {
	static $uc = null;
	if (empty($uc)) {
		require_once UC_CLIENT_ROOT . 'class_core.php';
		$uc = new UC();
	}
	$class = $uc->control($class);

	if (method_exists($class, $method)) {
		return call_user_func_array(array(&$class, $method), $args);
	} else {
		return 'error';
	}
}
?>