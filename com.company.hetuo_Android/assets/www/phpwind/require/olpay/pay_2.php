<?php
!function_exists('readover') && exit('Forbidden');

$ret_url = 'index.php';

$f = $db->get_one("SELECT f.fid,f.name,f.forumsell,fe.forumset FROM pw_forums f LEFT JOIN pw_forumsextra fe USING(fid) WHERE f.fid=" . pwEscape($rt['paycredit']));

if ($f) {

	$date = $rt['extra_1'];
	$overdate = $timestamp + $date * 86400;
	$db->update("INSERT INTO pw_forumsell SET " . pwSqlSingle(array(
		'fid'		=> $f['fid'],
		'uid'		=> $rt['uid'],
		'buydate'	=> $timestamp,
		'overdate'	=> $overdate,
		'credit'	=> 'RMB',
		'cost'		=> $rt['price']
	),false));

	require_once(R_P.'require/msg.php');
	$message = array(
		'toUser'	=> $rt['username'],
		'subject'	=> 'forumbuy_title',
		'content'	=> 'forumbuy_content',
		'other'		=> array(
			'fee'		=> $fee,
			'fname'		=> $f['name'],
			'number'	=> $date
		)
	);
	pwSendMsg($message);
	$ret_url = 'thread.php?fid=' . $f['fid'];
}
?>