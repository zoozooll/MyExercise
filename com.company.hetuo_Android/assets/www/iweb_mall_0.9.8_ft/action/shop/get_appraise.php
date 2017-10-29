<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

/* post 数据处理 */

$credit=short_check(get_args('credit'));
$time=short_check(get_args('time'));//
$t=short_check(get_args('t'));
$user_id=short_check(get_args('userid'));//shopid

if(empty($t) or empty($user_id)){
	exit('-1');
}

//数据表定义区
$t_credit = $tablePreStr."credit";
$t_goods = $tablePreStr."goods";
$t_user = $tablePreStr."users";
$t_order_info = $tablePreStr."order_info";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$SexMonth = $ctime->time_stamp() - (180 * 24 * 60 * 60);
$SexMonth = date('Y-m-d', $SexMonth);

$sql="select a.*,b.goods_name,b.goods_price,c.user_name from $t_credit as a,$t_goods as b,$t_user as c ";
//来自卖家
if($t=="seller"){
	$sql.="where a.buyer=$user_id and b.goods_id=a.goods_id and c.user_id=a.seller ";
	if($credit!=''){
		$sql.=" and a.buyer_credit=$credit ";
	}
	if($time!='' && $time!='-1'){
		$sql.=" and a.buyer_evaltime>='$time' ";
	}else if($time=='-1'){
		$sql.=" and a.buyer_evaltime<'$SexMonth' ";
	}
	$sql.=" order by a.buyer_evaltime desc";
//来自买家
}else if($t=="buyer"){
	$sql.="where a.seller=$user_id and b.goods_id=a.goods_id and c.user_id=a.buyer ";
	if($credit!=''){
		$sql.=" and a.seller_credit=$credit ";
	}
	if($time!='' && $time!='-1'){
		$sql.=" and a.seller_evaltime>='$time' ";
	}else if($time=='-1'){
		$sql.=" and a.seller_evaltime<'$SexMonth' ";
	}
	$sql.=" order by a.seller_evaltime desc";
}
//echo $sql;
//exit;
$result=$dbo->fetch_page($sql,10);
//print_r($result);
if($result['result']){
	echo json_encode($result);
}else{
	exit('-1');
}

?>