<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* post 数据处理 */
$cat_id = intval(get_args('cat_id'));

if(!$cat_id) {
	exit("-1");
}

require("foundation/module_category.php");
//数据表定义区
$t_category = $tablePreStr."category";
//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$category_list = get_sub_category($dbo,$t_category,$cat_id);
if(!$category_list) {
	exit("-1");
}

$return_array = array();
$i = 0;
foreach($category_list as $value) {
	$return_array[$i]['cat_id'] = $value['cat_id'];
	$return_array[$i]['cat_name'] = $value['cat_name'];
	$i++;
}

echo json_encode($return_array);
?>