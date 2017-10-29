<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("foundation/module_credit.php");

$t_credit = $tablePreStr."credit";
$t_integral = $tablePreStr."integral";

/* 处理商铺自定义分类 */
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

//获取商家信用值
$credit=get_credit($dbo,$t_credit,$shop_id);
$credit['SUM(seller_credit)']=intval($credit['SUM(seller_credit)']);
$integral=get_integral($dbo,$t_integral,$credit['SUM(seller_credit)']);
?>