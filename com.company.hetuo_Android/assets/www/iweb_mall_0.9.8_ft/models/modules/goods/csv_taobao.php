<?php
	/*
	***********************************************
	*$ID:csv_import
	*$NAME:csv_import
	*$AUTHOR:E.T.Wei
	*DATE:Wed Mar 24 13:58:49 CST 2010
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
	
	//读写分类定义方法
	
	//获得店铺ID
	$shop_id = get_sess_shop_id();
?>