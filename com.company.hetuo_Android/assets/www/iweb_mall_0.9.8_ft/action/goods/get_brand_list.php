<?php
	/*
	***********************************************
	*$ID:get_brand_list
	*$NAME:get_brand_list
	*$AUTHOR:E.T.Wei
	*DATE:Fri Apr 02 16:01:42 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	require("foundation/module_brand.php");
	//引入语言包
	$m_langpackage=new moduleslp;
	//数据表定义区
	$t_brand = $tablePreStr."brand";
	$t_brand_category = $tablePreStr."brand_category";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("r",$dbServs);
	$cat_id = intval(get_args("cat_id"));
	$sql = "SELECT brand_id FROM $t_brand_category WHERE cat_id = '$cat_id'";
	$arr = array();
	$brand_ids = $dbo->getRs($sql);
	foreach ($brand_ids as $value){
		$sql="SELECT brand_name,brand_id FROM $t_brand WHERE brand_id='{$value['brand_id']}'";
//		echo $sql;
		$arr[]=$dbo->getRow($sql);
	}
	echo json_encode($arr);

?>