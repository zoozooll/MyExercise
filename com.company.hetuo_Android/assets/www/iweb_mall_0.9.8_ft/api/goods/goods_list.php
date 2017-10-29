<?php
include(dirname(__file__)."/../includes.php");

function goods_list_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_goods=$tablePreStr."goods";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');
	
	$sql=" select $fields from $t_goods ";
	$sql.=" where is_delete=1 and is_on_sale=1 and lock_flg=0";
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
//新品 热销 精品 特价 最新上架
function goods_list_by_terms($fields="*",$condition="",$by_col="",$order=""){
	$fields=filt_fields($fields);
	if($condition!=''){
		$condition=" and $condition=1";
	}
	return goods_list_read_base($fields,$condition,$by_col,$order);
}
//店铺商品列表
function goods_list_by_shopid($fields="*",$id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($id);
	if($id_str!=''){
		$condition=" and shop_id in ($id_str) ";
	}
	return goods_list_read_base($fields,$condition,$by_col,$order);
}
//商品信息
function goods_list_by_goodsid($fields="*",$id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($id);
	if($id_str!=""){
		$condition=" and goods_id in ($id_str) ";
	}
	return goods_list_read_base($fields,$condition,$by_col,$order);
}
?>