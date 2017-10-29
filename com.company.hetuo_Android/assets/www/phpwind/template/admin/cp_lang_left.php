<?php
!function_exists('readover') && exit('Forbidden');
require GetLang('purview');
$nav_head = array(
	'initiator'		=> '创始人',
	'config'		=> '核 心',
	'forums'		=> '版 块',
	'member'		=> '用 户',
	'read'			=> '帖 子',
	'wholemanager'	=> '管 理',
	'hackstyle'		=> '应用中心',
	'others'		=> '辅 助',
	'system'		=> '工 具',
	'extend'		=> '扩 展',
	'mode'			=> '模 式',
);
$nav_manager = array(
	'name'			=> '创始人选项',
	'option'		=> array(
		'rightset'		=> array('后台权限管理',"$admin_file?adminjob=rightset"),
		'manager'		=> array('创始人管理',"$admin_file?adminjob=manager"),
		'ystats'		=> array('量子恒道统计',"$admin_file?adminjob=ystats&action=config"),
		'diyoption'		=> array('常用选项定制',"$admin_file?adminjob=diyoption"),
		'optimize'		=> array('优化方案',"$admin_file?adminjob=optimize"),
	),
);

$nav_left = array(
	'config' => array(
		'settings' => array(
			'name' => '核心设置',
			'option' => array(
				'settings'	=> array(
					'basic'		=> array('常规设置',"$admin_file?adminjob=settings&admintype=basic"),
					'safe'		=> array('安全与优化',"$admin_file?adminjob=settings&admintype=safe"),
					'att'		=> array('附件与FTP',"$admin_file?adminjob=settings&admintype=att"),
					'credit'	=> array('积分设置',"$admin_file?adminjob=settings&admintype=credit"),
					'reg'		=> array('用户注册',"$admin_file?adminjob=settings&admintype=reg"),
					'member'	=> array('会员相关',"$admin_file?adminjob=settings&admintype=member"),
					'pcache'	=> array('页面缓存',"$admin_file?adminjob=settings&admintype=pcache"),
					'index'		=> array('首页细节',"$admin_file?adminjob=settings&admintype=index"),
					'thread'	=> array('主题列表细节',"$admin_file?adminjob=settings&admintype=thread"),
					'read'		=> array('帖子阅读细节',"$admin_file?adminjob=settings&admintype=read"),
					'jsinvoke'	=> array('JS调用',"$admin_file?adminjob=settings&admintype=jsinvoke"),
					'popinfo'	=> array('弹出信息设置',"$admin_file?adminjob=settings&admintype=popinfo"),
					'wap'		=> array('WAP设置',"$admin_file?adminjob=settings&admintype=wap"),
					'email'		=> array('邮件设置',"$admin_file?adminjob=settings&admintype=email"),
				),
			),
		),
	),
	'forums' => array(
		'forummaneger'=>array(
			'name'	=> '版块管理',
			'option'=> array(
				'setforum'		=> array('版块管理',"$admin_file?adminjob=setforum"),
				'uniteforum'	=> array('版块合并',"$admin_file?adminjob=uniteforum"),
				'creathtm'		=> array('生成htm',"$admin_file?adminjob=creathtm"),
				'forumsell'		=> array('版块出售记录',"$admin_file?adminjob=forumsell"),
				'singleright'	=> array('版块用户权限',"$admin_file?adminjob=singleright"),
			),
		),
	),
	'read' => array(
		'artmanager' => array(
			'name'	=> '帖子管理',
			'option'=> array(
				'tpccheck'		=> array('审核帖子',"$admin_file?adminjob=tpccheck"),
				'tagset'		=> array('标签管理',"$admin_file?adminjob=tagset"),
				'pwcode'		=> array('自定义代码',"$admin_file?adminjob=pwcode"),
				'setbwd'		=> array('词语过滤',"$admin_file?adminjob=setbwd"),
				'setform'		=> array('预设帖子格式',"$admin_file?adminjob=setform"),
				'topiccate'		=> array('帖子分类信息',"$admin_file?adminjob=topiccate"),
				'postcate'		=> array('团购活动',"$admin_file?adminjob=postcate"),
				'urlcheck'		=> array('外链提醒白名单',"$admin_file?adminjob=urlcheck"),
			),
		),
		'attmanager'=>array(
			'name'	=> '附件管理',
			'option'=> array(
				'attachment'	=> array('附件管理',"$admin_file?adminjob=attachment"),
				'attachstats'	=> array('附件统计',"$admin_file?adminjob=attachstats"),
				'attachrenew'	=> array('附件修复',"$admin_file?adminjob=attachrenew"),
			),
		),
		'appmanager'=>array(
			'name'	=> '基础app管理',
			'option'=> array(
				'app_photos'		=> array('相册管理',"$admin_file?adminjob=apps&admintype=app_photos&action=albums"),
				'app_diary'		=> array('日志管理',"$admin_file?adminjob=apps&admintype=app_diary&action=cp"),
				'app_groups'		=> array('群组帖子管理',"$admin_file?adminjob=apps&admintype=app_groups&action=argument"),
				'app_share'		=> array('分享管理',"$admin_file?adminjob=apps&admintype=app_share&action=share"),
				'app_write'		=> array('记录管理',"$admin_file?adminjob=apps&admintype=app_write&action=writes"),
				'app_hot'		=> array('热榜',"$admin_file?adminjob=apps&admintype=app_hot"),
				'topiccate'		=> array('分类信息',"$admin_file?adminjob=topiccate"),
				'postcate'		=> array('团购活动',"$admin_file?adminjob=postcate"),
				'app_stopic'	=> array('专题制作',"$admin_file?adminjob=apps&admintype=app_stopic"),
			),
		),
	),
	'member' => array(
		'members'	=> array(
			'name'	=> '会员管理',
			'option'=>array(
				'setuser'		=> array('会员管理',"$admin_file?adminjob=setuser"),
				'delmember'		=> array('删除会员',"$admin_file?adminjob=superdel&admintype=delmember"),
				'unituser'		=> array('合并会员',"$admin_file?adminjob=unituser"),
				'usercheck'		=> array(
					'checkreg'		=> array('注册审核',"$admin_file?adminjob=usercheck&admintype=checkreg"),
					'checkemail'	=> array('邮件审核',"$admin_file?adminjob=usercheck&admintype=checkemail"),
				),
				'banuser'		=> array('会员禁言',"$admin_file?adminjob=banuser"),
				'viewban'		=> array('查看禁言会员',"$admin_file?adminjob=viewban"),
				'customcredit'	=> array('自定义积分',"$admin_file?adminjob=customcredit"),
				'ucset'			=> array('用户中心设置',"$admin_file?adminjob=ucset"),
				'ucapp'			=> array('应用管理',"$admin_file?adminjob=ucapp"),
				'uccredit'		=> array('积分同步',"$admin_file?adminjob=uccredit"),
				'ucnotify'		=> array('消息队列',"$admin_file?adminjob=ucnotify"),
			),
		),
		'groups'	=> array(
			'name'	=> '会员组管理',
			'option'=> array(
				'level'			=> array('会员组权限',"$admin_file?adminjob=level"),
				'userstats'		=> array('成员统计',"$admin_file?adminjob=userstats"),
				'upgrade'		=> array('会员组提升方案',"$admin_file?adminjob=upgrade"),
				'editgroup'		=> array('批量添加',"$admin_file?adminjob=editgroup"),
				'uptime'		=> array('有效期设置',"$admin_file?adminjob=uptime"),
			),
		),
	),
	'wholemanager' => array(
		'wholemanager'=> array(
			'name'		=> '统筹管理',
			'option'	=> array(
				'sethtm'		=> array('静态目录部署',"$admin_file?adminjob=sethtm"),
				'datastate'		=> array('数据统计',"$admin_file?adminjob=datastate"),
				'sitemap'		=> array('网站地图',"$admin_file?adminjob=sitemap"),
				'postcache'		=> array('动作表情',"$admin_file?adminjob=postcache"),
				'ipban'			=> array('IP 禁止',"$admin_file?adminjob=ipban"),
				'ipstates'		=> array('IP 统计',"$admin_file?adminjob=ipstates"),
				'ipsearch'		=> array('IP 检索',"$admin_file?adminjob=ipsearch"),
				'customfield'	=> array('会员栏目',"$admin_file?adminjob=customfield"),
				'updatecache'	=> array('缓存管理',"$admin_file?adminjob=updatecache"),
				'creditdiy'		=> array('自定义积分',"$admin_file?adminjob=creditdiy"),
				'creditchange'	=> array('积分转换',"$admin_file?adminjob=creditchange"),
				'rebang'		=> array('热榜排行',"$admin_file?adminjob=rebang"),
				'pwcache'		=> array('pw缓存库',"$admin_file?adminjob=pwcache"),
				'report'		=> array('举报管理',"$admin_file?adminjob=report"),
				'msphinx'       => array('Sphinx全文索引',"$admin_file?adminjob=advanced&admintype=msphinx"),
				'mmemcache'     => array('Memcahce缓存',"$admin_file?adminjob=advanced&admintype=mmemcache"),
			),
		),
		'log'=>array(
			'name'	=> '管理日志',
			'option'	=> array(
				'record'		=> array(
					'adminlog'	=> array('后台管理日志',"$admin_file?adminjob=record&admintype=adminlog"),
				),
				'forumlog'		=> array('前台管理日志',"$admin_file?adminjob=forumlog"),
				'creditlog'		=> array('积分流通日志',"$admin_file?adminjob=creditlog"),
				'adminrecord'		=> array('后台管理记录',"$admin_file?adminjob=adminrecord"),
			),
		),
	),
	'hackstyle' => array(
		'hackstyle'=> array(
			'name'	=> '应用中心',
			'option'=> array(
				'app'			=> array('APP应用',"$admin_file?adminjob=app"),
				'hackcenter'	=> array('插件中心',"$admin_file?adminjob=hackcenter"),
				'setstyles'		=> array('风格模板',"$admin_file?adminjob=setstyles"),
			),
		),
	),
	'others' => array(
		'info'	=> array(
			'name'	=> '信息管理',
			'option'=> array(
				'announcement'	=> array('公告管理',"$admin_file?adminjob=announcement"),
				'draftset'		=> array('草稿箱管理',"$admin_file?adminjob=draftset"),
				'sendmail'		=> array('邮件群发',"$admin_file?adminjob=sendmail"),
				'sendmsg'		=> array('消息群发',"$admin_file?adminjob=sendmsg"),
				'present'		=> array('节日送礼',"$admin_file?adminjob=present"),
			),
		),
		'assistant'=> array(
			'name'	=> '辅助管理',
			'option'=> array(
				'setads'		=> array('宣传设置',"$admin_file?adminjob=setads"),
				'share'			=> array('友情链接',"$admin_file?adminjob=share"),
				'viewtoday'		=> array('今日到访会员',"$admin_file?adminjob=viewtoday"),
				'chmod'			=> array('文件属性检查',"$admin_file?adminjob=chmod"),
				'safecheck'		=> array('文件安全检查',"$admin_file?adminjob=safecheck"),
				'help'			=> array('自定义帮助文档',"$admin_file?adminjob=help"),
				'navmain'		=> array('社区主导航',"$admin_file?adminjob=customnav&admintype=navmain"),
				'navside'		=> array('顶部底部导航',"$admin_file?adminjob=customnav&admintype=navside"),
				'navmode'		=> array('模式导航',"$admin_file?adminjob=customnav&admintype=navmode"),
				'setadvert'		=> array('广告管理',"$admin_file?adminjob=setadvert"),
				'postindex'		=> array('帖子索引',"$admin_file?adminjob=postindex"),
			),
		),
	),
	'system' => array(
		'supdel' => array(
			'name'	=> '批量删除',
			'option'=> array(
				'superdel'		=> array(
					'article'		=> array('删除帖子',"$admin_file?adminjob=superdel&admintype=article"),
					'delmember'		=> array('删除会员',"$admin_file?adminjob=superdel&admintype=delmember"),
					'message'		=> array('删除短消息',"$admin_file?adminjob=superdel&admintype=message"),
				),
				'guestdir'		=> array('缓存文件',"$admin_file?adminjob=guestdir"),
				'recycle'		=> array('回收站',"$admin_file?adminjob=recycle"),
			),
		),
		'task'	=> array(
			'name'	=> '计划任务',
			'option'=> array(
				'plantodo'		=> array('计划任务管理',"$admin_file?adminjob=plantodo"),
				'addplan'		=> array('添加新任务',"$admin_file?adminjob=addplan"),
			),
		),
		'database'=>array(
			'name'	=> '数据库',
			'option'=> array(
				'bakup'		=> array(
					'bakout'		=> array('数据备份',"$admin_file?adminjob=bakup&admintype=bakout"),
					'bakin'			=> array('数据恢复',"$admin_file?adminjob=bakup&admintype=bakin"),
				),
				'repair'		=> array('数据修复',"$admin_file?adminjob=repair"),
				'ptable'		=> array('数据库分卷',"$admin_file?adminjob=ptable"),
			),
		),
	),
	'extend' => array(
		'onlinepay' => array(
			'name'	=> '网上支付设置',
			'option'=> array(
				'userpay'		=> array('网上支付',"$admin_file?adminjob=userpay"),
				'orderlist'		=> array('订单管理',"$admin_file?adminjob=orderlist"),
			),
		),
	),
	'mode' => array(
		'modemanage'=> array(
			'name'	=> '模式管理',
			'option'=> array(
				'modeset'		=> array('模式设置',"$admin_file?adminjob=modeset"),
				//'modestamp'		=> array('模块分类设置',"$admin_file?adminjob=modestamp"),
				//'modepush'		=> array('推送帖子管理',"$admin_file?adminjob=modepush"),
			),
		),
	),
);
if (isset($db_modes['area'])) {
	$nav_left['mode']['area'] = array(
		'name'	=> '门户模式',
		'option'=> array(
			'area_tplcontent'	=> array('模块内容管理',"$admin_file?adminjob=mode&admintype=area_tplcontent"),
			'area_edittpl'		=> array('本站模板管理',"$admin_file?adminjob=mode&admintype=area_edittpl"),
			'area_selecttpl'	=> array('模板中心',"$admin_file?adminjob=mode&admintype=area_selecttpl"),
			'maketemplate'		=> array('模块生成',"$admin_file?adminjob=maketemplate"),
			'area_configarea'	=> array('门户首页静态化',"$admin_file?adminjob=mode&admintype=area_configarea"),
		),
	);
}
if (isset($db_modes['o'])) {
	$nav_left['mode']['o'] = array(
		'name'	=> '圈子模式',
		'option'=> array(
			'o_global'		=> array('圈子核心',"$admin_file?adminjob=mode&admintype=o_global"),
			'o_comments'	=> array('评论',"$admin_file?adminjob=mode&admintype=o_comments"),

		),
	);
}

if (file_exists('c_global.php')) {
	$nav_left['extend']['cms'] = array(
		'name'	=> '文章系统',
		'option'=> array(
			'c_set'		=> array('参数设置',"$admin_file?adminjob=c_set"),
			'c_forum'	=> array('分类管理',"$admin_file?adminjob=c_forum"),
			'c_atc'		=> array('内容管理',"$admin_file?adminjob=c_atc"),
			'c_unite'	=> array('合并分类',"$admin_file?adminjob=c_unite"),
			'c_htm'		=> array('生成htm',"$admin_file?adminjob=c_htm"),
		),
	);
}
?>