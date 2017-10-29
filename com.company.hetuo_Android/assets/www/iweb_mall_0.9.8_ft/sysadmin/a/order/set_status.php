<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_payment.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("order_status");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* post */
$post = get_args('order_id');
$order_ids = implode(",",$post);
$status = get_args('status');


$t_order_info = $tablePreStr."order_info";
$t_admin_log = $tablePreStr."admin_log";

$sql = "update `$t_order_info` set ";
if($status=='cancel') {
	$sql .= " order_status=0 ";
} elseif($status=='pay') {
	$sql .= " pay_status=1 ";
} elseif($status=='nopay') {
	$sql .= " pay_status=0 ";
} elseif($status=='transport') {
	$sql .= " transport_status=1 ";
} elseif($status=='notransport') {
	$sql .= " transport_status=0 ";
}

$sql .= " where order_id in ($order_ids)";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

if($dbo->exeUpdate($sql)) {
	admin_log($dbo,$t_admin_log,$sn = '修改订单状态');
	action_return(1,$a_langpackage->a_amend_suc,"-1");
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>