<?php
include(dirname(__file__)."/../includes.php");

function complaint_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_complaint = $tablePreStr."complaint";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');

	$sql=" select $fields from $t_complaint ";
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
//安id取投诉
function complaint_by_complaintsid($fields="*",$complaints_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($complaints_id);
	if($id_str!=''){
		$condition=" and complaints_id in($id_str) ";
	}
	return complaint_read_base($fields,$condition,$by_col,$order);
}
//安userid取投诉
function complaint_by_userid($fields="*",$user_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($user_id);
	if($id_str!=''){
		$condition=" and user_id in($id_str) ";
	}
	return complaint_read_base($fields,$condition,$by_col,$order);
}
//安usered_id取投诉
function complaint_by_useredid($fields="*",$usered_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($usered_id);
	if($id_str!=''){
		$condition=" and usered_id in($id_str) ";
	}
	return complaint_read_base($fields,$condition,$by_col,$order);
}
//安order_id取投诉
function complaint_by_orderid($fields="*",$order_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($order_id);
	if($id_str!=''){
		$condition=" and order_id in($id_str) ";
	}
	return complaint_read_base($fields,$condition,$by_col,$order);
}
//取得投诉列表
function complaint_by_all($fields="*",$by_col="",$order=""){
	$fields=filt_fields($fields);
	$condition='';
	return complaint_read_base($fields,$condition,$by_col,$order);
}

?>