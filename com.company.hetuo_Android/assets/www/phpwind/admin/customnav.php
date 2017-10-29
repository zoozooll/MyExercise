<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=customnav&admintype=$admintype";

InitGP(array('action','type'));
foreach ($db_modes as $key=>$value) {
	if (!$value['ifopen']) {
		unset($db_modes[$key]);
	}
}
if ($admintype == 'navmain') {
	unset($db_modes['o']);
	$type = 'main';
} elseif ($admintype == 'navside') {
	$type == "foot" || $type="head";
} elseif ($admintype == 'navmode') {
	foreach ($db_modes as $key=>$value) {
		$navtitle[$key.'_navinfo'] = $value['title'] ? $value['title'] : $value['m_name'];
	}

	$type || $type = 'bbs_navinfo';
	if (!array_key_exists($type,$navtitle)) {
		adminmsg('undefine_action');
	}
	$pos = str_replace('_navinfo','',$type);
	$upnavlist = array();
	$query = $db->query("SELECT nid,title,type FROM pw_nav WHERE type IN (".pwImplode(array_keys($navtitle),false).") AND upid=0 ORDER BY view ASC");
	while ($rt = $db->fetch_array($query)) {
		$upnavlist[$rt['type']][$rt['nid']] = $rt['title'];
	}
} else {
	adminmsg('undefine_action');
}
$navMenu = L::loadClass('navmenu');
$navMenu->settype('bbs_navinfo');
$action || $action = '';

if(!$action){
	$rs	 = $db->query("SELECT * FROM pw_nav WHERE type=".pwEscape($type)."ORDER BY view");
	$nav = $sublinkdb = $linkdb = $navdb = array();
	while($navdb = $db->fetch_array($rs)){
		$style_array = explode("|",$navdb['style']);
		$style_color = $style_array[0];
		$navdb['title'] = strip_tags($navdb['title']);
		$navdb['fontstyle'] = "color:$style_color;";
		if ($style_array[1]) {
			$navdb['fontstyle'] .= "font-weight='bolder';";
		}
		if ($style_array[2]) {
			$navdb['fontstyle'] .= "font-style='italic';";
		}
		if ($style_array[3]) {
			$navdb['fontstyle'] .= "text-decoration='underline';";
		}
		$navdb['isshow'] = $navdb['isshow'] ? "checked" :'';
		$navdb['posname'] = '';
		if ($navdb['pos']) {
			foreach ($db_modes as $key=>$value) {
				if ($navdb['pos'] == '-1' || strpos(",{$navdb['pos']},",",$key,")!==false) {
					$navdb['posname'] .= ($value['title'] ? $value['title'] : $value['m_name']).' ';
				}
			}
		}
		if ($navdb['upid'] == '-1' || $navdb['upid'] == '0') {
			$linkdb[$navdb['nid']] = $navdb;
		} elseif ($navdb['upid'] > 0) {
			$sublinkdb[$navdb['upid']][] = $navdb;
		}
	}

	foreach ($linkdb as $key => $value) {
		$nav[$value['nid']] = $value;
		if ($sublinkdb[$value['nid']]) {
			foreach ($sublinkdb[$value['nid']] as $k => $v) {
				$nav[$v['nid']] = $v;
			}
		}
	}

	if ($type == 'area_navinfo') {
		$area_navshow = 0;
		include_once (D_P.'data/bbscache/area_config.php');
		ifcheck($area_navshow,'navshow');
	}

	require PrintEOT("customnav");exit;
} elseif ($action == "edit") {
	InitGP(array('nid'));
	!isset($nid) && adminmsg("undefine_action");
	@extract($db->get_one("SELECT * FROM pw_nav WHERE nid=".pwEscape($nid)."AND type=".pwEscape($type)));
	$style_array = explode("|",$style);
	$style_color = $style_array[0];
	$fontstyle = "color:$style_color;";
	if ($style_array[1]) {
		$b_check = "checked";
		$fontstyle .= "font-weight='bolder';";
	}
	if ($style_array[2]) {
		$i_check = "checked";
		$fontstyle .= "font-style='italic';";
	}
	if ($style_array[3]) {
		$u_check = "checked";
		$fontstyle .= "text-decoration='underline';";
	}
	unset($upnavlist[$type][$nid]);
	$sel_isshow = $isshow ? 'checked' : '';
	$type == "foot" ? $foot_check = "checked" : $head_check = "checked";
	$target == 1 ? $blank_check = "checked" : $self_check = "checked";
	require PrintEOT("customnav");exit;
} elseif ($action == "add") {
	$blank_check = "checked";
	$sel_isshow = 'checked';
	$pos = 'bbs,area';
	$type == "foot" ? $foot_check = "checked" : $head_check = "checked";
	require PrintEOT("customnav");exit;
} elseif($action=="save"){
	InitGP(array('upid','pos','title','link','view','alt','color','b','i','u','target','nid','isshow'),'P');
	empty($title) && adminmsg("nav_empty_title");
	//empty($link) && adminmsg("nav_empty_link");

	$upid	= (int)$upid[$type];
	$view	= (int)$view;
	$nid	= (int)$nid;
	$style	= $color."|".$b."|".$i."|".$u;
	$isshow = $isshow ? 1 : 0;

	if ($upid > 0 && !isset($upnavlist[$type][$upid])) $upid = 0;
	foreach($pos as $key=>$value){
		if (!isset($db_modes[$value])) {
			unset($pos[$key]);
		}
	}
	if (count($db_modes) ==  count($pos)) {
		$pos = '-1';
	} elseif ($pos) {
		$pos = implode(',',$pos);
	} else {
		$pos = '';
	}

	if(empty($nid)){
		$db->update("INSERT INTO pw_nav"
			. " SET " . pwSqlSingle(array(
				'type'	=> $type,
				'pos'	=> $pos,
				'title'	=> $title,
				'style'	=> $style,
				'link'	=> $link,
				'alt'	=> $alt,
				'target'=> $target,
				'view'	=> $view,
				'upid'	=> $upid,
				'isshow'=> $isshow
		)));
	} else {
		$onenav = $db->get_one("SELECT * FROM pw_nav WHERE nid=".pwEscape($nid));
		if ($upid >0 && $onenav['upid'] <= 0) {
			$count = $db->get_value("SELECT COUNT(*) FROM pw_nav WHERE upid=".pwEscape($nid,false));
			if ($count > 0) {
				adminmsg("nav_hassub","$basename&type=$type");
			}
		}
		$db->update("UPDATE pw_nav"
			. " SET " . pwSqlSingle(array(
				'type'	=> $type,
				'pos'	=> $pos,
				'title'	=> $title,
				'style'	=> $style,
				'link'	=> $link,
				'alt'	=> $alt,
				'target'=> $target,
				'view'	=> $view,
				'upid'	=> $upid,
				'isshow'=> $isshow
			))
			. " WHERE nid=".pwEscape($nid)
		);
		if ($onenav['upid'] > 0) {
			if (!$count = $db->get_value("SELECT COUNT(*) FROM pw_nav WHERE upid=".pwEscape($onenav['upid'],false))) {
				$db->update("UPDATE pw_nav SET upid='0' WHERE nid=".pwEscape($onenav['upid'],false));
			}
		}
	}

	$navMenu->cache();
	adminmsg("operate_success","$basename&type=$type");
} elseif ($action == "del"){
	$nid = (int)GetGP('nid');
	$db->update("DELETE FROM pw_nav WHERE nid=".pwEscape($nid)."AND type=".pwEscape($type));
	$navMenu->cache();
	adminmsg("operate_success","$basename&type=$type");
} elseif ($action=="editview"){
	InitGP(array('view','isshow','title','config'),'P');
	$nids = array();
	foreach($view as $key=>$val){
		if (empty($title[$key])) continue;
		$val = (int)$val;
		$show = $isshow[$key] ? 1 : 0;
		if ($show == 1) {
			$nids[] = $key;
		}
		$db->update("UPDATE pw_nav SET title=".pwEscape($title[$key]).",view=".pwEscape($val).",isshow=".pwEscape($show)."WHERE nid=".pwEscape($key)."AND type=".pwEscape($type));
	}
	if ($nids) {
		$db->update("UPDATE pw_nav SET pos='-1' WHERE nid IN (".pwImplode($nids).") AND type=".pwEscape($type)."AND pos=''");
	}
	if ($type == 'area_navinfo') {
		if ($config['navshow']) {
			$navshow = $db->get_value("SELECT MAX(LENGTH(title)) FROM `pw_nav` WHERE type='area_navinfo'");
			$navshow = ceil($navshow/2)+1;
		} else {
			$navshow = 0;
		}
		$db->pw_update(
			"SELECT hk_name FROM pw_hack WHERE hk_name='area_navshow'",
			"UPDATE pw_hack SET hk_value=".pwEscape($navshow,false)."WHERE hk_name='area_navshow'",
			"INSERT INTO pw_hack SET hk_name='area_navshow',vtype='string',hk_value=".pwEscape($navshow,false)
		);
	}
	$navMenu->cache();
	adminmsg("operate_success","$basename&type=$type");
} elseif ($action == 'restore') {
	$view = 0;$navlists = $uplist = array();
	if ($type == 'area_navinfo') {
		$db->update("DELETE FROM pw_nav WHERE type='area_navinfo'");
		foreach ($forum as $value) {
			if ($value['type'] == 'category' && $value['f_type'] != 'hidden' && $value['cms']!=1 && $value['childid']>0) {
				$navlists[] = array(
					'nkey'	=> $value['fid'],
					'type'	=> $type,
					'pos'	=> 'area',
					'title'	=> strip_tags($value['name']),
					'style'	=> '',
					'link'	=> 'cate.php?cateid='.$value['fid'],
					'alt'	=> '',
					'target'=> 0,
					'view'	=> $view++,
					'upid'	=> 0,
					'isshow'=> 1
				);
			}
		}
	} elseif ($type == 'o_navinfo') {
		$db->update("DELETE FROM pw_nav WHERE type='o_navinfo'");
		$mode_o_nav['index'] = ($db_modes['o']['title'] ? $db_modes['o']['title'] : $db_modes['o']['m_name']).getLangInfo('all','nav_index');
		$mode_o_nav['home'] = getLangInfo('all','mode_o_nav_home');
		$mode_o_nav['user'] = getLangInfo('all','mode_o_nav_user');
		$mode_o_nav['friend'] = getLangInfo('all','mode_o_nav_friend');
		$mode_o_nav['browse'] = getLangInfo('all','mode_o_nav_browse');
		$links = array('index'=>'index.php?m=o','home'=>'mode.php?m=o');
		foreach ($mode_o_nav as $key=>$value) {
			$navlists[] = array(
				'nkey'	=> $key,
				'type'	=> $type,
				'pos'	=> 'o',
				'title'	=> $value,
				'style'	=> '',
				'link'	=> (isset($links[$key])? $links[$key] : 'mode.php?m=o&q='.$key),
				'alt'	=> '',
				'target'=> 0,
				'view'	=> $view++,
				'upid'	=> 0,
				'isshow'=> 1
			);
		}
	} elseif ($type == 'main') {
		$db->update("DELETE FROM pw_nav WHERE type='main'");
		$db_modes = $db->get_value("SELECT db_value FROM pw_config WHERE db_name='db_modes'");
		if (!is_array($db_modes = unserialize($db_modes))) {
			$db_modes = array();
		}
		$tmpSel = empty($db_mode) ? 'bbs' : $db_mode;
		$view = 10;
		$vieworder = array('area'=>'1','bbs'=>'2','o'=>'3');
		foreach ($db_modes as $key=>$value) {
			$link = $db_modedomain[$key] ? 'http://'.$db_modedomain[$key] :($tmpSel == $key ? "./" : 'index.php?m='.$key);
			$target = $key == 'o' ? 1 : 0;
			$navlists[] = array(
				'nkey'	=> $key,
				'type'	=> $type,
				'pos'	=> '-1',
				'title'	=> ($value['title'] ? $value['title'] : $value['m_name']),
				'style'	=> '',
				'link'	=> $link,
				'alt'	=> '',
				'target'=> $target,
				'view'	=> $vieworder[$key] ? $vieworder[$key] : $view++,
				'upid'	=> 0,
				'isshow'=> $value['ifopen']
			);
		}
		$view = 0;
	} elseif ($type == 'bbs_navinfo') {
		$db->update("DELETE FROM pw_nav WHERE type='bbs_navinfo'");
		$mode_bbs_nav = getLangInfo('all','mode_bbs_nav');
		foreach ($db_hackdb as $value) {
			$value[2] != 2 && $uplist['hack_'.$value[1]] = 'hack';
			$navlists[] = array(
				'nkey'	=> 'hack_'.$value[1],
				'type'	=> $type,
				'pos'	=> 'o',
				'title'	=> $value[0],
				'style'	=> '',
				'link'	=> 'hack.php?H_name='.$value[1],
				'alt'	=> '',
				'target'=> 0,
				'view'	=> $view++,
				'upid'	=> 0,
				'isshow'=> ($value[2] ? 1 : 0)
			);
		}
		$navs = explode("\n",$mode_bbs_nav);
		foreach ($navs as $value) {
			if (!trim($value)) continue;
			list($title,$nkey,$link,$upkey) = explode(',',$value);
			$upkey != 'root' && $uplist[$nkey] = $upkey;
			$navlists[] = array(
				'nkey'	=> trim($nkey),
				'type'	=> $type,
				'pos'	=> 'o',
				'title'	=> trim($title),
				'style'	=> '',
				'link'	=> trim($link),
				'alt'	=> '',
				'target'=> 0,
				'view'	=> $view++,
				'upid'	=> 0,
				'isshow'=> 1
			);
		}
	}
	if ($navlists) {
		$db->update("INSERT INTO pw_nav (nkey,type,pos,title,style,link,alt,target,view,upid,isshow) VALUES " . pwSqlMulti($navlists));
	}
	if ($uplist) {
		foreach ($uplist as $key=>$value) {
			$navMenu->setupnav($key,$value);
		}
	}
	$navMenu->cache();
	adminmsg("operate_success","$basename&type=$type");
} else {
	adminmsg("undefine_action");
}
?>