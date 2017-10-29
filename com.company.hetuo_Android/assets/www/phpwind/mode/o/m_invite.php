<?php
!defined('M_P') && exit('Forbidden');
empty($o_invite) && Showmsg('mode_o_invite_close');

InitGP(array('u','hash','app'));
$u = (int)$u;
!$u && Showmsg('undefined_action');
$thisbase = $basename."q=invite&";

if($winduid) {
	if (getOneFriend($u)) {
		Showmsg('mode_o_is_friend');
	}
}

$friend = $db->get_one("SELECT m.uid,m.username,m.icon as face,m.honor,md.f_num,md.postnum,od.diarynum,od.photonum,od.owritenum,od.sharenum,od.groupnum FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid LEFT JOIN pw_ouserdata od ON m.uid=od.uid WHERE m.uid=".pwEscape($u));
!$friend && Showmsg('undefined_action');
if ($hash != appkey($u,$app)) {
	Showmsg('mode_o_invite_hash_error');
} elseif ($winduid) {
	$o_u = $u;
	if (is_numeric($o_u) && strlen($hash)==18) {
		require_once(R_P.'require/o_invite.php');
	}
	refreshto("u.php?uid=$u",'add_friend_success');
}else {
	Cookie('o_invite',"$u\t$hash\t$app");
}

require_once(R_P.'require/showimg.php');
list($friend['face']) = showfacedesign($friend['face'],1,'m');
if ($friend['f_num']) {
	$friends = getFriends($u,0,6,'',1);
}
$forward = rawurlencode("u.php?uid=$u");
require_once(M_P.'require/header.php');
require_once PrintEot('m_invite');
footer();
?>