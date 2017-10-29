<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");

require("foundation/module_payment.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_payment = $tablePreStr."payment";
$t_shop_payment = $tablePreStr."shop_payment";

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

$payment = get_payment_info($dbo,$t_payment,1);

$info = get_shop_payment_info($dbo,$t_shop_payment,$shop_id);
?>