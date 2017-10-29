<?php
!function_exists('readover') && exit('Forbidden');

$lang['cpmsg'] = array (

'check_error'				=> '认证码错误',
'undefine_action'			=> '您没有权限进行此项操作或此功能未完成',
'login_out'					=> '成 功 退 出 管 理<br><br><a href=index.php target=_blank>进 入 首 页</a>',
'operate_error'				=> '没有选择操作对象',
'operate_success'			=> '完成相应操作',
'operate_fail'				=> '操作失败，请检查数据完整性',
'upgrade_error'				=> '无此提升方式',
'forumid_error'				=> '版块ID错误,请重试！',
'noenough_condition'		=> '没有提供足够的条件',
'manager_right'				=> '只有创始人才能管理和编辑管理员帐号',
'admin_right'				=> '只有管理员才能管理和编辑总版主或版主的帐号',
'forum_right'				=> '您无权对此版块的帖子进行操作',
'password_confirm'			=> '两次输入密码不一致，请重新输入',
'username_exists'			=> '该用户名已经被注册了,请返回重新填写.',
'illegal_username'			=> '用户名太长或包含不可接受字符',
'illegal_password'			=> '密码包含不可接受字符',
'illegal_customimg'			=> '非法自定义头像：必须以 http 开头，不可包含此字符\'|\'，必须在(0-185)*(0-200)的大小范围里',
'illegal_email'				=> 'email格式错误',
'illegal_fid'				=> '非法版块ID',
'forum_havesub'				=> '该版块含有子版块，请先转移所有子版块，再进行此操作',
'forum_descrip'				=> '版块描述字节数不得大于 255',
'recycle_del'				=> '不能删除回收站，取消回收站功能请到核心设置里取消此功能。',
'user_not_exists'			=> '用户‘{$GLOBALS[errorname]}’不存在',
'log_min'					=> '管理日志少于100不允许删除!!',
'log_del'					=> '已删除多余的管理日志',
'adminrecord_del'			=> '已删除多余的管理记录',
'adminrecord_min'			=> '管理记录少于100不允许删除!!',
'have_banned'				=> '用户‘{$GLOBALS[username]}’已经被禁言,要解除禁言请到会员禁言处设置',
'ban_error'					=> '禁言失败，{$GLOBALS[username]}不为会员组，只能禁言会员组',
'ban_limit'					=> '请输入禁言时间',
'not_banned'				=> '用户‘{$GLOBALS[username]}’没有被禁言',
'credit_error'				=> '无效积分ID',
'login_error'				=> '用户名密码或安全问题错误,您还可以尝试{$GLOBALS[L_left]}次',
'login_fail'				=> '已经连续 $GLOBALS[F_count] 次进行无效登录<br>您将在 20 分钟内无法进行任何后台操作<br>还剩余 $GLOBALS[L_T] 秒',
'sql_config'				=> '配置文件sql_config.php不存在<br />请重新上传此文件',
'managerinfo_error'			=> '创始人账号信息错误<br />请检查配置文件sql_config.php',
'installfile_exists'		=> 'install.php 文件仍然在您的服务器上，请马上利用 FTP 来将其删除！！ 当您删除之后，刷新本页面重新进入管理中心。',
'hack_error'				=> '未安装此插件或此插件无后台设置!',
'viewban_free'				=> '完成解除禁言',
'updatecache_step'			=> '正在更新{$GLOBALS[start]}到{$GLOBALS[next]}项',
'updatecache_autostep'		=> '正在更新缓存,请耐心等待!',
'updatecache_step1'         => '当前版块<font color=red>{$GLOBALS[forumname]}</font>更新已完成，现在转入下个版块继续操作!',
'updatecache_null'			=> '当前缓存无需更新!',
'updatecache_total_step'	=> '本次更新共{$GLOBALS[total]}步，正在更新第{$GLOBALS[step]}步!',
'newpic_not_needupdate'		=> '最新图片缓存由前台发图片帖时更新，后台不作更新',
'unite_type'				=> '合并操作只能在版块之间进行(不包括分类和回收站)',
'unite_same'				=> '源论坛和目标论坛不能相同',
'template_noforum'			=> '目前还没有使用静态模板功能的版块，请先设置需要使用静态模版功能的版块',
'template_error'			=> '该版块没有启用静态模板功能，请先启用',
'setuser_forumadmin'		=> '设置或取消会员的版主权限，请到<font color="red">论坛版块管理</font>处设置',
'setuser_ban'				=> '禁言用户和解除禁言请到会员禁言处设置',
'setuser_empty'				=> '用户名,密码或email为空',
'setuser_img'				=> '头像网址必须以 \'http\' 开头.',
'setting_http'				=> '使用跨台图片链必须以http开头',
'icp_http'				    => 'ICP 备案信息链接地址必须以 \'http\' 开头',
'bbsurl_http'				=> '论坛地址必须以 \'http\' 开头',
'config_777'				=> '<font color=red>无法修改论坛核心,请将 data/bbscache/config.php 文件属性设为可写模式(777)</font>',
'dbreg_777'					=> '<font color=red>无法修改用户注册功能,请将 data/bbscache/dbreg.php 文件属性设为可写模式(777)</font>',
'setting_777_pic'			=> '<font color=red>无法更改图片目录名：该目录属性不可写或欲修改的目录名称已存在。</font>',
'setting_777_att'			=> '<font color=red>无法更改附件目录名：该目录属性不可写或欲修改的目录名称已存在。</font>',
'setting_777_htm'			=> '<font color=red>无法更改静态html文件目录名：该目录属性不可写或欲修改的目录名称已存在。</font>',
'setting_direrror'			=> '<font color=red>图片或附件目录{$GLOBALS[errordir]}不存在</font>',
'setting_recycle_type'		=> '不能将分类设置为回收站',
'setting_recycle_error'		=> '设置的回收站版块ID不存在，请先建立一个版块，再把回收站ID设为新建版块的ID',
'setting_gd_error'          => '图片水印功能开启失败: PHP编译时需要同时支持gd jpeg freetype',
'setting_rigfile_error'     => '注册文件名格式错误，请已.php结尾并且文件名中不要出现特殊字符',
'setting_adminfile_error'   => '后台登录文件名格式错误，请已.php结尾并且文件名中不要出现特殊字符',
'db_empty_tables'			=> '请选择要操作的表',

'style_add_success'			=> '添加风格完成,请速到images目录下建立{$GLOBALS[setting][0]}并放上相应的图片',
'style_exists'				=> '此名称已存在，请另选名称',
'style_empty'				=> '名称不能为空',
'style_not_exists'			=> '此风格不存在',
'style_del_error'			=> '不能删除默认风格,请先更换默认风格',
'style_777'					=> '请将此文件(data/style/{$GLOBALS[tplpath]}_css.htm)属性设置为 777 可写模式',
'illegal_sid'				=> '风格名称中含有非法字符\'_\'',
'setforum_empty'			=> '版块名称为空，请填写',
'setforum_cms'				=> 'CMS分类的上级分类或子分类只能是CMS分类',
'setforum_fupsame'			=> '不能将所属上级分类设为自己.',
'sendmsg_step'				=> '正在发送,一共要发送 $GLOBALS[count] 个用户，目前已经发送了 $GLOBALS[havesend] 个用户<br /><center><input type="button" value="暂停发送" class="btn" onClick="javascript:window.location=\'$GLOBALS[basename]\'"/></center>',
'sendmsg_success'			=> '发送完成:一共发送了{$GLOBALS[count]}个用户',
'sendmsg_empty'				=> '主题和内容不能为空！！',
'sendmsg_nolog'				=> '没有邮件群发的任务记录',
'postcache_emmpty'			=> '动作或表情内容为空，请完整填写',
'level_del'					=> '不能删除默认组',
'level_credit_error'		=> '<div style="text-align:left;margin-left:50px;"><font color="orange">评分设置错误：</font>'
								. '<br />1. 最大评分点数不能小于每次评分的最大和最小值的绝对值'
								. '<br />2. 每次评分的最大值不能小于每次评分的最小值!</div>',
'hackcenter_empty'			=> '插件信息不完整，请重新填写！',
'hackcenter_sign_exists'	=> '插件标识符{$GLOBALS[hackdir]}已经存在，请另外选择标识符',
'hackcentre_upload'         => '找不到插件文件,请先上传文件!',
'hackcentre_file_writable'  => '安装失败,请设置插件data目录777属性!',
'hackcenter_del'			=> '卸载失败，插件不存在或已经被卸载',
'hackcenter_del_fail'       => '插件卸载完成删除以下文件夹失败，请手动删除<br> hack 目录下的 {$GLOBALS[id]} 目录',
'hackcenter_del_success'	=> '插件卸载完成删除以下文件失败，请手动删除<br>{$GLOBALS[faildelfile]}',
'bakup_in'					=> '正在导入第{$GLOBALS[i]}卷备份文件，程序将自动导入余下备份文件...',
'bakup_out'					=> '已全部备份,备份文件保存在data目录下，备份文件为<br>$GLOBALS[bakfile]',
'bakup_step'				=> '正在备份数据库表 $GLOBALS[t_name]: 共 $GLOBALS[rows] 条记录，已经备份至 $GLOBALS[c_n] 条记录<br>'
								. '<br>已生成 $GLOBALS[f_num] 个备份文件，程序将自动备份余下部分',
'attachstats_del'			=> '共删除{$GLOBALS[count]}条记录，{$GLOBALS[delnum]}个附件<br>已删除:<br>$GLOBALS[delname]',
'attach_renew'				=> '附件修复完成',
'attach_renew_wait'				=> '附件修复中，请稍候',
'annouce_right'				=> '您没有权限编辑该公告',
'annouce_title'				=> '公告标题不能为空',
'annouce_content'			=> '公告链接或内容不能为空',
'annouce_fid'				=> '请选择公告发布的范围',
'annouce_time'				=> '结束时间必须大于起始日期',
'annouce_empty'				=> '标题和内容为空，请完整填写',
'annouce_all'				=> '您没有发表<b>论坛公告</b>的权限',
'annouce_category'			=> '您没有发表<b>分类公告</b>的权限',
'annouce_forum'				=> '您没有发表<b>版块公告</b>的权限',
'annouce_date'				=> '您设置的时间有误，起始时间或结束时间应大于上一次添加的公告',
'bankset_rate_error'		=> '转换率设置错误，应为大零的整数',
'bankset_rate_bug'          => '<font color="red">积分I到积分积分II</font> 的转换比例应该小于或等于 '
								. '<font color="red">积分I到积分积分II</font> 的转换比例',
'bankset_exists'			=> '{$GLOBALS[credittype][$credit1[0]]}与{$GLOBALS[credittype][$credit1[1]]}之间的转化已经存在',
'bankset_save'				=> '同种积分之间不能转换',
'forum_name'				=> '版块名称字节数不得大于 50',
'session_error'				=> '登录后台失败, 请先登录服务器设置 data/bbscache 目录属性为可写(777模式)',
'htm_error'					=> '分类：“{$GLOBALS[f_name]}” 没有启用静态模板功能，请先启用',
'atc_error'					=> '主题或内容为空',
'no_cmscate'				=> '目前还没有设置文章分类,请先到 '
								. '<a href="$GLOBALS[db_adminfile]?adminjob=c_forum"><font color="red">文章分类管理</font></a> 中设置文章分类',
'ipsearch_username'			=> '请输入要搜索的用户名.',
'ipsearch_userip'			=> '请输入要搜索的IP',
'attach_step'				=> '已经搜索了 $GLOBALS[num] 个文件, 程序在自动搜索更多的结果,请耐心等待...',
'attach_success'			=> '已经完成搜索,正在进入搜索结果列表.',
'attach_delfile'			=> '附件删除成功.',
'forum_not_exists'			=> '版块不存在。',
'forum_hidden'				=> '将版块设置为隐藏版块，需要同时设置允许‘浏览版块’的用户组',
'numerics_checkfailed'		=> '您提交的数据中包含非法数据,请返回重新操作.',
'record_aminonly'			=> '只有管理员才能删除日志记录',
'member_only'				=> '批量添加用户组功能只允许对普通用户进行操作。',
'chiefadmin_right'			=> '只有论坛创始人和管理员能添加总版主。',
'word_error'				=> '您提交的内容中含有‘&lt;iframe’‘&lt;script’‘&lt;meta’等系统禁用的HTML标签，请联系论坛创始人解决。',
'msg_managerright'			=> '您没有权限查看或删除创始人的短消息！',
'msg_adminright'			=> '您没有权限查看或删除管理员的短消息！',
'colonyset_empty'			=> '分类名称不能为空',
'colonyset_notfind'			=> '此分类不存在',
'colonyset_same'			=> '该分类名已经存在，请另外选择一个分类名。',
'colonyset_addsuccess'		=> '分类添加成功。',
'colonyset_delname'			=> '请输入要删除的{$GLOBALS[cn_name]}名称。',
'colonyset_noclass'			=> '您要删除的{$GLOBALS[cn_name]}不存在。',
'colonyset_delsuccess'		=> '{$GLOBALS[cn_name]}删除成功。',
'currrate_error'			=> '<font color="red">{$GLOBALS[db_currencyname]}到论坛积分</font> 的转换比例'
								. '要小于或等于 <font color="red">论坛积分到{$GLOBALS[db_currencyname]}</font> 的转换比例',
'cecode_error'				=> '访问域名出错，请使用合法的后台管理域名访问该页面，如果疑问请联系论坛创始人',
'unituser_newname_error'	=> '目标用户不存在，请检查您输入是目标用户是否正确！',
'unituser_username_error'	=> '原用户ID ‘{$GLOBALS[value]}’ 不存在，请检查您输入是原用户ID是否正确！',
'unituser_username_empty'	=> '原用户ID不能为空！',
'unituser_newname_empty'	=> '目标用户ID不能为空！',
'unituser_samename'			=> '原用户ID不能和目标用户ID相同',
'delattach_step'			=> '正在删除论坛冗余附件，已经删除 $GLOBALS[deltotal] 个附件，程序将自动完成整个过程，请耐心等待...',
'ip_ban'					=> '系统限制了允许进入后台的IP，您的IP无权访问该页面。',
'module_id_error'			=> '模块ID错误，该模块不存在！',
'module_adderror'			=> '调用变量名和模块标题不能为空',
'advert_code_error'			=> '广告代码不能为空或超过1024字符',
'advert_code_hire_error'	=> '广告代码类型不支持出租',
'advert_txt_error'			=> '文字内容和文字链接不能为空',
'advert_txt_hire_error'		=> '文字内容不能为空',
'advert_img_error'			=> '图片地址和图片链接不能为空',
'advert_img_hire_error'		=> '图片地址不能为空',
'advert_flash_error'		=> 'flash 链接不能为空',
'advert_float_error'		=> '横幅广告不支持javascript代码',
'advert_price_error'		=> '请设置广告位的价格',
'advert_descrip'			=> '请填写广告描述!',
'advert_id_error'			=> '广告ID为空!',
'advert_days_error'			=> '该用户的申请天数有误!',
'advert_creditype_error'	=> '本广告位积分类型有误!',
'advert_creditype_lack'		=> '本广告位积分类型有误!',
'advert_time_error'			=> '开始时间不能大于结束时间!',
'attachrenew_forbidden'		=> '您的网站使用了远程附件功能，附件修复功能失效。',
'fieldid_error'				=> '您要编辑栏目ID错误。',
'options_error'				=> '请填写选项内容',
'no_module_fid'				=> '没有选择分类',
'same_varname'				=> '该模块标识符已经存在，请使用其它模块标识符。',
'please_settime'			=> '请到计划任务里设置相应的时间，才能正常使用该功能!',
'uptime_has'				=> '该用户已经设置了系统组有效期限!',
'help_empty'				=> '标题和内容不能为空，请完整填写!',
'help_title'				=> '标题已经存在',
'hup_error1'				=> '不能将项目本身设置为自己的所属项目',
'hup_error2'				=> '不能将所属项目设置为自己的子项目',
'gid_same'					=> '限期头衔和到期变成头衔不能相同!',
'right_set'					=> '该用户权限已经设置!',
'descrip_long'				=> '版块介绍超过限定字符，请删掉一些!',
'groups_empty'				=> '该用户没有可用的用户组可以设置!',
'illegal_request'			=> '非法请求，请返回重试!',
'undefined_action'			=> '非法操作，请返回重试!',
'bbs_open'					=> '由于数据操作较大，请先关闭论坛后，继续操作，'
								. '<a href=$GLOBALS[admin_file]?adminjob=settings&admintype=basic target=blank style="color:blue">进入“核心设置-常规设置”</a>！',
'table_change'				=> '数据转移中,请稍侯...<br />正在处理第 $GLOBALS[tstart] 到 $GLOBALS[end] 条帖子!',
'table_same'				=> '请选择不同的数据表进行操作!',
'only_numeric'				=> '请输入表下标,只允许数字!',
'table_exists'				=> '该表名已存在，请输入其他的下表数字!',
'reg_timelimit'				=> '会员注册年限设置错误，为了您系统的稳定，设置区间请不要大于 150!',
'reg_username_limit'		=> '用户名长度错误：最小值不能小于1，最小值不能比最大值大。',
'reg_password_limit'		=> '密码长度错误：最小值不能小于1，最小值不能比最大值大。',
'smile_path_error'			=> '未指定路径或者该图片路径不存在',
'smile_name_error'			=> '表情组名称不能为空',
'smile_rename'				=> '名称重复',
'delete_recycle'			=> '正在清理回收站数据，请稍侯...',
'all_file_ok'				=> '没有发现可疑文件',
'safefiles_not_exists'		=> '校验文件不存在!',
'safecv_prompt'				=> '系统强制您所在的用户组必须设置安全问题，请先到<a href="profile.php?action=modify"><b>控制面板</b></a>'
								. '设置安全问题后再进行其他操作',
'manager_error'				=> '<font color="red">如果您确认需要修改创始人信息，请将 data/sql_config.php 文件设置可写模式(777)</font>',
'update_home'				=> '正在更新缓存，请稍侯...',
'setform_empty'				=>  '标题或帖子格式内容为空!',
'deltable_error1'			=> '您要删除的数据库表不存在',
'deltable_error2'			=> '您要删除的数据库表中含有数据，请先将数据转移到其他表再进行删除操作！',
'delptable_error'			=> '不能删除“<b>当前回复表</b>”，请先修改其他表为“<b>当前回复表</b>”再进行删除操作！',
'home_save'					=> '布局调整成功!',
'home_addads'				=> '操作成功\treload',
'home_add_error'			=> '该标志符已存在',
'upload_error'				=> '文件上传失败',
'upload_type_error'			=> '文件类型错误',
'bakout_words'				=> '词语导出成功!',
'bakout_grouptitle'			=> '用户组头衔名称导出成功!',
'credit_isnum'				=> '积分点数必须为数字',
'sendemail_failed'			=> '连接邮件服务器失败，请检查：<br />1、服务器地址和端口是否设置正确！ 2、网络是否通畅！',
'no_payemail'				=> '请填写付款帐号',
'adcode_error'				=> '请输入正确的通用密钥',
'localhost_error'			=> '本地服务器不支持该操作',

'search_word_limit'			=> '关键字长度不足2个字符，请重新输入',
'search_cate'				=> '不能搜索分类',
'no_condition'				=> '请输入搜索条件.',
'search_none'				=> '没有查找匹配的内容',
'ipsearch_force'			=> '由于该功能具有一定的负载性，会员超过 10万，帖子超过30万的站点禁止使用该功能',
'guestdir_delete'			=> '正在删除游客缓存文件，已删除{$GLOBALS[delnum]}个!',
'gusetdir_not_exists'		=> '游客缓存目录不存在，请到“核心设置-页面缓存”里进行设置!',
'fcache_delete'				=> '正在删除主题列表缓存文件，已删除{$GLOBALS[delnum]}个!',
'msglog_delete_step'		=> '正在删除短消息记录，已删除{$GLOBALS[delnum]}条!',

'ystat_active_account'		=> '量子恒道统计账号未激活,第一次开启量子恒道统计时账号将被自动激活',
'ystat_ymail_error'			=> '已经绑定量子恒道帐号,请勿重复绑定',
'ystat_ymail_format'		=> '邮箱格式出错<br />请确认你的输入是 yahoo.com.cn或yahoo.cn 邮箱的完整地址<br />'
								. '邮箱规则：4至32个字符(包括字母、数字、下划线)，且必须以英文字母开始',
'ystat_date_error'			=> '请选择正确时段,时段要小于当前时间',
'ystat_wrong_request_parameter'	=> '数据请求参数验证错误',
'ystat_invalid_mail_account'=> '要绑定的邮箱账号非法',
'ystat_inner_db_error'		=> '量子恒道统计内部数据库错误',
'ystat_add_user_failed'		=> '添加量子恒道统计用户失败',
'ystat_account_overload'	=> '要绑定的账号已经下属了30个网站（每个账号最多30个）',
'ystat_site_already_exist'	=> '该站点已经被绑定',
'ystat_uid_key_unmatch'		=> '量子恒道统计非法账号',
'ystat_xmldata_error'		=> '无法连接量子恒道统计服务器,请确认你的服务器可以连接外网',

'mode_domain_error'			=> '域名格式有误，域名字符只支持26位英文字符和数字和‘.’',
'mode_no_directory'			=> '该模式目录不存在，请确认该模式目录和安装配置文件相对应',
'mode_no_info'				=> '该模式的安装配置文件不存在',
'mode_have_noopen'			=> '论坛未使用特殊模式，请使用了之后再进行本操作',
'mode_page_noconfig'		=> '本页面无需配置',
'mode_page_no_need_config'	=> '本页面没有标签需要设置',
'stamp_namestamp_must'		=> '名称和唯一标识符不能为空',
'stamp_namestamp_toolang'	=> '名称和唯一标识符字节数不能超过30',
'stamp_have_exist'			=> '该名称的分类已存在，请重新修改名称',
'block_namefunction_must'	=> '名称和调用的函数不能为空',
'block_lenth_toolang'		=> '请控制名词和调用函数的字节数在30之内',
'mode_nofile'				=> '请选择文件',
'mode_fileexterror'			=> '文件格式有误，请确认配置文件是否正确',
'mode_import_error'			=> '您导入的文件不是本模式的配置',
'stamp_system_lock'			=> '本分类为系统内置模块，无法操作',
'block_system_lock'			=> '本模块为系统内置模块，无法操作',
'mode_default_config_nexist'=> '默认配置不存在，可以将您现有的配置文件放置到本mode/{$GLOBALS[db_mode]}文件夹下，作为默认配置',
'mode_template_not_exist'	=> '模板文件不存在',

'forumadmin_already'		=> '该用户已经是本版块的版主！',
'rightset_empty'			=> '权限设置为空',
'rightset_setgroup'			=> '请选择你要操作的用户组',
'rightset_delgroup'			=> "您确认删除此用户组的后台权限？",
'manager_empty'				=> '用户名或密码为空。',
'manager_had'				=> '用户名已存在。',
'manager_errorusername'		=> '用户名带有非法字符，请重新输入。',
'manager_errorpassword'		=> '密码带有非法字符，请重新输入。',
'manager_delusername'		=> "您确认删除此创始人吗？",
'manager_only'				=> '请至少保留一位创始人帐号!',
'beyond_sql_max_size'		=> '主题分类的字节数超出数据库字段最大值',
'user_not_right'			=> '该用户不是系统组成员，不能配置个人权限!',
'forum_category_err'		=> '不可查询分类，请选择相应版块',
'category_add_success'		=> '版块分类添加成功！',
'fup_empty'					=> '父版块不存在或父版块为最低等级版块！',
'singleright_nodata'		=> '没有要查询的数据！',

'settings_regfile_dfnotfind'	=> '找不到默认注册文件，请下载升级包，上传“register.php”文件到论坛根目录。',
'settings_regfile_error'		=> '注册文件名格式错误，请检查文件名格式，以“.php”结尾并以字母或数字或组合。',
'settings_adminfile_dfnotfind'	=> '找不到默认后台登录文件，请下载升级包，上传“admin.php”文件到论坛根目录。',
'settings_adminfile_error'		=> '后台登录文件名格式错误，请检查文件名格式，以“.php”结尾并以字母或数字或组合。',
'settings_picdir_dfnotfind'		=> '找不到默认图片目录，请下载升级包，上传“images”目录到论坛根目录。',
'settings_picdir_error'			=> '图片目录格式错误，请检查目录名以字母或数字或组合。',
'settings_attdir_dfnotfind'		=> '找不到默认附件目录，请下载升级包，上传“attachment”目录到论坛根目录。',
'settings_attdir_error'			=> '附件目录格式错误，请检查目录名以字母或数字或组合。',
'settings_htmdir_dfnotfind'		=> '找不到默认静态HTML文件目录，请下载升级包，上传“htm_data”目录到论坛根目录。',
'settings_htmdir_error'			=> '静态HTML文件目录格式错误，请检查目录名以字母或数字或组合。',
'settings_stopicdir_dfnotfind'	=> '找不到默认专题目录，请手动创建$GLOBALS[default_stopicdir]目录。',
'settings_stopicdir_error'		=> '专题目录格式错误，请检查目录名以字母或数字或组合。',

'adminrecord_open'				=> '未开启管理员操作记录功能，请在“创始人”->“创始人管理”处开启',
'email_success' 				=> '邮件发送成功!',
'watermark_error' 				=> '无法生成水印预览，检查设置',
'athumb_error' 					=> '无法生成缩略预览，检查设置',
'orderlist_none'				=> '您查询的订单号不存在!',

'nav_empty_title'				=> '标题内容不能为空！',
'nav_empty_link'				=> '链接不能为空！',
'album_not_exist'				=> '相册不存在！',
'no_album_selid'				=> '未选择相册！',
'no_photos'						=> '没有搜索到图片！',
'comment_not_exist'				=> '没有搜索到评论内容！',
'write_not_exist'				=> '没有搜索到记录内容！',
'share_not_exist'				=> '没有搜索到分享内容！',
'no_share_selid'				=> '未选择分享！',
'no_write_selid'				=> '未选择记录！',
'main_nav_empty'				=> '主导航名称不能为空！',
'no_nav'						=> '该导航不存在！',
'pelase_write_nav'				=> '请填写导航信息！',
'system_nav'					=> '该导航为系统内置导航，不能删除！',
'invokename_empty'				=> '请填写模块名称',
'invokename_toolang'			=> '模块名称长度过长，请保持在50个字节之内',
'invokename_repeat'				=> '该模块名已被使用，请使用其他模块名',
'invokename_error'				=> '模块名只允许使用中文和数字及英文',
'no_invoke_in_this_page'		=> '本页面没有可自定义内容的标签模块',
//7.3.2 End
'diyoption_maxlength'			=> '常用功能定制最多只能定制15条操作',
'advert_time_end'				=> '您购买的广告天数超过广告位到期时间',
'advert_ckey_error'				=> '广告标示符添加有误，只能为字母、数字、下划线和点号的组合',
'safecheck_operate_error'		=> '没有输入关键字或没有选择文件夹',
'advert_ckey_noexist'			=> '广告位不存在',
'code_appid_error'				=> '您好，您在APP平台未绑定贵站信息，请立即<a href="http://www.phpwind.com/login.php" target="_blank">注册或绑定</a>！',
'nav_hassub'					=> '该导航下的子导航不为空,不能被移动',
'author_nofind'					=> '没有找到相匹配的帖子作者,请仔细检查',
//7.5RC end
'topic_name'					=> '主题名称不能为空或大小不能超过14字节',
'model_not_none'				=> '必须启用一个或一个以上',
'illegal_cateid_or_modelid'		=> '分类信息或者主题模板不存在',
'samefieldname'					=> '字段{$GLOBALS[samefieldname]}的字段名称已存在',
'mode_o_hot_sortIsInt'			=> '您输入了错误的排序序号',
'mode_o_hot_itemIsInt'			=> '您输入了错误的显示条数',
'postcate_not_exists'			=> '该主题不存在，请确认！',
'topic_search_none'				=> '至少输入一个搜索条件',
'search_keyword_empty'		    => '查找的关键字找不到帖子',
'mode_admin_error'				=> '本模式不存在，或者本模式没有后台管理',
'topic_name_exist'				=> '主题名称已经存在',

'uc_server_set'					=> '该功能是用户中心服务端设置选项！',
'uc_cname_empty'				=> '请选择同步积分!',
'uc_syncredit_is_exists'		=> '“{$GLOBALS[credittype][$GLOBALS[cid]]}” 已经是同步积分，不能重复设置!',
'uc_appcredit_is_exists'		=> '应用“{$GLOBALS[appName]}”的积分“{$GLOBALS[cName]}”已经是同步积分!',

'time_error'					=> '请填写计划任务时间!',
'topiccate_copyfield_none'		=> '请选择要导入的模板字段!',

//7.5 sp1 end
'localhost_error'				=> '本地无法操作此功能',
'app_site_alipay_error'			=> '支付宝实名认证未绑定',
'app_register_error'			=> '通信失败，没有绑定成功，请查看<a href="http://faq.phpwind.net/answer-595">如何解决？</a>',
'tpl_string_error'				=> '提交的模板数据非法，不支持字符：EOT;,<<<,?&gt;,&lt;?,&lt;!--#,#--&gt;',

/****************APP增加语言****************/
    'filtermsg_thread_title'		=> '【敏感词】您发表的帖子含有敏感词!',
	'filtermsg_thread_content'		=> '您发表的帖子：<a style="color:blue;" href="read.php?tid={$L[tid]}">{$L[subject]}</a>，包含敏感词，此帖子现已禁止浏览，审核通过后才可正常浏览。\n\n'
									. '<a style="color:blue;" href="post.php?action=modify&fid={$L[fid]}&tid={$L[tid]}&pid=tpc&article=0">修改帖子。</a>\n',
    'filtermsg_post_title'			=> '【敏感词】您表发的回复含有敏感词!',
	'filtermsg_post_content'		=> '您在帖子：<a style="color:blue;" href="read.php?tid={$L[tid]}">{$L[subject]}</a>的回复内容里包含敏感词，此回复现已禁止浏览，审核通过后才可正常浏览。\n\n'
									. '<a style="color:blue;" href="post.php?action=modify&fid={$L[fid]}&tid={$L[tid]}&pid={$L[pid]}&article=1">修改回复。</a>\n',
	'filtermsg_thread_pass_title'	=> '【敏感词】您表发的帖子已经通过审核!',
	'filtermsg_thread_pass_content'	=> '您发布的帖子：{$L[subject]}，已经通过审核。\n\n',
	'filtermsg_thread_del_title'	=> '【敏感词】您表发的帖子因包含敏感内容被管理员删除!',
	'filtermsg_thread_del_content'	=> '您发布的帖子：{$L[subject]}，因包含敏感内容被管理员删除。\n\n',
	'filtermsg_post_pass_title'		=> '【敏感词】您表发的回复已经通过审核!',
	'filtermsg_post_pass_content'	=> '您发表于：{$L[subject]}的回复，已经通过审核。\n\n',
	'filtermsg_post_del_title'		=> '【敏感词】您表发的回复因包含敏感内容被管理员删除!',
	'filtermsg_post_del_content'	=> '您发表于：{$L[subject]}的回复，因包含敏感内容被管理员删除。\n\n',
	'filtermsg_not_update'			=> '没有需要更新的敏感词',
	'filtermsg_post_already_delete' => '该回复已经被删除',
	'filtermsg_cannot'  			=> '敏感词不能为空',
	'filtermsg_scanfinish' 			=> '扫描完毕！',
	'filter_forum_notcontent' 		=> '该版块无内容！',
	'filter_class_open'				=> '开启此分类',
	'filter_class_close'			=> '关闭此分类',
	'filter_class_nonentity'		=> '未分类',
	'filter_word_repeat'			=> '以下敏感词重复：{$L[prompt]}。 请编辑后重新提交！',
	'filter_class_repeat'			=> '已有重复分类！',
	'filter_repeat'					=> '已有重复敏感词！',
	'filter_all_word'				=> '全部词库',
	'filter_class_cannot_delete'	=> '该分类无法删除！',
	'filter_class_state'			=> '只能对分类词库进行开启/关闭操作',
	'filter_class_show_open'		=> '开启',
	'filter_class_show_close'		=> '停用',
	'filter_setting_not_word'		=> '您还未设置敏感词,无法扫描！',
	'filter_increase_threads'		=> '没有新增的帖子需要扫描！',
	'filter_operate_not'			=> '未审核',
	'filter_operate_pass'			=> '通过审核',
	'filter_operate_del'			=> '已删除',
	'filter_operate_replace'		=> '自动替换',
	'filter_scan_type_thread'		=> '主题帖',
	'filter_scan_type_post'			=> '回复帖',
	'filter_word_level_forbidden'	=> '禁用',
	'filter_word_level_checked'		=> '审核',
	'filter_word_level_replace'		=> '替换',
	'filter_switch_open'			=> '开启后此分类下的词库将生效。',
	'filter_switch_close'			=> '关闭后此分类下的词库将失效。',
	'filter_import_error'			=> '词库导入失败。',
	'filter_keyword'				=> '敏感词关键字',
	'filter_class_len'				=> '分类长度超过限制！',
	'filter_upload_dict_file'		=> '请上传词库文件！',
	/*****************APP增加语言********************/
);
?>