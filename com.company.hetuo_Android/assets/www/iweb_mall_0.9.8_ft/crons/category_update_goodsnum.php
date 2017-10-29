<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//数据表定义区
$t_goods = $tablePreStr."goods";
$t_category = $tablePreStr."category";
//定义操作
dbtarget('r',$dbServs);
$dbo = new dbex;
$sql = "select goods_id,cat_id from `$t_goods` where is_on_sale=1";
$goods_r = $dbo->getRs($sql);
$sql_category = "select * from `$t_category`";
$result = $dbo->getRs($sql_category);
$cat_info = array();
foreach($result as $v) {
	$cat_info[$v['cat_id']] = $v;
	$array[$v['cat_id']] = 0;
}
foreach($goods_r as $value) {
	$array[$value['cat_id']]++; 
	// 二级分类
	if(isset($cat_info[$value['cat_id']]['parent_id']) && $cat_info[$value['cat_id']]['parent_id']>0) {
		$array[$cat_info[$value['cat_id']]['parent_id']]++;
	}
	// 三级分类
	if(isset($cat_info[$cat_info[$value['cat_id']]['parent_id']]['parent_id']) && $cat_info[$cat_info[$value['cat_id']]['parent_id']]['parent_id']>0) {
		$array[$cat_info[$cat_info[$value['cat_id']]['parent_id']]['parent_id']]++;
	}
	// 四级分类
	if(isset($cat_info[$cat_info[$cat_info[$value['cat_id']]['parent_id']]['parent_id']]['parent_id']) && $cat_info[$cat_info[$cat_info[$value['cat_id']]['parent_id']]['parent_id']]['parent_id']>0) {
		$array[$cat_info[$cat_info[$cat_info[$value['cat_id']]['parent_id']]['parent_id']]['parent_id']]++;
	}
	// ..
}
//定义写操作
dbtarget('w',$dbServs);
$dbo = new dbex;
foreach($cat_info as $v){
	if($v['goods_num'] != $array[$v['cat_id']]) {
		$sql = "update `$t_category` set goods_num='".$array[$v['cat_id']]."' where cat_id='".$v['cat_id']."'";
		$dbo->exeUpdate($sql);
	}
}
?>