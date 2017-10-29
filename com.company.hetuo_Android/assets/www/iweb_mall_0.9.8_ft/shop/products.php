<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/products.html
 * 如果您的模型要进行修改，请修改 models/shop/products.php
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
if(filemtime("templates/default/shop/products.html") > filemtime(__file__) || (file_exists("models/shop/products.php") && filemtime("models/shop/products.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/products.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_users.php");


//引入语言包
$s_langpackage=new shoplp;

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";

/* 商铺信息处理 */
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}
$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks[0];

$header['title'] = $s_langpackage->s_products."- ".$SHOP['shop_name'];
$header['keywords'] = $SHOP['shop_management'];
$header['description'] = sub_str(strip_tags($SHOP['shop_intro']),100);

$sql = "select goods_id,goods_name,goods_price,goods_thumb,is_set_image from `$t_goods` where shop_id='$shop_id' and is_on_sale=1 order by sort_order asc,goods_id desc";
$result = $dbo->fetch_page($sql,20);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo  $header['title'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="keywords" content="<?php echo  $header['keywords'];?>" />
<meta name="description" content="<?php echo  $header['description'];?>" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/shop_<?php echo  $SHOP['shop_template'];?>.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<style>
.shop_header {background:#4A9DA5;}
<?php if($SHOP['shop_template_img']){?>
.shop_header {background:transparent url(<?php echo  $SHOP['shop_template_img'];?>) no-repeat scroll 0 0;}
<?php }?>
</style>
</head>
<body>
<div id="wrapper">
	<?php  require("shop/index_header.php");?>
<div class="clear"></div>
<div class="shop_header">
	<h1><?php echo  $SHOP['shop_name'];?></h1>
	<?php  require("shop/menu.php");?>
</div>
	<?php  require("shop/left.php");?>
<div class="bigpart">
	<div class="pro_list top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="<?php echo  shop_url($shop_id,'products');?>"><?php echo $s_langpackage->s_shop_allgoods;?></a></h2><span class="right"><a id="window" onclick="changeStyle2('window',this)" href="javascript:void(0);"  class="selected" hidefocus="true"><?php echo $s_langpackage->s_shop_smallimg;?></a><a id="list" onclick="changeStyle2('list',this)"  href="javascript:void(0);" hidefocus="true"><?php echo $s_langpackage->s_shop_bigimg;?></a></span></div>
		<div class="c_m">
		 <ul id="pro_list">
		 <?php 
		if($result['result']) {
			foreach($result['result'] as $v){?>
			<li><a href="<?php echo  goods_url($v['goods_id']);?>"><img src="<?php echo  $v['is_set_image'] ? $v['goods_thumb'] : 'skin/default/images/nopic.gif';?>" width="95" height="95" alt="<?php echo  $v['goods_name'];?>" /></a><a class="pro_name" href="<?php echo  goods_url($v['goods_id']);?>" title="<?php echo  $v['goods_name'];?>"><?php echo  sub_str($v['goods_name'],22,false);?></a><label>￥<?php echo  $v['goods_price'];?><?php echo  $s_langpackage->s_yan;?></label></li>
		<?php }?>
		 <?php  }else {
			echo $s_langpackage->s_no_goods;
		}?>
		</ul>
		<div style="clear:both;">&nbsp;</div>
		<div class="bottom">
			<span><?php echo  str_replace("{num}",$result['countnum'],$s_langpackage->s_page_num);?></span>
			<a href="<?php echo  shop_url($shop_id,'products',$result['firstpage']);?>" ><?php echo  $s_langpackage->s_page_first;?></a>
			<a href="<?php echo  shop_url($shop_id,'products',$result['prepage']);?>"><?php echo  $s_langpackage->s_page_pre;?></a>
			<a href="<?php echo  shop_url($shop_id,'products',$result['nextpage']);?>" ><?php echo  $s_langpackage->s_page_next;?></a>
			<a href="<?php echo  shop_url($shop_id,'products',$result['lastpage']);?>" ><?php echo  $s_langpackage->s_page_last;?></a>
			<span><?php echo  $s_langpackage->s_page_now;?><?php echo  $result['page'];?>/<?php echo  $result['countpage'];?></span>
			<span><?php echo  str_replace("{num}",$result['countpage'],$s_langpackage->s_page_count);?></span>
		</div>
		</div>
	</div>
</div>
	<?php  require("shop/footer.php");?>
</div>
</body>
</html><?php } ?>