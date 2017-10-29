<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_crons.php");
require("../foundation/module_admin_logs.php");
$right=check_rights("programme_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
//语言包引入
$a_langpackage=new adminlp;

/* post 数据处理 */
if(empty($_POST)){
	$id = intval(get_args('id'));
}else{
	$key_id = get_args('searchkey');
	$id=implode(',',$key_id);
}
//数据表定义区
$t_crons = $tablePreStr."crons";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$del_suc=del_crons($dbo,$t_crons,$id);

if($del_suc) {
	/** 添加log */
	$admin_log ="删除计划任务";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_del_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}
?>