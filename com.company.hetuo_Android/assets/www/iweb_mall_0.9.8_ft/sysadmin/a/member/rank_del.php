<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_users.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("user_rank_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

/* get */
$rankid=get_args('rank_id');

if($rankid){
	$id=implode(",", $rankid);
}else{
	$id = intval(get_args('id'));
}
if(!$id) {
	action_return(0,$a_langpackage->a_error,'-1');
}

//数据表定义区
$t_user_rank = $tablePreStr."user_rank";
$t_users = $tablePreStr."users";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql="delete from $t_user_rank where rank_id in($id)";
$update_sql = "update $t_users set rank_id ='1' where rank_id in($id)";
if($dbo->exeUpdate($sql) and $dbo->exeUpdate($update_sql)) {
	admin_log($dbo,$t_admin_log,$sn = '管理员级别删除');
	action_return();
} else {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}
?>