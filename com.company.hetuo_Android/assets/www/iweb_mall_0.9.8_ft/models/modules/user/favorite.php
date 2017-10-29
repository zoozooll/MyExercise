<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_user_favorite = $tablePreStr."user_favorite";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "SELECT c.user_id,a.goods_id,a.favorite_id,a.add_time,b.shop_id,b.goods_name,b.goods_thumb,b.goods_price,b.favpv,c.shop_name,c.shop_id FROM `$t_user_favorite` AS a, `$t_goods` AS b, `$t_shop_info` as c WHERE a.goods_id=b.goods_id AND b.shop_id=c.shop_id AND a.user_id='$user_id'";

$sql .= " order by a.add_time desc";

$result = $dbo->fetch_page($sql,10);
?>