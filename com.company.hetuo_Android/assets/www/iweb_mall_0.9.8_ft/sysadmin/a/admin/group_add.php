<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* post 数据处理 */
$id = intval(get_args('id'));
$value = get_args('value');
//echo $value;
if(!$id) {
	exit();
}

//数据表定义区
$t_admin_group=$tablePreStr."admin_group";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_admin_group`  set group_name='$value' where id='$id'";

if($dbo->exeUpdate($sql)) {
	echo 1;
}
?>