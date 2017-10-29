<?php
!function_exists('readover') && exit('Forbidden');

function Loginout() {
	global $db,$timestamp,$db_onlinetime,$groupid,$windid,$winduid,$db_ckpath,$db_ckdomain,$db_online;
	$thisvisit=$timestamp-$db_onlinetime*1.5;
	$db->update('UPDATE pw_memberdata SET thisvisit='.pwEscape($thisvisit).' WHERE uid='.pwEscape($winduid));
	
	/*update cache*/
	$_cache = getDatastore();
	$_cache->delete("UID_".$winduid);
	
	list($db_ckpath,$db_ckdomain)=explode("\t",GetCookie('ck_info'));
	Cookie('winduser','',0);
	Cookie('hideid','',0);
	Cookie('lastvisit','',0);
	$pwdcheck = GetCookie('pwdcheck');
	if (is_array($pwdcheck)) {
		foreach ($pwdcheck as $key => $value) {
			Cookie("pwdcheck[$key]",'',0);
		}
	}
	Cookie('ck_info','',0);
	Cookie('msghide','',0,false);
	$windid = $winduid = '';
}
function Loginipwrite($winduid) {
	global $db,$timestamp,$onlineip,$montime;
	$logininfo="$onlineip|$timestamp|6";
	$pwSQL = "monoltime=IF(lastvisit<'$montime',0,monoltime),";
	$pwSQL .= pwSqlSingle(array(
		'lastvisit'	=> $timestamp,
		'thisvisit'	=> $timestamp,
		'onlineip'	=> $logininfo
	));
	$db->update("UPDATE pw_memberdata SET $pwSQL WHERE uid=".pwEscape($winduid));
}

function checkLgt($lgt) {
	global $db_logintype;
	!$db_logintype && $db_logintype = 1;
	return ($db_logintype & pow(2, intval($lgt)));
}

function getLoginUser($uid) {
	return $GLOBALS['db']->get_one("SELECT m.uid,m.username,m.password,m.safecv,m.groupid,m.memberid,m.yz,md.onlineip, md.postnum,md.rvrc,md.money,md.credit,md.currency,md.lastpost,md.onlinetime,md.todaypost,md.monthpost FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE m.uid=" . pwEscape($uid) . " AND m.groupid<>'0' AND md.uid IS NOT NULL");
}

function checkpass($username, $password, $safecv, $lgt=0) {
	global $db_ifsafecv,$db_ifpwcache,$db,$timestamp,$onlineip;
	if (!checkLgt($lgt)) {
		//Showmsg('login_errortype');
		return 'login_errortype';
	}
	require_once(R_P . 'uc_client/uc_client.php');
	$uc_user = uc_user_login($username, $password, $lgt);

	if ($uc_user['status'] == -1) {
		$GLOBALS['errorname'] = $username;
		//Showmsg('user_not_exists');
		return 'user_not_exists';
	}
	if ($uc_user['status'] == -3) {
		//Showmsg('reg_email_have_same');
		return 'reg_email_have_same';
	}
	if (!$men = getLoginUser($uc_user['uid'])) {
		$register = L::loadClass('Register');
		$register->appendUser($uc_user['uid'], $uc_user['username'], $password, $uc_user['email']);
		$men = getLoginUser($uc_user['uid']);
	}
	if (empty($men)) {
		$GLOBALS['errorname'] = $username;
		//Showmsg('user_not_exists');
		return 'user_not_exists';
	}
	$e_login = explode("|", $men['onlineip']);

	if ($e_login[0] == $onlineip.' *' && ($timestamp - $e_login[1]) < 600 && $e_login[2] < 2) {
		$GLOBALS['L_T'] = 600 - ($timestamp - $e_login[1]);
		//Showmsg('login_forbid');
		return 'login_forbid';
	}
	if ($men['yz'] > 1) {
		$GLOBALS['jihuo_uid'] = $men['uid'];
		//Showmsg('login_jihuo');
		return 'login_jihuo';
	}
	if ($uc_user['status'] == -2 || ($db_ifsafecv && $men['safecv'] != $safecv)) {
		global $L_T;
		$L_T = ($timestamp - $e_login[1]) > 600 ? 6 : intval($e_login[2]);
		$L_T--;
		$F_login = "$onlineip *|$timestamp|$L_T";
		$db->update("UPDATE pw_memberdata SET onlineip=" . pwEscape($F_login) . " WHERE uid=" . pwEscape($uc_user['uid']));
		//Showmsg('login_pwd_error');
		return 'login_pwd_error';
	}
	$diff_sql = array();
	
	if ($men['password'] != $password) {
		$diff_sql['password'] = $password;
	}
	if ($men['email'] != $uc_user['email']) {
		$diff_sql['email'] = $uc_user['email'];
	}
	if ($diff_sql) {
		$db->update("UPDATE pw_members SET " . pwSqlSingle($diff_sql) . ' WHERE uid=' . pwEscape($uc_user['uid']));
	}
	$winduid = $men['uid'];
	$groupid = $men['groupid'] == '-1' ? $men['memberid'] : $men['groupid'];
	$windpwd = PwdCode($password);

	//Start Here会员排行榜
	if ($db_ifpwcache & 1) {
		require_once(R_P . 'lib/elementupdate.class.php');
		$elementupdate = new ElementUpdate();
		$elementupdate->userSortUpdate($men);
	}
	//End Here
	return array($winduid, $groupid, $windpwd , $uc_user['synlogin']);
}

function checkpass1($username,$password,$safecv,$lgt=0) {
	global $db,$timestamp,$onlineip,$db_ckpath,$db_ckdomain,$men_uid,$db_ifsafecv,$db_ifpwcache,$db_logintype;
	$str_logintype = '';
	if ($db_logintype) {
		for ($i = 0; $i < 3; $i++) {
			${'logintype_'.$i} = ($db_logintype & pow(2,$i)) ? 1 : 0;
		}
	} else {
		$logintype_0 = 1;
	}
	!${'logintype_'.$lgt} && Showmsg('login_errortype');
	switch (intval($lgt)) {
		case 0:
			$str_logintype = 'username';
			break;
		case 1:
			$str_logintype = 'uid';
			break;
		case 2:
			!preg_match("/^[-a-zA-Z0-9_\.]+@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$/",$username) && Showmsg('illegal_email');
			$str_logintype = 'email';
			break;
		default:
			$str_logintype = 'username';
			break;
	}
	$men_uid = '';
	if (intval($lgt) == 2) {
		$query = $db->query("SELECT m.uid,m.username,m.password,m.safecv,m.groupid,m.memberid,m.yz,md.onlineip,md.postnum,md.rvrc,md.money,md.credit,md.currency,md.lastpost,md.onlinetime,md.todaypost,md.monthpost,md.monoltime,md.digests "
				. " FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid"
				. " WHERE m.".$str_logintype."=".pwEscape($username)." LIMIT 2");
		$int_querynum = $db->num_rows($query);
		if (!$int_querynum) {
			Showmsg('user_not_exists');
		} elseif ($int_querynum == 1) {
			$men = $db->fetch_array($query);
		} else {
			Showmsg('reg_email_have_same');
		}
	} else {
		$men = $db->get_one("SELECT m.uid,m.username,m.password,m.safecv,m.groupid,m.memberid,m.yz,md.onlineip,md.postnum,md.rvrc,md.money,md.credit,md.currency,md.lastpost,md.onlinetime,md.todaypost,md.monthpost"
				. " FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid"
				. " WHERE m.".$str_logintype."=".pwEscape($username));
	}
	if ($men) {
		$e_login = explode("|",$men['onlineip']);
		if ($e_login[0] != $onlineip.' *' || ($timestamp-$e_login[1])>600 || $e_login[2]>1 ) {
			$men_uid = $men['uid'];
			$men_pwd = $men['password'];
			$check_pwd = $password;
			$men['yz'] > 2 && Showmsg('login_jihuo');

			if (strlen($men_pwd) == 16) {
				$check_pwd=substr($password,8,16);/*支持 16 位 md5截取密码*/
			}
			if ($men_pwd == $check_pwd && (!$db_ifsafecv || $men['safecv'] == $safecv)) {
				if (strlen($men_pwd)==16) {
					$db->update("UPDATE pw_members SET password=".pwEscape($password)."WHERE uid=".pwEscape($men_uid));
				}
				$L_groupid = $men['groupid']=='-1' ? $men['memberid'] : $men['groupid'];
				Cookie("ck_info",$db_ckpath."\t".$db_ckdomain);
			} else {
				global $L_T;
				$L_T = ($timestamp-$e_login[1])>600 ? 5 : $e_login[2];
				$L_T ? $L_T--:$L_T=5;
				$F_login = "$onlineip *|$timestamp|$L_T";
				$db->update("UPDATE pw_memberdata SET onlineip=".pwEscape($F_login)."WHERE uid=".pwEscape($men_uid));
				Showmsg('login_pwd_error');
			}
		} else {
			global $L_T;
			$L_T=600-($timestamp-$e_login[1]);
			Showmsg('login_forbid');
		}
	} else {
		global $errorname;
		$errorname = $username;
		Showmsg('user_not_exists');
	}
	//Start Here会员排行榜
	if ($db_ifpwcache & 1) {
		require_once(R_P.'lib/elementupdate.class.php');
		$elementupdate = new ElementUpdate();
		$elementupdate->userSortUpdate($men);
	}
	//End Here
	return array($men_uid,$L_groupid,PwdCode($password));
}
function questcode($question,$customquest,$answer) {
	$question = $question=='-1' ? $customquest : $question;
	return $question ? substr(md5(md5($question).md5($answer)),8,10) : '';
}
?>