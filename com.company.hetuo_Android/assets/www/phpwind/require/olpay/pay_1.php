<?php
!function_exists('readover') && exit('Forbidden');

$tool = $db->get_one("SELECT id,name FROM pw_tools WHERE id=" . pwEscape($rt['paycredit']));

if ($tool) {

	$db->update("UPDATE pw_tools SET stock=stock-" . pwEscape($rt['number']) . " WHERE id=" . pwEscape($tool['id']));

	$db->pw_update(
		"SELECT uid FROM pw_usertool WHERE uid=" . pwEscape($rt['uid']) . " AND toolid=" . pwEscape($tool['id']),
		"UPDATE pw_usertool SET nums=nums+" . pwEscape($rt['number']) . " WHERE uid=" . pwEscape($rt['uid']) . " AND toolid=" . pwEscape($tool['id']),
		"INSERT INTO pw_usertool SET " . pwSqlSingle(array('nums' => $rt['number'], 'uid' => $rt['uid'], 'toolid' => $tool['id']))
	);

	require_once(R_P.'require/tool.php');
	$logdata = array(
		'type'		=>	'buy',
		'nums'		=>	$nums,
		'money'		=>	$price,
		'descrip'	=>	'buy_descrip',
		'uid'		=>	$rt['uid'],
		'username'	=>	$rt['username'],
		'ip'		=>	$onlineip,
		'time'		=>	$timestamp,
		'toolname'	=>	$tool['name'],
		'from'		=>	'',
	);
	writetoollog($logdata);

	require_once(R_P.'require/msg.php');
	$message = array(
		'toUser'	=> $rt['username'],
		'subject'	=> 'toolbuy_title',
		'content'	=> 'toolbuy_content',
		'other'		=> array(
			'fee'		=> $fee,
			'toolname'	=> $tool['name'],
			'number'	=> $rt['number']
		)
	);
	pwSendMsg($message);
}

$ret_url = 'hack.php?H_name=toolcenter&action=mytool';
?>