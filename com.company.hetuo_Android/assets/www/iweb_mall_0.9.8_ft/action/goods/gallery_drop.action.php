<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_goods.php");
require("foundation/module_gallery.php");

/* post 数据处理 */
$id = intval(get_args('id'));
$gid = intval(get_args('gid'));
$unset_image = intval(get_args('s'));

if(!$id || !$gid) {
	exit();
}

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_goods_gallery = $tablePreStr."goods_gallery";

//定义写操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$gallery_info = get_gallery_info($dbo,$t_goods_gallery,$gid,$id);
if(empty($gallery_info)) { exit(); }

$goods_info = get_goods_info($dbo,$t_goods,'is_set_image',$gid,$shop_id);
if(empty($goods_info)) { exit(); }

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "delete from  `$t_goods_gallery` where goods_id='$gid' and img_id='$id'";
if($dbo->exeUpdate($sql)) {
	@unlink($gallery_info['img_url']);
	@unlink($gallery_info['thumb_url']);
	@unlink($gallery_info['img_original']);
	echo '1';
	if($unset_image==1) {
		update_goods_info($dbo,$t_goods,array('is_set_image'=>0,'goods_thumb'=>''),$gid,$shop_id);
	}
}
?>