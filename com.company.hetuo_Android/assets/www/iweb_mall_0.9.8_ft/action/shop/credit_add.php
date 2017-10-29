<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

/* post 数据处理 */
$oid=intval(get_args('id'));
$t=short_check(get_args('t'));
$grade = short_check(get_args('grade'));
$content = short_check(get_args('content'));

//数据表定义区
$t_credit = $tablePreStr."credit";
$t_order_info = $tablePreStr."order_info";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$time = $ctime->long_time();
if($t=="seller"){
	$sql = "update `$t_credit` set buyer_credit='$grade',buyer_evaluate='$content',buyer_evaltime='$time' where order_id=$oid and seller=$user_id";
//	$sql = "insert into $t_credit(buyer_credit,buyer_evaluate,buyer_evaltime,order_id,seller) value($grade,'$content','$time',$oid,$user_id)";
	$sql1 = "update `$t_order_info` set buyer_reply='1'";
}elseif($t=="buyer"){
	$sql = "update `$t_credit` set seller_credit='$grade',seller_evaluate='$content',seller_evaltime='$time' where order_id=$oid and buyer=$user_id";
//	$sql = "insert into $t_credit(seller_credit,seller_evaluate,seller_evaltime,order_id,buyer) value($grade,'$content','$time',$oid,$user_id)";
	$sql1 = "update `$t_order_info` set seller_reply='1'";
}
//echo $sql;
//exit;
if($dbo->exeUpdate($sql) && $dbo->exeUpdate($sql1)) {
	action_return(1,$m_langpackage->m_add_success,'modules.php?app=user_my_order');
} else {
	action_return(0,$m_langpackage->m_add_fail,'modules.php?app=user_my_order');
}
?>