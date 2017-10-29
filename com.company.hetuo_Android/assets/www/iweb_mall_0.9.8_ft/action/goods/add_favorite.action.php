<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

if(!$user_id) {
	exit("-2");
}

/* post 数据处理 */
$goods_id = intval(get_args('id'));

if(!$goods_id) {
	exit();
}

//数据表定义区
$t_user_favorite = $tablePreStr."user_favorite";
$t_goods = $tablePreStr."goods";

//定义写操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select * from `$t_user_favorite` where goods_id='$goods_id' and user_id='$user_id'";
$row = $dbo->getRow($sql);
if($row) {
	exit('-1');
}

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$add_time = $ctime->long_time();

$sql = "insert into `$t_user_favorite` (user_id,goods_id,add_time) values ('$user_id','$goods_id','$add_time');";
if($dbo->exeUpdate($sql)) {
	$dbo->exeUpdate("update `$t_goods` set favpv=favpv+1 where goods_id='$goods_id'");
	echo "1";
}
?>