<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_complaint.php");

//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("complaint_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

/* post 数据处理 */
$complaints_id = intval(get_args('id'));

//数据表定义区
$t_complaints = $tablePreStr."complaints";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

	$del_suc = del_complaint($dbo,$t_complaints,$complaints_id) ;

	if($del_suc) {
		action_return(1,$a_langpackage->a_del_suc,'m.php?app=complaint_list');
	} else {
		action_return(0,$a_langpackage->a_del_lose,'-1');
	}

?>