<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

class indexlp{
	var $i_location = "所在位置";
	var $i_index = "首頁";
	var $i_search = "搜索頁";
	var $i_s_company = "搜商家";
	var $i_goods_category = "商品詳細分類";
	var $i_want_buy = "我要買";
	var $i_want_sell = "我要賣";
	var $i_my_shop = "我的店舖";
	var $i_u_center = "用戶中心";
	var $i_hi = "您好";
	var $i_welcome = "您好，歡迎來到";
	var $i_logout_safe = "安全退出";
	var $i_serach_ad = "高級搜索";
	var $i_keywords = "關鍵字";
	var $i_category = "分類";
	var $i_brand = "品牌";
	var $i_post = "提交";
	var $i_store_list = "商家列表";
	var $i_like_storelist = "相關店舖";
	var $i_searchs = "搜索";
	var $i_hot_tags = "熱門Tags";
	var $i_category_toselect_goods = "按分類選擇商品";
	var $i_user_register = "會員註冊";
	var $i_s_com = "商家信息";
	var $i_loading_img = "正在加載清晰圖片";
	var $i_getback_pw = "找回密碼";

	/* index */
	var $i_seller = "賣家";
	var $i_fastsell = "活動促銷";
	var $i_amsell = "我是賣家";
	var $i_ammall = "我是買家";
	var $i_notice_board = "公告欄";
	var $i_activities_board = "活動欄";
	var $i_u_register = "用戶註冊";
	var $i_u_login = "用戶登陸";
	var $i_u_guide = "新手指南";
	var $i_u_loginnow = "現在登錄";
	var $i_u_alipaylogin = "支付寶登錄";
	var $i_g_category = "產品分類";
	var $i_g_torelease = "我要發布產品";
	var $i_g_hot = "熱門產品";
	var $i_g_saleslist = "銷售排行";
	var $i_g_bestcompany = "優秀企業";
	var $i_g_webhelp = "網站幫助";
	var $i_u_start = "新手上路";
	var $i_iam_buyer = "我是買家";
	var $i_shop_service = "商城服務";
	var $i_all_category = "所有分類";
	var $i_service_tel = "客服電話";
	var $i_service_something = "客服熱線不受理商品咨詢！如需購買咨詢，請直接聯繫出售該商品的商家。";
	var $i_bbs_best = "社區精華";
	var $i_language_ch = "語言選擇";
	var $i_group_buy_small_shops = "小商舖如何團購賣商品？";
	var $i_credit_evaluation_system = "店舖信用評價體繫";

	/* search brand category */
	var $i_s_result = "搜索結果";
	var $i_s_kindseach = "全部商品分類導航";
	var $i_find_num = "共找到了{num}個匹配結果";
	var $i_c_logo = "店標";
	var $i_c_info = "商家信息/介紹";
	var $i_g_shop_info = "產品信息";
	var $i_s_goto = "進入店舖";
	var $i_c_hotgoods = "商家熱賣產品";
	var $i_hotgoods_rec = "熱門產品推薦";
	var $i_more_hotgoods = "更多熱門產品";
	var $i_comapny_none = "沒有商家！";
	var $i_low_height = "低->高";
	var $i_height_low = "高->低";
	var $i_search_pv = "人氣";
	var $i_web_rrr = "已經過壓縮機商城網的認證";
	var $i_price_discuss = "面議";
	var $i_loading_bigimage = "正在加載清晰圖片";
	var $i_cate_result = "分類結果";
	var $i_cate_allgoods = "下的所有產品";
	var $i_cate_sub = "子分類";
	var $i_choice_sort = "按分類選擇商品";
	var $i_choice_good = "您選擇的所有商品";
	var $i_list = "列表";
	var $i_show_window = "櫥窗";
	var $i_handle = "具體操作";
	var $i_particular = "詳細";
	var $i_buy = "購買";
	var $i_no_goods = "沒有產品";
	var $i_boutique = "精品推薦";
	var $i_hot = "熱點排行";
	var $i_new = "最新瀏覽";
	var $i_approve_company = "認證商家";
	var $i_noapprove_company = "未認證商家";
	var $i_send_to = "發送至";
	var $i_askprice_title = "詢價標題";
	var $i_select_products_by_brand ="按品牌選擇商品";

	/* login */
	var $i_au_register = "你還沒有註冊用戶？";
	var $i_register_now = "馬上註冊";
	var $i_registeru_loginnow = "註冊用戶請直接登錄";
	var $i_login_email = "郵箱/用戶名";
	var $i_login_password = "密　　　碼";
	var $i_login_in = "登 錄";
	var $i_login_forgot = "忘記密碼了？";
	var $i_verifycode = "驗　證　碼";
	var $i_email_notnone = "郵箱不能為空！";
	var $i_password_notnone = "密碼不能為空！";
	var $i_verifycode_notnone = "請輸入驗證碼！";
	var $i_admin ="後台管理繫統";


	/* reg */
	var $i_reginfo_1 = "1、填寫會員信息";
	var $i_reginfo_2 = "2、通過郵件激活";
	var $i_reginfo_3 = "3、註冊成功";
	var $i_reg_info = "填寫你的註冊資料";
	var $i_reg_username = "會員用戶名";
	var $i_reg_unameinfo = "由4-16位數字或英文字母組成";
	var $i_reg_email = "Email帳號";
	var $i_reg_emailinfo = "如：shop@jooyea.com";
	var $i_reg_passwd = "設置密碼";
	var $i_reg_passwdinfo = "數字或英文，4-16位";
	var $i_reg_repasswd = "確認密碼";
	var $i_reg_nameandcontact = "姓名和聯繫方式";
	var $i_reg_truename = "真實姓名";
	var $i_reg_truenameinfo = "請填寫2-4個漢字";
	var $i_reg_tel = "固定電話";
	var $i_reg_telinfo = "如：053188888888";
	var $i_reg_mobile = "手機";
	var $i_reg_mobileinfo = "如：13688888888";
	var $i_reg_inputvf = "請輸入上圖中的文字";
	var $i_reg_postreg = "同意以下條款，並提交註冊信息";
	var $i_rmsg_inputuname = "請填寫會員用戶名！";
	var $i_rmsg_formatname = "填寫的會員用戶名格式不正確！";
	var $i_rmsgname_checknow = "正在檢測您的用戶名是否可用！";
	var $i_rmsgname_isok = "恭喜，您的用戶名可用！";
	var $i_rmsgname_used = "很抱歉，您的用戶名已被使用！";
	var $i_rmsgmail_input = "請填寫Email帳號！";
	var $i_rmsgmail_format = "填寫的Email帳號格式不正確！";
	var $i_rmsgmail_checknow = "正在檢測您的Email帳號是否可用！";
	var $i_rmsgmail_isok = "恭喜，您的Email帳號可用！";
	var $i_rmsgmail_used = "很抱歉，您的Email帳號已被使用！";
	var $i_rmsgpwd_input = "請填寫密碼！";
	var $i_rmsgpwd_format = "填寫的密碼格式不正確！";
	var $i_rmsgpwd_right = "密碼格式正確！";
	var $i_rmsgrepwd_format = "確認密碼格式正確！";
	var $i_rmsgrepwd_input = "請填寫確認密碼！";
	var $i_rmsgpwd_notfaf = "兩次填寫的密碼不一致！";
	var $i_rmsgtn_input = "請填寫真實姓名！";
	var $i_rmsgtn_notright = "填寫的真實姓名格式不正確,請填寫2-4個漢字！";
	var $i_rmsgtn_right = "真實姓名格式正確！";
	var $i_rmsgtel_input = "請填寫聯繫電話！";
	var $i_rmsgtel_notright = "填寫的聯繫電話格式不正確！";
	var $i_rmsgtel_right = "聯繫電話格式正確！";
	var $i_rmsgmob_input = "請填寫聯繫手機！";
	var $i_rmsgmob_notright = "填寫的聯繫手機格式不正確！";
	var $i_rmsgmob_right = "聯繫手機格式正確！";
	var $i_rmsgvf_input = "請填寫驗證碼！";
	var $i_reg2_checkmail = "就差最後一步了，登錄您註冊時填寫的郵箱完成註冊吧！";
	var $i_reg2_loginmail = "登錄郵箱激活";
	var $i_reg2_nomail = "沒有收到郵件？";
	var $i_reg3_regsucess = "恭喜您，註冊成功！";
	var $i_goto_manage = "進入管理";
	var $i_find_goods = "查找商品";
	var $i_member_login = "會員登錄";
	var $i_welcome_iwebshop = "歡迎您登錄{iweb_mall}多用戶商城繫統";
	var $i_remembe_me = "記住我（請不要在公共計算機上使用此項）";
	var $i_login_info_first = "便宜有好貨！超過1000萬件商品任您選。";
	var $i_noshop_account_clickhere = "如果你已擁有{iweb_mall}賬戶<br />請點擊";
	var $i_pls_info_safe_msg = "請認真、仔細地填寫下以信息，嚴肅的商業信息有助於您獲得别人的信任。";
	var $i_login_info_sec = "買賣更安心！支付寶交易超安全。";
	var $i_login_info_the = "輕鬆賺錢交商友。";
	var $i_login_info_foru = "超人氣社區！精彩活動每一天。";
	var $i_iwantget_price = "我要詢價";
	var $i_article_list = "文章分類";
	var $i_up_article = "上一篇";
	var $i_down_article = "下一篇";
	var $i_none_article = "沒有了";
	var $i_none_articles = "沒有文章";

	var $i_question_see = "常見問題";
	var $i_safe_compp = "安全交易";
	var $i_process_of_purchase = "購買流程";
	var $i_howto_pay = "如何付款";
	var $i_contact_us = "聯繫我們";
	var $i_make_a_proposal = "合作提案";
	var $i_site_map = "網站地圖";
	var $i_email_complete_registration ="請登陸郵箱進行郵箱驗證完成註冊！";
	var $i_resend_verification ="重新發送驗證碼";
	var $i_email_reminded_end ="如果您已經填寫了註冊信息，請到您註冊的郵箱點擊驗證。<br />如驗證失敗，可使用本方式重新發送驗證連接。<br />請填寫註冊時的郵箱，否則将無法發送郵件到您的郵箱内。";

	/* forgot */
	var $i_find_password = "找回密碼";
	var $i_forgot_1 = "1、請輸入會員名";
	var $i_forgot_2 = "2、請輸入您的註冊電子郵箱";
	var $i_forgot_3 = "3、密碼成功找回";
	var $i_fgot_inputname = "請輸入會員名";
	var $i_next_step = "下一步";
	var $i_fgot_namenotnone = "會員名稱不能為空！";
	var $i_fgot2_info = "請填寫下面信息，繫統将通過郵箱找回密碼";
	var $i_fgot2_email = "註冊郵箱";
	var $i_fgot2_emailnotnone = "註冊郵箱不能為空！";
	var $i_complete = "完成";

	/* inquiry */
	var $i_inquiry = "詢價";
	var $i_inquiry_info1 = "1、向供應商發送詢價";
	var $i_inquiry_info2 = "2、詢價完成";
	var $i_inquiry_sucess = "恭喜您，詢價完成！";
	var $i_back_goodspage = "返回當前商品頁";
	var $i_inq_resave = "接收方";
	var $i_inq_comname = "公司名稱";
	var $i_inq_address = "聯繫地址";
	var $i_inq_askinfo = "填寫詢價内容";
	var $i_inq_title = "標題";
	var $i_inq_msat = "咨詢{name}相關信息";
	var $i_inq_content = "内容";
	var $i_inq_namelink = "填寫姓名和聯繫方式";
	var $i_inq_truename = "真實姓名";
	var $i_inq_email = "E-mail地址";
	var $i_inq_tel = "固定電話";
	var $i_inq_mob = "手機";
	var $i_inq_telandmobile = "固定電話和手機號碼至少填寫一項";
	var $i_inq_sendmsg = "發送詢價";

	var $i_allgoodsheader_category = "查看所有商品類目";
	var $i_goodsheader_category = "商品分類";
	var $i_new_goods = "新品速遞";
	var $i_promote_goods = "精品促銷";
	var $i_brand_sort = "品牌專區";
	var $i_links_list = "友情鏈接";
	var $i_hotgoods_sort = "人氣排行";
	var $i_new_goodsb = "最新上架";
	var $i_index_nocite = "公告欄";
	var $i_new_user_help = "新手上路";
	var $i_register_free = "免費註冊";
	var $i_best_store = "推薦店舖";
	var $i_goods_num = "商品數量";
	var $i_shop_info = "商品數量";
	var $i_shop_logo = "店標";
	var $i_iwant_mkstore = "我要開店";
	var $i_shop_help = "商城幫助";
	var $i_about_shop = "關於商城";
	var $i_goods_search = "搜商品";
	var $i_goods_infos = "商品信息";



	/* page */
	var $i_page_num = "共{num}條記錄";
	var $i_page_first = "首頁";
	var $i_page_pre = "上一頁";
	var $i_page_next = "下一頁";
	var $i_page_last = "尾頁";
	var $i_page_now = "當前";
	var $i_page_count = "共{num}頁";


	var $i_more_search = "更多搜索";
	var $i_creat_time = "創建時間";
	var $i_in_area = "所在地區";
	var $i_specialarea = "專區";
	var $i_info = "摘要";
	var $i_shop = "店舖";
	var $i_news = "資訊";
	var $i_price = "價格";
	var $i_company = "商家";
	var $i_goods = "產品";
	var $i_more = "更多";
	var $i_moregoods = "更多推薦商品";
	var $i_moreshop = "更多店舖";
	var $i_recommend = "推薦";
	var $i_best = "精品";
	var $i_promote = "促銷";
	var $i_logout = "退出";
	var $i_login = "登錄";
	var $i_register = "註冊";
	var $i_cart = "購物車";
	var $i_favorite = "收藏夾";
	var $i_back_index = "返回首頁";
	var $i_yan = "元";
	var $i_is_vis = "認證";
	var $i_shopour = "店主";

	var $i_select_goods = "選擇商品";
	var $i_select = "按";
	var $i_choice_attr = "按{num}選擇商品";
}

// g 商品
// u 用戶
// s 搜索
// c 商家
?>