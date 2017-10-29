<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_complaint.php");

//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("del_complaint_title");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

/* post 数据处理 */
$type_id = intval(get_args('type_id'));

//数据表定义区
$t_complaint_type = $tablePreStr."complaint_type";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

	$del_suc = del_complaint_type($dbo,$t_complaint_type,$type_id) ;

	if($del_suc) {
		action_return(1,$a_langpackage->a_del_suc,'m.php?app=complaint_type');
	} else {
		action_return(0,$a_langpackage->a_del_lose,'-1');
	}

?>