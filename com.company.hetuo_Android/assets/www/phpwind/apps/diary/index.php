<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');
require_once(D_P."data/bbscache/forum_cache.php");
require_once(R_P.'require/showimg.php');

$isGM = CkInArray($windid,$manager);
!$isGM && $groupid==3 && $isGM=1;

if ($db_question && ($o_share_qcheck || $o_diary_qcheck)) {
	$qkey = array_rand($db_question);
}

InitGP(array('a','u','space','s'));
$page = (int)GetGP('page');

!$db_dopen && Showmsg('dairy_close');

$db_perpage = 10;
$page < 1 && $page = 1;

$u = (int)GetGP('u');
$u = $u ? $u : $winduid;

if ($u != $winduid) {
	$userdb = $db->get_one("SELECT m.username,m.icon,o.index_privacy,o.diary_privacy FROM pw_members m LEFT JOIN pw_ouserdata o ON m.uid=o.uid WHERE m.uid=".pwEscape($u));
	//$thisbase = $basename.'q=diary&u='.$u.'&';
	$username = $userdb['username'];
} else {
	$username = $windid;
	//$thisbase = $basename.'q=diary&';
}

if ($space == 1 && defined('F_M')) {
	if (!$userdb) {
		$userdb = $db->get_one("SELECT m.username,m.icon,o.index_privacy,o.diary_privacy FROM pw_members m LEFT JOIN pw_ouserdata o ON m.uid=o.uid WHERE m.uid=".pwEscape($u));
	}
	if(!in_array($a,array('copydiary','next','pre','privacy'))) $a = '';
	$spaceurl = $baseUrl;
}
if ($u != $winduid || ($space == 1 && defined('F_M')))	{
	if (empty($userdb)) {
		$errorname = '';
		Showmsg('user_not_exists');
	}

	list($isU,$privacy) = pwUserPrivacy($u,$userdb);
	if ($groupid == 3 || $isU == 2 || $isU !=2 && $privacy['index']) {
		$SpaceShow = 1;
	}
	if (!$privacy['diary'] && !$isGM || !$SpaceShow) {
		Showmsg('mode_o_diary_right');
	}
}


$db_uploadfiletype = $o_uploadsize = !empty($o_uploadsize) ? unserialize($o_uploadsize) : array();

$uploadfiletype = $uploadfilesize = ' ';
foreach ($o_uploadsize as $key => $value) {
	$uploadfiletype .= $key.' ';
	$uploadfilesize .= $key.':'.$value.'KB; ';
}

if (empty($a) || $a == 'privacy') {

	if ($u != $winduid && !$space) {
		if (!getOneFriend($u) && $groupid != 3) {
			Showmsg('mode_o_not_friend');
		}
	}

	$did = (int)GetGP('did');
	require_once(R_P.'require/bbscode.php');
	$wordsfb = L::loadClass('FilterUtil');

	$nums = $count = $dnum = $fnum = 0;
	$sqladd = "WHERE d.uid=".pwEscape($u);

	$diarydb = $diarytype = $dtids = $ftids = array();
	$query = $db->query("SELECT d.* FROM pw_diarytype d $sqladd ORDER BY dtid");

	while ($rt = $db->fetch_array($query)) {
		$nums += $rt['num'];
		$diarytype[$rt['dtid']] = $rt;
	}

	$u != $winduid && !getOneFriend($u) && $nofriend = 1;
	$query = $db->query("SELECT d.dtid,d.privacy FROM pw_diary d $sqladd");
	while ($rt = $db->fetch_array($query)) {
		if ($rt['privacy'] == '2') {
			$dtids[$rt['dtid']] ++;
			$dnum ++;
		}
		if ($nofriend && $rt['privacy'] == '1') {
			$ftids[$rt['dtid']] ++;
			$fnum ++;
		}
		$count ++;
	}

	if ($dtids && $u != $winduid) {
		foreach ($dtids as $key => $val) {
			if ($key) {
				$diarytype[$key]['num'] -= $val;
			} else {
				$nums ++;
			}
		}
	}
	if ($ftids && $u != $winduid) {
		foreach ($ftids as $key => $val) {
			if ($key) {
				$diarytype[$key]['num'] -= $val;
			} else {
				$nums ++;
			}
		}
	}
	if ($u != $winduid && $nofriend) {
		$count = $count - $dnum - $fnum;
		$other = $count + $dnum + $fnum - $nums;
	} elseif ($u != $winduid && !$nofriend) {
		$count = $count - $dnum;
		$other = $count + $dnum - $nums;
	} else {
		$other = $count - $nums;
	}

	if (!$did) {
		$dtid = (int)GetGP('dtid');

		$dtype = $dtid;
		$dtid == '-1' && $dtype = 'null';
		if ($u != $winduid) {
			$sqladd .= " AND d.privacy!=2";
			!getOneFriend($u) && $sqladd .= ' AND d.privacy=0';
		} else {
			$a == 'privacy' && $sqladd .= ' AND d.privacy=2';
		}
		if ($dtid == '-1') {
			$sqladd .= ' AND d.dtid=0';
		} elseif (is_numeric($dtid) && $dtid > 0) {
			$sqladd .= ' AND d.dtid='.pwEscape($dtid);
		}

		$sum = $db->get_value("SELECT COUNT(*) as count FROM pw_diary d $sqladd");

		if ($sum) {

			list($pages,$limit) = pwLimitPages($sum,$page,"{$basename}dtid=$dtid&a=$a&");

			$query = $db->query("SELECT d.did,d.dtid,d.uid,d.aid,d.username,d.privacy,d.subject,d.ifconvert,d.ifwordsfb,d.content,d.r_num,d.c_num,d.postdate,m.groupid FROM pw_diary d LEFT JOIN pw_members m ON d.uid=m.uid $sqladd ORDER BY d.postdate DESC $limit");
			while ($rt = $db->fetch_array($query)) {
				if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
					$rt['subject'] = '';
					$rt['content'] = appShield('ban_diary');
				}
				$rt['postdate'] = get_date($rt['postdate'],'Y-m-d H:i');

				if (!$wordsfb->equal($rt['ifwordsfb'])) {
					$rt['content'] = $wordsfb->convert($rt['content'], array(
						'id'	=> $rt['did'],
						'type'	=> 'diary',
						'code'	=> $rt['ifwordsfb']
					));
				}
				$rt['content'] = preg_replace("/\[s:(.+?)\]/eis",'',$rt['content']);
				if ($rt['ifconvert'] == 2) {
					$rt['content'] = convert($rt['content'], $db_windpost);
				} else {
					//strpos($rt['content'],'[s:') !== false && $rt['content'] = showface($rt['content']);
				}
				if ($o_shownum) {
					$rt['content'] = substrs($rt['content'],$o_shownum);
				}
				$rt['content'] = str_replace("\n","<br />",$rt['content']);

				$attachs = $aids = array();
				if ($rt['aid']) {
					$attachs = unserialize($rt['aid']);
					if (is_array($attachs)) {
						$aids = attachment($rt['content']);
					}
				}

				if ($attachs && is_array($attachs)) {
					if ($winduid == $rt['authorid'] || $groupid == 3) {
						$dfadmin = 1;
					} else {
						$dfadmin = 0;
					}
					foreach ($attachs as $at) {
						$atype = '';
						$rat = array();
						if ($at['type'] == 'img') {
							$a_url = geturl('diary/'.$at['attachurl'],'show');
							if (is_array($a_url)) {
								$atype = 'pic';
								$dfurl = '<br>'.cvpic($a_url[0], 1, 450, 338, $at['ifthumb']);
								$rat = array('aid' => $at['aid'], 'img' => $dfurl, 'dfadmin' => $dfadmin, 'desc' => $at['desc']);
							}
						}
						if (!$atype) continue;
						if (in_array($at['aid'], $aids)) {
							$rt['content'] = attcontent($rt['content'], $atype, $rat);
						} else {
							$rt[$atype][$at['aid']] = $rat;
						}
					}
				}
				$diarydb[] = $rt;
			}
		}
	} elseif (is_numeric($did)) {

		if ($u != $winduid && $groupid != 3) {
			$sqladd .= " AND d.privacy!=2 AND d.did=".pwEscape($did);
		} else {
			$sqladd .= " AND d.did=".pwEscape($did);
		}

		$diary = $db->get_one("SELECT d.did,d.dtid,d.uid,d.aid,d.username,d.privacy,d.subject,d.content,d.ifconvert,d.ifwordsfb,d.r_num,d.c_num,d.postdate,d.ifcopy,d.copyurl,m.groupid FROM pw_diary d LEFT JOIN pw_members m ON d.uid=m.uid $sqladd");
		!$diary && Showmsg('mode_o_no_diary');

		if ($u != $winduid && $groupid != 3 && !getOneFriend($u) && $diary['privacy'] == 1) {
			Showmsg('diary_friend_right');
		}
		$diary['r_num'] += 1;
		if ($diary['groupid'] == 6 && $db_shield && $groupid != 3) {
			$diary['subject'] = '';
			$diary['content'] = appShield('ban_diary');
		}
		$attachs = $aids = array();
		if ($diary['aid']) {
			$attachs = unserialize($diary['aid']);
			if (is_array($attachs)) {
				$aids = attachment($diary['content']);
			}
		}
		$tpc_author = $windid;
		$diary['content'] = str_replace("\n","<br />",$diary['content']);

		if ($attachs && is_array($attachs)) {
			if ($winduid == $diary['authorid'] || $groupid == 3) {
				$dfadmin = 1;
			} else {
				$dfadmin = 0;
			}
			foreach ($attachs as $at) {
				$atype = '';
				$rat = array();
				if ($at['type'] == 'img') {
					$a_url = geturl('diary/'.$at['attachurl'],'show');

					if (is_array($a_url)) {
						$atype = 'pic';
						$dfurl = '<br>'.cvpic($a_url[0], 1, 450, 338, $at['ifthumb']);
						$rat = array('aid' => $at['aid'], 'img' => $dfurl, 'dfadmin' => $dfadmin, 'desc' => $at['desc']);
					}
				}
				if (!$atype) continue;
				if (in_array($at['aid'], $aids)) {
					$diary['content'] = attcontent($diary['content'], $atype, $rat);
				} else {
					$diary[$atype][$at['aid']] = $rat;
				}
			}
		}

		$diary['link'] = "$db_bbsurl/{$basename}q=diary&u=$diary[uid]&did=$diary[did]";
		$diary['title'] = "($diary[link])";

		if (!$wordsfb->equal($diary['ifwordsfb'])) {
			$diary['content'] = $wordsfb->convert($diary['content'], array(
				'id'	=> $diary['did'],
				'type'	=> 'diary',
				'code'	=> $diary['ifwordsfb']
			));
		}
		if ($diary['ifconvert'] == 2) {
			$diary['content'] = convert($diary['content'], $db_windpost);
		} else {
			strpos($diary['content'],'[s:') !== false && $diary['content'] = showface($diary['content']);
		}
		list($diary['copyuid'],$diary['copyer'],$diary['url']) = explode("|",$diary['copyurl']);

		$diary['postdate'] = get_date($diary['postdate'],'Y-m-d H:i');

		$db->update("UPDATE pw_diary SET r_num=r_num+1 WHERE uid=".pwEscape($u)." AND did=".pwEscape($did));

		$url = 'mode.php?m=o&q=diary&u='.$u.'&did='.$did.'&';
		list($commentdb,$subcommentdb,$pages) = getCommentDbByTypeid('diary',$did,$page,$url);
		$comment_type = 'diary';
		$comment_typeid = $did;
	}

} elseif ($a == 'friend') {

	$diarytype = array();
	$query = $db->query("SELECT d.* FROM pw_diarytype d $sqladd ORDER BY dtid");
	while ($rt = $db->fetch_array($query)) {
		$diarytype[$rt['dtid']] = $rt;
	}

	if ($friends = getFriends($winduid)) {
		$sqladd = 'WHERE d.privacy!=2';
		$uids = array_keys($friends);
		$uids = array_diff($uids,array($winduid));
		$uids && $sqladd .= ' AND d.uid IN('.pwImplode($uids).')';
	}
	if ($sqladd) {

		require_once(R_P.'require/bbscode.php');
		$sum = $db->get_value("SELECT COUNT(*) as count FROM pw_diary d $sqladd");

		if ($sum) {

			$wordsfb = L::loadClass('FilterUtil');
			list($pages,$limit) = pwLimitPages($sum,$page,$basename."q=diary&a=$a&");

			$query = $db->query("SELECT d.did,d.dtid,d.uid,d.aid,d.username,d.privacy,d.subject,d.ifconvert,d.ifwordsfb,d.content,d.r_num,d.c_num,d.postdate,m.groupid,m.icon FROM pw_diary d LEFT JOIN pw_members m ON d.uid=m.uid $sqladd ORDER BY d.postdate DESC $limit");

			while ($rt = $db->fetch_array($query)) {
				if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
					$rt['subject'] = '';
					$rt['content'] = appShield('ban_diary');
				} elseif (!$wordsfb->equal($rt['ifwordsfb'])) {
					$rt['content'] = $wordsfb->convert($rt['content'], array(
						'id'	=> $rt['did'],
						'type'	=> 'diary',
						'code'	=> $rt['ifwordsfb']
					));
				}
				$rt['postdate'] = get_date($rt['postdate'],'Y-m-d H:i');
				$rt['content'] = preg_replace("/\[s:(.+?)\]/eis",'',$rt['content']);
				if ($rt['ifconvert'] == 2) {
					$rt['content'] = convert($rt['content'], $db_windpost);
				} else {
					strpos($rt['content'],'[s:') !== false && $rt['content'] = showface($rt['content']);
				}
				if ($o_shownum) {
					$rt['content'] = substrs($rt['content'],$o_shownum);
				}
				$rt['content'] = str_replace("\n","<br />",$rt['content']);

				$attachs = $aids = array();
				if ($rt['aid']) {
					$attachs = unserialize($rt['aid']);
					if (is_array($attachs)) {
						$aids = attachment($rt['content']);
					}
				}

				if ($attachs && is_array($attachs)) {
					if ($winduid == $rt['authorid'] || $groupid == 3) {
						$dfadmin = 1;
					} else {
						$dfadmin = 0;
					}
					foreach ($attachs as $at) {
						$atype = '';
						$rat = array();
						if ($at['type'] == 'img') {
							$a_url = geturl('diary/'.$at['attachurl'],'show');
							if (is_array($a_url)) {
								$atype = 'pic';
								$dfurl = '<br>'.cvpic($a_url[0], 1, 450, 338, $at['ifthumb']);
								$rat = array('aid' => $at['aid'], 'img' => $dfurl, 'dfadmin' => $dfadmin, 'desc' => $at['desc']);
							}
						}
						if (!$atype) continue;
						if (in_array($at['aid'], $aids)) {
							$rt['content'] = attcontent($rt['content'], $atype, $rat);
						} else {

							$rt[$atype][$at['aid']] = $rat;
						}
					}
				}

				$rt['link'] = "$db_bbsurl/{$basename}q=diary&u=$rt[uid]&did=$rt[did]";
				$rt['title'] = "($rt[link])";

				if ($rt['uid']!=$winduid) {
					list($rt['icon']) = showfacedesign($rt['icon'],1);
				}

				$diarydb[] = $rt;
			}
		}
	}

} elseif ($a == 'write') {
	//权限设置
	/**
	* 禁止受限制用户发言
	*/
	banUser();
	/*
	* 新注册会员发日志时间限制
	*/
	if ($db_postallowtime && $timestamp - $winddb['regdate'] < $db_postallowtime*60) {
		Showmsg('post_newd_limit');
	}

	/*
	* 用户组发日志权限限制
	*/
	if ($groupid != 3 && $o_diary_groups && strpos($o_diary_groups,",$groupid,") === false) {
		Showmsg('diary_group_right');
	}

	/*
	* 灌水机制
	*/
	$endtime = $tdtime + 24*3600;
	$postdate = $db->get_value("SELECT postdate FROM pw_diary WHERE uid=".pwEscape($winduid)." ORDER BY postdate DESC LIMIT 1");
	$todaycount = $db->get_value("SELECT COUNT(*) as count FROM pw_diary WHERE uid=".pwEscape($winduid)." AND postdate>=".pwEscape($tdtime)." AND postdate<".pwEscape($endtime));

	$tdtime  >= $postdate && $todaycount = 0;

	if ($groupid != 3 && $o_diarylimit && $todaycount >= $o_diarylimit) {
		Showmsg('diary_gp_limit');
	}

	if ($groupid != 3 && $o_diarypertime && $timestamp >= $postdate && $timestamp - $postdate <= $o_diarypertime) {
		Showmsg('diary_limit');
	}
	//权限设置

	if (!$_POST['step']) {

		$editor = getstatus($winddb['userstatus'],11) ? 'wysiwyg' : 'textmode';
		$dtsel = '';
		$query = $db->query("SELECT * FROM pw_diarytype WHERE uid=".pwEscape($winduid)." ORDER BY dtid");
		while ($rt = $db->fetch_array($query)) {
			$dtsel .= "<option value=\"$rt[dtid]\">$rt[name]</option>";
		}
		$checked = 'checked';
		$disabled = '';

	} elseif ($_POST['step'] == 2) {
		initGP(array("privacy"));
		require_once(R_P.'require/postfunc.php');
		PostCheck(1,$o_diary_gdcheck,$o_diary_qcheck);
		InitGP(array('dtid','privacy','ifcopy'),'P');
		require_once(R_P.'require/bbscode.php');

		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($_POST['atc_title'])) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($_POST['atc_content'], false)) !== false) {
			Showmsg('content_wordsfb');
		}

		list($atc_title,$atc_content,$ifconvert,$ifwordsfb) = check_data('new');
		//$db_tcheck && $winddb['postcheck'] == tcheck($atc_content) && Showmsg('diary_content_same'); //内容验证

		$dtid = (int)$dtid;
		$privacy = (int)$privacy;
		$ifcopy = (int)$ifcopy;
		!$privacy && $ifcopy = 1;

//		require_once(M_P.'require/upload.php');
		require_once(R_P.'require/app_upload.php');

		$pwSQL = pwSqlSingle(array(
			'uid'		=> $winduid,
			'dtid'		=> $dtid,
			'aid'		=> (!empty($attachs) ? addslashes(serialize($attachs)) : ''),
			'username'	=> $windid,
			'privacy'	=> $privacy,
			'subject'	=> $atc_title,
			'content'	=> $atc_content,
			'ifcopy'	=> $ifcopy,
			'ifconvert'	=> $ifconvert,
			'ifupload'	=> $ifupload,
			'ifwordsfb'	=> $ifwordsfb,
			'postdate'	=> $timestamp,
		));
		$db->update("INSERT INTO pw_diary SET $pwSQL");
		$did = $db->insert_id();
		$db->update("UPDATE pw_diarytype SET num=num+1 WHERE uid=".pwEscape($winduid)." AND dtid=".pwEscape($dtid));//更新分类日志数

		if ($aids) {
			$db->update("UPDATE pw_attachs SET did=".pwEscape($did)." WHERE aid IN($aids)");
		}

		countPosts('+1');
		if (!$privacy) {
			//会员资讯缓存
			$usercachedata = array();
			$usercache = L::loadDB('Usercache');
			$usercachedata['content'] = substrs(stripWindCode($atc_content),100,N);
			$usercachedata['subject'] = $atc_title;
			$usercachedata['postdate'] = $timestamp;
			if ($attachs) {
				foreach ($attachs as $value) {
					if ($value['type'] == 'img') {
						$usercachedata['attimages'][]= array('attachurl' => $value['attachurl'], 'ifthumb' => $value['ifthumb']);
					}
				}
			}
			$usercache->update($winduid,'diary',$did,$usercachedata);
		}
		//积分变动
		require_once(R_P.'require/credit.php');
		$o_diary_creditset = unserialize($o_diary_creditset);
		$creditset = getCreditset($o_diary_creditset['Post']);
		$creditset = array_diff($creditset,array(0));
		if (!empty($creditset)) {
			$credit->sets($winduid,$creditset,true);
			updateMemberid($winduid);
		}

		if ($creditlog = unserialize($o_diary_creditlog)) {
			addLog($creditlog['Post'],$windid,$winduid,'diary_Post');
		}
		updateUserAppNum($winduid,'diary');
		if ($privacy != 2) {
			pwAddFeed($winduid,'diary',$did,array(
									'lang'=>'diary_data',
									'username'=>$windid,
									'uid'=>$winduid,
									'did'=>$did,
									'subject'=>$atc_title,)
				);
		}
		refreshto("{$basename}q=diary",'operate_success',2,true);
	}
} elseif ($a == 'edit') {

	if (!$_POST['step']) {

		$did = (int)GetGP('did');
		$editor = getstatus($winddb['userstatus'],11) ? 'wysiwyg' : 'textmode';
		$dtsel = '';
		$diary = $db->get_one("SELECT did,dtid,aid,privacy,subject,content,ifcopy FROM pw_diary WHERE uid=".pwEscape($winduid)." AND did=".pwEscape($did));

		!$diary && Showmsg('illegal_request');
		$attach = '';
		if ($diary['aid']) {
			$attachs = unserialize($diary['aid']);
			if (is_array($attachs)) {
				foreach ($attachs as $key => $value) {
					list($value['attachurl'],) = geturl('diary/'.$value['attachurl'],'lf');
					$attach .= "'$key' : ['$value[name]', '$value[size]', '$value[attachurl]', '$value[type]', '$value[special]', '$value[needrvrc]', '$value[ctype]', '$value[desc]'],";
				}
				$attach = rtrim($attach,',');
			}
		}

		$atc_content = $diary['content'];
		${'privacy_'.$diary['privacy']} = 'selected';
		$diary['ifcopy'] && $checked = 'checked';
		($diary['privacy'] == '2') && $disabled = 'disabled';

		$query = $db->query("SELECT * FROM pw_diarytype WHERE uid=".pwEscape($winduid)." ORDER BY dtid");
		while ($rs = $db->fetch_array($query)) {
			$selected = '';
			$rs['dtid'] == $diary['dtid'] && $selected .= 'selected';
			$dtsel .= "<option value=\"$rs[dtid]\" $selected>$rs[name]</option>";
		}
	} elseif ($_POST['step'] == 2) {

		InitGP(array('did','dtid','dtided','privacy','privacyed','ifcopy'),'P');

		require_once(R_P.'require/bbscode.php');
		require_once(R_P.'require/postfunc.php');
		PostCheck(1,$o_diary_gdcheck,$o_diary_qcheck);

		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($_POST['atc_title'])) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($_POST['atc_content'], false)) !== false) {
			Showmsg('content_wordsfb');
		}

		list($atc_title,$atc_content,$ifconvert,$ifwordsfb) = check_data('modify');
		//$db_tcheck && $winddb['postcheck'] == tcheck($atc_content) && Showmsg('diary_content_same'); //内容验证

		$dtid = (int)$dtid;
		$dtided = (int)$dtided;
		$privacy = (int)$privacy;
		$ifcopy = (int)$ifcopy;

		/**
		* 附件修改
		*/
		$oldattach = $replacedb = $unsetattach = array();

		$aid = $db->get_value("SELECT aid FROM pw_diary WHERE uid=".pwEscape($winduid)." AND did=".pwEscape($did));

		if ($aid) {
			InitGP(array('keep','oldatt_special','oldatt_needrvrc'), 'P', 2);
			InitGP(array('oldatt_ctype','oldatt_desc'), 'P');
			$oldattach = unserialize(stripslashes($aid));
			foreach ($oldattach as $key => $value) {
				if (!@in_array($key,$keep)) {
					$unsetattach[$key] = $value;
					unset($oldattach[$key]);
				} else {
					$v = array(
						'special'	=> $oldatt_special[$key],		'ctype'		=> $oldatt_ctype[$key],
						'needrvrc'	=> $oldatt_needrvrc[$key],		'desc'		=> $oldatt_desc[$key]
					);

					$v['needrvrc'] = $v['special'] = 0;
					$v['ctype'] = '';

					$oldattach[$key] = array_merge($oldattach[$key], $v);

					if (array_key_exists('replace_'.$key, $_FILES)) {
						$db_attachnum++;
						$replacedb[$key] = $oldattach[$key];
					} elseif ($value['ctype'] <> $v['ctype'] || $value['desc'] <> $v['desc']) {
						$runsql[] = 'UPDATE pw_attachs SET ' . pwSqlSingle(array(
							'needrvrc'	=> $v['needrvrc'],
							'descrip'	=> $v['desc'],
							'special'	=> $v['special'],
							'ctype'		=> $v['ctype']
						)) . ' WHERE aid=' . pwEscape($key);
					}
				}
			}
		}
		//require_once(M_P.'require/upload.php');
		require_once(R_P.'require/app_upload.php');

		if ($attachs) {
			foreach ($attachs as $key => $value) {
				$oldattach[$key] = $value;
			}
			$upmemdata .= ($upmemdata ? ',' : '')."uploadtime=".pwEscape($winddb['uploadtime']).",uploadnum=".pwEscape($winddb['uploadnum']);
		}
		if ($upmemdata) {
			$db->update("UPDATE pw_memberdata SET $upmemdata WHERE uid=".pwEscape($winduid));
		}
		if ($oldattach) {
			$oldattach = addslashes(serialize($oldattach));
		} else {
			$oldattach = '';
		}
		/**
		* 附件修改
		*/

		$pwSQL = pwSqlSingle(array(
			'dtid'		=> $dtid,
			'aid'		=> $oldattach,
			'privacy'	=> $privacy,
			'subject'	=> $atc_title,
			'content'	=> $atc_content,
			'ifcopy'	=> $ifcopy,
			'ifconvert'	=> $ifconvert,
			'ifupload'	=> $ifupload,
			'ifwordsfb'	=> $ifwordsfb,
		));


		$db->update("UPDATE pw_diary SET $pwSQL WHERE uid=".pwEscape($winduid)." AND did=".pwEscape($did));

		if ($aids) {
			$db->update("UPDATE pw_attachs SET did=".pwEscape($did)." WHERE aid IN($aids)");
		}

		if ($dtided != $dtid) {
			$db->update("UPDATE pw_diarytype SET num=num-1 WHERE uid=".pwEscape($winduid)." AND dtid=".pwEscape($dtided));
			$db->update("UPDATE pw_diarytype SET num=num+1 WHERE uid=".pwEscape($winduid)." AND dtid=".pwEscape($dtid));
		}

		if ($privacyed == 2 && $privacy !=2) {
			pwAddFeed($winduid,'diary',$did,array(
									'lang'=>'diary_data',
									'username'=>$windid,
									'uid'=>$winduid,
									'did'=>$did,
									'subject'=>$atc_title,)
				);
			countPosts('+1');
		} elseif ($privacyed != 2 && $privacy ==2) {
			if ($affected_rows = delAppAction('diary',$did)) {
				countPosts("-$affected_rows");
			}
		}

		refreshto("{$basename}q=diary",'operate_success');
	}
} elseif ($a == 'copydiary') {
	define('AJAX', 1);
	define('F_M',true);
	banUser();
	InitGP(array('did'));

	empty($did) && Showmsg('data_error');

	$dtsel = '';
	$query = $db->query("SELECT * FROM pw_diarytype WHERE uid=".pwEscape($winduid)." ORDER BY dtid");
	while ($rt = $db->fetch_array($query)) {
		$dtsel .= "<option value=\"$rt[dtid]\">$rt[name]</option>";
	}
	require_once PrintEot('m_ajax');ajax_footer();

} elseif ($a == 'next') {

	define('AJAX',1);
	$did = (int)GetGP('did');
	$sqladd = "WHERE uid=".pwEscape($u);
	if ($u != $winduid) {
		$sqladd .= " AND privacy!=2 AND did>".pwEscape($did);
	} else {
		$sqladd .= " AND did>".pwEscape($did);
	}

	$did = $db->get_value("SELECT MIN(did) FROM pw_diary $sqladd");

	echo "success\t$did";
	ajax_footer();

} elseif ($a == 'pre') {

	define('AJAX',1);
	$did = (int)GetGP('did');
	$sqladd = "WHERE uid=".pwEscape($u);
	if ($u != $winduid) {
		$sqladd .= " AND privacy!=2 AND did<".pwEscape($did);
	} else {
		$sqladd .= " AND did<".pwEscape($did);
	}

	$did = $db->get_value("SELECT MAX(did) FROM pw_diary $sqladd");
	echo "success\t$did";
	ajax_footer();

}

//require_once(M_P.'require/header.php');
if ($space == 1 && defined('F_M')) {
	require_once(R_P.'require/credit.php');
	list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);
	require_once PrintEot('header');
	require_once PrintEot('user_diary');
	footer();
} else {
	if($s){
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_diary_bottom",true);
	}else{
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_diary",true);
	}
	//require_once PrintEot($s?'m_diary_bottom':'m_diary');
}
//$s?"":footer();
?>