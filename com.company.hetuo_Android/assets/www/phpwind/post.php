<?php
define('SCR','post');
require_once('global.php');
require_once(R_P.'lib/forum.class.php');
require_once(R_P.'lib/post.class.php');
include_once(D_P.'data/bbscache/cache_post.php');

/**
* 版块缓冲文件
*/
empty($fid) && Showmsg('undefined_action');

$pwforum = new PwForum($fid);
$pwpost  = new PwPost($pwforum);
$pwpost->forumcheck();
$pwpost->postcheck();
list($uploadcredit,$uploadmoney,,) = explode("\t", $pwforum->forumset['uploadset']);

$foruminfo =& $pwforum->foruminfo;
$forumset =& $pwforum->forumset;

InitGP(array('action','article','pid','page'));
InitGP(array('special','modelid','pcid'),GP,2);
$replacedb = array();
$secondurl = "thread.php?fid=$fid";
!$action && $action = "new";
$replayorder_default = 'checked';
if ($action == 'new') {
	if ($modelid > 0) {/*主题分类*/
		require_once(R_P.'lib/posttopic.class.php');
		$postTopic = new postTopic($pwpost);
		if (!$_G['allowmodelid']) {
			Showmsg('post_allowpost');
		}
		if (strpos(",".$pwforum->foruminfo['modelid'].",",",".$modelid.",") === false) {
			Showmsg('forum_model_unfined');
		}
		if (!$postTopic->topiccatedb[$postTopic->topicmodeldb[$modelid]['cateid']]['ifable']) {
			Showmsg('topic_cate_unable');
		}
		!$postTopic->topicmodeldb[$modelid]['ifable'] && Showmsg('topic_model_unable');
		$special = $pcid = 0;
	} elseif ($pcid > 0) {/*团购活动*/////the app to do

		require_once(R_P.'lib/postcate.class.php');
		$postCate = new postCate($pwpost);
		if (strpos(",".$pwforum->foruminfo['pcid'].",",",".$pcid.",") === false) {
			Showmsg('post_allowtype');
		}
		if (!$postCate->postcatedb[$pcid]['ifable']) {
			Showmsg('forum_pc_unfined');
		}
		if (strpos(",".$_G['allowpcid'].",",",".$pcid.",") === false) {
			Showmsg('post_allowpost');
		}
		$special = $modelid = 0;
	} elseif (!($pwforum->foruminfo['allowtype'] & pow(2,$special))) {
		$modelid = $pcid = 0;
		if (empty($special) && $pwforum->foruminfo['allowtype'] > 0) {
			$special = (int)log($pwforum->foruminfo['allowtype'],2);
		} elseif ($pwforum->foruminfo['modelid'] || $pwforum->foruminfo['pcid']) {
			require_once(R_P.'lib/posttopic.class.php');
			$postTopic = new postTopic($pwpost);
			$modeliddb = explode(",",$pwforum->foruminfo['modelid']);
		
			foreach ($modeliddb as $value) {
				if ($postTopic->topiccatedb[$postTopic->topicmodeldb[$value]['cateid']]['ifable'] && $_G['allowmodelid'] && $postTopic->topicmodeldb[$value]['ifable']) {
					$modelid = $value;
					break;
				}
			}

			if (!$modelid) {
				require_once(R_P.'lib/postcate.class.php');
				$postCate = new postCate($pwpost);
				$pciddb = explode(",",$pwforum->foruminfo['pcid']);
			
				foreach ($pciddb as $value) {
					if ($postCate->postcatedb[$value]['ifable'] && strpos(",".$_G['allowpcid'].",",",".$value.",") !== false) {
						$pcid = $value;
						break;
					}
				}
				if (!$pcid) {
					Showmsg('post_allowtype');
				}
			}
		} else {
			Showmsg('post_allowtype');
		}

	}
}

/**
* 禁止受限制用户发言
*/
if ($groupid == 6 || getstatus($winddb['userstatus'],1)) {
	$pwSQL = '';
	$flag  = 0;
	$bandb = $delban = array();
	$query = $db->query("SELECT * FROM pw_banuser WHERE uid=".pwEscape($winduid));
	while ($rt = $db->fetch_array($query)) {
		if ($rt['type'] == 1 && $timestamp - $rt['startdate'] > $rt['days']*86400) {
			$delban[] = $rt['id'];
		} elseif ($rt['fid'] == 0 || $rt['fid'] == $fid) {
			$bandb[$rt['fid']] = $rt;
		} else {
			$flag = 1;
		}
	}
	$delban && $db->update('DELETE FROM pw_banuser WHERE id IN('.pwImplode($delban).')');
	($groupid == 6 && !isset($bandb[0])) && $pwSQL .= "groupid='-1',";
	if (getstatus($winddb['userstatus'],1) && !isset($bandb[$fid]) && !$flag) {
		$pwSQL .= 'userstatus=userstatus&(~1),';
	}
	if ($pwSQL = rtrim($pwSQL,',')) {
		$db->update("UPDATE pw_members SET $pwSQL WHERE uid=".pwEscape($winduid));
	}
	if ($bandb) {
		$bandb = current($bandb);
		if ($bandb['type'] == 1) {
			$s_date = get_date($bandb['startdate']);
			$e_date = $bandb['startdate'] + $bandb['days']*86400;
			$e_date = get_date($e_date);
			Showmsg('ban_info1');
		} else {
			if ($bandb['type'] == 3) {
				Cookie('force',$winduid);
				Showmsg('ban_info3');
			} else {
				Showmsg('ban_info2');
			}
		}
	}
}
if (GetCookie('force') && $winduid != GetCookie('force')) {
	$force = GetCookie('force');
	$bandb = $db->get_one("SELECT type FROM pw_banuser WHERE uid=".pwEscape($force)." AND fid='0'");
	if ($bandb['type'] == 3) {
		Showmsg('ban_info3');
	} else {
		Cookie('force','',0);
	}
}

$userlastptime = $groupid != 'guest' ?  $winddb['lastpost'] : GetCookie('userlastptime');
/**
* 灌水预防
*/
$tdtime  >= $winddb['lastpost'] && $winddb['todaypost'] = 0;
$montime >= $winddb['lastpost'] && $winddb['monthpost'] = 0;
if ($_G['postlimit'] && $winddb['todaypost'] >= $_G['postlimit']) {
	Showmsg('post_gp_limit');
}
if ($action != "modify" && !$pwpost->isGM && $_G['postpertime'] && $timestamp>=$userlastptime && $timestamp-$userlastptime<=$_G['postpertime'] && !pwRights($pwpost->isBM,'postpers')) {
	Showmsg('post_limit');
}
list(,,$postq) = explode("\t", $db_qcheck);
$_G['uploadtype'] && $db_uploadfiletype = $_G['uploadtype'];
$db_uploadfiletype = !empty($db_uploadfiletype) ? (is_array($db_uploadfiletype) ? $db_uploadfiletype : unserialize($db_uploadfiletype)) : array();
empty($db_sellset['type']) && $db_sellset['type'] = array('money');
empty($db_enhideset['type']) && $db_enhideset['type'] = array('rvrc');

if (empty($_POST['step'])) {

	require_once(R_P.'require/credit.php');
	$editor = getstatus($winddb['userstatus'],11) ? 'wysiwyg' : 'textmode';
	!is_numeric($db_attachnum) && $db_attachnum = 1;
	$htmlsell = ($pwforum->foruminfo['allowsell'] && $_G['allowsell']) ? '' : 'disabled';
	$htmlhide = ($pwforum->forumset['allowencode'] && $_G['allowencode']) ? '' : 'disabled';
	$htmlpost = $htmlatt = ($pwforum->foruminfo['allowhide'] && $_G['allowhidden']) ? '' : 'disabled';
	$ifanonymous= ($pwpost->isGM || $pwforum->forumset['anonymous'] && $_G['anonymous']) ? '' : 'disabled';
	$groupid   == 'guest' && $userrvrc = 0;
	$atc_title  = $atc_content = $ifmailck = $selltype = $enhidetype = $alltype = '';
	$uploadfiletype = $uploadfilesize = ' ';
	foreach ($db_uploadfiletype as $key => $value) {
		$uploadfiletype .= $key.' ';
		$uploadfilesize .= $key.':'.$value.'KB; ';
	}
	foreach ($credit->cType as $key => $value) {
		$alltype .= "<option value=\"$key\">".$value."</option>";
	}
	foreach ($db_sellset['type'] as $key => $value) {
		$selltype .= "<option value=\"$value\">".$credit->cType[$value]."</option>";
	}
	if(is_array($db_enhideset['type'])){
		foreach ($db_enhideset['type'] as $key => $value) {
				$enhidetype .= "<option value=\"$value\">".$credit->cType[$value]."</option>";
		}
	}
	require_once(R_P.'require/showimg.php');
	list($postFaceUrl) = showfacedesign($winddb['icon'],1,'m');
	/**
	* 标题表情
	*/
	$icondb = array(
		'1'=>'1.gif',	'2'=>'2.gif',
		'3'=>'3.gif',	'4'=>'4.gif',
		'5'=>'5.gif',	'6'=>'6.gif',
		'7'=>'7.gif',	'8'=>'8.gif'
	);
	if ($db_allowupload && $_G['allowupload']) {
		$mutiupload = $db->get_value("SELECT COUNT(*) AS sum FROM pw_attachs WHERE tid=0 AND pid='0' AND uid=" . pwEscape($winduid));
	}

} else {

	PostCheck(1, ($db_gdcheck & 4) && $winddb['postnum'] < $db_postgd, $winddb['postnum'] < $postq);
	!$windid && $windid = '游客';
	/*
	if ($db_xforwardip && $_POST['_hexie'] != GetVerify($onlineip.$winddb['regdate'].$fid.$tid)) {
		Showmsg('undefined_action');
	}
	*/
}
//默认动漫表情处理
if ($db_windmagic && ($action == 'new' || ($action == 'modify' && $pid == 'tpc'))) {
	$mDef = '';
	@include_once(D_P."data/bbscache/myshow_default.php");
}
if ($action == "new") {
	require_once(R_P.'require/postnew.php');
} elseif ($action == "reply" || $action == "quote") {
	require_once(R_P.'require/postreply.php');
} elseif ($action == "modify") {
	require_once(R_P.'require/postmodify.php');
} else {
	Showmsg('undefined_action');
}
?>