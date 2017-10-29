<?php
!function_exists('readover') && exit('Forbidden');
$purview = array(
	'basic'			=> array('常规设置',"$admin_file?adminjob=settings&admintype=basic"),
	'safe'			=> array('安全与优化',"$admin_file?adminjob=settings&admintype=safe"),
	'seoset'		=> array('SEO优化',"$admin_file?adminjob=seoset"),
	'att'			=> array('附件设置',"$admin_file?adminjob=settings&admintype=att"),
	'attachment'	=> array('附件优化管理',"$admin_file?adminjob=attachment"),
	'attachrenew'	=> array('附件修复',"$admin_file?adminjob=attachrenew"),
	'attachstats'	=> array('附件统计',"$admin_file?adminjob=attachstats"),
	'credit'		=> array('积分设置',"$admin_file?adminjob=settings&admintype=credit"),
	'reg'			=> array('用户注册控制',"$admin_file?adminjob=settings&admintype=reg"),
	'member'		=> array('会员相关',"$admin_file?adminjob=settings&admintype=member"),
	'pcache'		=> array('页面缓存',"$admin_file?adminjob=settings&admintype=pcache"),
	'index'			=> array('首页细节设置',"$admin_file?adminjob=settings&admintype=index"),
	'thread'		=> array('帖子列表页设置',"$admin_file?adminjob=settings&admintype=thread"),
	'read'			=> array('帖子阅读页设置',"$admin_file?adminjob=settings&admintype=read"),
	'jsinvoke'		=> array('JS调用设置',"$admin_file?adminjob=settings&admintype=jsinvoke"),
	'popinfo'		=> array('信息提示设置',"$admin_file?adminjob=settings&admintype=popinfo"),
	'email'			=> array('邮件设置',"$admin_file?adminjob=settings&admintype=email"),
	'wap'			=> array('WAP设置',"$admin_file?adminjob=settings&admintype=wap"),
	'urlcheck'		=> array('外链提醒',"$admin_file?adminjob=urlcheck"),
	'help'			=> array('帮助中心设置',"$admin_file?adminjob=help"),
	'sethtm'		=> array('静态目录部署',"$admin_file?adminjob=sethtm"),
	'setforum'		=> array('管理版块',"$admin_file?adminjob=setforum"),
	'uniteforum'	=> array('版块合并',"$admin_file?adminjob=uniteforum"),
	'creathtm'		=> array('生成htm',"$admin_file?adminjob=creathtm"),
	'forumsell'		=> array('版块出售记录',"$admin_file?adminjob=forumsell"),
	'singleright'	=> array('用户特殊权限',"$admin_file?adminjob=singleright"),
	'article'		=> array('帖子管理',"$admin_file?adminjob=superdel&admintype=article"),
	'message'		=> array('短消息管理',"$admin_file?adminjob=superdel&admintype=message"),
	'recycle'		=> array('回收站',"$admin_file?adminjob=recycle"),
	'draftset'		=> array('草稿箱管理',"$admin_file?adminjob=draftset"),
	'tpccheck'		=> array('帖子审核',"$admin_file?adminjob=tpccheck"),
	'report'		=> array('举报管理',"$admin_file?adminjob=report"),
	'tagset'		=> array('标签管理',"$admin_file?adminjob=tagset"),
	'pwcode'		=> array('自定义代码格式',"$admin_file?adminjob=pwcode"),
	'setform'		=> array('预设帖子格式',"$admin_file?adminjob=setform"),
	'topiccate'		=> array('分类信息',"$admin_file?adminjob=topiccate"),
	'postcate'		=> array('团购活动',"$admin_file?adminjob=postcate"),
	'setbwd'		=> array('敏感词审核',"$admin_file?adminjob=setbwd"),
	'postcache'		=> array('动作表情',"$admin_file?adminjob=postcache"),
	'level'			=> array('会员组权限',"$admin_file?adminjob=level"),
	'userstats'		=> array('成员统计',"$admin_file?adminjob=userstats"),
	'upgrade'		=> array('会员组提升方案',"$admin_file?adminjob=upgrade"),
	'editgroup'		=> array('批量添加',"$admin_file?adminjob=editgroup"),
	'uptime'		=> array('有效期设置',"$admin_file?adminjob=uptime"),
	'setuser'		=> array('会员管理',"$admin_file?adminjob=setuser"),
	'unituser'		=> array('合并会员',"$admin_file?adminjob=unituser"),
	'banuser'		=> array('会员禁言',"$admin_file?adminjob=banuser"),
	'viewban'		=> array('查看禁言会员',"$admin_file?adminjob=viewban"),
	'delmember'		=> array('删除会员',"$admin_file?adminjob=superdel&admintype=delmember"),
	'checkreg'		=> array('注册审核',"$admin_file?adminjob=usercheck&admintype=checkreg"),
	'checkemail'	=> array('邮件审核',"$admin_file?adminjob=usercheck&admintype=checkemail"),
	'customcredit'	=> array('自定义积分',"$admin_file?adminjob=customcredit"),
	'customfield'	=> array('自定义用户信息',"$admin_file?adminjob=customfield"),
	'ipban'			=> array('IP 管理',"$admin_file?adminjob=ipban"),
	'ipstates'		=> array('IP 统计',"$admin_file?adminjob=ipban&job=ipstates"),
	'ipsearch'		=> array('IP 检索',"$admin_file?adminjob=ipban&job=ipsearch"),
	'adminlog'		=> array('后台管理日志',"$admin_file?adminjob=record&admintype=adminlog"),
	'forumlog'		=> array('前台管理日志',"$admin_file?adminjob=forumlog"),
	'creditlog'		=> array('积分流通日志',"$admin_file?adminjob=creditlog"),
	'adminrecord'	=> array('后台操作记录',"$admin_file?adminjob=adminrecord"),
	'bakout'		=> array('数据备份',"$admin_file?adminjob=bakup&admintype=bakout"),
	'bakin'			=> array('数据恢复',"$admin_file?adminjob=bakup&admintype=bakin"),
	'repair'		=> array('数据修复',"$admin_file?adminjob=repair"),
	'ptable'		=> array('数据库分卷',"$admin_file?adminjob=ptable"),
	'updatecache'	=> array('缓存管理',"$admin_file?adminjob=updatecache"),
	'pwcache'		=> array('PW缓存库',"$admin_file?adminjob=pwcache"),
	'guestdir'		=> array('缓存文件清理',"$admin_file?adminjob=guestdir"),
	'chmod'			=> array('文件属性检查',"$admin_file?adminjob=chmod"),
	'safecheck'		=> array('文件安全检查',"$admin_file?adminjob=safecheck"),
	'datastate'		=> array('数据统计',"$admin_file?adminjob=datastate"),
	'viewtoday'		=> array('今日到访会员',"$admin_file?adminjob=viewtoday"),
	'hackcenter'	=> array('插件中心',"$admin_file?adminjob=hackcenter"),
	'setstyles'		=> array('风格模板',"$admin_file?adminjob=setstyles"),
	'userpay'		=> array('网上支付',"$admin_file?adminjob=userpay"),
	'orderlist'		=> array('订单管理',"$admin_file?adminjob=orderlist"),
	'announcement'	=> array('公告管理',"$admin_file?adminjob=announcement"),
	'sendmail'		=> array('邮件群发',"$admin_file?adminjob=sendmail"),
	'sendmsg'		=> array('消息群发',"$admin_file?adminjob=sendmsg"),
	'present'		=> array('节日送礼',"$admin_file?adminjob=present"),
	'setads'		=> array('宣传设置',"$admin_file?adminjob=setads"),
	'share'			=> array('友情链接',"$admin_file?adminjob=share"),
	'sitemap'		=> array('网站地图',"$admin_file?adminjob=sitemap"),
	'rebang'		=> array('首页多栏展现',"$admin_file?adminjob=rebang"),
	'plantodo'		=> array('计划任务',"$admin_file?adminjob=plantodo"),
	'modeset'		=> array('基本设置',"$admin_file?adminjob=modeset"),
	//'modestamp'		=> array('模块分类设置',"$admin_file?adminjob=modestamp"),
	//'modepush'		=> array('推送帖子管理',"$admin_file?adminjob=modepush"),
	//'area_forumtype'	=> array('版块导航设置',"$admin_file?adminjob=mode&admintype=area_forumtype"),
	//'area_indexforum'	=> array('首页版块展示',"$admin_file?adminjob=mode&admintype=area_indexforum"),
	'area_selecttpl'	=> array('模板中心',"$admin_file?adminjob=mode&admintype=area_selecttpl"),
	'area_edittpl'	=> array('本站模板管理',"$admin_file?adminjob=mode&admintype=area_edittpl"),
	'area_tplcontent'	=> array('模块内容管理',"$admin_file?adminjob=mode&admintype=area_tplcontent"),
	'area_configarea'	=> array('门户首页静态化',"$admin_file?adminjob=mode&admintype=area_configarea"),
	'o_global'		=> array('圈子核心设置',"$admin_file?adminjob=mode&admintype=o_global"),
	'rightset'		=> array('后台权限管理',"$admin_file?adminjob=rightset"),
	'manager'		=> array('创始人管理',"$admin_file?adminjob=manager"),
	'ystats'		=> array('量子恒道统计',"$admin_file?adminjob=ystats&action=config"),
	'diyoption'		=> array('常用选项定制',"$admin_file?adminjob=diyoption"),
	'msphinx'       => array('Sphinx全文索引',"$admin_file?adminjob=advanced&admintype=msphinx"),
	'mmemcache'     => array('Memcache缓存',"$admin_file?adminjob=advanced&admintype=mmemcache"),
	'advanced'     => array('高级应用配置',"$admin_file?adminjob=advanced&admintype=msphinx"),
	'optimize'		=> array('站点优化方案',"$admin_file?adminjob=optimize"),
	'maketemplate'	=> array('模块生成',"$admin_file?adminjob=maketemplate"),
	//'all'			=> array('所有选项',"$admin_file?adminjob=settings&admintype=all"),
	'navmain'		=> array('社区主导航',"$admin_file?adminjob=customnav&admintype=navmain"),
	'navside'		=> array('顶部底部导航',"$admin_file?adminjob=customnav&admintype=navside"),
	'navmode'		=> array('模式导航',"$admin_file?adminjob=customnav&admintype=navmode"),
	'setadvert'		=> array('广告管理',"$admin_file?adminjob=setadvert"),
	'postindex'		=> array('帖子索引',"$admin_file?adminjob=postindex"),
	'ucset'			=> array('用户中心设置',"$admin_file?adminjob=ucset"),
	'ucapp'			=> array('应用管理',"$admin_file?adminjob=ucapp"),
	'uccredit'		=> array('积分同步',"$admin_file?adminjob=uccredit"),
	'ucnotify'		=> array('消息队列',"$admin_file?adminjob=ucnotify"),
	'app_photos'	=> array('相册管理',"$admin_file?adminjob=apps&admintype=app_photos&action=albums"),
	'app_diary'		=> array('日志管理',"$admin_file?adminjob=apps&admintype=app_diary&action=cp"),
	'app_groups'	=> array('群组管理',"$admin_file?adminjob=apps&admintype=app_groups&action=argument"),
	'app_share'		=> array('分享管理',"$admin_file?adminjob=apps&admintype=app_share&action=share"),
	'app_write'		=> array('记录管理',"$admin_file?adminjob=apps&admintype=app_write&action=writes"),
	'app_hot'		=> array('热榜管理',"$admin_file?adminjob=apps&admintype=app_hot"),
	'app_stopic'	=> array('专题制作',"$admin_file?adminjob=apps&admintype=app_stopic"),
	'o_comments'	=> array('评论管理',"$admin_file?adminjob=mode&admintype=o_comments"),
//taolianjie
	'taolianjie'		=> array('淘链接',"$admin_file?adminjob=app&admintype=taolianjie"),
//end	
	'appslist'			=> array('基础应用',"$admin_file?adminjob=app&admintype=appslist"),
	'onlineapp_setting'	=> array('APP帐号与设置',"$admin_file?adminjob=app&admintype=appset"),
	'onlineapp_list'	=> array('社区应用',"$admin_file?adminjob=app&admintype=onlineapp"),
	'onlineapp_i9p'		=> array('随拍随发',"$admin_file?adminjob=app&admintype=i9p"),
	'onlineapp_ctid'	=> array('帖子交换',"$admin_file?adminjob=app&admintype=blooming"),
	'job'               => array('任务中心',"$admin_file?adminjob=job"),
	'overprint'         => array('主题印戳',"$admin_file?adminjob=overprint"),
	'app'               => array('APP平台帐号',"$admin_file?adminjob=app"),


);
?>