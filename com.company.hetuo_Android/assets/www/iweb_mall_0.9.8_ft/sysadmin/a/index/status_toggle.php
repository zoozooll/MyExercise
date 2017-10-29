<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_admin_logs.php");
/* post 数据处理 */
$id = intval(get_args('id'));
$s = intval(get_args('s'));

if(!$id) {
	exit();
}

//数据表定义区
$t_index_images = $tablePreStr."index_images";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_index_images`  set status='$s' where id='$id'";

if($dbo->exeUpdate($sql)) {
	if ($s){
		/** 添加log */
		$admin_log ="设置显示首页轮显图片";
		admin_log($dbo,$t_admin_log,$admin_log);
		echo "yes";
	}else {
		/** 添加log */
		$admin_log ="取消显示首页轮显图片";
		admin_log($dbo,$t_admin_log,$admin_log);
		echo "no";
	}
}
?>