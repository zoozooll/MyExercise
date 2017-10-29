<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/brand.html
 * 如果您的模型要进行修改，请修改 models/brand.php
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
if(filemtime("templates/default/brand.html") > filemtime(__file__) || (file_exists("models/brand.php") && filemtime("models/brand.php") > filemtime(__file__)) ) {
	tpl_engine("default","brand.html",1);
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

//引入语言包
$i_langpackage=new indexlp;

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_users = $tablePreStr."users";
$t_goods = $tablePreStr."goods";
$t_areas = $tablePreStr."areas";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();


/* 产品处理 */
$sql_best = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_best=1 order by pv desc limit 4";
$sql_hot = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_hot=1 order by pv desc limit 10";
$goods_best = $dbo->getRs($sql_best);
$goods_hot = $dbo->getRs($sql_hot);

/* 地区信息 */
$areainfo = get_areas_kv($dbo,$t_areas);

/* 列表处理 */

$k = short_check(get_args('k'));

$sql = "SELECT * FROM `$t_shop_info` as a, `$t_users` as b WHERE a.user_id=b.user_id and open_flg=0";
if($k) {
	$sql .= " and a.shop_name LIKE '%$k%' ";
}
$sql .= " ORDER BY shop_creat_time DESC";

$result = $dbo->fetch_page($sql,$SYSINFO['seller_page']);

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

$search_type = 'brand';

$header['title'] = $i_langpackage->i_s_company." - ".$SYSINFO['sys_title'];
$header['keywords'] = $i_langpackage->i_s_company.','.$SYSINFO['sys_keywords'];
$header['description'] = $SYSINFO['sys_description'];
/*导航位置*/
$nav_selected=3;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo  $header['title'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
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
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <a href="brand.php"><?php echo $i_langpackage->i_store_list;?></a></div>
<div class="main2 left">
	<div class="top">
	  <h2><a href="#"><?php echo $i_langpackage->i_like_storelist;?></a></h2>
	</div>
	<div id="listcontent" class="c_m">
		<ul>
			<li class="list_item_title">
				<ul><li class="place"><?php echo $i_langpackage->i_in_area;?></li>
				<li class="level"><?php echo $i_langpackage->i_is_vis;?></li>
				<li class="seller"><?php echo $i_langpackage->i_shopour;?></li>
				<li class="price"><?php echo $i_langpackage->i_goods_num;?></li>
				<li class="summary2"><?php echo $i_langpackage->i_s_com;?></li>
				<li class="logo"><?php echo $i_langpackage->i_shop_logo;?></li></ul>
			</li>
			<?php  if($result['result']) {
				foreach($result['result'] as $v) {?>
			<li class="list_item list_item2">
				<div class="photo2"><a href="<?php echo  shop_url($v['shop_id']);?>"><img src="<?php echo  $v['shop_logo'] ? $v['shop_logo'] : 'skin/default/images/shop_nologo.gif';?>" width="99" height="49" alt="" /></a></div>
                <div class="summary2"><h3><a href="<?php echo  shop_url($v['shop_id']);?>"><?php echo  $v['shop_name'];?></a></h3><p>[<?php echo  $i_langpackage->i_info;?>]<?php echo  sub_str(strip_tags($v['shop_intro']),28);?></p></div>
				<ul class="attribute">
                    <li class="place2"><?php echo  $areainfo[$v['shop_province']];?>.<?php echo  $areainfo[$v['shop_city']];?></li>
                    <li class="level"><?php  if($v['rank_id']>2){?><a href="javascript:;" title="<?php echo $i_langpackage->i_approve_company;?>"  class="shop_cert left"><?php echo $i_langpackage->i_approve_company;?></a><?php  } else{?><a href="javascript:;" title="<?php echo $i_langpackage->i_noapprove_company;?>"  class="shop_cert2 left"><?php echo $i_langpackage->i_noapprove_company;?></a>
                    <?php }?>
                    </li>
					<li class="seller"><a class="name" href="<?php echo  shop_url($v['shop_id']);?>"><?php echo  $v['user_name'];?></a></li>
					<li class="num"><?php echo  $v['goods_num'];?></li>
                </ul>
			</li>
			<?php   }} else {
				echo "<li>".$i_langpackage->i_comapny_none."</li>";
			}?>
		</ul>
		<div class="page"><span><?php echo  str_replace("{num}",$result['countnum'],$i_langpackage->i_page_num);?></span><a href="brand.php<?php echo  $result['firsturl'];?>" ><?php echo  $i_langpackage->i_page_first;?></a><span><a href="brand.php<?php echo  $result['preurl'];?>"><?php echo  $i_langpackage->i_page_pre;?></a></span><a href="brand.php<?php echo  $result['nexturl'];?>" ><?php echo  $i_langpackage->i_page_next;?></a><a href="brand.php<?php echo  $result['lasturl'];?>" ><?php echo  $i_langpackage->i_page_last;?></a><span><?php echo  $i_langpackage->i_page_now;?><?php echo  $result['page'];?>/<?php echo  $result['countpage'];?></span><span><?php echo  str_replace("{num}",$result['countpage'],$i_langpackage->i_page_count);?></span></div>
	</div>
</div>

<!-- main end -->
<div class="main_right2 left10">
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
		<?php  foreach($goods_hot as $value){?>
			<li onmouseover="promote_change(this)"><span class="num"><?php echo $i;?></span><a href="<?php echo  goods_url($value['goods_id']);?>"><img height="60" width="60" src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $value['goods_name'];?>" /> <span title="<?php echo  $value['goods_name'];?>"><?php echo  sub_str($value['goods_name'],14,false);?></span></a><div class="price"><span>￥<?php echo $value['goods_price'];?></span></div></li>
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
			<?php  foreach($goodshistory as $value){?>
              <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" /></a><a class="pro_name" href="<?php echo  goods_url($value['goods_id']);?>"><?php echo  sub_str($value['goods_name'],20,false);?></a><label>￥<?php echo $value['goods_price'];?></label></li>
			<?php }?>
        	</ul>
        </div>
	</div>
</div>
<!--main right end-->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>
</body>
</html>
<?php } ?>