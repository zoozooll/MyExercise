<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

/* post 数据处理 */
$id = intval(get_args('id'));
if(!$id){
	$key_id = get_args('searchkey');
	if (!$key_id){
		echo "<script language='javascript'> alert('".$a_langpackage->a_need_select_one."'); history.go(-1);</script>";
		exit;
	}
	$id=implode(',',$key_id);
}
//权限管理
$right=check_rights("keyword_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

//数据表定义区
$t_keywords_count = $tablePreStr."keywords_count";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "delete from $t_keywords_count where id in($id)";

if($dbo->exeUpdate($sql)) {
	/** 添加log */
	$admin_log ="删除关键字";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_del_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}

?>