<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/menu.html
 * 如果您的模型要进行修改，请修改 models/shop/menu.php
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
if(filemtime("templates/default/shop/menu.html") > filemtime(__file__) || (file_exists("models/shop/menu.php") && filemtime("models/shop/menu.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/menu.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
$app = short_check(get_args('app'));
?>	<ul class="tabbar">
	<li <?php if($app == 'index') {?>class="selected"<?php }?>><a href="<?php echo  shop_url($shop_id,'index');?>" hideFocus=true><?php echo $s_langpackage->s_shop_indexs;?></a></li>
	<li <?php if($app == 'credit') {?>class="selected"<?php }?>><a href="<?php echo  shop_url($shop_id,'credit');?>" hideFocus=true><?php echo $s_langpackage->s_shop_credit;?></a></li>
	<li <?php if($app == 'intro') {?>class="selected"<?php }?>><a href="<?php echo  shop_url($shop_id,'intro');?>" hideFocus=true><?php echo $s_langpackage->s_shop_intro;?></a></li>
	<li <?php if($app == 'products') {?>class="selected"<?php }?>><a href="<?php echo  shop_url($shop_id,'products');?>" hideFocus=true><?php echo  $s_langpackage->s_products;?></a></li>
	<li <?php if($app == 'groupbuy') {?>class="selected"<?php }?>><a href="<?php echo  shop_url($shop_id,'groupbuy');?>" hideFocus=true><?php echo  $s_langpackage->s_shop_groupbuy;?></a></li>
	<?php if($SYSINFO['map'] == 'true' && ($SHOP['map_x']!=0 || $SHOP['map_y']!=0) ) {?>
	<li <?php if($app == 'map') {?>class="selected"<?php }?>><a href="<?php echo  shop_url($shop_id,'map');?>" hideFocus=true><?php echo $s_langpackage->s_shop_seat;?></a></li>
	<?php }?>
	<!-- plugins !-->
	<div id="shop_menu_buttun">
		<?php echo isset($plugins['shop_menu_buttun'])?show_plugins($plugins['shop_menu_buttun']):'';?>
	</div>
	<!-- plugins !-->
	</ul><?php } ?>