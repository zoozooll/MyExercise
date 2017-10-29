<?php
include(dirname(__file__)."/../includes.php");

function groupbuy_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_groupbuy = $tablePreStr."groupbuy";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');

	$sql=" select $fields from $t_groupbuy ";
	$sql.=" where 1=1 ";
	if($condition!=''){
		$sql.=" $condition ";
	}
	if($by_col!=''){
		$sql.=" order by $by_col $order ";
	}
	if($num==''||$num >= 1000){
		$sql.=" limit 1000 ";
	}else{
		$sql.=" limit $num ";
	}
//	if($cache==1 && in_array($num,array(10,20,50)) && $cache_key!=''){
//		$key=$cache_key.$order.'_'.$num;
//		$key_mt=$cache_key.'mt_'.$order.'_'.$num;
//		$plugin_rs=model_cache($key,$key_mt,$dbo,$sql);
//	}
	if(empty($plugin_rs)){
		$plugin_rs=$dbo->getRs($sql);
	}
	return $plugin_rs;
}
//取得团购列表
function groupbuy_by_all($fields="*",$by_col="",$order=""){
	$fields=filt_fields($fields);
	$condition='';
	return groupbuy_read_base($fields,$condition,$by_col,$order);
}
//安groupid取团购
function groupbuy_by_groupid($fields="*",$group_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($group_id);
	if($id_str!=''){
		$condition=" and group_id in($id_str) ";
	}
	return groupbuy_read_base($fields,$condition,$by_col,$order);
}
//安shopid取团购
function groupbuy_by_shopid($fields="*",$shop_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($shop_id);
	if($id_str!=''){
		$condition=" and shop_id in($id_str) ";
	}
	return groupbuy_read_base($fields,$condition,$by_col,$order);
}
?>