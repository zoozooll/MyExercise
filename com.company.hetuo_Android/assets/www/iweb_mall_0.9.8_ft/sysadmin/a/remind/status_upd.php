<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_admin_logs.php");

$v = short_check(get_args('v'));
$id = short_check(get_args('id'));

//数据表定义区
$t_remind = $tablePreStr."remind";
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql="update `$t_remind` set enable=$v where remind_id='$id'";
$remind=$dbo->exeUpdate($sql);
//echo $sql;
if($remind){
	/** 添加log */
	$admin_log ="更新提醒设置状态";
	admin_log($dbo,$t_admin_log,$admin_log);

	echo 1;
}
?>