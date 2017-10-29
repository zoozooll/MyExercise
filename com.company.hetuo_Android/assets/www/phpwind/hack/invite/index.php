<?php
!function_exists('readover') && exit('Forbidden');
$wind_in = 'invite';

include_once(D_P."data/bbscache/inv_config.php");

$inv_open!='1' && Showmsg('inv_close');

InitGP(array('action'));

if (!$windid && !in_array($action,array('pay','alipay'))) {
	Showmsg('not_login');
}

$usrecredit = ${'db_'.$inv_credit.'name'};
$creditto = array(
	'rvrc'    => $userrvrc,
	'money'   => $winddb['money'],
	'credit'  => $winddb['credit'],
	'currency'=> $winddb['currency']
);
!array_key_exists($inv_credit,$creditto) && exit('Forbidden');

$allowinvite = allowcheck($inv_groups,$groupid,$winddb['groups']) ? 1 : 0;

if (empty($action)) {

	$page = GetGP('page');
	$db_perpage = 10;
	(!is_numeric($page) || $page<1) && $page = 1;
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
	$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_invitecode WHERE uid=".pwEscape($winduid));
	$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&");

	$query = $db->query("SELECT * FROM pw_invitecode WHERE uid=".pwEscape($winduid)."ORDER BY id DESC $limit");
	$invdb = array();
	while ($rt = $db->fetch_array($query)) {
		$rt['uselate'] = 0;
		if ($rt['ifused']!=2 && $timestamp-$rt['createtime']>$inv_days*86400) {
			$rt['uselate']=1;
		}
		$rt['createtime'] = get_date($rt['createtime'],'Y-m-d H:i:s');
		$rt['usetime'] = $rt['usetime'] ? get_date($rt['usetime'],'Y-m-d H:i:s') : '';
		$invdb[] = $rt;
	}
	require_once PrintHack('index');footer();

} elseif ($action == 'send') {

	if (!$_POST['step']) {

		$inv_dayss = $inv_days*86400;
		InitGP(array('id'));
		if ($id) {
			$invcode = $db->get_one("SELECT * FROM pw_invitecode WHERE id=".pwEscape($id)."AND ifused='0' AND uid=".pwEscape($winduid));
			if ($timestamp-$invcode['createtime']>$inv_dayss) {
				Showmsg('days_limit');
			}
		} else {
			$invcode = $db->get_one("SELECT * FROM pw_invitecode WHERE uid=".pwEscape($winduid)."AND ifused='0' AND createtime>".pwEscape($timestamp-$inv_dayss)."ORDER BY id ASC limit 0,1");
		}
		!$invcode && Showmsg('invcode_error');
		$subject = getLangInfo('other','invite');
		$atc_content = getLangInfo('other','invite_content');
		require_once PrintHack('index');footer();

	} elseif ($_POST['step'] == '3') {

		InitGP(array('id','subject','atc_content','sendtoemail'),'P');
		if (empty($subject)) {
			Showmsg('sendeamil_subject_limit');
		}
		if (empty($atc_content) || strlen($atc_content)<=20) {
			Showmsg('sendeamil_content_limit');
		} elseif (!ereg("^[-a-zA-Z0-9_\.]+\@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$",$sendtoemail)){
			Showmsg('illegal_email');
		}
		require_once(R_P.'require/sendemail.php');
		$additional = "From:{$winddb[email]}\r\nReply-To:{$winddb[email]}\r\nX-Mailer: PHPWind mailer";
		$sendinfo = sendemail($sendtoemail,$subject,$atc_content,$additional);
		if ($sendinfo === true) {
			$db->update("UPDATE pw_invitecode SET ifused='1' WHERE id=".pwEscape($id)."AND uid=".pwEscape($winduid));
			refreshto($basename,'mail_success');
		} else {
			Showmsg('mail_failed');
		}
	}
} elseif ($action == 'buy') {

	$allowinvite == 0 && Showmsg('group_invite');
	if ($inv_limitdays) {
		$rt = $db->get_one("SELECT createtime FROM pw_invitecode WHERE uid=".pwEscape($winduid)."ORDER BY createtime DESC LIMIT 0,1");
		if ($timestamp-$rt['createtime']<$inv_limitdays*86400) {
			Showmsg('inv_limitdays');
		}
	}
	if (!$_POST['step']) {

		require_once PrintHack('index');footer();

	} else {

		InitGP(array('invnum'),'P');
		(!is_numeric($invnum) || $invnum<1) && $invnum = 1;
		if ($invnum > 10) {
			Showmsg('invite_buy');
		}
		if ($creditto[$inv_credit] < $invnum*$inv_costs) {
			Showmsg('invite_costs');
		}
		for ($i = 0;$i < $invnum;$i++) {
			$invcode = randstr(16);
			$db->update("INSERT INTO pw_invitecode"
				. " SET " . pwSqlSingle(array(
					'invcode'	=> $invcode,
					'uid'		=> $winduid,
					'createtime'=> $timestamp
			)));
		}
		$cutcredit = $invnum*$inv_costs;
		require_once(R_P.'require/credit.php');
		$credit->addLog('hack_invcodebuy',array($inv_credit => -$cutcredit),array(
			'uid'		=> $winduid,
			'username'	=> $windid,
			'ip'		=> $onlineip,
			'invnum'	=> stripslashes($invnum)
		));
		$credit->set($winduid,$inv_credit,-$cutcredit);

		refreshto($basename,'operate_success');
	}
} elseif ($_POST['action'] == 'delete') {

	InitGP(array('selid'),'P');
	(!$selid || !is_array($selid)) && Showmsg('del_error');
	$delids = array();
	foreach ($selid as $value) {
		is_numeric($value) && $delids[] = $value;
	}
	$delids = pwImplode($delids);
	$db->update("DELETE FROM pw_invitecode WHERE id IN ($delids) AND uid=".pwEscape($winduid));
	refreshto($basename,'operate_success');

} elseif ($action == 'pay') {

	empty($inv_onlinesell) && Showmsg('invite_onlinesell');
	include_once(D_P.'data/bbscache/ol_config.php');

	if (empty($_POST['step'])) {

		$num	= 1;
		$email	= '';
		require_once PrintHack('index');footer();

	} else {

		InitGP(array('invnum','email','method'));
		(!is_numeric($invnum) || $invnum<1) && $invnum = 1;
		$order_no = ($method-1).str_pad('0',10,"0",STR_PAD_LEFT).get_date($timestamp,'YmdHis').num_rand(5);
		$rt = array();
		if ($email) {
			$rt = $db->get_one("SELECT * FROM pw_clientorder WHERE payemail=" . pwEscape($email) . " AND uid='0' AND state='0'");
		}
		if ($rt) {
			if (!isset($_POST['submit'])) {
				$num	= $rt['number'];
				$email	= $rt['payemail'];
				require_once PrintHack('index');footer();
			}
			$db->Update("UPDATE pw_clientorder SET " . pwSqlSingle(array('order_no' => $order_no, 'number' => $invnum)) . ' WHERE id=' . pwEscape($rt['id']));
		} else {
			$db->update("INSERT INTO pw_clientorder SET " . pwSqlSingle(array(
				'order_no'	=> $order_no,
				'type'		=> 4,
				'uid'		=> 0,
				'price'		=> $inv_price,
				'payemail'	=> $email,
				'number'	=> $invnum,
				'date'		=> $timestamp,
				'state'		=> 0,
			)));
		}

		switch ($method) {
			case 2 :
				if (!$ol_payto) {
					Showmsg('olpay_alipayerror');
				}
				require_once(R_P.'require/onlinepay.php');
				$olpay = new OnlinePay($ol_payto);
				ObHeader($olpay->alipayurl($order_no, $invnum * $inv_price, 4));
				break;
			case 4 :
				if(!$ol_tenpay || !$ol_tenpaycode){
					Showmsg('olpay_tenpayerror');
				}
				$strBillDate = get_date($timestamp,'Ymd');
				$strSpBillNo = substr($order_no,-10);
				$strTransactionId = $ol_tenpay.$strBillDate.$strSpBillNo;
				$db->update("UPDATE pw_clientorder SET order_no=".pwEscape($strTransactionId)."WHERE order_no=".pwEscape($order_no));
				$url  = "http://pay.phpwind.net/pay/create_payurl.php?";
				$para = array(
					'cmdno' => '1',
					'date' => $strBillDate,
					'bargainor_id' => $ol_tenpay,
					'transaction_id' => $strTransactionId,
					'sp_billno' => $strSpBillNo,
					'total_fee' => $invnum*$inv_price*100,
					'bank_type' => 0,
					'fee_type' => 1,
					'return_url' => "{$db_bbsurl}/hack.php?H_name=invite&action=tenpay",
					'attach' => 'my_magic_string',
				);
				$arg='';
				foreach($para as $key => $value){
					if($value){
						$url .= "$key=".urlencode($value)."&";
						$arg .= "$key=$value&";
					}
				}
				$strSign = strtoupper(md5($arg."key=$ol_tenpaycode"));
				$url .= "desc=".getLangInfo('other','currency')."&sign=$strSign";
				ObHeader($url);
				break;
		}
		Showmsg('undefined_action');
	}
} elseif ($action == 'tenpay') {

	include_once(D_P.'data/bbscache/ol_config.php');
	if (!$ol_onlinepay) {
		Showmsg($ol_whycolse);
	}
	if (!$ol_tenpay || !$ol_tenpaycode) {
		Showmsg('olpay_tenpayerror');
	}

	InitGP(array('cmdno','pay_result','date','bargainor_id','transaction_id','sp_billno','total_fee', 'fee_type','attach','sign'));

	$text = "cmdno=$cmdno&pay_result=$pay_result&date=$date&transaction_id=$transaction_id&sp_billno=$sp_billno&total_fee=$total_fee&fee_type=$fee_type&attach=$attach&key=$ol_tenpaycode";
	$mac = strtoupper(md5($text));

	if ($mac != $sign) {
		Showmsg( "验证MD5签名失败");
	}
	if ($ol_tenpay != $bargainor_id ) {
		Showmsg( "错误的商户号");
	}
	if ($pay_result != "0" ) {
		Showmsg( "支付失败");
	}

	$rt = $db->get_one("SELECT * FROM pw_clientorder WHERE order_no=".pwEscape($transaction_id));
	if (!$rt) {
		refreshto('userpay.php','系统中没有您的充值订单，无法完成充值！');
	}
	if ($rt['state'] == 2) {
		refreshto('userpay.php','该订单已经充值成功！');
	}
	$db->update("UPDATE pw_clientorder SET payemail=".pwEscape($buyer_email).",state=2,descrip='已完成订单' WHERE order_no=".pwEscape($transaction_id));

	$invcodes = $fistinvcode = '';
	for ($i = 0; $i<$rt['number']; $i++) {
		$invcode = randstr(16);
		$i == 0 && $fistinvcode = $invcode;
		$invcodes .= ($invcodes ? "\n" : '').$invcode;
		$db->update("INSERT INTO pw_invitecode SET " . pwSqlSingle(array(
			'invcode'		=> $invcode,
			'uid'			=> 0,
			'createtime'	=> $timestamp
		)));
	}
	require_once(R_P.'require/sendemail.php');
	$sendinfo = sendemail($rt['payemail'],'email_invite_subject','email_invite_content','email_additional');

	refreshto($db_registerfile.'?invcode='.$fistinvcode, 'email_invite_success');
}
?>