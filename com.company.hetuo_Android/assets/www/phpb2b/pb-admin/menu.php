<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
$menus = array(
    'dashboard' => array(
        'text'      => L("homepage", "tpl"),
        'subtext'   => L("common_action", "tpl"),
        'default'   => 'welcome',
        'children'  => array(
            'welcome'   => array(
                'text'  => L("welcome_page", "tpl"),
                'url'   => 'home.php',
            ),
            'basic_setting'  => array(
                'parent'=> 'setting',
                'text'  => L("settings", "tpl"),
                'url'   => 'setting.php?do=basic',
            ),
            'member'  => array(
                'parent'=> 'members',
                'text'  => L("member_manage", "tpl"),
                'url'   => 'member.php',
            ),
            'note'  => array(
                'parent'=> 'tools',
                'text'  => L("my_notes", "tpl"),
                'url'   => 'adminnote.php',
            ),
        ),
    ),
    'setting'   => array(
        'text'      => L("global", "tpl"),
        'subtext'	=> L("global_set", "tpl"),
        'default'   => 'basic_setting',
        'children'  => array(
            'basic_setting'  => array(
                'text'  => L("common_action", "tpl"),
                'url'   => 'setting.php?do=basic',
            ), 
            'register' => array(
                'text'  => L("reg_and_visit", "tpl"),
                'url'   => 'setting.php?do=register',
            ),
			 'functions' => array(
                'text'  => L("functions", "tpl"),
                'url'   => 'setting.php?do=functions',
            ),
            'cache' => array(
                'text'  => L("cache_setting", "tpl"),
                'url'   => 'setting.php?do=cache',
            ),
            'datetime' => array(
                'text'  => L("time_set", "tpl"),
                'url'   => 'setting.php?do=datetime',
            ),
			 'auth' => array(
                'text'  => L("secure_set", "tpl"),
                'url'   => 'setting.php?do=secure',
            ),
			 'industry' => array(
                'text'  => L("industry", "tpl"),
                'url'   => 'industry.php',
            ),
			 'area' => array(
                'text'  => L("area", "tpl"),
                'url'   => 'area.php',
            ),
			 'announce' => array(
                'text'  => L("announcement", "tpl"),
                'url'   => 'announce.php',
            ),
			 'userpage' => array(
                'text'  => L("userpage", "tpl"),
                'url'   => 'userpage.php',
            ),
			 'service' => array(
                'text'  => L("service_center", "tpl"),
                'url'   => 'service.php',
            ),
			 'email' => array(
                'text'  => L("email_set", "tpl"),
                'url'   => 'setting.php?do=email',
            ),
        ),
    ),
    'infocenter' => array(
        'text'      => L("info_manage", "tpl"),
        'subtext'	=> L("com_info_manage", "tpl"),
        'default'   => 'trade',
        'children'  => array(
            'trade' => array(
                'text'  => L("offer", "tpl"),
                'url'   => 'offer.php',
            ),
            'product' => array(
                'text'  => L("product_center", "tpl"),
                'url'   => 'product.php',
            ),
            'company' => array(
                'text'  => L("yellow_page", "tpl"),
                'url'   => 'company.php',
            ),
            'market' => array(
                'text'  => L("pro_market", "tpl"),
                'url'   => 'market.php',
            ),
            'job' => array(
                'text'  => L("job", "tpl"),
                'url'   => 'job.php',
            ),
            'fair' => array(
                'text'  => L("fair", "tpl"),
                'url'   => 'fair.php',
            ),
            'news' => array(
                'text'  => L("industry_news", "tpl"),
                'url'   => 'news.php',
            ),
            'standard' => array(
                'text'  => L('cp_standard', 'tpl'),
                'url'   => 'standard.php',
            ),
            'tag' => array(
                'text'  => L("tag_center", "tpl"),
                'url'   => 'tag.php',
            ),
            'keyword' => array(
                'text'  => L("keyword_center", "tpl"),
                'url'   => 'keyword.php',
            ),
            'companynews' => array(
                'text'  => L("companynews", "tpl"),
                'url'   => 'companynews.php',
            ),
            'dict' => array(
                'text'  => L("industry_dict", "tpl"),
                'url'   => 'dict.php',
            ),
                                    
        ),
    ),
    'members'     => array(
        'text'      => L("member_center", "tpl"),
        'subtext'	=> L("member_manage_center", "tpl"),
        'default'   => 'member',
        'children'  => array(
            'member' => array(
                'text'  => L("member_manage", "tpl"),
                'url'   => 'member.php',
            ),
			'membergroup'  => array(
                'text'  => L("membergroup", "tpl"),
                'url'   => 'membergroup.php?type=define',
            ),
            'member_add' => array(
                'text'  => L("add_member", "tpl"),
                'url'   => 'member.php?do=edit',
            ),
            'member_add' => array(
                'text'  => L("pms", "tpl"),
                'url'   => 'message.php',
            ),
			 'adminer' => array(
                'text'  => L("adminer", "tpl"),
                'url'   => 'adminer.php',
            ),
			 'adminer_passwd' => array(
                'text'  => L("change_pass", "tpl"),
                'url'   => 'adminer.php?do=password',
            ),
        ),
    ),
    'ads' => array(
        'text'      => L("ads", "tpl"),
        'subtext'	=> L("ads_manage", "tpl"),
        'default'   => 'ad',
        'children'  => array(
            'ad' => array(
                'text'  => L("ads", "tpl"),
                'url'   => 'ad.php',
            ),
			 'ad_add' => array(
                'text'  => L("add_ads", "tpl"),
                'url'   => 'ad.php?do=edit',
            ),
			 'adzone' => array(
                'text'  => L("adzone", "tpl"),
                'url'   => 'adzone.php',
            ),
            'goods' => array(
                'text'  => L("service_manage", "tpl"),
                'url'   => 'goods.php',
            ),
            'order' => array(
                'text'  => L("order_center", "tpl"),
                'url'   => 'order.php',
            ),
			 'friendlink' => array(
                'text'  => L("friendlink", "tpl"),
                'url'   => 'friendlink.php',
            ),
        ),
    ),
    'templet' => array(
        'text'      => L("templet", "tpl"),
        'default'   => 'skin',
        'children'  => array(
			 'language' => array(
                'text'  => L("templet_language", "tpl"),
                'url'   => 'language.php',
            ),
			 'skin' => array(
                'text'  => L("company_templet", "tpl"),
                'url'   => 'templet.php?type=user',
            ),
			 'system' => array(
                'text'  => L("system_templet", "tpl"),
                'url'   => 'templet.php?type=system',
            ),
			 'nav' => array(
                'text'  => L("nav", "tpl"),
                'url'   => 'nav.php',
            ),
        ),
    ),
    'tools' => array(
        'text'      => L("system_tool", "tpl"),
        'default'   => 'cache_update',
        'children'  => array(
            'cache_update' => array(
                'text'  => L("cache", "tpl"),
                'url'   => 'htmlcache.php',
            ),
			'log' => array(
                'text'  => L("log_browse", "tpl"),
                'url'   => 'log.php',
            ),
            'db' => array(
                'text'  => L("database", "tpl"),
                'url'   => 'db.php',
            ),
			'passport' => array(
                'text'  => L("passport", "tpl"),
                'url'   => 'passport.php',
            ),
			'checkfile' => array(
			     'text'  =>L("check_file", "tpl"),
				 'url'   =>'checkfile.php',
		    ),
           'payment' => array(
                'text'  => L("payment_method", "tpl"),
                'url'   => 'payment.php',
            ),
			'note' => array(
                'text'  => L("my_notes", "tpl"),
                'url'   => 'adminnote.php',
            ),
        ),
    ),
    'extesions' => array(
        'text'      => L("extension", "tpl"),
        'default'   => 'help',
        'children'  => array(
			 'help' => array(
                'text'  => L("help_file", "tpl"),
                'url'   => 'help.php',
            ),
			 'type' => array(
                'text'  => L("options", "tpl"),
                'url'   => 'type.php',
            ),
			 'trust' => array(
                'text'  => L("trusts", "tpl"),
                'url'   => 'trust.php',
            ),
        ),
    ),
    'plugins' => array(
        'text'      => L("plugin", "tpl"),
        'default'   => 'plugin',
        'children'  => array(
            'plugin'   => array(
                'text'  => L("management", "tpl"),
                'url'   => 'plugin.php',
            ),
        ),
    ),
);
?>