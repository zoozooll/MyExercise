<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$a_langpackage=new adminlp;
$right_array=array(
    "site_set_browse"    =>"0",
    "image_browse"    =>   "0",
    "programme_browse"    =>   "0",
    "area_browse"    =>   "0",
    "pay_browse"    =>   "0",
    "keyword_browse"    =>   "0",
    "credit_browse"    =>   "0",
    "remind"    =>   "0",
    "email_browse"    =>   "0",
    "flink_list"    =>   "0",
    "flink_add"    =>   "0",

    "user_browse"    =>   "0",
    "user_rank_browse"    =>   "0",
    "add_user_rank_browse"    =>   "0",
    "audit_company"    =>   "0",

    "shop_browse"    =>   "0",

    "order_list_browse"    =>   "0",
    "pay_order_list_browse"    =>   "0",
    "complaint_list"       => "0",
    "complaint_title"      => "0",

    "goods_list_browse"    =>   "0",
    "cat_add"    =>   "0",
    "cat_list"    =>   "0",
    "brand_add"    =>   "0",
    "brand_show"    =>   "0",
    "attr_manager"    =>   "0",

    "adv_position_list"    =>   "0",
    "adv_add"    =>   "0",
    "adv_list"    =>   "0",
    "adver_add"    =>   "0",

    "news_catlist"    =>   "0",
    "news_list"    =>   "0",
    "add_news_cat"    =>   "0",
    "add_news"    =>   "0",

    "template_mana"    =>   "0",

    "tools_list"    =>   "0",
    "tools_download"    =>   "0",
    "tools_mana"    =>   "0",

    "database"    =>   "0",
    "database_recover"    =>   "0",

    "admin_list"    =>   "0",
    "admin_group"    =>   "0",
    "admin_add"    =>   "0",
    "pass_browse"    =>   "0",
    "plugin"       => "",
);
foreach($right_array as $key => $value){
	$right_array[$key]=check_rights($key);
}

$id = get_args('value');
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<script type='text/javascript' src="skin/js/jy.js"></script>
<style>
</style>
</head>
<body onload="showlink()">
<div id="jybody" style="margin-top:0px;">
    <div id="leftmenu">
		<?php if ($id == 'index' or $id == ''){?>
	        <ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
	        		<li class="active" onclick="changeMenu(this);"><a href="m.php?app=main" target="main-frame"><?php echo $a_langpackage->a_manager_index; ?></a></li>
	        	<?php if($right_array["site_set_browse"]){?>
	        		<li class="" onclick="changeMenu(this);"><a href="m.php?app=sys_setting" target="main-frame"><?php echo $a_langpackage->a_syssite_setting; ?></a></li>
	            <?php }?>
	            <?php if($right_array["image_browse"]){?>
	            	<li class="" onclick="changeMenu(this);"><a href="m.php?app=index_images" target="main-frame"><?php echo $a_langpackage->a_index_image; ?></a></li>
	            <?php }?>
	            <?php if($right_array["area_browse"]){?>
	            	<li class="" onclick="changeMenu(this);"><a href="m.php?app=sys_area" target="main-frame"><?php echo $a_langpackage->a_sys_area; ?></a></li>
	            <?php }?>
	            <?php if($right_array["pay_browse"]){?>
	            	<li class="" onclick="changeMenu(this);"><a href="m.php?app=order_payment" target="main-frame"><?php echo $a_langpackage->a_m_order_payment; ?></a></li>
	            <?php }?>
	            <?php if($right_array["credit_browse"]){?>
	            	<li class="" onclick="changeMenu(this);"><a href="m.php?app=sys_integral" target="main-frame"><?php echo $a_langpackage->a_m_sys_integral; ?></a></li>
				<?php }?>
				<?php if($right_array["remind"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=remind_set" target="main-frame"><?php echo $a_langpackage->a_m_remind_set; ?></a></li>
				<?php }?>
				<?php if($right_array["email_browse"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=mailtpl_set" target="main-frame"><?php echo $a_langpackage->a_m_mailtpl_set; ?></a></li>
	        	<?php }?>
	        </ul>
		<?php }?>
	    <?php if ($id == 'application'){?>
			<ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
				<?php if($right_array["programme_browse"]){?>
					<li class="active" onclick="changeMenu(this);"><a href="m.php?app=sys_crons" target="main-frame"><?php echo $a_langpackage->a_sys_cron; ?></a></li>
				<?php }?>
				<?php if($right_array["keyword_browse"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=searchkey_admin" target="main-frame"><?php echo $a_langpackage->a_m_search_k; ?></a></li>
				<?php }?>
				<?php if($right_array["flink_list"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=flink_list" target="main-frame"><?php echo $a_langpackage->a_flink_list; ?></a></li>
	        	<?php }?>
	        	<?php if($right_array["template_mana"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=manage_template" target="main-frame"><?php echo $a_langpackage->a_m_template_management; ?></a></li>
				<?php }?>
				<?php if($right_array["plugin"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=plugin_list" target="main-frame"><?php echo $a_langpackage->a_m_plugin_management; ?></a></li>
				<?php }?>
				<?php if($right_array["tools_list"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=tool_list" target="main-frame"><?php echo $a_langpackage->a_m_tool_list; ?></a></li>
	        	<?php }?>
	        	<?php if($right_array["tools_download"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=d_tool" target="main-frame"><?php echo $a_langpackage->a_m_download_tools; ?></a></li>
	        	<?php }?>
				<?php if($right_array["tools_mana"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=m_tool" target="main-frame"><?php echo $a_langpackage->a_m_tools_management; ?></a></li>
				<?php }?>
				<?php if($right_array["database"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=db_save" target="main-frame"><?php echo $a_langpackage->a_m_dbs_backup; ?></a></li>
				<?php }?>
				<?php if($right_array["database_recover"]){?>
					<li class="" onclick="changeMenu(this);"><a href="m.php?app=db_recover" target="main-frame"><?php echo $a_langpackage->a_m_dbs_recover; ?></a></li>
				<?php }?>
	        </ul>
		<?php }?>
		<?php if ($id == 'member'){?>
			<ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
			<?php if($right_array["user_browse"]){?>
				<li class="active" onclick="changeMenu(this);"><a href="m.php?app=member_list" target="main-frame"><?php echo $a_langpackage->a_memeber_list; ?></a></li>
			<?php }?>
			<?php if($right_array["user_rank_browse"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=member_rank" target="main-frame"><?php echo $a_langpackage->a_m_member_level_set; ?></a></li>
			<?php }?>
			<?php if($right_array["add_user_rank_browse"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=member_rank_add" target="main-frame"><?php echo $a_langpackage->a_m_member_level_add; ?></a></li>
			<?php }?>
			<?php if($right_array["audit_company"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=shop_request" target="main-frame"><?php echo $a_langpackage->a_m_check_commember; ?></a></li>
			<?php }?>
			<?php if($right_array["admin_list"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=admin_list" target="main-frame"><?php echo $a_langpackage->a_admin_list; ?></a></li>
			<?php }?>
			<?php if($right_array["admin_group"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=admin_group" target="main-frame"><?php echo $a_langpackage->a_m_admingroup_set; ?></a></li>
			<?php }?>
			<?php if($right_array["admin_add"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=admin_add" target="main-frame"><?php echo $a_langpackage->a_add_admin; ?></a></li>
			<?php }?>
			<?php if($right_array["pass_browse"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=change_password" target="main-frame"><?php echo $a_langpackage->a_edit_admin_psd; ?></a></li>
			<?php }?>
			</ul>
		<?php }?>
		<?php if ($id == 'shops'){?>
			<ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
			<?php if($right_array["shop_browse"]){?>
				<li class="active" onclick="changeMenu(this);"><a href="m.php?app=shop_list" target="main-frame"><?php echo $a_langpackage->a_shop_list; ?></a></li>
			<?php }?>
			</ul>
	   <?php }?>
	   <?php if ($id == 'order'){?>
			<ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
	        <?php if($right_array["order_list_browse"]){?>
				<li class="active" onclick="changeMenu(this);"><a href="m.php?app=order_alllist" target="main-frame"><?php echo $a_langpackage->a_alllist; ?></a></li>
			<?php }?>
			<?php if($right_array["pay_order_list_browse"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=order_alllist&pay_status=1" target="main-frame"><?php echo $a_langpackage->a_m_opayedrder_list; ?></a></li>
			<?php }?>
			<?php if($right_array["complaint_list"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=complaint_list" target="main-frame"><?php echo $a_langpackage->a_complaints_list; ?></a></li>
			<?php }?>
			<?php if($right_array["complaint_title"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=complaint_type" target="main-frame"><?php echo $a_langpackage->a_complaints_title_adm; ?></a></li>
			<?php }?>
			</ul>
	   <?php }?>
	   <?php if ($id == 'commodity'){?>
			<ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
			<?php if($right_array["goods_list_browse"]){?>
				<li class="active" onclick="changeMenu(this);"><a href="m.php?app=goods_list" target="main-frame"><?php echo $a_langpackage->a_goods_list; ?></a></li>
			<?php }?>
			<?php if($right_array["brand_show"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=goods_brand_list" target="main-frame"><?php echo $a_langpackage->a_brand_list; ?></a></li>
			<?php }?>
			<?php if($right_array["cat_add"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=goods_category_add" target="main-frame"><?php echo $a_langpackage->a_category_add; ?></a></li>
			<?php }?>
			<?php if($right_array["cat_list"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=goods_category_list" target="main-frame"><?php echo $a_langpackage->a_category_list; ?></a></li>
			<?php }?>
			<?php if($right_array["attr_manager"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=goods_attr_manage" target="main-frame"><?php echo $a_langpackage->a_m_attr_management; ?></a></li>
			<?php }?>
			</ul>
       <?php }?>
	   <?php if ($id == 'ad'){?>
			<ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
			<?php if($right_array["adv_position_list"]){?>
				<li class="active" onclick="changeMenu(this);"><a href="m.php?app=asd_position_list" target="main-frame"><?php echo $a_langpackage->a_asdposition_list; ?></a></li>
			<?php }?>
			<?php if($right_array["adv_add"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=asd_position_add" target="main-frame"><?php echo $a_langpackage->a_add_asdposition; ?></a></li>
			<?php }?>
			<?php if($right_array["adv_list"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=asd_list" target="main-frame"><?php echo $a_langpackage->a_asd_list; ?></a></li>
			<?php }?>
			<?php if($right_array["adver_add"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=asd_add" target="main-frame"><?php echo $a_langpackage->a_add_asd; ?></a></li>
			<?php }?>
			</ul>
	   <?php }?>
	   <?php if ($id == 'article'){?>
			<ul class="submenu" id="index">
	        	<li id="separator" class="separator"></li>
			<?php if($right_array["news_list"]){?>
				<li class="active" onclick="changeMenu(this);"><a href="m.php?app=news_list" target="main-frame"><?php echo $a_langpackage->a_news_list; ?></a></li>
			<?php }?>
			<?php if($right_array["add_news"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=news_add" target="main-frame"><?php echo $a_langpackage->a_news_add; ?></a></li>
			<?php }?>
			<?php if($right_array["news_catlist"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=news_catlist" target="main-frame"><?php echo $a_langpackage->a_category_list; ?></a></li>
			<?php }?>
			<?php if($right_array["add_news_cat"]){?>
				<li class="" onclick="changeMenu(this);"><a href="m.php?app=news_catadd" target="main-frame"><?php echo $a_langpackage->a_category_add; ?></a></li>
			<?php }?>
			<ul>
	   <?php }?>
    </div>
</div>
<script language="JavaScript">
<!--
function showlink() {
	var eli = document.getElementsByTagName("li");
	for(var i=0; i<eli.length; i++) {
		if(eli[i].className=='active') {
			parent.frames[2].location = eli[i].children[0].href;
			parent.document.getElementById('frame-body').cols = '180,*';
			break;
		}
	}
}

document.getElementById('separator').onclick = function (){
	if(document.body.className == 'folden') {
		document.body.className = '';
		parent.document.getElementById('frame-body').cols = '180,*';
	} else {
		document.body.className = 'folden';
		parent.document.getElementById('frame-body').cols = '50,*';
	}
}
//-->
</script>
</body>
</html>