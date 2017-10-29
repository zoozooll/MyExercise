<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_shop.php");

//语言包引入
$m_langpackage=new moduleslp;
//print_r($_POST);
//exit;
//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_shop_info = $tablePreStr."shop_info";

// 处理post变量
$post['shop_name'] = short_check(get_args('shop_name'));
$post['shop_address'] = long_check(get_args('shop_address'));
$post['shop_template'] = short_check(get_args('shop_template'));
$post['shop_country'] = intval(get_args('country'));
$post['shop_province'] = intval(get_args('province'));
$post['shop_city'] = intval(get_args('city'));
$post['shop_district'] = intval(get_args('district'));
$post['shop_intro'] = big_check(get_args('shop_intro'));
$post['shop_management'] = short_check(get_args('shop_management'));
$post['map_x'] = short_check(get_args('now_x'));
$post['map_y'] = short_check(get_args('now_y'));
$post['map_zoom'] = short_check(get_args('now_zoom'));
$shop_id = intval(get_args('shop_id'));

$upload_1 = new upload('jpg|gif|png',1024,'attach_images');
$upload_1->set_dir("uploadfiles/","shop/{y}/{m}/{d}");
$file_1 = $upload_1->execute();
if($file_1 && $file_1[0]['flag']==1) {
	$post['shop_images'] = $file_1[0]['dir'].$file_1[0]['name'];
}

$upload_2 = new upload('jpg|gif|png',1024,'attach_logo');
$upload_2->set_dir("uploadfiles/","shop/{y}/{m}/{d}");
$file_2 = $upload_2->execute();
if($file_2 && $file_2[0]['flag']==1) {
	$post['shop_logo'] = $file_2[0]['dir'].$file_2[0]['name'];
}

$upload_3 = new upload('jpg|gif|png',1024,'attach_template');
$upload_3->set_dir("uploadfiles/","shop/{y}/{m}/{d}");
$file_3 = $upload_3->execute();
if($file_3 && $file_3[0]['flag']==1) {
	$post['shop_template_img'] = $file_3[0]['dir'].$file_3[0]['name'];
}

if(update_shop_info($dbo,$t_shop_info,$post,$shop_id)) {
	action_return(1,$m_langpackage->m_edit_success);
} else {
	action_return(0,$m_langpackage->m_edit_fail,'-1');
}
exit;
?>