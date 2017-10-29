<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/article_list.html
 * 如果您的模型要进行修改，请修改 models/shop/article_list.php
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
if(filemtime("templates/default/shop/article_list.html") > filemtime(__file__) || (file_exists("models/shop/article_list.php") && filemtime("models/shop/article_list.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/article_list.html",1);
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

$cat_id = intval(get_args('id'));

$sql = "SELECT * FROM `$t_article_cat` order by sort_order asc";
$article_cat = $dbo->getRs($sql);
if(!$article_cat) {
	exit("没有分类！");
}

foreach ($article_cat as $val){
	if($val['cat_id']==$cat_id){
		$cat_name=$val['cat_name'];
	}
}

$sql = "SELECT * FROM `$t_article` WHERE is_show=1";
if($cat_id){
	$sql.= " and cat_id=$cat_id";
}
$result = $dbo->fetch_page($sql,$SYSINFO['article_page']);
if(!$result) {
	exit("没有资讯！");
}

$header['title'] = $cat_name;
$header['keywords'] = $cat_name;
$header['description'] = sub_str(strip_tags($cat_name),100);
/*导航位置*/
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
	<div class="path" style="padding:2px 0 6px 0; line-height:14px;"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <?php echo $cat_name;?></div>
    <div class="article pro_class">
        <ul>
        <?php if($result['result']){?>
        <?php foreach($result['result'] as $val){?>
            <li><span class="right"><?php echo  $val['add_time'];?></span><a href="<?php echo article_list_url($val['cat_id']);?>">[<?php echo $cat_name;?>]</a> <a href="<?php echo article_url($val['article_id']);?>"><?php echo  $val['title'];?></a></li>
        <?php }?>
        <?php }else{ ;?>
        <?php echo $i_langpackage->i_none_articles;?>
        <?php }?>
        </ul>
		<div class="page"><span><?php echo  str_replace("{num}",$result['countnum'],$i_langpackage->i_page_num);?></span><a href="<?php echo  article_list_url($cat_id,$result['firstpage']);?>" ><?php echo  $i_langpackage->i_page_first;?></a><span><a href="<?php echo  article_list_url($cat_id,$result['prepage']);?>"><?php echo  $i_langpackage->i_page_pre;?></a></span><a href="<?php echo  article_list_url($cat_id,$result['nextpage']);?>" ><?php echo  $i_langpackage->i_page_next;?></a><a href="<?php echo  article_list_url($cat_id,$result['lastpage']);?>" ><?php echo  $i_langpackage->i_page_last;?></a><span><?php echo  $i_langpackage->i_page_now;?><?php echo  $result['page'];?>/<?php echo  $result['countpage'];?></span><span><?php echo  str_replace("{num}",$result['countpage'],$i_langpackage->i_page_count);?></span></div>
	</div>
</div>
<div class="clear"></div>
<!-- main end -->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>
</body>
</html>
<?php } ?>