<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_goods.php");
require("foundation/module_users.php");


//引入语言包
$s_langpackage=new shoplp;

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 用户分类信息获取 */
$sql = "select * from `$t_shop_category` where shop_cat_id='$shop_cat_id'";
$catinfo = $dbo->getRow($sql);
if(!$catinfo && $s) {
	$catinfo['shop_id'] = $s;
	$catinfo['shop_cat_name'] = "搜索";
}

$ids = $shop_cat_id;
$sql = "select shop_cat_id from `$t_shop_category` where parent_id='$shop_cat_id'";
$rows = $dbo->getRs($sql);
if($rows) {
	foreach($rows as $v){
		$ids .= ','.$v['shop_cat_id'];
	}
}

$sql = "select goods_id,goods_name,goods_price,goods_thumb,is_set_image from `$t_goods` where is_on_sale=1 ";
if($shop_cat_id>0) {
	$sql .= " and ucat_id in ($ids) ";
}
/*E.T eidt start*/
if ($s) {
	$sql .= "and shop_id='$s' ";
}
/*E.T eidt end*/
if($k) {
	$sql .= " and goods_name like '%$k%'";
}
$sql .= " order by sort_order asc,goods_id desc";

$result = $dbo->fetch_page($sql,20);


/* 商铺信息处理 */
$shop_id = $catinfo['shop_id'];
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}
$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks['rank_id'];
$SHOP['user_name'] = $ranks['user_name'];

$header['title'] = $catinfo['shop_cat_name']." - ".$SHOP['shop_name'];
$header['keywords'] = $catinfo['shop_cat_name'].','.$SHOP['shop_name'];
$header['description'] = $SHOP['shop_name'].$catinfo['shop_cat_name'];

?>