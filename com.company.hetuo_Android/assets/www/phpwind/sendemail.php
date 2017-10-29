<?php
define('SCR','sendemail');
require_once('global.php');
require_once(R_P.'require/header.php');

$groupid == 'guest' && Showmsg('not_login');
InitGP(array('action'));
!$action && $action = 'mailto';

if ($action == 'mailto') {

	InitGP(array('uid','username'));
	if ($username || is_numeric($uid)) {
		if ($username) {
			$sql = "username=".pwEscape($username);
		} else {
			$sql = "uid=".pwEscape($uid);
		}
		$userdb = $db->get_one("SELECT uid,username,email,userstatus FROM pw_members WHERE $sql");
	} else {
		$userdb = '';
	}
	!$userdb && Showmsg('undefined_action');

	$rt = $db->get_one("SELECT lasttime FROM pw_memberinfo WHERE uid=".pwEscape($winduid));
	if ($timestamp-$rt['lasttime'] < 60) {
		Showmsg('sendeamil_limit');
	}
	if (empty($_POST['step'])) {

		if (!getstatus($userdb['userstatus'],8) && $groupid != '3' && $groupid != '4') {
			Showmsg('sendeamil_refused');
		}
		$to_mail = $userdb['email'];
		$to_user = $userdb['username'];

		if (!getstatus($userdb['userstatus'],7) && $groupid != '3' && $groupid != '4') {
			$hiddenmail = 1;
		} else {
			$hiddenmail = 0;
		}
		require_once(PrintEot('sendmail'));footer();

	} else {

		PostCheck(1,$db_gdcheck & 16);

		if (!getstatus($userdb['userstatus'],8) && $groupid != '3' && $groupid != '4') {
			Showmsg('sendeamil_refused');
		}
		$sendtoemail = $userdb['email'];
		InitGP(array('subject','atc_content','fromname','fromemail','sendtoname'));

		if (empty($subject)) {
			Showmsg('sendeamil_subject_limit');
		}
		if (empty($atc_content) || strlen($atc_content) <= 20) {
			Showmsg('sendeamil_content_limit');
		} elseif (!ereg("^[-a-zA-Z0-9_\.]+\@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$",$sendtoemail) || !ereg("^[-a-zA-Z0-9_\.]+\@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$",$fromemail)) {
			Showmsg('illegal_email');
		}
		if ($rt) {
			$db->update("UPDATE pw_memberinfo SET lasttime=".pwEscape($timestamp)."WHERE uid=".pwEscape($winduid));
		} else {
			$db->update("INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$winduid,'lasttime'=>$timestamp)));
		}
		require_once(R_P.'require/sendemail.php');
		$sendinfo = sendemail($sendtoemail,$subject,$atc_content,'email_additional');
		if ($sendinfo === true) {
			refreshto('index.php','mail_success');
		} else {
			Showmsg(is_string($sendinfo) ? $sendinfo : 'mail_failed');
		}
	}
}
?>