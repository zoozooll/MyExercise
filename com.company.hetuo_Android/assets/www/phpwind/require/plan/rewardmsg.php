<?php
!function_exists('readover') && exit('Forbidden');

include_once(D_P.'data/bbscache/forum_cache.php');

$query = $db->query("SELECT t.tid,t.fid,t.authorid,t.subject,t.postdate FROM pw_threads t LEFT JOIN pw_reward r USING(tid) WHERE t.special='3' AND t.state='0' AND r.timelimit<'$timestamp' ORDER BY t.postdate ASC LIMIT 100");
$tids = $uiddb = $msg_a = array();
while ($rt = $db->fetch_array($query)) {
	$rt['postdate']	  = get_date($rt['postdate']);
	$tids[$rt['tid']] = $rt;
}
$title	 = Char_cv(getLangInfo('writemsg','rewardmsg_notice_title'));
foreach ($tids as $tid => $msg) {
	$L = array(
		'tid'		=> $tid,
		'subject'	=> $msg['subject'],
		'postdate'	=> $msg['postdate'],
		'fid'		=> $msg['fid'],
		'name'		=> $forum[$msg['fid']]['name']
	);
	$content = Char_cv(getLangInfo('writemsg','rewardmsg_notice_content',$L));
	$msg_a[] = array($msg['authorid'],'0','SYSTEM','rebox','1',$timestamp,$title,$content);
}
if ($msg_a) {
	require_once(R_P.'require/msg.php');
	send_msgc($msg_a);
}
?>