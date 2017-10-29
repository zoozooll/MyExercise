<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}



//语言包引入
$m_langpackage=new moduleslp;

/* post 数据处理 */
$cid=intval(get_args('id'));
$t=short_check(get_args('t'));
$reply = short_check(get_args('reply'));

//数据表定义区
$t_credit = $tablePreStr."credit";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$time= $ctime->long_time();
if($t=="seller"){
//来自卖家评价
	$sql = "update `$t_credit` set buyer_explanation='$reply',buyer_exptime='$time' where cid=$cid and buyer=$user_id";
}elseif($t=="buyer"){
	$sql = "update `$t_credit` set seller_explanation='$reply',seller_exptime='$time' where cid=$cid and seller=$user_id";
}
//echo $sql;
//exit;
if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_add_success,'-1');
} else {
	action_return(0,$m_langpackage->m_add_fail,'-1');
}
?>