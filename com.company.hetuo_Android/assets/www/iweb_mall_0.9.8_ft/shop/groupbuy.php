<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/groupbuy.html
 * 如果您的模型要进行修改，请修改 models/shop/groupbuy.php
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
if(filemtime("templates/default/shop/groupbuy.html") > filemtime(__file__) || (file_exists("models/shop/groupbuy.php") && filemtime("models/shop/groupbuy.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/groupbuy.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_users.php");
require("foundation/flefttime.php");
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
$t_shop_groupbuy = $tablePreStr."groupbuy";

/* 商铺信息处理 */
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}
$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks[0];

$header['title'] = "团购展示 - ".$SHOP['shop_name'];
$header['keywords'] = $SHOP['shop_management'];
$header['description'] = sub_str(strip_tags($SHOP['shop_intro']),100);
/* 时间处理 */
$now_time = new time_class();
$now_time = $now_time -> short_time();
$sql = "SELECT b.*,g.* FROM `$t_shop_groupbuy` b left join `$t_goods` g on b.goods_id = g.goods_id";
$sql .= " WHERE b.shop_id='$shop_id' and b.recommended = 0 and g.lock_flg =0";
$sql .= " and b.start_time <'$now_time' and '$now_time' < b.end_time";
$result = $dbo->fetch_page($sql,$SYSINFO['product_page']);
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
	<?php  require("shop/header.php");?>
<div class="clear"></div>
<div class="shop_header">
	<h1><?php echo  $SHOP['shop_name'];?></h1>
	<?php  require("shop/menu.php");?>
</div>
	<?php  require("shop/left.php");?>
<div class="bigpart">
	<div class="pro_list top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="<?php echo  shop_url($shop_id,'groupbuy');?>"><?php echo $s_langpackage->s_shop_groupbuy;?></a></h2></div>
		<div class="pro_show">
        <table class="tab_group" width="100%">
			<tbody>
                <tr>
                    <th class="proName"><?php echo $s_langpackage->s_groupbuy_goods;?></th>
                    <th class="groupPic"><?php echo $s_langpackage->s_groupbuy_price;?></th>
                    <th class="groupQut"><?php echo $s_langpackage->s_groupbuy_num;?></th>
                    <th class="groupName"><?php echo $s_langpackage->s_goods_name;?></th>
                    <th class="timeLeft"><?php echo $s_langpackage->s_groupbuy_time;?></th>
                </tr>
			<?php if($result['result']) {
			foreach($result['result'] as $v){?>
				<tr>
                    <td valign="middle" class="proName">
		            <div class="photo"><a href="goods.php?id=<?php echo  $v['group_id'];?>&app=groupbuyinfo"><img src="<?php echo  $v['is_set_image'] ? $v['goods_thumb'] : 'skin/default/images/nopic.gif';?>" width="95" height="95" alt="<?php echo  $v['goods_name'];?>" /></a>
		            </div><div class="proInfo">
		            	<h3 align="left">&nbsp;<a href="goods.php?id=<?php echo  $v['group_id'];?>&app=groupbuyinfo"><?php echo  sub_str($v['group_name'],22,false);?></a></h3>
		            	<p>[<?php echo $s_langpackage->s_groupbuy_shows;?>]&nbsp;&nbsp;&nbsp;<?php echo  sub_str($v['group_desc'],70,false);?></p>
		            </div></td>
                    <td class="groupPic"><em class="pic">￥<?php echo  $v['spec_price'];?><?php echo  $s_langpackage->s_yan;?></em></td>
                    <td class="groupQut"><?php echo  $v['min_quantity'];?></td>
                    <td class="groupName"><a href="goods.php?id=<?php echo  $v['goods_id'];?>"><?php echo  sub_str($v['goods_name'],22,false);?></a></td>
                    <td class="timeleft"><?php echo  time_left(strtotime($v['end_time']));?></td>
                </tr>
			<?php }?>
			<?php  }else {?>
			 <tr><td colspan="5"><?php echo  $s_langpackage->s_no_goods;?></td></tr>
			<?php }?>
			</tbody>
		</table>
		<div style=" clear:both; padding:8px 0; text-align:center;">
			<span><?php echo  str_replace("{num}",$result['countnum'],$s_langpackage->s_page_num);?></span>
			<a href="<?php echo  shop_url($shop_id,'groupbuy',$result['firstpage']);?>" ><?php echo  $s_langpackage->s_page_first;?></a>
			<a href="<?php echo  shop_url($shop_id,'groupbuy',$result['prepage']);?>"><?php echo  $s_langpackage->s_page_pre;?></a>
			<a href="<?php echo  shop_url($shop_id,'groupbuy',$result['nextpage']);?>" ><?php echo  $s_langpackage->s_page_next;?></a>
			<a href="<?php echo  shop_url($shop_id,'groupbuy',$result['lastpage']);?>" ><?php echo  $s_langpackage->s_page_last;?></a>
			<span><?php echo  $s_langpackage->s_page_now;?><?php echo  $result['page'];?>/<?php echo  $result['countpage'];?></span>
			<span><?php echo  str_replace("{num}",$result['countpage'],$s_langpackage->s_page_count);?></span>
		</div>
		</div>
		</div>
	</div>
</div>
	<?php  require("shop/footer.php");?>
</div>
</body>
</html><?php } ?>