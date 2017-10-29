<?php
	/*
	***********************************************
	*$ID:
	*$NAME:
	*$AUTHOR:E.T.Wei
	*DATE:Wed Mar 24 08:57:03 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	include_once("foundation/asystem_info.php");
	//引入语言包
	$m_langpackage=new moduleslp;
	//数据表定义区
	$g_tables = $tablePreStr."goods";
	//读写分类定义方法
	$dbo=new dbex;
	dbtarget('r',$dbServs);	
	//取得当前用户的信息
	$shop_id = get_sess_shop_id();
	$sql = "SELECT goods_id,goods_name FROM $g_tables WHERE shop_id='$shop_id' ORDER BY goods_id DESC";
	$goods_list = $dbo->getRs($sql);
?>