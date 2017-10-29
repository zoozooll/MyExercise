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

//定义文件表
$t_goods = $tablePreStr."goods";
$t_goods_attr = $tablePreStr."goods_attr";
$t_goods_gallery = $tablePreStr."goods_gallery";
$t_shop_info=$tablePreStr."shop_info";
$t_img_size=$tablePreStr."img_size";

$post = array(
	'goods_name'	=> short_check(get_args('goods_name')),
	'cat_id'		=> intval(get_args('cat_id')),
	'ucat_id'		=> intval(get_args('ucat_id')),
	'brand_id'		=> intval(get_args('brand_id')),
	'goods_intro'	=> big_check(get_args('goods_intro')),
	'goods_wholesale'=> big_check(get_args('goods_wholesale')),
	'goods_number'=> intval(get_args('goods_number')),
	'keyword'		=> short_check(get_args('keyword')),
	'goods_price'	=> floatval(get_args('goods_price')),
	'transport_price'	=> floatval(get_args('transport_price')),
	'is_on_sale'	=> intval(get_args('is_on_sale')),
	'is_best'		=> intval(get_args('is_best')),
	'is_new'		=> intval(get_args('is_new')),
	'is_hot'		=> intval(get_args('is_hot')),
	'is_promote'	=> intval(get_args('is_promote')),
	'type_id'		=> intval(get_args('type_id')),
	'is_transport_template'=>intval(get_args("is_transport_template")),
	'transport_template_id'=>intval(get_args("transport_template_id")),
);
$post['shop_id'] = $shop_id;
$post['add_time'] = $ctime->long_time();

/* 属性处理 */
$post_attr = get_args('attr');

/* 图片上传处理 */
$cupload = new upload();
$cupload->set_dir("uploadfiles/goods/","{y}/{m}/{d}");
$setthumb = array(
	'width' => array($SYSINFO['width1'],$SYSINFO['width2']),
	'height' => array($SYSINFO['height1'],$SYSINFO['height2']),
	'name' => array('thumb','m')
);
$cupload->set_thumb($setthumb);
$file = $cupload->execute();

//数据库操作
$dbo=new dbex();
dbtarget('r',$dbServs);

$row=get_shop_info($dbo,$t_shop_info,$shop_id);
$img_size['8']='';
$img_size=unserialize(get_sess_privilege());
$img_size_k=$img_size['8']*1024*1024;
if($row['count_imgsize']>$img_size_k){
	action_return(0,'您已经上传超过'.$img_size['8'].'M的图片，请清理后继续上传','-1');
}else
if(count($file)) {
	$insert_array = array();
	foreach($file as $k=>$v) {
		if($v['flag']==1) {
			$insert_array[$k]['img_url'] = $v['dir'].$v['m'];
			$insert_array[$k]['thumb_url'] = $v['dir'].$v['thumb'];
			$insert_array[$k]['img_original'] = $v['dir'].$v['name'];
			$insert_array[$k]['is_set'] = '1';
			$post['goods_thumb'] = $v['dir'].$v['thumb'];
			$post['is_set_image'] = '1';

			$post1[$k]['uid']=$user_id;
			$post1[$k]['img_size']=$v['size'];
			$post1[$k]['upl_time'] = $ctime->long_time();
			$post1[$k]['img_url'] = str_replace($webRoot,"",$v['dir']).$v['name'];
		}
	}
	dbtarget('w',$dbServs);

	img_size($dbo,$t_img_size,$post1,$t_shop_info,$row['count_imgsize'],$shop_id);
}

if($post['is_best']==1 || $post['is_new']==1 || $post['is_hot']==1 || $post['is_promote']==1){
	$is_best=0;
	$is_promote=0;
	$is_new=0;
	$is_hot=0;

	//数据库操作
	dbtarget('r',$dbServs);

	$rs=get_isname_num($dbo,$t_goods,$shop_id);
	foreach ($rs as $val){
		if(isset($val['goods_id'])){
			if($val['is_best']==1){
				$is_best++;
			}
			if($val['is_promote']==1){
				$is_promote++;
			}
			if($val['is_new']==1){
				$is_new++;
			}
			if($val['is_hot']==1){
				$is_hot++;
			}
		}
	}

	if($is_best>=$user_privilege[4]){
		$post['is_best']=0;
	}
	if($is_promote>=$user_privilege[5]){
		$post['is_promote']=0;
	}
	if($is_new>=$user_privilege[6]){
		$post['is_new']=0;
	}
	if($is_hot>=$user_privilege[7]){
		$post['is_hot']=0;
	}
}
//数据库操作
dbtarget('w',$dbServs);

if($goods_id = insert_goods_info($dbo,$t_goods,$post)) {
	insert_goods_attr($dbo,$t_goods_attr,$post_attr,$goods_id);
	if(count($file)) {
		insert_gallery_info($dbo,$t_goods_gallery,$goods_id,$insert_array);
	}
	action_return(1,$m_langpackage->m_addgoods_success);
} else {
	action_return(0,$m_langpackage->m_addgoods_fail,'-1');
}
exit;
?>