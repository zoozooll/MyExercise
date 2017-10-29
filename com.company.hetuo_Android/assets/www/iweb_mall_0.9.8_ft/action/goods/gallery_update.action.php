<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_goods.php");
require("foundation/module_gallery.php");
require("foundation/module_img_size.php");
require("foundation/module_shop.php");

//语言包引入
$m_langpackage=new moduleslp;

$goods_id = intval(get_args('goods_id'));
if(!$goods_id) {
	exit($m_langpackage->m_handle_err);
}


//定义文件表
$t_goods = $tablePreStr."goods";
$t_goods_gallery = $tablePreStr."goods_gallery";
$t_shop_info=$tablePreStr."shop_info";
$t_img_size=$tablePreStr."img_size";


//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();

// 判断 goods_id 是否为 本店铺下的产品id;
$goods_info = get_goods_info($dbo,$t_goods,'goods_id',$goods_id,$shop_id);
if(empty($goods_info)) {
	exit($m_langpackage->m_handle_err);
}

$row=get_shop_info($dbo,$t_shop_info,$shop_id);

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

// 更新老图片的描述
$old_img_desc = array();
if(get_args('old_img_desc')) {
	foreach(get_args('old_img_desc') as $k=>$v) {
		$old_img_desc[$k] = short_check($v);
	}
}
if(!empty($old_img_desc)) {
	update_gallery_desc($dbo,$t_goods_gallery,$goods_id,$old_img_desc);
}

// 图片上传处理
$cupload = new upload();
$cupload->set_dir("uploadfiles/goods/","{y}/{m}/{d}");
$setthumb = array(
	'width' => array($SYSINFO['width1'],$SYSINFO['width2']),
	'height' => array($SYSINFO['height1'],$SYSINFO['height2']),
	'name' => array('thumb','m')
);
$cupload->set_thumb($setthumb);
$file = $cupload->execute();

$img_desc = array();
foreach(get_args('img_desc') as $k=>$v) {
	$img_desc[$k] = short_check($v);
}

$img_size=unserialize(get_sess_privilege());
$img_size_k=$img_size['8']*1024*1024;
if($row['count_imgsize']>$img_size_k){
	action_return(0,'您已经上传超过'.$img_size['8'].'M的图片，请清理后继续上传','-1');
}else
if($file) {
	$insert_array = array();
	foreach($file as $k=>$v) {
		if($v['flag']==1) {
			$insert_array[$k]['img_url'] = $v['dir'].$v['m'];
			$insert_array[$k]['thumb_url'] = $v['dir'].$v['thumb'];
			$insert_array[$k]['img_original'] = $v['dir'].$v['name'];
			$insert_array[$k]['img_desc'] = $img_desc[$k];
			
			$post1[$k]['uid']=$user_id;
			$post1[$k]['img_size']=$v['size'];
			$post1[$k]['upl_time'] = $ctime->long_time();
			$post1[$k]['img_url'] = str_replace($webRoot,"",$v['dir']).$v['name'];
		}
	}
	img_size($dbo,$t_img_size,$post1,$t_shop_info,$row['count_imgsize'],$shop_id);
	insert_gallery_info($dbo,$t_goods_gallery,$goods_id,$insert_array);
}

action_return(1,$m_langpackage->m_goodsimg_updatesuccess,'modules.php?app=goods_gallery&id='.$goods_id);

?>