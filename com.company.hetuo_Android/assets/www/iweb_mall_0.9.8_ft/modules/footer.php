<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/footer.html
 * 如果您的模型要进行修改，请修改 models/modules/footer.php
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
if(filemtime("templates/default/modules/footer.html") > filemtime(__file__) || (file_exists("models/modules/footer.php") && filemtime("models/modules/footer.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/footer.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><div class="foot apart"><div class="link"><a href="article/2.html"><?php echo  $m_langpackage->m_frequently_asked_questions;?></a>|<a href="article/3.html"><?php echo  $m_langpackage->m_secure_transaction;?></a>|<a href="article/4.html"><?php echo  $m_langpackage->m_purchase_process;?></a>|<a href="article/5.html"><?php echo  $m_langpackage->m_how_to_pay;?></a>|<a href="article/6.html"><?php echo  $m_langpackage->m_contact_us;?></a>|<a href="article/7.html"><?php echo  $m_langpackage->m_cooperation_proposal;?></a>|<a href="article/8.html"><?php echo  $m_langpackage->m_site_map;?></a></div>
<p>Powered by <a href="http://www.jooyea.net"><?php echo  $SYSINFO['sys_name'];?><?php echo  $SYSINFO['version'];?></a> <?php echo  $SYSINFO['sys_company'];?></p><?php echo  $SYSINFO['sys_copyright'];?> <?php echo  $SYSINFO['sys_icp'];?></div><?php } ?>