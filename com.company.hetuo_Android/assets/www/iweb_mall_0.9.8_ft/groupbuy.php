<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/groupbuy.html
 * 如果您的模型要进行修改，请修改 models/groupbuy.php
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
if(filemtime("templates/default/groupbuy.html") > filemtime(__file__) || (file_exists("models/groupbuy.php") && filemtime("models/groupbuy.php") > filemtime(__file__)) ) {
	tpl_engine("default","groupbuy.html",1);
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
require("foundation/flefttime.php");

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
$s_langpackage=new shoplp;

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_shop_groupbuy = $tablePreStr."groupbuy";
$t_areas = $tablePreStr."areas";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

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


$header['title'] = "团购展示 "." - ".$SYSINFO['sys_title'];
$header['keywords'] = "团购展示 ".','.$SYSINFO['sys_keywords'];
$header['description'] = $SYSINFO['sys_description'];

/* 时间处理 */
$now_time = new time_class();
$now_time = $now_time -> short_time();

$sql = "SELECT b.*,g.* FROM `$t_shop_groupbuy` b left join `$t_goods` g on b.goods_id = g.goods_id";
$sql .= " WHERE b.recommended = 0 and g.lock_flg =0";
$sql .= " and b.start_time <'$now_time' and '$now_time' < b.end_time";
$result = $dbo->fetch_page($sql,$SYSINFO['product_page']);


$nav_selected =6;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo  $header['title'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="keywords" content="<?php echo  $header['keywords'];?>" />
<meta name="description" content="<?php echo  $header['description'];?>" />
<meta http-equiv="X-UA-Compatible" content="IE=E0mulateIE7" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<form name="category_form" id="category_form" method="POST" action="<?php echo  category_url($cat_id);?>">
<input type="hidden" name="name" value="">
<input type="hidden" name="order" value="">
<!--header end -->
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a>>团购列表</div>

<div class="top5 clear"></div>
<div class="main2 left top5">
	<div class="top">
	  <span class="right"><a id="list" onclick="changeStyle2('list',this)" class="selected" href="javascript:void(0);" hidefocus="true"><?php echo $i_langpackage->i_list;?></a><a id="window" onclick="changeStyle2('window',this)" href="javascript:void(0);" hidefocus="true"><?php echo $i_langpackage->i_show_window;?></a></span>
	  <h2><a href="#"><?php echo $i_langpackage->i_choice_good;?></a></h2>
	</div>
	<div class="groupShow clearfix">
		<table class="tab_group " width="100%">
			<tbody>
                <tr>
                    <th class="proName"><?php echo $s_langpackage->s_groupbuy_goods;?></th>
                    <th class="groupPic"><?php echo $s_langpackage->s_groupbuy_price;?></th>
                    <th class="groupQut"><?php echo $s_langpackage->s_groupbuy_num;?></th>
                    <th class="groupName"><?php echo $s_langpackage->s_goods_name;?></th>
                    <th class="timeLeft"><?php echo $s_langpackage->s_groupbuy_time;?></th>
                </tr>
			<?php  if($result['result']) {
			foreach($result['result'] as $v){?>
				<tr>
                    <td valign="middle" class="proName">
		            <div class="photo"><a href="goods.php?id=<?php echo  $v['group_id'];?>&app=groupbuyinfo"><img src="<?php echo  $v['is_set_image'] ? $v['goods_thumb'] : 'skin/default/images/nopic.gif';?>" width="95" height="95" alt="<?php echo  $v['goods_name'];?>" /></a>
		            </div><div class="proInfo"><h3><a href="goods.php?id=<?php echo  $v['group_id'];?>&app=groupbuyinfo"><?php echo  sub_str($v['group_name'],22,false);?></a></h3>
		            <p>[团购说明]<?php echo  $v['group_desc'];?></p>
		            </div></td>
                    <td class="groupPic"><em class="pic">￥<?php echo  $v['spec_price'];?><?php echo  $s_langpackage->s_yan;?></em></td>
                    <td class="groupQut"><?php echo  $v['min_quantity'];?></td>
                    <td class="groupName"><a href="goods.php?id=<?php echo  $v['goods_id'];?>"><?php echo  sub_str($v['goods_name'],22,false);?></a></td>
                    <td class="timeleft"><?php echo  time_left(strtotime($v['end_time']));?></td>
                </tr>
			<?php }?>
			<?php  }else {?>
				<tr><?php echo $i_langpackage->i_no_goods;?>！</tr>
			<?php }?>
			</tbody>
		</table>
		<div class="page">
			<span><?php echo  str_replace("{num}",$result['countnum'],$s_langpackage->s_page_num);?></span>
			<a href="<?php echo  shop_url('groupbuy',$result['firstpage']);?>" ><?php echo  $s_langpackage->s_page_first;?></a>
			<a href="<?php echo  shop_url('groupbuy',$result['prepage']);?>"><?php echo  $s_langpackage->s_page_pre;?></a>
			<a href="<?php echo  shop_url('groupbuy',$result['nextpage']);?>" ><?php echo  $s_langpackage->s_page_next;?></a>
			<a href="<?php echo  shop_url('groupbuy',$result['lastpage']);?>" ><?php echo  $s_langpackage->s_page_last;?></a>
			<span><?php echo  $s_langpackage->s_page_now;?><?php echo  $result['page'];?>/<?php echo  $result['countpage'];?></span>
			<span><?php echo  str_replace("{num}",$result['countpage'],$s_langpackage->s_page_count);?></span>
		</div>
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
			    <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $value['goods_name'];?>" /> <span><?php echo  sub_str($value['goods_name'],20,false);?></span></a><label>￥<?php echo  $value['goods_price'];?><?php echo $i_langpackage->i_yan;?></label></li>
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
</form>

</body>
</html><?php } ?>