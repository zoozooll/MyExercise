<?php
!function_exists('readover') && exit('Forbidden');
require GetLang('purview');
$nav_manager = array(
	'name'		=> '创始人',
	'items'		=> array(
		'manager',
		'rightset',
		'optimize',
		'diyoption',
		'advanced',
		'usercenter'=> array(
			'name'		=> '用户中心',
			'items'		=> array(
				'ucset',
				'ucapp',
				'uccredit',
				'ucnotify',
			),
		),
	),
);

$nav_left = array(
	'config' => array(
		'name' => '核心设置',
		'items' => array(
			'basic',
			'regset'	=> array(
				'name'	=> '注册设置',
				'items'	=> array(
					'reg',
					'customfield',
				),
			),
			'credit',
			/*
			'creditset'	=> array(
				'name'	=> '积分设置',
				'items'	=> array(
					'credit',
					'creditdiy',
					'creditchange',
				)
			),
			*/
			'attftp'	=> array(
				'name'	=>'附件设置',
				'items'	=>array(
					'att',
					'attachment',
					'attachrenew',
					'attachstats',
				)
			),
			'email',
			'help',
			'wap',
			'safe',
			'seoset',
			'member',
			'pcache',
			'sethtm',
		),
	),
	'consumer' => array(
		'name'	=> '会员权限',
		'items'	=> array(
			'groups'	=> array(
				'name'	=> '会员组设置',
				'items'=> array(
					'level',
					'userstats',
					'upgrade',
					'editgroup',
					'uptime',
				),
			),
			'members'	=> array(
				'name'	=> '会员管理',
				'items'=>array(
					'setuser',
					'delmember',
					'banuser',
					'viewban',
					'unituser',
				),
			),
			'usercheck'		=> array(
				'name'			=> '会员审核',
				'items'			=> array(
					'checkreg',
					'checkemail',
				),
			),
			'customcredit',
		),
	),
	'contentforums'	=> array(
		'name'	=> '内容版块',
		'items'	=> array(
			'forums' => array(
				'name'	=> '版块管理',
				'items'=> array(
					'setforum',
					'singleright',
					'uniteforum',
					'forumsell',
					'creathtm',
				),
			),
			'contentmanage' => array(
				'name'	=> '内容管理',
				'items'=> array(
					'article',
					'app_photos',
					'app_diary',
					'app_groups',
					'app_share',
					'app_write',
					'o_comments',
					'message',
					'report',
					'draftset',
					'recycle',
				),
			),
			'contentcheck'	=> array(
				'name'	=> '内容审核',
				'items'	=> array(
					'tpccheck',
					'setbwd',
					'urlcheck',
				),
			),
			'rulelist'	=> array(
				'name'=>'内容过滤设置',
				'items'=>array(
				)
			),
			'tagset',
			'pwcode',
			'setform',
			'overprint',
			'postcache',
		),
	),
	'datacache'	=> array(
		'name'	=>'数据缓存',
		'items'	=> array(
			'aboutcache'	=> array(
				'name'	=> '缓存相关',
				'items'	=> array(
					'updatecache',
					'pwcache',
					'guestdir',
				),
			),
			'database'	=> array(
				'name'	=> '数据库',
				'items'	=> array(
					'bakout',
					'bakin',
					'repair',
					'ptable',
				),
			),
			'log'	=> array(
				'name'	=> '管理日志',
				'items'	=> array(
					'adminlog',
					'forumlog',
					'creditlog',
					'adminrecord',
				),
			),
			'check'	=> array(
				'name'	=> '文件检查',
				'items'	=> array(
					'chmod',
					'safecheck',
				),
			),
			'ipban',
			'viewtoday',
			'datastate',
			'postindex',
		),
	),
	'applicationcenter'	=> array(
		'name'	=> '应用中心',
		'items'	=> array(
			'onlineapplication' => array(
				'name'	=> '在线应用',
				'items' => array(
					'onlineapp_setting',
					'onlineapp_list',
					'onlineapp_i9p',
				),
			),
			//taolianjie
			'taolianjie',
			//end
			'appslist',
			'onlineapp_ctid',
			'topiccate',
			'app_stopic',
			'postcate',
			'hackcenter',
			'job',
			'onlinepay' => array(
				'name'	=> '网上支付设置',
				'items' => array(
					'userpay',
					'orderlist',
				),
			),
		),
	),
	'markoperation'=> array(
		'name'	=> '运营工具',
		'items'	=> array(
			'setadvert',
			'announcement',
			'sendmail',
			'sendmsg',
			'navmenu'		=> array(
				'name'	=> '导航菜单管理',
				'items'=> array(
					'navmain',
					'navside',
					'navmode'
				),
			),
			'share',
			'plantodo',
			'present',
			'setads',
			'ystats',
			'sitemap',
		),
	),

			'modemanage'=> array(
				'name'	=> '模式设置',
				'items'=> array(
					'modeset',
					//'modestamp',
					//'modepush',
				),
			),
			'bbs'	=> array(
				'name'	=> '论坛模式',
				'items'=> array(
					'detail'	=> array(
						'name' => '界面设置',
						'items' => array(
							'index',
							'thread',
							'read',
							'popinfo',
							'jsinvoke',
						)
					),
					'rebang',
					'setstyles',
				),
	),
);

if (isset($db_modes['area'])) {
	$nav_left['area'] = array(
		'name'	=> '门户模式',
		'items'=> array(
			'area_tplcontent',
			'area_edittpl',
			'area_selecttpl',
			'maketemplate',
			'area_configarea',
		),
	);
}
if (isset($db_modes['o'])) {
	$nav_left['o'] = array(
		'name'	=> '圈子模式',
		'items'=> array(
			'o_global',
			//'o_app'			=> array('app应用',"$admin_file?adminjob=mode&admintype=o_app"),
		),
	);
}
?>