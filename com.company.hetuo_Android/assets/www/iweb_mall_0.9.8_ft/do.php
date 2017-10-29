<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require('foundation/asession.php');
require('configuration.php');
require('includes.php');

/* do 公共信息处理 */
require 'foundation/alogin_cookie.php';

$user_id = get_sess_user_id();
$user_name = get_sess_user_name();
$shop_id = get_sess_shop_id();
$privilege=get_sess_privilege();
if($privilege) {
	$user_privilege = unserialize($privilege);
}
//当前可访问的action动作,先列出公共部分,然后按各个模块列出
$actArray = array(
	'login'				=> array('action/login_act.php'),
	'logout'			=> array('action/logout_act.php'),
	'register'			=> array('action/reg_act.php'),
	'send_code'		=> array('action/send_code.php'),
	'resend_email'		=> array('action/reg_resend_email.php','modules.php?app=reg2'),
	'forgot'			=> array('action/forgot.php','login.php'),
	'set_username'		=> array('action/set_username.php','index.php'),

	// user
	'user_profile'		=> array('action/user/profile.action.php','modules.php?app=user_profile'),
	'user_ico'			=> array('action/user/ico.action.php','modules.php?app=user_ico'),
	'user_passwd'		=> array('action/user/passwd.action.php','modules.php?app=user_passwd'),
	'user_guestbook_del'=> array('action/user/guestbook_del.action.php','modules.php?app=user_guestbook'),
	'user_favorite_del'	=> array('action/user/favorite_del.action.php','modules.php?app=user_favorite'),
	'user_cart_del'		=> array('action/user/cart_del.action.php','modules.php?app=user_cart'),
	'user_order'		=> array('action/user/order.action.php','modules.php?app=user_my_order'),
	'user_order_del'	=> array('action/user/order_del.action.php','modules.php?app=user_my_order'),
	'user_order_checkget'	=> array('action/user/order_checkget.action.php','modules.php?app=user_my_order'),
	'user_address_add'	=> array('action/user/address_add.action.php','modules.php?app=user_address'),
	'user_address_del'	=> array('action/user/address_del.action.php','modules.php?app=user_address'),
	'user_remind_upd'	=> array('action/user/remind_upd.action.php'),
	'user_remind_info'	=> array('action/user/remind_info.action.php'),
	'user_pay_message'	=> array('action/user/pay_message.action.php'),
	'user_complaint_add'	=> array('action/user/complaint_add.action.php'),
	'user_complaint_add'	=> array('action/user/complaint_add.action.php'),
	'get_back_money'	=> array('action/user/get_back_money.action.php'),

	// shop
	'shop_create'		=> array('action/shop/create.action.php','modules.php?app=shop_info'),
	'shop_info'			=> array('action/shop/info.action.php','modules.php?app=shop_info'),
	'shop_notice'		=> array('action/shop/notice.action.php','modules.php?app=shop_notice'),
	'shop_category_add'	=> array('action/shop/category_add.action.php','modules.php?app=shop_category'),
	'shop_category_edit'=> array('action/shop/category_edit.action.php','modules.php?app=shop_category'),
	'shop_category_del' => array('action/shop/category_del.action.php','modules.php?app=shop_category'),
	'shop_news_del'		=> array('action/shop/news_del.action.php','modules.php?app=shop_news'),
	'shop_news_edit'	=> array('action/shop/news_edit.action.php'),
	'shop_news_add'		=> array('action/shop/news_add.action.php','modules.php?app=shop_news'),
	'shop_honor_update'	=> array('action/shop/honor_update.action.php'),
	'shop_guestbook_del'=> array('action/shop/guestbook_del.action.php','modules.php?app=shop_guestbook'),
	'shop_askprice_del'	=> array('action/shop/askprice_del.action.php','modules.php?app=shop_askprice'),
	'shop_order_check'	=> array('action/shop/order_check.action.php','modules.php?app=shop_my_order'),
	'shop_order_checkput'	=> array('action/shop/order_checkput.action.php','modules.php?app=shop_my_order'),
	'shop_order_priceedit'	=> array('action/shop/order_priceedit.action.php'),
	'shop_payment'			=> array('action/shop/payment.action.php','modules.php?app=shop_payment'),
	'shop_payment_update'	=> array('action/shop/payment_update.action.php','modules.php?app=shop_payment'),
	'shop_payment_del'		=> array('action/shop/payment_del.action.php','modules.php?app=shop_payment'),
	'shop_request'			=> array('action/shop/request.action.php','modules.php?app=shop_request'),
	'shop_reply_add'		=> array('action/shop/seller_reply_add.php'),
	'shop_rate_reply_add'	=> array('action/shop/rate_reply_add.php'),
	'shop_credit_add'		=> array('action/shop/credit_add.php'),
	'shop_askprice'		=> array('action/shop/askprice_edit.action.php'),

	// goods
	'goods_add'			=> array('action/goods/add.action.php','modules.php?app=goods_list'),
	'goods_del'			=> array('action/goods/del.action.php','modules.php?app=goods_list'),
	'goods_edit'		=> array('action/goods/edit.action.php'),
	'goods_list'		=> array('action/goods/list.action.php','modules.php?app=goods_list'),
	'goods_attr_edit'	=> array('action/goods/attr_edit.action.php'),
	'goods_gallery_update'	=> array('action/goods/gallery_update.action.php'),
	'goods_gallery_set'	=> array('action/goods/gallery_set.action.php'),
	'goods_num_edit'	=> array('action/goods/num_edit.action.php'),
	'category_get_catlist'	=> array('action/category/get_catlist.action.php'),
	'category_add_catlist'	=> array('action/category/add_catlist.action.php'),
	'csv_export'			=>array('action/goods/csv_export.php'),
	'csv_import'			=>array('action/goods/csv_import.php'),
	'goods_csv_taobao'			=>array('action/goods/csv_taobao.php'),
	'add_transport_template'=>array('action/goods/add_transport_template.php'),
	'edit_transport_template'=>array('action/goods/edit_transport_template.php'),
	'edit_guestbook'    => array('action/goods/exit_guestbook.action.php'),

	// ajax
	'ajax_areas'		=> array('action/ajax/areas.action.php'),
	'shop_catsort_edit'	=> array('action/shop/catsort_edit.action.php'),
	'shop_catunit_edit'	=> array('action/shop/catunit_edit.action.php'),
	'shop_isshow_toggle'=> array('action/shop/isshow_toggle.action.php'),
	'goods_toggle'		=> array('action/goods/toggle.action.php'),
	'goods_sort_edit'	=> array('action/goods/sort_edit.action.php'),
	'goods_number_edit'	=> array('action/goods/number_edit.action.php'),
	'goods_price_edit'	=> array('action/goods/price_edit.action.php'),
	'goods_attr_list'	=> array('action/goods/attr_list.action.php'),
	'goods_gallery_drop'=> array('action/goods/gallery_drop.action.php'),
	'shop_honor_drop'	=> array('action/shop/honor_drop.action.php'),
	'user_check_username'	=> array('action/user/check_username.action.php'),
	'user_check_useremail'	=> array('action/user/check_useremail.action.php'),
	'goods_add_favorite'=> array('action/goods/add_favorite.action.php'),
	'goods_add_cart'	=> array('action/goods/add_cart.action.php'),
	'goods_get_imgurl'	=> array('action/goods/get_imgurl.action.php'),
	'goods_ucategory_add' => array('action/goods/ucategory_add.action.php'),
	'shop_appraise' 	=> array('action/shop/get_appraise.php'),
	'shop_open_flg' 	=> array('action/ajax/open_flg.php'),
	'gettransportprice' => array('action/goods/gettransportprice.php'),
	'shop_img_select' => array('action/shop/img_select.php'),

	'groupbuy_selectgoods' 	=> array('action/groupbuy/select_goods_list.action.php'),
	'get_brand_list' 	=> array('action/goods/get_brand_list.php'),
	'groupbuy_add'		=>array('action/groupbuy/add.action.php'),
	'groupbuy_release'		=>array('action/groupbuy/release.action.php'),
	'get_transport_price'		=>array('action/shop/get_transport_price.php'),
	'groupbuy_end'		=>array('action/groupbuy/end.action.php'),
	'groupbuy_del'		=>array('action/groupbuy/del.action.php'),

	// guestbook
	'shop_guestbook'	=> array('action/shop/guestbook.action.php'),
	// pubtools
	'upload_act'		=> array('action/pubtools/upload.action.php'),
	//参加团购
	'goods_add_groupbuy' => array('action/goods/add_groupbuy.action.php'),
	//退出团购
	'goods_exit_groupbuy' => array('action/goods/exit_groupbuy.action.php'),
);

$actId=getActId();
$acttarget=$actArray[$actId];

/* ajax请求时判断处理 */
$ajaxCheckArray = array('shop_catsort_edit','shop_catunit_edit','ajax_areas','shop_isshow_toggle','goods_toggle','goods_sort_edit','goods_number_edit','goods_price_edit','goods_attr_list','goods_gallery_drop','user_check_username','user_check_useremail','goods_add_favorite','goods_get_imgurl','goods_add_groupbuy','goods_exit_groupbuy','get_brand_list','get_transport_price','gettransportprice');
if(in_array($actId,$ajaxCheckArray)) {
	if(strtolower($_SERVER['HTTP_X_REQUESTED_WITH']) != 'xmlhttprequest') {
		exit('非法操作003！请重新<a href="login.php">登陆</a>！');
	}
}

$notCheckLoginArray = array('login','logout','register','ajax_areas','shop_guestbook','user_check_username','user_check_useremail','forgot','goods_get_imgurl','goods_add_favorite','shop_appraise','goods_add_groupbuy','goods_exit_groupbuy','get_transport_price','gettransportprice','goods_add_cart','user_cart_del');
if(!in_array($actId,$notCheckLoginArray)) {
	/* 判断用户是否登陆 */
	if(!$user_id) { exit('请先<a href="login.php">登陆</a>！'); }
	/* 判断用户相关操作是否合法 */
	if(isset($_POST['user_id']) && $_POST['user_id'] != $user_id) { exit('非法操作002！请重新<a href="login.php">登陆</a>！'); }
	/* 判断商铺相关操作是否合法 */
	if($shop_id>0 && isset($_POST['shop_id']) && $_POST['shop_id'] != $shop_id) { exit('非法操作001！请重新<a href="login.php">登陆</a>！'); }
}

//action动作成功控制函数
function action_return($success=1,$return_mess='',$activeUrl='') {
	global $acttarget;
	echo "<script language='javascript'>";
	if(trim($return_mess)!=''){
		echo "alert('",$return_mess,"');";
	}

	$setUrl = '';
	if($activeUrl!=''){
		$setUrl = $activeUrl;
	} else {
		$setUrl = $acttarget[1];
	}

	if($setUrl=='-1'){
		echo 'history.go(-1);';
	} else if($setUrl=='0') {
		echo 'window.close();';
	} else {
		echo "location.href='".$setUrl."';";
	}
	echo '</script>';
	exit();
}

if(isset($acttarget)) {
	require($acttarget[0]);
} else {
	echo 'no pages!';
}
?>