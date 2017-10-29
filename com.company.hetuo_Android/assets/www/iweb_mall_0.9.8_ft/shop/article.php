<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/article.html
 * 如果您的模型要进行修改，请修改 models/shop/article.php
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
if(filemtime("templates/default/shop/article.html") > filemtime(__file__) || (file_exists("models/shop/article.php") && filemtime("models/shop/article.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/article.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

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
$t_article = $tablePreStr."article";
$t_article_cat = $tablePreStr."article_cat";

$sql = "SELECT * FROM `$t_article_cat` order by sort_order asc";
$article_cat = $dbo->getRs($sql);
if(!$article_cat) {
	exit("没有分类！");
}

$sql = "SELECT * FROM `$t_article` WHERE is_show=1 and article_id='$article_id'";
$article_info = $dbo->getRow($sql);
if(!$article_info) {
	exit("不存在此资讯！");
}

foreach ($article_cat as $val){
	if($val['cat_id']==$article_info['cat_id']){
		$cat_name=$val['cat_name'];
	}
}

$sql="SELECT * FROM $t_article WHERE article_id < $article_id ORDER BY article_id DESC LIMIT 1";
$up_article=$dbo->getRow($sql);
$sql="SELECT * FROM $t_article WHERE article_id > $article_id ORDER BY article_id ASC LIMIT 1";
$down_article=$dbo->getRow($sql);

if($article_info['is_link'] && $article_info['link_url']) {
	echo "<script>location.href = '".$article_info['link_url']."'</script>";
	exit;
}

$header['title'] = $article_info['title'];
$header['keywords'] = $article_info['title'];
$header['description'] = sub_str(strip_tags($article_info['content']),100);
$nav_selected=5;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
	<?php  require("shop/index_header.php");?>
<!--search end -->
<div class="asd top5 clear"><script language="JavaScript" src="uploadfiles/asd/1.js"></script></div>
<div class="main_right2 left top5">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $i_langpackage->i_article_list;?></h2></div>

        <div class="c_m article_list">
            <ul>
            <?php foreach($article_cat as $val){?>
                <li><a href="<?php echo article_list_url($val['cat_id']);?>"><?php echo $val['cat_name'];?></a></li>
            <?php }?>
            </ul>
        </div>
</div>
<!--main right end-->
<div class="main2 right top5">
	<div class="path" style="padding:2px 0 6px; line-height:14px;"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <a href="<?php echo article_list_url($article_info['cat_id']);?>"><?php echo $cat_name;?></a> > <?php echo  $article_info['title'];?></div>
    <div class="article pro_class">
    	<span><?php echo  $s_langpackage->s_time;?>: <?php echo  substr($article_info['add_time'],0,10);?></span>
    <h1><?php echo  $article_info['title'];?></h1>
		<p><?php echo  $article_info['content'];?></p>
        <div class="next_page"><?php echo $i_langpackage->i_up_article;?>：<?php if(empty($up_article)){?> <?php echo $i_langpackage->i_none_article;?> <?php }?><a href="<?php echo article_url($up_article['article_id']);?>"><?php echo $up_article['title'];?></a><br/><?php echo $i_langpackage->i_down_article;?>：<?php if(empty($down_article)){?> <?php echo $i_langpackage->i_none_article;?> <?php }?><a href="<?php echo article_url($down_article['article_id']);?>"><?php echo $down_article['title'];?></a></div>
	</div>
    
</div><div class="clear"></div>
<!-- main end -->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>
</body>
</html>
<?php } ?>