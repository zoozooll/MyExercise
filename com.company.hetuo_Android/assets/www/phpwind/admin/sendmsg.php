<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename="$admin_file?adminjob=sendmsg";

if (empty($action)) {

	include PrintEot('sendmsg');exit;

} elseif ($action == "send") {

	$pwServer['REQUEST_METHOD']!='POST' && PostCheck($verify);
	InitGP(array('step','by','sendto','subject','atc_content'));

	if ($by == 1) {

		!$sendto && adminmsg('operate_error');
		if (empty($subject) || empty($atc_content)) {
			adminmsg('sendmsg_empty');
		}
		$subject     = Char_cv($subject);
		$sendmessage = Char_cv($atc_content);
		$sendto = implode(",",$sendto);
		$sqlwhere = "groupid IN('".str_replace(",","','",$sendto)."')";
		$db->update("INSERT INTO pw_msg"
			. " SET " . pwSqlSingle(array(
				'togroups'	=> ','.$sendto.',',
				'fromuid'	=> 0,
				'username'	=> 'SYSTEM',
				'type'		=> 'public',
				'ifnew'		=> 0,
				'mdate'		=> $timestamp
		)));
		$mid = $db->insert_id();
		$db->update("REPLACE INTO pw_msgc"
			. " SET " . pwSqlSingle(array(
				'mid'		=> $mid,
				'title'		=> $subject,
				'content'	=> $sendmessage
		)));
		$db->update("UPDATE pw_members SET newpm=newpm+1 WHERE $sqlwhere");
		adminmsg('operate_success');

	} elseif ($by == 2) {

		$cache_file = D_P."data/bbscache/".substr(md5($admin_pwd),10,10).".txt";
		if (!$step) {
			writeover($cache_file,$atc_content);
		} else {
			$atc_content = readover($cache_file);
		}
		if (empty($subject) || empty($atc_content)) {
			adminmsg('sendmsg_empty');
		}
		$subject     = Char_cv($subject);
		$sendmessage = Char_cv($atc_content);
		$percount = 100;
		empty($step) && $step = 1;

		require_once(R_P.'require/getonlineuser.php');
		$onlineuser = GetOnlineUser();
		$count = count($onlineuser);
		$uids = array();
		$start = ($step-1)*$percount;
		$uids = array_splice(array_keys($onlineuser),$start,$percount);
		if ($uids) {
			$uids = pwImplode($uids);

			$msg_a = array();
			$query = $db->query("SELECT uid,username,email,newpm FROM pw_members WHERE uid IN($uids)");
			while (@extract($db->fetch_array($query))) {
				$sendmessage = str_replace("\$email",$email,$atc_content);
				$sendmessage = str_replace("\$windid",$username,$sendmessage);
				$msg_a[] = array($uid,'0','System','rebox','1',$timestamp,$subject,$sendmessage);
			}
			//TODO 新消息提醒
			if ($msg_a) {
				require_once(R_P.'require/msg.php');
				send_msgc($msg_a);
			}
		}
		$havesend = $step*$percount;
		if ($count > ($step*$percount)) {
			$step++;
			$j_url = "$basename&action=$action&step=$step&subject=".rawurlencode($subject)."&by=$by";
			adminmsg("sendmsg_step",EncodeUrl($j_url),1);
		} else {
			P_unlink($cache_file);
			adminmsg('sendmsg_success');
		}
	} else {
		adminmsg('operate_error');
	}
}
?>