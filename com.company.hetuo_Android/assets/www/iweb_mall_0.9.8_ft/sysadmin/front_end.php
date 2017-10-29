<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("../foundation/asession.php");
require("../configuration.php");
require("includes.php");
require("../foundation/module_admin_logs.php");

//数据表定义区
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('w',$dbServs);
$dbo=new dbex;

require("atool_box.php");
//当前可访问的应用工具
$appArray=array(
	"start"=>'modules/start.php',
	"hstart"=>'modules/homestart.php',
	"user_ico_select"=>'../modules/album/photo_ico_select.php',
);
$appArray=array_merge($appArray,$tools_box_array);
$appId=getAppId();
$apptarget=$appArray[$appId];
if(isset($apptarget)){
	/** 添加log */
	$admin_log ="运行工具";
	admin_log($dbo,$t_admin_log,$admin_log);

	require($apptarget);
}
else{
	echo 'no pages!';
}

?>