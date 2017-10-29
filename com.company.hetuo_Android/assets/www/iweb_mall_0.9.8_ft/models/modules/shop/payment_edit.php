<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_payment.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_payment = $tablePreStr."payment";
$t_shop_payment = $tablePreStr."shop_payment";

$pay_id = intval(get_args('pay_id'));
if(!$pay_id) {
	exit("非法操作！");
}

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

$payment = get_one_payment($dbo,$t_payment,$pay_id);
if(!$payment) {
	exit("非法操作!");
}

$payment_config = $payment['config'];
$line = explode("\n",$payment_config);
$config_arr = array();
foreach($line as $value) {
	$v = explode("|",$value);
	if($v[0] && $v[1]) {
		$config_arr[$v[0]] = $v[1];
		$config_select[$v[0]] = '';
		if(isset($v[2])){
			$config_select[$v[0]] = unserialize($v[2]);
		}
	}
}

$info = get_one_shop_payment($dbo,$t_shop_payment,$shop_id,$pay_id);
?>