<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/footer.html
 * 如果您的模型要进行修改，请修改 models/shop/footer.php
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
if(filemtime("templates/default/shop/footer.html") > filemtime(__file__) || (file_exists("models/shop/footer.php") && filemtime("models/shop/footer.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/footer.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("foundation/asystem_info.php");
?><div class="footer top10 clear"><a href="<?php echo  article_url(2);?>"><?php echo $s_langpackage->s_question_see;?></a>|<a href="<?php echo  article_url(3);?>"><?php echo $s_langpackage->s_safe_compp;?></a>|<a href="<?php echo  article_url(4);?>"><?php echo $s_langpackage->s_process_of_purchase;?></a>|<a href="<?php echo  article_url(5);?>"><?php echo $s_langpackage->s_howto_pay;?></a>|<a href="<?php echo  article_url(6);?>"><?php echo $s_langpackage->s_contact_us;?></a>|<a href="<?php echo  article_url(7);?>"><?php echo $s_langpackage->s_make_a_proposal;?></a>|<a href="<?php echo  article_url(8);?>"><?php echo $s_langpackage->s_site_map;?></a></div>
<div class="copyright"><p>Powered by <a href="http://www.jooyea.net" style="font-weight:bold;">iWebMall <?php echo  $SYSINFO['version'];?></a> <?php echo  $SYSINFO['sys_company'];?></p><?php echo  $SYSINFO['sys_copyright'];?> <?php echo  $SYSINFO['sys_icp'];?> <?php echo  $SYSINFO['sys_countjs'];?>
</div><?php } ?>