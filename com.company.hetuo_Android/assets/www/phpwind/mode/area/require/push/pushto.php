<?php
!defined('M_P') && exit('Forbidden');
InitGP(array('step','selid','back'));
$fid = (int)$fid;
$selid = (int)$selid;
if (empty($fid) || empty($selid)) {
	define('AJAX',1);
	Showmsg('undefined_action');
}
require_once(R_P.'require/forum.php');
require_once(R_P.'require/msg.php');
require_once(R_P.'require/writelog.php');
include_once(D_P.'data/bbscache/forum_cache.php');

if (!($foruminfo = L::forum($fid))) {
	define('AJAX',1);
	Showmsg('data_error');
}

(!$foruminfo || $foruminfo['type'] == 'category') && Showmsg('data_error');
wind_forumcheck($foruminfo);

$isGM = CkInArray($windid,$manager);
$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
if (!pwRights($isBM,'areapush') && !$isGM) {
	define('AJAX',1);
	Showmsg('mawhole_right');
}
//TODO 用户操作权限
if (empty($step)) {
	$areaPage = array();
	$mConfig = L::loadDB('mpageconfig');
	$tmpArray = $mConfig->getInvokesByMode('area');
	if (empty($tmpArray)) {
		define('AJAX',1);
		Showmsg('undefined_action');
	}
	$thisCateId = getCateid($fid);
	foreach ($tmpArray as $key => $value) {
		$index = $value['mode'].'_'.$value['scr'].'_';
		if ($value['scr'] == 'index') {
			if (!checkEditAdmin($windid,'index')) continue;
			$areaPage[$index.'0'] = $db_modes[$value['mode']]['m_name'].getLangInfo('other','pushto_index');
		} elseif ($value['scr'] == 'cate') {
			if ($value['fid'] == 0) {
				foreach ($forum as $v) {
					if ($v['type'] == 'category' && $v['fid']==$thisCateId && !isset($areaPage[$index.$v['fid']])) {
						if (!checkEditAdmin($windid,$v['fid'])) continue;
						$areaPage[$index.$v['fid']] = $forum[$v['fid']]['name'];
					}
				}
			} else {
				if (!checkEditAdmin($windid,$value['fid']) || $value['fid']!=$thisCateId) continue;
				$areaPage[$index.$value['fid']] = $forum[$value['fid']]['name'];
			}
		}
	}
	if (!$areaPage){
		define('AJAX',1);
		Showmsg('area_push_right');
	}
	if (!$back && count($areaPage) == 1) {
		$_GET['index'] = key($areaPage);
		$step = 1;
	} else {
		define('AJAX','1');
		include areaLoadFrontView($action);
		ajax_footer();
	}
}
if ($step == 1) {
	InitGP(array('index'));
	list($keyid['mode'],$keyid['scr'],$keyid['fid']) = explode('_',$index);
	$keyid['fid'] = (int)$keyid['fid'];
	if (!isset($db_modes[$keyid['mode']]) || ($keyid['scr'] == 'cate' && !$keyid['fid'])) {
		Showmsg('undefined_action');
	}
	$paramfid = 0;
	if ($keyid['fid']) {
		include_once(D_P.'data/bbscache/area_config.php');
		if ($area_cateinfo && isset($area_cateinfo[$keyid['fid']]) && isset($area_cateinfo[$keyid['fid']]['tpl'])) {
			$paramfid = $keyid['fid'];
		}
	}
	$areaPage = array();$selTitle = '';
	$mConfig = L::loadDB('mpageconfig');
	$areaPage = $mConfig->getInvokes($keyid['mode'],$keyid['scr'],$paramfid);
	if (empty($areaPage)) {
		$areaPage = $mConfig->getInvokes($keyid['mode'],$keyid['scr']);
		if (empty($areaPage)) {
			Showmsg('undefined_action');
		}
	}
	$pw_invoke	= L::loadDB('invoke');
	$invokedata	= $pw_invoke->getDatasByNames($areaPage);
	foreach ($areaPage as $key=>$value) {
		if (!in_array($invokedata[$value]['type'],array('subject','image','player'))) {
			unset($areaPage[$key]);
		} elseif ($invokedata[$value]['ifloop']) {
			if ($invokedata[$value]['loops']) {
				foreach ($invokedata[$value]['loops'] as $v) {
					$areaPage[$areaPage[$key].','.$v] = $forum[$v]['name'];
				}
			}
			unset($areaPage[$key]);
		}
	}
	if ($keyid['scr'] == 'index') {
		$selTitle = $db_modes[$keyid['mode']]['m_name'].getLangInfo('other','pushto_index');
	} elseif ($keyid['scr'] == 'cate') {
		$selTitle = $forum[$keyid['fid']]['name'];
	}
	$index = $keyid['mode'].'_'.$keyid['scr'].'_'.$keyid['fid'];
	if (!$back && count($areaPage) == 1) {
		$_GET['name'] = current($areaPage);
		$_GET['index'] = $index;
		$step = 2;
	} else {
		define('AJAX','1');
		include areaLoadFrontView($action);
		ajax_footer();
	}
}
if ($step == 2) {
	InitGP(array('name','index'));
	list($keyid['mode'],$keyid['scr'],$keyid['fid']) = explode('_',$index);
	$keyid['fid'] = (int)$keyid['fid'];
	if (!isset($db_modes[$keyid['mode']]) || ($keyid['scr'] == 'cate' && !$keyid['fid'])) {
		Showmsg('undefined_action');
	}
	list($tname,$loopid) = explode(',',$name);
	$pw_invokepiece	= L::loadDB('invokepiece');
	$invokepiece = $pw_invokepiece->getDatasByInvokeName($tname);
	empty($invokepiece) && Showmsg('undefined_action');
	$subtitle = $mInvok = array();$invokeparam = $firstKey = '';
	if (count($invokepiece)>1) {
		foreach ($invokepiece as $value) {
			$invokeparam .= $value['id'].':'.$value['num'].',';
			$subtitle[$value['id']] = $value['title'];
			if ($firstKey === '') {
				$firstKey = $value['id'];
				$mInvok = $value;
			}
		}
	} else{
		$mInvok = current($invokepiece);
	}
	$invokeparam = '{'.trim($invokeparam,',').'}';

	$attimages = array();
	$read = $db->get_one("SELECT * FROM pw_threads WHERE tid=".pwEscape($selid));
	empty($read) && Showmsg('data_error');
	$fid	= $read['fid'];
	$cateid = getCateid($read['fid']);
	if (!checkEditAdmin($windid,$cateid)) {
		Showmsg('admin_forum_right');
	}
	$read['postdate'] = get_date($read['postdate']);
	$read['url'] = 'read.php?tid='.$read['tid'];
	$pw_tmsgs = GetTtable($read['tid']);
	$content = $db->get_value("SELECT content FROM $pw_tmsgs WHERE tid=".pwEscape($read['tid'],false));
	require_once R_P.'require/bbscode.php';
	$content = convert($content, $db_windpost);
	$content = substrs(strip_tags($content),300,'N');
	$query = $db->query("SELECT attachurl FROM pw_attachs WHERE uid=".pwEscape($read['authorid'],false)." AND tid=".pwEscape($read['tid'],false)." AND type='img' LIMIT 5");
	while ($rt = $db->fetch_array($query)) {
		$a_url = geturl($rt['attachurl'],'show');
		if ($a_url != 'nopic') {
			$attimages[$rt['attachurl']] = is_array($a_url) ? $a_url[0] : $a_url;
		}
	}
	$selTitle = '';
	if ($keyid['scr'] == 'index') {
		$selTitle = $db_modes[$keyid['mode']]['m_name'].getLangInfo('other','pushto_index');
	} elseif ($keyid['scr'] == 'cate') {
		$selTitle = $forum[$keyid['fid']]['name'];
	}
	$selTitle .= ' -&gt; '.$tname . ($loopid ? ' -&gt; ' . $forum[$loopid]['name'] : '');
	$index = $keyid['mode'].'_'.$keyid['scr'].'_'.$keyid['fid'];

	require_once M_P.'require/pingfen.php';

	require_once R_P.'require/header.php';
	require_once areaLoadFrontView($action);
	footer();

} elseif ($step == 3) {
	InitGP(array('subject','content','titleid','offset','addtype','index','invokename','starttime','endtime','title1','title2','title3','title4','titletime'));
	InitGP(array('url','attachurl','cimgurl'),'P',0);
	$invokeService = L::loadClass('InvokeService');
	$url 		= str_replace('&#61;','=',$url);
	$attachurl 	= str_replace('&#61;','=',$attachurl);
	$cimgurl 	= str_replace('&#61;','=',$cimgurl);
	list($invoke,$loopid) = explode(',',$invokename);
	$loopid = intval($loopid);
	$invokepiece = $invokeService->getInvokePieceByInvokeName($invoke);
	(empty($invokepiece) || !isset($invokepiece[$titleid])) && Showmsg('undefined_action');
	$offset = (int)$offset;
	$mInvoke = $invokepiece[$titleid];
	if ($offset >= $mInvoke['num']) {
		$offset = $mInvoke['num']-1;
	}
	if ($offset < 0) {
		$offset = 0;
	}
	$read = $db->get_one("SELECT * FROM pw_threads WHERE tid=".pwEscape($selid));
	empty($read) && Showmsg('data_error');
	$cateid = getCateid($read['fid']);
	$fid	= $read['fid'];
	$tid	= $selid;
	if (!checkEditAdmin($windid,$cateid)) {
		Showmsg('admin_forum_right');
	}
	$custom = array();
	foreach ($mInvoke['param'] as $key=>$value) {
		switch ($key) {
			case 'title':
				$custom[$key] = $subject;
				break;
			case 'url':
				$custom[$key] = $url;
				break;
			case 'descrip':
				$custom[$key] = $content;
				break;
			case 'image':
				$uploadPicUrl = '';
				if (count($_FILES) && $_FILES["uploadpic"]["name"] && $_FILES["uploadpic"]["size"]) {
					$uploadPicUrl = $invokeService->uploadPicture($_FILES, $mInvoke['id'], $windid);
				}
				$custom[$key] = $uploadPicUrl ? $uploadPicUrl : $cimgurl;
				break;
			case 'author':
				$custom[$key] = $read['author'];
				break;
			case 'forumname':
				$custom[$key] = $forum[$read['fid']]['name'];
				break;
			case 'tagrelate':
				$custom[$key] = array();
				break;

		}
	}
	require_once M_P.'require/pingfen.php';
	if ($custom) {
		$mInvoke['rang'] != 'fid' && $cateid = 0;
		$titlecss = $invokeService->pushDataTitleCss($title1,$title2,$title3,$title4,$titletime);

		if ($addtype) {
			$invokeService->addPushData(array('invokename'=>$invoke,'invokepieceid'=>$mInvoke['id'],'fid'=>$cateid,'loopid'=>$loopid,'editor'=>$windid,'offset'=>$offset,'starttime'=>$starttime,'endtime'=>$endtime,'data'=>$custom,'titlecss'=>$titlecss));
		} else {
			$invokeService->insertPushData(array('invokename'=>$invoke,'invokepieceid'=>$mInvoke['id'],'fid'=>$cateid,'loopid'=>$loopid,'editor'=>$windid,'offset'=>$offset,'starttime'=>$starttime,'endtime'=>$endtime,'data'=>$custom,'titlecss'=>$titlecss));
		}
		$invokeService->deleteCacheData($mInvoke['id'],$cateid,$loopid);
	}

	refreshto("read.php?tid=".$read['tid'],'operate_success');
}

include areaLoadFrontView($action);

footer();
?>