<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* post 数据处理 */
$id = intval(get_args('id'));
$v = intval(get_args('v'));

if(!$id) {
	exit();
}


//数据表定义区
$t_cart = $tablePreStr."cart";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_cart` set goods_number='$v' where cart_id='$id'";
//echo $sql;
if($dbo->exeUpdate($sql)) {
	echo '1';
}
?>