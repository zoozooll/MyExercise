<?php
!defined('A_P') && exit('Forbidden');
define('F_M',true);
!$winduid && Showmsg('not_login');
define('AJAX','1');
InitGP(array('a'));
$isGM = CkInArray($windid,$manager);
!$isGM && $groupid==3 && $isGM=1;
$tpc_author = $windid;
if ($a == 'delshare') {
	InitGP(array('id'),'',2);
	if (!$id) Showmsg('undefined_action');
	$share = $db->get_one("SELECT * FROM pw_share WHERE id=".pwEscape($id));
	!$share && Showmsg('mode_o_no_share');
	if ($winduid != $share['uid'] && !$isGM) {
		Showmsg('mode_o_delshare_permit_err');
	}
	$db->update("DELETE FROM pw_share WHERE id=".pwEscape($id));
	if ($affected_rows = delAppAction('share',$id)) {
		countPosts("-$affected_rows");
	}
	//积分变动
	require_once(R_P.'require/credit.php');
	$o_share_creditset = unserialize($o_share_creditset);
	$creditset = getCreditset($o_share_creditset['Delete'],false);
	$creditset = array_diff($creditset,array(0));
	if (!empty($creditset)) {
		require_once(R_P.'require/postfunc.php');
		$credit->sets($share['uid'],$creditset,true);
		updateMemberid($share['uid'],false);
	}

	if ($creditlog = unserialize($o_share_creditlog)) {
		addLog($creditlog['Delete'],$share['username'],$share['uid'],'share_Delete');
	}
	updateUserAppNum($share['uid'],'share','minus');
	echo "success\t";ajax_footer();
} elseif ($a == 'delfriend') {
	InitGP(array('u'),'',2);
	if ($u) {
		if ($friend = getOneFriend($u)) {
			$db->update("DELETE FROM pw_friends WHERE (uid=".pwEscape($winduid)." AND friendid=".pwEscape($u).") OR (uid=".pwEscape($u)." AND friendid=".pwEscape($winduid).")");
			$db->update("UPDATE pw_memberdata SET f_num=f_num-1 WHERE (uid=".pwEscape($winduid)." OR uid=".pwEscape($u).") AND f_num>0");
			echo "success|";ajax_footer();
		} else {
			Showmsg('mode_o_not_friend');
		}
	} else {
		Showmsg('mode_o_not_uid');
	}
} elseif ($a == 'addfriend') {
	InitGP(array('u'),'',2);
	if ($u == $winduid) {
		Showmsg('friend_selferror');
	}
	if ($u) {
		$userstatus = $db->get_value("SELECT userstatus FROM pw_members WHERE uid=".pwEscape($u));
		$friendcheck = getstatus($userstatus,3,3);
		if ($friendcheck == 2) {
			Showmsg('friend_not_add');
		} elseif ($friendcheck == 1) {
			Showmsg('friend_add_check');
		}
		if (getOneFriend($u)) {
			Showmsg('mode_o_is_friend');
		}
		$pwSQL = array();
		$pwSQL[] = array($winduid,$u,$timestamp,0);
		$pwSQL[] = array($u,$winduid,$timestamp,0);
		$db->update("REPLACE INTO pw_friends(uid,friendid,joindate,status) VALUES ".pwSqlMulti($pwSQL,false));
		$db->update("UPDATE pw_memberdata SET f_num=f_num+1 WHERE uid=".pwEscape($winduid)." OR uid=".pwEscape($u));
		$myurl = $db_bbsurl."/".$baseUrl."q=user&u=".$winduid;
		$msg = array(
				'toUid'		=> $u,
				'subject'	=> 'o_friend_success_title',
				'content'	=> 'o_friend_success_cotent',
				'other'		=> array(
					'uid'		=> $winduid,
					'username'	=> $windid,
					'myurl'		=> $myurl
				)
			);
		require_once R_P.'require/msg.php';
		pwSendMsg($msg);
		
		//job sign
		$friend = $db->get_one("SELECT username FROM pw_members WHERE uid=".pwEscape($u));
		initJob($winduid,"doAddFriend",array('user'=>$friend['username']));
		
		pwAddFeed($winduid,'friend','',array('uid' => $friend['uid'], 'friend' => $friend['username']));
			
		echo "success|";ajax_footer();
	} else {
		Showmsg('mode_o_not_uid');
	}
} elseif ($a == 'showcommlist') {

	InitGP(array('type','id'),'P');
	$id = (int)$id;
	if (!$id) Showmsg('undefined_action');
	if(!checkCommType($type)) Showmsg('undefined_action');
	require_once(R_P.'require/showimg.php');
	require_once(R_P.'require/bbscode.php');
	$wordsfb = L::loadClass('FilterUtil');
	$comment = array();
	$query = $db->query("SELECT c.*,m.icon as face,m.groupid FROM pw_comment c LEFT JOIN pw_members m ON c.uid=m.uid WHERE c.type=".pwEscape($type)." AND c.typeid=".pwEscape($id)." AND upid='0' ORDER BY c.postdate DESC".pwLimit(0,100));
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate']);
		if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
			$rt['title'] = getLangInfo('other','ban_comment');
		} elseif (!$wordsfb->equal($rt['ifwordsfb'])) {
			$rt['title'] = $wordsfb->convert($rt['title'], array(
				'id'	=> $rt['id'],
				'type'	=> 'comments',
				'code'	=> $rt['ifwordsfb']
			));
		}
		list($rt['face']) =  showfacedesign($rt['face'],1,'m');
		$comment[] = $rt;
	}
	$str = '';
	if ($comment) {
		$str = pwJsonEncode($comment);
	}
	echo "success\t".$str;
	ajax_footer();

} elseif ($a == 'commreply') {

	require_once(R_P.'require/showimg.php');
	require_once(R_P.'require/postfunc.php');
	banUser();
	InitGP(array('type','id','title','upid'),'P');
	$title 	= str_replace('&#61;','=',$title);
	$id = (int)$id;
	$upid = (int)$upid ? (int)$upid : 0;
	if (!$id) Showmsg('undefined_action');
	if(!checkCommType($type)) Showmsg('undefined_action');
	$app_table = $id_filed = '';
	list($app_table,$id_filed) = getCommTypeTable($type);
	$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_comment WHERE type=".pwEscape($type)." AND typeid=".pwEscape($id));
	if ($count > 99) Showmsg('mode_o_com_count_max');
	if (strlen($title)<3 || strlen($title)>200) {
		Showmsg('mode_o_com_title_error');
	}
	require_once(R_P.'require/bbscode.php');
	$wordsfb = L::loadClass('FilterUtil');
	if (($banword = $wordsfb->comprise($title, true, true)) !== false) {
		Showmsg('title_wordsfb');
	}
	$data = array(
		'uid' 		=> $winduid,
		'username'	=> $windid,
		'title'		=> $title,
		'type'		=> $type,
		'typeid'	=> $id,
		'upid'		=> $upid,
		'postdate'	=> $timestamp,
		'ifwordsfb' => $wordsfb->ifwordsfb(stripslashes($title))
	);
	$db->update("INSERT INTO pw_comment SET ".pwSqlSingle($data));
	$insertid = $db->insert_id();
	if ($app_table && $id_filed) {
		$db->update("UPDATE $app_table SET c_num=c_num+1 WHERE $id_filed=".pwEscape($id));
	}
	if ($insertid) {
		countPosts('+1');
		$threadComment = array(
			'diary'	=> 'diaryComment',
			'photo'	=> 'picComment'
		);
		if (isset($threadComment[$type])) {
			updateDatanalyse($id,$threadComment[$type],1);
		}
		if (strpos($title,'[s:') !== false) {
			$title = showface($title);
		}
		list($face) = showfacedesign($winddb['icon'],1,'m');
		$title = convert($title,$db_windpost);

		echo "success\t".$insertid."\t".$face."\t".$title;
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'commdel') {
	InitGP(array('id'),'P',2);
	if (!$id) Showmsg('undefined_action');
	$thiscomm = $db->get_one("SELECT uid,type,typeid FROM pw_comment WHERE id=".pwEscape($id));
	if (!$isGM) {
		if ($thiscomm['uid'] != $winduid) Showmsg('mode_o_com_del_priv');
	}

	$updatenum = 0;
	$db->update("DELETE FROM pw_comment WHERE id=".pwEscape($id));
	$updatenum += $db->affected_rows();
	$db->update("DELETE FROM pw_comment WHERE upid=".pwEscape($id));
	$updatenum += $db->affected_rows();
	list($app_table,$app_filed) = getCommTypeTable($thiscomm['type']);
	if ($updatenum && $app_table && $thiscomm['typeid']) {
		$db->update("UPDATE $app_table SET c_num=c_num-".pwEscape($updatenum)." WHERE $app_filed=".pwEscape($thiscomm['typeid']));
	}
	countPosts("-$updatenum");
	echo "success\t";
	ajax_footer();
} elseif ($a == 'addfriendtype') {
	InitGP(array('u','name'),'P');
	$u = (int) $u;
	if (!$u) Showmsg('undefined_action');
	if ($u != $winduid && !$isGM) Showmsg('undefined_action');
	if (strlen($name)<1 || strlen($name)>20) Showmsg('mode_o_addftype_name_leng');
	$check = $db->get_one("SELECT ftid FROM pw_friendtype WHERE uid=".pwEscape($u)." AND name=".pwEscape($name));
	if ($check) Showmsg('mode_o_addftype_name_exist');
	$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_friendtype WHERE uid=".pwEscape($u));
	if ($count>=20) Showmsg('mode_o_addftype_length');
	$db->update("INSERT INTO pw_friendtype(uid,name) VALUES(".pwEscape($u).",".pwEscape($name).")");
	$id = $db->insert_id();
	if ($id) {
		echo "success\t$id";
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'delfriendtype') {
	InitGP(array('u','ftid'),'P',2);
	$where = '';
	if (!$isGM) {
		if (!$u) Showmsg('undefined_action');
		if ($u != $winduid) Showmsg('undefined_action');
		$where .= " AND uid=".pwEscape($u);
	}

	if (!$ftid) Showmsg('undefined_action');
	$db->update("DELETE FROM pw_friendtype WHERE ftid=".pwEscape($ftid)."$where");
	if ($db->affected_rows()) {
		$db->update("UPDATE pw_friends SET ftid=0 WHERE ftid=".pwEscape($ftid));
		echo "success";
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'eidtfriendtype') {
	InitGP(array('u','ftid','name'),'P');
	$u = (int) $u;
	$ftid = (int) $ftid;
	if (!$u) Showmsg('undefined_action');
	if (!$isGM) {
		if ($u != $winduid) Showmsg('mode_o_addftype_n');
	}
	if (!$ftid) Showmsg('undefined_action');
	if (strlen($name)<1 || strlen($name)>20) Showmsg('mode_o_addftype_name_leng');
	$check = $db->get_one("SELECT ftid FROM pw_friendtype WHERE uid=".pwEscape($u)." AND name=".pwEscape($name));
	if ($check) Showmsg('mode_o_addftype_name_exist');
	$db->update("UPDATE pw_friendtype SET name=".pwEscape($name)." WHERE uid=".pwEscape($u)." AND ftid=".pwEscape($ftid));
	if ($db->affected_rows()) {
		echo "success";
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'postboard') {

	require_once(R_P.'require/postfunc.php');
	banUser();
	InitGP(array('uid','title'),'P');
	$title 	= str_replace('&#61;','=',$title);
	$uid = (int)$uid;
	if (!$uid) Showmsg('undefined_action');
	if ($uid == $winduid) Showmsg('mode_o_board_self');
	if (!isFriend($uid,$winduid)) Showmsg('mode_o_board_not_friend');
	if (strlen($title)>3 && strlen($title)>200) Showmsg('mode_o_board_too_lang');
	$tousername = $db->get_value("SELECT username FROM pw_members WHERE uid=".pwEscape($uid));
	if (!$tousername) Showmsg('undefined_action');

	require_once(R_P.'require/bbscode.php');
	$wordsfb = L::loadClass('FilterUtil');
	if (($banword = $wordsfb->comprise($title, false)) !== false) {
		Showmsg('title_wordsfb');
	}
	$data = array(
		'uid' 		=> $winduid,
		'username'	=> $windid,
		'touid'		=> $uid,
		'title'		=> $title,
		'postdate'	=> $timestamp,
		'ifwordsfb' => $wordsfb->ifwordsfb(stripslashes($title))
	);
	$db->update("INSERT INTO pw_oboard SET ".pwSqlSingle($data));
	$thisid = $db->insert_id();
	if ($thisid) {
		require_once(R_P.'require/msg.php');
		$msg = array(
			'toUid'		=> $uid,
			'subject'	=> 'o_board_success_title',
			'content'	=> 'o_board_success_cotent',
			'other'		=> array(
				'formuid' 	=> $winduid,
				'formname'	=> $windid,
				'touid'		=> $uid
			)
		);
		pwSendMsg($msg);

		pwAddFeed($winduid,'board','',array(
							'lang'		=>'post_board',
							'uid'		=>$winduid,
							'touid'		=> $uid,
							'tousername'=>$tousername
							)
		);
		countPosts('+1');
		require_once(R_P.'require/showimg.php');
		list($myface) = showfacedesign($winddb['icon'],1,'m');
		//require_once(R_P.'require/bbscode.php');
		if (strpos($title,'[s:') !== false) {
			$title = showface($title);
		}
		//require_once(R_P.'require/bbscode.php');
		$title = convert($title,$db_windpost);

		echo "success\t".$thisid."\t".$myface."\t".$title;
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'delboard') {
	InitGP(array('id'),'P',2);
	if (!$id) Showmsg('undefined_action');
	if (!$isGM) {
		$board = $db->get_one("SELECT * FROM pw_oboard WHERE id=".pwEscape($id));
		if (!$board || ($board['uid'] != $winduid && $board['touid'] != $winduid)) Showmsg('undefined_action');
	}
	$db->update("DELETE FROM pw_oboard WHERE id=".pwEscape($id));
	$affected_rows = delAppAction('board',$id)+1;
	countPosts("-$affected_rows");
	echo "success";
	ajax_footer();
} elseif ($a == 'showftlist') {
	InitGP(array('u'),'P',2);
	if (!$u) Showmsg('undefined_action');
	if ($u != $winduid) Showmsg('undefined_action');
	$query = $db->query("SELECT * FROM pw_friendtype WHERE uid=".pwEscape($u)." ORDER BY ftid");
	$types = array();
	while ($rt = $db->fetch_array($query)) {
		$types[] = $rt;
	}
	if (count($types)) {
		$str = pwJsonEncode($types);
	} else {
		$str = '';
	}
	echo "success\t$str";
	ajax_footer();
} elseif ($a == 'setfriendtype') {
	InitGP(array('friendid','ftid'),'P',2);
	!$ftid && $ftid = 0;
	if (!$friendid) Showmsg('undefined_action');
	$db->update("UPDATE pw_friends SET ftid=".pwEscape($ftid)." WHERE uid=".pwEscape($winduid)." AND friendid=".pwEscape($friendid));
	echo "success";
	ajax_footer();
} elseif ($a == 'adddiarytype') {
	InitGP(array('u','name','b'),'P');
	if ((int)$b == 1) {
		echo "success\t$b";
		ajax_footer();
	}
	$u = (int) $u;
	if (!$u) Showmsg('undefined_action');
	if ($u != $winduid && !$isGM) Showmsg('undefined_action');

	if (strlen($name)<1 || strlen($name)>20) Showmsg('mode_o_adddtype_name_leng');
	$check = $db->get_one("SELECT dtid FROM pw_diarytype WHERE uid=".pwEscape($u)." AND name=".pwEscape($name));
	if ($check) Showmsg('mode_o_adddtype_name_exist');
	$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_diarytype WHERE uid=".pwEscape($u));
	if ($count>20) Showmsg('mode_o_adddtype_length');
	$db->update("INSERT INTO pw_diarytype(uid,name) VALUES(".pwEscape($u).",".pwEscape($name).")");
	$id = $db->insert_id();

	if ($id) {
		echo "success\t$id";
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'deldiarytype') {
	InitGP(array('u','dtid'),'P',2);
	$where = '';
	if (!$isGM) {
		if (!$u) Showmsg('undefined_action');
		if ($u != $winduid) Showmsg('undefined_action');
		$where .= " AND uid=".pwEscape($u);
	}

	if (!$dtid) Showmsg('undefined_action');
	$db->update("DELETE FROM pw_diarytype WHERE dtid=".pwEscape($dtid)."$where");
	if ($db->affected_rows()) {
		$db->update("UPDATE pw_diary SET dtid=0 WHERE dtid=".pwEscape($dtid));
		echo "success";
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'eidtdiarytype') {
	InitGP(array('u','dtid','name'),'P');
	$u = (int) $u;
	$dtid = (int) $dtid;
	if (!$u) Showmsg('undefined_action');
	if (!$isGM) {
		if ($u != $winduid) Showmsg('undefined_action');
	}
	if (!$dtid) Showmsg('undefined_action');
	if (strlen($name)<1 || strlen($name)>20) Showmsg('mode_o_adddtype_name_leng');
	$check = $db->get_one("SELECT dtid FROM pw_diarytype WHERE uid=".pwEscape($u)." AND name=".pwEscape($name));
	if ($check){
		echo "success";ajax_footer();/*friendly prompt*/
		//Showmsg('mode_o_adddtype_name_exist');
	}
	$db->update("UPDATE pw_diarytype SET name=".pwEscape($name)." WHERE uid=".pwEscape($u)." AND dtid=".pwEscape($dtid));
	if ($db->affected_rows()) {
		echo "success";
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}
} elseif ($a == 'deldiary') {
	InitGP(array('id'),'',2);
	if (!$id) Showmsg('undefined_action');
	$diary = $db->get_one("SELECT did,dtid,uid,username FROM pw_diary WHERE did=".pwEscape($id));
	!$diary && Showmsg('mode_o_no_diary');

	if ($winduid != $diary['uid'] && !$isGM) {
		Showmsg('mode_o_deldiary_permit_err');
	}
	$db->update("DELETE FROM pw_diary WHERE did=".pwEscape($id));
	$db->update("UPDATE pw_diarytype SET num=num-1 WHERE dtid=".pwEscape($diary['dtid']));
	if ($affected_rows = delAppAction('diary',$id)) {
		countPosts("-$affected_rows");
	}

	$usercache = L::loadDB('Usercache');
	$usercache->delete($winduid,'diary',$id);
	//积分变动
	require_once(R_P.'require/credit.php');
	$o_diary_creditset = unserialize($o_diary_creditset);
	$creditset = getCreditset($o_diary_creditset['Delete'],false);
	$creditset = array_diff($creditset,array(0));
	if (!empty($creditset)) {
		require_once(R_P.'require/postfunc.php');
		$credit->sets($diary['uid'],$creditset,true);
		updateMemberid($diary['uid'],false);
	}

	if ($creditlog = unserialize($o_diary_creditlog)) {
		addLog($creditlog['Delete'],$diary['username'],$diary['uid'],'diary_Delete');
	}

	updateUserAppNum($diary['uid'],'diary','minus');
	echo "success\t";ajax_footer();
} elseif ($a == 'copydiary') {
	InitGP(array('did','dtid','privacy'));
	if (!$did) Showmsg('undefined_action');
	$diary = $db->get_one("SELECT d.did,d.uid,d.dtid,d.subject,d.content,d.ifconvert,d.ifwordsfb,d.ifcopy,m.username FROM pw_diary d LEFT JOIN pw_members m USING(uid) WHERE d.did=".pwEscape($did));

	!$diary['ifcopy'] && Showmsg('mode_o_copy_permit_err');

	$diary['copyurl'] = $diary['uid']."|".$diary['username']."|{$GLOBALS[db_bbsurl]}/mode.php?m=o&q=diary&space=1&u={$diary[uid]}&did={$diary[did]}";

	/*$rt = $db->get_one("SELECT name FROM pw_diarytype WHERE dtid=".pwEscape($diary['dtid'])." AND uid=".pwEscape($diary['uid']));
	if ($rt['name']) {
		$check = $db->get_one("SELECT dtid FROM pw_diarytype WHERE uid=".pwEscape($winduid)." AND name=".pwEscape($rt['name']));
		$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_diarytype WHERE uid=".pwEscape($winduid));
		if (!$check && $count <= 20) {
			$db->update("INSERT INTO pw_diarytype(uid,name,num) VALUES(".pwEscape($winduid).",".pwEscape($rt['name']).",1)");
			$dtid = $db->insert_id();
		} elseif ($count > 20) {
			$dtid = 0;
		} else {
			$dtid = $check['dtid'];
			$db->update("UPDATE pw_diarytype SET num=num+1 WHERE dtid=".pwEscape($dtid));
		}
	}*///分类不存在则自动生成分类


	$dtid = (int)$dtid;
	$privacy = (int)$privacy;

	$pwSQL = pwSqlSingle(array(
		'uid'		=> $winduid,
		'dtid'		=> $dtid,
		'username'	=> $windid,
		'privacy'	=> $privacy,
		'subject'	=> $diary['subject'],
		'content'	=> $diary['content'],
		'copyurl'	=> $diary['copyurl'],
		'ifcopy'	=> $diary['ifcopy'],
		'ifconvert'	=> $diary['ifconvert'],
		'ifwordsfb'	=> $diary['ifwordsfb'],
		'postdate'	=> $timestamp,
	));


	$db->update("INSERT INTO pw_diary SET $pwSQL");
	$db->update("UPDATE pw_diarytype SET num=num+1 WHERE uid=".pwEscape($winduid)." AND dtid=".pwEscape($dtid));//更新分类日志数
	$did = $db->insert_id();

	pwAddFeed($winduid,'diary',$did,array(
							'lang'=>'diary_copy',
							'username'=>$windid,
							'uid'=>$winduid,
							'did'=>$did,
							'subject'=>$diary['subject'],)
		);
	countPosts('+1');
	updateUserAppNum($winduid,'diary');
	echo "success";ajax_footer();
} elseif ($a == 'feedsetting') {
	if (empty($_POST['step'])) {
		$friend = getFriends($winduid);
		if (empty($friend)) Showmsg('no_friend');
		foreach ($friend as $key => $value) {
			$value['iffeed'] && $checked[$key] = 'CHECKED';
			$frienddb[$value['ftid']][] = $value;
		}
		$query = $db->query("SELECT * FROM pw_friendtype WHERE uid=".pwEscape($winduid)." ORDER BY ftid");
		$friendtype = array();
		while ($rt = $db->fetch_array($query)) {
			$friendtype[$rt['ftid']] = $rt;
		}
		$no_group_name = getLangInfo('other','no_group_name');
		$friendtype[0] = array('ftid' => 0,'uid' => $winduid,'name' => $no_group_name);
		require_once PrintEot('m_ajax');ajax_footer();
	} else {
		InitGP(array('selid'));
		if (!empty($selid)) {
			$db->update("UPDATE pw_friends SET iffeed='1' WHERE uid=".pwEscape($winduid)." AND friendid IN (".pwImplode($selid).")");
			$db->update("UPDATE pw_friends SET iffeed='0' WHERE uid=".pwEscape($winduid)." AND friendid NOT IN (".pwImplode($selid).")");
		} else {
			$db->update("UPDATE pw_friends SET iffeed='0' WHERE uid=".pwEscape($winduid));
		}
		Showmsg('o_feedfriend_success');
	}
} elseif ($a == 'mutiuploadphoto') {
	InitGP(array('aid'));
	!$aid && Showmsg('select_ablum');
	!$winduid && Showmsg('undefined_action');
	$rt = $db->get_one("SELECT atype,ownerid,photonum FROM pw_cnalbum WHERE aid=" . pwEscape($aid));
	if (empty($rt)) {
		Showmsg('undefined_action');
	} elseif ($rt['atype'] == 0 && $winduid != $rt['ownerid']) {
		Showmsg('colony_phototype');
	}
	$o_maxphotonum && $rt['photonum'] > $o_maxphotonum && Showmsg('mutiupload_photofull');
	echo "success";ajax_footer();
}
?>