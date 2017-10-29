<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/inquiry.html
 * 如果您的模型要进行修改，请修改 models/shop/inquiry.php
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
if(filemtime("templates/default/shop/inquiry.html") > filemtime(__file__) || (file_exists("models/shop/inquiry.php") && filemtime("models/shop/inquiry.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/inquiry.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$i_langpackage=new indexlp;



/* 定义文件表 */
$t_shop_info=$tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_goods = $tablePreStr."goods";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 产品信息获取 */
$sql = "select * from `$t_goods` as a,`$t_shop_info` as b where a.goods_id=$goods_id and a.shop_id=b.shop_id";
$info = $dbo->getRow($sql);
if(!$info) { exit("没有此商品!"); }

if($USER['user_id']) {
	$sql = "SELECT * FROM `$t_user_info` WHERE user_id='".$USER['user_id']."'";
	$user_info = $dbo->getRow($sql);
	$user_info['user_email'] = $USER['user_email'];
}

$nav_selected='1';
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo $i_langpackage->i_inquiry;?> - <?php echo $info['shop_name'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<base href="<?php echo $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
</head>
<style type="text/css">
table{ border:#ccc 1px solid; text-align:left}
td{ height:22px; padding:5px; border-bottom:#ccc 1px dashed;}</style>
<body>
<div id="wrapper">
  <!-- 顶部 -->
  <?php  include("shop/index_header.php");?>
  <!-- 头部 -->
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <?php echo $i_langpackage->i_iwantget_price;?> </div>
<div class="">
<form action="inquiry.php?app=post&gid=<?php echo $goods_id;?>" method="post">
<TABLE cellSpacing=2 cellPadding=0 width="100%" align=center border=0>
  <TR>
    <TD align=right width="22%"><?php echo $i_langpackage->i_send_to;?>：</TD>
    <TD width="78%"><span class="subtitle">
		<a href="shop.php?shopid=2&app=index>"><?php echo $info['shop_name'];?></a>
		<input name="shop_id" type="hidden" value="<?php echo $info['shop_id'];?>" />
		<input name="goods_id" type="hidden" value="<?php echo $info['goods_id'];?>" />
		<input name="goods_name" type="hidden" value="<?php echo $info['goods_name'];?>" />
    </span></TD></TR>
  <TR>
    <TD align=right><?php echo $i_langpackage->i_askprice_title;?>：</TD>
    <TD> <input class=InputBorder maxlength=120 size=40 name="title" type="text" value="<?php echo str_replace("{name}",$info['goods_name'],$i_langpackage->i_inq_msat);?>" /></TD></TR>
  <TR>
    <TD align=right valign="top"><?php echo $i_langpackage->i_inq_askinfo;?>：</TD>
    <TD><textarea name="content" cols="40" rows="10"></textarea></TD></TR>
  <TR>
    <TD align=right><?php echo $i_langpackage->i_inq_truename;?>：</TD>
    <TD><input name="user_truename" type="text" value="<?php echo $user_info['user_truename'];?>" /></TD></TR>
  <tr>
    <td align="right"><label><?php echo $i_langpackage->i_inq_email;?></label>：</td>
    <td><input name="user_email" type="text" value="<?php echo $user_info['user_email'];?>" /></td>
  </tr>
  <tr>
    <td align="right"><?php echo $i_langpackage->i_inq_tel;?>：</td>
    <td><input name="user_telphone" type="text" value="<?php echo $user_info['user_telphone'];?>" /></td>
  </tr>
  <tr>
    <td align="right"><?php echo $i_langpackage->i_inq_mob;?>：</td>
    <td><input name="user_mobile" type="text" value="<?php echo $user_info['user_mobile'];?>" /></td>
  </tr>
  <TR>
    <TD  style="border-bottom:0px;" align=right>&nbsp;</TD>
    <TD height="40" style="border-bottom:0px;"><input name="button" type="submit" class="button" value="<?php echo $i_langpackage->i_inq_sendmsg;?>" />
    </TD></TR>
  </TABLE></FORM>
</div>
<div class="top5 clear"></div>
<!-- 底部 -->
<?php  require("shop/index_footer.php");?>
</div>
</body>
</html><?php } ?>