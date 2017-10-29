<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}


//引入语言包
$m_langpackage=new moduleslp;


//数据表定义区
$t_goods = $tablePreStr."goods";
$t_credit = $tablePreStr."credit";
$t_user = $tablePreStr."users";

$cid=intval(get_args('id'));
$t=short_check(get_args('t'));

$credit=array(
		"1"=>"好",
		"0"=>"中",
		"-1"=>"差",
	);

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

if($t=="seller"){
//来自卖家评价
	$sql="select a.*,b.goods_name,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.cid=$cid and a.buyer=$user_id and b.goods_id=a.goods_id and c.user_id=a.seller";
	$result = $dbo->getRow($sql);
	$result['credit']=$result['buyer_credit'];
	$result['evaluate']=$result['buyer_evaluate'];
	$result['evaltime']=$result['buyer_evaltime'];
}elseif($t=="buyer"){
	$sql="select a.*,b.goods_name,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.cid=$cid and a.seller=$user_id and b.goods_id=a.goods_id and c.user_id=a.buyer";
	$result = $dbo->getRow($sql);
	$result['credit']=$result['seller_credit'];
	$result['evaluate']=$result['seller_evaluate'];
	$result['evaltime']=$result['seller_evaltime'];
}

?>