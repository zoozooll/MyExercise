<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/index_header.html
 * 如果您的模型要进行修改，请修改 models/shop/index_header.php
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
if(filemtime("templates/default/shop/index_header.html") > filemtime(__file__) || (file_exists("models/shop/index_header.php") && filemtime("models/shop/index_header.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/index_header.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
if($USER['shop_id']) {
	$url=shop_url($USER['shop_id']);
} else {
	$url='modules.php?app=shop_info';
}
?>	<div class="apart">
    	<div class="topbar">
    	  <p class="left"><a href="search.php"><?php echo  $i_langpackage->i_want_buy;?></a><a href="modules.php?app=goods_add"><?php echo  $i_langpackage->i_want_sell;?></a><a href="modules.php?app=shop_info" style="background:none"><?php echo $i_langpackage->i_iwant_mkstore;?></a></p>
       	  <p class="right"><?php echo  $i_langpackage->i_welcome;?><?php echo $SYSINFO['sys_name'];?>!
          <?php  if($USER['login']){?><?php echo  $i_langpackage->i_hi;?>!<?php echo  $USER['user_name'];?><a href="modules.php"><?php echo  $i_langpackage->i_u_center;?></a><a href="do.php?act=logout"><?php echo  $i_langpackage->i_logout;?></a><?php } else {?><a href="login.php"><?php echo $i_langpackage->i_login;?></a><a href="modules.php?app=reg"><?php echo $i_langpackage->i_register_free;?></a><a href="<?php echo article_list_url(2);?>" style="background:none"><?php echo $i_langpackage->i_shop_help;?></a><?php }?></p>
        </div>
        <div class="head">
        	<h1><a href="index.php"><?php  if($SYSINFO['sys_logo']){?><img src="<?php echo  $SYSINFO['sys_logo'];?>"/><?php } else {?><img src="skin/<?php echo  $SYSINFO['templates'];?>/images/malllogo.gif" alt="iwebmall" /><?php }?></a></h1>
            <div class="menu">
	            <div class="bg_right"></div><div class="bg_left"></div><p><a href="modules.php?app=user_cart"><?php echo  $i_langpackage->i_cart;?></a><a href="modules.php?app=user_favorite"><?php echo  $i_langpackage->i_favorite;?></a><a href=""><?php echo  $i_langpackage->i_my_shop;?></a><a href="modules.php" class="more"><?php echo  $i_langpackage->i_more;?></a></p>
            </div>
	         <div class="nav"><div class="nav_tab"><ul class="mainnav"><div class="bg_left"></div><li onclick="changeMenu();location.href='index.php'" <?php if($nav_selected=='1') {?>class="active"<?php }?>><a href="index.php"><?php echo $i_langpackage->i_index;?></a></li><li onclick="changeMenu();location.href='brand.php'" <?php if($nav_selected=='3') {?>class="active"<?php }?>><a href="brand.php" hidefocus="true"><?php echo $i_langpackage->i_company;?></a></li><li onclick="changeMenu();location.href='search.php'" <?php if($nav_selected=='4') {?>class="active"<?php }?>><a href="search.php" hidefocus="true"><?php echo $i_langpackage->i_goods;?></a></li><li onclick="changeMenu();location.href='<?php echo  article_list_url(1);?>'" <?php if($nav_selected=='5') {?>class="active"<?php }?>><a href="<?php echo  article_list_url(1);?>" hidefocus="true"><?php echo $i_langpackage->i_news;?></a></li><li onclick="changeMenu();location.href='groupbuy.php'" <?php if($nav_selected=='6') {?>class="active"<?php }?>><a href="groupbuy.php" hidefocus="true">团购</a></li></ul></div></div>
        	<div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
   		</div>
	    <div class="schbox">
		   <h2 style="overflow:hidden;"><?php echo $i_langpackage->i_s_kindseach;?></h2>
		   <form action="search.php" method="POST" id="search_form" >
                   <select class="snpt" style="margin:0 0 3px 8px; display:inline; vertical-align:middle" name="" onchange="searchselect(this.value)">
                      <option value="1"><?php echo $i_langpackage->i_goods_search;?></option>
                      <option value="2" <?php  if(isset($search_type) && $search_type=='brand') {?> selected <?php }?>><?php echo $i_langpackage->i_s_company;?></option>
                   </select>

		        <input class="inpt"  name="k" type="text" />
		        <input class="btn" type="submit" value="<?php echo $i_langpackage->i_searchs;?>" />
		        <span><?php echo $i_langpackage->i_hot_tags;?>:<script src="cache/tag_keyword.js"></script></span>
		 </form>

	     </div>
    </div>
    <div class="clear"></div>
<!--search end -->
<script language="JavaScript">
<!--
function searchselect(v) {
	var search_form = document.getElementById("search_form");
	if(v==1) {
		search_form.action = 'search.php';
	} else {
		search_form.action = 'brand.php';
	}
}
<?php  if(isset($search_type) && $search_type=='brand') {?>
searchselect(2);
<?php }?>
//-->
</script><?php } ?>