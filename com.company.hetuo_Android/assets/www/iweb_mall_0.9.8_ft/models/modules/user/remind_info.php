<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//require("foundation/module_remind.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_remind_info = $tablePreStr."remind_info";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$sql="select * from $t_remind_info where user_id=$user_id order by isread";
$remind_rs=$dbo->getRs($sql);

//print_r($remind_rs);

$type=array(
	"0"=>"未读",
	"1"=>"已读",
);

?>