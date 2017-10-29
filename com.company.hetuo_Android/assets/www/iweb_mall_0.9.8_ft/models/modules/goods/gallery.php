<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_goods.php");
require("foundation/module_gallery.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_goods_gallery = $tablePreStr."goods_gallery";

$goods_id = intval(get_args('id'));
if(!$goods_id) {
	exit("非法操作！");
}

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

// 判断用户操作的是自己店铺下的商品。
$goods_info = get_goods_info($dbo,$t_goods,'is_set_image',$goods_id,$shop_id);
if(empty($goods_info)) { exit("非法操作！"); }

$gallery_list = get_gallery_list($dbo,$t_goods_gallery,$goods_id);

?>