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
$t_shop_category=$tablePreStr."shop_category";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_shop_category` set sort_order='$v' where shop_cat_id='$id' and shop_id='$shop_id'";
//echo $sql;
if($dbo->exeUpdate($sql)) {
	echo '1';
}
?>