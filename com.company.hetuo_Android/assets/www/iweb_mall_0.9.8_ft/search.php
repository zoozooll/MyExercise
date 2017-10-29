<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/search.html
 * 如果您的模型要进行修改，请修改 models/search.php
 *
 * 修改完成之后需要您进入后台重新编译，才会重新生成。
 * 如果您开启了debug模式运行，那么您可以省去上面这一步，但是debug模式每次都会判断程序是否更新，debug模式只适合开发调试。
 * 如果您正式运行此程序时，请切换到service模式运行！
 *
 * 如您有问题请到官方论坛（http://tech.jooyea.com/bbs/）提问，谢谢您的支持。
 */
?><?php
/*
 * 此段代码由debug模式下生成运行，请勿改动！
 * 如果debug模式下出错不能再次自动编译时，请进入后台手动编译！
 */
/* debug模式运行生成代码 开始 */
if(!function_exists("tpl_engine")) {
	require("foundation/ftpl_compile.php");
}
if(filemtime("templates/default/search.html") > filemtime(__file__) || (file_exists("models/search.php") && filemtime("models/search.php") > filemtime(__file__)) ) {
	tpl_engine("default","search.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo  $header['title'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="<?php echo  $header['keywords'];?>" />
<meta name="description" content="<?php echo  $header['description'];?>" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<!--search end -->
<?php if(!$status){?>
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <?php echo $i_langpackage->i_goodsheader_category;?></div>
<div class="pro_class">
	<?php $i=0;?>
	<?php  foreach($CATEGORY[$i] as $cat) {
	   $i++;?>
	<?php if($i%2){?>
    <div class="category_list" onmouseover="javascript:this.className ='category_list category_list3'" onmouseout="javascript:this.className='category_list'"><h3><a href="<?php echo  category_url($cat['cat_id']);?>" title="<?php echo  $cat['cat_name'];?>"><?php echo  $cat['cat_name'];?></a>(<?php echo  $cat['goods_num'];?>)</h3>
	<?php  } else {?>
	<div class="category_list2" onmouseover="javascript:this.className ='category_list2 category_list3'" onmouseout="javascript:this.className='category_list2'"><h3><a href="<?php echo  category_url($cat['cat_id']);?>" title="<?php echo  $cat['cat_name'];?>"><?php echo  $cat['cat_name'];?></a>(<?php echo  $cat['goods_num'];?>)</h3>
	<?php }?>
        <dl>
        <?php  if(isset($CATEGORY[$cat['cat_id']])) {
        	foreach($CATEGORY[$cat['cat_id']] as $subcat) {?>
            <dd><a href="<?php echo  category_url($subcat['cat_id']);?>" title="<?php echo  $subcat['cat_name'];?>"><?php echo  $subcat['cat_name'];?></a>(<?php echo  $subcat['goods_num'];?>)
            </dd>
        <?php  } }?>
        </dl>
    </div>
	<div class="clear"></div>
    <?php }?>
</div>
<div class="top5 clear"></div>
<?php  } else {?>
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <?php echo $i_langpackage->i_keywords;?>“<?php echo  $k;?>”</div>
<div class="pro_class">
<?php  if($attr_info) {?>
<?php  foreach($attr_info as $key => $value){?>
<div class="pro_class" id="<?php echo  $key;?>">
	<div style="cursor:pointer;" onclick="hidden('<?php echo  $key;?>','<?php echo  $value['cat_name'];?>')"><h3><?php echo $i_langpackage->i_select;?><?php echo  $value['cat_name'];?><?php echo $i_langpackage->i_select_goods;?></h3></div>
	<dl><?php  foreach($value['list'] as $k => $v){?>
		<dd><a title="<?php echo  $v;?>" href="<?php echo $v['url'];?>"><?php echo  $v['name'];?></a></dd>
		<?php }?>
	</dl>
</div>
<div class="pro_class" onclick="show('<?php echo  $key;?>','<?php echo  $value['cat_name'];?>')" id="<?php echo  $value['cat_name'];?>" style="display:none;cursor:pointer;">
	<h4><?php echo $i_langpackage->i_select;?><?php echo  $value['cat_name'];?><?php echo $i_langpackage->i_select_goods;?></h4>
</div>
<br>
<?php }?>
<?php }?>
<?php  if(count($brand_list)>0) {?>
<div class="pro_class" id="brand_box" style="border:0">
	<div style="cursor:pointer;" onclick="hidden('brand_box','<?php echo $i_langpackage->i_select_products_by_brand;?>')"><h3><?php echo $i_langpackage->i_select_products_by_brand;?></h3></div>
	<dl><?php  foreach($brand_list as $key => $value){?>
		<dd><a title="<?php echo  $value['brand_name'];?>" href="search.php?brand_id=<?php echo  $value['brand_id'];?>&cid=<?php echo  $cat_id;?>"><?php echo  $value['brand_name'];?></a></dd>
		<?php }?>
	</dl><div class="clear"></div>
</div>
<div class="pro_class" id="<?php echo $i_langpackage->i_select_products_by_brand;?>" onclick="show('brand_box','<?php echo $i_langpackage->i_select_products_by_brand;?>')" style="display:none;cursor:pointer;">
	<h4><?php echo $i_langpackage->i_select;?><?php echo $i_langpackage->i_brand;?><?php echo $i_langpackage->i_select_goods;?></h4>
</div>
<br>
<?php }?>
</div>
<div class="top5 clear"></div>
<div class="main2 left top5">
	<div class="top">
	  <span class="right"><a id="list" onclick="changeStyle2('list',this)" class="selected" href="javascript:void(0);" hidefocus="true"><?php echo $i_langpackage->i_list;?></a><a id="window" onclick="changeStyle2('window',this)" href="javascript:void(0);" hidefocus="true"><?php echo $i_langpackage->i_show_window;?></a></span>
	  <h2><a href="#"><?php echo $i_langpackage->i_choice_good;?></a></h2>
	</div>

	<div id="listcontent" class="c_m">
		<ul>
			<li class="list_item_title">
				<ul><li class="operate"><?php echo $i_langpackage->i_handle;?></li><li class="place"><?php echo $i_langpackage->i_in_area;?></li><li class="price"><?php echo $i_langpackage->i_price;?></li><li class="summary"><?php echo $i_langpackage->i_goods_infos;?></li></ul>
			</li>
			<?php  if($result['result']) {
				foreach($result['result'] as $v) {?>
			<li class="list_item" id="showli_<?php echo $v['goods_id'];?>">
			  <div class="photo"><a href="<?php echo  goods_url($v['goods_id']);?>"><img onmouseover="showbox(<?php echo $v['goods_id'];?>)" onmouseout="hidebox(<?php echo $v['goods_id'];?>)" src="<?php echo  $v['is_set_image'] ? $v['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $v['goods_name'];?>"  width="84" height="84" /></a></div>
              <div class="summary">
                <h3><a href="<?php echo  goods_url($v['goods_id']);?>"><?php echo  $v['goods_name'];?></a></h3>
                <p>[<?php echo  $i_langpackage->i_info;?>]<?php echo  sub_str(strip_tags($v['goods_intro']),90);?></p>
                <p><span class="left"><?php echo  $i_langpackage->i_shop;?>：<a href="<?php echo  shop_url($v['shop_id']);?>"><?php echo  $v['shop_name'];?></a></span>
				<?php  if($v['rank_id']>2){?>
				<a href="javascript:;" title="<?php echo $i_langpackage->i_approve_company;?>"  class="shop_cert left"><?php echo $i_langpackage->i_approve_company;?></a>
				<?php  } else{?>
				<a href="javascript:;" title="<?php echo $i_langpackage->i_noapprove_company;?>"  class="shop_cert2 left"><?php echo $i_langpackage->i_noapprove_company;?></a>
				<?php }?>
				</p>
              </div>
			  <ul class="attribute">
			    <li class="operate"><a class="more" href="<?php echo  goods_url($v['goods_id']);?>"><?php echo $i_langpackage->i_particular;?></a><a class="buy" href="modules.php?app=user_order&gid=<?php echo $v['goods_id'];?>&v=1"><?php echo $i_langpackage->i_buy;?></a></li>
			    <li class="place"><?php echo  $areainfo[$v['shop_province']];?>.<?php echo  $areainfo[$v['shop_city']];?><br /><br /><script src="imshow.php?u=<?php echo  $v['user_id'];?>"></script></li>
			    <li class="price">￥<?php echo  $v['goods_price']=='0.00' ? $i_langpackage->i_price_discuss : $v['goods_price'];?></li>
		      </ul>
			  <div class="showbox" id="showbox_<?php echo $v['goods_id'];?>" style="display:none">
				<div class="subbox"><img id="showimg_<?php echo $v['goods_id'];?>" src="<?php echo  $v['is_set_image'] ? $v['goods_thumb'] : 'skin/default/images/nopic.gif';?>" width="234" height="234" alt="正在加载清晰图片" /> </div>
			  </div>
		  </li>
			<?php }?>
		<?php  }else {?>
			<li><?php echo $i_langpackage->i_no_goods;?>！</li>
		<?php }?>
		</ul>
	<div class="page"><span><?php echo  str_replace("{num}",$result['countnum'],$i_langpackage->i_page_num);?></span><a href="search.php<?php echo  $result['firsturl'];?>" ><?php echo  $i_langpackage->i_page_first;?></a><span><a href="search.php<?php echo  $result['preurl'];?>"><?php echo  $i_langpackage->i_page_pre;?></a></span><a href="search.php<?php echo  $result['nexturl'];?>" ><?php echo  $i_langpackage->i_page_next;?></a><a href="search.php<?php echo  $result['lasturl'];?>" ><?php echo  $i_langpackage->i_page_last;?></a><span><?php echo  $i_langpackage->i_page_now;?><?php echo  $result['page'];?>/<?php echo  $result['countpage'];?></span><span><?php echo  str_replace("{num}",$result['countpage'],$i_langpackage->i_page_count);?></span></div>
	</div>
</div>

<!-- main end -->
<div class="main_right2 left10 top5">
    <div class="recommend2">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $i_langpackage->i_boutique;?></h2><span class="right"><a href="search.php?best=1"><?php echo $i_langpackage->i_more;?>>></a></span></div>
        <div class="c_m">
        	<ul>
              <?php  foreach($goods_best as $value) {?>
			    <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $value['goods_name'];?>" /> <span><?php echo  sub_str($value['goods_name'],8,false);?></span></a><label>￥<?php echo  $value['goods_price'];?>元</label></li>
			  <?php }?>
			</ul>
        </div>
	</div>
    <div class="rank">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $i_langpackage->i_hot;?></h2><span class="right"><a href="search.php?hot=1"><?php echo $i_langpackage->i_more;?>>></a>&nbsp;</span></div>
        <div class="c_m">
		<ul id="promote_goods" class="cls clear">
		<?php $i=1;?>
		<?php   foreach($goods_hot as $value){?>
			<li onmouseover="promote_change(this)"><span class="num"><?php echo $i;?></span><a href="<?php echo  goods_url($value['goods_id']);?>"><img height="60" width="60" src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $value['goods_name'];?>" /> <span title="<?php echo  $value['goods_name'];?>"><?php echo  sub_str($value['goods_name'],8,false);?></span></a><div class="price"><span>￥<?php echo $value['goods_price'];?></span></div></li>
			<?php $i++;?>
		<?php }?>
		</ul>
		</div>
	</div>
    <div class="recommend2">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $i_langpackage->i_new;?></h2></div>
        <div class="c_m">
        	<ul>
			<?php   foreach($goodshistory as $value){?>
              <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" /></a><a class="pro_name" href="<?php echo  goods_url($value['goods_id']);?>"><?php echo  sub_str($value['goods_name'],12,false);?></a><label>￥<?php echo $value['goods_price'];?></label></li>
			<?php }?>
        	</ul>
        </div>
	</div>
</div>
<?php }?>
<!-- main end -->
<!--main right end-->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function showbox(id) {
	document.getElementById("showli_"+id).className = "list_item now";
	document.getElementById("showbox_"+id).style.display = '';
	var showimg = document.getElementById("showimg_"+id);
	if(showimg.alt=='<?php echo $i_langpackage->i_loading_img;?>') {
		ajax("do.php?act=goods_get_imgurl","POST","id="+id,function(data){
			if(data) {
				showimg.src = data;
				showimg.alt = '';
			}
		});
	}
}
function hidebox(id) {
	document.getElementById("showli_"+id).className = "list_item";
	document.getElementById("showbox_"+id).style.display = 'none';
}
</script>
</body>
</html>
<?php } ?>