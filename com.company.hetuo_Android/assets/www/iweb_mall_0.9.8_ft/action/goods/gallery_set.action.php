<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_goods.php");
require("foundation/module_gallery.php");

//语言包引入
$m_langpackage=new moduleslp;

$img_id = intval(get_args('id'));
$goods_id = intval(get_args('gid'));

if(!$goods_id || !$img_id) {
	exit($m_langpackage->m_handle_err);
}


//定义文件表
$t_goods = $tablePreStr."goods";
$t_goods_gallery = $tablePreStr."goods_gallery";

//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();

// 判断 goods_id 是否为 本店铺下的产品id;
$goods_info = get_goods_info($dbo,$t_goods,'goods_id',$goods_id,$shop_id);
if(empty($goods_info)) {
	exit($m_langpackage->m_handle_err);
}

// 获取需要设置成首图的img信息
$sql = "select * from `$t_goods_gallery` where goods_id='$goods_id' and img_id='$img_id'";
if($result = $dbo->getRow($sql)) {
	$goods_thumb = $result['thumb_url'];
} else {
	exit($m_langpackage->m_handle_err);
}

// 获取老的已设为首图的img_id
$sql = "select img_id from `$t_goods_gallery` where goods_id='$goods_id' and is_set='1'";
if($result = $dbo->getRow($sql)) {
	$old_set_img_id = $result['img_id'];
}

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$update = array('is_set'=>'1');
if(update_gallery_info($dbo,$t_goods_gallery,$update,$goods_id,$img_id)) {
	$update_items = array('is_set_image'=>'1','goods_thumb'=>$goods_thumb,'last_update_time'=>$ctime->long_time());
	update_goods_info($dbo,$t_goods,$update_items,$goods_id,$shop_id);
	if($old_set_img_id) {
		update_gallery_info($dbo,$t_goods_gallery,array('is_set'=>'0'),$goods_id,$old_set_img_id);
	}
	action_return(1,$m_langpackage->m_fgoodsimg_setsuccess,'modules.php?app=goods_gallery&id='.$goods_id);
} else {
	action_return(0,$m_langpackage->m_fgoodsimg_setfail,'modules.php?app=goods_gallery&id='.$goods_id);
}
?>