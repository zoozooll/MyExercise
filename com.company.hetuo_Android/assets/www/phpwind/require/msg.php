<?php
!function_exists('readover') && exit('Forbidden');

/**
 * 发送社区短消息或系统通知
 *
 * @param array $msg 信息格式如下:
 * 	$msg = array(
 *		'toUser'	=> 'admin', //接收者用户名,可为数组群发:array('admin','abc')
 *		'toUid'		=> 1,		//接收者uid,可为数组群发:array(1,2),当与 toUser 同时存在时，自然失效
 *		'fromUid'	=> 2,		//发送者UID,与fromUser同时存在才有效 (可选,默认为'0')
 *		'fromUser'	=> 'pwtest',//发送者用户名,与fromUid同时存在才有效(可选,默认为'SYSTEM')
 *		'subject'	=> 'Test',	//消息标题
 *		'content'	=> '~KO~',	//消息内容
 *		'other'		=> array()	//其他信息变量
 *	);
 * @return boolean 返回消息发送是否完成
 */
function pwSendMsg($msg) {
	global $db,$timestamp;
	if ((!$msg['toUser'] && !$msg['toUid']) || !$msg['subject'] || !$msg['content']) {
		return false;
	}
	$toType = 'username';
	if (empty($msg['toUser'])) {
		$msg['toUser'] = $msg['toUid'];
		$toType = 'uid';
	}
	$msg['subject'] = getLangInfo('writemsg',$msg['subject'],$msg);
	$msg['content'] = getLangInfo('writemsg',$msg['content'],$msg);

	if (!$msg['fromUid'] || !$msg['fromUser']) {
		$msg['fromUid']		= 0;
		$msg['fromUser']	= 'SYSTEM';
	}
	if (is_array($msg['toUser'])) {//group send message
		$msgdb = array();
		$query = $db->query("SELECT uid FROM pw_members WHERE $toType IN (".pwImplode($msg['toUser'],false).')');
		while ($rt = $db->fetch_array($query)) {
			$msgdb[] = array($rt['uid'], $msg['fromUid'], $msg['fromUser'], 'rebox', 1, $timestamp, $msg['subject'], $msg['content']);
		}
		$msgdb && send_msgc($msgdb,false);
	} else {
		$rt = $db->get_one("SELECT uid FROM pw_members WHERE $toType=".pwEscape($msg['toUser'],false));
		if (empty($rt)) return false;

		$db->update('INSERT INTO pw_msg SET '.pwSqlSingle(array(
			'touid'		=> $rt['uid'],
			'fromuid'	=> $msg['fromUid'],
			'username'	=> $msg['fromUser'],
			'type'		=> 'rebox',
			'ifnew'		=> 1,
			'mdate'		=> $timestamp
		),false));

		$mid = $db->insert_id();
		$db->update('REPLACE INTO pw_msgc SET '.pwSqlSingle(array(
			'mid'		=> $mid,
			'title'		=> $msg['subject'],
			'content'	=> $msg['content']
		), false));

		$db->update("UPDATE pw_members SET newpm=newpm+1 WHERE uid=".pwEscape($rt['uid'],false));
	}
	return true;
}

function delete_msgc($ids = null) {
	global $db;
	if ($db->server_info() > '4') {
		$GLOBALS['db']->update("DELETE ".($db->server_info() > '4.1' ? 'mc' : 'pw_msgc')." FROM pw_msgc mc LEFT JOIN pw_msg m ON mc.mid=m.mid LEFT JOIN pw_msglog ml ON mc.mid=ml.mid WHERE m.mid is NULL AND ml.mid is NULL".($ids ? " AND mc.mid IN($ids)" : ''));
	} else {
		$delids = array();
		$query  = $db->query("SELECT mc.mid FROM pw_msgc mc LEFT JOIN pw_msg m ON mc.mid=m.mid LEFT JOIN pw_msglog ml ON mc.mid=ml.mid WHERE m.mid is NULL AND ml.mid is NULL".($ids ? " AND mc.mid IN($ids)" : ''));
		while ($rt = $db->fetch_array($query)) {
			$delids[] = $rt['mid'];
		}
		//TODO SQL太长
		!empty($delids) && $db->update("DELETE FROM pw_msgc WHERE mid IN(".pwImplode($delids).")");
	}
}

function send_msgc($msg,$isNotify=true) {
	global $db;
	if (!is_array($msg)) return;

	$uid = $sql = $mc_sql = array();
	foreach ($msg as $k => $v) {
		if (is_array($v)) {
			$sql[] = array($v[0],$v[1],$v[2],$v[3],$v[4],$v[5]);
			$uid[] = $v[0];
		}
	}
	if ($sql) {
		$db->update("INSERT INTO pw_msg(touid,fromuid,username,type,ifnew,mdate) VALUES ".pwSqlMulti($sql,false));
		$mid = $db->insert_id();
		foreach ($msg as $k => $v) {
			if (is_array($v)) {
				$mc_sql[] = array($mid++,$v[6],$v[7]);
			}
		}
		if ($mc_sql) {
			$db->update("REPLACE INTO pw_msgc(mid,title,content) VALUES ".pwSqlMulti($mc_sql,false));
		}
		updateNewpm($uid,'add');
	}
}
?>