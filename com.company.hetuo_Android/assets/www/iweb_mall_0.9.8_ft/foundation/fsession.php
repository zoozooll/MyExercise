<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
// 设置session
function set_session($k,$v) {
	global $session_prefix;
	$_SESSION[$session_prefix.$k] = $v;
}

// 获取session
function get_session($k) {
	global $session_prefix;
	if(isset($_SESSION[$session_prefix.$k])) return $_SESSION[$session_prefix.$k];
	return null;
}
//USER_ID
function set_sess_user_id($v) {
	set_session('user_id',$v);
}
function get_sess_user_id() {
	return get_session('user_id');
}
//USER_NAME
function set_sess_user_name($v) {
	set_session('user_name',$v);
}
function get_sess_user_name() {
	return get_session('user_name');
}
//SHOP_ID
function set_sess_shop_id($v) {
	set_session('shop_id',$v);
}
function get_sess_shop_id() {
	return get_session('shop_id');
}

//USER_EMAIL
function set_sess_user_email($v) {
	set_session('user_email',$v);
}
function get_sess_user_email() {
	return get_session('user_email');
}
//EMAIL_CHECK
function set_sess_email_check($v) {
	set_session('email_check',$v);
}
function get_sess_email_check() {
	return get_session('email_check');
}
//privilege
function set_sess_privilege($v){
	set_session('privilege',$v);
}
function get_sess_privilege() {
	return get_session('privilege');
}
//rank_id
function set_sess_rank_id($v){
	set_session('rank_id',$v);
}
function get_sess_rank_id() {
	return get_session('rank_id');
}
//rank_name
function set_sess_rank_name($v){
	set_session('rank_name',$v);
}
function get_sess_rank_name() {
	return get_session('rank_name');
}
//error message
function set_sess_err_msg($v){
	set_session('err_msg',$v);
}
function get_sess_err_msg() {
	return get_session('err_msg');
}
function get_sess_admin()
{
	return get_session('admin_name');
}
?>