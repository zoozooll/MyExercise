<?php
!function_exists('readover') && exit('Forbidden');

include_once(D_P . 'data/bbscache/level.php');
list($m_faceurl) = showfacedesign($winddb['icon'], 1, 'm');

$uinfo = $db->get_one("SELECT m.bday,m.location,m.site,m.introduce,o.index_privacy FROM pw_members m LEFT JOIN pw_ouserdata o ON m.uid=o.uid WHERE m.uid=" . pwEscape($winduid));
$winddb += $uinfo;

$regdate = get_date($winddb['regdate'], 'Y-m-d');
$lastvisit = get_date($winddb['lastvisit'], 'Y-m-d');
$onlinetime = floor($winddb['onlinetime'] / 3600);
$winddb['lastpost'] < $tdtime && $winddb['todaypost'] = 0;
$averagepost = round($winddb['postnum'] / (ceil(($timestamp - $winddb['regdate'])/(3600*24))));
$friendcheck = getstatus($winddb['userstatus'], 3, 3);

//$newmsg = array();
//$query = $db->query("SELECT * FROM pw_msg WHERE ");

$msgdb = array();
$query = $db->query("SELECT m.mid,m.fromuid,m.type,m.username as `from`,m.mdate,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='rebox' AND m.touid=" . pwEscape($winduid) . " AND ifnew=1 ORDER BY m.mdate DESC ".pwLimit(0,5));
while ($msginfo = $db->fetch_array($query)) {
	$msginfo['title'] = substrs($msginfo['title'],40);
	$msgdb[] = $msginfo;
}
//群发消息
$pubmsg = getUserPublicMsgRecord($winduid);
@extract($pubmsg);
$checkmsg = $readmsg.','.$delmsg;
$msg_gid = $winddb['groupid'];
$query2  = $db->query("SELECT m.mid,m.fromuid,m.type,m.username as `from`,m.mdate,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='public' AND m.togroups LIKE ".pwEscape("%,$msg_gid,%")." AND m.mdate>".pwEscape($winddb['regdate'])." ORDER BY m.mdate DESC LIMIT 5");
while ($msginfo = $db->fetch_array($query2)) {
	if ($checkmsg && strpos(",$checkmsg,",",$msginfo[mid],") !== false) {
		continue;
	}
	$msginfo['title'] = substrs($msginfo['title'],40);
	$msgdb[] = $msginfo;
}
if (empty($msgdb) && $winddb['newpm'] > 0) {
	$db->update("UPDATE pw_members SET newpm='0' WHERE uid=".pwEscape($winduid));
}
usort ($msgdb, "sort_cmp");
if (count($msgdb) > 5) {
	$msgdb = array_slice($msgdb, 0, 5);
}

require_once(R_P . 'require/credit.php');
$usercredit = $credit->get($winduid);

$creditdb = $usercredit + array(
	'postnum'	=> $winddb['postnum'],
	'digests'	=> $winddb['digests'],
	'onlinetime'=> $winddb['onlinetime']
);
$creditdb['rvrc'] *= 10;

$upgradeset  = unserialize($db_upgrade);
$totalcredit = CalculateCredit($creditdb, $upgradeset);

require_once PrintEot('profile_index');
footer();

function sort_cmp($a, $b) {
	if ($a['mdate'] == $b['mdate']) return 0;
    return ($a['mdate'] > $b['mdate']) ? -1 : 1;
}
?>