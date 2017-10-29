<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_goods.php");
require("foundation/module_honor.php");

//语言包引入
$m_langpackage=new moduleslp;

$t_shop_honor = $tablePreStr."shop_honor";

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
	update_honor_desc($dbo,$t_shop_honor,$shop_id,$old_img_desc);
}

// 图片上传处理
$cupload = new upload();
$cupload->set_dir("uploadfiles/","shop/honor/$shop_id");
$setthumb = array(
	'width' => array('100','200'),
	'height' => array('130','260'),
	'name' => array('thumb','m')
);
$cupload->set_thumb($setthumb);
$file = $cupload->execute();

$img_desc = array();
foreach(get_args('img_desc') as $k=>$v) {
	$img_desc[$k] = short_check($v);
}

if($file) {
	$insert_array = array();
	foreach($file as $k=>$v) {
		if($v['flag']==1) {
			$insert_array[$k]['honor_img'] = $v['dir'].$v['m'];
			$insert_array[$k]['honor_thumb'] = $v['dir'].$v['thumb'];
			$insert_array[$k]['honor_original'] = $v['dir'].$v['name'];
			$insert_array[$k]['honor_desc'] = $img_desc[$k];
		}
	}

	insert_honor_info($dbo,$t_shop_honor,$shop_id,$insert_array);
}

action_return(1,$m_langpackage->m_honor_updatesuccess,'modules.php?app=shop_honor');

?>