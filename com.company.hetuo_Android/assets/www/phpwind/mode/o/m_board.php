<?php
!defined('M_P') && exit('Forbidden');
if(!$winduid) ObHeader('index.php?m=o');
InitGP(array('u','page','space'),'',2);
$space = 1;
!$u && $u = $winduid;

$basename .= 'space=1&';
$spaceurl = $basename."u=$u&";

require_once(R_P.'require/showimg.php');
$isGM = CkInArray($windid,$manager);
!$isGM && $groupid==3 && $isGM=1;

$userdb = $db->get_one("SELECT m.uid,m.username,o.index_privacy FROM pw_members m LEFT JOIN pw_ouserdata o ON m.uid=o.uid WHERE m.uid=".pwEscape($u));
list($isU,$privacy) = pwUserPrivacy($u,$userdb);
if ($groupid == 3 || $isU == 2 || $isU !=2 && $privacy['index']) {
	$SpaceShow = 1;
}
if (!$SpaceShow) Showmsg('mode_o_index_right');

if ($u != $winduid) {
	$username	= $userdb['username'];
} else {
	$username	= $windid;
}

$uid = isset($userdb['uid']) ? $userdb['uid'] : $winddb['uid'];

$count	= $db->get_value("SELECT COUNT(*) AS count FROM pw_oboard WHERE touid=".pwEscape($u));
list($pages,$limit) = pwLimitPages($count,$page,$basename."q=board&u=".$u."&");

$boards = array();
require_once(R_P.'require/bbscode.php');
$wordsfb = L::loadClass('FilterUtil');
$query = $db->query("SELECT o.*,m.icon as face FROM pw_oboard o LEFT JOIN pw_members m ON o.uid=m.uid WHERE o.touid=".pwEscape($u)." ORDER BY o.id DESC $limit");
while ($rt = $db->fetch_array($query)) {
	$rt['postdate']	= get_date($rt['postdate']);
	list($rt['face'])	=  showfacedesign($rt['face'],1,'m');
	if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
		$rt['title'] = appShield('ban_feed');
	} elseif (!$wordsfb->equal($rt['ifwordsfb'])) {
		$rt['title'] = $wordsfb->convert($rt['title'], array(
			'id'	=> $rt['id'],
			'type'	=> 'comments',
			'code'	=> $rt['ifwordsfb']
		));
	}
	if (strpos($rt['title'],'[s:') !== false) {
		$rt['title'] = showface($rt['title']);
	}
	if (strpos($rt['title'],'[url') !== false) {
		$rt['title'] = convert($rt['title'],$db_windpost);
	}
	$boardids[] = $rt['id'];
	$boards[] = $rt;
}
if (!empty($boardids)) {
	$commentdb = getCommentDb('board',$boardids);
}

require_once(M_P.'require/header.php');
require_once(R_P.'require/credit.php');
list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);

require_once PrintEot('m_board');

footer();
?>