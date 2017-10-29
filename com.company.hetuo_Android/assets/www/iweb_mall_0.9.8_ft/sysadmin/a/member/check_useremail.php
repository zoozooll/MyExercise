<?php
	/*
	***********************************************
	*$ID:
	*$NAME:
	*$AUTHOR:E.T.Wei
	*DATE:Tue Mar 23 10:35:56 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	
	//引入语言包
	$a_langpackage=new adminlp;
	//数据表定义区
	$t_users = $tablePreStr."users";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget('r',$dbServs);
	$v = get_args("v");
	$sql = "SELECT `user_id` FROM $t_users WHERE `user_email`='$v' LIMIT 1";
	$id = $dbo->getRow($sql);
	if ($id>0) {
		echo 0;
	}else{
		echo 1;
	}
?>