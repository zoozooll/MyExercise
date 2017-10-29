<?php
!function_exists('readover') && exit('Forbidden');

$rmbrate = $db_creditpay[$rt['paycredit']]['rmbrate'];
!$rmbrate && $rmbrate = 10;
$currency = round($rt['price'] * $rmbrate);

require_once(R_P.'require/credit.php');
$credit->addLog('main_olpay',array($rt['paycredit'] => $currency),array(
	'uid'		=> $rt['uid'],
	'username'	=> $rt['username'],
	'ip'		=> $onlineip,
	'number'	=> $rt['price']
));
$credit->set($rt['uid'],$rt['paycredit'],$currency);

require_once(R_P.'require/msg.php');
$message = array(
	'toUser'	=> $rt['username'],
	'subject'	=> 'olpay_title',
	'content'	=> 'olpay_content_2',
	'other'		=> array(
		'currency'	=> $currency,
		'cname'		=> $credit->cType[$rt['paycredit']],
		'number'	=> $rt['price']
	)
);
pwSendMsg($message);
?>