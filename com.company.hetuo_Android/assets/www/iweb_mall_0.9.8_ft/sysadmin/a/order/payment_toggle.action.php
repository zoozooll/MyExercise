<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_admin_logs.php");

$name_array = array('on_sale','hot','new','best','promote');
/* post 数据处理 */
$id = intval(get_args('id'));
$v = intval(get_args('v'));

if(!$id) {
	exit();
}

//数据表定义区
$t_payment = $tablePreStr."payment";
$t_admin_log = $tablePreStr."admin_log";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_payment` set enabled='$v' where pay_id='$id'";

if($dbo->exeUpdate($sql)) {
	/** 添加log */
	$admin_log ="更新支付接口设置";
	admin_log($dbo,$t_admin_log,$admin_log);

	echo "1";
} else {
	echo "-1";
}
exit;
?>