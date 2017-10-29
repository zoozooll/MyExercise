<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_users = $tablePreStr."users";

$oid=intval(get_args('id'));
$t=short_check(get_args('t'));

$credit=array(
		"1"=>"好",
		"0"=>"中",
		"-1"=>"差",
	);

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

//if($t=="seller"){
	$sql="select a.goods_name,a.goods_id,b.user_name,b.user_id from $t_order_info as a,$t_users as b where a.order_id=$oid and b.user_id=a.user_id";
//}elseif($t=="buyer"){
//	$sql="select a.goods_name,b.user_name from $t_order_info as a,$t_users as b where a.order_id=$oid and a.user_id=b.user_id";
//}

//echo $sql;
$result = $dbo->getRow($sql);

?>