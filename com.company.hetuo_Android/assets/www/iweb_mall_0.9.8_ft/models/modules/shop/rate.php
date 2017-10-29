<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_rate.php");
require_once("foundation/fstring.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_credit = $tablePreStr."credit";
$t_user = $tablePreStr."users";

//变量定义区
$t=short_check(get_args('t'));

$credit=array(
		"1"=>"好",
		"0"=>"中",
		"-1"=>"差",
	);

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

if($t=="seller"){
//来自卖家评价
	$sql="select a.*,b.goods_name,b.goods_price,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.buyer=$user_id and b.goods_id=a.goods_id and c.user_id=a.seller";
	$result=$dbo->fetch_page($sql,10);
	if(!empty($result['result'])){
		foreach($result['result'] as $key=>$val){
			if(empty($val['buyer_evaltime'])){
				unset($result['result'][$key]);
			}else{
				$result['result'][$key]['people']=$val['buyer'];
				$result['result'][$key]['credit']=$val['buyer_credit'];
				$result['result'][$key]['evaluate']=$val['buyer_evaluate'];
				$result['result'][$key]['evaltime']=$val['buyer_evaltime'];
				$result['result'][$key]['explanation']=$val['buyer_explanation'];
				$result['result'][$key]['exptime']=$val['buyer_exptime'];
			}
		}
	}
}elseif($t=="buyer"){
	$sql="select a.*,b.goods_name,b.goods_price,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.seller=$user_id and b.goods_id=a.goods_id and c.user_id=a.buyer";
	$result=$dbo->fetch_page($sql,10);
	if(!empty($result['result'])){
		foreach($result['result'] as $key=>$val){
			if(empty($val['seller_evaltime'])){
				unset($result['result'][$key]);
			}else{
				$result['result'][$key]['people']=$val['seller'];
				$result['result'][$key]['credit']=$val['seller_credit'];
				$result['result'][$key]['evaluate']=$val['seller_evaluate'];
				$result['result'][$key]['evaltime']=$val['seller_evaltime'];
				$result['result'][$key]['explanation']=$val['seller_explanation'];
				$result['result'][$key]['exptime']=$val['seller_exptime'];
			}
		}
	}
}elseif($t=="bymain"){
	$sql="select a.*,b.goods_name,b.goods_price,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where (a.buyer=$user_id and b.goods_id=a.goods_id and c.user_id=a.seller) or (a.seller=$user_id and b.goods_id=a.goods_id and c.user_id=a.buyer)";
	$result=$dbo->fetch_page($sql,10);
	
	if(!empty($result['result'])){
		foreach($result['result'] as $key=>$val){
			if($val['seller']==$user_id){
				if(empty($val['buyer_evaltime'])){
					unset($result['result'][$key]);
				}else{
					$result['result'][$key]['people']=$val['buyer'];
					$result['result'][$key]['credit']=$val['buyer_credit'];
					$result['result'][$key]['evaluate']=$val['buyer_evaluate'];
					$result['result'][$key]['evaltime']=$val['buyer_evaltime'];
					$result['result'][$key]['explanation']=$val['buyer_explanation'];
					$result['result'][$key]['exptime']=$val['buyer_exptime'];
					$result['result'][$key]['exptime']='exptime';
				}
			}elseif($val['buyer']==$user_id){
				if(empty($val['seller_evaltime'])){
					unset($result['result'][$key]);
				}else{
					$result['result'][$key]['people']=$val['seller'];
					$result['result'][$key]['credit']=$val['seller_credit'];
					$result['result'][$key]['evaluate']=$val['seller_evaluate'];
					$result['result'][$key]['evaltime']=$val['seller_evaltime'];
					$result['result'][$key]['explanation']=$val['seller_explanation'];
					$result['result'][$key]['exptime']=$val['seller_exptime'];
					$result['result'][$key]['exptime']='exptime';
				}
			}
		}
	}
}
//echo $sql;
//print_r($result);
//print_r($result_seller);
//print_r($result_bymain);
?>