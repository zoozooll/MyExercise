<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename="$admin_file?adminjob=currency";
require_once(R_P."require/forum.php");

if(!$action){
	include PrintEot('currency');exit;
} elseif($action == 'edit'){
	if(!$_POST['step']){
		InitGP(array('uid','username'));
		if(is_numeric($uid)){
			$sqladd = "m.uid=".pwEscape($uid);
		} else{
			$sqladd = "m.username=".pwEscape($username);
		}
		$rt = $db->get_one("SELECT m.uid,m.username,md.currency FROM pw_members m LEFT JOIN pw_memberdata md USING(uid) WHERE $sqladd");
		!$rt && adminmsg('user_not_exists');
		include PrintEot('currency');exit;
	} else{
		InitGP(array('uid','currency'),'P');
		$db->update("UPDATE pw_memberdata SET currency=".pwEscape($currency)."WHERE uid=".pwEscape($uid));
		adminmsg('operate_success');
	}
}
?>