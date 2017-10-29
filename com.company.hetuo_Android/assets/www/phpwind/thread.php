<?php
define('SCR','thread');
require_once('global.php');
require_once(R_P.'require/forum.php');
include_once(D_P.'data/bbscache/cache_thread.php');

//读取版块信息
empty($fid) && Showmsg('data_error');

/*The app client*/
if ($db_siteappkey && $db_apps_list['17']['status'] == 1) {
	$forumappinfo = array();
	$appclient = L::loadClass('appclient');
	$forumappinfo = $appclient->showForumappinfo($fid,'thread','17');
}
/*The app client*/

if (!($foruminfo = L::forum($fid))) {
	Showmsg('data_error');
}

$rt = $db->get_one("SELECT fd.topic,fd.top1,fd.top2,fd.topthreads,fd.lastpost,fd.aid,fd.aids,fd.aidcache,fd.tpost,fd.topic,fd.article ,a.ifconvert,a.author,a.startdate,a.enddate,a.subject,a.content FROM pw_forumdata fd LEFT JOIN pw_announce a ON fd.aid=a.aid WHERE fd.fid=".pwEscape($fid));

$rt && $foruminfo += $rt;#版块信息合并
$forumset = $foruminfo['forumset'];
!$forumset['commend'] && $foruminfo['commend'] = null;
$foruminfo['type']=='category' && ObHeader('cate.php?cateid='.$fid);
if ($forumset['link']) {
	$flink = str_replace("&amp;","&",$forumset['link']);
	ObHeader($flink);
}

$type = (int)GetGP('type');
$pw_seoset = L::loadClass('seoset');
$pw_seoset->set_types($foruminfo['topictype'],$type);
$pw_seoset->set_ifCMS($foruminfo['ifcms']);
$webPageTitle = $pw_seoset->getPageTitle($foruminfo['title'],$foruminfo['name']);
$metaDescription = $pw_seoset->getPageMetadescrip($foruminfo['metadescrip'],$foruminfo['name']);
$metaKeywords = $pw_seoset->getPageMetakeyword($foruminfo['keywords'],$foruminfo['name']);
$toptids = trim($foruminfo['topthreads'],',');

//门户形式浏览
if ($foruminfo['ifcms'] && $db_modes['area']['ifopen']) {
	InitGP(array('viewbbs'));
	if (!$viewbbs) {
		require_once R_P. 'mode/area/area_thread.php';exit;
	}
	$viewbbs = $viewbbs ? "&viewbbs=$viewbbs" : "";
}

wind_forumcheck($foruminfo);

//版块浏览及管理权限
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
	!$foruminfo['allowvisit'] && forum_creditcheck();#积分限制浏览
	$foruminfo['forumsell'] && forum_sell($fid);#出售版块
}

$forumset['newtime'] && $db_newtime = $forumset['newtime'];
if ($foruminfo['aid'] && ($foruminfo['startdate']>$timestamp || ($foruminfo['enddate'] && $foruminfo['enddate']<$timestamp))) {
	$foruminfo['aid'] = 0;
}

list($guidename,$forumtitle) = getforumtitle(forumindex($foruminfo['fup'],1));

require_once(R_P.'require/header.php');

$msg_guide = headguide($guidename);
unset($guidename,$foruminfo['forumset']);

//版主列表
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
//版主推荐
if ($forumset['commend'] && ($forumset['autocommend'] || $forumset['commendlist']) && $forumset['commendtime'] && $timestamp-$forumset['ifcommend']>$forumset['commendtime']) {
	updatecommend($fid,$forumset);
}

//版块浏览记录
$threadlog = str_replace(",$fid,",',',GetCookie('threadlog'));
$threadlog.= ($threadlog ? '' : ',').$fid.',';
substr_count($threadlog,',')>11 && $threadlog = preg_replace("/[\d]+\,/i",'',$threadlog,3);
Cookie('threadlog',$threadlog);

Update_ol();

$orderClass = array();//排序
InitGP(array('subtype','search','orderway','asc','topicsearch'));
InitGP(array('page','modelid','pcid','special'),'GP',2);
($orderway && $asc == "DESC" ) ? $orderClass[$orderway] = "↓" : $orderClass['lastpost'] = "↓";

$searchadd = $thread_children = $thread_online = $fastpost = $updatetop = $urladd = '';

$db_maxpage && $page > $db_maxpage && $page = $db_maxpage;
(int)$page<1 && $page = 1;

//版块及所属分类公告
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
//子版块
$forumdb = array();
if ($foruminfo['childid']) {
	require_once(R_P."require/thread_child.php");
}

//快捷管理
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
	$trd_adminhide = "<form action=\"mawhole.php?$viewbbs\" method=\"post\" name=\"mawhole\"><input type=\"hidden\" name=\"fid\" value=\"$fid\">";
} else {
	$trd_adminhide = '';
}

$colspannum = 6;

if ($foruminfo['allowtype'] && (($foruminfo['allowtype'] & 1) || ($foruminfo['allowtype'] & 2 && $_G['allownewvote']) || ($foruminfo['allowtype'] & 4 && $_G['allowactive']) || ($foruminfo['allowtype'] & 8 && $_G['allowreward'])|| ($foruminfo['allowtype'] & 16) || $foruminfo['allowtype'] & 32 && $_G['allowdebate'])) {
	$N_allowtypeopen = true;
} else {
	$N_allowtypeopen = false;
}

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
	if (strpos(",".$foruminfo['modelid'].",",",".$modelid.",") === false) {
		Showmsg('forum_model_unfined');
	}
	!$postTopic->topicmodeldb[$modelid]['ifable'] && Showmsg('topic_model_unable');
	if (!$postTopic->topiccatedb[$postTopic->topicmodeldb[$modelid]['cateid']]['ifable']) {
		Showmsg('topic_cate_unable');
	}

	foreach ($fielddb as $key => $value) {
		if($value['threadshow'] == 1) {
			$threadshowfield[$key] = $value;
		}
	}
	$colspannum = count($threadshowfield) + 2;
	$initSearchHtml = $postTopic->initSearchHtml($modelid);
} elseif ($pcid > 0) {/*团购活动*/
	$fielddb = $postCate->getFieldData($pcid,'one');
	if (strpos(",".$foruminfo['pcid'].",",",".$pcid.",") === false || !$postCate->postcatedb[$pcid]['ifable']) {
		Showmsg('forum_pc_unfined');
	}

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
}
if ($modelid > 0) {//选择某个信息分类中的某个模板的条件下
	$searchadd .= " AND modelid=".pwEscape($modelid) . " AND ifcheck='1'";
	$urladd .= "&modelid=$modelid";
	$pwSelectType = 'model_'.$modelid;
} elseif ($pcid > 0) {//团购活动
	$searchadd .= " AND special=".pwEscape($pcid+20) . " AND ifcheck='1'";
	$urladd .= "&pcid=$pcid";
	$pwSelectType = 'pcid_'.$pcid;
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
	$count += $foruminfo['top2']+$foruminfo['top1'];
}
$sql = 'fid='.pwEscape($fid).' AND topped=0';

$tpcdb = $ordersel = $ascsel = array();
if ($_G['alloworder']) {
	if (!in_array($orderway,array('lastpost','postdate','hits','replies','favors'))) {
		$orderway = $forumset['orderway'] ? $forumset['orderway'] : 'lastpost';
	} else {
		$urladd .= "&orderway=$orderway";
	}
	$ordersel[$orderway] = 'selected';

	if (!in_array($asc,array('DESC','ASC'))) {
		$asc = $forumset['asc'] ? $forumset['asc'] : 'DESC';
	} else {
		$urladd .= "&asc=$asc";
	}
	$ascsel[$asc]='selected';
} else {
	$asc = $forumset['asc'] ? $forumset['asc'] : 'DESC';
	$orderway = $forumset['orderway'] ? $forumset['orderway'] : 'lastpost';
}
$condisel[$search] = 'selected';

$numofpage = ceil($count/$db_perpage);
$numofpage < 1 && $numofpage = 1;
if ($page > $numofpage) {
	$page  = $numofpage;
}
$start_limit = intval(($page-1) * $db_perpage);
$totalpage	= min($numofpage,$db_maxpage);

$pages		= numofpage($count,$page,$numofpage,"thread.php?fid=$fid{$urladd}$viewbbs&",$db_maxpage);
$attachtype	= array('1'=>'img','2'=>'txt','3'=>'zip');

$fcache = 0;
if ($db_fcachenum && $page < $db_fcachenum && empty($urladd) && $topicsearch != 1) {
	$fcachetime = pwFilemtime(D_P."data/bbscache/fcache_{$fid}_{$page}.php");
	$lastpost = explode("\t",$foruminfo['lastpost']);
	if (!file_exists(D_P."data/bbscache/fcache_{$fid}_{$page}.php") || $lastpost[2]>$fcachetime && $timestamp-$fcachetime>$db_fcachetime) {
		$fcache = 1;
	} else {
		$fcache = 2;
	}
}
$threaddb = array();

if ($fcache < 2) {
	$R = 0;
	if ($db_topped && !$pcid && !$modelid) {
		$rows = (int)($foruminfo['top2'] + $foruminfo['top1']);
		if ($start_limit < $rows) {
			$L = (int)min($rows-$start_limit,$db_perpage);
			$limit  = pwLimit($start_limit,$L);
			$offset = 0;
			$limit2 = $L == $db_perpage ? '' : $db_perpage-$L;
			if ($toptids) {
				$query = $db->query("SELECT * FROM pw_threads WHERE tid IN($toptids) ORDER BY topped DESC,lastpost DESC $limit");
				while ($rt = $db->fetch_array($query)) {
					$tpcdb[] = $rt;
				}
				$db->free_result($query);
			}
			unset($toptids,$L,$limit);
		} else {
			list($st,$lt,$asc,$R) = getstart($start_limit-$rows,$asc,$count);
			$offset = $st; $limit2 = $lt;
		}
		unset($rows);
	} else {
		list($st,$lt,$asc,$R) = getstart($start_limit,$asc,$count);
		$offset = $st; $limit2 = $lt;
	}

	if ($topicsearch == 1) {
		InitGP(array('searchname','new_searchname'));
		$pcsqladd = '';
		if ($search == 'digest') {
			$pcsqladd .= " AND digest>'0' AND ifcheck='1'";
		} elseif ($search == 'check') {
			if ($isGM || $pwSystem['viewcheck']) {
				$pcsqladd .= " AND ifcheck='0'";
			} else {
				$pcsqladd .= ' AND authorid='.pwEscape($winduid)." AND ifcheck='0'";
			}
		} elseif (is_numeric($search)) {
			$pcsqladd .= " AND lastpost>=".pwEscape($timestamp - $search*84600)." AND ifcheck='1'";
		}

		$searchname && $new_searchname = StrCode(serialize($searchname));
		if ($modelid > 0) {
			list($count,$tiddb,$alltiddb) = $postTopic->getSearchvalue($new_searchname,'one',true);
		} elseif($pcid > 0) {
			list($count,$tiddb,$alltiddb) = $postCate->getSearchvalue($new_searchname,'one',true);
		}

		if ($search && $count && $alltiddb) {
			$count = $db->get_value("SELECT COUNT(*) as count FROM pw_threads WHERE tid IN (".pwImplode($alltiddb).") $pcsqladd");
		}

		$numofpage = ceil($count/$db_perpage);
		$numofpage < 1 && $numofpage = 1;
		if ($page > $numofpage) {
			$page  = $numofpage;
		}
		$totalpage	= min($numofpage,$db_maxpage);
		$count == -1 && $count = 0;
		$pages		= numofpage($count,$page,$numofpage,"thread.php?fid=$fid&pcid=$pcid&topicsearch=$topicsearch&new_searchname=$new_searchname&search=$search&orderway=$orderway&asc=$asc&",$db_maxpage);
		$tpcdb = array();
		if ($tiddb){
			$query = $db->query("SELECT * FROM pw_threads WHERE tid IN (".pwImplode($tiddb).") $pcsqladd ORDER BY $orderway $asc");
			while ($thread = $db->fetch_array($query)) {
				$tpcdb[] = $thread;
			}
			$db->free_result($query);
		}

	} elseif ($limit2) {
		if (empty($urladd) && strtolower($db_datastore) == 'memcache' && !$R && $offset < 980) {
			$threadlist = L::loadClass("threadlist");
			$tmpTpcdb = $threadlist->getThreads($fid,$offset,$limit2);
			$tpcdb = array_merge((array)$tpcdb,(array)$tmpTpcdb);
		} else {
			//$topadd .= $R ? 'topped ASC,' : 'topped DESC,';
			$query = $db->query("SELECT * FROM pw_threads WHERE $sql $searchadd ORDER BY $topadd $orderway $asc ".pwLimit($offset,$limit2));
			while ($thread = $db->fetch_array($query)) {
				$tpcdb[] = $thread;
			}
			$db->free_result($query);
			$R && $tpcdb = array_reverse($tpcdb);
		}
	}

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
		$thread['tpcurl'] = "read.php?tid={$thread[tid]}$viewbbs".($page>1 ? "&fpage=$page" : '');
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
				/* clear thread cache*/
				$threads = L::loadClass('Threads');
				$threads->delThreads($thread['tid']);
			}
		}
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
		$thread['tooltip'] = $p_status;
		$thread['status'] = "<img src=\"$imgpath/$stylepath/thread/".$p_status.".gif\" border=0 align=\"absmiddle\">";
		$thread['topped'] && $ifsort=1;
		$thread['ispage'] = '';
		if ($thread['topreplays']+$thread['replies']+1>$db_readperpage) {
			$numofpage = ceil(($thread['topreplays']+$thread['replies']+1)/$db_readperpage);
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
		//if ($managecheck) {
		//	if ($thread['fid']==$fid) {
				$thread['adminbox']="<input type=\"checkbox\" name=\"tidarray[]\" value=\"$thread[tid]\" />";
		//	} else {
		//		$thread['adminbox']="&nbsp;&nbsp;&nbsp;";
		//	}
		//}
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
	if ($fcache == 1) {
		writeover(D_P."data/bbscache/fcache_{$fid}_{$page}.php", "<?php\r\n\$threaddb=".pw_var_export($threaddb).";\r\n?>");
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

$db_threadshowpost == 1 && $groupid != 'guest' && $fastpost = 'fastpost';


$psot_sta = $titletop1 = '';

$t_exits  = 0;
$t_typedb = $t_subtypedb = array();
if ($t_db) {
	foreach ($t_db as $value) {
		if ($value['upid'] == 0) {
			$t_typedb[$value['id']] = $value;
		} else {
			$t_subtypedb[$value['upid']][$value['id']] = strip_tags($value['name']);
		}
		$t_exits = 1;
	}
}
$t_childtypedb = $t_subtypedb;
foreach ($t_typedb as $value) {
	if ($t_childtypedb[$value['id']]) {
		$db_menuinit .= ",'thread_type_$value[id]' : 'thread_typechild_$value[id]'";
	}
}
if ($t_subtypedb) {
	$t_subtypedb = pwJsonEncode($t_subtypedb);
	$t_sub_exits = 1;
}
$db_forcetype = $t_exits && $t_per=='2' && !$admincheck ? 1 : 0; // 是否需要强制主题分类


$db_maxtypenum == 0 && $db_maxtypenum = 5;
$db_menuinit .= ",'td_post' : 'menu_post','td_post1' : 'menu_post','td_special' : 'menu_special'";
if ($winddb['shortcut']) {
	$myshortcut = 'true';
} else {
	$myshortcut = 'false';
}

if (defined('M_P') && file_exists(M_P.'thread.php')) {
	require_once(M_P.'thread.php');
}
require_once PrintEot('thread');
$noticecache = 900;
$foruminfo['enddate'] && $foruminfo['enddate']<=$timestamp && $foruminfo['aidcache'] = $timestamp-$noticecache;
if ($foruminfo['aidcache'] && $timestamp-$foruminfo['aidcache']>$noticecache-1 && ($foruminfo['startdate']>$timestamp || ($foruminfo['enddate'] && ($foruminfo['enddate']<=$timestamp || $foruminfo['aids'])))) {
	$foruminfo['aid'] && $foruminfo['aids'] .= ",$foruminfo[aid]";
	require_once(R_P.'require/updatenotice.php');
	updatecache_i($fid,$foruminfo['aids']);
}
footer();

function getstart($start,$asc,$count) {
	global $db_perpage,$page,$numofpage;
	$limit = $db_perpage;
	if ($page>20 && $page>ceil($numofpage/2)) {
		$asc = $asc=='DESC' ? 'ASC' : 'DESC';
		$start = $count-$page*$db_perpage;
		if ($start < 0) {
			$limit = $db_perpage+$start;
			$start = 0;
		}
		return array($start,$limit,$asc,1);
	} else {
		return array($start,$limit,$asc,0);
	}
}
?>