<?php
define('PRO','1');
define('SCR','profile');
require_once('global.php');

!$winduid && Showmsg('not_login');
InitGP(array('action'));

require_once(R_P . 'require/showimg.php');
list($faceurl) = showfacedesign($winddb['icon'],1,'s');

empty($action) && $action = 'index';
$pro_tab = $action;

if (!$pw_seoset && $pw_seoset = L::loadClass('seoset')) {
	$webPageTitle = $pw_seoset->getPageTitle();
	$metaDescription = $pw_seoset->getPageMetadescrip();
	$metaKeywords = $pw_seoset->getPageMetakeyword();
}

$db_menuinit .= ",'td_userinfomore' : 'menu_userinfomore'";

if (file_exists(R_P . "require/profile/{$action}.php")) {
	require_once Pcv(R_P . "require/profile/{$action}.php");
} else {
	Showmsg('undefined_action');
}
exit;

if ($action == 'bbsset') {

	$userdb = $db->get_one("SELECT style,timedf,datefm,t_num,p_num,banpm,msggroups,userstatus FROM pw_members WHERE uid=".pwEscape($winduid));

	$t_num = array(10,20,30,40);
	$p_num = array(10,20,30);
	if ($groupid && in_array($groupid,array(3,4,5))) {
		$t_num[] = 100;
		$p_num[] = 100;
	}
	if (empty($_POST['step'])) {

		require_once(R_P.'require/forum.php');
		InitGP(array('info_type'));
		!in_array($info_type,array('fancy','friend','msg')) && $info_type = 'fancy';

		$ifsign	  = false;
		$friend_0 = $friend_1 = $friend_2 = '';
		$editor_Y = $editor_N = $check_12 = $d_type_0 = $d_type_1 = $email_Y = $email_N = '';
		$check_24 = 'checked';
		${'friend_'.getstatus($userdb['userstatus'],3,3)} = 'checked';
		${'email_'.(getstatus($userdb['userstatus'],8) ? 'Y' : 'N')} = 'checked';
		${'editor_'.(getstatus($userdb['userstatus'],11) ? 'Y' : 'N')} = 'checked';
		$userdb['style'] == '' && $skin = '';
		$choseskin = getstyles($skin);

		$customstyles = $customname = '';
		$query = $db->query("SELECT sid,name,customname FROM pw_styles WHERE uid=".pwEscape($winduid));
		while ($rt = $db->fetch_array($query)) {
			$customname = $rt['customname'] ? $rt['customname'] : $rt['sid'];
			if ($rt['sid'] == $userdb['style'] && !$skinco) {
				$customstyles .= "<option value=\"".$rt['sid']."\" selected>$customname</option>";
			} else {
				$customstyles .= "<option value=\"".$rt['sid']."\">$customname</option>";
			}
		}
		$choseskin = $choseskin.$customstyles;

		if ($db_signmoney && strpos($db_signgroup,",$groupid,") !== false) {
			require_once(R_P.'require/credit.php');
			$cur = $credit->get($winduid,$db_signcurtype);
			$cur === false && Showmsg('numerics_checkfailed');
			$ifsign = true;
			$days = $cur < 0 ? 0 : floor($cur/$db_signmoney);
			if ($userdb['starttime'] && $userdb['starttime'] <= $tdtime) {
				$haveshow = floor(($tdtime-$userdb['starttime'])/86400)+1;
			} else {
				$haveshow = 0;
			}
			${'showsign_'.(getstatus($userdb['userstatus'],10) ? 'Y' : 'N')} = 'checked';
		}
		if ($userdb['timedf']) {
			$temptimedf = str_replace('.','_',abs($userdb['timedf']));
			$userdb['timedf'] < 0 ? ${'zone_0'.$temptimedf} = 'selected' : ${'zone_'.$temptimedf} = 'selected';
		}
		if ($userdb['datefm']) {
			if (strpos($userdb['datefm'],'h:i A') !== false) {
				$userdb['datefm'] = str_replace(' h:i A','',$userdb['datefm']);
				$check_12 = 'checked';
				$check_24 = '';
			} else {
				$userdb['datefm'] = str_replace(' H:i','',$userdb['datefm']);
			}
			$userdb['datefm'] = str_replace(array('m','n','d','j','y','Y'), array('mm','m','dd','d','yy','yyyy'), $userdb['datefm']);
			$d_type_1 = 'checked';
		} else {
			$userdb['datefm'] = 'yyyy-mm-dd';
			$d_type_0 = 'checked';
		}
		if ($_G['msggroup']) {
			include_once(D_P.'data/bbscache/level.php');
			$usergroup = '';
			foreach ($ltitle as $key => $value) {
				if ($key != 1 && $key != 2) {
					if ($userdb['msggroups'] && strpos($userdb['msggroups'],','.$key.',') !== false) {
						$checked = 'checked';
					} else {
						$checked = '';
					}
					$usergroup .= "<div style=\"width:32%;float:left\"><input type=\"checkbox\" name=\"msggroups[]\" value=\"$key\" $checked>$value</div>";
				}
			}
		}
		${'T_'.(int)$userdb['t_num']} = ${'P_'.(int)$userdb['p_num']} = 'selected';//p_num step2 bug

		require_once PrintEot('profile');
		footer();

	} else {

		PostCheck();
		InitGP(array('date_f','timedf','tpskin','banidinfo'),'P');
		InitGP(array('showsign','editor','proreceivemail','friendcheck','msggroups'),'P',2);

		$upmemdata = array();
		$ustatus = '';
		if ($editor != getstatus($userdb['userstatus'],11)) {
			$ustatus.= ',userstatus=userstatus'.($editor ? '|' : '&~') .'(1<<10)';
		}
		if ($proreceivemail != getstatus($userdb['userstatus'],8)) {
			$ustatus.= ',userstatus=userstatus'.($proreceivemail ? '|' : '&~') .'(1<<7)';
		}
		if ($friendcheck != getstatus($userdb['userstatus'],3,3)) {
			switch ($friendcheck) {
				case 0:
					$ustatus .= ',userstatus=userstatus&~(3<<2)';break;
				case 1:
					$ustatus .= ',userstatus=userstatus|(1<<2),userstatus=userstatus&~(1<<3)';break;
				case 2:
					$ustatus .= ',userstatus=userstatus&~(1<<2),userstatus=userstatus|(1<<3)';break;
			}
		}
		if (GetCookie('skinco') && $tpskin != GetCookie('skinco')) {
			Cookie('skinco','',0);
		}
		$showsign = $showsign ? 1 : 0;

		if ($db_signmoney && ($singstatus = getstatus($userdb['userstatus'],10)) != $showsign) {
			$ustatus .= ',userstatus=userstatus'.($showsign ? '|' : '&~').'(1<<9)';
			if ($singstatus && $showsign == 0) {
				$upmemdata['starttime'] = 0;
			} else {
				require_once(R_P.'require/credit.php');
				if (($cur = $credit->get($winduid,$db_signcurtype)) === false) {
					Showmsg('numerics_checkfailed');
				}
				if ($cur < $db_signmoney) {
					Showmsg('noenough_currency');
				}
				$credit->addLog('main_showsign',array($db_signcurtype => -$db_signmoney),array(
					'uid'		=> $winduid,
					'username'	=> $windid,
					'ip'		=> $onlineip
				));
				if (!$credit->set($winduid,$db_signcurtype,-$db_signmoney,false)) {
					Showmsg('numerics_checkfailed');
				}
				$upmemdata['starttime'] = $tdtime;
			}
		}
		if ($_POST['d_type'] && $date_f) {
			$date_f  = strpos($date_f,'mm') !== false ? str_replace('mm','m',$date_f) : str_replace('m','n',$date_f);
			$date_f  = strpos($date_f,'dd') !== false ? str_replace('dd','d',$date_f) : str_replace('d','j',$date_f);
			$date_f  = str_replace(array('yyyy','yy'),array('Y','y'),$date_f);
			$date_f .= $_POST['time_f']=='12' ? ' h:i A' :' H:i';
		} else {
			$date_f = '';
		}
		$t_num = $_POST['t_num'] && !in_array($_POST['t_num'],$t_num) ? 0 : $_POST['t_num'];
		$p_num = $_POST['p_num'] && !in_array($_POST['p_num'],$p_num) ? 0 : $_POST['p_num'];

		//update memdata
		if ($upmemdata) {
			$pwSQL = pwSqlSingle($upmemdata);
			if (is_object($credit)) {
				$credit->runsql();
			}
			$db->update("UPDATE pw_memberdata SET $pwSQL WHERE uid=".pwEscape($winduid));
		}
		$groups = '';
		if ($_G['msggroup'] && $msggroups) {
			foreach ($msggroups as $key => $val) {
				if ($val) {
					$groups .= $groups ? ','.$val : $val;
				}
			}
		}
		$groups && $groups = ",$groups,";

		$pwSQL = pwSqlSingle(array('style' => $tpskin, 'datefm' => $date_f, 'timedf' => $timedf, 't_num' => $t_num, 'p_num' => $p_num, 'banpm' => $banidinfo, 'msggroups' => $groups));

		$db->update("UPDATE pw_members SET {$pwSQL}{$ustatus} WHERE uid=".pwEscape($winduid));

		refreshto("profile.php?action=bbsset",'operate_success');
	}
} else {
	Showmsg('undefined_action');
}
?>