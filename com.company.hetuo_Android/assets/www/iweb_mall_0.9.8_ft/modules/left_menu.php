<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/left_menu.html
 * 如果您的模型要进行修改，请修改 models/modules/left_menu.php
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
if(filemtime("templates/default/modules/left_menu.html") > filemtime(__file__) || (file_exists("models/modules/left_menu.php") && filemtime("models/modules/left_menu.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/left_menu.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
	/*
	***********************************************
	*$ID:
	*$NAME:
	*$AUTHOR:E.T.Wei
	*DATE:Mon Mar 22 16:19:15 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}

	//文件引入
	include_once("foundation/asystem_info.php");
	include_once("foundation/module_users.php");

	//引入语言包
	$m_langpackage=new moduleslp;
	//数据表定义区
	$user_table = $tablePreStr."users";
	$t_order_info = $tablePreStr."order_info";
	$t_shop_inquiry = $tablePreStr."shop_inquiry";
	$t_shop_guestbook = $tablePreStr."shop_guestbook";
	//读写分类定义方法
	$dbo=new dbex;
	dbtarget('r',$dbServs);
	$user_id = get_sess_user_id();
	$userinfo = get_user_info($dbo,$user_table,$user_id);
	$user_rank = $userinfo['rank_id'];
	/* 收到的询价 */
	$sql = "select shop_id from `$t_shop_inquiry` where shop_id='$user_id' and shop_del_status=1 and read_status=0";
	$rs = $dbo->getRs($sql);
	$shop_inquiry_num = 0;
	foreach($rs as $value) {
		$shop_inquiry_num++;
	}
	/* 收到的留言 */
	$sql = "SELECT shop_id FROM `$t_shop_guestbook` WHERE shop_id='$user_id' and shop_del_status=1 and read_status=0";
	$rs = $dbo->getRs($sql);
	$shop_guestbook_num = 0;
	foreach($rs as $value) {
		$shop_guestbook_num++;
	}
	/* 我的留言 */
	$sql = "SELECT shop_id FROM `$t_shop_guestbook` WHERE user_id='$user_id' and user_del_status=1";
	$rs = $dbo->getRs($sql);
	$my_guestbook_num = 0;
	foreach($rs as $value) {
		$my_guestbook_num++;
	}
	/* 我的,收到的订单 */
	$sql = "SELECT shop_id,user_id,order_status FROM `$t_order_info` WHERE shop_id='$user_id' OR user_id='$user_id'";
	$rs = $dbo->getRs($sql);
	$my_order_num = 0;
	$get_order_num = 0;
	$u_order_num = 0;
	$s_order_num = 0;
	foreach($rs as $value) {
		if($value['shop_id']==$user_id) {
			if($value['order_status']=='3') {
				$u_order_num++;
			}else{
				$get_order_num++;
			}
		}
		if($value['user_id']==$user_id) {
			$my_order_num++;
			if($value['order_status']=='3') {
				$s_order_num++;
			}
		}
	}
?><div class="sidebar">
	<div class="title_uc"><h3 class="t1"><?php echo  $m_langpackage->m_buyer;?></h3></div>
    <ul class="item_uc">
    	<li><a href="modules.php?app=user_cart"><?php echo  $m_langpackage->m_my_cart;?></a></li>
    	<li><a href="modules.php?app=user_my_order"><?php echo  $m_langpackage->m_my_order;?></a></li>
    	<li><a href="modules.php?app=user_group"><?php echo  $m_langpackage->m_my_groupbuy;?></a></li>
    	<li><a href="modules.php?app=user_favorite"><?php echo  $m_langpackage->m_my_favorite;?></a></li>
    	<li><a href="modules.php?app=user_guestbook"><?php echo  $m_langpackage->m_my_guestbook;?></a></li>
    	<li><a href="modules.php?app=user_address"><?php echo $m_langpackage->m_getgoods_address;?></a></li>
    </ul>
	<div class="title_uc"><h3 class="t2"><?php echo  $m_langpackage->m_seller;?></h3></div>
    <ul class="item_uc">
    	<?php if(isset($user_privilege[1]) && $user_privilege[1] && get_sess_shop_id()>0){?>
		<?php if($_SESSION['shop_lock']==0) {?>
			<li>
			<a href="modules.php?app=shop_info"><?php echo  $m_langpackage->m_shop_info;?></a>
			<span class="fr">
				<span id="shop_flg">
				<?php if($_SESSION['shop_open']==1){?>
				<a href="javascript:void(0)" onclick="change_open_status('0')" style="color:red;"><?php echo  $m_langpackage->m_open;?></a>
				<?php  } else {?>
				<a href="javascript:void(0)" onclick="change_open_status('1')" style="color:red;"><?php echo  $m_langpackage->m_close;?></a>
				<?php }?>
				&nbsp;&nbsp;
				</span>
			<a href="<?php echo shop_url($shop_id);?>" style="color:red;" target="_blank"><?php echo  $m_langpackage->m_shop_view;?></a>&nbsp;&nbsp;
			</span>
			</li>
			<!-- <li><a href="modules.php?app=shop_honor"><?php echo  $m_langpackage->m_shop_honor;?></a></li> -->
			<li><a href="modules.php?app=shop_category"><?php echo  $m_langpackage->m_shop_category;?></a></li>
			<li><a href="modules.php?app=goods_list"><?php echo  $m_langpackage->m_goods_list;?></a></li>
			<li><a href="modules.php?app=goods_add"><?php echo  $m_langpackage->m_add_newgoods;?></a></li>
			<li><a href="modules.php?app=groupbuy_list"><?php echo  $m_langpackage->m_groupbuy_list;?></a></li>
			<li><a href="modules.php?app=groupbuy_add"><?php echo  $m_langpackage->m_add_groupbuy;?></a></li>
			<li><a href="modules.php?app=csv_export"><?php echo  $m_langpackage->m_csv_export;?></a></li>
			<li><a href="modules.php?app=csv_import"><?php echo  $m_langpackage->m_csv_import;?></a></li>
			<li><a href="modules.php?app=shop_my_order"><?php echo  $m_langpackage->m_rc_order;?>(<?php echo  $get_order_num;?>)<?php if($get_order_num>0){?><img src="skin/<?php echo $SYSINFO['templates'];?>/images/woo.gif" width="21" height="12" /><?php }?></a></li>
			<li><a href="modules.php?app=shop_guestbook"><?php echo  $m_langpackage->m_rc_guestbook;?>(<?php echo  $shop_guestbook_num;?>)<?php if($shop_guestbook_num>0){?><img src="skin/<?php echo $SYSINFO['templates'];?>/images/woo.gif" width="21" height="12" /><?php }?></a></li>
			<li><a href="modules.php?app=shop_askprice"><?php echo  $m_langpackage->m_rc_askprice;?>(<?php echo $shop_inquiry_num;?>)<?php if($shop_inquiry_num>0){?><img src="skin/<?php echo $SYSINFO['templates'];?>/images/woo.gif" width="21" height="12" /><?php }?></a></li>
			<li><a href="modules.php?app=shop_notice"><?php echo  $m_langpackage->m_shop_notice;?></a></li>
			<!-- <li><a href="modules.php?app=shop_news"><?php echo  $m_langpackage->m_shopnews_manage;?></a></li> -->
			<!-- <li><a href="modules.php?app=shop_news_add"><?php echo  $m_langpackage->m_add_shopnews;?></a></li> -->
			<li><a href="modules.php?app=shop_payment"><?php echo  $m_langpackage->m_payment_setting;?></a></li>
		<?php  } else {?>
		<li><font color="black"><?php echo  $m_langpackage->m_shop_lock;?></font></li>
		<?php }?>
		<?php  } else {?>
			<li><a href='modules.php?app=shop_create' style="color:red;"><?php if($user_rank<2){?><?php echo  $m_langpackage->m_createshop_vip;?><?php }?>
			<?php echo  $m_langpackage->m_create_shop;?></a></li>
			<?php if($user_rank<2){?>
			<li><a href="modules.php?app=shop_request" style="color:#1E88C0"><?php echo $m_langpackage->m_reg_com_user;?></a></li>
			<?php }?>
		<?php }?>
    </ul>
	<div class="title_uc"><h3 class="t4"><?php echo  $m_langpackage->m_shoprate_manage;?></h3></div>
	<ul class="item_uc">
		<li><a href="modules.php?app=shop_rate&t=buyer"><?php echo  $m_langpackage->m_shoprate_frombuyer;?></a></li>
		<li><a href="modules.php?app=shop_rate&t=seller"><?php echo  $m_langpackage->m_shoprate_fromseller;?></a></li>
		<li><a href="modules.php?app=shop_rate&t=bymain"><?php echo  $m_langpackage->m_shoprate_topep;?></a></li>
    </ul>
	<div class="title_uc"><h3 class="t3"><?php echo  $m_langpackage->m_base_setting;?></h3></div>
    <ul class="item_uc">
		<li><a href="modules.php?app=user_profile"><?php echo  $m_langpackage->m_profile;?></a></li>
		<li><a href="modules.php?app=user_remind"><?php echo  $m_langpackage->m_remind_setting;?></a></li>
		<li><a href="modules.php?app=user_remind_info"><?php echo  $m_langpackage->m_my_remind;?></a></li>
		<?php if($im_enable){?>
		<li><a href="modules.php?app=user_ico"><?php echo  $m_langpackage->m_userico_setting;?></a></li>
		<?php }?>
		<li><a href="modules.php?app=user_passwd"><?php echo  $m_langpackage->m_edit_password;?></a></li>
		<li><a href="do.php?act=logout"><?php echo  $m_langpackage->m_logout;?></a></li>
    </ul>
	<!-- plugins !-->
	<div id="user_menu_buttun">
		<?php echo isset($plugins['user_menu_buttun'])?show_plugins($plugins['user_menu_buttun']):'';?>
	</div>
	<!-- plugins !-->

</div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function change_open_status(flg) {
	if(flg==1){
		var open=confirm('<?php echo  $m_langpackage->m_open_message;?>');

		if (open){
			ajax("do.php?act=shop_open_flg","POST","value="+flg,function(return_text){
				return_text = return_text.replace(/[\n\r]/g,"");
				document.getElementById("shop_flg").innerHTML = return_text;
			});
		}
	}else {
		ajax("do.php?act=shop_open_flg","POST","value="+flg,function(return_text){
			return_text = return_text.replace(/[\n\r]/g,"");
			document.getElementById("shop_flg").innerHTML = return_text;
		});
	}
}
//-->
</script><?php } ?>