<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* post 数据处理 */
$id = intval(get_args('id'));
$s = intval(get_args('s'));

if(!$id) {
	exit();
}

//数据表定义区
$t_article=$tablePreStr."article";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_article`  set is_show='$s' where article_id='$id'";
if($dbo->exeUpdate($sql)) {
	echo $s ? 'yes' : 'no';
}
?>