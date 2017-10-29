<?php
include(dirname(__file__)."/../includes.php");

function order_info_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_order_info = $tablePreStr."order_info";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');
	
	$sql=" select $fields from $t_order_info ";
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
//订单信息
function order_info_by_orderid($fields="*",$order_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($order_id);
	if($id_str!=''){
		$condition="and order_id in($id_str) ";
	}
	return order_info_read_base($fields,$condition,$by_col,$order);
}
//店铺订单列表（作为卖家）
function order_info_by_shopid($fields="*",$shop_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($shop_id);
	if($id_str!=''){
		$condition="and shop_id in($id_str) ";
	}
	return order_info_read_base($fields,$condition,$by_col,$order);
}
//用户订单列表（作为买家）
function order_info_by_uid($fields="*",$user_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($user_id);
	if($id_str!=''){
		$condition="and user_id in($id_str) ";
	}
	return order_info_read_base($fields,$condition,$by_col,$order);
}
?>