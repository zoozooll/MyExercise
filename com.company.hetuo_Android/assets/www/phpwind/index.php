<?php
define('SCR','index');
require_once('global.php');

include_once(D_P.'data/bbscache/cache_index.php');
include_once(D_P.'data/bbscache/forum_cache.php');

//notice
$noticedb = array();
foreach ($notice_A as $value) {
	if ($value['startdate']<=$timestamp && (!$value['enddate'] || $value['enddate']>=$timestamp)) {
		$value['startdate'] = $value['stime'] ? $value['stime'] : get_date($value['startdate'],'y-m-d');
		!$value['url'] && $value['url'] = 'notice.php#'.$value['aid'];
		$noticedb[$value['aid']] = $value;
	}
}
$notice_A = $noticedb;
unset($noticedb);
$topics = $article = $tposts = 0;
$cateid = (int)GetGP('cateid');

$secdomain = null;
$m = GetGP('m');
if (!$cateid && $db_modedomain && $secdomain = array_search($pwServer['HTTP_HOST'], $db_modedomain)) {
	$cateinfo = explode("_",$secdomain);
	if ('cate' == $cateinfo[0]) {
		$cateid = (int)$cateinfo[1];
		$db_bbsurl = $_mainUrl;
		$m = 'area';
	}
	unset($cateinfo);
}

selectMode($m);
if (defined('M_P') && file_exists(M_P . ($cateid > 0 ? 'cate' : 'index') . '.php')) {
	if ($cateid > 0) {
		$_GET['fid'] = $_GET['cateid'] = $cateid;
		require_once M_P.'cate.php';
	} else {
		require_once(M_P.'index.php');
		$db_newinfoifopen = 0;
	}
} else {

	$pw_seoset = L::loadClass('seoset');
	$webPageTitle = $pw_seoset->getPageTitle();
	$metaDescription = $pw_seoset->getPageMetadescrip();
	$metaKeywords = $pw_seoset->getPageMetakeyword();

	$newpic = (int)GetCookie('newpic');
	$forumdb = $catedb = $showsub = array();
	$c_htm = 0;
	if ($db_forumdir) {
		require_once(R_P.'require/dirname.php');
	} elseif ($cateid > 0) {
		$catestyle = $forum[$cateid]['style'];
		if ($catestyle && file_exists(D_P."data/style/$catestyle.php")) {
			$skin = $catestyle;
		}
		#SEO
		$pw_seoset->set_page('thread');
		$pw_seoset->set_ifCMS($forum[$cateid]['ifcms']);
		$webPageTitle = $pw_seoset->getPageTitle($forum[$cateid]['title'],$forum[$cateid]['name']);
		$metaDescription = $pw_seoset->getPageMetadescrip($forum[$cateid]['metadescrip'],$forum[$cateid]['name']);
		$metaKeywords = $pw_seoset->getPageMetakeyword($forum[$cateid]['keywords'],$forum[$cateid]['name']);

		$sqlwhere = 'AND (f.fid=' . pwEscape($cateid) . ' OR f.fup=' . pwEscape($cateid) . ')';
		unset($metakeyword);
	}
	require_once(R_P.'require/header.php');
	!$db_showcms && $sqlwhere .= " AND f.cms!='1'";

	/*The app client*/
	if ($db_siteappkey && ($db_apps_list['17']['status'] == 1 || is_array($db_threadconfig))) {
		$appclient = L::loadClass('appclient');
		if (is_array($db_threadconfig)) {
			$threadright = array();
			$threadright = $appclient->getThreadRight();
		}
	}
	/*The app client*/

	if ($cookie_deploy = $_COOKIE['deploy']) {
		$deployfids = explode("\t",$cookie_deploy);
		$deployfids = array_flip($deployfids);
		unset($cookie_deploy);
	} else {
		$deployfids = array();
	}

	$query  = $db->query("SELECT f.fid,f.name,f.type,f.childid,f.fup,f.logo,f.descrip,f.metadescrip,f.forumadmin,f.across,f.allowhtm,f.password,f.allowvisit,f.showsub,f.ifcms,fd.tpost,fd.topic,fd.article,fd.subtopic,fd.top1,fd.lastpost FROM pw_forums f LEFT JOIN pw_forumdata fd USING(fid) WHERE f.ifsub='0' AND f.ifcms!=2 $sqlwhere ORDER BY f.vieworder");
	while ($forums = $db->fetch_array($query)) {
		if ($forums['type'] === 'forum') {
			if ($forums['showsub'] && $forums['childid']) {
				$showsub[$forums['fid']] = '';
			}
			$forums['topics'] = $forums['topic']+$forums['subtopic'];
			if ($db_topped) {
				$forums['topics'] += $forums['top1'];
				$forums['article'] += $forums['top1'];
			}
			$article += $forums['article'];
			$topics += $forums['topics'];
			$tposts += $forums['tpost'];
			$forums['au'] = $forums['admin'] = '';
			if (!$forums['password'] && (!$forums['allowvisit'] || allowcheck($forums['allowvisit'],$groupid, $winddb['groups'],$forums['fid'],$winddb['visit']))) {
				list($forums['t'],$forums['au'],$forums['newtitle'],$forums['ft']) = explode("\t",$forums['lastpost']);
				$forums['pic'] = $newpic < $forums['newtitle'] && ($forums['newtitle'] + $db_newtime > $timestamp) ? 'new' : 'old';
				$forums['newtitle'] = get_date($forums['newtitle']);
				$forums['t'] = substrs($forums['t'],26);
			} elseif ($forum[$forums['fid']]['f_type'] === 'hidden' && $groupid != 3) {
				if ($forums['password'] && allowcheck($forums['allowvisit'],$groupid,$winddb['groups'], $forums['fid'],$winddb['visit'])) {
					$forums['pic'] = 'lock';
				} else {
					continue;
				}
			} else {
				$forums['pic'] = 'lock';
			}
			$forums['allowhtm'] == 1 && $c_htm = 1;
			if ($db_indexfmlogo == 2) {
				if(!empty($forums['logo']) && strpos($forums['logo'],'http://') === false && file_exists($attachdir.'/'.$forums['logo'])){
					$forums['logo'] = "$attachpath/$forums[logo]";
				}
			} elseif ($db_indexfmlogo == 1 && file_exists("$imgdir/$stylepath/forumlogo/$forums[fid].gif")) {
				$forums['logo'] = "$imgpath/$stylepath/forumlogo/$forums[fid].gif";
			} else {
				$forums['logo'] = '';
			}
			if ($forums['forumadmin']) {
				$forumadmin = explode(',',$forums['forumadmin']);
				foreach ($forumadmin as $value) {
					if ($value) {
						if (!$db_adminshow) {
							$forums['admin'] .= '<a href="u.php?action=show&username='.rawurlencode($value)."\">$value</a> ";
						} else {
							$forums['admin'] .= "<option value=\"$value\">$value</option>";
						}
					}
				}
			}

			/*The app client*/
			if ($db_siteappkey && $db_apps_list['17']['status'] == 1) {
				$forums['forumappinfo'] = $appclient->showForumappinfo($forums['fid'],'forum_erect,forum_across','17');
			}
			/*The app client*/

			$forumdb[$forums['fup']][] = $forums;
		} elseif ($forums['type'] === 'category') {
			if (isset($deployfids[$forums['fid']])) {
				$forums['deploy_img'] = 'open';
				$forums['tbody_style'] = 'none';
				$forums['admin'] = '';
			} else {
				$forums['deploy_img'] = 'fold';
				$forums['tbody_style'] = $forums['admin'] = '';
			}
			if ($forums['forumadmin']) {
				$forumadmin = explode(',',$forums['forumadmin']);
				foreach ($forumadmin as $key => $value) {
					if ($value) {
						if ($key==10) {
							$forums['admin'] .= '...';
							break;
						}
						$forums['admin'] .= '<a href="u.php?action=show&username='.rawurlencode($value)."\" class=\"cfont\">$value</a> ";
					}
				}
			}
			$catedb[] = $forums;
		}
	}
	$db->free_result($query);
	// View sub
	if (!empty($showsub)) {
		foreach ($forum as $value) {
			if (isset($showsub[$value['fup']]) && $value['f_type'] != 'hidden') {
				$showsub[$value['fup']] .= ($showsub[$value['fup']] ? ' | ' : '')."<a href=\"thread.php?fid=$value[fid]\">$value[name]</a>";
			}
		}
	}
	unset($forums,$forum,$db_showcms);
	//info deploy
	if (isset($deployfids['info'])) {
		$cate_img  = 'open';
		$cate_info = 'none';
	} else {
		$cate_img  = 'fold';
		$cate_info = '';
	}
	// update birth day
	if ($db_indexshowbirth) {
		$brithcache = '';
		require_once(R_P.'require/birth.php');
	}
}

// Sharing Information
extract($db->get_one("SELECT * FROM pw_bbsinfo WHERE id=1"));
//$rt = $db->get_one('SELECT newmember,totalmember,higholnum,higholtime,tdtcontrol,yposts,hposts,hit_tdtime,hit_control,plantime FROM pw_bbsinfo WHERE id=1');
$newmember = '<a href="u.php?action=show&username='.rawurlencode($newmember).'" target="_blank">'.$newmember.'</a>';

$article += $o_post;
$topics += $o_post;
$tposts += $o_tpost;

// online users
Update_ol();

$userinbbs = $guestinbbs = 0;
if (empty($db_online)) {
	include_once(D_P.'data/bbscache/olcache.php');
} else {
	$query = $db->query("SELECT uid!=0 as ifuser,COUNT(*) AS count FROM pw_online GROUP BY uid!='0'");
	while ($rt = $db->fetch_array($query)) {
		if ($rt['ifuser']) {
			$userinbbs = $rt['count'];
		} else {
			$guestinbbs = $rt['count'];
		}
	}
}
$usertotal = $guestinbbs + $userinbbs;
if ($db_indexonline) {
	InitGP(array('online'));
	empty($online) && $online = GetCookie('online');
	if ($online == 'yes') {
		if ($usertotal > 2000 && !CkInArray($windid,$manager)) {
			$online = 'no';
			Cookie('online',$online);
		} else {
			$index_whosonline = '';
			$db_online = intval($db_online);
			include_once Pcv(R_P."require/online_{$db_online}.php");
		}
	}
}
$showgroup = $db_showgroup ? explode(',',$db_showgroup) : array();

// Share union
if ($db_indexmqshare && $sharelink[1]) {
	$sharelink[1] = "<marquee scrolldelay=\"100\" scrollamount=\"4\" onmouseout=\"if (document.all!=null){this.start()}\" onmouseover=\"if (document.all!=null){this.stop()}\" behavior=\"alternate\">$sharelink[1]</marquee>";
}

//update pw_feed
if ($winduid && PwStrtoTime(get_date($lastvisit,'Y-m-d')) < $tdtime && $db_hostweb == 1 && !$cateid && $groupid != 'guest' && !defined('M_P')) {
	$_flag = $db->get_value("SELECT id FROM pw_feed WHERE uid = ".pwEscape($winduid)." ORDER BY id DESC LIMIT 29,1");
	$_flag && $db->update("DELETE FROM pw_feed WHERE uid = ".pwEscape($winduid)." AND id < ". pwEscape($_flag));
}

if ($db_hostweb == 1 && $groupid != 'guest' && !$cateid && $tdtcontrol < $tdtime && !defined('M_P')) {
	require_once(R_P.'require/updateforum.php');
	updateshortcut();
	$db->update("UPDATE pw_bbsinfo SET".pwSqlSingle(array('yposts' => $tposts, 'tdtcontrol' => $tdtime,'o_tpost'=>0))."WHERE id='1'");
	$db->update("UPDATE pw_forumdata SET tpost=0 WHERE tpost<>'0'");
//	$db->update("DELETE FROM pw_feed WHERE timestamp<".pwEscape($tdtime - 604800));
}

// update posts hits
if ($c_htm || $db_hithour) {
	$db_hithour == 0 && $db_hithour = 4;
	$hit_wtime = $hit_control * $db_hithour;
	$hit_wtime > 24 && $hit_wtime = 0;
	$hitsize = @filesize(D_P.'data/bbscache/hits.txt');
	if ($hitsize && ($hitsize > 1024 || ($timestamp - $hit_tdtime) > $hit_wtime * 3600) && procLock('hitupdate')) {
		require_once(R_P.'require/hitupdate.php');
		procUnLock('hitupdate');
	}
}

if ($higholnum < $usertotal) {
	$db->update("UPDATE pw_bbsinfo SET ".pwSqlSingle(array('higholnum' => $usertotal,'higholtime'=> $timestamp))." WHERE id=1");
	$higholnum = $usertotal;
}
if ($hposts < $tposts) {
	$db->update('UPDATE pw_bbsinfo SET hposts='.pwEscape($tposts).' WHERE id=1');
	$hposts = $tposts;
}
$mostinbbstime = get_date($higholtime);
if (!$ol_offset && $db_onlinelmt != 0 && $usertotal >= $db_onlinelmt) {
	Cookie('ol_offset','',0);
	Showmsg('most_online');
}
if ($plantime && $timestamp > $plantime && procLock('task')) {
	require_once(R_P.'require/task.php');
	procUnLock('task');
}
$db_newinfoifopen && require_once(R_P."require/newinfo.php");
require_once PrintEot('index');

if (isset($area_refresh_static) && $area_refresh_static && function_exists("area_static_deal")) {
	$area_static_content = ob_get_contents();
	area_static_deal("saveStaticContent", array('content'=>$area_static_content));
	ob_clean();
	echo $area_static_header, $area_static_content;
	unset($area_static_header, $area_static_content);
}
footer();
?>