<?php
include(dirname(__file__)."/../includes.php");

function user_info_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_user_info=$tablePreStr."user_info";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');
	
	$sql=" select $fields from $t_user_info ";
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
//用户基本信息
function user_info_by_uid($fields="*",$user_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($user_id);
	if($id_str!=""){
		$condition=" and user_id in ($id_str) ";
	}
	return user_info_read_base($fields,$condition,$by_col,$order);
}
?>