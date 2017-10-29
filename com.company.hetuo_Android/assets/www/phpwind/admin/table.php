<?php
!function_exists('adminmsg') && exit('Forbidden');

$tabledb = array(
	'pw_actions',
	'pw_activity',
	'pw_actmember',
	'pw_administrators',
	'pw_adminlog',
	'pw_adminset',
	'pw_advert',
	'pw_announce',
	'pw_argument',
	'pw_attachbuy',
	'pw_attachs',
	'pw_banuser',
	'pw_bbsinfo',
	'pw_block',
	'pw_buyadvert',
	'pw_cache',
	'pw_cachedata',
	'pw_clientorder',
	'pw_cmembers',
	'pw_cnalbum',
	'pw_cnclass',
	'pw_cnphoto',
	'pw_colonys',
	'pw_config',
	'pw_creditlog',
	'pw_credits',
	'pw_customfield',
	'pw_comment',
	'pw_datastate',
	'pw_debates',
	'pw_debatedata',
	'pw_datanalyse',
	'pw_datastore',
	'pw_diary',
	'pw_diarytype',
	'pw_draft',
	'pw_elements',
	'pw_extragroups',
	'pw_favors',
	'pw_feed',
	'pw_filter',
	'pw_filter_class',
	'pw_filter_dictionary',
	'pw_focus',
	'pw_forumdata',
	'pw_forumlog',
	'pw_forums',
	'pw_forumsell',
	'pw_forumsextra',
	//'pw_forumtype',
	'pw_forummsg',
	'pw_friendtype',
	'pw_friends',
	'pw_hack',
	'pw_help',
	'pw_invitecode',
	'pw_invoke',
	'pw_invokepiece',
	'pw_ipstates',
	'pw_job',
	'pw_jober',
	'pw_medalinfo',
	'pw_medalslogs',
	'pw_medaluser',
	'pw_membercredit',
	'pw_memberdata',
	'pw_memberinfo',
	'pw_members',
	'pw_memo',
	'pw_modehot',
	'pw_msg',
	'pw_msgc',
	'pw_msglog',
	'pw_mpageconfig',
	'pw_nav',
	'pw_online',
	'pw_owritedata',
	'pw_oboard',
	'pw_ouserdata',
	'pw_overprint',
	'pw_pcfield',
	'pw_pcmember',
	'pw_permission',
	'pw_pidtmp',
	'pw_plan',
	'pw_polls',
	'pw_postcate',
	'pw_posts',
	'pw_postsfloor',
	'pw_poststopped',
	'pw_pushpic',
	'pw_proclock',
	'pw_pushdata',
	'pw_pinglog',
	'pw_rate',
	'pw_rateconfig',
	'pw_rateresult',
	'pw_recycle',
	'pw_report',
	'pw_reward',
	'pw_schcache',
	'pw_setform',
	'pw_sharelinks',
	'pw_singleright',
	'pw_smiles',
	'pw_sqlcv',
	'pw_stamp',
	'pw_stopic',
	'pw_stopicblock',
	'pw_stopiccategory',
	'pw_stopicpictures',
	'pw_stopicunit',
	'pw_styles',
	'pw_share',
	'pw_tagdata',
	'pw_tags',
	'pw_task',
	'pw_threads',
	'pw_tmsgs',
	'pw_tpl',
	'pw_tpltype',
	'pw_toollog',
	'pw_tools',
	'pw_topictype',
	'pw_topiccate',
	'pw_topicfield',
	'pw_topicmodel',
	'pw_trade',
	'pw_tradeorder',
	'pw_usercache',
	'pw_usergroups',
	'pw_usertool',
	'pw_ucapp',
	'pw_ucnotify',
	'pw_ucsyncredit',
	'pw_voter',
	'pw_userapp',
	'pw_userbinding',
	'pw_windcode',
	'pw_wordfb'
);

if (trim($db_modelids,',')) {
	$m_list = explode(',',$db_modelids);
	foreach ($m_list as $value) {
		$tabledb[] = 'pw_topicvalue'.intval($value);
	}
}
if (trim($db_pcids,',')) {
	$m_list = explode(',',$db_pcids);
	foreach ($m_list as $value) {
		$tabledb[] = 'pw_pcvalue'.intval($value);
	}
}
if ($db_plist && count($db_plist)>1) {
	foreach ($db_plist as $key => $value) {
		if($key == 0) continue;
		$tabledb[] = 'pw_posts'.$key;
	}
}
if ($db_tlist) {
	!is_array($db_tlist) && $db_tlist = array();
	foreach ($db_tlist as $key => $value) {
		if ($key == 0) continue;
		$tabledb[] = 'pw_tmsgs'.$key;
	}
}

sort($tabledb);

function N_getTabledb($showother=null){//fix bug $PW
	global $tabledb,$PW;
	$table_a = array();
	if ($PW!='pw_') {
		foreach ($tabledb as $key => $value) {
			$table_a[0][$key] = str_replace('pw_',$PW,$value);
		}
	} else {
		$table_a[0] = $tabledb;
	}
	if (!empty($showother)) {
		global $db;
		$table_a[1] = array();
		$query = $db->query('SHOW TABLES');
		while ($rt = $db->fetch_array($query,MYSQL_NUM)) {
			$value = trim($rt[0]);
			!in_array($value,$table_a[0]) && $table_a[1][] = $value;
		}
	}
	return $table_a;
}
?>