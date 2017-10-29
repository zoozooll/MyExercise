<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入文件
require_once("foundation/fsqlitem_set.php");
require_once("foundation/module_goods.php");
//引入语言包
$m_langpackage=new moduleslp;

$k = short_check(get_args('k'));
$cat = intval(get_args('cat'));

//数据表定义区
$t_cart = $tablePreStr."cart";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";
$user_id = get_sess_user_id();
//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
$sql="SELECT goods_id FROM $t_cart WHERE user_id='$user_id'";
$rs = $dbo->getRs($sql);
$arr = array();
foreach ($rs as $k=>$v){
	$arr[]=$v['goods_id'];
}
$goods_ids="";
$dbo = new dbex;
dbtarget('w',$dbServs);
if (isset($_SESSION['cart'])) {
	foreach ($_SESSION['cart'] as $key=>$value){
		if ($user_id) {
			if (!in_array($key,$arr)) {
				$insert_array = array(
					'user_id' => $user_id,
					'goods_id' => $key,
					'goods_number' => $_SESSION['cart'][$key]['num'],
					'add_time' => $ctime->long_time(),
				);
				$goods_info = get_goods_info($dbo,$t_goods,array('goods_name','goods_price','goods_number'),$key);
				$item_sql = get_insert_item($insert_array);
				$sql = "insert into `$t_cart` $item_sql ";
				if($dbo->exeUpdate($sql)) {
					$new_goods_num = $goods_info['goods_number'] - $_SESSION['cart'][$key]['num'];
					$sql = "update `$t_goods` set goods_number='$new_goods_num' where goods_id='$key'";
					$dbo->exeUpdate($sql);
				}
			}
		}
		$goods_ids.="($key,";
	}
}
$goods_ids = substr($goods_ids,0,-1);
$goods_ids.=")";
//读写分离定义方法
if ($user_id) {
	$dbo = new dbex;
	dbtarget('r',$dbServs);
	
	//$sql = "select * from `$t_cart` where user_id='$user_id'";
	//
	//$sql .= " order by add_time desc";
	//$result = $dbo->fetch_page($sql,13);
	$sql = "SELECT c.user_id,a.goods_id,a.cart_id,a.add_time,a.goods_number,b.shop_id,b.goods_name,b.goods_thumb,b.goods_price,b.favpv,c.shop_name,c.shop_id FROM `$t_cart` AS a, `$t_goods` AS b, `$t_shop_info` as c WHERE a.goods_id=b.goods_id AND b.shop_id=c.shop_id AND a.user_id='$user_id'";
	$sql .= " order by a.add_time desc";
	$result = $dbo->fetch_page($sql,10);
}else{
	$dbo = new dbex;
	dbtarget('r',$dbServs);
	$sql = "SELECT c.user_id,b.shop_id,b.goods_id,b.goods_name,b.goods_thumb,b.goods_price,b.favpv,c.shop_name,c.shop_id FROM `$t_goods` AS b, `$t_shop_info` as c WHERE b.shop_id=c.shop_id AND b.goods_id IN $goods_ids";
	$sql .= " order by b.goods_id desc";
	$result = $dbo->fetch_page($sql,10);
	foreach ($result['result'] as $k=>$v){
		$result['result'][$k]['add_time']=$_SESSION['cart'][$v['goods_id']]['add_time'];
		$result['result'][$k]['goods_number']=$_SESSION['cart'][$v['goods_id']]['num'];
		$result['result'][$k]['cart_id']=0;
	}
}
?>