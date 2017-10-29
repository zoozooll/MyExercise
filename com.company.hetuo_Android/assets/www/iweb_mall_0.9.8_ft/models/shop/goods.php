<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_goods.php");
require("foundation/module_users.php");
require("foundation/module_areas.php");
require("foundation/module_credit.php");

//引入语言包
$s_langpackage=new shoplp;
$i_langpackage=new indexlp;

/* 定义数据表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_goods_gallery = $tablePreStr."goods_gallery";
$t_areas = $tablePreStr."areas";
$t_goods_attr = $tablePreStr."goods_attr";
$t_credit = $tablePreStr."credit";
$t_integral = $tablePreStr."integral";
$t_attribute = $tablePreStr."attribute";
$t_shop_payment = $tablePreStr."shop_payment";
$t_payment = $tablePreStr."payment";
$t_areas = $tablePreStr."areas";
$t_transport_template = $tablePreStr."goods_transport";

/* 数据库操作 */
dbtarget('w',$dbServs);
$dbo=new dbex();
$sql = "update $t_goods set pv=pv+1 where goods_id='$goods_id'";
$dbo->exeUpdate($sql);
/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 产品信息获取 */
$sql = "select * from `$t_goods` where goods_id=$goods_id and is_on_sale=1";

$goodsinfo = $dbo->getRow($sql);
if($goodsinfo['lock_flg']) { exit("此商品已被锁定!"); }
if(!$goodsinfo) { exit("没有此商品!"); }
//获得地区列表
$area_list = get_area_list_bytype($dbo,$t_areas,1);

//获取商家信用值
$shop_id = $goodsinfo['shop_id'];
$credit=get_credit($dbo,$t_credit,$shop_id);
$credit['SUM(seller_credit)'] = intval($credit['SUM(seller_credit)']);
$integral=get_integral($dbo,$t_integral,$credit['SUM(seller_credit)']);

$sql = "SELECT * FROM $t_goods_gallery WHERE goods_id='$goods_id' order by is_set desc";
$gallery = $dbo->getRs($sql);

$sql = "SELECT * FROM $t_goods_attr WHERE goods_id='$goods_id'";
$goods_attr = $dbo->getRs($sql);
$attr = array();
$attr_ids = array();
$attr_status = false;
if($goods_attr) {
	foreach($goods_attr as $key=>$value) {
		$attr[$value['attr_id']] = $value['attr_values'];
		$attr_ids[] = $value['attr_id'];
	}
	$sql = "SELECT attr_id,attr_name FROM $t_attribute WHERE attr_id IN (".implode(',',$attr_ids).")";
	$attribute_result = $dbo->getRs($sql);
	$attribute = array();
	foreach($attribute_result as $value) {
		$attribute[$value['attr_id']] = $value['attr_name'];
	}
	$attr_status = true;
}

$areainfo = get_areas_kv($dbo,$t_areas);

/* 显示支付方式 */
$sql = "SELECT b.pay_id,b.pay_code FROM $t_shop_payment AS a, $t_payment AS b WHERE a.pay_id=b.pay_id AND a.shop_id=$shop_id AND a.enabled=1";
$result = $dbo->getRs($sql);
$payment_info = array();
if($result) {
	foreach($result as $value) {
		$temp = trim($value['pay_code'],' 0123456789');
		$payment_info[$temp] = $temp;
	}
}

/* 商铺信息处理 */
$shop_id = $goodsinfo['shop_id'];
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}

$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);

$SHOP['rank_id'] = $ranks[0];

$sql = "select goods_id,goods_name,goods_price,goods_thumb,is_set_image from `$t_goods` where is_best=1 and is_on_sale=1 order by sort_order asc,goods_id desc limit 9";
$best_goods = $dbo->getRs($sql);

set_hisgoods_cookie($goodsinfo['goods_id']);
$nav_selected =4;
$header['title'] = $goodsinfo['goods_name']." - ".$SHOP['shop_name'];
$header['keywords'] = $goodsinfo['goods_name'].','.$goodsinfo['keyword'];
$header['description'] = sub_str(strip_tags($goodsinfo['goods_intro']),100);
?>