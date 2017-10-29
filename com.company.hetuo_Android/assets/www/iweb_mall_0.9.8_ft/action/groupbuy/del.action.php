<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require 'foundation/module_groupbuy.php';

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_groupbuy = $tablePreStr."groupbuy";
$t_groupbuy_log = $tablePreStr."groupbuy_log";

/* post 数据处理 */
$group_id = intval(get_args('id'));

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$suc=del_groupbuy($dbo,$t_groupbuy,$group_id);

if($suc) {
	$suc=del_groupbuy($dbo,$t_groupbuy_log,$group_id);
	if($suc){
		action_return(1,$m_langpackage->m_edit_success,'-1');
	}else {
		action_return(0,$m_langpackage->m_edit_fail,'-1');
	}
} else {
	action_return(0,$m_langpackage->m_edit_fail,'-1');
}

exit;
?>