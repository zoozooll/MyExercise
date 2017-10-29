<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function set_cookie($k,$v) {
	global $session_prefix;
	setcookie($session_prefix.$k, $v, time()+30*24*3600);
}

function get_cookie($k) {
	global $session_prefix;
	if(isset($_COOKIE[$session_prefix.$k])) return $_COOKIE[$session_prefix.$k];
	return null;
}

function unset_cookie($k) {
	global $session_prefix;
	setcookie($session_prefix.$k, '', time()-3600);
}

function get_hisgoods_cookie() {
	return get_cookie("hisgoods");
}

function set_hisgoods_cookie($gid) {
	return set_cookie("hisgoods[$gid]",time());
}
?>