<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_news.php");

//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("group_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
$ctime = new time_class;

/* post 数据处理 */
$id = intval(get_args('id'));

if(!$id) {
	action_return(0,$a_langpackage->a_error,'-1');
	exit;
}

//数据表定义区
$t_admin_group=$tablePreStr."admin_group";
$t_admin_user =$tablePreStr."admin_user";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
$sql="update $t_admin_group set del_flg=1 where id='$id'";
$sql_update = "update $t_admin_user set group_id ='1' where group_id in($id)";
if($dbo->exeUpdate($sql) and $dbo->exeUpdate($sql_update)) {
	action_return(1,$a_langpackage->a_amend_suc,'m.php?app=admin_group');
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>