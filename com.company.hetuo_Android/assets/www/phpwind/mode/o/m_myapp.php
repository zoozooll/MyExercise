<?php
!defined('M_P') && exit('Forbidden');

!$winduid && Showmsg('not_login');

InitGP(array('action'));
require_once(R_P.'require/showimg.php');
list($faceimg) = showfacedesign($winddb['icon'],1);

if (empty($action)) {

	if (!$db_appo || !$db_appifopen || !$db_siteappkey) Showmsg('app_close');
	
	/*** userapp **/
	$app_array = array();
	$appclient = L::loadClass('appclient');
	$app_array = $appclient->userApplist($winduid);
	$url = $appclient->ShowAppsList();
	/*** userapp **/

	require_once(M_P.'require/header.php');
	require_once PrintEot('m_myapp');footer();

} elseif ($action == 'my') {

	
	$app_array = $basic_app_array = array();

	if ($db_appo) {
		if ($db_siteappkey) {
			$appsdb = array();
			$appclient = L::loadClass('appclient');
			$appsdb = $appclient->getApplist();
		}

		$query = $db->query("SELECT * FROM pw_userapp WHERE uid=" . pwEscape($winduid));
		while ($rt = $db->fetch_array($query)) {
			if (strpos($winddb['appshortcut'],','.$rt['appid'].',') !== false) {
				$rt['showchecked'] = 'checked';
			}
			$rt['allowfeed'] = $rt['allowfeed'] ? 'checked' : '';
			if ($appsdb[$rt['appid']] && $appsdb[$rt['appid']]) {
				$app_array[] = $rt;
			}
		}
	}


	//基础应用列表

	$isshowdb = explode(',',$winddb['appshortcut']);
	$rt = $db->get_one("SELECT owrite_privacy,photos_privacy,diary_privacy,article_isfeed,write_isfeed,diary_isfeed,share_isfeed,photos_isfeed FROM pw_ouserdata WHERE uid=".pwEscape($winduid));
	if (!$rt) {
		$db->query("INSERT INTO pw_ouserdata SET uid=".pwEscape($winduid));
		$rt = $db->get_one("SELECT owrite_privacy,photos_privacy,diary_privacy,article_isfeed,write_isfeed,diary_isfeed,share_isfeed,photos_isfeed FROM pw_ouserdata WHERE uid=".pwEscape($winduid));
	}
	@extract($rt);
	$all_basic_app = array('article','write','diary','share','groups','photos');
	$basic_app_with_privacy = array('write','diary','photos');
	$basic_app_with_feed = array('article','write','diary','share','photos');

	$write_privacy = $owrite_privacy;
	foreach ($all_basic_app as $key => $value) {
		if(!getIfopenOfApp($value)) continue;
		${$value.'_isshow'} = in_array($value,$isshowdb) ? 1 : 0;
		${$value.'_privacy'} = in_array($value,$basic_app_with_privacy) ? ${$value.'_privacy'} : 0;
		$name = getLangInfo('other',$value);
		$showchecked = ${$value.'_isshow'} ? 'checked' : '';
		$feedchecked = ${$value.'_isfeed'} ? 'checked' : '';
		if (in_array($value,$basic_app_with_privacy)) {
			$privacy = ${$value.'_privacy'};
			${'privace_'.$value.'_'.$privacy} = 'selected';
		}
		$basic_app_array[$value] = array('name' => $name, 'isshow' => ${$value.'_isshow'},'privacy' => ${$value.'_privacy'},'isfeed' => ${$value.'_isfeed'},'showchecked' => $showchecked,'feedchecked'=>$feedchecked);

	}
	require_once(M_P.'require/header.php');
	require_once PrintEot('m_myapp');footer();

} elseif ($action == 'del') {

	define('AJAX',1);
	InitGP(array('id'));

	$db->update("DELETE FROM pw_userapp WHERE uid=" . pwEscape($winduid) . ' AND appid=' . pwEscape($id));

	if ($db->affected_rows()) {

		if (!$db_appo || !$db_appifopen || !$db_siteappkey) Showmsg('app_close');

		/*** userapp **/
		$appclient = L::loadClass('appclient');
		$url = $appclient->MoveAppsList($id);
		/*** userapp **/
	}
	echo 'ok';
	ajax_footer();
} elseif ($action == 'edit') {
	InitGP(array('show','privacy','feed'));
	//显示在快捷菜单栏处理
	list($fidshortcut) = explode("\t",$winddb['shortcut']);
	foreach ($show as $key => $value) {
		if ($value == 1) {
			$showshortcut[] = $key;
		}
	}
	$shortcut = $fidshortcut."\t".','.implode(',',$showshortcut).',';

	$db->update("UPDATE pw_members SET shortcut=".pwEscape($shortcut)." WHERE uid=".pwEscape($winduid));

	$basic_app_with_privacy = array('write','diary','photos');
	$SQL = array('uid'=>$winduid);
	foreach ($privacy as $key => $value) {
		if (!in_array($key,$basic_app_with_privacy)) continue;
		if ($key == 'write') {
			$SQL['o'.$key.'_privacy'] = (int)$value;
		} else {
			$SQL[$key.'_privacy'] = (int)$value;
		}
	}

	$basic_app_with_feed = array('article','write','diary','share','photos');
	foreach ($basic_app_with_feed as $key => $value) {
		$SQL[$value.'_isfeed'] = (int)$feed[$value];
	}

	$appid = array();
	foreach($feed as $key => $value){
		if ($value && !in_array($key,$basic_app_with_feed)) {
			$appid[] = $key;
		}
	}
	if ($appid) {
		$db->update("UPDATE pw_userapp SET allowfeed='0' WHERE uid=".pwEscape($winduid)." AND appid NOT IN (".pwImplode($appid).")");
		$db->update("UPDATE pw_userapp SET allowfeed='1' WHERE uid=".pwEscape($winduid)." AND appid IN (".pwImplode($appid).")");
	}

	$db->pw_update(
		"SELECT uid FROM pw_ouserdata WHERE uid=".pwEscape($winduid),
		"UPDATE pw_ouserdata SET ".pwSqlSingle($SQL)." WHERE uid=".pwEscape($winduid),
		"INSERT INTO pw_ouserdata SET ".pwSqlSingle($SQL)
	);
	refreshto($basename."q=myapp&action=my",'myapp_success');
}

function getIfopenOfApp($app) {
	global $db_dopen,$db_share_open,$db_groups_open,$db_phopen;
	switch ($app) {
		case 'diary' :
			$return = $db_dopen ? '1' : '0';
			break;
		case 'share' :
			$return = $db_share_open ? '1' : '0';
			break;
		case 'groups' :
			$return = $db_groups_open ? '1' : '0';
			break;
		case 'photos' :
			$return = $db_phopen ? '1' : '0';
			break;
		default:
			$return = '1';
	}
	return $return;
}
?>