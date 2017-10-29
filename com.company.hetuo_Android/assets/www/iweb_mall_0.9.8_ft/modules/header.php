<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/header.html
 * 如果您的模型要进行修改，请修改 models/modules/header.php
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
if(filemtime("templates/default/modules/header.html") > filemtime(__file__) || (file_exists("models/modules/header.php") && filemtime("models/modules/header.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/header.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php $plugins=array();?>
<div class="apart">
	<div class="topbar">
	  <p class="left"><a href="search.php"><?php echo  $m_langpackage->m_buy;?></a><a href="modules.php?app=goods_add"><?php echo  $m_langpackage->m_sell;?></a><a href="" style="background:none"><?php echo $m_langpackage->m_free_shop;?></a></p>
      <p class="right"><a href="article_list.php?id=2" ><?php echo $m_langpackage->m_shop_help;?></a><a href="index.php" ><?php echo $m_langpackage->m_back_index;?></a></p>
    </div>
    <div class="head">
    	<h1>
    		<a href="index.php">
	    		<?php  if($SYSINFO['sys_logo']){?>
					<img src="<?php echo  $SYSINFO['sys_logo'];?>"/>
				<?php } else {?>
					<img src="skin/<?php echo  $SYSINFO['templates'];?>/images/malllogo.gif" />
				<?php }?>
			</a>

    	</h1>
        <div class="menu"><div class="bg_right"></div><div class="bg_left"></div>
        	<p>
        		<a href="modules.php?app=user_cart"><?php echo  $m_langpackage->m_cart;?></a>
        		<a href="modules.php?app=user_favorite"><?php echo  $m_langpackage->m_favorite;?></a>
        		<a href="do.php?act=logout"><?php echo $m_langpackage->m_logout;?></a><a href="" class="more"><?php echo $m_langpackage->m_more;?></a>
        	</p>
        </div>
		</div>
    <div class="head_line"></div>
</div><?php } ?>