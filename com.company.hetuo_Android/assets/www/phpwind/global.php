<?php
/**
 *
 *  Copyright (c) 2003-09  PHPWind.net. All rights reserved.
 *  Support : http://www.phpwind.net
 *  This software is the proprietary information of PHPWind.com.
 *
 */
file_exists('install.php') && ObHeader('install.php');
error_reporting(E_ERROR | E_PARSE);
set_magic_quotes_runtime(0);
function_exists('date_default_timezone_set') && date_default_timezone_set('Etc/GMT+0');

define('R_P',getdirname(__FILE__));
define('D_P',R_P);
define('A_P', R_P.'apps/');
define('P_W','global');
defined('SCR') || define('SCR','other');

$P_S_T	 = pwMicrotime();
require_once(R_P.'require/common.php');
pwInitGlobals();

require_once(D_P.'data/bbscache/config.php');
$timestamp = time();
$db_cvtime!=0 && $timestamp += $db_cvtime*60;
$wind_in = $SCR = SCR;

$onlineip = pwGetIp();
if ($db_forcecharset && !defined('W_P') && !defined('AJAX')) {
	@header("Content-Type:text/html; charset=$db_charset");
}
if ($db_loadavg && !defined('W_P') && pwLoadAvg($db_loadavg)) {
	$db_cc = 2;
}
if ($db_cc && !defined('COL')) {
	pwDefendCc($db_cc);
}

if ($db_dir && $db_ext) {
	$_NGET = array();
	$self_array = explode('-',substr($pwServer['QUERY_STRING'],0,strpos($pwServer['QUERY_STRING'],$db_ext)));
	$s_count = count($self_array);
	for ($i=0;$i<$s_count-1;$i++) {
		$_key	= $self_array[$i];
		$_value	= rawurldecode($self_array[++$i]);
		$_NGET[$_key] = addslashes($_value);
	}
	!empty($_NGET) && $_GET = $_NGET;
	unset($_NGET);
}

foreach ($_POST as $_key => $_value) {
	if (!in_array($_key,array('atc_content','atc_title','prosign','pwuser','pwpwd'))) {
		CheckVar($_POST[$_key]);
	}
}

foreach ($_GET as $_key => $_value) {
	CheckVar($_GET[$_key]);
}

$db_debug && error_reporting(E_ALL ^ E_NOTICE ^ E_DEPRECATED);
list($wind_version,$wind_repair,$wind_from) = explode(',',WIND_VERSION);
$db_olsize    = 96;

if (false !== ($dirstrpos = strpos($pwServer['PHP_SELF'],$db_dir))) {
	$tmp = substr($pwServer['PHP_SELF'],0,$dirstrpos);
	$pwServer['PHP_SELF'] = "$tmp.php";
	unset($dirstrpos);
} else {
	$tmp = $pwServer['PHP_SELF'];
}
$REQUEST_URI = $pwServer['PHP_SELF'].($pwServer['QUERY_STRING'] ? '?'.$pwServer['QUERY_STRING'] : '');

$_mainUrl = $index_url = $db_bbsurl;
$R_url = $db_bbsurl = Char_cv("http://".$pwServer['HTTP_HOST'].substr($tmp,0,strrpos($tmp,'/')));
defined('SIMPLE') && SIMPLE && $db_bbsurl = substr($db_bbsurl,0,-7);
$defaultMode = empty($db_mode) ? 'bbs' : $db_mode;
$db_mode = 'bbs';

if ($cookie_lastvisit = GetCookie('lastvisit')) {
	list($c_oltime,$lastvisit,$lastpath) = explode("\t",$cookie_lastvisit);
	($onbbstime=$timestamp-$lastvisit)<$db_onlinetime && $c_oltime+=$onbbstime;
	unset($cookie_lastvisit);
} else {
	$lastvisit = $lastpath = '';
	$c_oltime = $onbbstime = 0;
	Cookie('lastvisit',$c_oltime."\t".$timestamp."\t".$REQUEST_URI);
}

InitGP(array('fid','tid'),'GP',2);
$db = $ftp = $credit = null;

require_once(D_P.'data/sql_config.php');
!is_array($manager) && $manager = array();
$newmanager = array();
foreach ($manager as $key => $value) {
	if (!empty($value) && !is_array($value)) {
		$newmanager[$key] = $value;
	}
}
$manager = $newmanager;
if ($database == 'mysqli' && Pwloaddl('mysqli') === false) {
	$database = 'mysql';
}
ObStart();//noizy

if ($db_http != 'N') {
	$imgpath = $db_http;
	if (D_P != R_P) {
		$R_url = substr($db_http,-1)=='/' ?  substr($db_http,0,-1) : $db_http;
		$R_url = substr($R_url,0,strrpos($R_url,'/'));
	}
} else {
	$imgpath = $db_picpath;
}
/* performance */
//$attachpath = $db_attachurl != 'N' ? $db_attachurl : $db_attachname;
//$imgdir		= R_P.$db_picpath;
//$attachdir	= R_P.$db_attachname;
//$pw_posts   = 'pw_posts';
//$pw_tmsgs   = 'pw_tmsgs';
//$runfc		= 'N';
list($attachpath,$imgdir,$attachdir,$pw_posts,$pw_tmsgs,$runfc) = array($db_attachurl != 'N' ? $db_attachurl : $db_attachname, R_P.$db_picpath, R_P.$db_attachname, 'pw_posts', 'pw_tmsgs', 'N');
list($winduid,$windpwd,$safecv) = explode("\t",addslashes(StrCode(GetCookie('winduser'),'DECODE')));

$loginhash = GetVerify($onlineip,$db_pptkey);
if ($db_pptifopen && $db_ppttype == 'client') {
	if (strpos($db_pptloginurl,'?') === false) {
		$db_pptloginurl .= '?';
	} elseif (substr($db_pptloginurl,-1) != '&') {
		$db_pptloginurl .= '&';
	}
	if (strpos($db_pptregurl,'?') === false) {
		$db_pptregurl .= '?';
	} elseif (substr($db_pptregurl,-1) != '&') {
		$db_pptregurl .= '&';
	}
	$urlencode	= rawurlencode($db_bbsurl);
	$loginurl	= "$db_pptserverurl/{$db_pptloginurl}forward=$urlencode";
	$loginouturl= "$db_pptserverurl/$db_pptloginouturl&forward=$urlencode&verify=$loginhash";
	$regurl		= "$db_pptserverurl/{$db_pptregurl}forward=$urlencode";
} else {
	$loginurl	= 'login.php';
	$loginouturl= "login.php?action=quit&verify=$loginhash";
	$regurl		= $db_registerfile;
}

$ol_offset = (int)GetCookie('ol_offset');
$skinco	   = GetCookie('skinco');
if ($db_refreshtime && $REQUEST_URI == $lastpath && $onbbstime < $db_refreshtime) {
	!GetCookie('winduser') && $groupid = 'guest';
	$skin = $skinco ? $skinco : $db_defaultstyle;
	Showmsg('refresh_limit');
}
if (!$db_bbsifopen && !defined('CK')) {
	require_once(R_P.'require/bbsclose.php');
}

$H_url =& $db_wwwurl;
$B_url =& $db_bbsurl;
$_time		= array('hours'=>get_date($timestamp,'G'),'day'=>get_date($timestamp,'j'),'week'=>get_date($timestamp,'w'));
$tdtime		= PwStrtoTime(get_date($timestamp,'Y-m-d'));
$montime	= PwStrtoTime(get_date($timestamp,'Y-m').'-1');

if (!defined('CK') && ($_COOKIE || $timestamp%3 == 0)) {
	switch (SCR) {
		case 'thread': $lastpos = "F$fid";break;
		case 'read': $lastpos = "T$tid";break;
		case 'cate': $lastpos = "C$fid";break;
		case 'index': $lastpos = 'index';break;
		case 'mode': $lastpos = $db_mode;break;
		default: $lastpos = 'other';
	}

	if ($timestamp-$lastvisit>$db_onlinetime || $lastpos != GetCookie('lastpos')) {
		$runfc = 'Y';
		Cookie('lastpos',$lastpos);
	}
}
if (is_numeric($winduid) && strlen($windpwd)>=16) {
	$winddb	  = User_info();
	
	/* performance */
//	$winduid  = $winddb['uid'];
//	$groupid  = $winddb['groupid'];
//	$userrvrc = floor($winddb['rvrc']/10);
//	$windid	  = $winddb['username'];
//	$_datefm  = $winddb['datefm'];
//	$_timedf  = $winddb['timedf'];
//	$credit_pop = $winddb['creditpop'];
	list($winduid,$groupid,$userrvrc,$windid,$_datefm,$_timedf,$credit_pop) = array($winddb['uid'],$winddb['groupid'],floor($winddb['rvrc']/10),$winddb['username'],$winddb['datefm'],$winddb['timedf'],$winddb['creditpop']);

	
	if ($credit_pop && $db_ifcredit) {//Credit Changes Tips
		$credit_pop = str_replace(array('&lt;','&quot;','&gt;'),array('<','"','>'),$credit_pop);
		$creditdb = explode('|',$credit_pop);
		$credit_pop = Char_cv(GetCreditLang('creditpop',$creditdb['0']));

		unset($creditdb['0']);
		foreach ($creditdb as $val) {
			list($credit_1,$credit_2) = explode(':',$val);
			$credit_pop .= '<span class="b st2 pdD w">'.pwCreditNames($credit_1).'&nbsp;<span class="f24">'.$credit_2.'</span></span>';
		}
		$db->update("UPDATE pw_memberdata SET creditpop='' WHERE uid=".pwEscape($winduid));
	}

	list($winddb['style'],$ifcustomstyle) = explode('|',$winddb['style']);
	$skin	  = $winddb['style'] ? $winddb['style'] : $db_defaultstyle;
	list($winddb['onlineip']) = explode('|',$winddb['onlineip']);
	$groupid == '-1' && $groupid = $winddb['memberid'];
	$curvalue = $db_signcurtype == 'rvrc' ? $userrvrc : $winddb[$db_signcurtype];
	if (getstatus($winddb['userstatus'],10) && (!$winddb['starttime'] && $db_signmoney && strpos($db_signgroup,",$groupid,") !== false && $curvalue > $db_signmoney || $winddb['starttime'] && $winddb['starttime'] != $tdtime)) {
		require_once(R_P.'require/Signfunc.php');
		Signfunc($winddb['starttime'],$curvalue);
	}
	unset($curvalue);
} else {
	$skin	 = $db_defaultstyle;
	$groupid = 'guest';
	$winddb  = $windid = $winduid = $_datefm = $_timedf = '';
}
$verifyhash = GetVerify($winduid);

if ($db_bbsifopen==2 && SCR!='login' && !defined('CK')) {
	require_once(R_P.'require/bbsclose.php');
}
if ($db_ifsafecv && !$safecv && !defined('PRO') && strpos($db_safegroup,",$groupid,") !== false ) {
	Showmsg('safecv_prompt');
}
if ($db_ads && !$windid && (is_numeric($_GET['u']) || ($_GET['a'] && strlen($_GET['a'])<16)) && strpos($pwServer['HTTP_REFERER'],$pwServer['HTTP_HOST'])===false) {
	InitGP(array('u','a'));
	Cookie('userads',"$u\t$a\t".md5($pwServer['HTTP_REFERER']));
} elseif ( $db_ads=='1' && ($cookie_userads = GetCookie('userads')) ) {
	list($u,$a) = explode("\t",$cookie_userads);
	if ((int)$u>0 || ($a && strlen($a)<16)) {
		require_once(R_P.'require/userads.php');
	}
}
unset($u,$a,$cookie_userads);

if ($_POST['skinco']) {
	$skinco = $_POST['skinco'];
} elseif ($_GET['skinco']) {
	$skinco = $_GET['skinco'];
}
if ($skinco && strpos($skinco,'..')===false && file_exists(D_P."data/style/$skinco.php") ) {
	$skin = $skinco;
	Cookie('skinco',$skin);
}

if ($db_columns && !defined('W_P') && !defined('SIMPLE') && !defined('COL')) {
	$j_columns = GetCookie('columns');
	if (!$j_columns) {
		$db_columns==2 && $j_columns = 2;
		Cookie('columns',$j_columns);
	}
	if ($j_columns==2 && (strpos($pwServer['HTTP_REFERER'],$db_bbsurl)===false || strpos($pwServer['HTTP_REFERER'],$db_adminfile)!==false)) {
		strpos($REQUEST_URI,'index.php')===false ? Cookie('columns','1') : ObHeader('columns.php?action=columns');
	}
	unset($j_columns);
}
Ipban();

Cookie('lastvisit',$c_oltime."\t".$timestamp."\t".$REQUEST_URI);

if ($groupid == 'guest' && $db_guestdir && GetGcache()) {
	require_once(R_P.'require/guestfunc.php');
	getguestcache();
}
PwNewDB();
unset($db_whybbsclose,$db_whycmsclose,$db_ipban,$db_diy,$dbhost,$dbuser,$dbpw,$dbname,$pconnect,$manager_pwd,$newmanager);
if ($groupid == 'guest') {
	require_once(D_P.'data/groupdb/group_2.php');
} elseif (file_exists(D_P."data/groupdb/group_$groupid.php")) {
	require_once Pcv(D_P."data/groupdb/group_$groupid.php");
} else {
	require_once(D_P.'data/groupdb/group_1.php');
}

if ($_G['pwdlimitime'] && !defined('PRO') && !CkInArray($windid,$manager) && $timestamp-86400*$_G['pwdlimitime']>$winddb['pwdctime'] ) {
	Showmsg('pwdchange_prompt');
}

function runTask(){
	$taskClass = L::loadclass('task');
	$taskClass->run();
}

function runJob(){
	global $db_job_isopen,$winduid,$groupid;
	if(!$db_job_isopen || !$winduid){ /*是否开启用户任务*/
		return;
	}
	$taskClass = L::loadclass('job');
	$taskClass->run($winduid,$groupid);
}

function selectMode(&$m) {
	global $defaultMode,$db_mode,$db_modes,$db_modepages,$pwServer,$db_modedomain;
	if (defined('M_P')) return;
	if (in_array(SCR, array('index', 'cate', 'mode'))) {
		$db_mode = $defaultMode;
		if (!$m && $db_modedomain) {
			$m = array_search($pwServer['HTTP_HOST'], $db_modedomain);
		}
		if ($db_modes && isset($db_modes[$m]) && is_array($db_modes[$m]) && $db_modes[$m]['ifopen']) {
			$db_mode = $m;
		}
		if (!empty($db_mode) && $db_mode!='bbs' && file_exists(R_P."mode/$db_mode/")) {
			define('M_P',R_P."mode/$db_mode/");
			$db_modepages = $db_modepages[$db_mode];
			$GLOBALS['pwModeImg'] = "mode/$db_mode/images";
		}
	}
}

function refreshto($URL,$content,$statime=1,$forcejump=false){
	if (defined('AJAX')) Showmsg($content);
	global $db_ifjump;

	if ($forcejump || ($db_ifjump && $statime>0)) {
		ob_end_clean();
		global $expires,$db_charset,$tplpath,$fid,$imgpath,$db_obstart,$db_bbsname,$B_url,$forumname,$tpctitle,$db_bbsurl;
		$index_name =& $db_bbsname;
		$index_url =& $B_url;
		ObStart();//noizy
		extract(L::style());

		$content = getLangInfo('refreshto',$content);
		@require PrintEot('refreshto');
		$output = str_replace(array('<!--<!---->','<!---->',"\r\n\r\n"),'',ob_get_contents());
		echo ObContents($output);exit;
	} else {
		ObHeader($URL);
	}
}
function ObHeader($URL){
	ob_end_clean();
	header("Location: $URL");exit;
}
function Showmsg($msg_info,$dejump=0){
	@extract($GLOBALS, EXTR_SKIP);
	global $stylepath,$tablewidth,$mtablewidth,$tplpath,$db;
	define('PWERROR',1);
	$msg_info = getLangInfo('msg',$msg_info);
	if (defined('AJAX')) {
		echo $msg_info;ajax_footer();
	}
	$showlogin = false;
	if ($dejump!='1' && $groupid=='guest' && $REQUEST_URI==str_replace(array('register','login'),'',$REQUEST_URI) && (!$db_pptifopen || $db_ppttype != 'client')) {
		if (strpos($REQUEST_URI,'post.php')!==false || strpos($REQUEST_URI,'job.php?action=vote') !== false || strpos($REQUEST_URI,'job.php?action=pcjoin') !== false) {
			$tmpTid = (int)GetGP('tid','GP');
			$tmpTid && $REQUEST_URI = substr($REQUEST_URI,0,strrpos($REQUEST_URI,'/'))."/read.php?tid=$tmpTid&toread=1";
		}
		$jumpurl = "http://".$pwServer['HTTP_HOST'].$REQUEST_URI;
		list(,$qcheck)=explode("\t",$db_qcheck);
		$qkey = $qcheck && $db_question ? array_rand($db_question) : '';
		$showlogin = true;
	}
	extract(L::style());
	list($_Navbar,$_LoginInfo) = pwNavBar();
	ob_end_clean();ObStart();
	require_once PrintEot('showmsg');exit;
}
function GetLang($lang,$EXT='php'){
	global $tplpath;
	
	if (file_exists(R_P."template/$tplpath/lang_$lang.$EXT")) {
		return R_P."template/$tplpath/lang_$lang.$EXT";
	} elseif (file_exists(R_P."template/wind/lang_$lang.$EXT")) {
		return R_P."template/wind/lang_$lang.$EXT";
	} else {
		exit("Can not find lang_$lang.$EXT file");
	}
}
function PrintEot($template,$EXT='htm'){
	//Copyright (c) 2003-09 PHPWind
	global $db_mode,$db_modes,$pwModeImg,$db_tplstyle,$appdir,$tplapps;
	$tplpath = L::style('tplpath');
	!$template && $template = 'N';
	
	//apps template render
	if(!defined('PWERROR')) {
		if(defined('A_P') && $appdir && in_array($template,$tplapps) && file_exists(A_P."$appdir/template/$template.$EXT")){
			return Pcv(A_P."$appdir/template/$template.$EXT");
		}
		if (defined('F_M')/* || ($db_mode && $db_mode != 'bbs')*/) {
			$temp = modeEot($template,$EXT);
			if ($temp) return Pcv($temp);
		}
	}
	//if (defined('A_P') && !in_array($template,array('header','footer'))/* || ($db_mode && $db_mode != 'bbs')*/) {
	//	return A_P."$appdir/template/$template.$EXT";
	//}
	if (file_exists(R_P."template/$tplpath/$template.$EXT")) {
		return Pcv(R_P."template/$tplpath/$template.$EXT");
	} elseif (file_exists(R_P."template/wind/$template.$EXT")) {
		return Pcv(R_P."template/wind/$template.$EXT");
	} else {
		exit("Can not find $template.$EXT file");
	}
}

function Ipban(){
	global $db_ipban;
	if ($db_ipban) {
		global $onlineip,$imgpath,$stylepath;
		$baniparray = explode(',',$db_ipban);
		/* performance */
		//foreach ($baniparray as $banip) {
		//	if ($banip && strpos(",$onlineip.",','.trim($banip).'.')!==false) {
		//		Showmsg('ip_ban');
		//	}
		//}
		if(in_array($onlineip,$baniparray)){
			Showmsg('ip_ban');
		}
	}
}
function Update_ol(){
	global $runfc,$db_online;
	if ($runfc == 'Y') {
		if ($db_online) {
			Sql_ol();
		} else {
			Txt_ol();
		}
		$runfc = 'N';
	}
}
function Txt_ol(){
	global $ol_offset,$winduid,$db_ipstates,$isModify;
	require_once(R_P.'require/userglobal.php');
	if ($winduid>0) {
		list($alt_offset,$isModify) = addonlinefile($ol_offset,$winduid);
	} else {
		list($alt_offset,$isModify) = addguestfile($ol_offset);
	}
	$alt_offset!=$ol_offset && Cookie('ol_offset',$alt_offset);
	$ipscookie = GetCookie('ipstate');
	if ($db_ipstates && ((!$ipscookie && $isModify===1) || ($ipscookie && $ipscookie<$GLOBALS['tdtime']))) {
		require_once(R_P.'require/ipstates.php');
	}
}
function Sql_ol(){
	global $db,$fid,$tid,$timestamp,$windid,$winduid,$onlineip,$groupid,$wind_in,$db_onlinetime,$db_ipstates,$db_today,$lastvisit;
	$olid	= (int)GetCookie('olid');
	$ifhide = $GLOBALS['_G']['allowhide'] && GetCookie('hideid') ? 1 : 0;
	$isModify = 0;
	PwNewDB();
	if ($olid) {
		$sqladd = $winduid ? '(uid='.pwEscape($winduid).' OR olid='.pwEscape($olid).' AND uid=0 AND ip='.pwEscape($onlineip).')' : 'olid='.pwEscape($olid).' AND ip='.pwEscape($onlineip);
		$pwSQL = pwSqlSingle(array(
			'username'	=> $windid,
			'lastvisit'	=> $timestamp,
			'fid'		=> $fid,
			'tid'		=> $tid,
			'groupid'	=> $groupid,
			'action'	=> $wind_in,
			'ifhide'	=> $ifhide,
			'uid'		=> $winduid,
			'ip'		=> $onlineip
		));
		$db->update("UPDATE pw_online SET $pwSQL WHERE $sqladd");
		if ($winduid && $db->affected_rows() > 1) {
			$db->update('DELETE FROM pw_online WHERE uid='.pwEscape($winduid).' AND olid!='.pwEscape($olid));
		}
	} elseif (!$_COOKIE) {
		$pwSQL = pwSqlSingle(array(
			'username'	=> $windid,
			'lastvisit'	=> $timestamp,
			'fid'		=> $fid,
			'tid'		=> $tid,
			'groupid'	=> $groupid,
			'action'	=> $wind_in,
			'ifhide'	=> $ifhide,
			'uid'		=> $winduid
		));
		$db->update("UPDATE pw_online SET $pwSQL WHERE ip=".pwEscape($onlineip));
	}
	if (!$olid && $_COOKIE || $db->affected_rows()==0) {
		$db->update('DELETE FROM pw_online WHERE uid!=0 AND uid='.pwEscape($winduid).' OR lastvisit<'.pwEscape($timestamp-$db_onlinetime));
		$rt = $db->get_one("SELECT MAX(olid) FROM pw_online",MYSQL_NUM);
		$olid = $rt[0]+1;
		$pwSQL = pwSqlSingle(array(
			'olid'		=> $olid,
			'username'	=> $windid,
			'lastvisit'	=> $timestamp,
			'ip'		=> $onlineip,
			'fid'		=> $fid,
			'tid'		=> $tid,
			'groupid'	=> $groupid,
			'action'	=> $wind_in,
			'ifhide'	=> $ifhide,
			'uid'		=> $winduid
		));
		$db->update("REPLACE INTO pw_online SET $pwSQL");
		Cookie('olid',$olid);
		$isModify = 1;
	}
	$ipscookie = GetCookie('ipstate');
	if ($db_ipstates && ((!$ipscookie && $isModify===1) || ($ipscookie && $ipscookie<$GLOBALS['tdtime']))) {
		require_once(R_P.'require/ipstates.php');
	}
	if ($db_today && $timestamp-$lastvisit>$db_onlinetime) {
		require_once(R_P.'require/today.php');
	}
}
function footer() {
	global $db,$db_obstart,$db_footertime,$db_htmifopen,$P_S_T,$mtablewidth,$db_ceoconnect,$wind_version,$imgpath, $stylepath,$footer_ad,$db_union,$timestamp,$db_icp,$db_icpurl,$db_advertdb,$groupid,$SCR,$db_ystats_ifopen,$db_ystats_unit_id,$db_ystats_style,$db_redundancy,$pwServer,$db_ifcredit,$credit_pop,$db_foot,$db_mode,$db_modes,$shortcutforum,$_G,$winddb,$db_toolbar,$winduid,$db_menuinit,$db_appifopen,$db_job_ispop,$db_job_isopen,$db_siteappkey,$db_appbbs,$db_appo;

	defined('AJAX') && ajax_footer();
	Update_ol();

	$wind_spend	= '';
	$ft_gzip = ($db_obstart ? 'Gzip enabled' : 'Gzip disabled').$db_union[3];
	if ($db_footertime == 1){
		$totaltime	= number_format((pwMicrotime()-$P_S_T),6);
		$qn = $db ? $db->query_num : 0;
		$wind_spend	= "Total $totaltime(s) query $qn,";
		
	}
	$ft_time = get_date($timestamp,'m-d H:i');
	$db_icp && $db_icp = "<a href=\"http://www.miibeian.gov.cn\" target=\"_blank\">$db_icp</a>";

	if ($db_toolbar) {
		if ($_COOKIE['toolbarhide']) {
			$toolbarstyle = 'style="display:none"';
			$openbarstyle = '';
			$closebarstyle = 'style="display:none"';
		} else {
			$toolbarstyle = '';
			$openbarstyle = 'style="display:none"';
			$closebarstyle = '';
			if ($db_appifopen && ($db_appbbs || $db_appo)) {
				$appshortcut = trim($winddb['appshortcut'],',');
				if (!empty($appshortcut) && $db_siteappkey) {
					$appshortcut = explode(',',$appshortcut);
					$bottom_appshortcut = array();
					$appclient = L::loadClass('appclient');
					$bottom_appshortcut = $appclient->userApplist($winduid,$appshortcut,1);
				}
			}
		}
	}
	$db_menuinit = trim($db_menuinit,',');

	runJob();

	require PrintEot('footer');
	if ($db_advertdb['Site.PopupNotice'] || $db_advertdb['Site.FloatLeft'] || $db_advertdb['Site.FloatRight'] || $db_advertdb['Site.FloatRand']) {
		require PrintEot('advert');
	}
	$output = ob_get_contents();

	if ($db_htmifopen) {
		$output = preg_replace(
			"/\<a(\s*[^\>]+\s*)href\=([\"|\']?)((index|cate|thread|read|faq|rss)\.php\?[^\"\'>\s]+\s?)[\"|\']?/ies",
			"Htm_cv('\\3','<a\\1href=\"')",
			$output
		);
	}

	if ($db_redundancy && $SCR!='post') {
		/*
		$output = str_replace(
			array("\r","\n\n","\n\t","\n ",">\n","\n<","}\n","{\n",";\n","/\n","\t ",">\t","\t<","}\t","{\t",";\t","/\t",'  ','<!--<!---->','<!---->'),
			array('',"\n",' ',' ','>','<','}','{',';','/',' ','>','<','}','{',';','/',' ','',''),
			$output
		);
		*/
		$output = str_replace(
			array("\r",'<!---->-->','<!--<!---->',"<!---->\n",'<!---->','<!-- -->',"<!--\n-->","\t\t",'        ',"\n\t","\n\n"),
			array('','','','','','','','',"\n","\n"),
			$output
		);
	} else {
		$output = str_replace(array('<!---->-->','<!--<!---->',"<!---->\r\n",'<!---->','<!-- -->',"\t\t\t"),'',$output);
	}
	if ($SCR!='post') {
		$ceversion = defined('CE') ? 1 : 0;
		$output .= "<script language=\"JavaScript\" src=\"http://init.phpwind.net/init.php?sitehash={$GLOBALS[db_sitehash]}&v=$wind_version&c=$ceversion\"></script>";
	}
	if ($groupid == 'guest' && !defined('MSG') && GetGcache()) {
		require_once(R_P.'require/guestfunc.php');
		creatguestcache($output);
	}
	updateCacheData();
	echo ObContents($output);
	unset($output);
	N_flush();
	exit;
}
function updateCacheData(){
	$pw_tplgetdata = L::loadClass('tplgetdata','',true);
	if ($pw_tplgetdata) {
		if ($pw_tplgetdata->updates) {
			$pw_cachedata = L::loadDB('cachedata');
			$pw_cachedata->updates($pw_tplgetdata->updates);
		}
	}
}
function Htm_cv($url,$tag){
	global $db_dir,$db_ext;
	$tmppos = strpos($url,'#');
	$add = $tmppos!==false ? substr($url,$tmppos) : '';
	$url = str_replace(
		array('.php?','=','&amp;','&',$add),
		array($db_dir,'-','-','-',''),
		$url
	).$db_ext.$add;
	return stripslashes($tag).$url.'"';
}
function getUserByUid($uid) {
	global $db;
	$sqladd = $sqltab = '';
	if (in_array(SCR, array('index','read','thread','post'))) {
		$sqladd = (SCR == 'post') ? ',md.postcheck,sr.visit,sr.post,sr.reply' : ',sr.visit';
		$sqltab = "LEFT JOIN pw_singleright sr ON m.uid=sr.uid";
	}
	$detail = $db->get_one("SELECT m.uid,m.username,m.password,m.safecv,m.email,m.oicq,m.groupid,m.memberid,m.groups,m.icon,m.regdate,m.honor,m.timedf, m.style,m.datefm,m.t_num,m.p_num,m.yz,m.newpm,m.userstatus,m.shortcut,md.postnum,md.rvrc,md.money,md.credit,md.currency,md.lastvisit,md.thisvisit,md.onlinetime,md.lastpost,md.todaypost,md.monthpost,md.onlineip,md.uploadtime,md.uploadnum,md.starttime,md.pwdctime,md.monoltime,md.digests,md.f_num,md.creditpop,md.jobnum $sqladd FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid $sqltab WHERE m.uid=" . pwEscape($uid) . " AND m.groupid<>'0' AND md.uid IS NOT NULL");
	return $detail;
}
function User_info() {
	global $db,$timestamp,$db_onlinetime,$winduid,$windpwd,$safecv,$db_ifonlinetime,$c_oltime,$onlineip,$db_ipcheck,$tdtime,$montime,$db_ifsafecv, $db_ifpwcache,$uc_server;
	PwNewDB();
	$detail = getUserByUid($winduid);
	if (empty($detail) && $uc_server) {
		require_once(R_P . 'require/ucuseradd.php');
	}
	$loginout = 0;
	if ($db_ipcheck && strpos($detail['onlineip'],$onlineip) === false) {
		$iparray = explode('.',$onlineip);
		strpos($detail['onlineip'],$iparray[0].'.'.$iparray[1]) === false && $loginout = 1;
	}
	if (!$detail || PwdCode($detail['password']) != $windpwd || ($db_ifsafecv && $safecv != $detail['safecv']) || $loginout || $detail['yz'] > 1) {
		$GLOBALS['groupid'] = 'guest';
		require_once(R_P.'require/checkpass.php');
		Loginout();
		if ($detail['yz'] > 1) {
			$GLOBALS['jihuo_uid'] = $detail['uid'];
			Showmsg('login_jihuo');
		}
		Showmsg('ip_change');
	} else {
		list($detail['shortcut'], $detail['appshortcut']) = explode("\t",$detail['shortcut']);
		unset($detail['password']);
		$detail['honor'] = substrs($detail['honor'],90);
		$distime = $timestamp - $detail['lastvisit'];
		if ($distime > $db_onlinetime || $distime > 3600) {
			//Start elementupdate
			if ($db_ifpwcache & 1 && SCR != 'post' && SCR != 'thread') {
				require_once(R_P.'lib/elementupdate.class.php');
				$elementupdate = new ElementUpdate();
				$elementupdate->userSortUpdate($detail);
			}
			//End elementupdate
			if (!GetCookie('hideid')) {
				$ecpvisit = pwEscape($timestamp,false);
				$ct = 'lastvisit='.$ecpvisit.',thisvisit='.$ecpvisit;
				if ($db_ifonlinetime) {
					$c_oltime = $c_oltime <= 0 ? 0 : ($c_oltime > $db_onlinetime*1.2 ? $db_onlinetime : intval($c_oltime));
					$s_oltime = pwEscape($c_oltime,false);
					$ct .= ',onlinetime=onlinetime+'.$s_oltime;
					if ($detail['lastvisit'] > $montime) {
						$ct .= ',monoltime=monoltime+'.$s_oltime;
					} else {
						$ct .= ',monoltime='.$s_oltime;
					}
					$c_oltime && updateDatanalyse($winduid,'memberOnLine',$c_oltime);
					$c_oltime = 0;
				}
				$db->update("UPDATE pw_memberdata SET $ct WHERE uid=".pwEscape($winduid));
				$detail['lastvisit'] = $detail['thisvisit'] = $timestamp;
			}
		}
	}
	return $detail;
}

function pwAdvert($ckey,$fid=0,$lou=-1,$scr=0) {
	global $timestamp,$db_advertdb,$db_mode,$_time;
	if (empty($db_advertdb[$ckey])) return false;
	$hours = $_time['hours'] + 1;
	$fid || $fid = $GLOBALS['fid'];
	$scr || $scr = $GLOBALS['SCR'];
	$scr = strtolower($scr);
	$lou = (int)$lou;
	$tmpAdvert = $db_advertdb[$ckey];
	if ($db_advertdb['config'][$ckey] == 'rand') {
		shuffle($tmpAdvert);
	}
	$arrAdvert = array();$advert = '';
	foreach ($tmpAdvert as $key=>$value) {
		if ($value['stime'] > $timestamp ||
			$value['etime'] < $timestamp ||
			($value['dtime'] && strpos(",{$value['dtime']},",",{$hours},")===false) ||
			($value['mode'] && strpos($value['mode'],$db_mode)===false) ||
			( $value['page'] &&
				(strpos($value['page'],",$scr,") === false || ($scr == 'read' && $value['page'] == 'thread')) ) ||
			($value['fid'] && $scr != 'index' && strpos(",{$value['fid']},",",$fid,")===false) ||
			($value['lou'] && strpos(",{$value['lou']},",",$lou,")===false)
		) {
			continue;
		}
		if ((!$value['ddate'] && !$value['dweek']) ||
			($value['ddate'] && strpos(",{$value['ddate']},",",{$_time['day']},")!==false) ||
			($value['dweek'] && strpos(",{$value['dweek']},",",{$_time['week']},")!==false)
		) {
			$arrAdvert[] = $value['code'];
			$advert .= is_array($value['code']) ? $value['code']['code'] : $value['code'];
			if ($db_advertdb['config'][$ckey] != 'all') break;
		}
	}
	return array($advert,$arrAdvert);
}

function admincheck($forumadmin,$fupadmin,$username){
	if (!$username) {
		return false;
	}
	if ($forumadmin && strpos($forumadmin,",$username,")!==false) {
		return true;
	}
	if ($fupadmin && strpos($fupadmin,",$username,")!==false) {
		return true;
	}
	return false;
}
function getdirname($path=null){
	if (!empty($path)) {
		if (strpos($path,'\\')!==false) {
			return substr($path,0,strrpos($path,'\\')).'/';
		} elseif (strpos($path,'/')!==false) {
			return substr($path,0,strrpos($path,'/')).'/';
		}
	}
	return './';
}
function allowcheck($allowgroup,$groupid,$groups,$fid='',$allowforum=''){
	if ($allowgroup && strpos($allowgroup,",$groupid,")!==false) {
		return true;
	}
	if ($allowgroup && $groups) {
		$groupids = explode(',',substr($groups,1,-1));
		foreach ($groupids as $value) {
			if (strpos($allowgroup,",$value,")!==false) {
				return true;
			}
		}
	}
	if ($fid && $allowforum && strpos(",$allowforum,",",$fid,")!==false) {
		return true;
	}
	return false;
}
function GetGcache() {
	global $db_fguestnum,$db_tguestnum,$db_guestindex;
	$page = isset($GLOBALS['page']) ? $GLOBALS['page'] : (int)$_GET['page'];
	if (SCR == 'thread' && $page < $db_fguestnum && !isset($_GET['type']) && !GetGP('search')) {
		return true;
	} elseif (SCR == 'read' && $page < $db_tguestnum && !isset($_GET['uid'])) {
		return true;
	} elseif (SCR == 'index' && $db_guestindex && !isset($_GET['cateid'])) {
		return true;
	}
	return false;
}
function GetVerify($str,$app = null) {
	empty($app) && $app = $GLOBALS['db_siteid'];
	return substr(md5($str.$app.$GLOBALS['pwServer']['HTTP_USER_AGENT']),8,8);
}
function PostCheck($verify = 1,$gdcheck = 0,$qcheck = 0,$refer = 1) {
	global $pwServer;
	$verify && checkVerify();
	if ($refer && $pwServer['REQUEST_METHOD'] == 'POST') {
		$referer_a = @parse_url($pwServer['HTTP_REFERER']);
		if ($referer_a['host']) {
			list($http_host) = explode(':',$pwServer['HTTP_HOST']);
			if ($referer_a['host'] != $http_host) {
				Showmsg('undefined_action');
			}
		}
	}
	$gdcheck && GdConfirm($_POST['gdcode']);
	$qcheck && Qcheck($_POST['qanswer'],$_POST['qkey']);
}
function checkVerify($hash = 'verifyhash') {
	GetGP('verify') <> $GLOBALS[$hash] && Showmsg('illegal_request');
}
function GdConfirm($code) {
	Cookie('cknum','',0);
	if (!$code || !SafeCheck(explode("\t",StrCode(GetCookie('cknum'),'DECODE')),strtoupper($code),'cknum',1800)) {
		Showmsg('check_error');
	}
}
function Qcheck($answer,$qkey) {
	global $db_question,$db_answer;
	if ($db_question && (!isset($db_answer[$qkey]) || $answer!=$db_answer[$qkey])) {
		Showmsg('qcheck_error');
	}
}
function PwNewDB() {
	if (!is_object($GLOBALS['db'])) {
		global $db,$database,$dbhost,$dbuser,$dbpw,$dbname,$PW,$charset,$pconnect;
		require_once Pcv(R_P."require/db_$database.php");
		$db = new DB($dbhost, $dbuser, $dbpw, $dbname, $PW, $charset, $pconnect);
	}
}
function Pwloaddl($mod,$ckfunc='mysqli_get_client_info') {
	return extension_loaded($mod) && $ckfunc && function_exists($ckfunc) ? true : false;
}
function setstatus(&$status,$b,$setv = '1') {
	--$b;
	for ($i = strlen($setv)-1; $i >= 0 ; $i--) {
		if ($setv[$i]) {
			$status |= 1 << $b;
		} else {
			$status &= ~(1 << $b);
		}
		++$b;
	}
	//return $status;
}
function sendHeader($num,$rtarr=null){
	static $sapi = null;
	if ($sapi===null) {
		$sapi = php_sapi_name();
	}
	$header_a = array(
		'200' => 'OK',
		'206' => 'Partial Content',
		'304' => 'Not Modified',
		'404' => '404 Not Found',
		'416' => 'Requested Range Not Satisfiable',
	);
	if ($header_a[$num]) {
		if ($sapi=='cgi' || $sapi=='cgi-fcgi') {
			$headermsg = "Status: $num ".$header_a[$num];
		} else {
			$headermsg = "HTTP/1.1: $num ".$header_a[$num];
		}
		if (empty($rtarr)) {
			header($headermsg);
		} else {
			return $headermsg;
		}
	}
	return '';
}
function getLastDate($time,$type = 1){
	global $timestamp,$tdtime;
	static $timelang = false;
	if ($timelang==false) {
		$timelang = array(
			'second'	=>getLangInfo('other','second'),
			'yesterday'	=>getLangInfo('other','yesterday'),
			'hour'		=>getLangInfo('other','hour'),
			'minute'	=>getLangInfo('other','minute'),
			'qiantian'	=>getLangInfo('other','qiantian'),
		);
	}
	$decrease = $timestamp-$time;
	$thistime = PwStrtoTime(get_date($time,'Y-m-d'));
	$thisyear = PwStrtoTime(get_date($time,'Y'));
	$thistime_without_day = get_date($time,'H:i');
	$yeartime = PwStrtoTime(get_date($timestamp,'Y'));
	$result = get_date($time);
	if ($thistime == $tdtime) {
		if ($type == 1){
			if ($decrease <= 60) {
				return array($decrease.$timelang['second'],$result);
			} if ($decrease <= 3600) {
				return array(ceil($decrease/60).$timelang['minute'],$result);
			} else {
				return array(ceil($decrease/3600).$timelang['hour'],$result);
			}
		} else {
			return array(get_date($time,'H:i'),$result);
		}
	} elseif ($thistime == $tdtime-86400) {
		if ($type == 1) {
			return array($timelang['yesterday']." ".$thistime_without_day,$result);
		} else {
			return array(get_date($time,'m-d'),$result);
		}
	} elseif ($thistime == $tdtime-172800) {
		if ($type == 1) {
			return array($timelang['qiantian']." ".$thistime_without_day,$result);
		} else {
			return array(get_date($time,'m-d'),$result);
		}
	} elseif ($thisyear == $yeartime){
		return array(get_date($time,'m-d'),$result);
	} else {
		if ($type == 1) {
			return array(get_date($time,'Y-m-d'),$result);
		} else {
			return array(get_date($time,'y-n-j'),$result);
		}
	}
}
function procLock($t, $u = 0) {
	global $db,$timestamp;
	if ($db->query("INSERT INTO pw_proclock (uid,action,time) VALUES ('$u','$t','$timestamp')",'U',false)) {
		return true;
	}
	$db->update("DELETE FROM pw_proclock WHERE uid='$u' AND action='$t' AND time < '$timestamp' - 30");
	return false;
}
function procUnLock($t = '', $u = 0) {
	$GLOBALS['db']->update("DELETE FROM pw_proclock WHERE uid='$u' AND action='$t'");
}
function pwNavBar() {
	global $winduid,$db_mainnav,$db_menu,$groupid,$winddb,$SCR,$db_modes,$db_mode,$defaultMode,$db_menuinit;

	$tmpLogin = $tmpNav = array();
	if ($groupid != 'guest') {
		require_once(R_P.'require/showimg.php');
		list($tmpLogin['faceurl']) = showfacedesign($winddb['icon'],1,'s');
		$tmpLogin['lastlodate'] = get_date($winddb['lastvisit'],'Y-m-d');
	} else {
		global $db_question,$db_logintype,$db_qcheck;
		if ($db_question) {
			list(,$tmpLogin['qcheck']) = explode("\t",$db_qcheck);
			if ($tmpLogin['qcheck']) $tmpLogin['qkey'] = array_rand($db_question);
		}
		if ($db_logintype) {
			for ($i = 0; $i < 3; $i++) {
				if ($db_logintype & pow(2,$i)) $tmpLogin['logintype'][] = $i;
			}
		} else {
			$tmpLogin['logintype'][0] = 0;
		}
	}
	if (in_array(SCR,array('index','cate','mode','read','thread')) || $SCR == 'm_home') {
		$tmpSel= empty($db_mode) ? 'KEYbbs' : 'KEY'.$db_mode;
	} else {
		$tmpSel = '';
	}
	empty($db_mainnav) && $db_mainnav = array();
	foreach ($db_mainnav as $key=>$value) {
		if ($value['pos'] == '-1' || strpos(",{$value['pos']},",','.$db_mode.',') !== false) {
			$tmpNav['main']['html'] .= $tmpSel == $key ? "<li class=\"current\">{$value['html']}</li>" : "<li>{$value['html']}</li>";
		}
	}
	return array($tmpNav,$tmpLogin);
}
function pwGetShortcut() {
	static $shortcutforum = array();
	if (empty($shortcutforum)) {
		global $winddb,$forum,$winduid,$db_shortcutforum;
		if (trim($winddb['shortcut'],',')) {
			isset($forum) || require(D_P.'data/bbscache/forum_cache.php');
			$tempshortcut = explode(',',$winddb['shortcut']);
			foreach ($tempshortcut as $value) {
				if ($value && isset($forum[$value])) {
					$shortcutforum[$value] = strip_tags($forum[$value]['name']);
				}
			}
		}
		if (empty($shortcutforum)) {
			if (!$db_shortcutforum && $winduid) {
				require_once(R_P.'require/updateforum.php');
				$shortcutforum = updateshortcut();
			} else {
				$shortcutforum = $db_shortcutforum;
			}
		}
	}
	return $shortcutforum;
}

function getSecDomain($url, $mainUrl = null) {
	global $pwServer;
	if ($mainUrl && $url == $mainUrl) {
		return '';
	}
	$dirname = substr($pwServer['HTTP_HOST'], 0, strpos($pwServer['HTTP_HOST'], '.'));
	if (preg_match('/[^\w]' . $dirname . '\./i', $mainUrl)) {
		return '';
	}
	return $dirname;
}

/* performance 2010-2-10 */
function pwMicrotime(){
	$t_array = explode(' ',microtime());
	return $t_array[0] + $t_array[1];
}


?>