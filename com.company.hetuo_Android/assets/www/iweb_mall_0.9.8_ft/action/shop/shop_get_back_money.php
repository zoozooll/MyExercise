<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入模块公共方法文件
require("foundation/module_shop.php");
//语言包引入
$m_langpackage=new moduleslp;
//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_shop_request = $tablePreStr."shop_request";

// 处理post变量

?>