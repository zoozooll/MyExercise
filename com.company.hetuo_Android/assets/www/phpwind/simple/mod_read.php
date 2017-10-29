<?php
!function_exists('readover') && exit('Forbidden');

require_once(R_P.'require/forum.php');
require_once(R_P.'require/bbscode.php');

(!is_numeric($page) || $page < 1) && $page = 1;
if ($page > 1) {
	$S_sql = $J_sql = '';
} else {
	!$page && $page = 1;
	$start_limit = 0;
	$pw_tmsgs = GetTtable($tid);
	$S_sql = ',tm.*';
	$J_sql = "LEFT JOIN $pw_tmsgs tm ON t.tid=tm.tid";
}
$read = $db->get_one("SELECT t.*,m.uid,m.groupid,m.userstatus $S_sql FROM pw_threads t LEFT JOIN pw_members m ON t.authorid=m.uid $J_sql WHERE t.tid=".pwEscape($tid));
!$read && Showmsg('illegal_tid');
$fid = $read['fid'];

if (!($foruminfo = L::forum($fid))) {
	Showmsg('data_error');
}
wind_forumcheck($foruminfo);

if (!$foruminfo['allowvisit'] && $_G['allowread'] == 0 && $_COOKIE) {
	Showmsg('read_group_right');
}

$subject = $read['subject'];
$forumname = strip_tags($foruminfo['name']);
$forumset  = $foruminfo['forumset'];
$pw_posts = GetPtable($read['ptable']);
$openIndex 	= getstatus($read['tpcstatus'], 2);	#是否开启高楼索引

//SEO setting
if (!$pw_seoset) {
	$pw_seoset = L::loadClass('seoset');
}
$pw_seoset->set_page('read');
$ifConvert = $read['ifwordsfb'] != $db_wordsfb ? true : false;
$pw_seoset->set_summary($read['content'],$ifConvert);
if ($groupid == 'guest' && !$read['ifshield'] && !isban($read,$fid)) {
	$pw_seoset->set_default($pw_seoset->get_summary().' '.$db_bbsname);
}else{
	$pw_seoset->set_tags($read['tags']);
	$pw_seoset->set_types(null,$read['type']);
}
$webPageTitle = $pw_seoset->getPageTitle('',$foruminfo['name'],$read['subject']);
$metaDescription = $pw_seoset->getPageMetadescrip('',$foruminfo['name'],$read['subject']);
$metaKeywords = $pw_seoset->getPageMetakeyword('',$foruminfo['name'],$read['subject']);


$isGM = CkInArray($windid,$manager);
$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
if ($windid && ($isGM || $isBM)) {
	$admincheck = 1;
} else {
	$admincheck = 0;
}
if ($foruminfo['allowread'] && !$admincheck && !allowcheck($foruminfo['allowread'],$groupid,$winddb['groups'])) {
	Showmsg('forum_read_right');
}
if (!$admincheck && !$foruminfo['allowvisit']) {
	forum_creditcheck();
}
if (!$admincheck && $foruminfo['forumsell']) {
	forum_sell($fid);
}
if ($read['ifcheck']==0 && !$isGM && $windid!=$read['author'] && !pwRights($isBM,'viewcheck')) {
	Showmsg('read_check');
}
if ($read['locked']%3==2 && !$isGM && !pwRights($isBM,'viewclose')) {
	Showmsg('read_locked');
}

$db_metakeyword = substr($read['tags'],0,strpos($read['tags'],"\t"));
$db_metakeyword = (empty($db_metakeyword) ? $subject : $db_metakeyword).','.$forumtitle;
$db_metakeyword = trim(str_replace(array('|',' - ',"\t",' ',',,,',',,'),',',$db_metakeyword),',');
if ($groupid == 'guest' && !$read['ifshield'] && !isban($read,$fid)) {
	if ($read['ifconvert'] == 2) {
		$metadescrip = stripWindCode($read['content']);
		$metadescrip = strip_tags($metadescrip);
	} else {
		$metadescrip = strip_tags($read['content']);
	}
	$metadescrip = str_replace(array('"',"\n","\r",'&nbsp;','&amp;','&lt;','','&#160;'),'',$metadescrip);
	$metadescrip = substrs($metadescrip,255,false);
	if ($read['ifwordsfb'] != $db_wordsfb) {
		//$metadescrip = wordsfb($metadescrip,$read['ifwordsfb']);
		$wordsfb = L::loadClass('FilterUtil');
		$metadescrip = $wordsfb->convert($metadescrip);
	}
	if (trim($metadescrip)) {
		$db_metadescrip = $metadescrip;
	}
	unset($metadescrip,$tmpAllow);
}
$db_metadescrip = $db_bbsname.','.$db_metadescrip;

$db_readperpage = 50;/*perpage*/

#高楼索引支持
if ($openIndex) {
	$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_postsfloor WHERE tid =". pwEscape($tid))." LIMIT 1";
}else{
	$count = $read['replies'] + 1;
}

if ($count % $db_readperpage == 0) {
	$numofpage = $count/$db_readperpage;
} else {
	$numofpage = floor($count/$db_readperpage) + 1;
}
if ($page > $numofpage) {
	$page = $numofpage;
}

Update_ol();
$readdb = array();

if ($page == 1) {
	$readdb[] = $read;
}
$pages = PageDiv($count,$page,$numofpage,"{$DIR}t$tid");

if (!$db_hithour) {
	$db->update("UPDATE pw_threads SET hits=hits+1 WHERE tid=".pwEscape($tid));
} else {
	writeover(D_P."data/bbscache/hits.txt",$tid."\t",'ab');
}

if ($read['replies'] > 0) {
	if ($openIndex) {
		$start_limit = (int)($page-1)*$db_readperpage-1;
		$start_limit < 0 && $start_limit = 0;
		$end = $start_limit + $db_readperpage;
		$sql_floor = " AND f.floor > " . $start_limit ." AND f.floor <= ".$end." ";
		$query = $db->query("SELECT f.pid FROM pw_postsfloor f WHERE f.tid = ". pwEscape($tid) ." $sql_floor ORDER BY f.floor");
		while ($rt = $db->fetch_array($query)) {
			$postIds[] = $rt['pid'];
		}
		if ($postIds) {
			$postIds && $sql_postId = " AND p.pid IN ( ". pwImplode($postIds,false) ." ) ";
			$query = $db->query("SELECT p.*,m.uid,m.groupid,m.userstatus 
				FROM $pw_posts p LEFT JOIN pw_members m ON p.authorid=m.uid 
				WHERE p.tid=".pwEscape($tid)." $sql_postId ORDER BY p.postdate");
			while ($read = $db->fetch_array($query)) {
				if ($read['ifcheck']!='1') {
					$read['subject'] = '';
					$read['content'] = getLangInfo('bbscode','post_unchecked');
				}
				$currentPostsId[] = $read['pid'];
				$currentPosts[$read['pid']] = $read;
			}
			foreach ($postIds as $key => $value) {
				if (in_array($value,$currentPostsId)) {
					$readdb[] = $currentPosts[$value];
				}else{
					$readdb[] = array('postdate'=>'N','content'=>getLangInfo('bbscode','post_deleted'));
				}
			}
		}
	}else{
		$start_limit = ($page - 1) * $db_readperpage;
		if ($page == 1) {
			$readnum = $db_readperpage-1;
		} else{
			$readnum = $db_readperpage;
			$start_limit--;
		}
		$query = $db->query("SELECT p.*,m.uid,m.groupid,m.userstatus FROM $pw_posts p LEFT JOIN pw_members m ON p.authorid=m.uid WHERE p.tid=".pwEscape($tid)." AND p.ifcheck='1' ORDER BY p.postdate".pwLimit($start_limit,$readnum));
	
		while ($read = $db->fetch_array($query)) {
			$readdb[] = $read;
		}
	}
	$db->free_result($query);
}
$bandb = isban($readdb,$fid);
$start_limit = ($page - 1) * $db_readperpage;
foreach ($readdb as $key => $read) {
	isset($bandb[$read['authorid']]) && $read['groupid'] = 6;
	$readdb[$key] = viewread($read,$start_limit++);
}

function viewread($read,$start_limit) {
	global $groupid,$admincheck,$attach_url,$attachper,$winduid,$tablecolor,$tpc_author,$tpc_buy,$count,$timestamp,$db_onlinetime,$attachpath,$_G,$readcolorone,$readcolortwo,$lpic,$ltitle,$imgpath,$db_ipfrom,$db_showonline,$stylepath,$db_windpost,$db_windpic,$db_signwindcode,$fid,$tid,$pid,$pic_a,$db_shield,$db_anonymousname;
	$tpc_buy = $read['buy'];
	$tpc_author = $read['author'];
	$read['ifsign'] < 2 && $read['content'] = str_replace("\n","<br>",$read['content']);

	$read['postdate'] = get_date($read['postdate']);

	if ($read['ifshield']) {
		$read['subject'] = $groupid == '3' ? shield($read['ifshield'] == '2' ? 'shield_del_title' : 'shield_title') : '';
		$groupid != '3' && $read['content'] = shield($read['ifshield'] == '1' ? 'shield_article' : 'shield_del_article');
	} elseif ($read['groupid'] == 6 && $groupid != 3 && $db_shield) {
		$read['subject'] = '';
		$read['content'] = shield('ban_article');
	} else {
		$wordsfb = L::loadClass('FilterUtil');
		if (!$wordsfb->equal($read['ifwordsfb'])) {
			$read['content'] = $wordsfb->convert($read['content']);
		}
		$read['ifconvert'] == 2 && $read['content'] = convert($read['content'],$db_windpost);
	}
	$GLOBALS['foruminfo']['copyctrl'] && $read['content'] = preg_replace("/<br>/eis","copyctrl('#FFFFFF')",$read['content']);
	/**
	* convert the post content
	*/
	//$read['content']=stripslashes($read['content']);
	$read['anonymous'] && !$admincheck && $winduid!=$read['authorid'] && $read['author']=$db_anonymousname;
	return $read;
}

require_once PrintEot('simple_header');

require_once PrintEot('simple_read');
?>