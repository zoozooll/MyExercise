<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/inquiry2.html
 * 如果您的模型要进行修改，请修改 models/shop/inquiry2.php
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
if(filemtime("templates/default/shop/inquiry2.html") > filemtime(__file__) || (file_exists("models/shop/inquiry2.php") && filemtime("models/shop/inquiry2.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/inquiry2.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//require("foundation/fsession.php");
//引入语言包
$i_langpackage=new indexlp;

/* 定义文件表 */
$t_shop_inquiry = $tablePreStr."shop_inquiry";
$t_users = $tablePreStr."users";

$user_id=get_sess_user_id();

$post['shop_id'] = intval(get_args('shop_id'));
$post['goods_id'] = intval(get_args('goods_id'));
$post['goods_name'] = short_check(get_args('goods_name'));
$post['user_id'] = intval($user_id);
$post['name'] = short_check(get_args('user_truename'));
$post['email'] = short_check(get_args('user_email'));
$post['mobile'] = short_check(get_args('user_mobile'));
$post['telphone'] = short_check(get_args('user_telphone'));
$post['title'] = short_check(get_args('title'));
$post['content'] = long_check(get_args('content'));
$post['add_time'] = $ctime->long_time();
$post['add_ip'] = $_SERVER['REMOTE_ADDR'];
$shop_id=$post['shop_id'];

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$item_sql = get_insert_item($post);
$sql = "insert into `$t_shop_inquiry` $item_sql";
$dbo->exeUpdate($sql);

$sql="update $t_users set inquiry_num=inquiry_num+1 where user_id=$shop_id ";
$dbo->exeUpdate($sql);

$nav_selected='1';
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo $i_langpackage->i_inquiry;?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<base href="<?php echo $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
</head>
<style type="text/css">
table{ border:#ccc 1px solid; text-align:left}
td{ height:22px; padding:5px; border-bottom:#ccc 1px dashed;}
p.ttt{margin:20px auto;}
</style>
<body>
<div id="wrapper">
  <!-- 顶部 -->
  <?php  include("shop/index_header.php");?>
  <!-- 头部 -->
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <?php echo $i_langpackage->i_iwantget_price;?> </div>
<div style="padding:5px; border:#ccc 1px dashed; margin:10px auto;">
<p class="ttt"><?php echo  $i_langpackage->i_inquiry_sucess;?></p>
<p class="ttt"><a href="index.php"><?php echo  $i_langpackage->i_back_index;?></a></p>
<p class="ttt"><a href="goods.php?id=<?php echo  $post['goods_id'];?>"><?php echo  $i_langpackage->i_back_goodspage;?></a></p>
</div>
<div class="top5 clear"></div>
<!-- 底部 -->
<?php  require("shop/index_footer.php");?>
</div>
</body>
</html><?php } ?>