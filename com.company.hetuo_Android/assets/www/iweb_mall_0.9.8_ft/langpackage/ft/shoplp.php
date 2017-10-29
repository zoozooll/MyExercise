<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

class shoplp{

	var $s_time = "時間";
	var $s_yuan = "元";
	var $s_more = "更多";
	var $s_logout = "退出";
	var $s_login = "登錄";

	var $s_cart = "購物車";
	var $s_favorite = "收藏夾";
	var $s_back_index = "返回首頁";
	var $s_want_buy = "我要買";
	var $s_want_sell = "我要賣";

	var $s_u_center = "用戶中心";
	var $s_hi = "您好";
	var $s_company_news = "企業資訊";
	var $s_guestbook = "留言簿";

	var $s_intro = "店舖簡介";
	var $s_products = "產品展示";
	var $s_honor = "榮譽資質";
	var $s_job = "招聘中心";
	var $s_contact = "聯繫我們";
	var $s_register_free = "免費註冊";
	var $s_no_goods = "沒有產品！";
	var $s_goods_category = "商品分類";

	var $s_search = "搜索";

	var $s_shop_indexs = "店舖首頁";
	var $s_shop_credit = "信用評價";
	var $s_shop_intro = "店舖介紹";
	var $s_shop_notices = "店舖公告";
	var $s_shop_groupbuy = "團購商品";
	var $s_shop_seat = "店舖位置";
	var $s_shop_smallimg = "小圖";
	var $s_shop_bigimg = "大圖";
	var $s_shop_allgoods = "所有商品";
	var $s_buyer_comm = "買家留言";
	var $s_post_comm = "發表留言";
	var $s_wantto_comm = "我要留言";
	var $s_login_pls = "您還沒有登陸！";
	var $s_type_comm_pls = "您的留言内容不能為空！";
	var $s_want_mkstore = "我要開店";
	var $s_shop_help = "商城幫助";
	var $s_click_viewbigimg = "移動鼠標看大圖";

	var $s_goods_price = "售　　價";
	var $s_no_price = "面議";
	var $s_goods_transport = "運　　費";
	var $s_goods_number = "庫　　存";
	var $s_goods_pv = "關 註 度";
	var $s_send_address = "發 貨 地";

	var $s_goods_wtbuy = "購買數量";
	var $s_g_askprice = "立即詢價";
	var $s_g_buy = "立即購買";
	var $s_g_tocart = "加入購物車";
	var $s_g_tofavorite = "加入收藏夾";
	var $s_certification = "認證信息";
	var $s_g_addedfavorite = "已成功添加到您的收藏夾！";
	var $s_g_stayfavorite = "此商品已在您的收藏夾裡！";
	var $s_g_addfailed = "添加失敗，請確認您是否已登陸！";
	var $s_g_addedcart = "已成功添加到您的購物車！";
	var $s_staycart = "此商品已在您的購物車裡！";
	var $s_nomachgoods = "庫存裡沒有這麼多商品！";
	var $s_seller_shop = "逛逛賣家店舖";
	var $s_details = "詳細介紹";
	var $s_wholesale = "批發說明";

	var $s_goods_mum = "還剩{num}件";
	var $s_goods_pvnum = "已有{pv}關註";
	var $s_company_Support = "商家支持";
	var $s_buy_n = "(可購 N 件)";
	var $s_collect = "人收藏";
	var $s_collect_num = "收藏人氣";
	var $s_contact_seller = "聯繫賣家";
	var $s_seller_c = "賣家信用";
	var $s_no_payment = "此商家沒有開通任何一種支付方式";
	var $s_shop_index = "商舖首頁";

	var $s_shop_noaddcategory = "本商舖還沒有添加分類！";
	var $s_question_see = "常見問題";
	var $s_safe_compp = "安全交易";
	var $s_process_of_purchase = "購買流程";
	var $s_howto_pay = "如何付款";
	var $s_contact_us = "聯繫我們";
	var $s_make_a_proposal = "合作提案";
	var $s_site_map = "網站地圖";
	var $s_approve_company = "認證商家";
	var $s_noapprove_company = "未認證商家";
	var $s_approve_info = "認證信息";
	var $s_store_insearch = "店内搜索";

	var $s_com = "商家信息";

	var $s_seller_credit = "賣家累積信用";
	var $s_week = "最近1周";
	var $s_month = "最近1個月";
	var $s_sex_month = "最近6個月";
	var $s_before_smonth = "6個月前";
	var $s_sum = "總計";
	var $s_good = "好評";
	var $s_centre = "中評";
	var $s_diff = "差評";
	var $s_good_pro = "好評率";
	var $s_buyer_com = "買家信息";
	var $s_buyer_credit = "買家累積信用";
	var $s_no_seller_credit = "沒有來自買家的評價";
	var $s_no_buyer_credit = "沒有來自賣家的評價";
	var $s_from_seller_credit = "來自買家的評價";
	var $s_from_buyer_credit = "來自賣家的評價";

	var $s_seller_reply = "店主回復";

	var $s_nickname = "賣家昵稱";
	var $s_goods_num = "商品數量";
	var $s_new_login = "最近登錄";
	var $s_creat_time = "創店時間";
	var $s_yan = "元";

	/* page */
	var $s_page_num = "共{num}條記錄";
	var $s_page_first = "首頁";
	var $s_page_pre = "上一頁";
	var $s_page_next = "下一頁";
	var $s_page_last = "尾頁";
	var $s_page_now = "當前";
	var $s_page_count = "共{num}頁";

	/* 團購 */
	var $s_goods_name = "商品名稱";
	var $s_groupbuy_price = "團購價";
	var $s_groupbuy_num = "成團數";
	var $s_groupbuy_goods = "團購名稱";
	var $s_groupbuy_time = "剩餘時間";
	var $s_groupbuy = "團購";
	var $s_groupbuy_start_time = "起始時間";
	var $s_groupbuy_regiment_number = "成團件數";
	var $s_groupbuy_restult_num = "已購件數";
	var $s_groupbuy_old_price = "原價";
	var $s_groupbuy_buy_num = "購買數量";
	var $s_groupbuy_real_name = "真實姓名";
	var $s_groupbuy_tel = "聯繫電話";
	var $s_groupbuy_add = "參加團購";
	var $s_groupbuy_del = "退出團購";
	var $s_groupbuy_isset_num = "請填寫購買數量!";
	var $s_groupbuy_isset_name = "請填寫真實姓名!";
	var $s_groupbuy_isset_tel = "請填寫聯繫電話!";
	var $s_groupbuy_isset_one = "購買數量不正確!";
	var $s_groupbuy_isset_true = "已成功參加團購!";
	var $s_groupbuy_isset_false = "已成功退出團購!";
	var $s_group_buy_state ="團購狀態";
	var $s_completed ="已完成";
	var $s_detriment ="購買";
	var $s_ongoing ="進行中";
	var $s_required ="必填";
	var $s_not_published ="未發布";
	var $s_group_buy_introduction ="團購介紹";
	var $s_isset_login = "您還沒有登錄";
	var $s_groupbuy_shows = "團購說明";
}
?>