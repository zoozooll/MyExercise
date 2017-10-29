<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_brand.php");
include('../foundation/module_admin_logs.php');
//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("brand_update");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');	
}

//数据表定义区
$t_brand = $tablePreStr."brand";
$t_brand_category = $tablePreStr."brand_category";
$t_admin_log = $tablePreStr."admin_log";
/* post 数据处理 */
$post['brand_name'] = short_check(get_args('brand_name'));
$post['brand_desc'] = long_check(get_args('brand_desc'));
$post['site_url'] = short_check(get_args('site_url'));
$post['is_show'] = intval(get_args('is_show'));
$id = intval(get_args('brand_id'));

$brand_id = intval(get_args('brand_id'));
if(!$brand_id) {exit($a_langpackage->a_error);}
$cat_id_list = get_args("cat_id");
if(empty($post['brand_name'])) {
	action_return(0,$a_langpackage->a_brand_null,'-1');
	exit;
}

$upload_1 = new upload('jpg|gif|png',1024,'logo');
$upload_1->set_dir("../uploadfiles/","brand/{y}/{m}/{d}");
$file_1 = $upload_1->execute();
if (count($upload_1)>1) {
	if($file_1[0]['flag']==1) {
		//数据表定义
		$t_brand=$tablePreStr."brand";
		
		//读写分离定义方法
		$dbo = new dbex;
		dbtarget('r',$dbServs);
		$sql="select brand_logo from $t_brand where brand_id=$id";
	//	echo $sql;
		$brand_row = $dbo->getRow($sql);
	//	$logo_dir = $brand_row['brand_logo'];
		$logo_dir = str_replace('./','../',$brand_row['brand_logo']);
		@unlink($logo_dir);
		
		$dir = str_replace('../','./',$file_1[0]['dir']);
		$post['brand_logo'] = $dir.$file_1[0]['name'];
	}
}
//数据表定义区
$t_brand = $tablePreStr."brand";
$t_brand_category = $tablePreStr."brand_category";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

//删除原来的对应关系，插入新的对应关系
$sql="DELETE FROM $t_brand_category WHERE brand_id ='$brand_id'";
$dbo->exeUpdate($sql);
if (!empty($cat_id_list)) {
	$err_no=0;
	foreach ($cat_id_list as $value){
		$info = array("cat_id"=>$value,"brand_id"=>$brand_id);
		if (!$dbo->createbyarr($info,$t_brand_category)) {
			$err_no++;
		}
	}
	if ($err_no) {
		echo "关联分类出错！";
	}
}

if(update_brand_info($dbo,$t_brand,$post,$brand_id)) {
	admin_log($dbo,$t_admin_log,"修改品牌：$brand_id");
	action_return(1,$a_langpackage->a_amend_suc,'m.php?app=goods_brand_edit&id='.$brand_id);
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>