<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/header.html
 * 如果您的模型要进行修改，请修改 models/shop/header.php
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
if(filemtime("templates/default/shop/header.html") > filemtime(__file__) || (file_exists("models/shop/header.php") && filemtime("models/shop/header.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/header.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
 
?><?php $plugins=array();?>
<div class="header">
    <div class="top">
        <div class="top_left left"></div>
            <div class="top_center left">
                <span class="left"><a href="search.php"><?php echo  $s_langpackage->s_want_buy;?></a>|<a href="modules.php?app=goods_add"><?php echo  $s_langpackage->s_want_sell;?></a>|<a href="modules.php?app=shop_info"><?php echo $s_langpackage->s_want_mkstore;?></a></span>
                <span class="right top_ico"><a href="<?php echo article_list_url(2);?>"><?php echo $s_langpackage->s_shop_help;?></a><a href="index.php"><?php echo $s_langpackage->s_back_index;?></a></span>
            </div>
         <div class="top_right left"></div>
    </div>
    <!--top end -->
    <?php  if($SYSINFO['sys_logo']){?>
		<div class="logo left">
	    	<a href="index.php"><img style="-moz-background-clip:border;-moz-background-inline-policy:continuous;-moz-background-origin:padding;display:block;height:43px;margin:8px 0 0 8px;overflow:hidden;text-indent:9999px;width:177px;" src="<?php echo  $SYSINFO['sys_logo'];?>"/>
</a>
	    </div>
	<?php } else {?>
		<div class="logo left">
	    	<h1><a href="index.php">jooyea</a></h1>
	    </div>
	<?php }?>
	<div class="tool left">
		<div class="tool_right"><a href="modules.php?app=user_favorite"><?php echo  $s_langpackage->s_favorite;?></a></div>
    </div>
    <div class="tool left">
        <div class="tool_right"><a href="modules.php?app=user_cart"><?php echo  $s_langpackage->s_cart;?></a></div>
    </div>
<?php  if($USER['login']){?>
	<div class="tool left">
        <div class="tool_right"><a href="do.php?act=logout"><?php echo  $s_langpackage->s_logout;?></a></div>
    </div>
	<div class="tool left">
        <div class="tool_right"><a href="modules.php"><?php echo  $s_langpackage->s_u_center;?></a></div>
    </div>
	<div class="welcome left">
        <div class="welcome_right"><?php echo  $s_langpackage->s_hi;?>，<?php echo  $USER['user_name'];?></div>
    </div>
<?php } else {?>
    <div class="tool left">
        <div class="tool_right"><a href="modules.php?app=reg"><?php echo $s_langpackage->s_register_free;?></a></div>
    </div>
    <div class="tool left">
        <div class="tool_right"><a href="login.php"><?php echo $s_langpackage->s_login;?></a></div>
    </div>
<?php }?>

</div><?php } ?>