<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

/* post 数据处理 */
$post['shop_cat_name'] = short_check(get_args('name'));
$post['parent_id'] = 0;
$post['shop_cat_unit'] = '';
$post['sort_order'] = '0';
$post['shop_id'] = $shop_id;

if(empty($post['shop_cat_name'])) {
	exit("-1");
}

//数据表定义区
$t_shop_category = $tablePreStr."shop_category";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$item_sql = get_insert_item($post);
$sql = "insert `$t_shop_category` $item_sql";

if($dbo->exeUpdate($sql)) {
	echo mysql_insert_id();
	exit();
} else {
	exit("-1");
}
?>