<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');
if ($db_question && $o_share_qcheck) {
	$qkey = array_rand($db_question);
}
!$db_share_open && Showmsg('share_close');

require_once(R_P.'require/showimg.php');

InitGP(array('a','type','space'),null,1);
$u = (int)GetGP('u');
!$u && $u = $winduid;
if ($space == 1 && defined('F_M')) {
	//$basename .= "space=1&u=$u&";
	$spaceurl = $baseUrl;
	$userdb = $db->get_one("SELECT index_privacy FROM pw_ouserdata WHERE uid=".pwEscape($u));
	list($isU,$privacy) = pwUserPrivacy($u,$userdb);
	if ($groupid == 3 || $isU == 2 || $isU !=2 && $privacy['index']) {
		$SpaceShow = 1;
	}
	if (!$SpaceShow) Showmsg('mode_o_index_right');
}
//$thisbase = $basename."q=share&";
$sum = 0;
$where = '';
InitGP(array('see'),null,1);
if ($a == 'my' && $see == 'postfavor'){
	 if($a == 'my') {
		$isU = false;
		$u == $winduid && $isU = true;
		!$isU && Showmsg('space_over_right');
		InitGP(array('job','ftype'));
		//$space == 1 && $basename = "mode.php?m=o&space=1&";
		if (empty($job)) {
			$page = (int)GetGP('page');
			!$ftype && $ftype = 'all';
			$ftype == '-1' && $ftype = 'def';
			if ($u!=$winduid) {
				if ($friend != getOneFriend($u) && $groupid != 3) {
					Showmsg('mode_o_not_friend');
				}
			} else {
				$username = $windid;
				$where = '';
			}
			$favor = $db->get_one("SELECT tids,type FROM pw_favors WHERE uid=".pwEscape($u));
			
			Add_S($favor);
			if ($favor['type']) {
				$ftypeid = explode(',',$favor['type']);
			}
			
			$tids = $ptids = array();
			list($tiddb,$favor_num) = getfavor($favor['tids']);
			if ($tiddb) {
				if ($ftype == 'all') {
					foreach ($tiddb as $key => $val) {
						if ($val) {
							$tids += $val;
						}
					}
				} elseif ($ftype == 'def') {
					$tids = $tiddb['0'];
				} elseif ($tiddb[$ftype]) {
					$tids = $tiddb[$ftype];
				}
				
				$db_perpage = 20;
				$page<1 && $page = 1;
				$start_limit = intval(($page-1) * $db_perpage);
				$count = count($tids);
				$numofpage = ceil($count/$db_perpage);
				$numofpage < 1 && $numofpage = 1;
				$page > $numofpage && $page = $numofpage;
				$pages = numofpage($count,$page,$numofpage,$basename."a=$a&see=$see&ftype=$ftype&");
			}
			if ($tids) {
				$tids = pwImplode($tids);
				$where .= ' ORDER BY postdate DESC';
				$query = $db->query("SELECT fid,tid,subject,postdate,author,authorid,anonymous,replies,hits,titlefont FROM pw_threads WHERE tid IN($tids) $where");
				while ($rt = $db->fetch_array($query)) {
					$rt['subject']	= substrs($rt['subject'],45);
					list($rt['postdate'],$rt['posttime']) = getLastDate($rt['postdate']);
					$keyvalue		= get_key($rt['tid'],$tiddb);
					if ($rt['anonymous'] && !in_array($groupid,array('3','4')) && $rt['authorid'] != $winduid) {
						$rt['author']	= $db_anonymousname;
						$rt['authorid'] = 0;
					}
					$ftype == 'all' && $tidarray[$keyvalue][$rt['tid']] = $rt['tid'];
					$rt['forum']	= $forum[$rt['fid']]['name'];
					$rt['sel']		= $ftypeid[$keyvalue-1];
					$article[]		= $rt;
				}
				$article = array_slice($article,$start_limit,$db_perpage);
			}
			//$thisbase .= "a=$a&";
			$favor_other_num = $db->get_value("SELECT count(*) as sum FROM pw_share s WHERE s.uid=".pwEscape($winduid)." AND s.ifhidden=1");
			$sum = (int)$favor_num + (int)$favor_other_num;
			
		} elseif ($job == 'addtype') {
			PostCheck();
			
			(!$ftype || strlen($ftype)>20) && Showmsg('favor_cate_error');
			strpos($ftype,',') !== false && Showmsg('favor_cate_limit');
			$favor   = $db->get_one("SELECT type FROM pw_favors WHERE uid=".pwEscape($winduid));
			$newtype = $favor['type'];
			$newtype.= $newtype ? ",".stripslashes($ftype) : stripslashes($ftype);
			$newtype = addslashes(Char_cv($newtype));
			if ($favor) {
				$db->update("UPDATE pw_favors SET type=".pwEscape($newtype)."WHERE uid=".pwEscape($winduid));
			} else{
				$db->update("INSERT INTO pw_favors SET".pwSqlSingle(array('uid'=>$winduid,'type'=>$newtype)));
			}
			refreshto("$basename".'a=my&see=postfavor','operate_success');
		} elseif ($job == 'clear') {

			PostCheck();
			
			InitGP(array('selid'),'P');
			!$selid && Showmsg('sel_error');
			$rs = $db->get_one("SELECT tids FROM pw_favors WHERE uid=".pwEscape($winduid));
			if ($rs) {
				list($tiddb) = getfavor($rs['tids']);

				$db->update("UPDATE pw_threads SET favors=favors-1 WHERE tid IN (".pwImplode($selid).")");
				$db->update("UPDATE pw_elements SET value=value-1 WHERE type='hotfavor' AND id IN (".pwImplode($selid).")");
				$db->update("UPDATE pw_elements SET value=value-1 WHERE type='newfavor' AND id IN (".pwImplode($selid).")");

				foreach ($selid as $key => $tid) {
					foreach ($tiddb as $k => $v) {
						if (in_array($tid,$v)) {
							unset($tiddb[$k][$tid]);
						}
					}
				}
				foreach ($tiddb as $key => $val) {
					if (empty($val)) {
						unset($tiddb[$key]);
					}
				}
				$newtids = makefavor($tiddb);
				$db->update("UPDATE pw_favors SET tids=".pwEscape($newtids)."WHERE uid=".pwEscape($winduid));
				refreshto("$basename".'a=my&see=postfavor','operate_success');
			} else {
				Showmsg('job_favor_del');
			}
		} elseif ($job == 'change') {

			PostCheck();
			
			InitGP(array('selid'),'P');
			!$selid && Showmsg('sel_error');
			$rs = $db->get_one("SELECT tids FROM pw_favors WHERE uid=".pwEscape($winduid));
			if ($rs) {
				list($tiddb) = getfavor($rs['tids']);
				foreach ($selid as $key => $tid) {
					if (!is_numeric($tid)) continue;
					foreach ($tiddb as $k => $v) {
						if (in_array($tid,$v)) {
							unset($tiddb[$k][$tid]);
						}
					}
					$tiddb[$ftype][$tid] = $tid;
				}
				foreach ($tiddb as $key => $val) {
					if (empty($val)) {
						unset($tiddb[$key]);
					}
				}
				$newtids = makefavor($tiddb);
				$db->update("UPDATE pw_favors SET tids=".pwEscape($newtids)."WHERE uid=".pwEscape($winduid));
			}
			refreshto("$basename".'a=my&see=postfavor','operate_success');

		}  elseif ($job == 'deltype') {

			PostCheck();
			
			(int)$ftype<1 && Showmsg('type_error');
			$tnum  = $ftype-1;
			$rs    = $db->get_one("SELECT tids,type FROM pw_favors WHERE uid=".pwEscape($winduid));
			list($tiddb) = getfavor($rs['tids']);
			$ftypedb= explode(',',$rs['type']);
			Add_S($ftypedb);
			unset($ftypedb[$tnum]);
			if ($tiddb[$ftype]) {
				foreach ($tiddb[$ftype] as $key => $val) {
					$tiddb['0'][$val] = $val;
				}
			}
			unset($tiddb[$ftype]);
			$newtids = makefavor($tiddb);
			$newtype = Char_cv(implode(',',$ftypedb));
			$db->update("UPDATE pw_favors SET ".pwSqlSingle(array('tids'=>$newtids,'type'=>$newtype))."WHERE uid=".pwEscape($winduid));
			refreshto("$basename".'a=my&see=postfavor','operate_success');
		}
		
//		require_once(M_P.'require/header.php');
		if ($space == 1 && defined('F_M')) {

			$isGM = CkInArray($windid,$manager);
			!$isGM && $groupid==3 && $isGM=1;
			
			require_once(R_P.'require/credit.php');
			list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);
			require_once PrintEot('header');
			require_once PrintEot('user_share');
			footer();
		} else {
			//require_once PrintEot('m_share');
			list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_share",true);
		}
//		footer();
	}

} elseif($a == 'friend' && !getFriends($winduid,0,0,false,1)) {
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_share",true);
	$username = $windid;
} else {

	if ($a == 'friend') {
		$friends = getFriends($winduid,0,0,false,1);
		//$thisbase .= "a=$a&";
		$uids = array_keys($friends);
		$where .= 's.uid IN('.pwImplode($uids).') AND s.ifhidden=0';
		$username = $windid;
		
	} elseif ($a == 'my') {
		InitGP(array('see'),null,1);

		$isU = false;
		$u == $winduid && $isU = true;
		!$isU && Showmsg('space_over_right');

		$username = $windid;
		$where .= 's.uid='.pwEscape($winduid).' AND s.ifhidden=1';
	} elseif ($a == 'recommend') {
		define('AJAX',1);
		define('F_M',true);
		if (empty($_POST['step'])) {
			InitGP(array('id'), null, 2);

			$friend = getFriends($winduid);
			if ($friend) {
				foreach ($friend as $key => $value) {
					$frienddb[$value['ftid']][] = $value;
				}
			}
			$query = $db->query("SELECT * FROM pw_friendtype WHERE uid=".pwEscape($winduid)." ORDER BY ftid");
			$friendtype = array();
			while ($rt = $db->fetch_array($query)) {
				$friendtype[$rt['ftid']] = $rt;
			}
			$no_group_name = getLangInfo('other','no_group_name');
			$friendtype[0] = array('ftid' => 0,'uid' => $winduid,'name' => $no_group_name);

			$a = 'recommend';
			$rt = $db->get_one("SELECT id,type,content,username FROM pw_share WHERE id=" . pwEscape($id));

			if (empty($rt)) {
				Showmsg('data_error');
			}

			$temp = unserialize($rt['content']);

			$rt['link']	= $temp['link'];
			if ($rt['type']=='user') {
				$title = $temp['user']['username']."($rt[link])";
			} elseif ($rt['type']=='photo') {
				$belong	= getLangInfo('app','photo_belong');
				$title= $belong.$temp['photo']['username']."($rt[link])";
			} elseif ($rt['type']=='album') {
				$belong	= getLangInfo('app','photo_belong');
				$title = $belong.$temp['album']['username']."($rt[link])";
			} elseif ($rt['type']=='group') {
				$title = $temp['group']['name']."($rt[link])";
			} elseif ($rt['type']=='diary') {
				$title = $temp['diary']['subject']."($rt[link])";
			} elseif ($rt['type']=='topic') {
				$title = $temp['topic']['subject']."($rt[link])";
			} else {
				$title = $rt['link'];
			}
			$descrip = $temp['descrip'];
			
			$atc_name = getLangInfo('app',$rt['type']);
			require_once PrintEot('m_ajax');
			ajax_footer();

		}
	} else {
		if ($u!=$winduid) {
			$friend = getOneFriend($u);
			if ($friend || $groupid == 3) {
				$where .= 's.uid='.pwEscape($u).' AND s.ifhidden=0';
				$faceimg = $friend['face'];
				$username = $friend['username'];
			} else {
				Showmsg('mode_o_not_friend');
			}
			//$thisbase .= "u=$u&";
		} else {
			$where .= 's.uid='.pwEscape($winduid).' AND s.ifhidden=0';
			$username = $windid;
		}
	}
	$sum = $db->get_value("SELECT count(*) as sum FROM pw_share s WHERE $where");

	if ($a == 'my') {
		$favor = $db->get_one("SELECT tids,type FROM pw_favors WHERE uid=".pwEscape($u));
		list(,$favor_num) = getfavor($favor['tids']);
		$sum += (int)$favor_num;
	}

	if ($type && in_array($type,array('web','user','photo','album','group','video','music','flash','diary','topic'))) {
		$where .= ($where=='' ? 's.type='.pwEscape($type) : ' AND s.type='.pwEscape($type));
	}

	$shares = array();

	$count = $db->get_value("SELECT count(*) as count FROM pw_share s WHERE $where");

	if ($count) {
		$page = (int)GetGP('page');
		$db_perpage = 10;
		list($pages,$limit) = pwLimitPages($count,$page,"{$basename}a=$a&type=$type&");
		
		if (!$db_dopen) {
			$where .= " AND s.type!='diary'";
		}
		if (!$db_phopen) {
			$where .= " AND s.type!='photo'";
		}
		if (!$db_groups_open) {
			$where .= " AND s.type!='group'";
		}


		$rs = $db->query("SELECT s.*,m.groupid,m.icon FROM pw_share s LEFT JOIN pw_members m ON s.uid=m.uid WHERE $where ORDER BY s.id DESC $limit");
		while ($rt = $db->fetch_array($rs)) {
			list($rt['postdate'],$rt['posttime']) = getLastDate($rt['postdate']);
			$temp = unserialize($rt['content']);
			if ($temp['video']) {
				$rt['host']	= $temp['video']['host'];
				$rt['hash'] = $temp['video']['hash'];
			}
			$rt['link']	= $temp['link'];
			if (strpos($rt['link'],'{#APPS_BASEURL#}') !== false) {
				$baseurl = $m == 'o' ? 'mode.php?m=o&' : 'apps.php?';
				$rt['link'] = str_replace('{#APPS_BASEURL#}',$baseurl,$rt['link']);
			}
			if ($rt['type']=='user') {
				$rt['image']	= $temp['user']['image'];
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['user']['username']."</a>";
			} elseif ($rt['type']=='photo') {
				$belong	= getLangInfo('app','photo_belong');
				$rt['image'] = $temp['photo']['image'];
				$temp_uid	= $temp['photo']['uid'];
				$rt['title']= $belong."<a href=\"u.php?uid=$temp_uid\" target=\"_blank\">".$temp['photo']['username']."</a>";
			} elseif ($rt['type']=='album') {
				$belong	= getLangInfo('app','photo_belong');
				$rt['image']	= $temp['album']['image'];
				$temp_uid	= $temp['album']['uid'];
				$rt['title']= $belong."<a href=\"u.php?uid=$temp_uid\" target=\"_blank\">".$temp['album']['username']."</a>";
			} elseif ($rt['type']=='group') {
				$rt['image']	= $temp['group']['image'];
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['group']['name']."</a>";
			} elseif ($rt['type']=='diary') {
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['diary']['subject']."</a>";
			} elseif ($rt['type']=='topic'){
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['topic']['subject']."</a>";
				$rt['abstract']=  stripWindCode($temp['topic']['abstract']);
				$rt['imgs']= unserialize($temp['topic']['imgs']);
			} else {
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".substrs($rt['link'],40)."</a>";
			}
			$rt['descrip']	= $temp['descrip'];
			if ($rt['ifhidden']) {
				$rt['type_name']	= getLangInfo('app',$rt['type'].'_self');
			} else {
				$rt['type_name']	= getLangInfo('app',$rt['type']);
			}
			list($rt['icon']) = showfacedesign($rt['icon'],1);

			unset($rt['content']);
			$shares[] = $rt;
		}
	}
	//require_once(M_P.'require/header.php');
	if ($space == 1 && defined('F_M')) {

		$isGM = CkInArray($windid,$manager);
		!$isGM && $groupid==3 && $isGM=1;

		require_once(R_P.'require/credit.php');
		list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);
		//require_once(M_P.'require/header.php');
		require_once PrintEot('header');
		require_once PrintEot('user_share');
		footer();
	} else {
		
		//require_once PrintEot('m_share');
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_share",true);
	}
}
//footer();



function getfavor($tids) {
	$tids  = explode('|',$tids);
	$tiddb = array();
	$count = 0;
	foreach ($tids as $key => $t) {
		if ($t) {
			$v = explode(',',$t);
			foreach ($v as $k => $v1) {
				$count++;
				$tiddb[$key][$v1] = $v1;
			}
		}
	}
	return array($tiddb,$count);
}
function get_key($tid,$tiddb) {
	foreach ($tiddb as $key => $value) {
		if (in_array($tid,$value)) {
			return $key;
		}
	}
	return null;
}
function getFidoff($gid) {
	global $db;
	$fidoff = array(0);
	$query = $db->query("SELECT fid FROM pw_forums WHERE type<>'category' AND (password!='' OR forumsell!='' OR allowvisit!='' AND allowvisit NOT LIKE '%,$gid,%')");
	while ($rt = $db->fetch_array($query)) {
		$fidoff[] = $rt['fid'];
	}
	return $fidoff;
}

function makefavor($tiddb) {
	$newtids = $ex = '';
	$k = 0;
	ksort($tiddb);
	foreach ($tiddb as $key => $val) {
		$new_tids = '';
		rsort($val);
		if ($key != $k) {
			$s = $key - $k;
			for ($i = 0; $i < $s; $i++) {
				$newtids .= '|';
			}
		}
		foreach ($val as $k => $v) {
			is_numeric($v) && $new_tids .= $new_tids ? ','.$v : $v;
		}
		$newtids .= $ex.$new_tids;
		$k  = $key + 1;
		$ex = '|';
	}
	return $newtids;
}
?>