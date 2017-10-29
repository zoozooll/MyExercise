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
require("foundation/flefttime.php");
//引入语言包
$s_langpackage=new shoplp;
$i_langpackage=new indexlp;
/* 定义文件表 */
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
$t_shop_groupbuy = $tablePreStr."groupbuy";
$t_shop_groupbuy_log = $tablePreStr."groupbuy_log";
$t_shop_guestbook = $tablePreStr."shop_guestbook";

$group_id = intval($_GET['id']);
/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();
$sql = "select goods_id from `$t_shop_groupbuy` where group_id = $group_id";
$groupbuyinfo = $dbo->getRow($sql);
if ($groupbuyinfo){
	$goods_id = $groupbuyinfo['goods_id'];
}else {
	exit('没有此团购!');
}
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

$header['keywords'] = $goodsinfo['goods_name'].','.$goodsinfo['keyword'];
$header['description'] = sub_str(strip_tags($goodsinfo['goods_intro']),100);

$user_id = get_sess_user_id();
/* 团购信息处理 */
if ($user_id){
	$isset_logo = false;
	if ($user_id == $shop_id){
		$sql = "select g.*,t.* from $t_shop_groupbuy g left join $t_goods t on g.goods_id = t.goods_id where g.shop_id = $user_id and g.group_id = $group_id";
	}else {
		$sql = "select * from $t_shop_groupbuy_log where group_id = $group_id and user_id = $user_id";
		$groupbuy_oneinfo = $dbo->getRs($sql);
		if ($groupbuy_oneinfo){
			$isset_groupbuy = true;
			$sql = "select g.*,l.* from $t_shop_groupbuy g left join $t_shop_groupbuy_log l on g.group_id = l.group_id where l.group_id = $group_id and l.user_id = $user_id";


		}else {
			$isset_groupbuy = false;
			$sql = "select * from $t_shop_groupbuy g left join $t_goods t on g.goods_id = t.goods_id where g.group_id = $group_id";
		}
	}
}else {
	$isset_logo = true;
	$sql = "select * from $t_shop_groupbuy g left join $t_goods t on g.goods_id = t.goods_id where g.group_id = $group_id";
}
$groupbuyinfo = $dbo->getRow($sql);

$goods_p_id = $groupbuyinfo['goods_id'];
if ($groupbuyinfo['goods_id']){
	$goods_price = "select goods_price from `$t_goods` where goods_id ='$goods_p_id'";
	$goods_price = $dbo->getRow($goods_price);
	$groupbuyinfo['goods_price'] =$goods_price['goods_price'];
}
$header['title'] = $groupbuyinfo['group_name']." - ".$SHOP['shop_name'];
/* 时间处理 */
$now_time = new time_class();
$start_time = strtotime($groupbuyinfo['start_time']);
$now_time = $now_time -> time_stamp();
$end_time = strtotime($groupbuyinfo['end_time']);
if ($user_id){
	$sql = "select user_id,quantity,linkman,tel FROM `$t_shop_groupbuy_log` where user_id = $user_id";
	$isset_add_groupbuy = $dbo->getRow($sql);
}
/* 留言管理 */
$sql = "SELECT * FROM $t_shop_guestbook WHERE shop_id='$shop_id' order by add_time desc limit 10";
$guestbook_list = $dbo->getRs($sql);
?>