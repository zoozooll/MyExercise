<?php
define('SCR','search');
require_once('global.php');
!$_G['allowsearch'] && Showmsg('search_group_right');
if ($groupid!=3 && $groupid!=4) {
	list($db_opensch,$db_schstart,$db_schend) = explode("\t",$db_opensch);
	if ($db_opensch && (($db_schstart > $db_schend) && ($_time['hours']>$db_schend) && ($_time['hours']<$db_schstart) || ($db_schstart < $db_schend) && (($db_schstart>-1 && $_time['hours']<$db_schstart) || ($db_schend>-1 && $_time['hours']>=$db_schend)))) {
		Showmsg('search_opensch');
	}
}
InitGP(array('sch_type','keyword','authorid','step','method','f_fid','sch_time','orderway','asc','pwuser','advanced'));
InitGP(array('sch_area','newatc','digest'),'GP',2);
$sch_area>0 && $_G['allowsearch']!=2 && Showmsg('search_tpost');
if (isset($authorid) && (int)$authorid<1) {
	$errorname = $authorid;
	Showmsg('user_not_exists');
}
if ($sch_time=='newatc') {
	$newatc = 1;
	$sch_time = 86400;
}

if ($keyword) {
	$keyword = str_replace('&nbsp;','',$keyword);
	$skeyword = $keyword;
	$keyword_A = explode(' ',$keyword);
	foreach ($keyword_A as $key=>$value) {
		$value = trim($value);
		if (empty($value)) {
			unset($keyword_A[$key]);
		} else {
			$keyword_A[$key] = $value;
		}
	}
	$keyword = $keyword_A ? implode('|',$keyword_A) : '';
	$keyword && strlen($keyword)<3  && Showmsg('search_word_limit');
	$metakeyword = strip_tags($keyword);
	$subject = "$metakeyword - ";
	$db_metakeyword = str_replace('|',',',$metakeyword);
}
require_once(R_P.'require/header.php');

$forumadd = $p_table = $f = $db_searchinfo = '';
$fidout = array('0');
($newatc || is_numeric($authorid) || $digest) && $step = 2;

require_once(D_P.'data/bbscache/forumcache.php');
$query = $db->query('SELECT fid,allowvisit,password '.($step!=2 ? ',name,f_type' : '')." FROM pw_forums WHERE type<>'category'");
while ($rt = $db->fetch_array($query)) {
	$allowvisit = (!$rt['allowvisit'] || $rt['allowvisit']!=str_replace(",$groupid,",'',$rt['allowvisit'])) ? true : false;
	if ($rt['f_type']=='hidden' && $allowvisit) {
		$forumadd .= "<option value=\"$rt[fid]\"> &nbsp;|- $rt[name]</option>";
	} elseif ($rt['password'] || !$allowvisit) {
		if ($step!=2) {
			$forumcache = preg_replace("/\<option value=\"$rt[fid]\"\>(.+?)\<\/option\>\\r?\\n/is",'',$forumcache);
		} else {
			$fidout[] = $rt['fid'];
		}
	}
}
$fidout = pwImplode($fidout);
$_G['schtime']!='all' && !is_numeric($_G['schtime']) && $_G['schtime'] = 7776000;
list($f,$db_searchinfo) = explode("\t",readover(D_P.'data/bbscache/info.txt'));
$disable = $_G['allowsearch']==1 ? 'disabled' : '';
if ($_G['allowsearch']==2) {
	$t_table = '';
	if ($db_plist && count($db_plist)>1) {

		$p_table = "<select name=\"ptable\">";
		foreach ($db_plist as $key=>$val) {
			$name = $val ? $val : ($key != 0 ? getLangInfo('other','posttable').$key : getLangInfo('other','posttable'));
			$p_table .= "<option value=\"$key\">".$name."</option>";
		}
		$p_table .= '</select>';
	}
	if ($db_tlist) {
		$t_table = '<select name="ttable">';
		foreach ($db_tlist as $key => $value) {
			$name = !empty($value['2']) ? $value['2'] : ($key == 0 ? 'tmsgs' : 'tmsgs'.$key);
			$t_table .= "<option value=\"$key\">$name</option>";
		}
		$t_table .= '</select>';
	}
}

${'time_'.$_G['schtime']} = 'selected';
$method == 'OR' ? $checked_or = 'checked' : $checked_and = 'checked';
$sch_area == 2 ? $checked_2 = 'checked' : ($sch_area == 1 ? $checked_1 = 'checked' : $checked_0 = 'checked');
$checked_disget = $digest==1 ? 'checked' : '';
if ($f_fid) {
	$forumcache = preg_replace("/\<option value=\"$f_fid\"\>(.+?)\<\/option\>(\\r?\\n)/is","<option value=\"".$f_fid."\" selected>\\1</option>\\2",$forumcache);
	$forumadd = preg_replace("/\<option value=\"$f_fid\"\>(.+?)\<\/option\>(\\r?\\n)/is","<option value=\"".$f_fid."\" selected>\\1</option>\\2",$forumadd);
}
$sch_time && ${'time_'.$sch_time} = 'selected';
${'order_'.$orderway} = 'selected';
$asc == 'ASC' ? $asc_ASC = 'checked' : $asc_DESC = 'checked';
if ($step == 2) {
	include(D_P.'data/bbscache/forum_cache.php');
	@set_time_limit(0);
	$keyword_A = array();
	$schedid = '';
	InitGP(array('sid','seekfid','page','ptable'));
	$f_fid = (int)$f_fid;
	!$seekfid && $seekfid = (empty($f_fid) || $f_fid=='all') ? 'all' : $f_fid;
	if ($seekfid != 'all') {
		$seekfid = (int)$seekfid;
	}
	$admincheck = $total = 0;
	$isGM = CkInArray($windid,$manager);
	if ($seekfid!='all') {
		if ($isGM) {
			$admincheck = 1;
		} else {
			$foruminfo = $db->get_one("SELECT forumadmin,fupadmin FROM pw_forums WHERE fid=".pwEscape($seekfid));
			$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
			$pwSystem = pwRights($isBM,false,$seekfid);
			if ($pwSystem && ($pwSystem['tpccheck'] || $pwSystem['digestadmin'] || $pwSystem['lockadmin'] || $pwSystem['pushadmin'] || $pwSystem['coloradmin'] || $pwSystem['downadmin'] || $pwSystem['delatc'] || $pwSystem['moveatc'] || $pwSystem['copyatc'] || $pwSystem['topped'])) {
				$admincheck = 1;
			}
		}
	}
	$superRight = ($SYSTEM['superright'] && $SYSTEM['delatc']) ? true : false;/*超级删除权限*/
	$superEdit = ($SYSTEM['superright'] && $SYSTEM['deltpcs']) ? true : false;/*超级编辑权限*/
	unset($f_fid);
	if($db_sphinx['isopen'] == 1 && $keyword){
		require_once R_P.'require/sphinxsearch.php';
	}else{
		require_once R_P.'require/normalsearch.php';
	}
}
require_once PrintEot('search');footer();

?>