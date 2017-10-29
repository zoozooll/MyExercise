<?php

!defined('P_W') && exit('Forbidden');
//api mode 3

class Msg {
	
	var $base;
	var $db;

	function Msg($base) {
		$this->base = $base;
		$this->db = $base->db;
	}

	function send($uids, $fromUid, $subject, $content) {
		$fromUser = '';
		if ($fromUid) {
			$fromUser = $this->db->get_value("SELECT username FROM pw_members WHERE uid=" . pwEscape($fromUid));
		}
		!is_numeric($uids) && $uids = explode(',',$uids);
		$msg = array(
			'toUid'		=> $uids,
			'fromUid'	=> $fromUid,
			'fromUser'	=> $fromUser,
			'subject'	=> Char_cv(stripslashes($subject)),
			'content'	=> Char_cv(stripslashes($content))
		);
		require_once(R_P.'require/msg.php');
		pwSendMsg($msg);

		return new ApiResponse(true);
	}

	function SendAppmsg ($toname, $fromname, $subject, $content) {

		$msg = array(
			'toUser'	=> $toname,
			'fromUid'	=> '-1',
			'fromUser'	=> $fromname,
			'subject'	=> Char_cv(stripslashes($subject)),
			'content'	=> Char_cv(stripslashes($content))
		);

		require_once(R_P.'require/msg.php');
		pwSendMsg($msg);

		return new ApiResponse(true);
	}
}
?>