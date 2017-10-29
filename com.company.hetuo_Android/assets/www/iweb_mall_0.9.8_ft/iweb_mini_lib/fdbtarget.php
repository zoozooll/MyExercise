<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//数据库连接控制方法
function dbtarget($rwAction,$dbServs)
{
	//$rwAction参数为SI库处理数据库读写分离时使用参数
	db_conn($dbServs[0],$dbServs[2],$dbServs[3],$dbServs[1]);
}

function dbplugin($rw)
{
	global	$dbServs;
	dbtarget($rw,$dbServs);
}

//建立数据库连接
function db_conn($host,$user,$pwd,$db)
{
	if($connStr=mysql_connect($host,$user,$pwd)){

	mysql_query("set names 'UTF8'");
	mysql_select_db($db,$connStr);
	}else{
		throw new Exception('');
		return false;
	}
}

//释放数据库连接
function dbtarget_free()
{
	@mysql_close(); 
}
?>