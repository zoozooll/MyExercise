<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("admin_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');	
}
/* post 数据处理 */
$id = intval(get_args('id'));

//数据表定义区
$t_admin_user = $tablePreStr."admin_user";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "delete from `$t_admin_user` where admin_id=$id";

if($dbo->exeUpdate($sql)) {
	action_return(1,$a_langpackage->a_del_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}
?>