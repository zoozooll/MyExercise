<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_shop.php");
//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_shop_request = $tablePreStr."shop_request";

// 处理post变量
$post['company_name'] = short_check(get_args('company_name'));
$post['person_name'] = short_check(get_args('person_name'));
$post['credit_type'] = short_check(get_args('credit_type'));
$post['credit_num'] = short_check(get_args('credit_num'));
$post['company_area'] = short_check(get_args('company_area'));
$post['company_address'] = short_check(get_args('company_address'));
$post['zipcode'] = short_check(get_args('zipcode'));
$post['mobile'] = short_check(get_args('mobile'));
$post['telphone'] = short_check(get_args('telphone'));
$post['user_id'] = intval(get_args('user_id'));

$post['add_time'] = $ctime->long_time();

$request_id = intval(get_args('request_id'));

// 图片上传处理
$cupload = new upload();
$cupload->set_dir("uploadfiles/","shop/request/$user_id");
$file = $cupload->execute();
if($file) {
	$post['credit_commercial'] = $file[0]['dir'].$file[0]['name'];
}

if($request_id) {
	$post['status'] = 0;
	$item_sql = get_update_item($post);
	$sql = "update `$t_shop_request` set $item_sql where request_id='$request_id'";
} else {
	$item_sql = get_insert_item($post);
	$sql = "insert `$t_shop_request` $item_sql";
}

if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_put_suc);
} else {
	action_return(0,$m_langpackage->m_put_lose,'-1');
}
exit;
?>