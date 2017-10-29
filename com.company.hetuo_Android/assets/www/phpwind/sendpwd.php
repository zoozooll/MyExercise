<?php
define('SCR','sendpwd');
require_once('global.php');
require_once(R_P.'require/header.php');
InitGP(array('action'));
!$action && $action = 'sendpwd';

if ($action == 'sendpwd') {

	if (empty($_POST['step'])) {

		require_once(PrintEot('sendpwd'));footer();

	} else {

		PostCheck(0,$db_gdcheck & 16);
		InitGP(array('pwuser','email','question','customquest','answer'));
		$userarray = $db->get_one("SELECT password,safecv,email,regdate FROM pw_members WHERE username=".pwEscape($pwuser));
		if (strtolower($userarray['email']) != strtolower($email)) {
			Showmsg('email_error',1);
		}
		$safecv = '';
		if ($db_ifsafecv) {
			require_once(R_P.'require/checkpass.php');
			$safecv = questcode($question,$customquest,$answer);
			if ($userarray['safecv'] != $safecv) {
				Showmsg('safecv_error',1);
			}
		}
		if ($userarray) {
			if ($timestamp - GetCookie('lastwrite') <= 60) {
				$_G['postpertime'] = 60;
				Showmsg('sendpwd_limit',1);
			}
			Cookie('lastwrite',$timestamp);
			$send_email = $userarray['email'];
			$submit     = $userarray['regdate'];
			$submit    .= md5(substr($userarray['password'],10));
			$sendtoname = $pwuser;
			$pwuser   = rawurlencode($pwuser);
			require_once(R_P.'require/sendemail.php');
			$sendinfo = sendemail($send_email,'email_sendpwd_subject','email_sendpwd_content','email_additional');
			if ($sendinfo === true) {
				Showmsg('mail_success',1);
			} else {
				Showmsg(is_string($sendinfo) ? $sendinfo : 'mail_failed',1);
			}
		} else {
			$errorname = $pwuser;
			Showmsg('user_not_exists',1);
		}
	}
} elseif ($action == 'getback') {

	InitGP(array('pwuser','submit'));
	if (CkInArray($pwuser,$manager)) {
		Showmsg('undefined_action',1);
	}
	$detail = $db->get_one("SELECT m.uid,m.password,m.regdate,md.onlineip FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE username=".pwEscape($pwuser));
	if ($detail) {
		$e_login = explode("|",$detail['onlineip']);
		if ($e_login[0]!=$onlineip.' *' || ($timestamp-$e_login[1])>600 || $e_login[2]>1 ) {
			$is_right  = $detail['regdate'];
			$is_right .= md5(substr($detail['password'],10));
			if ($submit == $is_right) {
				if (empty($_POST['jop'])) {
					require_once PrintEot('getpwd');footer();
				} else {
					InitGP(array('new_pwd','pwdreapt'));
					if ($new_pwd != $pwdreapt || !$new_pwd) {
						Showmsg('password_confirm',1);
					} else {
						$new_pwd = stripslashes($new_pwd);
						$new_pwd = str_replace("\t","",$new_pwd);
						$new_pwd = str_replace("\r","",$new_pwd);
						$new_pwd = str_replace("\n","",$new_pwd);
						$new_pwd = md5($new_pwd);
						$db->update("UPDATE pw_members SET password=".pwEscape($new_pwd)." WHERE username=".pwEscape($pwuser));
						refreshto('login.php','password_change_success');
					}
				}
			} else {
				global $L_T;
				$L_T = ($timestamp-$e_login[1])>600 ? 5 : $e_login[2];
				$L_T ? $L_T--:$L_T=5;
				$F_login = "$onlineip *|$timestamp|$L_T";
				$db->update("UPDATE pw_memberdata SET onlineip=".pwEscape($F_login)."WHERE uid=".pwEscape($detail['uid']));
				Showmsg('password_confirm_fail',1);
			}
		} else {
			global $L_T;
			$L_T = 600-($timestamp-$e_login[1]);
			Showmsg('login_forbid');
		}
	} else {
		$errorname = $pwuser;
		Showmsg('user_not_exists',1);
	}
}
?>