<?php
!function_exists('readover') && exit('Forbidden');

$_yes='是';
$_no='否';

$lang=array(

	'settings'=>    '是否允许进行"核心设置"',
	'updatecache'=> '是否允许进行"缓存数据管理"',
	'postcache'=>   '是否允许进行"动作表情管理"',
	'creathtm'=>    '是否允许进行"静态模板设置"',
	'credit'=>      '是否允许进行"自定义积分设置"',

	'bakout'=>      '是否允许进行"论坛数据备份"',
	'bakin'=>       '是否允许进行"论坛数据恢复"',
	'repair'=>		'是否允许进行"数据库修复"',

	'setforum'=>    '是否允许进行"论坛版块管理"',
	'uniteforum'=>	'是否允许进行"版块合并"',

	'setstyles'=>   '是否允许进行"风格模板设置"',

	'setuser'=>     '是否允许进行"会员管理"',
	'userstats'=>	'是否允许进行"用户组成员统计"',
	'upgrade'=>     '是否允许进行"会员提升方式管理"',
	'editgroup'=>	'是否允许进行"批量添加用户组"',
	'level'=>       '是否允许进行"用户组等级管理"',

	'announcement'=>'是否允许进行"发布 | 管理 公告"',
	'mailuser'=>    '是否允许进行"Email 群"',
	'sendmsg'=>     '是否允许进行"短消息群发"',
	'giveuser'=>    '是否允许进行"节日礼物赠送功能"',

	'spdelatc'=>    '是否允许进行"批量删除帖子"',
	'spdelmem'=>    '是否允许进行"批量删除用户"',
	'spdelmsg'=>    '是否允许进行"批量删除短消息"',

	'attachment'=>  '是否允许进行"附件管理"',
	'attachstats'=>'是否允许进行"附件统计"',
	'attachrenew'=>'是否允许进行"附件修复"',

	'atccheck'=>    '是否允许进行"主题验证管理"',

	'banuser'=>     '是否允许进行"会员禁言"',
	'viewban'=>		'是否允许进行"查看禁言会员"',
	'ipban'=>       '是否允许进行"IP 禁止"',
	'setbwd'=>      '是否允许进行"不良词语过滤"',

	'adminlog'=>    '是否允许进行"后台管理安全日志"',
	'forumlog'=>    '是否允许进行"前台管理安全日志"',
	'creditlog'=>	'是否允许进行"评分管理日志"',
	'userlog'=>		'是否允许进行"删除用户日志"',

	'setads'=>      '是否允许进行" 论坛宣传设置"',
	'share'=>       '是否允许进行"友情链接管理"',
	'viewtody'=>    '是否允许进行"查看今日到访会员"',
);
?>