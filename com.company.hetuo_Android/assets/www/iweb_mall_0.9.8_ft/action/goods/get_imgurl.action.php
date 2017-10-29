<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require_once("foundation/fsqlitem_set.php");

/* post 数据处理 */
$goods_id = intval(get_args('id'));

if(!$goods_id) {
	exit();
}

//数据表定义区
$t_goods_gallery = $tablePreStr."goods_gallery";

//定义写操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select * from `$t_goods_gallery` where goods_id='$goods_id' and is_set='1'";
$row = $dbo->getRow($sql);
if($row) {
	exit($row['img_url']);
}
?>