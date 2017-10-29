<?php
!function_exists('readover') && exit('Forbidden');

$lang['right_title'] = array(
	'basic'		=> '基本权限',
	'read'		=> '帖子权限',
	'att'		=> '附件权限',
	'special'	=> '用户组购买',
	'system'	=> '管理权限'
);
$lang['right'] = array (
	'basic' => array(
		'allowhide' => array(
			'title'	=> '隐身登录',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowhide]" $allowhide_Y />开启</li><li><input type="radio" value="0" name="group[allowhide]" $allowhide_N />关闭</li></ul>'
		),
		'userbinding' => array(
			'title'	=> '绑定其他帐号',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[userbinding]" $userbinding_Y />开启</li><li><input type="radio" value="0" name="group[userbinding]" $userbinding_N />关闭</li></ul>'
		),
		'allowread'	=> array(
			'title'	=> '浏览帖子',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowread]" $allowread_Y />开启</li><li><input type="radio" value="0" name="group[allowread]" $allowread_N />关闭</li></ul>'
		),
		'allowsearch'	=> array(
			'title'	=> '搜索控制',
			'html'	=> '<ul class="list_A"><li><input type="radio" value="0" name="group[allowsearch]" $allowsearch_0 />不允许</li>
			<li><input type="radio" value="1" name="group[allowsearch]" $allowsearch_1 />只允许搜索主题</li>
			<li><input type="radio" value="2" name="group[allowsearch]" $allowsearch_2 />允许搜索主题和内容</li></ul>'
		),
		'allowmember'	=> array(
			'title'	=> '查看会员列表',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowmember]" $allowmember_Y />开启</li><li><input type="radio" value="0" name="group[allowmember]" $allowmember_N />关闭</li></ul>'
		),
		'allowprofile'	=> array(
			'title'	=> '查看会员资料',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowprofile]" $allowprofile_Y />开启</li><li><input type="radio" value="0" name="group[allowprofile]" $allowprofile_N />关闭</li></ul>'
		),
		'atclog' => array(
			'title'	=> '查看帖子操作记录',
			'desc'	=> '允许用户查看自己的帖子被操作情况',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[atclog]" $atclog_Y />开启</li><li><input type="radio" value="0" name="group[atclog]" $atclog_N />关闭</li></ul>'
		),
		'show' => array(
			'title'	=> '使用展区',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[show]" $show_Y />开启</li><li><input type="radio" value="0" name="group[show]" $show_N />关闭</li></ul>'
		),
		'allowreport' => array(
			'title'	=> '使用举报',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowreport]" $allowreport_Y />开启</li><li><input type="radio" value="0" name="group[allowreport]" $allowreport_N />关闭</li></ul>'
		),
		'upload' => array(
			'title'	=> '头像上传',
			'desc'	=> '此设置需先在核心设置中开启头像上传功能才有效',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[upload]" $upload_Y />开启</li><li><input type="radio" value="0" name="group[upload]" $upload_N />关闭</li></ul>'
		),
		'allowportait'	=> array(
			'title'	=> '头像链接',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowportait]" $allowportait_Y />开启</li><li><input type="radio" value="0" name="group[allowportait]" $allowportait_N />关闭</li></ul>'
		),
		'allowhonor'	=> array(
			'title'	=> '个性签名',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowhonor]" $allowhonor_Y />开启</li><li><input type="radio" value="0" name="group[allowhonor]" $allowhonor_N />关闭</li></ul>'
		),
		'allowmessege'	=> array(
			'title'	=> '发送短消息',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowmessege]" $allowmessege_Y />开启</li><li><input type="radio" value="0" name="group[allowmessege]" $allowmessege_N />关闭</li></ul>'
		),
		'allowsort'	=> array(
			'title'	=> '查看统计排行',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowsort]" $allowsort_Y />开启</li><li><input type="radio" value="0" name="group[allowsort]" $allowsort_N />关闭</li></ul>'
		),
		'alloworder'	=> array(
			'title'	=> '主题排序',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[alloworder]" $alloworder_Y />开启</li><li><input type="radio" value="0" name="group[alloworder]" $alloworder_N />关闭</li></ul>'
		),
		'viewipfrom'	=> array(
			'title'	=> '查看ip来源',
			'desc'	=> '如果站点核心设置中关闭此功能，则此项设置无效',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[viewipfrom]" $viewipfrom_Y />开启</li><li><input type="radio" value="0" name="group[viewipfrom]" $viewipfrom_N />关闭</li></ul>'
		),
		'searchtime'	=> array(
			'title'	=> '两次搜索时间间隔[秒]',
			'html'	=> '<input class="input input_wa" name="group[searchtime]" value="$searchtime" />'
		),
		'schtime' => array(
			'title'	=> '搜索发表时间范围',
			'html'	=> '<select name="group[schtime]" class="select_wa">
				<option value="all" $schtime_all>所有主题</option>
				<option value="86400" $schtime_86400>1天内的主题</option>
				<option value="172800" $schtime_172800>2天内的主题</option>
				<option value="604800" $schtime_604800>1星期内的主题</option>
				<option value="2592000" $schtime_2592000>1个月内的主题</option>
				<option value="5184000" $schtime_5184000>2个月内的主题</option>
				<option value="7776000" $schtime_7776000>3个月内的主题</option>
				<option value="15552000" $schtime_15552000>6个月内的主题</option>
				<option value="31536000" $schtime_31536000>1年内的主题</option>
			</select>'
		),
		'signnum' => array(
			'title'	=> '个人签名最大字节数',
			'desc'	=> '为0则不限制',
			'html'	=> '<input class="input input_wa" name="group[signnum]" value="$signnum" />'
		),
		'imgwidth' => array(
			'title'	=> '签名中的图片最大宽度',
			'desc'	=> '留空使用核心里的设置',
			'html'	=> '<input class="input input_wa" name="group[imgwidth]" value="$imgwidth" />'
		),
		'imgheight' => array(
			'title'	=> '签名中的图片最大高度',
			'desc'	=> '留空使用核心里的设置',
			'html'	=> '<input class="input input_wa" name="group[imgheight]" value="$imgheight" />'
		),
		'fontsize'	=> array(
			'title'	=> '签名中[size]标签最大值',
			'desc'	=> '留空使用核心里的设置',
			'html'	=> '<input class="input input_wa" name="group[fontsize]" value="$fontsize" />'
		),
		'maxmsg'	=> array(
			'title'	=> '最大短消息数目',
			'desc'	=> '最大消息数为个人消息，不包括群发消息和系统消息',
			'html'	=> '<input class="input input_wa" value="$maxmsg" name="group[maxmsg]" />'
		),
		'maxsendmsg'	=> array(
			'title'	=> '每日最大发送短消息数目',
			'html'	=> '<input class="input input_wa" value="$maxsendmsg" name="group[maxsendmsg]" />'
		),
		'msggroup'	=> array(
			'title'	=> '只接收特定用户组的消息',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[msggroup]" $msggroup_Y />开启</li><li><input type="radio" value="0" name="group[msggroup]" $msggroup_N />关闭</li></ul>'
		),
		/*'ifmemo'	=> array(
			'title'	=> '能使用便笺的功能',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[ifmemo]" $ifmemo_Y />开启</li><li><input type="radio" value="0" name="group[ifmemo]" $ifmemo_N />关闭</li></ul>'
		),*/
		'pergroup' =>	array(
			'title'	=> '查看用户组权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="checkbox" name="group[pergroup][]" value="member" $pergroup_sel[member] />会员组</li><li><input type="checkbox" name="group[pergroup][]" value="system" $pergroup_sel[system] />系统组</li><li><input type="checkbox" name="group[pergroup][]" value="special" $pergroup_sel[special] />特殊组</li></ul>'
		),
		'maxfavor'	=> array(
			'title'	=> '收藏夹容量',
			'html'	=> '<input class="input input_wa" value="$maxfavor" name="group[maxfavor]" />'
		),
		'maxgraft'	=> array(
			'title'	=> '草稿箱容量',
			'html'	=> '<input class="input input_wa" value="$maxgraft" name="group[maxgraft]" />'
		),
		'pwdlimitime'	=> array(
			'title'	=> '强制用户组密码变更[天]',
			'desc'	=> '留空则为不限制',
			'html'	=> '<input class="input input_wa" value="$pwdlimitime" name="group[pwdlimitime]" />'
		),
		/*'maxcstyles'	=> array(
			'title'	=> '自定义风格数量',
			'desc'	=> '(设置0或留空，则不允许使用自定义风格)',
			'html'	=> '<input class="input input_wa" value="$maxcstyles" name="group[maxcstyles]" />'
		)*/
	),
	'read'	=> array(
		'allowpost'	=> array(
			'title'	=> '发表主题',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowpost]" $allowpost_Y />开启</li><li><input type="radio" value="0" name="group[allowpost]" $allowpost_N />关闭</li></ul>'
		),
		'allowrp'	=> array(
			'title'	=> '回复主题',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowrp]" $allowrp_Y />开启</li><li><input type="radio" value="0" name="group[allowrp]" $allowrp_N />关闭</li></ul>'
		),
		'allownewvote'	=> array(
			'title'	=> '发起投票',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allownewvote]" $allownewvote_Y />开启</li><li><input type="radio" value="0" name="group[allownewvote]" $allownewvote_N />关闭</li></ul>'
		),
		'modifyvote'	=> array(
			'title'	=> '修改发起的投票选项',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[modifyvote]" $modifyvote_Y />开启</li><li><input type="radio" value="0" name="group[modifyvote]" $modifyvote_N />关闭</li></ul>'
		),
		'allowvote'	=> array(
			'title' => '参与投票',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowvote]" $allowvote_Y />开启</li><li><input type="radio" value="0" name="group[allowvote]" $allowvote_N />关闭</li></ul>'
		),
		'viewvote'	=> array(
			'title'	=> '查看投票用户',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[viewvote]" $viewvote_Y />开启</li><li><input type="radio" value="0" name="group[viewvote]" $viewvote_N />关闭</li></ul>'
		),
		/*'allowactive'	=> array(
			'title'	=> '发活动帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowactive]" $allowactive_Y />开启</li><li><input type="radio" value="0" name="group[allowactive]" $allowactive_N />关闭</li></ul>'
		),*/
		'allowreward'	=> array(
			'title'	=> '悬赏帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowreward]" $allowreward_Y />开启</li><li><input type="radio" value="0" name="group[allowreward]" $allowreward_N />关闭</li></ul>'
		),
		'allowgoods'	=> array(
			'title'	=> '商品帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowgoods]" $allowgoods_Y />开启</li><li><input type="radio" value="0" name="group[allowgoods]" $allowgoods_N />关闭</li></ul>'
		),
		'allowdebate'	=> array(
			'title'	=> '辩论帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowdebate]" $allowdebate_Y />开启</li><li><input type="radio" value="0" name="group[allowdebate]" $allowdebate_N />关闭</li></ul>'
		),
		'allowmodelid'	=> array(
			'title'	=> '分类信息贴',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowmodelid]" $allowmodelid_Y />开启</li><li><input type="radio" value="0" name="group[allowmodelid]" $allowmodelid_N />关闭</li></ul>'
		),
		'allowpcid'	=> array(
			'title'	=> '团购活动帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="checkbox" name="group[allowpcid][]" value="1" $allowpcid_sel[1] />团购</li><li>
			<input type="checkbox" name="group[allowpcid][]" value="2" $allowpcid_sel[2] />活动</li></ul>'
		),
		'htmlcode'	=> array(
			'title'	=> '发表html帖',
			'desc'	=> '这将使用户拥有直接编辑 html 源代码的权利!',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[htmlcode]" $htmlcode_Y />开启</li><li><input type="radio" value="0" name="group[htmlcode]" $htmlcode_N />关闭</li></ul>'
		),
		'media'	=> array(
			'title'	=> '多媒体允许自动播放',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="checkbox" name="group[media][]" value="flash" $media_sel[flash] />flash</li><li>
			<input type="checkbox" name="group[media][]" value="wmv" $media_sel[wmv] />wmv</li><li>
			<input type="checkbox" name="group[media][]" value="rm" $media_sel[rm] />rm</li><li>
			<input type="checkbox" name="group[media][]" value="mp3" $media_sel[mp3] />mp3</li></ul>'
		),
		'allowhidden'	=> array(
			'title'	=> '隐藏帖',
			'desc'	=> '注：此设置同时受版块权限限制，开启版块权限中的发表隐藏帖功能方有效',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowhidden]" $allowhidden_Y />开启</li><li><input type="radio" value="0" name="group[allowhidden]" $allowhidden_N />关闭</li></ul>'
		),
		'allowsell'	=> array(
			'title'	=> '出售帖',
			'desc'	=> '注：此设置同时受版块权限限制，开启版块权限中的发表出售帖功能方有效',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowsell]" $allowsell_Y />开启</li><li><input type="radio" value="0" name="group[allowsell]" $allowsell_N />关闭</li></ul>'
		),
		'allowencode'	=> array(
			'title'	=> '加密帖',
			'desc'	=> '注：此设置同时受版块权限限制，开启版块权限中的发表加密帖功能方有效',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowencode]" $allowencode_Y />开启</li><li><input type="radio" value="0" name="group[allowencode]" $allowencode_N />关闭</li></ul>'
		),
		'anonymous'	=> array(
			'title'	=> '匿名帖',
			'desc'	=> '注：此设置同时受版块权限限制，开启版块权限中的发表匿名帖功能方有效',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[anonymous]" $anonymous_Y />开启</li><li><input type="radio" value="0" name="group[anonymous]" $anonymous_N />关闭</li></ul>'
		),
		'dig'	=> array(
			'title'	=> '推荐帖子',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[dig]" $dig_Y />开启</li><li><input type="radio" value="0" name="group[dig]" $dig_N />关闭</li></ul>'
		),
		'leaveword'	=>	array(
			'title'	=> '楼主留言',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[leaveword]" $leaveword_Y />开启</li><li><input type="radio" value="0" name="group[leaveword]" $leaveword_N />关闭</li></ul>'
		),
		'allowdelatc'	=> array(
			'title'	=> '删除自己的帖子',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowdelatc]" $allowdelatc_Y />开启</li><li><input type="radio" value="0" name="group[allowdelatc]" $allowdelatc_N />关闭</li></ul>'
		),
		'atccheck'	=> array(
			'title'	=> '该用户组文章须审核',
			'desc'	=> '开启版块文章审核时,发表的文章是否需要管理员审核，此项只有是开启版块文章审核时有效',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[atccheck]" $atccheck_Y />开启</li><li><input type="radio" value="0" name="group[atccheck]" $atccheck_N />关闭</li></ul>'
		),
		'markable'	=> array(
			'title'	=> '评分权限',
			'desc'	=> '版主在所管理版块始终有评分权限',
			'html'	=> '<ul class="list_A cc"><li><input type="radio" value="0" name="group[markable]" $markable_0 />无</li><li>
			<input type="radio" value="1" name="group[markable]" $markable_1 />允许评分</li><li>
			<input type="radio" value="2" name="group[markable]" $markable_2 />允许重复评分</li></ul>'
		),
		/*'maxcredit'	=> array(
			'title' => '评分上限<font color=blue> 说明：</font>每天所有积分总和允许的最大评分点数',
			'html'	=> '<input type="text" class="input input_wa" value="$maxcredit" name="group[maxcredit]" />'
		),
		'marklimit' => array(
			'title'	=> '评分限制<font color=blue> 说明：</font>每次评分的最大和最小值',
			'html'	=> '最小 <input type=text size="3" class="input" value="$minper" name="group[marklimit][0]" /> 最大 <input type=text size="3" class="input" value="$maxper" name="group[marklimit][1]" />'
		),*/
		'markset'	=> array(
			'title'	=> '评分设置',
			'desc'	=> '复选框不选中或者评分上限、评分限制任何一项留空/设为0，前台将无法使用该评分类型',
			'html'	=> $credit_type
		),
		/*'markdt'	=> array(
			'title'	=> '评分是否需要扣除自身相应的积分',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[markdt]" $markdt_Y />开启</li><li><input type="radio" value="0" name="group[markdt]" $markdt_N />关闭</li></ul>'
		),*/
		'postlimit'	=> array(
			'title'	=> '每天最多发表帖子数',
			'desc'	=> '0为不限制',
			'html'	=> '<input class="input input_wa" value="$postlimit" name="group[postlimit]" />'
		),
		'postpertime'	=> array(
			'title'	=> '灌水预防',
			'desc'	=> '多少秒间隔内不能发帖,0为不限制',
			'html'	=> '<input class="input input_wa" value="$postpertime" name="group[postpertime]" />'
		),
		'edittime'	=> array(
			'title'	=> '编辑时间约束[分钟]',
			'desc'	=> '超过设定时间后拒绝用户编辑。留空或者键入0则没有约束',
			'html'	=> '<input class="input input_wa" value="$edittime" name="group[edittime]" />'
		)
	),
	'att'	=> array(
		'allowupload'	=> array(
			'title'	=> '上传附件权限',
			'desc'	=> '可在版块设置处设置上传附件“奖励或扣除”积分',
			'html'	=> '<ul class="list_A"><li><input type="radio" value="0" name="group[allowupload]" $allowupload_0 />不允许上传附件</li><li><input type="radio" value="1" name="group[allowupload]" $allowupload_1 />允许上传附件，按照版块设置奖励或扣除积分</li><li><input type="radio" value="2" name="group[allowupload]" $allowupload_2 />允许上传附件，不奖励或扣除积分</li></ul>'
		),
		'allowdownload'	=> array(
			'title'	=> '下载附件权限',
			'desc'	=> '可在版块设置处设置下载附件“奖励或扣除”积分',
			'html'	=> '<ul class="list_A"><li><input type="radio" value="0" name="group[allowdownload]" $allowdownload_0 />不允许下载附件</li><li><input type="radio" value="1" name="group[allowdownload]" $allowdownload_1 />允许下载附件，按照版块设置奖励或扣除积分</li><li><input type="radio" value="2" name="group[allowdownload]" $allowdownload_2 />允许下载附件，不奖励或扣除积分</li></ul>'
		),
		'allownum'	=> array(
			'title'	=> '一天最多上传附件个数',
			'html'	=> '<input class="input input_wa" value="$allownum" name="group[allownum]" />'
		),
		'uploadtype'	=> array(
			'title'	=> '附件上传的后缀和尺寸',
			'desc'	=> '留空使用站点核心设置中的设置',
			'html'	=> '<div class="admin_table_b"><table cellpadding="0" cellspacing="0">
				<tbody id="mode" style="display:none"><tr>
					<td><input class="input input_wc" name="filetype[]" value=""></td>
					<td><input class="input input_wc" name="maxsize[]" value=""></td><td><a href="javascript:;" onclick="removecols(this);">[删除]</a></td>
				</tr></tbody>
				<tr>
					<td>后缀名(小写)</td>
					<td>最大尺寸(KB)</td>
					<td><a href="javascript:;" class="s3" onclick="addcols(\'mode\',\'ft\');">[添加]</a></td>
				</tr>
				{$upload_type}
				<tbody id="ft"></tbody>
			</table></div>
			<script language="JavaScript">
			addcols(\'mode\',\'ft\');
			</script>'
		)
	),
	'special' => array(
		'allowbuy'	=> array(
			'title'	=> '允许购买',
			'desc'	=> "开启该功能后，需同时到<a href=\"$admin_file?adminjob=plantodo\"><font color=\"blue\">计划任务</font></a>开启“限期头衔自动回收”功能.",
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowbuy]" $allowbuy_Y />开启</li><li><input type="radio" value="0" name="group[allowbuy]" $allowbuy_N />关闭</li></ul>'
		),
		'selltype'	=> array(
			'title'	=> '特殊组购买币种',
			'html'	=> '<select name="group[selltype]" class="select_wa">$special_type</select>'
		),
		'sellprice'	=> array(
			'title'	=> '每日价格[积分]',
			'html'	=> '<input type="text" class="input input_wa" name="group[sellprice]" value="$sellprice" />'
		),
		'rmbprice'	=> array(
			'title'	=> '每日价格[现金]',
			'desc'	=> '设置此价格，将允许通过网上支付来购买',
			'html'	=> '<input type="text" class="input input_wa" name="group[rmbprice]" value="$rmbprice" />'
		),
		'selllimit'	=> array(
			'title'	=> '购买特殊组天数限制',
			'html'	=> '<input type="text" class="input input_wa" name="group[selllimit]" value="$selllimit" />'
		),
		'sellinfo'	=> array(
			'title'	=> '特殊组描述',
			'desc'	=> '可以填写购买说明和该用户组拥有的特殊权限',
			'html'	=> '<textarea name="group[sellinfo]" class="textarea">$sellinfo</textarea>'
		)
	),
	'system'	=> array(
		'allowadmincp'	=> array(
			'title'	=> '可进后台',
			'desc'	=> "具体后台权限设置需要到<a href=\"$admin_file?adminjob=rightset\">创始人-后台权限管理</a>里进行配置",
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowadmincp]" $allowadmincp_Y />开启</li><li><input type="radio" value="0" name="group[allowadmincp]" $allowadmincp_N />关闭</li></ul>'
		),
		'superright' => array(
			'title'	=> '超级管理权限',
			'desc'	=> "<font color=\"red\">是</font>：表明以下针对版块的权限设置对所有版块生效（例如：管理员）<br /><font color=\"red\">否</font>：表明以下针对版块的权限设置对所有版块无效，此时如果要配置单个版块的管理权限，需要到<a href=\"$admin_file?adminjob=singleright\">版块用户权限</a>里进行配置<br />（注：当编辑版主权限时，开启则版主在所有版块都拥有权限，关闭则版主只在本版块拥有权限）",
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[superright]" $superright_Y />开启</li><li><input type="radio" value="0" name="group[superright]" $superright_N />关闭</li></ul>'
		)
	),
	'systemforum' => array(
		'posthide'	=> array(
			'title'	=> '查看隐藏帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[posthide]" $posthide_Y />开启</li><li><input type="radio" value="0" name="group[posthide]" $posthide_N />关闭</li></ul>'
		),
		'sellhide'	=> array(
			'title'	=> '查看出售帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[sellhide]" $sellhide_Y />开启</li><li><input type="radio" value="0" name="group[sellhide]" $sellhide_N />关闭</li></ul>'
		),
		'encodehide'	=> array(
			'title'	=> '查看加密帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[encodehide]" $encodehide_Y />开启</li><li><input type="radio" value="0" name="group[encodehide]" $encodehide_N />关闭</li></ul>'
		),
		'anonyhide'	=> array(
			'title'	=> '查看匿名帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[anonyhide]" $anonyhide_Y />开启</li><li><input type="radio" value="0" name="group[anonyhide]" $anonyhide_N />关闭</li></ul>'
		),
		'postpers'	=> array(
			'title'	=> '灌水',
			'desc'	=> '不受灌水时间限制',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[postpers]" $postpers_Y />开启</li><li><input type="radio" value="0" name="group[postpers]" $postpers_N />关闭</li></ul>'
		),
		'replylock'	=> array(
			'title'	=> '回复锁定帖',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type=radio value=1 $replylock_Y name=group[replylock]>开启</li><li><input type=radio value=0 $replylock_N name=group[replylock]>关闭</li></ul>'
		),
		'viewip'	=> array(
			'title'	=> '查看IP',
			'desc'	=> '浏览帖子时显示',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[viewip]" $viewip_Y />开启</li><li><input type="radio" value="0" name="group[viewip]" $viewip_N />关闭</li></ul>'
		),
		'topped'	=> array(
			'title'	=> '置顶权限',
			'html'	=> '<ul class="list_A"><li><input type="radio" value="0" name="group[topped]" $topped_0 />无</li><li>
			<input type="radio" value="1" name="group[topped]" $topped_1 />版块置顶</li><li>
			<input type="radio" value="2" name="group[topped]" $topped_2 />版块置顶,分类置顶</li><li>
			<input type="radio" value="3" name="group[topped]" $topped_3 />版块置顶,分类置顶,总置顶</li><li>
			<input type="radio" value="4" name="group[topped]" $topped_4 />任意版块置顶</li></ul>'
		),
		'replayorder' => array(
			'title'	  => '帖子回复显示顺序',
			'html'	  => '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[replayorder]" $replayorder_Y />开启</li><li><input type="radio" value="0" name="group[replayorder]" $replayorder_N />关闭</li></ul>',
		),
		'digestadmin'	=> array(
			'title'	=> '前台精华',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[digestadmin]" $digestadmin_Y />开启</li><li><input type="radio" value="0" name="group[digestadmin]" $digestadmin_N />关闭</li></ul>'
		),
		'lockadmin'	=> array(
			'title'	=> '前台锁定',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[lockadmin]" $lockadmin_Y />开启</li><li><input type="radio" value="0" name="group[lockadmin]" $lockadmin_N />关闭</li></ul>'
		),
		'pushadmin'	=> array(
			'title'	=> '前台提前',
			'html'	=> '
			<ul class="list_A list_80 cc fl mr20"><li><input type="radio" value="1" name="group[pushadmin]" $pushadmin_Y />开启</li><li><input type="radio" value="0" name="group[pushadmin]" $pushadmin_N />关闭</li></ul>'
		),
		'pushtime'	=> array(
			'title'	=> '提前时间上限[小时]',
			'desc'	=> '留空或0表示不限制',
			'html'	=> '<input type="text" value="$pushtime" name="group[pushtime]" class="input input_wa" />'
		),
		'coloradmin'	=> array(
			'title'	=> '前台加亮',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[coloradmin]" $coloradmin_Y />开启</li><li><input type="radio" value="0" name="group[coloradmin]" $coloradmin_N />关闭</li></ul>'
		),
		'downadmin'	=> array(
			'title'	=> '前台压贴',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[downadmin]" $downadmin_Y />开启</li><li><input type="radio" value="0" name="group[downadmin]" $downadmin_N />关闭</li></ul>'
		),
		'replaytopped' => array(
			'title'    => '前台帖内置顶',
			'html'	   => '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[replaytopped]" $replaytopped_Y />开启</li><li><input type="radio" value="0" name="group[replaytopped]" $replaytopped_N />关闭</li></ul>',
		),
		'tpctype'	=> array(
			'title'	=> '主题分类管理',
			'desc'	=> '<font color=blue> 说明：</font>主题分类批量管理权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[tpctype]" $tpctype_Y />开启</li><li><input type="radio" value="0" name="group[tpctype]" $tpctype_N />关闭</li></ul>'
		),
		'tpccheck'	=> array(
			'title'	=> '主题验证管理',
			'desc'	=> '<font color=blue> 说明：</font>前台主题验证管理权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[tpccheck]" $tpccheck_Y />开启</li><li><input type="radio" value="0" name="group[tpccheck]" $tpccheck_N />关闭</li></ul>'
		),
		'delatc'	=> array(
			'title'	=> '批量删除主题',
			'desc'	=> '<font color=blue> 说明：</font>前台帖子管理权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[delatc]" $delatc_Y />开启</li><li><input type="radio" value="0" name="group[delatc]" $delatc_N />关闭</li></ul>'
		),
		'moveatc'	=> array(
			'title'	=> '批量移动帖子',
			'desc'	=> '<font color=blue> 说明：</font>前台帖子管理权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[moveatc]" $moveatc_Y />开启</li><li><input type="radio" value="0" name="group[moveatc]" $moveatc_N />关闭</li></ul>'
		),
		'copyatc'	=> array(
			'title'	=> '批量复制帖子',
			'desc'	=> '<font color=blue> 说明：</font>前台帖子管理权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[copyatc]" $copyatc_Y />开启</li><li><input type="radio" value="0" name="group[copyatc]" $copyatc_N />关闭</li></ul>'
		),
		'modother'	=> array(
			'title'	=> '删除单一帖子[包括回复]',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[modother]" $modother_Y />开启</li><li><input type="radio" value="0" name="group[modother]" $modother_N />关闭</li></ul>'
		),
		'deltpcs'	=> array(
			'title'	=> '编辑用户帖子',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[deltpcs]" $deltpcs_Y />开启</li><li><input type="radio" value="0" name="group[deltpcs]" $deltpcs_N />关闭</li></ul>'
		),
		'viewcheck'	=> array(
			'title'	=> '查看需要验证的帖子',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[viewcheck]" $viewcheck_Y />开启</li><li><input type="radio" value="0" name="group[viewcheck]" $viewcheck_N />关闭</li></ul>'
		),
		'viewclose'	=> array(
			'title'	=> '查看关闭帖子',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[viewclose]" $viewclose_Y />开启</li><li><input type="radio" value="0" name="group[viewclose]" $viewclose_N />关闭</li></ul>'
		),
		'delattach'	=> array(
			'title'	=> '删除附件',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[delattach]" $delattach_Y />开启</li><li><input type="radio" value="0" name="group[delattach]" $delattach_N />关闭</li></ul>'
		),
		'shield'	=> array(
			'title'	=> '屏蔽单一主题',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[shield]" $shield_Y />开启</li><li><input type="radio" value="0" name="group[shield]" $shield_N />关闭</li></ul>'
		),
		'unite'	=> array(
			'title'	=> '合并主题',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[unite]" $unite_Y />开启</li><li><input type="radio" value="0" name="group[unite]" $unite_N />关闭</li></ul>'
		),
		'remind'	=> array(
			'title'	=> '帖子管理提醒',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[remind]" $remind_Y />开启</li><li><input type="radio" value="0" name="group[remind]" $remind_N />关闭</li></ul>'
		),
		'pingcp'	=> array(
			'title'	=> '管理评分记录',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[pingcp]" $pingcp_Y />开启</li><li><input type="radio" value="0" name="group[pingcp]" $pingcp_N />关闭</li></ul>'
		),
		'inspect'	=> array(
			'title'	=> '版主标记已阅',
			'desc'	=> '<font color=blue> 说明：</font>需在版块基本权限里开启后方可用',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[inspect]" $inspect_Y />开启</li><li><input type="radio" value="0" name="group[inspect]" $inspect_N />关闭</li></ul>'
		),
		'allowtime'	=> array(
			'title'	=> '不受版块发帖时间域限制',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[allowtime]" $allowtime_Y />开启</li><li><input type="radio" value="0" name="group[allowtime]" $allowtime_N />关闭</li></ul>'
		),
		'banuser'	=> array(
			'title'	=> '禁言用户的权限',
			'desc'	=> '<font color="blue">说明:</font><br />
			<font color="red">无禁言权限:</font>该用户组无权限对会员进行禁言操作<br />
			<font color="red">所有版块:</font>(有禁言权限)并且被禁言会员在所有版块中都没权限发言<br />
			<font color="red">单一版块</font>(有禁言权限)并且被禁言会员在帖子所在版块没权限发言,而在其他版块中可以发言',
			'html'	=> '<ul class="list_A"><li><input type="radio" value="0" name="group[banuser]" $banuser_0 />无禁言权限</li><li>
			<input type="radio" value="1" name="group[banuser]" $banuser_1 />单一版块</li><li>
			<input type="radio" value="2" name="group[banuser]" $banuser_2 />所有版块</li></ul>'
		),
		'bantype'	=> array(
			'title'	=> '永久禁言用户',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[bantype]" $bantype_Y />开启</li><li><input type="radio" value="0" name="group[bantype]" $bantype_N />关闭</li></ul>'
		),
		'banmax'	=> array(
			'title'	=> '禁言时间限制',
			'desc'	=> '<font color=blue> 说明：</font>禁言会员的最大天数',
			'html'	=> '<input type=text class="input input_wa" value="$banmax" name="group[banmax]" />'
		),
		'areapush'	=> array(
			'title'	=> '门户推送',
			'desc'	=> '<font color=blue> 说明：</font>是否有向门户模式的首页和频道页推送帖子的权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[areapush]" $areapush_Y />开启</li><li><input type="radio" value="0" name="group[areapush]" $areapush_N />关闭</li></ul>'
		),
		'overprint'	=> array(
			'title'	=> '帖子印戳',
			'desc'	=> '<font color=blue> 说明：</font>是否有给帖子加印戳效果的权限',
			'html'	=> '<ul class="list_A list_80 cc"><li><input type="radio" value="1" name="group[overprint]" $overprint_Y />开启</li><li><input type="radio" value="0" name="group[overprint]" $overprint_N />关闭</li></ul>'
		)
	)
);
?>