<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("../foundation/asession.php");
require("../configuration.php");
require("includes.php");

/* modules 公共信息处理 */
if(isset($_SESSION['admin_id'])) {
	$admin_id = $_SESSION['admin_id'];
}
if(isset($_SESSION['admin_name'])) {
	$admin_name = $_SESSION['admin_name'];
}
/* 判断用户是否登陆 */
if(!$admin_id) {
	echo '<script>top.location.href="login.php";</script>';
	exit;
}

//当前可访问的应用工具
$appArray = array(
	'top'			=> 'm/top.php',
	'menu'			=> 'm/menu.php',
	'drag'			=> 'm/drag.php',
	'main'			=> 'm/main.php',
	'start'			=> 'm/main.php',
	'error'			=> 'm/error.php',

	'crons'			=> '../crons.php',

	'change_password'	=> 'm/admin/change_password.php',
	'admin_add'			=> 'm/admin/add.php',
	'admin_list'		=> 'm/admin/list.php',

	'admin_group'		=> 'm/admin/group.php',
	'admin_group_add'	=> 'm/admin/group_add.php',
	'group_rights'		=> 'm/admin/group_rights.php',
	/* 系统 */
	'sys_setting'		=> 'm/sys/setting.php',
	'index_images'		=> 'm/index/images.php',
	'searchkey_admin'	=> 'm/sys/searchkey_admin.php',
	'sys_crons'			=> 'm/sys/crons.php',
	'sys_crons_edit'	=> 'm/sys/crons_edit.php',
	'sys_area'			=> 'm/sys/area.php',
	'sys_integral'		=> 'm/sys/integral.php',

	'remind_set'		=> 'm/remind/set.php',
	'remind_edit'		=> 'm/remind/edit.php',
	'flink_list'		=> 'm/remind/flink_list.php',
	'flink_add'		    => 'm/remind/flink_add.php',
	'flink_edit'		=> 'm/remind/flink_edit.php',

	'mailtpl_set'		=> 'm/mailtpl/set.php',
	'mailtpl_edit'		=> 'm/mailtpl/edit.php',
	'sys_ckmail'		=> 'm/sys/ckmail.php',
	/* 系统 */
	'shop_list'			=> 'm/shop/list.php',
	'shop_request'		=> 'm/shop/request.php',
	'shop_request_view'	=> 'm/shop/request_view.php',
	/* 工具箱 */
	'tool_list'			=> 'm/toolsbox/tool_list.php',
	'd_tool'			=> 'm/toolsbox/download_tool_list.php',
	'm_tool'			=> 'm/toolsbox/manage_tool.php',

	/* 会员 */
	'member_list'		=> 'm/member/list.php',
	'member_add'		=> 'm/member/add.php',
	'member_reinfo'		=> 'm/member/reinfo.php',
	'member_rank'		=> 'm/member/rank.php',
	'member_rank_edit'	=> 'm/member/rank_edit.php',
	'member_rank_add'	=> 'm/member/rank_add.php',

	/* 商品相关 */
	'goods_attr_manage'		=> 'm/goods/attr_manage.php',
	'goods_brand_add'		=> 'm/goods/brand_add.php',
	'goods_brand_edit'		=> 'm/goods/brand_edit.php',
	'goods_brand_list'		=> 'm/goods/brand_list.php',
	'goods_category_add'	=> 'm/goods/category_add.php',
	'goods_category_edit'	=> 'm/goods/category_edit.php',
	'goods_category_list'	=> 'm/goods/category_list.php',
	'goods_list'			=> 'm/goods/list.php',

	/* 订单 */
	'order_alllist'		=> 'm/order/alllist.php',
	'order_view'		=> 'm/order/view.php',
	'order_payment'		=> 'm/order/payment.php',
	'order_payment_edit'=> 'm/order/payment_edit.php',

	/* 广告 */
	'asd_position_add'	=> 'm/asd/position_add.php',
	'asd_position_edit'	=> 'm/asd/position_edit.php',
	'asd_position_list'	=> 'm/asd/position_list.php',
	'asd_position_view'	=> 'm/asd/position_view.php',
	'asd_getcode'		=> 'm/asd/getcode.php',
	'asd_add'			=> 'm/asd/add.php',
	'asd_edit'			=> 'm/asd/edit.php',
	'asd_list'			=> 'm/asd/list.php',

	/* 新闻 */
	'news_add'			=> 'm/news/add.php',
	'news_catadd'		=> 'm/news/cat_add.php',
	'news_edit'			=> 'm/news/edit.php',
	'news_catedit'			=> 'm/news/cat_edit.php',
	'news_list'			=> 'm/news/list.php',
	'news_catlist'		=> 'm/news/cat_list.php',

	/* 插件 */
	'plugin_list'		=> 'm/plugin/pluginlist.php',
	'plugin_install_1'	=> 'm/plugin/install_plugin_1.php',
	'plugin_install_2'	=> 'm/plugin/install_plugin_2.php',
	'plugin_install_3'	=> 'm/plugin/install_plugin_3.php',
	'plugin_update'		=> 'm/plugin/update_plugin.php',
	'plugin_unload'		=> 'm/plugin/unload_plugin.php',

	/* 投诉 */
	'complaint_list'		=> 'm/complaint/list.php',
	'complaint_type'		=> 'm/complaint/type.php',
	'complaint_type_edit'=> 'm/complaint/type_edit.php',

	/* 数据 */
	'db_save'			=> 'm/db/database.save.php',
	'db_recover'		=> 'm/db/database.recover.php',

	/* 模版管理 */
	'manage_template'	=> 'm/management/manage_template.php',
	/* 恢复模版 */
	'manage_UI_restore'	=> 'm/management/manage_UI_restore.php',
	/* 模版列表 */
	'tmp_list'			=> 'm/management/tmp_list.php',
	/* 修改模版 */
	'tmp_change'		=> 'm/management/tmp_change.php',
	/* 编译模版 */
	'action_comp'		=> 'm/management/action_comp.php',
	/* 模版恢复 */
	'UI_restore.action'	=>'m/management/UI_restore.action.php',

	'upload_form'		=> 'm/pubtools/upload.form.php',
);

$appId = getAppId();
$apptarget = $appArray[$appId];
if(isset($apptarget)) {
	require($apptarget);
} else {
	echo 'no pages!';
}
?>