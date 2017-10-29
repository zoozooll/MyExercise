<?php
include(dirname(__file__)."/../includes.php");

function shop_guestbook_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_shop_guestbook=$tablePreStr."shop_guestbook";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');
	
	$sql=" select $fields from $t_shop_guestbook ";
	$sql.=" where shop_del_status=1 and user_del_status=1 ";
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
//店铺得到的留言
function shop_guestbook_by_shopid($fields="*",$shop_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($shop_id);
	if($id_str!=""){
		$condition=" and shop_id in ($id_str) ";
	}
	return shop_guestbook_read_base($fields,$condition,$by_col,$order);
}
//给出的留言
function shop_guestbook_by_uid($fields="*",$user_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($user_id);
	if($id_str!=""){
		$condition=" and user_id in ($id_str) ";
	}
	return shop_guestbook_read_base($fields,$condition,$by_col,$order);
}
?>