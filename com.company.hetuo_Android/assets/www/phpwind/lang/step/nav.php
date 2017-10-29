<?php
!defined('PW_UPLOAD') && exit('Forbidden');

$db->update("DELETE FROM pw_nav WHERE type!='head' AND type!='foot'");
$maxnid = $db->get_value("SELECT MAX(nid) FROM pw_nav");
$nid = $maxnid+1;
$db->update("INSERT INTO pw_nav (nid, nkey, type, title, style, link, alt, pos, target, view, upid, isshow) VALUES
($nid+3, '', 'head', '|帮助', '|||', 'faq.php', '', 'bbs,area', 1, 0, 0, 1),
($nid+4, 'app', 'bbs_navinfo', '社区应用', '', '', '', '', 0, 0, 0, 1),
($nid+5, 'lastpost', 'bbs_navinfo', '最新帖子', '', 'search.php?sch_time=newatc', '', '', 0, 1, 0, 1),
($nid+6, 'digest', 'bbs_navinfo', '精华区', '', 'search.php?digest=1', '', '', 0, 2, 0, 1),
($nid+7, 'hack', 'bbs_navinfo', '社区服务', '', '', '', '', 0, 3, 0, 1),
($nid+8, 'member', 'bbs_navinfo', '会员列表', '', 'member.php', '', '', 0, 4, 0, 1),
($nid+9, 'sort', 'bbs_navinfo', '统计排行', '', 'sort.php', '', '', 0, 5, 0, 1),
($nid+10, 'search', 'bbs_navinfo', '搜索', '', 'search.php', '', '', 0, 6, 0, 0),
($nid+11, 'faq', 'bbs_navinfo', '帮助', '', 'faq.php', '', '', 0, 7, 0, 0),
($nid+12, 'sort_basic', 'bbs_navinfo', '基本信息', '', 'sort.php', '', '', 0, 8, $nid+9, 1),
($nid+13, 'sort_ipstate', 'bbs_navinfo', '到访IP统计', '', 'sort.php?action=ipstate', '', '', 0, 9, $nid+9, 1),
($nid+14, 'sort_team', 'bbs_navinfo', '管理团队', '', 'sort.php?action=team', '', '', 0, 10, $nid+9, 1),
($nid+15, 'sort_admin', 'bbs_navinfo', '管理操作', '', 'sort.php?action=admin', '', '', 0, 11, $nid+9, 1),
($nid+16, 'sort_online', 'bbs_navinfo', '在线会员', '', 'sort.php?action=online', '', '', 0, 12, $nid+9, 1),
($nid+17, 'sort_member', 'bbs_navinfo', '会员排行', '', 'sort.php?action=member', '', '', 0, 13, $nid+9, 1),
($nid+18, 'sort_forum', 'bbs_navinfo', '版块排行', '', 'sort.php?action=forum', '', '', 0, 14, $nid+9, 1),
($nid+19, 'sort_article', 'bbs_navinfo', '帖子排行', '', 'sort.php?action=article', '', '', 0, 15, $nid+9, 1),
($nid+20, 'sort_taglist', 'bbs_navinfo', '标签排行', '', 'job.php?action=taglist', '', '', 0, 16, $nid+9, 1),
($nid+21, 'index', 'o_navinfo', '圈子首页', '', 'index.php?m=o', '', 'o', 0, 0, 0, 1),
($nid+22, 'home', 'o_navinfo', '我的首页', '', 'mode.php?m=o', '', 'o', 0, 1, 0, 1),
($nid+23, 'user', 'o_navinfo', '个人空间', '', 'mode.php?m=o&q=user', '', 'o', 0, 2, 0, 1),
($nid+24, 'friend', 'o_navinfo', '朋友', '', 'mode.php?m=o&q=friend', '', 'o', 0, 3, 0, 1),
($nid+25, 'browse', 'o_navinfo', '随便看看', '', 'mode.php?m=o&q=browse', '', 'o', 0, 4, 0, 1),
($nid+26, 'app_article', 'bbs_navinfo', '帖子', '', 'apps.php?q=article', '', '', 0, 1, $nid+4, 1),
($nid+27, 'app_photos', 'bbs_navinfo', '相册', '', 'apps.php?q=photos', '', '', 0, 2, $nid+4, 1),
($nid+28, 'app_diary', 'bbs_navinfo', '日志', '', 'apps.php?q=diary', '', '', 0, 3, $nid+4, 1),
($nid+29, 'app_groups', 'bbs_navinfo', '群组', '', 'apps.php?q=groups', '', '', 0, 4, $nid+4, 1),
($nid+30, 'app_hot', 'bbs_navinfo', '热榜', '', 'apps.php?q=hot', '', '', 0, 5, $nid+4, 1),
($nid+31, 'app_share', 'bbs_navinfo', '分享', '', 'apps.php?q=share', '', '', 0, 6, $nid+4, 1),
($nid+32, 'app_write', 'bbs_navinfo', '记录', '', 'apps.php?q=write', '', '', 0, 7, $nid+4, 1)");

$query = $db->query("SELECT * FROM pw_config WHERE db_name IN ('db_modes','db_mode','db_hackdb','db_modedomain')");
while ($rt = $db->fetch_array($query)) {
	$$rt['db_name'] = unserialize($rt['db_value']);
	if (empty($$rt['db_name'])) {
		$$rt['db_name'] = array();
	}
}

$uplist = $navlists = array();
$view = 30;
$tmpSel = empty($db_mode) ? 'bbs' : $db_mode;

$vieworder = array('area'=>'1','bbs'=>'2','o'=>'3');
foreach ($db_modes as $key=>$value) {
	$link = $db_modedomain[$key] ? 'http://'.$db_modedomain[$key] : 'index.php?m='.$key;
	$target = $key == 'o' ? 1 : 0;
	$navlists[] = array(
		'nkey'	=> $key,
		'type'	=> 'main',
		'pos'	=> 'bbs,area',
		'title'	=> ($value['title'] ? $value['title'] : $value['m_name']),
		'style'	=> '',
		'link'	=> $link,
		'alt'	=> '',
		'target'=> $target,
		'view'	=> ($vieworder[$key] ? $vieworder[$key] : $view++),
		'upid'	=> 0,
		'isshow'=> $value['ifopen']
	);
}
$view = 50;
unset($db_hackdb['app']);
foreach ($db_hackdb as $key => $value) {
	if(!file_exists(R_P."hack/$key/index.php")) continue;
	$nkey = 'hack_'.$value[1];
	$value[2] != 2 && $uplist[$nkey] = 'hack';
	$navlists[] = array(
		'nkey'	=> $nkey,
		'type'	=> 'bbs_navinfo',
		'pos'	=> 'bbs',
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
if ($navlists) {
	$db->update("INSERT INTO pw_nav (nkey,type,pos,title,style,link,alt,target,view,upid,isshow) VALUES " . pwSqlMulti($navlists));
}
if ($uplist) {
	$navMenu = L::loadClass('navmenu');
	$navMenu->settype('bbs_navinfo');
	foreach ($uplist as $key=>$value) {
		$navMenu->setupnav($key,$value);
	}
}
?>