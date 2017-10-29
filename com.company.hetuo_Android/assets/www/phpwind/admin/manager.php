<?php
!defined('P_W') && exit('Forbidden');

if (!pwWritable(D_P.'data/sql_config.php')) {
	adminmsg('manager_error');
}

include D_P.'data/sql_config.php';
!is_array($manager) && $manager = array();
!is_array($manager_pwd) && $manager_pwd = array();
$newmanager = $newmngpwd = array();
foreach ($manager as $key => $value) {
	if (!empty($value) && !is_array($value)) {
		$newmanager[$key] = $value;
		$newmngpwd[$key] = $manager_pwd[$key];
	}
}
$manager = $newmanager;
$manager_pwd = $newmngpwd;
unset($newmanager,$newmngpwd);

InitGP(array('oldname','username','password'));

if (!$action) {

	ifcheck($db_adminrecord,'adminrecord');
	include PrintEot('manager');exit;

} elseif ($action == 'add') {

	if (!$username || !$password) {
		adminmsg('manager_empty');
	}
	if (GetGP('check_pwd') != $password) {
		adminmsg('password_confirm');
	}
	if (str_replace(array('\\','&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#'),'',$username) != $username) {
		adminmsg('manager_errorusername');
	}
	if (str_replace(array('\\','&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#'),'',$password) != $password) {
		adminmsg('manager_errorpassword');
	}
	$password = md5($password);
	
	if (CkInArray($username,$manager)) {
		adminmsg('manager_had');
	}
	
	$manager[] = $username;
	$manager_pwd[] = $password;
	$newconfig = array(
		'dbhost' => $dbhost,
		'dbuser' => $dbuser,
		'dbpw' => $dbpw,
		'dbname' => $dbname,
		'database' => $database,
		'PW' => $PW,
		'pconnect' => $pconnect,
		'charset' => $charset,
		'manager' => $manager,
		'manager_pwd' => $manager_pwd,
		'db_hostweb' => $db_hostweb,
		'attach_url' => $attach_url
	);
	require_once(R_P.'require/updateset.php');
	write_config($newconfig);
	unset($newconfig);
	
	pwUpdateManager($username,$password);
	adminmsg('operate_success');

} elseif ($action == 'edit') {

	if (!CkInArray($oldname,$manager)) {
		adminmsg('undefined_action');
	}
	if ($_POST['step'] != 2) {

		include PrintEot('manager');exit;

	} else {

		if (!$username) {
			adminmsg('manager_empty');
		}
		if (str_replace(array('\\','&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#'),'',$username) != $username) {
			adminmsg('manager_errorusername');
		}
		$key = (int)array_search($oldname,$manager);
		if (!$password) {
			$password = $manager_pwd[$key];
		} else {
			if (GetGP('check_pwd')!=$password) {
				adminmsg('password_confirm');
			}
			if (str_replace(array('\\','&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#'),'',$password)!=$password) {
				adminmsg('manager_errorpassword');
			}
			$password = $manager_pwd[$key] = md5($password);
		}
		if ($username != $oldname) {
			if (CkInArray($username,$manager)) {
				adminmsg('manager_had');
			}
			$manager[$key] = $username;
			$oldname == $admin_name && Cookie('AdminUser','',0);
		}
		$newconfig = array(
			'dbhost' => $dbhost,
			'dbuser' => $dbuser,
			'dbpw' => $dbpw,
			'dbname' => $dbname,
			'database' => $database,
			'PW' => $PW,
			'pconnect' => $pconnect,
			'charset' => $charset,
			'manager' => $manager,
			'manager_pwd' => $manager_pwd,
			'db_hostweb' => $db_hostweb,
			'attach_url' => $attach_url
		);
		require_once(R_P.'require/updateset.php');
		write_config($newconfig);
		unset($newconfig);

		pwUpdateManager($username,$password);
		adminmsg('operate_success');
	}
} elseif ($action == 'delete') {

	if ($_POST['step'] != 2) {

		$inputmsg = '<input name="step" type="hidden" value="2" /><input name="action" type="hidden" value="delete" /><input name="username" type="hidden" value="'.$oldname.'" />';
		pwConfirm('manager_delusername',$inputmsg);

	} else {

		if (count($manager) < 2) {
			adminmsg('manager_only');
		}
		$newmanager = $newmngpwd = array();
		foreach ($manager as $key => $value) {
			if ($username != $value) {
				$newmanager[$key] = $value;
				$newmngpwd[$key] = $manager_pwd[$key];
			}
		}
		$newconfig = array(
			'dbhost' => $dbhost,
			'dbuser' => $dbuser,
			'dbpw' => $dbpw,
			'dbname' => $dbname,
			'database' => $database,
			'PW' => $PW,
			'pconnect' => $pconnect,
			'charset' => $charset,
			'manager' => $newmanager,
			'manager_pwd' => $newmngpwd,
			'db_hostweb' => $db_hostweb,
			'attach_url' => $attach_url
		);
		require_once(R_P.'require/updateset.php');
		write_config($newconfig);
		unset($newconfig);
		$username == $admin_name && Cookie('AdminUser','',0);
		lowerManager($username);
		adminmsg('operate_success');
	}
} elseif ($action == 'ifopen') {

	InitGP(array('config'));
	foreach ($config as $key => $value) {
		setConfig("db_$key", $value);
	}
	updatecache_c();
	adminmsg('operate_success');
} else {
	ObHeader($basename);
}
function lowerManager($username){
	global $db;
	$rt = $db->get_one('SELECT uid,groups FROM pw_members WHERE username='.pwEscape($username));
	if($rt){
		$db->update("UPDATE pw_members SET groupid='8' WHERE uid=".pwEscape($rt['uid']));
	}
}
function pwUpdateManager($username,$password){
	global $db;
	$rt = $db->get_one('SELECT uid,groups FROM pw_members WHERE username='.pwEscape($username));
	if (!$rt['uid']) {
		global $timestamp,$onlineip;
		$db->update('INSERT INTO pw_members'
			. ' SET ' . pwSqlSingle(array(
			'username'	=> $username,
			'password'	=> $password,
			'groupid'	=> 3,
			'regdate'	=> $timestamp
		)));
		$rt['uid'] = $db->insert_id();
		$db->update('INSERT INTO pw_memberdata'
			. ' SET ' . pwSqlSingle(array(
			'uid'		=> $rt['uid'],
			'postnum'	=> 0,
			'lastvisit'	=> $timestamp,
			'thisvisit'	=> $timestamp,
			'onlineip'	=> $onlineip
		)));
	} else {
		$db->update('UPDATE pw_members SET password='.pwEscape($password).",groupid='3' WHERE uid=".pwEscape($rt['uid']));
	}
	admincheck($rt['uid'],$username,'3',$rt['groups'],'update');
}
?>