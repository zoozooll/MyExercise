<?php
	/*
	***********************************************
	*$ID:get_cat_brand_list
	*$NAME:get_cat_brand_list
	*$AUTHOR:E.T.Wei
	*DATE:Fri Apr 02 13:05:46 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	require_once("../foundation/module_brand.php");
	//引入语言包
	$a_langpackage=new adminlp;
	//数据表定义区
	$t_brand = $tablePreStr."brand";
	$t_brand_category = $tablePreStr."brand_category";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("r",$dbServs);
	$cat_id = get_args("cat_id");
	$sql = "SELECT b.brand_id,c.id,c.brand_id as brandid,c.cat_id FROM $t_brand as b,$t_brand_category as c WHERE c.cat_id ='$cat_id' AND c.brand_id=b.brand_id group BY b.brand_id DESC";
	$list = $dbo->getRs($sql);
	echo json_encode($list);
?>