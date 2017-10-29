<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_users.php");
require("foundation/module_areas.php");
require("foundation/module_goods.php");
require("foundation/module_payment.php");
//require_once("foundation/asession.php");
//引入语言包
$m_langpackage=new moduleslp;

$goods_id = intval(get_args('gid'));
$order_num = intval(get_args('v'));
$address_id = intval(get_args('address_id'));
if(!$goods_id) { exit("非法操作"); }
if($order_num<1) { exit("非法操作"); }

//数据表定义区
$t_users = $tablePreStr."users";
$t_goods = $tablePreStr."goods";
$t_transport_template = $tablePreStr."goods_transport";
$t_user_info = $tablePreStr."user_info";
$t_areas = $tablePreStr."areas";
$t_shop_payment = $tablePreStr."shop_payment";
$t_payment = $tablePreStr."payment";
$t_user_address = $tablePreStr."user_address";
$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$sql="select * from $t_user_address where user_id=$user_id";
$address_rs=$dbo->getRs($sql);

$user_info=array(
	'user_country'=>'',
	'user_id'=>'',
	'user_province'=>'',
	'user_city'=>'',
	'to_user_name'=>'',
	'user_district'=>'',
	'full_address'=>'',
	'zipcode'=>'',
	'mobile'=>'',
	'telphone'=>'',
	'email'=>'',

);

if($address_id && $address_rs){
	foreach($address_rs as $value) {
		if($address_id == $value['address_id']) {
			$user_info = $value;
		}
	}
} elseif ($address_rs) {
	$user_info = $address_rs[0];
	$address_id = $address_rs[0]['address_id'];
}

// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = $user_info['user_country'] ? $user_info['user_country'] : 1;

$shop_id=get_sess_shop_id();
$goods_info = get_goods_info($dbo,$t_goods,"*",$goods_id);

if($shop_id == $goods_info['shop_id']) {
	//$_SESSION['error_message'] = $m_langpackage->m_dontbuy_youself;
	set_sess_err_msg($m_langpackage->m_dontbuy_youself);
	echo '<script language="JavaScript">location.href="modules.php?app=message"</script>';
	exit();
}
if(!$goods_info) { exit("非法操作"); }
if($goods_info['goods_number'] < $order_num) {
	//$_SESSION['error_message'] = $m_langpackage->m_order_nomoregoods;
	set_sess_err_msg($m_langpackage->m_dontbuy_youself);
	echo '<script language="JavaScript">location.href="modules.php?app=message"</script>';
	exit;
}

//$user_info = get_user_info($dbo,$t_user_info,$user_id);
$user_info['user_id'] = $user_id;
// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = $user_info['user_country'] ? $user_info['user_country'] : 1;
$areas_info = get_areas_info($dbo,$t_areas);
$sql = "select * from `$t_shop_payment` where shop_id='$goods_info[shop_id]'";
$payment_info = $dbo->getRow($sql);
$payment = get_payment_info($dbo,$t_payment);
$transport_type =0;
//取得配送方式
if ($goods_info['is_transport_template']&&$goods_info['transport_template_id']) {
	$transport_template_info = $dbo->getRow("SELECT content FROM $t_transport_template WHERE id='{$goods_info['transport_template_id']}'");
	$transport = unserialize($transport_template_info['content']);
	foreach ($transport as $key=>$value){
		if ($key=="ems") {
			$name="EMS";
		}
		if ($key=="ex") {
			$name="快递";
		}
		if ($key=="pst") {
			$name="平邮";
		}
		$transport[$key]['name']=$name;
	}
	if (isset($transport['ems'])) {
		$transport_type='ems';
	}
	if (isset($transport['pst'])) {
		$transport_type='pst';
	}
	if (isset($transport['ex'])) {
		$transport_type='ex';
	}
}
?>