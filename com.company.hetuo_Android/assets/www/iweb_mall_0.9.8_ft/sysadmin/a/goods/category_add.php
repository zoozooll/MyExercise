<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_category.php");
require_once("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("cat_add");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');	
}

/* post 数据处理 */
$post['cat_name'] = short_check(get_args('cat_name'));
$post['parent_id'] = intval(get_args('parent_id'));
$post['sort_order'] = intval(get_args('sort_order'));
$brand_id_list = get_args("brand_id");
if(empty($post['cat_name'])) {
	action_return(0,$a_langpackage->a_class_null,'-1');
	exit;
}

//数据表定义区
$t_category = $tablePreStr."category";
$t_brand_category = $tablePreStr."brand_category";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$insert_id = insert_category_info($dbo,$t_category,$post);
if (is_array($brand_id_list)) {
	foreach ($brand_id_list as $value){
		$info = array("brand_id"=>$value,"cat_id"=>$insert_id);
		$dbo->createbyarr($info,$t_brand_category);
	}
}
if($insert_id) {
	admin_log($dbo,$t_admin_log,"新增分类：$insert_id");
	action_return(1,$a_langpackage->a_add_suc);
} else {
	action_return(0,$a_langpackage->a_add_lose,'-1');
}
?>