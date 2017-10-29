<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_complaint.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;

/* post 数据处理 */
$post['type_content'] = short_check(get_args('type_content'));
$type_id = intval(get_args('type_id'));

if ($type_id){
	//权限管理
	$right=check_rights("edit_complaint_title");
	if(!$right){
		action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
	}
}else {
	//权限管理
	$right=check_rights("add_complaint_title");
	if(!$right){
		action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
	}
}

//数据表定义区
$t_complaint_type = $tablePreStr."complaint_type";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

if($type_id!=0){
	$upd_suc=update_complaint_type_byid($dbo,$t_complaint_type,$post,$type_id);
	if($upd_suc) {
		admin_log($dbo,$t_admin_log,$sn = '修改投诉标题');
		action_return(1,$a_langpackage->a_amend_suc,'m.php?app=complaint_type');
	} else {
		action_return(0,$a_langpackage->a_amend_lose,'-1');
	}
}else{
	$insert_id = insert_complaint_type($dbo,$t_complaint_type,$post);

	if($insert_id) {
		admin_log($dbo,$t_admin_log,$sn = '添加投诉标题');
		action_return(1,$a_langpackage->a_add_suc,'m.php?app=complaint_type');
	} else {
		action_return(0,$a_langpackage->a_add_lose,'-1');
	}
}
?>