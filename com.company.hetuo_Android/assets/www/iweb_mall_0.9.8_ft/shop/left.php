<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/left.html
 * 如果您的模型要进行修改，请修改 models/shop/left.php
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
if(filemtime("templates/default/shop/left.html") > filemtime(__file__) || (file_exists("models/shop/left.php") && filemtime("models/shop/left.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/left.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("foundation/module_credit.php");

$t_credit = $tablePreStr."credit";
$t_integral = $tablePreStr."integral";

/* 处理商铺自定义分类 */
$category_list = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$category_list_new = array();
if(!empty($category_list)) {
	foreach($category_list as $v) {
		$category_list_new[$v['shop_cat_id']]['shop_cat_id'] = $v['shop_cat_id'];
		$category_list_new[$v['shop_cat_id']]['shop_cat_name'] = $v['shop_cat_name'];
		$category_list_new[$v['shop_cat_id']]['parent_id'] = $v['parent_id'];
		$category_list_new[$v['shop_cat_id']]['shop_cat_unit'] = $v['shop_cat_unit'];
		$category_list_new[$v['shop_cat_id']]['sort_order'] = $v['sort_order'];
	}
}
unset($category_list);

	function get_sub_category ($category_list,$parent_id) {
		$array = array();
		foreach($category_list as $k=>$v) {
			if($v['parent_id']==$parent_id) {
				$array[$k] = $v;
			}
		}
		return $array;
	}

//获取商家信用值
$credit=get_credit($dbo,$t_credit,$shop_id);
$credit['SUM(seller_credit)']=intval($credit['SUM(seller_credit)']);
$integral=get_integral($dbo,$t_integral,$credit['SUM(seller_credit)']);
?><div class="sidebar top10">
	<div class="shop_detail">
		<div class="c_m">
			<div class="shop_logo"><a href="<?php echo  shop_url($shop_id,'index');?>" title=""><img src="<?php echo  $SHOP['shop_logo'] ? $SHOP['shop_logo'] : 'skin/default/images/shop_nologo.gif';?>" width="198" height="98" alt="" /></a></div>
			<div class="shop_name"><a href="<?php echo  shop_url($shop_id,'index');?>"><?php echo  $SHOP['shop_name'];?></a></div>
			<ul>
				<li><?php echo $s_langpackage->s_nickname;?>： <?php echo  $ranks['user_name'];?></li>
				<?php if($im_enable==true){?><li><?php echo $s_langpackage->s_contact_seller;?>：<script src="imshow.php?u=<?php echo $SHOP['user_id'];?>"></script></li><?php }?>
				<li><?php echo $s_langpackage->s_goods_num;?>： <?php echo  $SHOP['goods_num'];?></li>
				<li><span style="float:left;"><?php echo $s_langpackage->s_seller_c;?>：<a href="<?php echo  shop_url($shop_id,'credit');?>" hideFocus=true><?php echo $credit['SUM(seller_credit)'];?></a></span><span class="icon<?php echo $integral['int_grade'];?>" style="float:left;"></span><div style="clear:both;"></div></li>
				<li><?php echo $s_langpackage->s_new_login;?>： <?php echo  $ranks['last_login_time'];?></li>
				<li><?php echo $s_langpackage->s_creat_time;?>： <?php echo  $SHOP['shop_creat_time'];?></li>
				<li><span class="left"><?php echo $s_langpackage->s_approve_info;?>：</span>
				<?php  if($SHOP['rank_id']>2){?>
				<a href="javascript:;" title="<?php echo $s_langpackage->s_approve_company;?>"  class="shop_cert left"><?php echo $s_langpackage->s_approve_company;?></a>
				<?php  } else{?>
				<a href="javascript:;" title="<?php echo $s_langpackage->s_noapprove_company;?>"  class="shop_cert2 left"><?php echo $s_langpackage->s_noapprove_company;?></a>
				<?php }?>
				</li>
			</ul>
			<div class="clear"></div>
		</div>
	</div>
	<div class="clear"></div>
	<div class="shop_search top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="ucategory.php?s=<?php echo  $SHOP['shop_id'];?>"><?php echo $s_langpackage->s_store_insearch;?></a></h2></div>

			<form action="ucategory.php" method="get">
			<input name="s" value="<?php echo  $SHOP['shop_id'];?>" type="hidden" />
			<div class="c_m">
				<div class="searchbox"><input class="text" name="k" type="text" /><input type="submit" value="<?php echo $s_langpackage->s_search;?>" class="button" /></div>
			</div>
			</form>
	</div>
	<div class="pro_class top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="<?php echo  shop_url($shop_id,'products');?>"><?php echo $s_langpackage->s_goods_category;?></a></h2><span class="right"><a href="<?php echo  shop_url($shop_id,'products');?>"><?php echo $s_langpackage->s_more;?>>></a></span></div>

			<div class="c_m">
				<ul>
				<?php  if(empty($category_list_new)){?>
				<li class="open"><?php echo  $s_langpackage->s_shop_noaddcategory;?></li>
				<?php } else {
					$category_0 = get_sub_category($category_list_new,0);
					foreach($category_0 as $v) {?>
					<li class="open"><a href="<?php echo  ucategory_url($v['shop_cat_id']);?>"><?php echo  $v['shop_cat_name'];?></a>
				<?php  
					$category_sub = get_sub_category($category_list_new,$v['shop_cat_id']);
					if(!empty($category_sub)){?>
					<?php foreach($category_sub as $value) {?>
						<li>&nbsp;&nbsp;&nbsp;&nbsp;<a href="<?php echo  ucategory_url($value['shop_cat_id']);?>"><?php echo  $value['shop_cat_name'];?></a>		
					<?php  }?>
					<?php  }?>
					</li>
				<?php  }?>
				<?php }?>
				</ul>
			</div>
	</div>
	
</div><?php } ?>