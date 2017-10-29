<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");

require("foundation/module_shop.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_info = $tablePreStr."shop_info";

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

$shop_info = get_shop_info($dbo,$t_shop_info,$shop_id);
?>