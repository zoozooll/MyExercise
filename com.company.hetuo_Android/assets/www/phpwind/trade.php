<?php
define('SCR','trade');
require_once('global.php');

InitGP(array('action'));

if ($action == 'buy') {

	$trade = $db->get_one("SELECT t.*,m.username FROM pw_trade t LEFT JOIN pw_members m ON t.uid=m.uid WHERE t.tid=".pwEscape($tid));

	if (empty($_POST['step'])) {
		if (empty($winduid)) Showmsg('not_login');
		require_once(R_P.'require/header.php');
		require_once PrintEot('trade');
		footer();

	} else {

		if ($trade['uid'] == $winduid) {
			Showmsg('onlinepay_goodsbuy');
		}
		InitGP(array('address','consignee','tel','descrip'));
		InitGP(array('quantity','zip','transport'), 2);

		if ($quantity < 1) {
			Showmsg('goods_num_error');
		}
		if (!$address || !$consignee || !$tel) {
			Showmsg('onlinepay_goods_address');
		}
		$transportfee = 0;
		if ($trade['transport']) {
			switch ($transport) {
				case 1:
					$transportfee = $trade['mailfee'];break;
				case 2:
					$transportfee = $trade['expressfee'];break;
				case 3:
					$transportfee = $trade['emsfee'];break;
				default:
					Showmsg('goods_transport');
			}
		} else {
			$transport = 0;
		}
		$order_no = '1'.str_pad($winduid,10, "0",STR_PAD_LEFT).get_date($timestamp,'YmdHis').num_rand(5);

		$db->update("INSERT INTO pw_tradeorder SET " . pwSqlSingle(array(
			'tid'			=> $tid,
			'order_no'		=> $order_no,
			'subject'		=> $trade['name'],
			'buyer'			=> $winduid,
			'seller'		=> $trade['uid'],
			'price'			=> $trade['costprice'],
			'quantity'		=> $quantity,
			'transportfee '	=> $transportfee,
			'transport'		=> $transport,
			'buydate'		=> $timestamp,
			'ifpay'			=> 0,
			'address'		=> $address,
			'consignee'		=> $consignee,
			'tel'			=> $tel,
			'zip'			=> $zip,
			'descrip'		=> $descrip
		)));
		$oid = $db->insert_id();

		ObHeader("trade.php?action=order&oid=$oid");
	}

} elseif ($action == 'order') {

	InitGP(array('oid'));

	$order = $db->get_one("SELECT td.*,t.paymethod,m.username FROM pw_tradeorder td LEFT JOIN pw_trade t ON td.tid=t.tid LEFT JOIN pw_members m ON td.seller=m.uid WHERE td.oid=".pwEscape($oid));

	if (empty($order) || !in_array($winduid,array($order['buyer'],$order['seller']))) {
		Showmsg('data_error');
	}
	$order['buydate'] = get_date($order['buydate']);
	$order['tradedate'] = get_date($order['tradedate']);
	$order['tradeinfo'] = str_replace("\n",'<br />',$order['tradeinfo']);
	$totalpay = $order['price'] * $order['quantity'] + $order['transportfee'];
	require_once(R_P.'require/header.php');
	require_once PrintEot('trade');
	footer();

} elseif ($action == 'pay') {

	InitGP(array('oid','method'));

	$order = $db->get_one("SELECT t.*,mb.tradeinfo FROM pw_tradeorder t LEFT JOIN pw_memberinfo mb ON t.seller=mb.uid WHERE t.oid=".pwEscape($oid));

	if (empty($order) || $order['buyer'] <> $winduid) {
		Showmsg('data_error');
	}
	if (!is_array($trade = unserialize($order['tradeinfo']))) {
		$trade = array();
	}
	if ($order['ifpay'] > 0) {
		Showmsg('onlinepay_haspay');
	}

	switch ($method) {

		case 1:

			InitGP(array('tradeinfo'));
			$db->update("UPDATE pw_tradeorder SET " . pwSqlSingle(array(
				'ifpay'		=> 1,
				'tradedate'	=> $timestamp,
				'payment'	=> 1,
				'tradeinfo'	=> $tradeinfo
			)) . " WHERE oid=".pwEscape($oid));

			require_once(R_P.'require/msg.php');
			pwSendMsg(array(
				'toUid'		=> $order['seller'],
				'subject'	=> 'goods_pay_title',
				'content'	=> 'goods_pay_content',
				'other'		=> array(
					'goodsname'	=> $order['subject'],
					'buydate'	=> get_date($order['buydate']),
					'buyer'		=> $windid,
					'tid'		=> $order['tid'],
					'descrip'	=> stripslashes($tradeinfo)
				))
			);

			refreshto("u.php?action=trade",'operate_success');

			break;

		case 2:

			if (empty($trade['alipay'])) {
				Showmsg('onlinepay_alipay');
			}
			include_once(D_P.'data/bbscache/ol_config.php');
			require_once(R_P.'require/onlinepay.php');
			$olpay = new OnlinePay($trade['alipay']);

			$param = array(
				'notify_url'		=> "{$db_bbsurl}/alipay.php?action=trade",
				'return_url'		=> "{$db_bbsurl}/alipay.php?action=trade",

				/* 业务参数 */
				'subject'           => $order['subject'],
				'out_trade_no'      => $order['order_no'],
				'price'             => $order['price'],
				'quantity'          => $order['quantity'],
				'payment_type'      => 1,

				/* 物流参数 */
				'logistics_type'    => 'EXPRESS',
				'logistics_fee'     => $order['transportfee'],
				'logistics_payment' => $order['transportfee'] > 0 ? 'BUYER_PAY' : 'SELLER_PAY',

				'receive_name'		=> $order['consignee'],
				'receive_address'	=> $order['address'],
				'receive_zip'		=> $order['zip'],
				'receive_phone'		=> $order['tel']
			);
			ObHeader($olpay->alipay2url($param));
			break;
		/*
		case 4:

			if (empty($trade['tenpay'])) {
				Showmsg('onlinepay_tenpay');
			}
			$url = "http://pay.phpwind.net/pay/create_payurl.php?";

			$para = array(
				'mch_vno'			=> $order['order_no'],
				'cmdno'				=> '12',
				'encode_type'		=> $db_charset == 'utf-8' ? '2' : '1',
				'seller'			=> $trade['tenpay'],
				'mch_name'			=> $order['subject'],
				'mch_price'			=> $order['price'] * $order['quantity'] * 100,
				'transport_desc'    => $order['transport'],
				'transport_fee'     => round($order['transportfee'] * 100),
				'mch_returl'		=> "{$db_bbsurl}/tenpay.php?action=trade",
				'show_url'			=> "{$db_bbsurl}/tenpay.php?action=trade",
				'version'			=> '2',
			);
			foreach ($para as $key => $value) {
				if ($value) {
					$url .= "$key=".urlencode($value)."&";
				}
			}
			ObHeader($url);
			break;
		*/

		default:
			exit('error');
	}

} elseif ($action == 'send') {

	InitGP(array('oid','logistics','orderid'));

	$order = $db->get_one("SELECT td.*,m.username FROM pw_tradeorder td LEFT JOIN pw_members m ON td.buyer=m.uid WHERE td.oid=".pwEscape($oid));

	if (empty($order) || $order['seller'] <> $winduid || $order['ifpay'] <> 1 || $order['payment'] <> 1) {
		Showmsg('data_error');
	}
	if (empty($logistics) || empty($orderid)) {
		Showmsg('onlinepay_logistics');
	}
	$descrip = getLangInfo('writemsg','onlinepay_logistics',array(
		'logistics' => $logistics,
		'orderid'	=> $orderid,
	));
	$db->update("UPDATE pw_tradeorder SET " . pwSqlSingle(array(
		'ifpay'		=> 2,
		'tradedate'	=> $timestamp,
		'tradeinfo'	=> $descrip
	)) . " WHERE oid=".pwEscape($oid));

	require_once(R_P.'require/msg.php');
	pwSendMsg(array(
		'toUser'	=> $order['username'],
		'subject'	=> 'goods_send_title',
		'content'	=> 'goods_send_content',
		'other'		=> array(
			'goodsname'	=> $order['subject'],
			'buydate'	=> get_date($order['buydate']),
			'seller'	=> $windid,
			'tid'		=> $order['tid'],
			'descrip'	=> $descrip
		))
	);

	refreshto("u.php?action=trade&type=saled",'operate_success');

} elseif ($action == 'get') {

	InitGP(array('oid'));

	$order = $db->get_one("SELECT * FROM pw_tradeorder WHERE oid=".pwEscape($oid));

	if (empty($order) || $order['buyer'] <> $winduid || $order['ifpay'] <> 2) {
		Showmsg('data_error');
	}

	$db->update("UPDATE pw_tradeorder SET ".pwSqlSingle(array(
		'ifpay'		=> 3,
		'tradedate'	=> $timestamp
	)) . " WHERE oid=".pwEscape($oid));

	$db->update("UPDATE pw_trade SET salenum=salenum+1 WHERE tid=".pwEscape($order['tid']));

	refreshto("u.php?action=trade",'operate_success');
} elseif ($action == 'pcalipay') {
	InitGP(array('tid','pcmid','pcid'),GP,2);

	$pcvaluetable = GetPcatetable($pcid);
	
	$order = $db->get_one("SELECT pv.price,pv.deposit,pm.username,pm.nums,pm.phone,pm.mobile,pm.address,pm.ifpay,pm.totalcash,t.author,t.authorid,t.subject FROM pw_pcmember pm LEFT JOIN $pcvaluetable pv ON pm.tid=pv.tid LEFT JOIN pw_threads t ON pv.tid=t.tid WHERE pm.tid=".pwEscape($tid)." AND pm.pcmid=".pwEscape($pcmid)." AND pm.uid=".pwEscape($winduid));
	
	$order['zip'] = '100000';

	$order['tradeinfo'] = $db->get_value("SELECT tradeinfo FROM pw_memberinfo WHERE uid=".pwEscape($order['authorid']));

	if (empty($order)) {
		Showmsg('data_error');
	}

	if (!is_array($trade = unserialize($order['tradeinfo']))) {
		$trade = array();
	}
	if ($order['ifpay'] > 0) {
		Showmsg('pcalipay_haspay');
	}

	if (empty($trade['alipay'])) {
		Showmsg('onlinepay_alipay');
	}

	include_once(D_P.'data/bbscache/ol_config.php');
	require_once(R_P.'require/onlinepay.php');
	$olpay = new OnlinePay($trade['alipay']);

	$price = !ceil($order['deposit']) ? $order['price'] : $order['deposit'];
	$price = number_format($price, 2, '.', '');

	$order_no = $pcmid.'_'.str_pad($winduid,10, "0",STR_PAD_LEFT).get_date($timestamp,'YmdHis').num_rand(5);

	$param = array(
		'notify_url'		=> "{$db_bbsurl}/alipay.php?action=pcalipay",
		'return_url'		=> "{$db_bbsurl}/alipay.php?action=pcalipay",

		/* 业务参数 */
		'subject'           => $order['subject'],
		'out_trade_no'      => $order_no,
		'price'             => $price,
		'quantity'          => $order['nums'],
		'payment_type'      => 1,

		/* 物流参数 */
		'logistics_type'    => 'EXPRESS',
		'logistics_fee'     => '0.00',
		'logistics_payment' => 'SELLER_PAY',

		'receive_name'		=> $order['username'],
		'receive_address'	=> $order['address'],
		'receive_zip'		=> $order['zip'],
		'receive_phone'		=> $order['mobile']
	);

	ObHeader($olpay->alipay2url($param));
}
?>