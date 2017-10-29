<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_areas.php");
require_once("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

/* post 数据处理 */
if(get_args('name')){
	$POST['parent_id'] = short_check(get_args('parent_id'));
	$POST['area_type'] = short_check(get_args('area_type'));
	$POST['area_name'] = short_check(get_args('name'));
}elseif(get_args('id')){
	$id = short_check(get_args('id'));
	$POST['area_name'] = short_check(get_args('v'));
}
//print_r($POST);
//exit;
//数据表定义区
$t_areas = $tablePreStr."areas";
$t_admin_log = $tablePreStr."admin_log";
//权限管理
$right=check_rights("area_add");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
if(get_args('name')){
	$ins_suc=insert_area($dbo,$t_areas,$POST);
	if($ins_suc) {
		/** 添加log */
		$admin_log ="地域管理添加目录";
		admin_log($dbo,$t_admin_log,$admin_log);

		action_return(1,$a_langpackage->a_upd_suc,'-1');
	} else {
		action_return(0,$a_langpackage->a_upd_lose,'-1');
	}
}elseif($id){
	$upd_suc=update_area($dbo,$t_areas,$POST,$id);
	if($upd_suc) {
		/** 添加log */
		$admin_log ="地域管理修改目录";
		admin_log($dbo,$t_admin_log,$admin_log);

		echo 1;
	}
}
?>