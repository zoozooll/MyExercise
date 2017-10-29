<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_users.php");

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_user_address = $tablePreStr."user_address";

// 处理post变量
//print_r($_GET);
$address_id = intval(get_args('address_id'));

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$del=del_address($dbo,$t_user_address,$address_id);

if($del) {
	action_return(1,$m_langpackage->m_del_success);
} else {
	action_return(0,$m_langpackage->m_del_fail);
}
exit;
?>