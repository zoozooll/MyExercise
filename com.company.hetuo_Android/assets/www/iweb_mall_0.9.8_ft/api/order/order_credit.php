<?php
include(dirname(__file__)."/../includes.php");

function order_credit_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_goods = $tablePreStr."goods";
	$t_credit = $tablePreStr."credit";
	$t_user = $tablePreStr."users";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');
	
	
	$sql=" select $fields from $t_credit as a,$t_goods as b,$t_user as c ";
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
//来自卖家的评价
function order_credit_by_seller($user_id,$by_col="",$order=""){
	$fields="a.*,b.goods_name,b.goods_price,c.user_name";
	$id_str=filt_num_array($user_id);
	if($id_str!=''){
		$condition="and a.buyer in($id_str) and b.goods_id=a.goods_id and c.user_id=a.seller ";
	}
	return order_credit_read_base($fields,$condition,$by_col,$order);
}
//来自买家的评价
function order_credit_by_buyer($user_id,$by_col="",$order=""){
	$fields="a.*,b.goods_name,b.goods_price,c.user_name";
	$id_str=filt_num_array($user_id);
	if($id_str!=''){
		$condition="and a.seller in($id_str) and b.goods_id=a.goods_id and c.user_id=a.buyer ";
	}
	return order_credit_read_base($fields,$condition,$by_col,$order);
}
//我对他人的评价
function order_credit_by_my($user_id,$by_col="",$order=""){
	$fields="a.*,b.goods_name,b.goods_price,c.user_name";
	$id_str=filt_num_array($user_id);
	if($id_str!=''){
		$condition="and (a.buyer in($id_str) and b.goods_id=a.goods_id and c.user_id=a.seller) or (a.seller in($id_str) and b.goods_id=a.goods_id and c.user_id=a.buyer)";
	}
	return order_credit_read_base($fields,$condition,$by_col,$order);
}
?>