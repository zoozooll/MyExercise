<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require_once("foundation/fstring.php");

require_once("foundation/module_areas.php");

/* URL信息处理 */
$cat_id = intval(get_args('id'));
if(!$cat_id) {
	exit("非法请求！");
}

/* 用户信息处理 */
require 'foundation/alogin_cookie.php';
if(get_sess_user_id()) {
	$USER['login'] = 1;
	$USER['user_name'] = get_sess_user_name();
	$USER['user_id'] = get_sess_user_id();
	$USER['user_email'] = get_sess_user_email();
	$USER['shop_id'] = get_sess_shop_id();
} else {
	$USER['login'] = 0;
	$USER['user_name'] = '';
	$USER['user_id'] = '';
	$USER['user_email'] = '';
	$USER['shop_id'] = '';
}


//引入语言包
$i_langpackage = new indexlp;

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_category = $tablePreStr."category";
$t_goods = $tablePreStr."goods";
$t_users = $tablePreStr."users";
$t_areas = $tablePreStr."areas";
$t_attribute = $tablePreStr."attribute";
$t_goods_attr = $tablePreStr."goods_attr";
$t_brand = $tablePreStr."brand";
$t_brand_category = $tablePreStr."brand_category";
/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result = $dbo->getRs($sql_category);
$CATEGORY = array();
$cat_info = array();
foreach($result as $v) {
	$CATEGORY[$v['parent_id']][$v['cat_id']] = $v;
}
foreach($result as $v) {
	$cat_info[$v['cat_id']] = $v;
}

/* 列表处理 */
if(!function_exists('getsubcats')) {
	function getsubcats($catinfo, $id) {
		$str = '';
		if(isset($catinfo[$id]) && $catinfo[$id]) {
			foreach($catinfo[$id] as $v) {
				$str .= ",".$v['cat_id'];
				$str .= getsubcats($catinfo, $v['cat_id']);
			}
		}
		return $str;
	}
}
$catids = $cat_id;
$catids .= getsubcats($CATEGORY, $cat_id);

$this_catinfo = $cat_info[$cat_id];
$this_parent_info = '';
if($cat_info[$cat_id]['parent_id']>0){
	$this_parent_info = $cat_info[$cat_info[$cat_id]['parent_id']];
}

//echo $catids;
$areainfo = get_areas_kv($dbo,$t_areas);

$sql = "SELECT g.pv,g.is_set_image,g.goods_thumb,g.goods_id,g.shop_id,g.goods_name,g.goods_price,g.goods_intro, s.shop_province,s.shop_city,
		s.shop_district,s.shop_name,s.user_id ,u.rank_id
		FROM `$t_goods` AS g, `$t_shop_info` AS s,`$t_users` AS u WHERE s.user_id=u.user_id and g.shop_id=s.shop_id AND
		g.cat_id IN ($catids) and g.is_on_sale=1 ";
if($_POST){
	//print_r($_POST);
	$order_name=$_POST['name'];
	$order=$_POST['order'];
	if (!$order_name) {
		$order_name="goods_id";
	}
	if (!$order) {
		$order="DESC";
	}
	$sql.= "ORDER BY g.$order_name $order,u.rank_id DESC,g.pv DESC ";

}else{
	$sql.= "ORDER BY u.rank_id DESC,g.goods_price asc,g.pv DESC ";
}

$result = $dbo->fetch_page($sql,$SYSINFO['product_page']);

/* 产品处理 */
$sql_best = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_best=1 order by pv desc limit 4";
$sql_hot = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_hot=1 order by pv desc limit 10";
$goods_best = $dbo->getRs($sql_best);
$goods_hot = $dbo->getRs($sql_hot);

/* 浏览记录 */
$getcookie = get_hisgoods_cookie();
$goodshistory = array();
if($getcookie) {
	arsort($getcookie);
	$getcookie = array_keys($getcookie);
	$gethisgoodsid = implode(",",array_slice($getcookie, 0, 4));
	$sql = "select is_set_image,goods_id,goods_name,goods_thumb,goods_price from $t_goods where goods_id in ($gethisgoodsid)";
	$goodshistory = $dbo->getRs($sql);
}


$header['title'] = $this_catinfo['cat_name']." - ".$SYSINFO['sys_title'];
$header['keywords'] = $this_catinfo['cat_name'].','.$SYSINFO['sys_keywords'];
$header['description'] = $SYSINFO['sys_description'];
/* 属性 */
$sql = "select * from $t_attribute where cat_id='$cat_id'";
$attr_info = $dbo->getRs($sql);

foreach($attr_info as $key => $value){

	$values=json_encode($value['attr_values']);
	$values_after=str_replace('\r\n',',',$values);
	$values_after=str_replace('\n',',',$values);
	$values_after=json_decode($values_after);
	$attr_info[$key]['attr_values']=explode(',',$values_after);

	foreach($attr_info[$key]['attr_values'] as $k => $va){
		$va=trim($va);
		$sql = "select count(*) AS attr_count from $t_goods_attr AS ga, $t_goods AS g  where ga.attr_values='$va' and g.is_on_sale=1 and
		g.goods_id=ga.goods_id ";
		$goods_attr_info = $dbo->getRow($sql);
		$attr_info[$key]['values_count'][$k]=$goods_attr_info["attr_count"];
	}
}
//品牌列表
$sql = "SELECT brand_id FROM $t_brand_category WHERE cat_id='$cat_id'";
$list = $dbo->getRs($sql);
$brand_list=array();
if (is_array($list)) {
	foreach ($list as $value){
		$sql="SELECT brand_id,brand_name FROM $t_brand WHERE brand_id='{$value['brand_id']}'";
		$brand_list[]=$dbo->getRow($sql);
	}
}
$nav_selected =4;
?>