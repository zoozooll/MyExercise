<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");

require("foundation/module_shop.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_category = $tablePreStr."shop_category";

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);
$category_list = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$category_list_new = array();
if(!empty($category_list)) {
	foreach($category_list as $v) {
		$category_list_new[$v['shop_cat_id']]['shop_cat_id'] = $v['shop_cat_id'];
		$category_list_new[$v['shop_cat_id']]['shop_cat_name'] = $v['shop_cat_name'];
		$category_list_new[$v['shop_cat_id']]['parent_id'] = $v['parent_id'];
		$category_list_new[$v['shop_cat_id']]['shop_cat_unit'] = $v['shop_cat_unit'];
		$category_list_new[$v['shop_cat_id']]['sort_order'] = $v['sort_order'];
	}
}
unset($category_list);

function get_sub_category ($category_list,$parent_id) {
	$array = array();
	foreach($category_list as $k=>$v) {
		if($v['parent_id']==$parent_id) {
			$array[$k] = $v;
		}
	}
	return $array;
}

/* 初始化数据 */
$shop_category_info = array(
	'shop_cat_name'	=> '',
	'parent_id'		=> intval(get_args('id')),
	'shop_cat_unit'	=> '',
	'sort_order'	=> 0
);


?>