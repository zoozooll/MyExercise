<?php
!function_exists('readover') && exit('Forbidden');

$lang['_othermsg'] = '\n\n[b]文章：[/b][url=$GLOBALS[db_bbsurl]/read.php?tid={$L[other][tid]}]{$L[other][subject]}[/url]\n'
					. '[b]发表日期：[/b]{$L[other][postdate]}\n'
					. '[b]所在版块：[/b][url=$GLOBALS[db_bbsurl]/thread.php?fid={$L[other][fid]}]{$L[other][forum]}[/url]\n'
					. '[b]操作时间：[/b]{$L[other][admindate]}\n'
					. '[b]操作理由：[/b]{$L[other][reason]}\n\n'
					. '论坛管理操作通知短消息，对本次管理操作有任何异议，请与我取得联系。';
$lang['_othermsg1'] = '\n\n[b]文章：[/b][url=$GLOBALS[db_bbsurl]/job.php?action=topost&tid={$L[other][tid]}&pid={$L[other][pid]}]{$L[other][subject]}[/url]\n'
					. '[b]发表日期：[/b]{$L[other][postdate]}\n'
					. '[b]所在版块：[/b][url=$GLOBALS[db_bbsurl]/thread.php?fid={$L[other][fid]}]{$L[other][forum]}[/url]\n'
					. '[b]操作时间：[/b]{$L[other][admindate]}\n'
					. '[b]操作理由：[/b]{$L[other][reason]}\n\n'
					. '论坛管理操作通知短消息，对本次管理操作有任何异议，请与我取得联系。';

$lang['writemsg'] = array (
	'olpay_title'			=> '积分充值支付成功.',
	'olpay_content'			=> '交易币充值支付成功，您需要登录支付宝使用“[color=red]确认收货[/color]”功能完成本次交易。\n'
								. '确认收货后系统会自动对您的交易币帐户进行充值。',
	'olpay_content_2'		=> '积分充值成功，本次充值金额：{$L[other][number]}RMB，总共获得{$L[other][cname]}个数：{$L[other][currency]}。\n谢谢使用！',

	'toolbuy_title'			=> '道具购买支付成功!',
	'toolbuy_content'		=> '道具购买成功，本次支付金额：{$L[other][fee]}RMB，共购得[b]{$L[other][toolname]}[/b]道具 [b]{$L[other][number]}[/b] 个!',

	'forumbuy_title'		=> '版块访问权限购买支付成功!',
	'forumbuy_content'		=> '版块访问权限购买成功，本次支付金额：{$L[other][fee]}RMB，共购得版块 [b]{$L[other][fname]}[/b] 访问期限 [b]{$L[other][number]}[/b] 天!',

	'groupbuy_title'		=> '特殊组购买支付成功!',
	'groupbuy_content'		=> '特殊组身份购买成功，本次支付金额：{$L[other][fee]}RMB，共购得用户组 [b]{$L[other][gname]}[/b] 身份 [b]{$L[other][number]}[/b] 天!',

	'virement_title'		=> '银行汇款通知!!',
	'virement_content'		=> '用户{$L[other][windid]}通过银行给你转帐{$L[other][to_money]}元钱，'
								. '系统自动把以前的利息加到你的存款中，你的利息将从现在重新开始计算\n转帐附言：{$L[other][memo]}',
	'metal_add'				=> '授予勋章通知',
	'metal_add_content'		=> '您被授予勋章\n\n勋章名称：{$L[other][mname]}\n操作：{$L[other][windid]}\n理由：{$L[other][reason]}',
	'metal_cancel'			=> '收回勋章通知',
	'metal_cancel_content'	=> '您的勋章被收回\n\n勋章名称：{$L[other][mname]}\n操作：{$L[other][windid]}\n理由：{$L[other][reason]}',
	'metal_cancel_text'		=> '您的勋章被收回\n\n勋章名称：{$L[other][medalname]}\n操作：SYSTEM\n理由：过期',
	'metal_refuse'			=> '勋章申请未通过',
	'metal_refuse_content'	=> '您的勋章申请未通过审核\n\n勋章名称：{$L[other][mname]}\n操作：{$L[other][windid]}\n理由：{$L[other][reason]}',
	'medal_apply_title'		=> '勋章申请通知!',
	'medal_apply_content'	=> '用户 {$L[other][username]} 于 {$L[other][time]} 申请了 {$L[other][medal]} 勋章，请求您审核。',
	'vire_title'			=> '交易币转帐通知',
	'vire_content'			=> '用户 [b]{$L[other][windid]}[/b] 使用积分转帐功能，给您转帐 {$L[other][paynum]} {$L[other][cname]}，请注意查收。',
	'cyvire_title'			=> '{$L[other][cn_name]}{$L[other][moneyname]}转帐通知',
	'cyvire_content'		=> '{$L[other][cn_name]}([url=$GLOBALS[db_bbsurl]/hack.php?'
								. 'H_name=colony&cyid={$L[other][cyid]}&job=view&id={$L[other][cyid]}]'
								. '{$L[other][all_cname]}[/url])管理员使用{$L[other][moneyname]}管理功能，'
								. '给你转帐 {$L[other][currency]} {$L[other][moneyname]}，请注意查收。',
	'donate_title'			=> '{$L[other][cn_name]}捐献通知消息',
	'donate_content'		=> '用户{$L[other][windid]}通过捐献功能，给{$L[other][cn_name]}({$L[other][allcname]})'
								. '捐献{$L[other][moneyname]}：{$L[other][sendmoney]}。',

	'top_title'				=> '您的文章被置顶.',
	'untop_title'			=> '您的文章被解除置顶.',
	'top_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]置顶[/b] 操作'.$lang['_othermsg'],
	'untop_content'			=> '您的文章被 [b]{$L[other][manager]}[/b] 执行 [b]解除置顶[/b] 操作'.$lang['_othermsg'],
	'digest_title'			=> '您的文章被设为精华帖',
	'digest_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]精华[/b] 操作\n\n'
								. '对您的影响：{$L[other][affect]}'.$lang['_othermsg'],
	'undigest_title'		=> '您的文章被取消精华',
	'undigest_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]取消精华[/b] 操作\n\n'
								. '对您的影响：{$L[other][affect]}'.$lang['_othermsg'],
	'lock_title'			=> '您的文章被锁定',
	'lock_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]锁定[/b] 操作'.$lang['_othermsg'],
	'unlock_title'			=> '您的文章被解除锁定',
	'unlock_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]解除锁定[/b] 操作'.$lang['_othermsg'],
	'push_title'			=> '您的文章被提前',
	'push_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]提前[/b] 操作'.$lang['_othermsg'],
	'pushto_title'			=> '您的文章被推送',
	'pushto_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]推送[/b] 操作'.$lang['_othermsg'],
	'unhighlight_title'		=> '您的文章标题被取消加亮显示',
	'unhighlight_content'	=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]标题取消加亮[/b] 操作'.$lang['_othermsg'],
	'highlight_title'		=> '您的文章标题被加亮显示',
	'highlight_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]标题加亮[/b] 操作'.$lang['_othermsg'],
	'del_title'				=> '您的文章被删除',
	'del_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]删除[/b] 操作\n\n'
								. '对您的影响：{$L[other][affect]}'.$lang['_othermsg'],
	'move_title'			=> '您的文章被移动',
	'move_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]移动[/b] 操作\n\n'
								. '[b]目的版块：[/b][url=$GLOBALS[db_bbsurl]/thread.php?fid={$L[other][tofid]}]{$L[other][toforum]}[/url]'.$lang['_othermsg'],
	'copy_title'			=> '您的文章被复制到新版块',
	'copy_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]复制[/b] 操作\n\n'
								. '[b]目的版块：[/b][url=$GLOBALS[db_bbsurl]/thread.php?fid={$L[other][tofid]}]{$L[other][toforum]}[/url]'.$lang['_othermsg'],
	'ping_title'			=> '您的文章被评分',
	'ping_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]评分[/b] 操作\n\n'
								. '对您的影响：{$L[other][affect]}'.$lang['_othermsg1'],
	'delping_title'			=> '您的文章被取消评分',
	'delping_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]取消评分[/b] 操作\n\n'
								. '对您的影响：{$L[other][affect]}'.$lang['_othermsg1'],
	'deltpc_title'			=> '您的文章被删除',
	'deltpc_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]删除[/b] 操作\n\n'
								. '对您的影响：{$L[other][affect]}'.$lang['_othermsg'],
	'delrp_title'			=> '您的回复被删除',
	'delrp_content'			=> '您发表的回复被 [b]{$L[other][manager]}[/b] 执行 [b]删除[/b] 操作\n\n'
								. '对您的影响：{$L[other][affect]}'.$lang['_othermsg'],
	'reward_title_1'		=> '您的回复被设为最佳答案!',
	'reward_content_1'		=> '您的回复被设为最佳答案!\n\n对您的影响：{$L[other][affect]}'.$lang['_othermsg'],
	'reward_title_2'		=> '您的回复获得热心助人奖励!',
	'reward_content_2'		=> '您的回复获得热心助人奖励!\n\n对您的影响：{$L[other][affect]}'.$lang['_othermsg'],
	'endreward_title_1'		=> '您的悬赏被取消!',
	'endreward_title_2'		=> '您的悬赏被强制结案!',
	'endreward_content_1'	=> '由于没有合适的答案，您的悬赏被管理员 [b]{$L[other][manager]}[/b] 执行 [b]取消[/b] 操作!\n\n'
								. '系统返回您:{$L[other][affect]}'.$lang['_othermsg'],
	'endreward_content_2'	=> '由于您长时间未对悬赏帖进行结案,且已经有合适的答案，所以被[b]{$L[other][manager]}[/b] 执行 [b]强制结案[/b] 操作\n\n'
								. '系统不返回所有积分'.$lang['_othermsg'],
	'rewardmsg_title'		=> '悬赏帖(编号:{$L[other][tid]})请求处理!',
	'rewardmsg_content'		=> '尊敬的版主:\n\t\t您好!\n\t\t由于该次悬赏帖没有产生合适答案，现请求您结案,'
								. '希望您仔细查证后，作出公平处理!'.$lang['_othermsg'],
	'rewardmsg_notice_title'	=> '悬赏帖到期通知!',
	'rewardmsg_notice_content'	=> '您的悬赏帖已经到期，系统提醒您尽快作出处理,否则版主有权强行结案!\n\n'
								. '[b]文章：[/b][url=$GLOBALS[db_bbsurl]/read.php?tid={$L[tid]}]{$L[subject]}[/url]\n'
								. '[b]发表日期：[/b]{$L[postdate]}\n'
								. '[b]所在版块：[/b][url=$GLOBALS[db_bbsurl]/thread.php?fid={$L[fid]}]{$L[name]}[/url]\n\n'
								. '论坛管理操作通知短消息，对本次管理操作有任何异议，请与我取得联系。',
	'shield_title_2'		=> '您的文章主题被删除',
	'shield_content_2'		=> '您发表的文章主题被 [b]{$L[other][manager]}[/b] [b]删除[/b]'.$lang['_othermsg'],
	'shield_title_1'		=> '您的文章被屏蔽',
	'shield_content_1'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] [b]屏蔽[/b]'.$lang['_othermsg'],
	'shield_title_0'		=> '您的文章被取消屏蔽',
	'shield_content_0'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]取消屏蔽[/b] 操作'.$lang['_othermsg'],
	'remind_title'			=> '您的文章被提醒管理',
	'remind_content'		=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]提醒管理[/b] 操作'.$lang['_othermsg'],
	'report_title'			=> '帖子报告处理',
	'report_content_1_1'	=> '该帖很优秀,建议加为精华帖!'.$lang['_othermsg'],
	'report_content_1_0'	=> '该帖很优秀,建议加为精华帖!'.$lang['_othermsg1'],
	'report_content_0_1'	=> '该帖包含不良信息,请及时处理!'.$lang['_othermsg'],
	'report_content_0_0'	=> '该帖包含不良信息,请及时处理!'.$lang['_othermsg1'],
	'unite_title'			=> '您的文章被合并',
	'unite_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]合并[/b] 操作'.$lang['_othermsg'],
	'leaveword_title'		=> '您的文章被留言',
	'leaveword_content'		=> '[b]{$L[other][author]}[/b] 在您发表的文章上留言了。'.$lang['_othermsg'],
	'birth_title'			=> '$L[toUser],祝您生日快乐',
	'birth_content'			=> '[img]$GLOBALS[imgpath]/wind/birth.jpg[/img]\r\n'
								. '祈愿您的生日，为您带来一个最瑰丽最金碧辉煌的一生。\r\n'
								. '只希望你的每一天都快乐、健康、美丽，生命需要奋斗、创造、把握！\r\n'
								. '生日的烛光中摇曳一季繁花，每一支都是我的祝愿：生日快乐！\r\n\r\n'
								. '--------------------------------------- {$L[fromUser]} 送上最真挚的祝福！\r\n\r\n',
	'down_title'			=> '您的文章被执行压帖操作',
	'down_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] [b]压后了 {$L[other][timelimit]} 小时[/b]'.$lang['_othermsg'],
	'change_type_title'		=> '您的文章被修改了主题分类',
	'change_type_content'	=> '您发表的文章主题被 [b]{$L[other][manager]}[/b] [b]修改了主题分类为：{$L[other][type]}[/b]'.$lang['_othermsg'],
	'check_title'			=> '您的文章已通过审核',
	'check_content'			=> '您发表的文章主题被 [b]{$L[other][manager]}[/b] [b]通过审核[/b]'.$lang['_othermsg'],

	'advert_buy_title'		=> '您的广告位申请已通过审核',
	'advert_buy_content'	=> '本广告位的价格为：{$L[other][creditnum]} {$L[other][creditypename]} 每天\n\n'
								. '你购买的天数为：{$L[other][days]}',
	'advert_apply_title'	=> '广告出租位申请通知!',
	'advert_apply_content'	=> '用户 {$L[other][username]} 于 {$L[other][time]} 申请了 {$L[other][days]} 天的广告展示，请求您审核。',

	'friend_add_title_1'	=> '好友系统通知：{$L[other][username]}将您列入他（她）的好友名单',
	'friend_add_content_1'	=> '[url=$GLOBALS[db_bbsurl]/u.php?action=show&uid={$L[other][uid]}]{$L[other][username]}[/url] 将您列入他（她）的好友名单。',
	'friend_add_title_2'	=> '好友系统通知：{$L[other][username]} 请求加您为好友',
	'friend_add_content_2'	=> '[url={$GLOBALS[db_bbsurl]}/u.php?action=show&uid={$L[other][uid]}]{$L[other][username]}[/url] 请求加您为好友。'
								. '([url={$GLOBALS[db_bbsurl]}/u.php?action=show&uid={$L[other][uid]}]查看好友[/url] '
								. '[url={$GLOBALS[db_bbsurl]}/u.php?action=friend&o=check&fuid={$L[other][uid]}]验证好友[/url])',
	'friend_delete_title'	=> '好友系统通知：{$L[other][username]} 解除与您的好友关系',
	'friend_delete_content'	=> '[url={$GLOBALS[db_bbsurl]}/u.php?action=show&uid={$L[other][uid]}]{$L[other][username]}[/url] 解除与您的好友关系。',
	'friend_accept_title'	=> '好友系统通知：{$L[username]} 通过了您的好友请求',
	'friend_accept_content' => '[url={$GLOBALS[db_bbsurl]}/u.php?action=show&uid={$L[uid]}]{$L[username]}[/url] 通过了您的好友请求。',
	'friend_acceptadd_title'=> '好友系统通知：{$L[username]} 通过了您的好友请求,并加您为好友',
	'friend_acceptadd_content'	=> '[url={$GLOBALS[db_bbsurl]}/u.php?action=show&uid={$L[uid]}]{$L[username]}[/url] 通过了您的好友请求,并加您为好友。',

	'friend_refuse_title'	=> '好友系统通知：{$L[username]} 拒绝了您的好友请求',
	'friend_refuse_content'	=> '[url={$GLOBALS[db_bbsurl]}/u.php?action=show&uid={$L[uid]}]{$L[username]}[/url]'
								. '拒绝了您的好友请求\r\n\r\n[b]拒绝理由：[/b]{$L[msg]}\r\n\r\n',
	'banuser_title'			=> '系统禁言通知',
	'banuser_content_1'		=> '你已经被管理员{$L[other][manager]} 禁言,禁言时间为{$L[other][limit]} 天\n\n{$L[other][reason]}',
	'banuser_content_2'		=> '你已经被管理员{$L[other][manager]} 禁言\n\n{$L[other][reason]}',

	'onlinepay_logistics'	=> '物流公司：{$L[logistics]}\r\n物流单号：{$L[orderid]}',
	'goods_pay_title'		=> '买家付款通知!',
	'goods_pay_content'		=> '买家 [b]{$L[other][buyer]}[/b] 于 {$L[other][buydate]} 下单的商品 [b][url={$GLOBALS[db_bbsurl]}/read.php?tid={$L[other][tid]}]{$L[other][goodsname]}[/url][/b] 已经付款，付款信息如下：\r\n\r\n{$L[other][descrip]}\r\n\r\n请确认后，尽快发货!',
	'goods_send_title'		=> '卖家发货通知!',
	'goods_send_content'	=> '您于 {$L[other][buydate]} 购买的商品[b][url={$GLOBALS[db_bbsurl]}/read.php?tid={$L[other][tid]}]{$L[other][goodsname]}[/url][/b]，卖家 [b]{$L[other][seller]}[/b] 已经发货，发货信息为：\r\n\r\n{$L[other][descrip]}',

	'sharelink_apply_title'		=> '自助友情链接申请通知!',
	'sharelink_apply_content'	=> '用户 {$L[other][username]} 于 {$L[other][time]} 申请了友情链接展示，请求您审核。',

	'o_addadmin_title'		=> '群组通知[{$L[other][cname]}]：您被升为管理员了!',
	'o_addadmin_content'	=> '您加入的群组[url={$L[other][curl]}]{$L[other][cname]}[/url]，已经将您升为管理员了，赶快去看看!',
	'o_deladmin_title'		=> '群组通知[{$L[other][cname]}]：您被取消管理员身份了!',
	'o_deladmin_content'	=> '您加入的群组[url={$L[other][curl]}]{$L[other][cname]}[/url]，已经将您取消管理员身份了，赶快去看看!',
	'o_check_title'			=> '群组通知[{$L[other][cname]}]：您已正式加入群组了!',
	'o_check_content'		=> '您日前申请加入的群组[url={$L[other][curl]}]{$L[other][cname]}[/url]，已经正式批准您加入了，赶快去看看!',
	'o_del_title'			=> '群组通知[{$L[other][cname]}]：您被踢出该群了!',
	'o_del_content'			=> '您加入的群组[url={$L[other][curl]}]{$L[other][cname]}[/url]，已经将您踢出该群了!',

	'o_friend_success_title'	=> '好友系统通知：您和{$L[other][username]}成为了好友',
	'o_friend_success_cotent'	=> '通过邀请好友：您和[url={$L[other][myurl]}]{$L[other][username]}[/url]成为了好友',

	'o_board_success_title'		=> '{$L[other][formname]}在您的留言板上留了言',
	'o_board_success_cotent'	=> '[url={$GLOBALS[db_bbsurl]}/u.php?uid={$L[other][formuid]}]{$L[other][formname]}[/url]在您的留言板上留了言。\n\n[url={$GLOBALS[db_bbsurl]}/mode.php?m=o&q=user&u={$L[other][touid]}]我的主页[/url]',
	'inspect_title'				=> '你的主题已被版主阅读',
	'inspect_content'			=> '您发表的文章被 [b]{$L[other][manager]}[/b] 执行 [b]已阅[/b] 操作\r\n[b]文章标题：[/b]<a target="_blank" href="read.php?tid={$L[other][tid]}">{$L[other][subject]}</a>\r\n[b]操作日期：[/b]{$L[other][postdate]}\r\n[b]操作理由：[/b]{$L[other][reason]}',
	'report_deal_title'			=> '您举报的内容被已被管理员处理',
	'report_deal_content'		=> '你举报的内容已被管理员 [b]{$L[other][manager]}[/b]处理\r\n [b]类型：[/b]{$L[other][type]}\r\n[b]操作日期：[/b]{$L[other][admindate]}\r\n[b]您的举报理由：[/b]{$L[other][reason]}\r\n[b]链接地址：[/b][url={$L[other][url]}]进入[/url]',
	'birth_title'				=> '今天是你的生日',
	'birth_content'				=> '生日快乐，稍上我的祝福，祝你开心每一天！',
);
?>