<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require_once("foundation/fstring.php");
require_once("foundation/module_areas.php");
/* 用户信息处理 */
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
/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_category = $tablePreStr."category";
$t_goods = $tablePreStr."goods";
$t_areas = $tablePreStr."areas";
$t_users = $tablePreStr."users";
$t_keywords_count = $tablePreStr."keywords_count";
$t_goods_attr = $tablePreStr."goods_attr";
$t_brand = $tablePreStr."brand";
$t_brand_category = $tablePreStr."brand_category";
$t_attribute = $tablePreStr."attribute";
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
/* 产品处理 */
$sql_best = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_best=1 order by pv desc limit 4";
$sql_hot = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_hot=1 order by pv desc limit 10";
$goods_best = $dbo->getRs($sql_best);
$goods_hot = $dbo->getRs($sql_hot);

/* 地区信息 */
$areainfo = get_areas_kv($dbo,$t_areas);
/* 列表处理 */
$status = false;
$cat_id = intval(get_args('cid'));
$hot = intval(get_args('hot'));
$best = intval(get_args('best'));
$new = intval(get_args('new'));
$promote = intval(get_args('promote'));
$orderprice = intval(get_args('orderprice'));
$cat = intval(get_args('cat'));
$brand = intval(get_args('brand_id'));
$min_price = intval(get_args('min_price'));
$max_price = intval(get_args('max_price'));
$country = intval(get_args('country'));
$province = intval(get_args('province'));
$city = intval(get_args('city'));
$district = intval(get_args('district'));
$attr = get_args('attr');
$k = short_check(get_args('k'));
$attr_table = '';
$attr_where = '';
$attr_url   = '';
$i = 0;
$goods_result=array();
$attr_picks =array();
if (isset($_GET['attr'])) {
	foreach ($_GET['attr'] AS $key => $value)
	{
	    $attr_url .= '&attr[' . $key . ']=' . $value;

	    $attr_picks[] = $key;
	    if ($i > 0)
	    {
	        if (empty($goods_result))
	        {
	            break;
	        }
	        $sql="SELECT goods_id FROM " .$t_goods_attr. " WHERE goods_id IN (" . implode(',' , $goods_result) . ") AND attr_id='$key' AND attr_values='$value'";
	        $goods_result = $dbo->getCol($sql);
	    }
	    else
	    {
	    	$sql="SELECT goods_id FROM " .$t_goods_attr." WHERE attr_id='$key' AND attr_values='$value'";
	        $goods_result = $dbo->getCol($sql);
	    }
	    $i++;
	}
	$status = true;
}
 /* 获取指定attr_id的名字 */
$sql = "SELECT attr_id, attr_name FROM ".$t_attribute." WHERE attr_id ".db_create_in(implode(',',$attr_picks));
$rs = $dbo->query($sql);
while ($row = mysql_fetch_assoc($rs))
{
    $picks[] = array('name'=>'<strong>'.$row['attr_name'].':</strong><br />'.urldecode($_GET['attr'][$row['attr_id']]), 'url'=>'pick_out.php?cat_id='.$cat_id.search_url($attr_picks, $row['attr_id']));
}
/* 查出数量 */
$goods_count = count($goods_result);
/* 获取符合条件的goods_id */
$str=implode(',', $goods_result);
$in_goods = 'AND g.goods_id '.db_create_in($str);

/* 获取符合条件的属性 */
$sql = "SELECT DISTINCT a.attr_id FROM ".$t_goods_attr." AS g, ".$t_attribute." AS a ".
       "WHERE a.attr_id = g.attr_id " . $in_goods;
$in_attr = $dbo->getCol($sql); // 符合条件attr_id;
$in_attr = array_diff($in_attr, $attr_picks); // 除去已经选择过的attr_id
$in_attr = 'AND g.attr_id '.db_create_in(implode(',', $in_attr));

/* 获取所有属性值 */
$sql = "SELECT DISTINCT g.attr_id, a.attr_name, g.attr_values FROM ".$t_goods_attr." AS g, ".$t_attribute ." AS a WHERE a.attr_id = g.attr_id ".$in_attr.$in_goods;
$rs = $dbo->query($sql);
$attr_info=array();
$condition=array();
while ($row = mysql_fetch_assoc($rs))
{
    if (empty($condition['cat'][$row['attr_id']]['cat_name']))
    {
        $condition['cat'][$row['attr_id']]['cat_name'] = $row['attr_name'];
    }
    $condition['cat'][$row['attr_id']]['list'][] = array('name'=>$row['attr_values'], 'url'=>'search.php?cat_id='.$cat_id.search_url($attr_picks).'&amp;attr['.$row['attr_id'].']='.urlencode($row['attr_values']));
}
if (isset($condition['cat'])) {
	$attr_info=$condition['cat'];
}
/* 显示商品 */
if ($str) {
	$sql = "SELECT g.pv,g.is_set_image,g.goods_thumb,g.brand_id,g.goods_id,g.shop_id,g.goods_name,g.goods_price,g.goods_intro, s.shop_province,
s.shop_city,s.shop_district,s.shop_name,s.user_id,u.rank_id FROM $t_goods AS g, $t_shop_info AS s,$t_users AS u ,$t_goods_attr AS a
WHERE s.user_id=u.user_id and g.shop_id=s.shop_id  and g.is_on_sale=1 $in_goods";

}else{
	$sql = "SELECT g.pv,g.is_set_image,g.goods_thumb,g.brand_id,g.goods_id,g.shop_id,g.goods_name,g.goods_price,g.goods_intro, s.shop_province,
s.shop_city,s.shop_district,s.shop_name,s.user_id,u.rank_id FROM $t_goods AS g, $t_shop_info AS s,$t_users AS u
WHERE s.user_id=u.user_id and g.shop_id=s.shop_id  and g.is_on_sale=1 ";

}
if($cat_id) {
	function getsubcats($catinfo, $id) {
		$str = '';
		if(isset($catinfo[$id])) {
			foreach($catinfo[$id] as $v) {
				$str .= ",".$v['cat_id'];
				$str .= getsubcats($catinfo, $v['cat_id']);
			}
		}
		return $str;
	}
	$catids = $cat_id;
	$catids .= getsubcats($CATEGORY, $cat_id);
	$sql .= " AND g.cat_id IN ($catids) ";
	$status = true;
}


if($brand) {
	$sql .= " AND g.brand_id = $brand ";
	$status = true;
}
if($min_price) {
	$sql .= " AND g.goods_price > $min_price ";
	$status = true;
}
if($max_price) {
	$sql .= " AND g.goods_price < $max_price ";
	$status = true;
}
if($country) {
	$sql .= " AND s.shop_country=$country ";
	$status = true;
}
if($province) {
	$sql .= " AND s.shop_province=$province ";
	$status = true;
}
if($city) {
	$sql .= " AND s.shop_city=$city ";
	$status = true;
}
if($district) {
	$sql .= " AND s.shop_district=$district ";
	$status = true;
}

if($hot) {
	$sql .= " AND g.is_hot=1 ";
	$status = true;
}
if($best) {
	$sql .= " AND g.is_best=1 ";
	$status = true;
}
if($new) {
	$sql .= " AND g.is_new=1 ";
	$status = true;
}
if($promote) {
	$sql .= " AND g.is_promote=1 ";
	$status = true;
}
if($k) {
 	$sql .= " AND g.goods_name like '%$k%' ";
	$status = true;
}

if($orderprice) {
	$sql .= " GROUP BY u.rank_id DESC,g.goods_price desc,g.pv DESC";
} else {
	$sql .= " GROUP BY u.rank_id DESC,g.goods_price asc,g.pv DESC";
}
//echo $sql;
$result = $dbo->fetch_page($sql,$SYSINFO['search_page']);
//$result = $dbo->fetch_page($sql,$SYSINFO['search_page']);
if(!empty($k)){
	$sql="select * from $t_keywords_count where keywords='$k'";
	$id_row=$dbo->getRow($sql);

	/* 数据库操作 */
	dbtarget('w',$dbServs);
	$dbo=new dbex();
	if(empty($id_row)){
		$time = $ctime->time_stamp();
		$sql="insert into $t_keywords_count(keywords,count,day,week,month,dataline) value('$k',1,1,1,1,$time)";
		$dbo->exeUpdate($sql);
	}else{
	$time = $ctime->time_stamp();
		$id=$id_row['id'];
		$day_time=mktime(0,0,0,$ctime->custom('m'),$ctime->custom('d'),$ctime->custom('Y'));
		$w=$ctime->custom('w');
		$week_time=mktime(0,0,0,$ctime->custom('m'),$ctime->custom('d')-$w,$ctime->custom('Y'));
		$month_time=mktime(0,0,0,$ctime->custom('m'),1,$ctime->custom('Y'));

		if($id_row['dataline']>$day_time){
			$sql="update $t_keywords_count set count=count+1,day=day+1,week=week+1,month=month+1,dataline=$time where id=$id";
		}else
		if($id_row['dataline']>$week_time){
			$sql="update $t_keywords_count set count=count+1,day=1,week=week+1,month=month+1,dataline=$time where id=$id";
		}else
		if($id_row['dataline']>$month_time){
			$sql="update $t_keywords_count set count=count+1,day=1,week=1,month=month+1,dataline=$time where id=$id";
		}else{
			$sql="update $t_keywords_count set count=count+1,day=1,week=1,month=1,dataline=$time where id=$id";
		}
		$dbo->exeUpdate($sql);
	}
}

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

//引入语言包
$i_langpackage = new indexlp;

if($status) {
	$header['title'] = $i_langpackage->i_search." - ".$SYSINFO['sys_title'];
	$header['keywords'] = $i_langpackage->i_search.','.$SYSINFO['sys_keywords'];
} else {
	$header['title'] = $i_langpackage->i_goods_category." - ".$SYSINFO['sys_title'];
	$header['keywords'] = $i_langpackage->i_goods_category.','.$SYSINFO['sys_keywords'];
}
$header['description'] = $SYSINFO['sys_description'];
$nav_selected =4;//导航位置 E.T add
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
?>
