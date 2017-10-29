<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_areas.php");
require("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("area_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* post 数据处理 */
$id = short_check(get_args('id'));
//$POST['area_type'] = short_check(get_args('area_type'));
//if(get_args('name')){
//	$POST['area_name'] = short_check(get_args('name'));
//}elseif(get_args('area_name')){
//	$POST['area_name'] = short_check(get_args('area_name'));
//}

//数据表定义区
$t_areas = $tablePreStr."areas";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
$ins_suc=del_area($dbo,$t_areas,$id);
if($ins_suc) {
	/** 添加log */
	$admin_log ="删除地域管理目录";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_del_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}

?>