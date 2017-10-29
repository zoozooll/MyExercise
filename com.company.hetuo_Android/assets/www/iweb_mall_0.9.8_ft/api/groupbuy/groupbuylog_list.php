<?php
include(dirname(__file__)."/../includes.php");

function groupbuylog_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_groupbuy_log = $tablePreStr."groupbuy_log";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');

	$sql=" select $fields from $t_groupbuy_log ";
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
//取得参加团购列表
function groupbuylog_type_by_all($fields="*",$by_col="",$order=""){
	$fields=filt_fields($fields);
	$condition='';
	return groupbuylog_read_base($fields,$condition,$by_col,$order);
}
//安grouplogid取参加团购
function groupbuylog_by_grouplogid($fields="*",$grouplog_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($grouplog_id);
	if($id_str!=''){
		$condition=" and grouplog_id in($grouplog_id) ";
	}
	return groupbuylog_read_base($fields,$condition,$by_col,$order);
}
//安userid取参加团购
function groupbuylog_by_userid($fields="*",$user_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($user_id);
	if($id_str!=''){
		$condition=" and user_id in($id_str) ";
	}
	return groupbuylog_read_base($fields,$condition,$by_col,$order);
}
//安groupid取参加团购
function groupbuylog_by_groupid($fields="*",$group_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($group_id);
	if($id_str!=''){
		$condition=" and group_id in($id_str) ";
	}
	return groupbuylog_read_base($fields,$condition,$by_col,$order);
}
?>