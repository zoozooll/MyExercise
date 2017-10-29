<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;

$payid = intval(get_args('id'));
if(!$payid) { exit("非法操作"); }

//数据表定义区
$t_order_info = $tablePreStr."order_info";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);


?>