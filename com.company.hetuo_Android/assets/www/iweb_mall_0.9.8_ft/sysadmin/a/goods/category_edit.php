<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_category.php");
require_once("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("cat_update");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* post 数据处理 */
$post['cat_name'] = short_check(get_args('cat_name'));
$post['parent_id'] = intval(get_args('parent_id'));
$post['sort_order'] = intval(get_args('sort_order'));

if(empty($post['cat_name'])) {
	action_return(0,$a_langpackage->a_class_null,'-1');
	exit;
}

$cat_id = intval(get_args('cat_id'));
$brand_id_list = get_args("brand_id");
if(!$cat_id) {exit($a_langpackage->a_error);}

//数据表定义区
$t_category = $tablePreStr."category";
$t_brand_category = $tablePreStr."brand_category";
$t_brand = $tablePreStr."brand";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
//删除原来的对应关系，插入新的对应关系
$sql="DELETE FROM $t_brand_category WHERE cat_id ='$cat_id'";
$dbo->exeUpdate($sql);
if (!empty($cat_id_list)) {
	$err_no=0;
	foreach ($cat_id_list as $value){
		$info = array("cat_id"=>$cat_id,"brand_id"=>$value);
		if (!$dbo->createbyarr($info,$t_brand_category)) {
			$err_no++;
		}
	}
	if ($err_no) {
		echo "关联分类出错！";
	}
}
if(update_category_info($dbo,$t_category,$post,$cat_id)) {
	admin_log($dbo,$t_admin_log,"编辑分类：$cat_id");
	action_return(1,$a_langpackage->a_amend_suc,'m.php?app=goods_category_edit&id='.$cat_id);
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>