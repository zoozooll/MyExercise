<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=topiccate";

InitGP(array('cateid','modelid'),GP,2);


if (empty($action)){

	@include_once(D_P.'data/bbscache/topic_config.php');
	$topiccatedb = array();
	$query = $db->query("SELECT cateid,name,ifable,vieworder,ifdel FROM pw_topiccate ORDER BY vieworder,cateid");
	while ($rt = $db->fetch_array($query)) {
		$topiccatedb[$rt['cateid']] = $rt;
	}

	$query = $db->query("SELECT modelid,name,cateid,vieworder,ifable FROM pw_topicmodel ORDER BY vieworder,modelid");
	while ($rt = $db->fetch_array($query)) {
		if ($rt['ifable']) {
			if (!$topiccatedb[$rt['cateid']]['model']) {
				$topiccatedb[$rt['cateid']]['model'] = "<a href=\"$basename&action=editmodel&cateid=$rt[cateid]&modelid=$rt[modelid]\">".$rt['name']."</a>";
			} else {
				$topiccatedb[$rt['cateid']]['model'] .= ','."<a href=\"$basename&action=editmodel&cateid=$rt[cateid]&modelid=$rt[modelid]\">".$rt['name']."</a>";
			}
		}
		$topicmodeldb[$rt['modelid']] = $rt['cateid'];
	}

	$modelidb = $modelforumdb = $oldforum = array();
	$query = $db->query("SELECT fid,modelid FROM pw_forums WHERE modelid!='' AND type<>'category'");
	while ($rt = $db->fetch_array($query)) {
		$modelidb[$rt['fid']] = $rt['modelid'];
	}


	foreach ($modelidb as $key => $value) {
		foreach (explode(",",$value) as $val) {
			$modelforumdb[$val][] = $key;
		}
	}

	foreach ($modelforumdb as $k => $value) {
		$cateforumdb[$topicmodeldb[$k]][] = $value;
	}

	foreach ($cateforumdb as $cateid => $value) {
		foreach ($value as $values) {
			foreach ($values as $val) {
				if ($oldforum[$cateid][$forum[$val]['name']] != $forum[$val]['name'] && $topiccatedb[$cateid]) {
					if (!$topiccatedb[$cateid]['forum']) {
						$topiccatedb[$cateid]['forum'] = $forum[$val]['name'];
					} else {
						$topiccatedb[$cateid]['forum'] .= ','.$forum[$val]['name'];
					}
					$oldforum[$cateid][$forum[$val]['name']] = $forum[$val]['name'];
				}

			}
		}
	}

	$ajax_basename_add = EncodeUrl($basename."&action=addtopic");
	$ajax_basename_delmodel = EncodeUrl($basename."&action=delmodel");

	include PrintEot('topiccate');
}  elseif ($action == 'topic') {
	InitGP(array('page','step','field','newfield'));

	$topicdb = $topiccatedb = $topicmodeldb = array();
	@include_once(D_P.'data/bbscache/topic_config.php');
	foreach ($topicmodeldb as $value) {
		$newmodeldb[$value['cateid']][] = $value;
	}
	$sql = '';
	!$modelid && $modelid = $db->get_value("SELECT modelid FROM pw_topicmodel ORDER BY vieworder");

	if ($modelid) {
		$searchhtml = $asearchhtml = '';
		$query = $db->query("SELECT fieldid,name,type,rules,ifsearch,ifasearch,vieworder FROM pw_topicfield WHERE ifable=1 AND (ifsearch=1 OR ifasearch=1) AND modelid=".pwEscape($modelid). "ORDER BY vieworder,fieldid");
		while ($rt = $db->fetch_array($query)) {
			$rt['fieldvalue'] = $field[$rt['fieldid']];
			$rt['rules'] && $rt['rules'] = unserialize($rt['rules']);
			if ($rt['ifsearch'] == 1) {
				$searchhtml .= getSearchHtml($rt);
			} elseif ($rt['ifasearch'] == 1) {
				$asearchhtml .= getSearchHtml($rt);
			}

		}

		$searchhtml && $searchhtml .= '</span>';
		$asearchhtml && $asearchhtml .= '</span>';
		if (strpos($searchhtml,'</span></span>') !== false) {
			$searchhtml = str_replace('</span></span>','</span>',$searchhtml);
		}
		if (strpos($asearchhtml,'</span></span>') !== false) {
			$asearchhtml = str_replace('</span></span>','</span>',$asearchhtml);
		}
		$tablename = GetTopcitable($modelid);
	}

	if ($step == 'search') {
		require_once(R_P.'lib/posttopic.class.php');
		$searchTopic = new postTopic($field);
		if (!$newfield) {
			$newfield = StrCode(serialize($field));
		}
		list($count,$tiddb,$alltiddb) = $searchTopic->getSearchvalue($newfield,'one',true,true);
		is_array($tiddb) && $sql .= " AND tv.tid IN(".pwImplode($tiddb).")";
		is_array($alltiddb) && $alltids = implode(',',$alltiddb);
	}

	if ($step != 'search' || !$count) {

		$alltiddb = $threadb = $newtiddb = array();
		$query = $db->query("SELECT tid FROM $tablename WHERE ifrecycle=0");
		while ($rt = $db->fetch_array($query)) {
			$alltiddb[] = $rt['tid'];
		}
		if ($alltiddb) {
			$query = $db->query("SELECT tid FROM pw_threads WHERE tid IN(".pwImplode($alltiddb).")");
			while ($rt = $db->fetch_array($query)) {
				$threadb[$rt['tid']] = $rt['tid'];
			}
		}
		foreach ($alltiddb as $value) {
			if (!$threadb[$value]) {
				$newtiddb[] = $value;
			}
		}

		if (count($newtiddb) > 0) {
			$db->update("DELETE FROM $tablename WHERE tid IN(".pwImplode($newtiddb).") AND ifrecycle=0");
		}

		is_array($threadb) && $alltids = implode(',',$threadb);
		$count = $db->get_value("SELECT COUNT(tid) as count FROM $tablename WHERE ifrecycle=0");
	}

	if ($count > 0) {
		$page < 1 && $page = 1;
		$numofpage = ceil($count/$db_perpage);
		if ($numofpage && $page > $numofpage) {
			$page = $numofpage;
		}
		$pages = numofpage($count,$page,$numofpage,"$admin_file?adminjob=topiccate&action=topic&modelid=$modelid&newfield=$newfield&step=$step&");
		if ($step != 'search') {
			$start = ($page-1)*$db_perpage;
			$limit = pwLimit($start,$db_perpage);
		}
		$query = $db->query("SELECT tv.tid,t.fid,t.subject,t.author,t.authorid,t.postdate FROM $tablename tv LEFT JOIN pw_threads t ON tv.tid=t.tid WHERE 1 AND ifrecycle=0 $sql ORDER BY t.postdate DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			$rt['postdate'] = get_date($rt['postdate']);
			$topicdb[] = $rt;
		}
	}

	include PrintEot('topiccate');exit;

} elseif ($_POST['sendmsg'] || $action == 'sendmsg') {
	InitGP(array('step','nexto'));
	if (empty($step)) {
		InitGP(array('selid','alltids'));

		if ($selid) {
			$selid = pwImplode($selid);
		} elseif ($alltids) {
			$alltids = explode(',',$alltids);
			$selid = pwImplode($alltids);
		} else {
			adminmsg('operate_error',"$basename&action=topic");
		}

		$uids = '';
		$query = $db->query("SELECT authorid FROM pw_threads WHERE tid IN($selid) GROUP BY authorid");
		while ($rt = $db->fetch_array($query)) {
			$uids .= $uids ? ','.$rt['authorid'] : $rt['authorid'];
		}
		include PrintEot('topiccate');exit;
	} elseif ($step == '2') {
		InitGP(array('subject','atc_content','uids'));
		$cache_file = D_P."data/bbscache/".substr(md5($admin_pwd),10,10).".txt";
		if (!$nexto) {
			writeover($cache_file,$atc_content);
		} else {
			$atc_content = readover($cache_file);
		}

		if (empty($subject) || empty($atc_content)) {
			adminmsg('sendmsg_empty','javascript:history.go(-1);');
		}

		$subject     = Char_cv($subject);
		$sendmessage = Char_cv($atc_content);
		$percount = 1;
		empty($nexto) && $nexto = 1;

		$uids = explode(',',$uids);
		$count = count($uids);

		if ($uids) {
			$uids = pwImplode($uids);

			$msg_a = array();
			$query = $db->query("SELECT uid,username,email,newpm FROM pw_members WHERE uid IN($uids)");
			while (@extract($db->fetch_array($query))) {
				$sendmessage = str_replace("\$email",$email,$atc_content);
				$sendmessage = str_replace("\$windid",$username,$sendmessage);
				$msg_a[] = array($uid,'0','System','rebox','1',$timestamp,$subject,$sendmessage);
			}
			//TODO 新消息提醒
			if ($msg_a) {
				require_once(R_P.'require/msg.php');
				send_msgc($msg_a);
			}
		}
		$havesend = $step*$percount;
		if ($count > ($nexto*$percount)) {
			$nexto++;
			$j_url = "$basename&action=$action&step=2&nexto=$nexto&subject=".rawurlencode($subject);
			adminmsg("sendmsg_step",EncodeUrl($j_url),1);
		} else {
			P_unlink($cache_file);
			adminmsg('operate_success',"$basename&action=topic");
		}
	}

} elseif ($action == 'delthreads') {

	InitGP(array('selid'));

	!$selid && adminmsg('operate_error');
	is_numeric($selid) && $selid = explode(',',$selid);
	$delids = array();
	foreach ($selid as $key => $value) {
		if (is_numeric($value)) {
			$delids[] = $value;
		}
	}

	!$delids && Showmsg('operate_error');

	$delarticle = L::loadClass('DelArticle');
	$readdb = $delarticle->getTopicDb('tid ' . $delarticle->sqlFormatByIds($delids));
	$delarticle->delTopic($readdb, $db_recycle);

	if ($db_ifpwcache ^ 1) {
		$db->update("DELETE FROM pw_elements WHERE type !='usersort' AND id IN(" . pwImplode($delids) . ')');
	}

	# $db->update("DELETE FROM pw_threads WHERE tid IN ($selids)");
	# ThreadManager
    $threadManager = L::loadClass("threadmanager");
	$threadManager->deleteByThreadIds($fid,$selids);

	P_unlink(D_P.'data/bbscache/c_cache.php');

	adminmsg('operate_success',"$basename&action=topic&modelid=$modelid");

} elseif ($action == 'addtopic') {

	define('AJAX',1);

	if (empty($_POST['step'])) {
		$ajax_basename_add = EncodeUrl($basename."&action=addtopic");
		$ifable_Y = 'checked';
		include PrintEot('topiccate');ajax_footer();
	} elseif ($_POST['step'] == 2) {
		InitGP(array('name'));
		InitGP(array('ifable','vieworder'),'P',2);

		$name = trim(ieconvert($name));
		if(!$name || strlen($name) > 14) adminmsg('topic_name');

		$rt = $db->get_one("SELECT cateid FROM pw_topiccate WHERE name=".pwEscape($name));
		$rt['cateid'] && adminmsg('topic_name_exist');

		$db->update("INSERT INTO pw_topiccate"
			. " SET " . pwSqlSingle(array(
				'name'=> $name                , 'ifable'=> $ifable,
				'vieworder'=> $vieworder
		)));
		$cateid = $db->insert_id();

		$db->update("INSERT INTO pw_topicmodel"
			. " SET " . pwSqlSingle(array(
				'name'		=> getLangInfo('other','pc_defaultmodel'),	'cateid'	=> $cateid,
				'vieworder'	=> 0,										'ifable'	=> 1
		)));
		$modelid = $db->insert_id();
		$charset =  $db->charset;
		$createsql = "CREATE TABLE ".$PW."topicvalue".$modelid." (tid mediumint(8) unsigned NOT NULL,fid SMALLINT(6) UNSIGNED NOT NULL ,ifrecycle TINYINT(1) NOT NULL default 0,PRIMARY KEY (tid))";

		if ($db->server_info() >= '4.1') {
			$extra = " ENGINE=MyISAM".($charset ? " DEFAULT CHARSET=$charset" : '');
		} else {
			$extra = " TYPE=MyISAM";
		}

		$createsql = $createsql.$extra;

		$db->query($createsql);

		updatecache_topic();

		Showmsg('topic_add_success');
	}
} elseif ($action == 'edittopic') {

	define('AJAX',1);

	if (empty($_POST['step'])) {

		$ajax_basename_add = EncodeUrl($basename."&action=edittopic&");

		$selectmodel = '';
		$topic = $db->get_one("SELECT cateid,name,ifable,vieworder,ifdel FROM pw_topiccate WHERE cateid=".pwEscape($cateid));

		$query = $db->query("SELECT name,modelid,cateid,ifable FROM pw_topicmodel WHERE cateid=".pwEscape($cateid));
		while ($rt = $db->fetch_array($query)) {
			$checked = '';
			if ($rt['ifable']) $checked = 'checked';
			$selectmodel .= "<span class=\"mr15 w fl\"><input style=\"vertical-align:middle;\" type=\"checkbox\" name=\"modelid[$rt[modelid]]\" value=\"$rt[ifable]\" $checked>$rt[name]</span>";
		}

		ifcheck($topic['ifable'],'ifable');
		include PrintEot('topiccate');ajax_footer();

	} elseif ($_POST['step'] == 2) {
		InitGP(array('name'));
		InitGP(array('ifable','vieworder'),'P',2);

		$name = trim(ieconvert($name));
		if(!$name || strlen($name) > 14) adminmsg('topic_name');

		$rt = $db->get_one("SELECT cateid FROM pw_topiccate WHERE name=".pwEscape($name));
		$rt['cateid'] != $cateid && $rt['cateid'] && adminmsg('topic_name_exist');

		$db->update("UPDATE pw_topiccate"
			. " SET " . pwSqlSingle(array(
					'name'		=> $name,			'ifable'	=> $ifable,
					'vieworder'	=> $vieworder
					))
			. " WHERE cateid=".pwEscape($cateid));


		!is_array($modelid) && $modelid = array();
		$updatedb = array();
		foreach ($modelid as $key => $value) {
			if (is_numeric($key)) {
				$value = (int)$value;
				$updatedb[] = $key;
			}
		}
		if ($updatedb) {
			$db->update("UPDATE pw_topicmodel SET ifable=1 WHERE cateid=".pwEscape($cateid)." AND modelid IN (".pwImplode($updatedb).')');
			$db->update("UPDATE pw_topicmodel SET ifable=0 WHERE cateid=".pwEscape($cateid)." AND modelid NOT IN (".pwImplode($updatedb).')');
		} else {
			adminmsg('model_not_none');
		}
		updatecache_topic();

		Showmsg('topic_edit_success');
	}
} elseif ($action == 'topiclist') {
	InitGP(array('selid','vieworder','name'));

	!is_array($selid) && $selid = array();
	$updatedb = array();
	foreach ($selid as $key => $value) {
		if (is_numeric($key)) {
			$value = (int)$value;
			$updatedb[] = $key;
		}
	}

	if ($updatedb) {
		$db->update("UPDATE pw_topiccate SET ifable=1 WHERE cateid IN (".pwImplode($updatedb).')');
		$db->update("UPDATE pw_topiccate SET ifable=0 WHERE cateid NOT IN (".pwImplode($updatedb).')');
	} else {
		$db->update("UPDATE pw_topiccate SET ifable=0");
	}

	foreach ($vieworder as $key => $value) {
		$key && $db->update("UPDATE pw_topiccate SET vieworder=".pwEscape($value).",name=".pwEscape($name[$key])."WHERE cateid=".pwEscape($key));
	}
	updatecache_topic();
	adminmsg('operate_success',$basename);
} elseif ($action == 'deltopic') {
	$db->UPDATE("DELETE FROM pw_topiccate WHERE cateid=".pwEscape($cateid));
	$delmodeldb = array();
	$query = $db->query("SELECT modelid FROM pw_topicmodel WHERE cateid=".pwEscape($cateid));
	while ($rt = $db->fetch_array($query)) {
		$tablename = GetTopcitable($rt['modelid']);
		$db->query("DROP TABLE $tablename");
		$delmodeldb[] = $rt['modelid'];
	}
	$delmodeldb && $db->UPDATE("DELETE FROM pw_topicfield WHERE modelid IN(".pwImplode($delmodeldb).")");
	$db->UPDATE("DELETE FROM pw_topicmodel WHERE cateid=".pwEscape($cateid));
	updatecache_topic();
	adminmsg('operate_success',$basename);
} elseif ($action == 'editmodel') {
	InitGP(array('step'),GP,2);

	$ajax_basename_add = EncodeUrl($basename."&action=addfield");

	if (empty($step)) {

		$ajax_basename = EncodeUrl($basename);
		$ajax_basename_edit = EncodeUrl($basename."&action=editfield");
		$ajax_basename_delfield = EncodeUrl($basename."&action=delfield");
		$ajax_basename_editindex = EncodeUrl($basename."&action=editindex");

		$cateid = $db->get_value("SELECT cateid FROM pw_topiccate WHERE cateid=".pwEscape($cateid));
		empty($cateid) && adminmsg('illegal_cateid_or_modelid');

		if (empty($modelid)) {
			$modelid = $db->get_value("SELECT modelid FROM pw_topicmodel WHERE cateid=".pwEscape($cateid)." ORDER BY vieworder ASC");
		}
		//获取主题模版

		$modeldb = getModeldbByCateid($cateid);
		//获取当前模板的字段内容
		if ($modelid) {
			$modelname = $db->get_value("SELECT name FROM pw_topicmodel WHERE modelid=".pwEscape($modelid));

			$query = $db->query("SELECT * FROM pw_topicfield WHERE modelid=".pwEscape($modelid)." ORDER BY vieworder,fieldid ASC");
			while ($rt = $db->fetch_array($query)){
				$rt['ifable_checked'] = $rt['ifable'] ? 'checked' : '';
				$rt['ifsearch_checked'] = $rt['ifsearch'] ? 'checked' : '';
				$rt['ifasearch_checked'] = $rt['ifasearch'] ? 'checked' : '';
				$rt['threadshow_checked'] = $rt['threadshow'] ? 'checked' : '';
				$rt['ifmust_checked'] = $rt['ifmust'] ? 'checked' : '';
				$rt['rules'] = unserialize($rt['rules']);
				if ($rt['ifsearch'] || $rt['ifasearch']) {
					$rt['ifindex'] = 1;
				}

				//获取字段的索引状态
				if (in_array($rt['type'],array('textarea','url','image','upload'))) {
					$rt['indexstate'] = '-1';
				} else {
					$tablename = GetTopcitable($modelid);
					$fieldname = $rt['fieldname'];
					$rt['indexstate'] = 0;
					$query2 = $db->query("SHOW KEYS FROM $tablename");
					while($rt2 = $db->fetch_array($query2)) {
						$fieldname == $rt2['Column_name'] && $rt['indexstate'] = 1;
					}
				}
				list($rt['name1'],$rt['name2']) = explode('{#}',$rt['name']);
				$fielddb[$rt['fieldid']] = $rt;
			}
		}

		include PrintEot('topiccate');exit;
	} elseif ($step == '2') {

		InitGP(array('ifable','vieworder','ifsearch','ifasearch','threadshow','ifmust','textsize'));
		foreach ($vieworder as $key => $value) {
			$db->update("UPDATE pw_topicfield SET ".pwSqlSingle(array('ifable'=>$ifable[$key],'vieworder'=>$value,'ifsearch'=>$ifsearch[$key],'ifasearch'=>$ifasearch[$key],'threadshow'=>$threadshow[$key],'ifmust'=>$ifmust[$key],'textsize'=>$textsize[$key]))." WHERE fieldid=".pwEscape($key));
		}
		adminmsg("operate_success",$basename."&action=editmodel&cateid=".$cateid."&modelid=".$modelid);

	} elseif ($step == '3') {

		require_once(R_P.'lib/posttopic.class.php');
		$pwpost = array();
		$postTopic = new postTopic($pwpost);
		$topichtml = $postTopic->getTopicHtml($modelid);
		include PrintEot('topiccate');exit;

	}
} elseif ($action == 'editmodelname') {
	define('AJAX',1);
	if (empty($_POST['step'])) {
		$ajax_basename = EncodeUrl($basename.'&action=editmodelname&');
		//获取主题模版
		$modeldb = getModeldbByCateid($cateid);
		include PrintEot('topiccate');ajax_footer();
	} else {
		InitGP(array('vieworder'),'P',2);
		InitGP(array('name'));
		foreach ($name as $key => $value) {
			strlen($value) > 30 && Showmsg('model_name_too_long');
		}
		foreach($name as $key => $value) {
			$db->update("UPDATE pw_topicmodel SET ".pwSqlSingle(array('name'=>$name[$key],'vieworder'=>$vieworder[$key]))."WHERE modelid=".pwEscape($key));
		}
		updatecache_topic();
		echo "success\t$cateid\t$modelid";ajax_footer();
	}

} elseif ($action == 'addmodelname') {
	define('AJAX',1);
	if (empty($_POST['step'])) {

		$ajax_basename = EncodeUrl($basename.'&action=addmodelname');
		include PrintEot('topiccate');ajax_footer();
	} else {

		InitGP(array('modename'));
		if (strlen($modename) > 30) {
			echo "mode_name_too_long\t";ajax_footer();
		}
		$oldmodel = $db->get_value("SELECT modelid FROM pw_topicmodel WHERE name=".pwEscape($modename)." AND cateid=".pwEscape($cateid));
		if ($oldmodel) {
			echo "samename\t$cateid\t$modelid";ajax_footer();
		}
		$db->update("INSERT INTO pw_topicmodel SET name=".pwEscape($modename).",cateid=".pwEscape($cateid));
		$modelid = $db->insert_id();

		$createsql = "CREATE TABLE ".$PW."topicvalue".$modelid." (tid mediumint(8) unsigned NOT NULL,fid SMALLINT( 6 ) UNSIGNED NOT NULL ,ifrecycle TINYINT(1) NOT NULL default 0,PRIMARY KEY (tid))";

		if ($db->server_info() >= '4.1') {
			$extra = " ENGINE=MyISAM".($charset ? " DEFAULT CHARSET=$charset" : '');
		} else {
			$extra = " TYPE=MyISAM";
		}

		$createsql = $createsql.$extra;

		$db->query($createsql);
		updatecache_topic();
		echo "success\t$cateid\t$modelid";ajax_footer();
	}
} elseif ($action == 'addfield')  {
	define('AJAX',1);

	if (!$_POST['step']) {
		$ajax_basename_add = EncodeUrl($basename."&action=addfield");
		$ajax_basename_copy = EncodeUrl($basename."&action=copyfield");
		$ajax_basename_showfield = EncodeUrl($basename."&action=showfield");

		//获取所有分类
		$query = $db->query("SELECT cateid,name FROM pw_topiccate");
		while ($rt = $db->fetch_array($query)) {
			$select_catedb[$rt['cateid']] = $rt;
		}
		//获取所有模版
		$select_modeldb = getModeldbByCateid();

		include PrintEot('topiccate');ajax_footer();

	} elseif ($_POST['step'] == 2) {
		InitGP(array('fieldtype','name','rule_min','rule_max','rules','descrip'));
		if (empty($fieldtype)) Showmsg('fieldtype_not_exists');
		if ($fieldtype == 'select' || $fieldtype == 'radio' || $fieldtype == 'checkbox') {
			foreach(explode("\n",stripslashes($rules)) as $key => $value) {
				if(strpos($value,'=') == false){
					Showmsg('field_rules_error');
				}
			}
			$s_rules = addslashes(serialize(explode("\n",stripslashes($rules))));
		} elseif ($fieldtype == 'number') {
			if (!$rule_min && $rule_max || $rule_min && !$rule_max) Showmsg('field_number_numerror');
			$rule_min > $rule_max && Showmsg('field_number_error');
			$s_rules = addslashes(serialize(array('minnum' => $rule_min,'maxnum' => $rule_max)));
		} else {
			$s_rules = '';
		}
		if (strlen($descrip) > 255) {
			Showmsg('field_descrip_limit');
		}

		$db->update("INSERT INTO pw_topicfield SET ".pwSqlSingle(array('name'=>$name,'modelid' => $modelid,'type'=>$fieldtype,'rules'=>$s_rules,'descrip'=>$descrip)));
		$fieldid = $db->insert_id();
		$fieldname = 'field'.$fieldid;
		$tablename = GetTopcitable($modelid);
		$db->update("UPDATE pw_topicfield SET fieldname=".pwEscape($fieldname)." WHERE fieldid=".pwEscape($fieldid));
		/*$ckfieldname = $db->get_one("SHOW COLUMNS FROM $tablename LIKE '$fieldname'");
		if ($ckfieldname) {
			Showmsg('field_have_exists');
		} else {
			$sql = getFieldSqlByType($fieldtype);
			$db->query("ALTER TABLE $tablename ADD $fieldname $sql");
		}*/
		$sql = getFieldSqlByType($fieldtype);
		$db->query("ALTER TABLE $tablename ADD $fieldname $sql");
		Showmsg('field_add_success');
	}

} elseif ($action == 'editfield') {
	define('AJAX',1);
	if (!$_POST['step']) {
		$ajax_basename_edit = EncodeUrl($basename."&action=editfield");
		InitGP(array('fieldid'));
		if (empty($fieldid)) Showmsg('field_not_select');
		$fielddb = $db->get_one("SELECT name,fieldname,rules,type,descrip,modelid FROM pw_topicfield WHERE fieldid=".pwEscape($fieldid));
		$tablename = GetTopcitable($fielddb['modelid']);
		$count = $db->get_value("SELECT COUNT(*) FROM $tablename WHERE ".$fielddb['fieldname']." != ''");//查找是否变量已有值
		if ($count) $ifhidden = '1';

		$rules = unserialize($fielddb['rules']);
		$type = $fielddb['type'];
		if ($type == 'number') {
			$minnum = $rules['minnum'];
			$maxnum = $rules['maxnum'];

		} elseif($type == 'select' || $type == 'radio' || $type == 'checkbox') {
			foreach($rules as $key => $value) {
				$rule_content .= ($rule_content ? "\r\n" : '').$value;
			}
		}
		include PrintEot('topiccate');ajax_footer();
	} elseif ($_POST['step'] == 2) {
		InitGP(array('fieldtype','name','rule_min','rule_max','rules','fieldid','descrip'));
		if (empty($fieldid)) Showmsg('field_not_select');
		if (empty($fieldtype)) Showmsg('fieldtype_not_exists');
		if ($fieldtype == 'select' || $fieldtype == 'radio' || $fieldtype == 'checkbox') {
			$s_rules = addslashes(serialize(explode("\n",stripslashes($rules))));
		} elseif ($fieldtype == 'number') {
			if (!$rule_min && $rule_max || $rule_min && !$rule_max) Showmsg('field_number_numerror');
			$rule_min > $rule_max && Showmsg('field_number_error');
			$s_rules = addslashes(serialize(array('minnum' => $rule_min,'maxnum' => $rule_max)));
		} else {
			$s_rules = '';
		}

		//查找字段在表，判断是否有数据，如有数据不可更改字段类型
		$fielddb = $db->get_one("SELECT modelid,ifable,vieworder,ifsearch,ifasearch,threadshow,ifmust,type FROM pw_topicfield WHERE fieldid=".pwEscape($fieldid));
		$tablename = GetTopcitable($fielddb['modelid']);
		$fieldname = 'field'.$fieldid;

		if ($fieldtype != $fielddb['type']) {
			$count = $db->get_value("SELECT COUNT(*) FROM $tablename WHERE $fieldname != ''");
			if ($count) Showmsg('can_not_modify_field_type');
		}

		$db->update("UPDATE pw_topicfield SET ".pwSqlSingle(array('name'=>$name,'type'=>$fieldtype,'rules'=>$s_rules,'descrip'=>$descrip))." WHERE fieldid=".pwEscape($fieldid));

		Showmsg('field_edit_success');
	}
} elseif ($action == 'showfield') {
	define('AJAX',1);
	InitGP(array('currentmodelid'));
	$query = $db->query("SELECT fieldid,name FROM pw_topicfield WHERE modelid=".pwEscape($modelid));

	while ($rt = $db->fetch_array($query)) {
		$fielddb[$rt['fieldid']] = $rt['name'];
	}
	$fielddb = pwJsonEncode($fielddb);
	echo "success\t$fielddb";ajax_footer();
} elseif ($action == 'copyfield') {
	define('AJAX',1);
	InitGP(array('copyfield'));
	if (empty($copyfield) || !is_array($copyfield)) {
		adminmsg('topiccate_copyfield_none');
	}
	$query = $db->query("SELECT name,type,rules,descrip FROM pw_topicfield WHERE fieldid IN (".pwImplode($copyfield).")");
	while ($rt = $db->fetch_array($query)) {
		$name = $db->get_value("SELECT name FROM pw_topicfield WHERE modelid=".pwEscape($modelid));
		$db->update("INSERT INTO pw_topicfield SET ".pwSqlSingle(array('name'=>$rt['name'],'fieldname'=>$rt['fieldname'],'modelid'=>$modelid,'type'=>$rt['type'],'rules'=>$rt['rules'],'descrip'=>$rt['descrip'])),false);
		$fieldid = $db->insert_id();
		$fieldname = 'field'.$fieldid;
		$tablename = GetTopcitable($modelid);
		$db->update("UPDATE pw_topicfield SET fieldname=".pwEscape($fieldname)." WHERE fieldid=".pwEscape($fieldid));
		$ckfieldname = $db->get_one("SHOW COLUMNS FROM $tablename LIKE '$fieldname'");
		if ($ckfieldname) {
			$db->update("DELETE FROM pw_topicfield WHERE fieldid=".pwEscape($fieldid));
			Showmsg('field_have_exists');
		} else {
			$sql = getFieldSqlByType($rt['type']);
			$db->query("ALTER TABLE $tablename ADD $fieldname $sql");
		}
	}
	Showmsg('copy_field_success');

} elseif ($action == 'delfield') {
	define('AJAX',1);
	InitGP(array('fieldid'));
	$ckfield = $db->get_one("SELECT fieldid,modelid FROM pw_topicfield WHERE fieldid=".pwEscape($fieldid));
	if ($ckfield) {
		$tablename = GetTopcitable($ckfield['modelid']);
		$fieldname = 'field'.$ckfield['fieldid'];
		$db->update("DELETE FROM pw_topicfield WHERE fieldid=".pwEscape($fieldid));
		$ckfield2 = $db->get_one("SHOW COLUMNS FROM $tablename LIKE '$fieldname'");
		if ($ckfield2) {
			$db->query("ALTER TABLE $tablename DROP $fieldname");
		} else {
			echo "fail";
		}
		echo "success\t$ckfield[modelid]";
	} else {
		echo "fail";
	}
	ajax_footer();

} elseif ($action == 'editindex') {
	define('AJAX',1);
	InitGP(array('type','fieldid'));
	$tablename = GetTopcitable($modelid);
	$fieldname = 'field'.$fieldid;
	$fielddb = $db->get_one("SELECT * FROM pw_topicfield WHERE fieldid=".pwEscape($fieldid));
	$field = $db->get_one("SHOW COLUMNS FROM $tablename LIKE ".pwEscape($fieldname));
	if (empty($fielddb) || empty($field)) {
		Showmsg('field_not_exists');
	}
	if (in_array($fielddb['type'],array('textarea','url','image','upload'))) {
		Showmsg('field_cannot_modify_index');
	}
	$fieldindex = 0;
	$query = $db->query("SHOW KEYS FROM $tablename");
	while($rt = $db->fetch_array($query)){
		$fieldname == $rt['Column_name'] && $fieldindex = 1;
	}
	if ($type == 'add') {
		if ($fieldindex) {
			Showmsg('field_key_have_exists');
		} else {
			$db->query("ALTER TABLE $tablename ADD INDEX ($fieldname)");
		}
	} else {
		if (empty($fieldindex)) {
			Showmsg('field_key_not_exists');
		} else {
			$db->query("ALTER TABLE $tablename DROP INDEX $fieldname");
		}
	}
	$cateid = $db->get_value("SELECT cateid FROM pw_topicmodel WHERE modelid=".pwEscape($modelid));

	echo "success\t$cateid\t$modelid";ajax_footer();
} elseif ($action == 'delmodel') {

	define('AJAX',1);

	$cateid = $db->get_value("SELECT cateid FROM pw_topicmodel WHERE modelid=".pwEscape($modelid));
	$count = $db->get_value("SELECT COUNT(*) as count FROM pw_topicmodel WHERE cateid=".pwEscape($cateid));
	if ($count == 1) Showmsg('model_mustone');

	$db->update("DELETE FROM pw_topicmodel WHERE modelid=".pwEscape($modelid));
	$db->update("DELETE FROM pw_topicfield WHERE modelid=".pwEscape($modelid));
	$tablename = GetTopcitable($modelid);
	$query = $db->query("SELECT tid FROM $tablename");
	while($rt = $db->fetch_array($query)){
		$tids[] = $rt['tid'];
	}

	$delarticle = L::loadClass('DelArticle');
	$delarticle->delTopicByTids($tids);

	$db->query("DROP TABLE IF EXISTS $tablename");

	updatecache_topic();
	echo "success\t$cateid";ajax_footer();

}


function getModeldbByCateid($cateid) {
	global $db;
	$cateid && $sql = "WHERE cateid=".pwEscape($cateid);
	$query = $db->query("SELECT * FROM pw_topicmodel $sql ORDER BY vieworder ASC");
	while ($rt = $db->fetch_array($query)){
		$modedb[$rt['modelid']] = $rt;
	}
	return $modedb;
}

function getFieldSqlByType($type) {
	if (in_array($type,array('number','calendar'))) {
		$sql = "INT(10) UNSIGNED NOT NULL default '0'";
	} elseif (in_array($type,array('radio','select'))){
		$sql = "TINYINT(3) UNSIGNED NOT NULL default '0'";
	} elseif ($type == 'textarea') {
		$sql = "TEXT NOT NULL";
	} else {
		$sql = "VARCHAR(255) NOT NULL";
	}
	return $sql;
}

function getSearchHtml($data) {
	global $vieworder_mark;
	list($name1,$name2) = explode('{#}',$data['name']);

	if ($data['vieworder'] == 0) {
		$searchhtml .= "<span>";
		$searchhtml .= $name1 ? $name1."：" : '';
	} elseif ($data['vieworder'] != 0) {
		if ($vieworder_mark != $data['vieworder']) {
			if ($vieworder_mark != 0 && $vieworder_mark) $searchhtml .= "</span>";
			$searchhtml .= "<span>";

			$searchhtml .= $name1 ? $name1."：" : '';
		} elseif ($vieworder_mark == $data['vieworder']) {
			$searchhtml .= $name1 ? $name1 : '';
		}
	}

	if (in_array($data['type'],array('radio','select'))) {
		$searchhtml .= "<select name=\"field[$data[fieldid]]\" class=\"input\"><option value=\"\"></option>";
		foreach($data['rules'] as $sk => $sv){
			$sv_value = substr($sv,0,strpos($sv,'='));
			$sv_name = substr($sv,strpos($sv,'=')+1);
			$selected = '';
			if ($sv_value == $data['fieldvalue']) $selected = 'selected';
			$searchhtml .= "<option value=\"$sv_value\" $selected>$sv_name</option>";
		}
		$searchhtml .= "</select>";
	} elseif ($data['type'] == 'checkbox') {
		foreach($data['rules'] as $ck => $cv){
			$cv_value = substr($cv,0,strpos($cv,'='));
			$cv_name = substr($cv,strpos($cv,'=')+1);
			$checked = '';
			if (strpos(",".implode(",",$data['fieldvalue']).",",",".$cv_value.",") !== false) $checked = 'checked';
			$searchhtml .= "<input type=\"checkbox\" class=\"input\" name=\"field[$data[fieldid]][]\" value=\"$cv_value\" $checked/> $cv_name ";
		}
	} elseif ($data['type'] == 'calendar') {
		$searchhtml .= "<input id=\"calendar_start_$data[fieldid]\" type=\"text\" class=\"input\" name=\"field[$data[fieldid]][start]\" value=\"{$data[fieldvalue][start]}\" onclick=\"ShowCalendar(this.id,0)\"/> - <input id=\"calendar_end_$data[fieldid]\" type=\"text\" class=\"input\" name=\"field[$data[fieldid]][end]\" value=\"{$data[fieldvalue][end]}\" onclick=\"ShowCalendar(this.id,0)\"/><script language=\"JavaScript\" src=\"js/date.js\"></script>";
	} elseif ($data['type'] == 'range') {
		$searchhtml .= "<input type=\"text\" size=\"5\" class=\"input\" name=\"field[$data[fieldid]][min]\" value=\"{$data[fieldvalue][min]}\"/> - <input type=\"text\" size=\"5\" class=\"input\" name=\"field[$data[fieldid]][max]\" value=\"{$data[fieldvalue][max]}\"/>";
	} else {
		$searchhtml .= "<input type=\"text\" name=\"field[$data[fieldid]]\" class=\"input\" value=\"$data[fieldvalue]\">";
	}
	if ($data['vieworder'] == 0) {
		$searchhtml .= $name2."</span>";
	} elseif ($data['vieworder'] != 0) {
		$searchhtml .= $name2;
		$vieworder_mark = $data['vieworder'];
	}
	return $searchhtml;
}
?>