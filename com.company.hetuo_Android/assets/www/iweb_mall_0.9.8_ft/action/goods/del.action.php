<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_goods.php");

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_goods = $tablePreStr."goods";

$id = intval(get_args('id'));

if(delete_goods($dbo,$t_goods,$id,$shop_id)) {
	action_return(1,$m_langpackage->m_delgoods_success);
} else {
	action_return(0,$m_langpackage->m_delgoods_fail,'-1');
}
exit;
?>