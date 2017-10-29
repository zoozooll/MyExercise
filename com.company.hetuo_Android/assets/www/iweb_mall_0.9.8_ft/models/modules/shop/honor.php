<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_honor.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_honor = $tablePreStr."shop_honor";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$honor_list = get_honor_list($dbo,$t_shop_honor,$shop_id);

?>