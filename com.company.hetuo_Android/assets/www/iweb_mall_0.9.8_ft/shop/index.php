<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/index.html
 * 如果您的模型要进行修改，请修改 models/shop/index.php
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
if(filemtime("templates/default/shop/index.html") > filemtime(__file__) || (file_exists("models/shop/index.php") && filemtime("models/shop/index.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/index.html",1);
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


//引入语言包
$s_langpackage=new shoplp;
$i_langpackage=new indexlp;
/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_shop_guestbook = $tablePreStr."shop_guestbook";

/* 商铺信息处理 */
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);

if(!$SHOP) { exit("没有此商铺！");}
if($SHOP['lock_flg']) { exit("此商铺已锁定！");}
if($SHOP['open_flg']) { exit("此商铺已关闭！");}

$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks[0];

$header['title'] = $s_langpackage->s_shop_index." - ".$SHOP['shop_name'];
$header['keywords'] = $SHOP['shop_management'];
$header['description'] = sub_str(strip_tags($SHOP['shop_intro']),100);

/* 本页面信息处理 */
$sql = "select goods_id,goods_name,goods_price,goods_thumb,is_set_image from `$t_goods` where shop_id='$shop_id' and is_best=1 and is_on_sale=1 and lock_flg=0 order by sort_order asc,goods_id desc limit 12";
$best_goods = $dbo->getRs($sql);

$sql = "select goods_id,goods_name,goods_price,goods_thumb,is_set_image from `$t_goods` where shop_id='$shop_id' and is_on_sale=1 and lock_flg=0 order by sort_order asc,goods_id desc limit 12";
$new_goods = $dbo->getRs($sql);


$sql = "SELECT * FROM $t_shop_guestbook WHERE shop_id='$shop_id' AND shop_del_status='1' order by add_time desc limit 10";
$guestbook_list = $dbo->getRs($sql);
$nav_selected="";
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
	<div class="shop_notice top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $s_langpackage->s_shop_notices;?></h2></div>
		<div class="c_m"><?php echo $SHOP['shop_notice'];?></div>
	</div>
	<div class="pro_list top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="<?php echo  shop_url($shop_id,'products');?>"><?php echo $s_langpackage->s_shop_allgoods;?></a></h2><span class="right"><a id="window" onclick="changeStyle2('window',this)" href="javascript:void(0);"  class="selected" hidefocus="true"><?php echo $s_langpackage->s_shop_smallimg;?></a><a id="list" onclick="changeStyle2('list',this)"  href="javascript:void(0);" hidefocus="true"><?php echo $s_langpackage->s_shop_bigimg;?></a></span></div>
			<div class="c_m">
			 <ul id="pro_list">
			 <?php if($best_goods) {
			foreach($new_goods as $value){?>
              <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" width="95" height="95" alt="<?php echo  $value['goods_name'];?>" /></a><a class="pro_name" href="<?php echo  goods_url($value['goods_id']);?>" title="<?php echo  $value['goods_name'];?>"><?php echo  sub_str($value['goods_name'],22,false);?></a><label>￥<?php echo  $value['goods_price'];?><?php echo $s_langpackage->s_yuan;?></label></li>
        	<?php }?>
			<?php }?>
            	<div class="clear"></div>
        	</ul>
			</div>
	</div>
	<div class="guestbook top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="javascript:;"><?php echo $s_langpackage->s_buyer_comm;?></a></h2></div>
			<div class="c_m">
			<?php foreach($guestbook_list as $value){?>
				<dl>
					<dt><label class="ftcolor"><?php echo  $value['name'];?>：</label><?php echo  $value['content'];?> <span><?php echo  $value['add_time'];?></span>
					</dt>
					<?php if($value['reply']) {?><dd><?php echo $s_langpackage->s_seller_reply;?>：<?php echo  $value['reply'];?></dd><?php }?>
				</dl>
			<?php }?>
				<div class="messagebox"><div id="sendbox" style="display:none;">
				<form action="do.php?act=shop_guestbook" name="guestForm" method="post" id="guestForm" onSubmit="return checkForm();">
				<input maxlength="20" name="name" type="hidden" value="<?php echo $USER['user_name'];?>" />
				<input maxlength="50" name="email" type="hidden" value="<?php echo $USER['user_email'];?>" />
				<input maxlength="50" name="contact" type="hidden" />
				<input type="hidden" name="shop_id" value="<?php echo  $shop_id;?>" />
				<input type="hidden" name="shop_name" value="<?php echo  $SHOP['shop_name'];?>" />
					<textarea cols="40" rows="4" name="content" id="textareac"></textarea><br/ >
					<input class="button" type="submit" value="<?php echo $s_langpackage->s_post_comm;?>" />
				</form>
				</div><input class="button" type="button" value="<?php echo $s_langpackage->s_wantto_comm;?>" onclick="this.style.display='none';document.getElementById('sendbox').style.display=''" /></div>
			</div>
	</div>
</div>
	<?php   require("shop/footer.php");?>
</div>
<script>
function checkForm() {
	var name = document.getElementsByName("name")[0];
	var textareac = document.getElementById("textareac")[0];
	if(!name.value) {
		alert("<?php echo $s_langpackage->s_login_pls;?>");
		return false;
	}
	if(textareac.value) {
		alert("<?php echo $s_langpackage->s_type_comm_pls;?>");
		return false;
	}
	return true;
}

</script>
</body>
</html>
<?php } ?>