<?php
include(dirname(__file__)."/../includes.php");

function article_list_read_base($fields="*",$condition="",$by_col="",$order="",$num="",$cache="",$cache_key=""){
	global $tablePreStr;
	$t_article=$tablePreStr."article";
	$plugin_rs=array();
	$dbo=new dbex;
	dbplugin('r');
	
	$sql=" select $fields from $t_article ";
	$sql.=" where is_show=1";
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
//新闻列表（按分类）
function article_list_by_catid($fields="*",$cat_id,$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($cat_id);
	if($id_str!=""){
		$condition=" and cat_id in ($id_str) ";
	}
	return article_list_read_base($fields,$condition,$by_col,$order);
}
//新闻内容列表
function article_list_all($fields="*",$article_id="",$by_col="",$order=""){
	$fields=filt_fields($fields);
	$id_str=filt_num_array($article_id);
	if($id_str!=""){
		$condition=" and article_id in ($id_str) ";
	}
	return article_list_read_base($fields,$condition,$by_col,$order);
}
?>