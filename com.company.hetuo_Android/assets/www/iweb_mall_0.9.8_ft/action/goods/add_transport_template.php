<?php
	/*
	***********************************************
	*$ID:csv_export
	*$NAME:csv_export
	*$AUTHOR:E.T.Wei
	*DATE:Wed Mar 24 10:31:49 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	require("foundation/module_goods.php");
	//引入语言包
	$m_langpackage=new moduleslp;
	//数据表定义区
	$t_transport_template = $tablePreStr."goods_transport";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("w",$dbServs);
	$shop_id = intval(get_args('shop_id'));
	$post = $_POST;
	if (!isset($post['transport_type'])) {
		action_return(0,"至少选择一种配送方式！","modules.php?app=goods_list");
		exit;
	}
	$arr = array();
	$info['transport_name']=$post['transport_name'];
	$info['description'] = $post['description'];
	foreach ($post['transport_type'] as $k=>$v){
		$arr[$v]=array();
	}
	if (isset($arr['ems'])) {
		$arr['ems']['frist']=$post['emsfrist'];
		$arr['ems']['second']=$post['emssecond'];
		foreach ($post['ord_item_destems'] as $k=>$v){
			$v = substr($v,0,-1);
			$areaarr = explode(",",$v);
			foreach ($areaarr as $key=>$value){
				$arr['ems'][$value]=array("frist"=>$post['ord_area_fristems'][$k],"second"=>$post['ord_area_secondems'][$k]);
			}
		}
	}
	if (isset($arr['ex'])) {
		$arr['ex']['frist']=$post['exfrist'];
		$arr['ex']['second']=$post['exsecond'];
		foreach ($post['ord_item_destex'] as $k=>$v){
			$v = substr($v,0,-1);
			$areaarr = explode(",",$v);
			foreach ($areaarr as $key=>$value){
				$arr['ex'][$value]=array("frist"=>$post['ord_area_fristex'][$k],"second"=>$post['ord_area_secondex'][$k]);
			}
		}
	}
	
	if (isset($arr['pst'])) {
		$arr['pst']['frist']=$post['pstfrist'];
		$arr['pst']['second']=$post['pstsecond'];
		foreach ($post['ord_item_destpst'] as $k=>$v){
			$v = substr($v,0,-1);
			$areaarr = explode(",",$v);
			foreach ($areaarr as $key=>$value){
				$arr['pst'][$value]=array("frist"=>$post['ord_area_fristpst'][$k],"second"=>$post['ord_area_seconpst'][$k]);
			}
		}
	}
	$info['shop_id']= get_sess_shop_id();
	$info['content'] = serialize($arr);
	if ($dbo->createbyarr($info,$t_transport_template)) {
		action_return(1,"添加成功！","modules.php?app=goods_list");
	}else{
		echo "no";
	}
?>