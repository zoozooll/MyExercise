<?php
define('SCR','register');
require_once('global.php');




$rg_config  = L::reg();
$inv_config = L::config(null, 'inv_config');

list($regminname,$regmaxname) = explode("\t", $rg_config['rg_namelen']);
list($regminpwd,$regmaxpwd) = explode("\t", $rg_config['rg_pwdlen']);

if (GetGP('vip') == 'activating') {

	InitGP(array('r_uid','pwd'),'G');
	$r_uid = (int)$r_uid;
	$u_db  = $db->get_one('SELECT username,yz FROM pw_members WHERE uid=' . pwEscape($r_uid));
	if ($u_db) {
		$yz = md5($u_db['yz'].substr(md5($db_sitehash),0,5).substr(md5($u_db['username']),0,5));
		if ($pwd == $yz) {
			$db->update('UPDATE pw_members SET yz=1 WHERE uid='.pwEscape($r_uid));
			require_once(R_P.'require/header.php');
			require_once(PrintEot('register'));
			footer();
		} else {
			Showmsg('reg_jihuo_fail');
		}
	} else {
		Showmsg('reg_jihuo_fail');
	}
}

if ($db_pptifopen && $db_ppttype == 'client') {
	Showmsg('passport_register');
}
list($regq) = explode("\t", $db_qcheck);

if (GetGP('action','P') == 'regcheck') {

	InitGP(array('type'),'P');

	if ($type == 'regname') {

		require_once(R_P . 'lib/register.class.php');
		InitGP('username','P');

		if (!PW_Register::checkNameLen(strlen($username))) {
			echo 1;
			ajax_footer();
		}
		$S_key = array("\\",'&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#','%','?','　');
		foreach ($S_key as $value) {
			if (strpos($username,$value) !== false) {
				echo 2;
				ajax_footer();
			}
		}
		if (!$rg_config['rg_rglower'] && !PW_Register::checkRglower($username)) {
			echo 3;
			ajax_footer();
		}

		$banname = explode(',',$rg_config['rg_banname']);
		foreach ($banname as $value) {
			if ($value && strpos($username,$value) !== false) {
				echo 2;
				ajax_footer();
			}
		}

		require_once(R_P . 'uc_client/uc_client.php');
		if (uc_user_get($username)) {
			echo 4;
		} else {
			echo 0;
		}
	} elseif ($type == 'regemail') {
		sleep(1);
		InitGP('email','P');
		if (!$email || !preg_match("/^[-a-zA-Z0-9_\.]+@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$/", $email)) {
			echo 1;
			ajax_footer();
		}

		if ($rg_config['rg_emailtype'] == 1 && $rg_config['rg_email']) {
			$e_check = 0;
			$e_limit = explode(',', $rg_config['rg_email']);
			foreach ($e_limit as $key => $val) {
				if (strpos($email,"@".$val) !== false) {
					$e_check = 1;
					break;
				}
			}
			if ($e_check == 0){
				echo 4;
				ajax_footer();
			}
		}


		if ($rg_config['rg_emailtype'] == 2 && $rg_config['rg_banemail']){
			$e_check = 0;
			$e_limit = explode(',', $rg_config['rg_banemail']);
			foreach ($e_limit as $key => $val) {
				if (strpos($email,"@".$val) !== false) {
					$e_check = 1;
					break;
				}
			}
			if ($e_check == 1){
				echo 5;
				ajax_footer();
			}
		}

		require_once(R_P . 'uc_client/uc_client.php');
		if (uc_user_get($email, 2)) {
			echo 2;
		} else {
			echo 0;
		}
	} elseif ($type == 'reggdcode') {
		InitGP('gdcode','P');
		if (!$gdcode || !SafeCheck(explode("\t",StrCode(GetCookie('cknum'),'DECODE')),strtoupper($gdcode),'cknum',1800)) {
			echo 1;
		} else {
			echo 0;
		}
	} elseif ($type == 'qanswer') {
		InitGP(array('answer','question'),'P');
		if ($db_question && (!isset($db_answer[$question]) || $answer != $db_answer[$question])) {
			echo 1;
		} else {
			echo 0;
		}
	} elseif ($type == 'invcode') {
		InitGP('invcode','P');
		if (empty($invcode)) {
			echo 1;
		} else {
			$inv_config['inv_days'] *= 86400;
			$inv = $db->get_one("SELECT id FROM pw_invitecode WHERE invcode=" . pwEscape($invcode) . " AND ifused<'2' AND createtime>" . pwEscape($timestamp - $inv_config['inv_days']));
			if (!$inv) {
				echo 2;
			} else {
				echo 0;
			}
		}
	}
	ajax_footer();
}
if ($rg_config['rg_allowregister'] == 0 || ($rg_config['rg_registertype'] == 1 && date('j',$timestamp) != $rg_config['rg_regmon']) || ($rg_config['rg_registertype'] == 2 && date('w',$timestamp) != $rg_config['rg_regweek'])) {
	Showmsg($rg_config['rg_whyregclose']);
}

InitGP(array('forward')); !$db_pptifopen && $forward = '';
InitGP(array('invcode','step'));

if ($rg_config['rg_allowsameip'] && file_exists(D_P.'data/bbscache/ip_cache.php') && !in_array($step,array('finish','permit'))) {
	$ipdata  = readover(D_P.'data/bbscache/ip_cache.php');
	$pretime = (int)substr($ipdata,13,10);
	if ($timestamp - $pretime > $rg_config['rg_allowsameip'] * 3600) {
		P_unlink(D_P.'data/bbscache/ip_cache.php');
	} elseif (strpos($ipdata,"<$onlineip>") !== false) {
		Showmsg('reg_limit');
	}
}

$step != 'finish' && $groupid != 'guest' && Showmsg('reg_repeat');

if (!$step && $step != 2) {

	!$rg_config['rg_timestart'] && $rg_config['rg_timestart'] = 1960;
	!$rg_config['rg_timeend'] && $rg_config['rg_timeend'] = 2000;
	$img = @opendir("$imgdir/face");
	while ($imagearray = @readdir($img)) {
		if ($imagearray!="." && $imagearray!=".." && $imagearray!="" && $imagearray!="none.gif") {
			$imgselect.="<option value='$imagearray'>$imagearray</option>";
		}
	}
	@closedir($img);
	require_once(R_P.'require/header.php');
	$custominfo = unserialize($db_union[7]);
	$customfield = L::config('customfield','customfield');
	require_once(PrintEot('register'));footer();

} elseif ($step == 2) {


	PostCheck(0, $db_gdcheck & 1, $regq, 0);
	if ($_GET['method'] || (!($db_gdcheck & 1) && $_POST['gdcode']) ||
		(!$regq && ($_POST['qanswer'] || $_POST['qkey']))/* ||
		($db_xforwardip && $_POST['_hexie'] != GetVerify($onlineip))*/
	) {
		Showmsg('undefined_action');
	}

	InitGP(array('regreason','regname','regpwd','regpwdrepeat','regemail','customdata', 'regemailtoall','rgpermit'),'P');
	InitGP(array('question','customquest','answer'),'P');

	$sRegpwd = $regpwd;
	$register = L::loadClass('Register');

	$inv_config['inv_open'] == '1' && $register->checkInv($invcode);
	$register->checkSameNP($regname, $regpwd);

	$register->setStatus(11);
	$regemailtoall && $register->setStatus(7);
	$register->setName($regname);
	$register->setPwd($regpwd, $regpwdrepeat);
	$register->setEmail($regemail);
	$register->setSafecv($question, $customquest, $answer);
	$register->setReason($regreason);
	$register->setCustomfield(L::config('customfield','customfield'));
	$register->setCustomdata($customdata);
	$register->execute();

	if ($inv_config['inv_open'] == '1') {
		$register->disposeInv();
	}
	list($winduid, $rgyz, $safecv) = $register->getRegUser();

	
	$windid  = $regname;
	$windpwd = md5($regpwd);
	//$iptime=$timestamp+86400;
	//Cookie("ifregip",$onlineip,$iptime);
	if ($rg_config['rg_allowsameip']) {
		if (file_exists(D_P.'data/bbscache/ip_cache.php')) {
			writeover(D_P.'data/bbscache/ip_cache.php',"<$onlineip>","ab");
		} else {
			writeover(D_P.'data/bbscache/ip_cache.php',"<?php die;?><$timestamp>\n<$onlineip>");
		}
	}
	//addonlinefile();
	if (GetCookie('userads') && $db_ads == '2') {
		list($u,$a) = explode("\t",GetCookie('userads'));
		if (is_numeric($u) || ($a && strlen($a)<16)) {
			require_once(R_P.'require/userads.php');
		}
	}
	if (GetCookie('o_invite') && $db_modes['o']['ifopen'] == 1) {
		list($o_u,$hash,$app) = explode("\t",GetCookie('o_invite'));
		if (is_numeric($o_u) && strlen($hash) == 18) {
			require_once(R_P.'require/o_invite.php');
		}
		Cookie('o_invite','');
	}
	if ($rgyz == 1) {
		Cookie("winduser",StrCode($winduid."\t".PwdCode($windpwd)."\t".$safecv));
		Cookie("ck_info",$db_ckpath."\t".$db_ckdomain);
		Cookie('lastvisit','',0);//将$lastvist清空以将刚注册的会员加入今日到访会员中
	}
	//发送短消息
	if ($rg_config['rg_regsendmsg']) {
		require_once(R_P.'require/msg.php');
		$rg_config['rg_welcomemsg'] = str_replace('$rg_name', $regname, $rg_config['rg_welcomemsg']);
		$messageinfo   = array(
			'toUser'	=> $windid,
			'subject'	=> "Welcome To[{$db_bbsname}]!",
			'content'	=> $rg_config['rg_welcomemsg']
		);
		pwSendMsg($messageinfo);
	}

	//发送邮件
	@include_once(D_P.'data/bbscache/mail_config.php');
	if ($rg_config['rg_emailcheck']) {
		$verifyhash = GetVerify();
		$rgyz = md5($rgyz . substr(md5($db_sitehash),0,5) . substr(md5($regname),0,5));
		require_once(R_P.'require/sendemail.php');
		$sendinfo = sendemail($regemail, 'email_check_subject', 'email_check_content', 'email_additional');
		if ($sendinfo === true) {
			ObHeader("$db_registerfile?step=finish&email=$regemail&verify=$verifyhash");
		} else {
			Showmsg(is_string($sendinfo) ? $sendinfo : 'reg_email_fail');
		}
	} elseif ($rg_config['rg_regsendemail'] && $ml_mailifopen) {
		require_once(R_P.'require/sendemail.php');
		sendemail($regemail,'email_welcome_subject','email_welcome_content','email_additional');
	}
	//发送结束

	//passport
	if ($db_pptifopen && $db_ppttype == 'server' && ($db_ppturls || $forward)) {
		$action = 'login';
		$jumpurl = $forward ? $forward : $db_ppturls;
		empty($forward) && $forward = $db_bbsurl;
		require_once(R_P.'require/passport_server.php');
	}
	//passport

	$verifyhash = GetVerify($winduid);
	ObHeader("$db_registerfile?step=finish&verify=$verifyhash");

} elseif ($step == 'finish') {

	InitGP(array('email'),'G');
	InitGP(array('option'));
	if ($option != 'uploadicon') {
		require_once(R_P.'require/header.php');
		PostCheck();
	}
	if ($email && $rg_config['rg_emailcheck']) {
		list(,$emailurl) = explode('@',$email);
		$emailurl = 'http://mail.'.$emailurl;
		if ($_GET['r']) {
			$men = $db->get_one("SELECT uid,username,password,email,regdate,yz FROM pw_members WHERE email=".pwEscape($email)." AND yz>'1'");
			!$men && Showmsg('remail_error',1);
			$regname = $men['username'];
			$winduid = $men['uid'];
			$timestamp = $men['regdate'];
			$sRegpwd = $men['password'];
			$rgyz = md5($men['yz'].substr(md5($db_sitehash),0,5).substr(md5($regname),0,5));
			require_once(R_P.'require/sendemail.php');
			$sendinfo = sendemail($email,'email_check_subject','email_check_content','email_additional');
			if ($sendinfo === true) {
				ObHeader("$db_registerfile?step=finish&email=$email&verify=$verifyhash");
			} else {
				Showmsg(is_string($sendinfo) ? $sendinfo : 'reg_email_fail');
			}
		}
	}
	if (!$rg_config['rg_emailcheck'] && $option != 'uploadicon') {
		!$winduid && Showmsg('illegal_request');
	}

	//系统头像
	$img = @opendir("$imgdir/face");
	while ($imgname = @readdir($img)) {
		if ($imgname != "." && $imgname != ".." && $imgname != "" && eregi("\.(gif|jpg|png|bmp)$",$imgname)) {
			$num++;
			$imgname_array[] = $imgname;
			if ($num >= 10) break;
		}
	}
	@closedir($img);

	if ($option == '2') {

		InitGP(array('reghomepage','regfrom','regintroduce','regsign','regsex','regbirthyear','regbirthmonth','regbirthday','regoicq'),'P');
		$regsex = (int)$regsex;
		$regsex = $regsex ? $regsex : "0";
		$rgbirth = (!$regbirthyear || !$regbirthmonth || !$regbirthday) ? '0000-00-00' : $regbirthyear."-".$regbirthmonth."-".$regbirthday;
		if ($regoicq && !ereg("^[0-9]{5,}$",$regoicq)) {
			Showmsg('illegal_OICQ');
		}

		require_once(R_P.'require/bbscode.php');
		if ($regsign != '') {
			$_G['signnum'] = $_G['signnum'] ? $_G['signnum'] : 50;
			if (strlen($regsign) > $_G['signnum']) {
				Showmsg('sign_limit');
			}
			$lxsign = convert($regsign,$db_windpic,2);

			if ($lxsign <> $regsign) {
				setstatus($userstatus,9);
			}
		}
		$wordsfb = L::loadClass('FilterUtil');
		foreach (array($regsign, $regintroduce) as $key => $value) {
			if (($banword = $wordsfb->comprise($value)) !== false) {
				Showmsg('sign_wordsfb');
			}
		}
		if (strlen($regintroduce)>500) Showmsg('introduce_limit');

		$db->update('UPDATE pw_members SET ' . pwSqlSingle(array('gender' => $regsex, 'bday' => $rgbirth, 'location' => $regfrom, 'oicq' => $regoicq, 'site' => $reghomepage, 'signature' => $regsign, 'introduce' => $regintroduce)) . ' WHERE uid=' . pwEscape($winduid));

		//flash头像上传参数
		if ($db_ifupload && $_G['upload']) {

			$pwServer['HTTP_USER_AGENT'] = 'Shockwave Flash';
			$swfhash = GetVerify($winduid);
			$upload_param = rawurlencode($db_bbsurl.'/job.php?action=uploadicon&verify='.$swfhash.'&uid='.$winduid.'&');
			$save_param = rawurlencode($db_bbsurl.'/job.php?action=uploadicon&step=2&from=reg&');
			$default_pic = rawurlencode("$db_picpath/facebg.jpg");
			session_start();
			$sid = session_id();
			$icon_encode_url = 'up='.$upload_param.'&saveFace='.$save_param.'&url='.$default_pic.'&PHPSESSID='.$sid.'&'.'imgsize='.$db_imgsize.'&';

		} else {
			$icon_encode_url = '';
		}

	} elseif ($option == '3') {

		InitGP(array('proicon','facetype'),'P');
		require_once(R_P.'require/showimg.php');

		//user icon
		$user_a = array();
		$usericon = '';
		if ($facetype == 1) {
			$usericon = setIcon($proicon, $facetype, $user_a);
		} elseif ($_G['allowportait'] && $facetype == 2) {
			$httpurl = $_POST['httpurl'];
			if (strncmp($httpurl[0],'http',4) != 0 || strrpos($httpurl[0],'|') !== false) {
				Showmsg('illegal_customimg');
			}
			$proicon = $httpurl[0];
			$httpurl[1] = (int)$httpurl[1];
			$httpurl[2] = (int)$httpurl[2];
			$httpurl[3] = (int)$httpurl[3];
			$httpurl[4] = (int)$httpurl[4];
			list($user_a[2], $user_a[3]) = flexlen($httpurl[1], $httpurl[2], $httpurl[3], $httpurl[4]);
			/*
			if (empty($httpurl[1]) && empty($httpurl[2])) {
				list($iconwidth,$iconheight) = getimagesize($proicon);
			} else {
				list($iconwidth,$iconheight) = getfacelen($httpurl[1],$httpurl[2]);
			}
			$user_a[2] = $iconwidth;
			$user_a[3] = $iconheight;
			*/
			$usericon = setIcon($proicon, $facetype, $user_a);
			unset($httpurl);
		}
		pwFtpClose($ftp);

		$db->update("UPDATE pw_members SET icon=".pwEscape($usericon,false)."WHERE uid=".pwEscape($winduid));

		refreshto("./$db_bfn",'reg_success');
	}

	require_once(PrintEot('register'));footer();

} elseif ($step == 'permit') {

	if (isset($_GET['ajax'])) {
		define('AJAX','1');
	}
	require_once PrintEot('register');ajax_footer();

} else {
	Showmsg('undefined_action');
}
?>