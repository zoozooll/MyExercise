<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=present";

require_once(R_P."require/credit.php");

if (empty($action)) {

	include PrintEot('present');exit;

} elseif ($action == "send") {

	$pwServer['REQUEST_METHOD']!='POST' && PostCheck($verify);
	InitGP(array('step','by','sendto','touser','subject','atc_content','present','percount','count'));
	$cache_file = D_P."data/bbscache/".substr($admin_pwd,10,10).".txt";
	if (!$step) {
		writeover($cache_file,$atc_content);
	} else {
		$atc_content = readover($cache_file);
	}
	if (empty($subject) || empty($atc_content)) {
		adminmsg('sendmsg_empty');
	}
	$sendmessage = $atc_content;
	!$percount && $percount = 100;
	empty($step) && $step = 1;
	$start = ($step-1)*$percount;
	$limit = pwLimit($start,$percount);

	$creditlist = '';
	$sendmessage .= '<br /><br /><b>'.getLangInfo('other','affect').'</b>';

	foreach ($present as $key => $val) {
		if (empty($val)) continue;
		if (is_numeric($val)) {
			$creditlist .= "&present[$key]=$val";
			$sendmessage .= $credit->cType[$key]."<font color=#FA891B>(+$val)</font> ";
		} else {
			adminmsg('credit_isnum');
		}
	}

	if ($by == 0) {
		!$sendto && adminmsg('operate_error');
		is_array($sendto) && $sendto = implode(",",$sendto);
		$sendto && $sqlwhere = "groupid IN('".str_replace(",","','",$sendto)."')";
		if ($step == 1 && $sqlwhere) {
			$rs = $db->get_one("SELECT count(*) AS count FROM pw_members WHERE $sqlwhere");
			$count = $rs['count'];
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
		}
	} elseif ($by == 1) {
		require_once(R_P.'require/getonlineuser.php');
		$onlineuser = GetOnlineUser();
		$uids = array();
		foreach ($onlineuser as $key => $value) {
			is_numeric($key) && $uids[] =  $key;
		}
		$uids = pwImplode($uids);
		$uids && $sqlwhere = "uid IN($uids)";
		$count = count($onlineuser);
	} elseif ($by == 2) {
		!$touser && adminmsg('operate_error');
		$to_a = explode(',', $touser);
		$to_a && $sqlwhere = "username IN(" . pwImplode($to_a) . ")";
		$count = count($to_a);
	} else {
		adminmsg('operate_error');
	}

	$pruids = $msg_a = $u_a = array();
	if($sqlwhere){
		$query = $db->query("SELECT uid,username,email,newpm FROM pw_members WHERE $sqlwhere $limit");
		while ($rt = $db->fetch_array($query)) {
			$u_a[] = $rt['uid'];
	
			if ($by > 0) {
				$msg_a[] = array($rt['uid'],'0','System','rebox','1',$timestamp,$subject,$sendmessage);
			} else {
				$pruids[] = $rt['uid'];
			}
			$credit->addLog('other_present',$present,array(
				'uid'		=> $rt['uid'],
				'username'	=> $rt['username'],
				'ip'		=> $onlineip,
				'admin'		=> $admin_name
			));
		}
	}
	if ($msg_a) {
		require_once(R_P.'require/msg.php');
		send_msgc($msg_a);
	}
	$pruids && $db->update("UPDATE pw_members SET newpm=newpm+1 WHERE uid IN(".pwImplode($pruids).")");
	if ($u_a) {
		$credit->setus($u_a,$present);
	}
	$havesend = $step*$percount;
	if ($count > $step*$percount) {
		$step++;
		$j_url = "$basename&action=$action&step=$step&count=$count&sendto=$sendto&touser=" . rawurlencode($touser) . "&subject=". rawurlencode($subject)."&by=$by&percount=$percount$creditlist";
		adminmsg("sendmsg_step",EncodeUrl($j_url),1);
	} else {
		P_unlink($cache_file);
		adminmsg('sendmsg_success');
	}
}
?>