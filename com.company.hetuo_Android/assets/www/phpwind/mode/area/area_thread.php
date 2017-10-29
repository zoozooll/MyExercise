<?php
!defined('P_W') && exit('Forbidden');

$db_mode = 'area';
define('M_P',R_P."mode/$db_mode/");
$m = $db_mode;
$db_modepages = $db_modepages[$db_mode];

$searchadd = $thread_children = $thread_online = $fastpost = $updatetop = $urladd = '';
wind_forumcheck($foruminfo);
$forumname = strip_tags($foruminfo['name']);
list($guidename,$forumtitle) = getforumtitle(forumindex($foruminfo['fup'],1));
$db_metakeyword = trim(str_replace(array('|',' - ',' ',',, ',', , '),', ',$forumtitle),', ');
$foruminfo['keywords'] && $db_metakeyword = $foruminfo['keywords'].','.$db_metakeyword;
if ($foruminfo['descrip']) {
	$db_metadescrip = Char_cv(strip_tags($foruminfo['descrip']));
	$db_metadescrip = $db_bbsname.','.str_replace(array("\n",'&nbsp;','&amp;','&lt;','&gt;'),'',$db_metadescrip);
}
$toptids = $foruminfo['topthreads'];
$pwSystem = array();
$isGM = $isBM = $admincheck = $ajaxcheck = $managecheck = $pwAnonyHide = $pwPostHide = $pwSellHide = $pwEncodeHide = 0;
if ($groupid != 'guest') {
	$isGM = CkInArray($windid,$manager);
	$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
	$admincheck = ($isGM || $isBM) ? 1 : 0;
	if (!$isGM) {
		$pwSystem = pwRights($isBM);
		if ($pwSystem && ($pwSystem['tpccheck'] || $pwSystem['digestadmin'] || $pwSystem['lockadmin'] || $pwSystem['pushadmin'] || $pwSystem['coloradmin'] || $pwSystem['downadmin'] || $pwSystem['delatc'] || $pwSystem['moveatc'] || $pwSystem['copyatc'] || $pwSystem['topped'] || $pwSystem['unite'] || $pwSystem['tpctype'])) {//system rights
			$managecheck = 1;
		}
		if (($groupid == 3 || $isBM) && $pwSystem['deltpcs']) {
			$ajaxcheck = 1;
		}
		$pwPostHide = $pwSystem['posthide'];
		$pwSellHide = $pwSystem['sellhide'];
		$pwEncodeHide = $pwSystem['encodehide'];
		$pwAnonyHide = $pwSystem['anonyhide'];
	} else {
		$managecheck = $ajaxcheck = $pwAnonyHide = $pwPostHide = $pwSellHide = $pwEncodeHide = 1;
	}
}

if (!$admincheck) {
	!$foruminfo['allowvisit'] && forum_creditcheck();
	$foruminfo['forumsell'] && forum_sell($fid);
}

require_once(M_P.'require/header.php');

InitGP(array('page','type','subtype','search','orderway','asc','special','modelid','pcid','topicsearch'));
$db_maxpage && $page > $db_maxpage && $page = $db_maxpage;
(int)$page<1 && $page = 1;

$admin_T = array();
if ($foruminfo['forumadmin']) {
	$forumadmin = explode(',',$foruminfo['forumadmin']);
	foreach ($forumadmin as $key => $value) {
		if ($value) {
			if (!$db_adminshow) {
				if ($key==10) {$admin_T['admin'].='...'; break;}
				$admin_T['admin'] .= '<a href="u.php?action=show&username='.rawurlencode($value).'">'.$value.'</a> ';
			} else {
				$admin_T['admin'] .= '<option value="'.$value.'">'.$value.'</option>';
			}
		}
	}
	$admin_T['admin'] = '&nbsp;'.$admin_T['admin'];
}
if ($forumset['commend']) {
	//$foruminfo['commend'] = unserialize($foruminfo['commend']);
	if ($forumset['commendtime'] && $timestamp-$forumset['ifcommend']>$forumset['commendtime']) {
		updatecommend($fid,$forumset);
	}
}

/***  版块浏览记录  ***/
$threadlog = str_replace(",$fid,",',',GetCookie('threadlog'));
$threadlog.= ($threadlog ? '' : ',').$fid.',';
substr_count($threadlog,',')>11 && $threadlog = preg_replace("/[\d]+\,/i",'',$threadlog,3);
Cookie('threadlog',$threadlog);

Update_ol();


$db_maxpage && $page > $db_maxpage && $page = $db_maxpage;
(int)$page<1 && $page = 1;

$ifsort = 0;
$NT_A = $NT_C = array();
if ($page==1) {
	$tempnotice = array('NT_A' => $notice_A,'NT_C' => $notice_C[$cateid]);
	foreach ($tempnotice as $key => $value) {
		if (!empty($value)) {
			$ifsort = 1;
			foreach ($value as $v) {
				if (empty(${$key}) && $v['startdate']<=$timestamp && (!$v['enddate'] || $v['enddate']>=$timestamp)) {
					$v['rawauthor'] = rawurlencode($v['author']);
					//$v['startdate'] = get_date($v['startdate']);
					!$v['url'] && $v['url'] = "notice.php?fid=$v[fid]#$v[aid]";
					${$key} = $v;
				}
			}
		}
	}
}
unset($notice_A,$notice_C);

if ($foruminfo['aid']) {
	require_once(R_P.'require/bbscode.php');
	$foruminfo['rawauthor'] = rawurlencode($foruminfo['author']);
	$foruminfo['startdate'] = get_date($foruminfo['startdate']);
	$foruminfo['content'] = convert(str_replace(array("\n","\r\n"),'<br />',$foruminfo['content']),$db_windpost,2);
}
if (strpos($_COOKIE['deploy'],"\tthread\t")===false) {
	$thread_img	 = 'fold';
	$cate_thread = '';
} else {
	$thread_img  = 'open';
	$cate_thread = 'display:none;';
}
if (strpos($_COOKIE['deploy'],"\tchildren\t")===false) {
	$children_img	 = 'fold';
	$cate_children = '';
} else {
	$children_img  = 'open';
	$cate_children = 'display:none;';
}
$forumdb = array();
if ($foruminfo['childid']) {
	require_once(R_P."require/thread_child.php");
}

if ($managecheck) {
	InitGP(array('concle'));
	$concle || $concle = GetCookie('concle');
	if ($concle==1 && ($isGM || $pwSystem['topped'] || $pwSystem['digestadmin'] || $pwSystem['lockadmin'] || $pwSystem['pushadmin'] || $pwSystem['coloradmin'] || $pwSystem['downadmin'])) {
		$concle = 2;$managemode = 1;
		Cookie("concle","1",0);
	} else {
		$concle = 1;$managemode = 0;
		Cookie("concle","",0);
	}
	$trd_adminhide = "<form action=\"mawhole.php\" method=\"post\" name=\"mawhole\"><input type=\"hidden\" name=\"fid\" value=\"$fid\">";
} else {
	$trd_adminhide = '';
}

$colspannum = 6;
/*分类、团购 start*/
/*分类信息*/
if ($foruminfo['modelid'] || $modelid > 0) {
	require_once(R_P.'lib/posttopic.class.php');
	$postTopic = new postTopic($pwpost);
	$modelids = explode(",",$foruminfo['modelid']);
	if ($foruminfo['modelid']) {
		$N_allowtopicopen = true;
	} else {
		$N_allowtopicopen = false;
	}
}

/*团购活动*/
if ($foruminfo['pcid'] || $pcid > 0) {
	require_once(R_P.'lib/postcate.class.php');
	$postCate = new postCate($pwpost);
	$pcids = explode(",",$foruminfo['pcid']);
	if ($foruminfo['pcid']) {
		$N_allowpostcateopen = true;
	} else {
		$N_allowpostcateopen = false;
	}
}

if ($modelid > 0) {/*分类信息*/
	$fielddb = $postTopic->getFieldData($modelid,'one');
	foreach ($fielddb as $key => $value) {
		if($value['threadshow'] == 1) {
			$threadshowfield[$key] = $value;
		}
	}
	$colspannum = count($threadshowfield) + 2;
	$initSearchHtml = $postTopic->initSearchHtml($modelid);
} elseif ($pcid > 0) {/*团购活动*/
	$fielddb = $postCate->getFieldData($pcid,'one');
	foreach ($fielddb as $key => $value) {
		if($value['threadshow'] == 1) {
			$threadshowfield[$key] = $value;
		}
	}
	$colspannum = count($threadshowfield) + 2;
	$initSearchHtml = $postCate->initSearchHtml($pcid);
}
/*分类、团购 end*/


$t_per = $foruminfo['t_type'];
$t_db = (array)$foruminfo['topictype'];
unset($foruminfo['t_type']);/* 0 */
$pwSelectType = $pwSelectSpecial = 'all';
if ($t_db && is_numeric($type) && isset($t_db[$type])) {
	if ($t_db[$type]['upid'] == 0) {
		foreach ($t_db as $key => $value) {
			$value['upid'] == $type && $typeids[] = $key;
		}
		if ($typeids) {
			$typeids = array_merge($typeids,array($type));
			$searchadd = ' AND type IN('.pwImplode($typeids).") AND ifcheck='1'";
		} else {
			$searchadd = ' AND type='.pwEscape($type)." AND ifcheck='1'";
		}
	} else {
		$searchadd = ' AND type='.pwEscape($type)." AND ifcheck='1'";
	}
	$urladd .= "&type=$type";
	$pwSelectType = $type;
} elseif ((int)$special>0) {
	$searchadd = ' AND special='.pwEscape($special)." AND ifcheck='1'";
	$urladd .= "&special=$special";
	$pwSelectSpecial = $special;
} elseif ($search == 'digest') {
	$searchadd = " AND digest>'0' AND ifcheck='1'";
	$urladd .= "&search=$search";
	$pwSelectType = 'digest';
} elseif ($search == 'check') {
	if ($isGM || $pwSystem['viewcheck']) {
		$searchadd = " AND ifcheck='0'";
	} else {
		$searchadd = ' AND authorid='.pwEscape($winduid)." AND ifcheck='0'";
	}
	$urladd .= "&search=$search";
	$pwSelectType = 'check';
} elseif (is_numeric($search)) {
	$searchadd = " AND lastpost>=".pwEscape($timestamp - $search*84600)." AND ifcheck='1'";
	$urladd .= "&search=$search";
} elseif (is_numeric($modelid)) {//选择某个信息分类中的某个模板的条件下
	$searchadd = " AND modelid=".pwEscape($modelid);
	$urladd .= "&modelid=$modelid";
} elseif (is_numeric($pcid)) {
	$special = $pcid + 20;
	$searchadd = " AND special=".pwEscape($special);
	$urladd .= "&pcid=".$pcid;
}
if ($searchadd) {
	$rs = $db->get_one('SELECT COUNT(*) AS count FROM pw_threads WHERE fid='.pwEscape($fid).$searchadd);
	$count = $rs['count'];
} else {
	$searchadd = " AND ifcheck='1'";
	$count = $foruminfo['topic'];
}
if ($winddb['t_num']) {
	$db_perpage = $winddb['t_num'];
} elseif ($forumset['threadnum']) {
	$db_perpage = $forumset['threadnum'];
}
if ($winddb['p_num']) {
	$db_readperpage = $winddb['p_num'];
} elseif ($forumset['readnum']) {
	$db_readperpage = $forumset['readnum'];
}

if ($db_topped && !$pcid && !$modelid) {
	$sql	= 'fid='.pwEscape($fid).' AND topped<1';
	$topadd	= 'topped';
	$count += $foruminfo['top1']+$foruminfo['top2'];
} else {
	$sql	= 'fid='.pwEscape($fid);
	$topadd	= 'topped';
}

$tpcdb = array();

$asc = 'DESC';
$orderway = 'postdate';

$numofpage = ceil($count/$db_perpage);
$numofpage < 1 && $numofpage = 1;
if ($page > $numofpage) {
	$page  = $numofpage;
}

$start_limit = intval(($page-1) * $db_perpage);
$totalpage	= min($numofpage,$db_maxpage);
$pages		= numofpage($count,$page,$numofpage,"thread.php?fid=$fid{$urladd}&",$db_maxpage);

$attachtype	= array('1'=>'img','2'=>'txt','3'=>'zip');

$fcache = 0;
$threaddb = array();

if ($fcache < 2) {

	$R = 0;
	if ($db_topped && !$pcid && !$modelid) {
		$rows = $foruminfo['top1'] + $foruminfo['top2'];
		if ($start_limit < $rows) {
			$L = (int)min($rows-$start_limit,$db_perpage);
			$limit  = pwLimit($start_limit,$L);
			$limit2 = $L == $db_perpage ? '' : pwLimit(0,$db_perpage-$L);
			if ($toptids) {
				$query = $db->query("SELECT * FROM pw_threads WHERE tid IN($toptids) AND topped>0 ORDER BY topped DESC,$orderway DESC $limit");
				while ($rt = $db->fetch_array($query)) {
					$tpcdb[] = $rt;
				}
				$db->free_result($query);
			}
			unset($toptids,$L,$limit,$toppeddb);
		} else {
			list($st,$lt,$asc,$R) = getstart($start_limit-$rows,$asc,$count);
			$limit2 = pwLimit($st,$lt);
		}
		unset($rows);
	} else {
		list($st,$lt,$asc,$R) = getstart($start_limit,$asc,$count);
		$limit2 = pwLimit($st,$lt);
	}
	if ($limit2) {
		if ($topicsearch == 1) {
			InitGP(array('searchname','new_searchname'));
			$searchname && $new_searchname = StrCode(serialize($searchname));

			if ($modelid > 0) {
				list($count,$tiddb,$alltiddb) = $postTopic->getSearchvalue($new_searchname,'one',true);
			} elseif($pcid > 0) {
				list($count,$tiddb,$alltiddb) = $postCate->getSearchvalue($new_searchname,'one',true);
			}

			if ($tiddb){
				$query = $db->query("SELECT * FROM pw_threads WHERE tid IN (".pwImplode($tiddb).")");
				$urladd = "&topicsearch=1&new_searchname=$new_searchname";
				while ($thread = $db->fetch_array($query)) {
					$tpcdb[] = $thread;
				}
				$db->free_result($query);
			}

		} else {
			$topadd && $topadd .= $R ? ' ASC,' : ' DESC,';
			$query = $db->query("SELECT * FROM pw_threads WHERE $sql $searchadd ORDER BY $topadd $orderway $asc $limit2");
			while ($thread = $db->fetch_array($query)) {
				$tpcdb[] = $thread;
			}
			$db->free_result($query);
		}
	}
	$R && $tpcdb = array_reverse($tpcdb);

	//Start Here pwcache
	if (($db_ifpwcache&112) && pwFilemtime(D_P.'data/bbscache/hitsort_judge.php')<$timestamp-600) {
		include_once(D_P.'data/bbscache/hitsort_judge.php');
		$updatelist = $updatetype = array();
		foreach ($tpcdb as $thread) {
			if ($db_ifpwcache & 16) {
				if ($thread['hits']>$hitsort_judge['hitsort'][$fid] && $thread['fid']==$fid) {
					$updatelist[] = array('hitsort',$fid,$thread['tid'],$thread['hits'],'',0);
					$updatetype['hitsort'] = 1;
				}
			}
			if ($db_ifpwcache & 32 && $thread['postdate']>$timestamp-24*3600) {
				if ($thread['hits']>$hitsort_judge['hitsortday'][$fid] && $thread['fid']==$fid) {
					$updatelist[] = array('hitsortday',$fid,$thread['tid'],$thread['hits'],$thread['postdate'],0);
					$updatetype['hitsortday'] = 1;
				}
			}

			if ($db_ifpwcache & 64 && $thread['postdate']>$timestamp-7*24*3600) {
				if ($thread['hits']>$hitsort_judge['hitsortweek'][$fid] && $thread['fid']==$fid) {
					$updatelist[] = array('hitsortweek',$fid,$thread['tid'],$thread['hits'],$thread['postdate'],0);
					$updatetype['hitsortweek'] = 1;
				}
			}
		}
		if ($updatelist) {
			require_once(R_P.'lib/elementupdate.class.php');
			$elementupdate = new ElementUpdate($fid);
			$elementupdate->setJudge('hitsort',$hitsort_judge);
			$elementupdate->setUpdateList($updatelist);
			$elementupdate->setUpdateType($updatetype);
			$elementupdate->updateSQL();
			unset($elementupdate);
		}
		unset($updatelist,$updatetype,$hitsort_judge);
	}
	//End Here
	$pwAnonyHide = $isGM || $pwSystem['anonyhide'];
	$rewids = $cyids = array();
	$arrStatus = array(1=>'vote',2=>'active',3=>'reward',4=>'trade',5=>'debate');
	foreach ($tpcdb as $key => $thread) {
		$foruminfo['allowhtm'] == 1 && $htmurl = $db_htmdir.'/'.$fid.'/'.date('ym',$thread['postdate']).'/'.$thread['tid'].'.html';
		$thread['tpcurl'] = "read.php?tid=$thread[tid]".($page>1 ? "&fpage=$page" : '');
		if ($managemode == 1) {
			$thread['tpcurl'] .= '&toread=1';
		} elseif (!$foruminfo['cms'] && $foruminfo['allowhtm']==1 && file_exists(R_P.$htmurl)) {
			$thread['tpcurl'] = "$htmurl";
		}
		if ($thread['toolfield']) {
			list($t,$e) = explode(',',$thread['toolfield']);
			$sqladd = '';
			if ($t && $t<$timestamp) {
				$sqladd .= ",toolinfo='',topped='0'";$t='';
				$thread['topped']>0 && $updatetop=1;
			}
			if ($e && $e<$timestamp) {
				$sqladd .= ",titlefont=''";$thread['titlefont']='';$e='';
			}
			if ($sqladd) {
				$thread['toolfield'] = $t.($e ? ','.$e : '');
				$db->update("UPDATE pw_threads SET toolfield=".pwEscape($thread['toolfield'])." $sqladd WHERE tid=".pwEscape($thread['tid']));
			}
		}
		$thread['src_subject'] = $thread['subject'];
		$thread['subject'] = substrs($thread['subject'], 50);
		if ($thread['titlefont']) {
			$titledetail = explode("~",$thread['titlefont']);
			if ($titledetail[0]) $thread['subject'] = "<font color=$titledetail[0]>$thread[subject]</font>";
			if ($titledetail[1]) $thread['subject'] = "<b>$thread[subject]</b>";
			if ($titledetail[2]) $thread['subject'] = "<i>$thread[subject]</i>";
			if ($titledetail[3]) $thread['subject'] = "<u>$thread[subject]</u>";
		}
		if ($thread['ifmark']) {
			$thread['ifmark'] = $thread['ifmark']>0 ? " <span class='gray tpage'>( +$thread[ifmark] )</span> " : " <span class='gray tpage'>( $thread[ifmark] )</span> ";
		} else {
			unset($thread['ifmark']);
		}
		if (isset($arrStatus[$thread['special']])) {
			$p_status = $thread['locked']%3 == 0 ? $arrStatus[$thread['special']] : $arrStatus[$thread['special']].'lock';
		} elseif ($thread['locked']%3<>0) {
			$p_status = $thread['locked']%3 == 1 ? 'topiclock' : 'topicclose';
		} else {
			$p_status = $thread['ifmagic'] ? 'magic' : ($thread['replies']>=10 ? 'topichot' : 'topicnew');
		}
		$thread['inspect'] && $thread['inspect'] = explode("\t",$thread['inspect']);
		$thread['status'] = $p_status;//"<img src=\"$imgpath/$stylepath/thread/".$p_status.".gif\" border=0 align=\"absmiddle\">";
		$thread['topped'] && $ifsort=1;
		$thread['ispage'] = '';
		if ($thread['replies']+1>$db_readperpage) {
			if (($thread['replies']+1)%$db_readperpage==0) {
				$numofpage = ($thread['replies']+1)/$db_readperpage;
			} else {
				$numofpage = floor(($thread['replies']+1)/$db_readperpage)+1;
			}
			$fpage = $page > 1 ? "&fpage=$page" : "";
			$thread['ispage']=' ';
			$thread['ispage'].=" <img src=\"$imgpath/$stylepath/file/multipage.gif\" border=0 align=\"absmiddle\"> <span style=\"font-family:verdana;\">";
			for($j=1; $j<=$numofpage; $j++) {
				if ($j==6 && $j+1<$numofpage) {
					$thread['ispage'].=" .. <a href=\"read.php?tid=$thread[tid]$fpage&page=$numofpage\">$numofpage</a>";
					break;
				} elseif ($j == 1) {
					$thread['ispage'].=" <a href=\"read.php?tid=$thread[tid]$fpage\">$j</a>";
				} else {
					$thread['ispage'].=" <a href=\"read.php?tid=$thread[tid]$fpage&page=$j\">$j</a>";
				}
			}
			$thread['ispage'].='</span> ';
		}
		$postdetail = explode(",",$thread['lastpost']);

		if ($thread['ifupload']) {
			$atype = $attachtype[$thread['ifupload']];
			$thread['titleadd']=" <img src=\"$imgpath/$stylepath/file/$atype.gif\" border=0 align=\"absmiddle\">";
		} else {
			$thread['titleadd']="";
		}
		if ($managecheck) {
			if ($thread['fid']==$fid) {
				$thread['adminbox']="<input type=\"checkbox\" name=\"tidarray[]\" value=\"$thread[tid]\" />";
			} else {
				$thread['adminbox']="&nbsp;&nbsp;&nbsp;";
			}
		}
		if ($db_threademotion) {
			if ($thread['icon']=="R"||!$thread['icon']) {
				$thread['useriocn']='';
			} else {
				$thread['useriocn']="<img src=\"$imgpath/post/emotion/$thread[icon].gif\" border=0 align=\"absmiddle\"> ";
			}
		}
		if ($thread['anonymous'] && $thread['authorid']!=$winduid && !$pwAnonyHide) {
			$thread['author']	= $db_anonymousname;
			$thread['authorid'] = 0;
		}
		if ($thread['special'] == 3 && $thread['state'] < 1) {
			$rewids[] = $thread['tid'];
		}

		//获取分类信息的帖子id
		if ($modelid > 0) {
			$topicids[] = $thread['tid'];
		}

		//获取团购的帖子id
		if ($pcid > 0) {
			$postcatepcids[] = $thread['tid'];
		}

		if (getstatus($thread['tpcstatus'], 1)) {
			$cyids[] = $thread['tid'];
		}
		$threaddb[$thread['tid']] = $thread;
	}
	if ($rewids) {
		$rewids = pwImplode($rewids);
		$query = $db->query("SELECT tid,cbval,caval FROM pw_reward WHERE tid IN($rewids)");
		while ($rt = $db->fetch_array($query)) {
			$threaddb[$rt['tid']]['rewcredit'] = $rt['cbval'] + $rt['caval'];
		}
	}
	if ($cyids) {
		$query = $db->query("SELECT a.tid,a.cyid,c.cname FROM pw_argument a LEFT JOIN pw_colonys c ON a.cyid=c.id WHERE tid IN (" . pwImplode($cyids) . ')');
		while ($rt = $db->fetch_array($query)) {
			$threaddb[$rt['tid']]['colony'] = $rt;
		}
	}

	if ($topicids) {
		$topicvaluetable = GetTopcitable($modelid);
		$query = $db->query("SELECT * FROM $topicvaluetable WHERE tid IN (" .pwImplode($topicids). ")");
		while ($rt = $db->fetch_array($query)) {
			$threaddb[$rt['tid']]['topic'] = $rt;
		}
	}

	if ($postcatepcids) {//团购活动
		$pcvaluetable = GetPcatetable($pcid);
		$query = $db->query("SELECT * FROM $pcvaluetable WHERE tid IN (" .pwImplode($postcatepcids). ")");
		while ($rt = $db->fetch_array($query)) {
			$threaddb[$rt['tid']]['topic'] = $rt;
		}
	}

	if ($updatetop) {
		require_once(R_P.'require/updateforum.php');
		updatetop();
	}
	unset($tpcdb,$query,$topadd,$searchadd,$sql,$limit2,$R,$p_status,$updatetop,$rewids,$arrStatus);
} else {
	include_once Pcv(D_P."data/bbscache/fcache_{$fid}_{$page}.php");
	if ($page == 1 && !$ifsort) {
		foreach ($threaddb as $key => $value) {
			$value['topped'] && $ifsort = 1;
			break;
		}
	}
}

$psot_sta = $titletop1 = '';
$fastpost = 'fastpost';
$t_exits  = 0;
$t_typedb = $t_subtypedb = array();
if ($t_db) {
	foreach ($t_db as $value) {
		if ($value['upid'] == 0) {
			$t_typedb[$value['id']] = $value;
		} else {
			$value['id'] == $type && $pwSelectType = $value['upid'];
			$t_subtypedb[$value['upid']][$value['id']] = $value['name'];
		}
		$t_exits = 1;
	}
}
$t_childtypedb = $t_subtypedb;

if ($t_subtypedb) {
	$t_subtypedb = pwJsonEncode($t_subtypedb);
	$t_sub_exits = 1;
}
if ($t_db[$type]['upid']) {
	$t_child = $t_childtypedb[$t_db[$type]['upid']];
} else {
	$t_child = $t_childtypedb[$type];
}

$db_forcetype = $t_exits && $t_per=='2' && !$admincheck ? 1 : 0; // 是否需要强制主题分类

$db_maxtypenum == 0 && $db_maxtypenum = 5;
$db_menuinit .= ",'td_post' : 'menu_post','td_post1' : 'menu_post','td_special' : 'menu_special'";
if ($winddb['shortcut']) {
	$myshortcut = 'true';
} else {
	$myshortcut = 'false';
}

//allowtype onoff
if ($foruminfo['allowtype'] && (($foruminfo['allowtype'] & 1) || ($foruminfo['allowtype'] & 2 && $_G['allownewvote']) || ($foruminfo['allowtype'] & 4 && $_G['allowactive']) || ($foruminfo['allowtype'] & 8 && $_G['allowreward'])|| ($foruminfo['allowtype'] & 16) || $foruminfo['allowtype'] & 32 && $_G['allowdebate'])) {
	$N_allowtypeopen = true;
} else {
	$N_allowtypeopen = false;
}
$element_class = L::loadClass('element');
$hot_threads = $element_class->replySortWeek($fid, 10);
$related_threads = threadrelated('allpost');
function threadrelated ($relatedcon) {

	global $db,$db_iftag,$db_threadrelated,$forumset,$fid,$read,$tid,$db_modes,$db_dopen,$db_phopen,$db_share_open,$db_groups_open,$groupid,$timestamp;

	$relatedb = array();

	if (in_array($relatedcon,array('allpost','alldigest','allhits','allreply','forumpost','forumdigest','forumhits','forumreply'))) {
		//require_once(R_P.'require/element.class.php');
		//$element = new Element($forumset['relatednums']);
		$element = L::loadClass('element');
		$element->setDefaultNum($forumset['relatednums']);
		switch ($relatedcon) {
			case 'allpost' :
				$relatedb = $element->newSubject();break;
			case 'alldigest' :
				$relatedb = $element->digestSubject();break;
			case 'allhits' :
				$relatedb = $element->hitSort();break;
			case 'allreply' :
				$relatedb = $element->replySort();break;
			case 'forumpost' :
				$relatedb = $element->newSubject($fid);break;
			case 'forumdigest' :
				$relatedb = $element->digestSubject($fid);break;
			case 'forumhits' :
				$relatedb = $element->hitSort($fid);break;
			case 'forumreply' :
				$relatedb = $element->replySort($fid);break;
		}
	} elseif ($relatedcon == 'oinfo') {//继续改进
		if ($db_modes['o']['ifopen']) {
			require_once("require/app_core.php");
			$addwhere = '';
			if (!$db_dopen) {
				$addwhere .= " AND type!='diary'";
			}
			if (!$db_phopen) {
				$addwhere .= " AND type!='photo'";
			}
			if (!$db_share_open) {
				$addwhere .= " AND type!='share'";
			}
			if (!$db_groups_open) {
				$addwhere .= " AND type!='colony'";
			}
			$query = $db->query("SELECT type,descrip FROM pw_feed WHERE uid=".pwEscape($read['authorid']).$addwhere." ORDER BY timestamp DESC  ".pwLimit(0,$forumset['relatednums']));
			while ($rt = $db->fetch_array($query)) {
				$rt['title'] = parseFeedRead($rt['descrip']);
				$rt['url'] = "u.php?uid=$read[authorid]";
				unset($rt['type']);
				$relatedb[] = $rt;
			}
		}

	} elseif (in_array($relatedcon,array('pictags','hottags'))) {
		$tagid = $tagdbs = array();
		$endtime = $timestamp - 30*24*3600;
		$sql = 'WHERE t.ifcheck=1 AND t.tid !='.pwEscape($tid). ' AND t.postdate >='.pwEscape($endtime);
		$fidout = array('0');
		$query = $db->query("SELECT fid,allowvisit,password FROM pw_forums WHERE type<>'category'");
		while ($rt = $db->fetch_array($query)) {
			$allowvisit = (!$rt['allowvisit'] || $rt['allowvisit'] != str_replace(",$groupid,",'',$rt['allowvisit'])) ? true : false;
			if ($rt['password'] || !$allowvisit) {
				$fidout[] = $rt['fid'];
			}
		}
		$fidout = pwImplode($fidout);
		$fidout && $sql .= " AND fid NOT IN ($fidout)";

		if ($db_iftag) {
			if ($read['tags'] && $relatedcon == 'pictags') {
				list($tagdb,$tpc_tag) = explode("\t",$read['tags']);
				$tagdbs = explode(' ',$tagdb);
			} elseif ($relatedcon == 'hottags') {
				@include_once(D_P.'data/bbscache/tagdb.php');
				$j = 0;
				foreach ($tagdb as $key => $val) {
					$j++;
					if ($j > 5) break;
					$tagdbs[] = $key;
				}
				unset($tagdb);
			}

			if ($tagdbs) {
				$query = $db->query("SELECT tagid FROM pw_tags WHERE tagname IN(" . pwImplode($tagdbs) . ')');
				while ($rt = $db->fetch_array($query)) {
					$tagid[] = $rt['tagid'];
				}
			}
			if ($tagid) {
				$query = $db->query("SELECT t.tid,t.subject FROM pw_tagdata tg LEFT JOIN pw_threads t USING(tid) $sql AND tg.tagid IN(" . pwImplode($tagid) . ") GROUP BY tid ORDER BY postdate DESC ".pwLimit(0,$forumset['relatednums']));
				while ($rt = $db->fetch_array($query)) {
					$rt['title'] = $rt['subject'];
					$rt['url'] = "read.php?tid=".$rt['tid'];
					unset($rt['subject']);
					unset($rt['tid']);
					$relatedb[] = $rt;
				}
			}
		}
	} elseif (in_array($relatedcon,array('ownpost','owndigest','ownhits','ownreply'))) {
		$endtime = $timestamp - 15*24*3600;
		$sql = "WHERE ifcheck=1 AND tid !=".pwEscape($tid). "AND postdate >=".pwEscape($endtime)." AND authorid=".pwEscape($read['authorid'])." AND fid>0 ";
		$orderby = '';

		switch ($relatedcon) {
			case 'ownpost' :
				$orderby .= " ORDER BY postdate DESC";
				break;
			case 'owndigest' :
				$sql .= " AND digest>0";
				$orderby .= " ORDER BY postdate DESC";
				break;
			case 'ownhits' :
				$orderby .= " ORDER BY hits DESC";
				break;
			case 'ownreply' :
				$orderby .= " ORDER BY replies DESC";
				break;
		}
		$query = $db->query("SELECT tid,subject FROM pw_threads FORCE INDEX(postdate) $sql $orderby".pwLimit(0,$forumset['relatednums']));
		while ($rt = $db->fetch_array($query)) {
			$rt['title'] = $rt['subject'];
			$rt['url'] = "read.php?tid=".$rt['tid'];
			unset($rt['subject']);
			unset($rt['tid']);
			$relatedb[] = $rt;
		}
	}

	return $relatedb;
}
require_once PrintEot('area_thread');

footer();

?>