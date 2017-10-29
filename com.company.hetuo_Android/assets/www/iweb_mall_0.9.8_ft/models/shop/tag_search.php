<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* 处理系统分类 */
$t_category = 'category';

$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result_search = $dbo->getRs($sql_category);
$CATEGORY = array();
foreach($result_search as $v) {
	$CATEGORY[$v['parent_id']][$v['cat_id']] = $v;
}
?>