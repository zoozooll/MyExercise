<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//require("foundation/module_remind.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_remind = $tablePreStr."remind";
$t_remind_user = $tablePreStr."remind_user";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$sql="select * from $t_remind where enable=1 order by remind_type";
$remind_rs=$dbo->getRs($sql);

$sql="select * from $t_remind_user where user_id=$user_id";
$remind_user_rs=$dbo->getRs($sql);

$remind_user_arr=array();
foreach($remind_user_rs as $val){
	$remind_user_arr[$val['remind_id']]=$val;
}


$type=array(
	"1"=>"买家提醒",
	"2"=>"卖家提醒",
);


?>