<?php
require_once('global.php');
require_once(R_P.'require/posthost.php');
include_once(D_P.'data/bbscache/ol_config.php');

InitGP(array('action','out_trade_no','trade_status','buyer_email','notify_id'));

!empty($_POST) && $_GET = $_POST;
$isPwPay = false;

if ($action || empty($ol_alipaykey) || empty($ol_alipaypartnerID)) {
	$ol_alipaypartnerID = '2088001505801569';
	$isPwPay = true;
}
$veryfy_result2 = PostHost("http://notify.alipay.com/trade/notify_query.do","notify_id=$notify_id&partner=" . $ol_alipaypartnerID, 'POST');

if ($isPwPay) {
	$url = '';
	foreach ($_GET as $key => $value) {
		if ($key <> 'action' && $value) {
			$url .= "$key=".urlencode($value)."&";
		}
	}
	$veryfy_result1 = PostHost("http://pay.phpwind.net/pay/alipay_notify.php", $url, 'POST');
} else {
	ksort($_GET);
	reset($_GET);
	$arg = '';
	foreach ($_GET as $key => $value) {
		if ($value && !in_array($key, array('action','sign','sign_type'))) {
			$value = str_replace('&#41;',')',$value);
			$arg .= "$key=$value&";
		}
	}
	$veryfy_result1 = ($_GET['sign'] == md5(substr($arg,0,-1).$ol_alipaykey)) ? 'true' : 'false';
}
if (!eregi("true$",$veryfy_result1) || !eregi("true$",$veryfy_result2)) {
	paymsg('userpay.php','alipay_failure','fail');
}

if (empty($action)) {

	if (!$ol_onlinepay) {
		Showmsg($ol_whycolse);
	}
	if (!$ol_payto) {
		Showmsg('olpay_seterror');
	}
	if (procLock('alipay',$winduid)) {
		$rt = $db->get_one('SELECT c.*,m.username,m.groupid,m.groups FROM pw_clientorder c LEFT JOIN pw_members m USING(uid) WHERE order_no=' . pwEscape($out_trade_no));
		if (empty($rt)) {
			procUnLock('alipay',$winduid);
			paymsg('userpay.php','alipay_ordersfailure');
		}
		$fee = $rt['number'] * $rt['price'];
	
		if ($fee != $_GET['total_fee'] || $_GET['seller_email'] != $ol_payto) {
			procUnLock('alipay',$winduid);
			paymsg('userpay.php','alipay_failure');
		}
		if ($trade_status == 'TRADE_FINISHED') {
			if ($rt['state'] == 2) {
				procUnLock('alipay',$winduid);
				paymsg('userpay.php','alipay_orderssuccess');
			}
			$ret_url = 'userpay.php';
	
			if (file_exists(R_P."require/olpay/pay_{$rt[type]}.php")) {
				require_once Pcv(R_P."require/olpay/pay_{$rt[type]}.php");
			}
			$db->update("UPDATE pw_clientorder SET payemail=" . pwEscape($buyer_email) . ",state=2 WHERE order_no=" . pwEscape($out_trade_no));
			procUnLock('alipay',$winduid);
			paymsg($ret_url,'alipay_orderssuccess');
		} else {
			procUnLock('alipay',$winduid);
			paymsg('userpay.php','alipay_topayfailure');
		}
	} else {
		Showmsg('proclock');
	}
} elseif ($action == 'trade') {

	$rt = $db->get_one("SELECT tid,ifpay FROM pw_tradeorder WHERE order_no='$out_trade_no'");

	if (empty($rt) || $rt['ifpay'] == '3') {
		paymsg('index.php','alipay_ordersfailure');
	}
	switch ($trade_status) {
		case 'WAIT_SELLER_SEND_GOODS':
			$db->update("UPDATE pw_tradeorder SET ifpay='1',tradedate='$timestamp',payment='2' WHERE order_no='$out_trade_no'");
			break;
		case 'WAIT_BUYER_CONFIRM_GOODS':
			$db->update("UPDATE pw_tradeorder SET ifpay='2',tradedate='$timestamp' WHERE order_no='$out_trade_no'");
			break;
		case 'TRADE_FINISHED':
			$db->update("UPDATE pw_tradeorder SET ifpay='3',tradedate='$timestamp' WHERE order_no='$out_trade_no'");
			$db->update("UPDATE pw_trade SET salenum=salenum+1 WHERE tid=".pwEscape($rt['tid']));
			break;
		default:
			$db->update("UPDATE pw_tradeorder SET ifpay='4',tradedate='$timestamp' WHERE order_no='$out_trade_no'");
	}
	paymsg('index.php','operate_success');
} elseif ($action == 'pcalipay') {//团购、活动
	list($pcmid) = explode('_',$out_trade_no);
	$rt = $db->get_one("SELECT tid,ifpay FROM pw_pcmember WHERE pcmid=".pwEscape($pcmid));

	if (empty($rt) || $rt['ifpay'] == '1') {
		paymsg("read.php?tid=$rt[tid]",'pcalipay_success');
	}
	if ($trade_status) {
		$db->update("UPDATE pw_pcmember SET ifpay=1 WHERE pcmid=".pwEscape($pcmid));
	}
	paymsg("read.php?tid=$rt[tid]",'pcalipay_success');
}

function paymsg($url,$msg,$notify = 'success') {
	if (empty($_POST)) {
		refreshto($url,$msg);
	}
	exit($notify);
}
?>