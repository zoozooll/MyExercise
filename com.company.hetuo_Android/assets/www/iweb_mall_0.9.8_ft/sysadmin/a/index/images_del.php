<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_admin_logs.php");
//引入模块公共方法文件

//语言包引入
$a_langpackage=new adminlp;
$right=check_rights("image_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();

//定义文件表
$t_index_images = $tablePreStr."index_images";
$t_admin_log = $tablePreStr."admin_log";

$id=get_args('id');
if(is_array($id)){
	$id=implode(",",$id);
}else{
	$id = intval(get_args('id'));
}


$sql = "select * from `$t_index_images` where id in($id)";
$rs = $dbo->getRs($sql);
if(!$rs) {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();
$sql = "delete from `$t_index_images` where id in($id)";

if($dbo->exeUpdate($sql)) {
	foreach($rs as $val){
		@unlink('../'.$val['images_url']);
	}
	/** 添加log */
	$admin_log ="删除首页轮显图片";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_del_suc);
} else {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}
exit;
?>