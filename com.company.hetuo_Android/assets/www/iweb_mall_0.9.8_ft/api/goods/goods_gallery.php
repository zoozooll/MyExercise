<?php
include(dirname(__file__)."/../includes.php");

function goods_gallery_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_goods_gallery=$tablePreStr."goods_gallery";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');
	
	
	$sql=" select $fields from $t_goods_gallery ";
	$sql.=" where 1=1";
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
//商品图片列表
function goods_gallery_list_by_goodsid($fields="*",$goods_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($goods_id);
	if($id_str!=''){
		$condition=" and goods_id in($id_str)";
	}
	return goods_gallery_read_base($fields,$condition,$by_col,$order);
}

?>