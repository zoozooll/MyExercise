<?php
	/*
	***********************************************
	*$ID:
	*$NAME:
	*$AUTHOR:E.T.Wei
	*DATE:Tue Mar 30 09:58:29 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	//文件引入
	require("foundation/module_goods.php");
	//定义数据表
	$t_goods = $tablePreStr."goods";
	$t_brand= $tablePreStr."brand";
	//读写分类定义方法
	$dbo=new dbex;
	dbtarget('r',$dbServs);	
	//引入语言包
	$m_langpackage=new moduleslp;
	//操作
	$goods_ids = substr(short_check(get_args("contrast_goods_id")),0,-1);
	$sql=" SELECT goods_name,goods_thumb,goods_id,goods_price,brand_id,favpv FROM $t_goods WHERE goods_id IN ($goods_ids) ";
	$goods_list = $dbo->getRs($sql);
	foreach ($goods_list as $key=>$value){
		$row=$dbo->getRow("SELECT brand_name FROM $t_brand WHERE brand_id='{$value['brand_id']}'");
		$goods_list[$key]['brand_name']=$row['brand_name'];
	}
?>
