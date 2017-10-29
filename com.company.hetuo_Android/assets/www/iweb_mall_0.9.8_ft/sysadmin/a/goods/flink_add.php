<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_brand.php");
require("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("brand_add");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* post 数据处理 */
$post['brand_name'] = short_check(get_args('brand_name'));
$post['brand_desc'] = long_check(get_args('brand_desc'));
$post['site_url'] = short_check(get_args('site_url'));
$post['is_show'] = intval(get_args('is_show'));

//权限管理
$right=check_rights("flink_add");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

if(empty($post['brand_name'])) {
	action_return(0,$a_langpackage->a_brand_null,'-1');
	exit;
}

$upload_1 = new upload('jpg|gif|png',1024,'logo');
$upload_1->set_dir("../uploadfiles/","flink/{y}/{m}/{d}");
$file_1 = $upload_1->execute();
if (count($file_1)>0) {
	if($file_1[0]['flag']==1) {
		$dir = str_replace('../','./',$file_1[0]['dir']);
		$post['brand_logo'] = $dir.$file_1[0]['name'];
	}
}
//数据表定义区
$t_brand = $tablePreStr."flink";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$type_id = insert_brand_info($dbo,$t_brand,$post);

if($type_id) {
	/** 添加log */
	$admin_log ="添加友情链接";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_add_suc);
} else {
	action_return(0,$a_langpackage->a_add_lose,'-1');
}
?>