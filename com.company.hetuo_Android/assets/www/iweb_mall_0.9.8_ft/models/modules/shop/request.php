<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_request = $tablePreStr."shop_request";


//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_shop_request` where user_id='$user_id'";
$request_info = $dbo->getRow($sql);

?>