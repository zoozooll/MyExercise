<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$a_langpackage=new adminlp;

require_once("../foundation/module_asd.php");
require_once("../foundation/module_admin_logs.php");
//权限管理
$right=check_rights("adv_position_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* get */
$position_id=get_args('position_id');
if($position_id){
	$position_id=implode(",", $position_id);
}else{
	$position_id = intval(get_args('id'));
}
if(!$position_id) {
	
	action_return(0,$a_langpackage->a_error,'-1');
}

//数据表定义区
$t_asd_position = $tablePreStr."asd_position";
$t_asd_content = $tablePreStr."asd_content";
$t_admin_log = $tablePreStr."admin_log";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "delete from `$t_asd_position` where position_id in($position_id)";
$sql_del = "delete from `$t_asd_content` where position_id in($position_id)";
if($dbo->exeUpdate($sql) and $dbo->exeUpdate($sql_del)) {
	admin_log($dbo,$t_admin_log,"删除广告位：$position_id");
	action_return();
} else {
	action_return(0,$a_langpackage->a_del_lose_repeat,'-1');
}
?>