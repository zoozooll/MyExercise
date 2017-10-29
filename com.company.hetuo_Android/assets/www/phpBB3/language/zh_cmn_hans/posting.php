<?php
/** 
*
* posting [Chinese Simplified]
*
* @package language
* @version $Id: posting.php 8555 2008-05-15 14:10:11Z Kellanved $
* @copyright (c) 2007 phpbbchina.com
* @license http://opensource.org/licenses/gpl-license.php GNU Public License
*
*/

/**
* DO NOT CHANGE
*/
if (!defined('IN_PHPBB'))
{
	exit;
}

if (empty($lang) || !is_array($lang))
{
	$lang = array();
}

// DEVELOPERS PLEASE NOTE
//
// All language files should use UTF-8 as their encoding and the files must not contain a BOM.
//
// Placeholders can now contain order information, e.g. instead of
// 'Page %s of %s' you can (and should) write 'Page %1$s of %2$s', this allows
// translators to re-order the output of data while ensuring it remains correct
//
// You do not need this where single placeholders are used, e.g. 'Message %d' is fine
// equally where a string contains only two placeholders which are used to wrap text
// in a url you again do not need to specify an order e.g., 'Click %sHERE%s' is fine

$lang = array_merge($lang, array(
	'ADD_ATTACHMENT'			=> '上传附件',
	'ADD_ATTACHMENT_EXPLAIN'	=> '如果您要上传附件，请在下面填写详细信息',
	'ADD_FILE'					=> '添加文件',
	'ADD_POLL'					=> '建立投票',
	'ADD_POLL_EXPLAIN'			=> '如果您不想在话题中添加投票，请不要填写以下内容',
	'ALREADY_DELETED'			=> '对不起，此内容已经被删除。',
	'ATTACH_QUOTA_REACHED'		=> '对不起，您的附件限额已经用完了。',
	'ATTACH_SIG'				=> '添加签名（签名可以在用户控制面板修改）',

	'BBCODE_A_HELP'				=> '行内显示附件: [attachment=]filename.ext[/attachment]',
	'BBCODE_B_HELP'				=> '粗体：[b]text[/b]  (alt+b)',
	'BBCODE_C_HELP'				=> '显示代码：[code]code[/code]  (alt+c)',
	'BBCODE_E_HELP'				=> '列表：添加条目',
	'BBCODE_F_HELP'				=> '文字大小：[size=85]small text[/size]',
	'BBCODE_IS_OFF'				=> '%sBBCode%s <em>禁止</em>',
	'BBCODE_IS_ON'				=> '%sBBCode%s <em>允许</em>',
	'BBCODE_I_HELP'				=> '斜体：[i]text[/i]  (alt+i)',
	'BBCODE_L_HELP'				=> '列表：[list]text[/list]  (alt+l)',
	'BBCODE_LISTITEM_HELP'		=> '列表条目: [*]text[/*]',
	'BBCODE_O_HELP'				=> '顺序列表：[list=]text[/list]  (alt+o)',
	'BBCODE_P_HELP'				=> '插入图像：[img]http://image_url[/img]  (alt+p)',
	'BBCODE_Q_HELP'				=> '引用文字：[quote]text[/quote]  (alt+q)',
	'BBCODE_S_HELP'				=> '字体颜色：[color=red]text[/color]  提示：您也可以使用 color=#FF0000',
	'BBCODE_U_HELP'				=> '下划线：[u]text[/u]  (alt+u)',
	'BBCODE_W_HELP'				=> '插入链接：[url]http://url[/url] or [url=http://url]URL text[/url]  (alt+w)',
	'BBCODE_D_HELP'				=> 'Flash动画：[flash=width,height]http://url[/flash]  (alt+d)',
	'BUMP_ERROR'				=> '您不能在发表后这么快就推举文章。',

	'CANNOT_DELETE_REPLIED'		=> '对不起，您只能删除尚无回复的话题。',
	'CANNOT_EDIT_POST_LOCKED'	=> '这篇帖子已经被锁定，您不能编辑它。',
	'CANNOT_EDIT_TIME'			=> '您不能再编辑或删除这篇帖子',
	'CANNOT_POST_ANNOUNCE'		=> '对不起，您不能发布公告。',
	'CANNOT_POST_STICKY'		=> '对不起，您不能发布置顶帖。',
	'CHANGE_TOPIC_TO'			=> '将主题类型转换成',
	'CLOSE_TAGS'				=> '关闭标签',
	'CURRENT_TOPIC'				=> '当前主题',

	'DELETE_FILE'				=> '删除文件',
	'DELETE_MESSAGE'			=> '删除帖子',
	'DELETE_MESSAGE_CONFIRM'	=> '您确认要删除这篇帖子吗？',
	'DELETE_OWN_POSTS'			=> '对不起，您只能删除自己的帖子。',
	'DELETE_POST_CONFIRM'		=> '您确认要删除这篇帖子吗？',
	'DELETE_POST_WARN'			=> '删除的帖子将不能恢复',
	'DISABLE_BBCODE'			=> '禁止解析BBCode',
	'DISABLE_MAGIC_URL'			=> '禁止自动生成超链接',
	'DISABLE_SMILIES'			=> '禁止生成表情',
	'DISALLOWED_CONTENT'		=> '上传失败, 因为文件可能包含安全隐患.',
	'DISALLOWED_EXTENSION'		=> '扩展名 %s 是禁止的',
	'DRAFT_LOADED'				=> '草稿已经导入编辑区，您可以结束编辑您的帖子。<br />您的草稿将在帖子提交后删除。',
	'DRAFT_LOADED_PM'			=> '草稿已经导入编辑区域, 您可以现在完成您的私人短信.<br />您的草稿将在提交后删除.',
	'DRAFT_SAVED'				=> '草稿保存成功',
	'DRAFT_TITLE'				=> '草稿标题',

	'EDIT_REASON'				=> '编辑此贴的原因',
	'EMPTY_FILEUPLOAD'			=> '上传的是空文件',
	'EMPTY_MESSAGE'				=> '发贴时，您必须输入正文。',
	'EMPTY_REMOTE_DATA'			=> '文件上传失败，请尝试手动上传此文件。',

	'FLASH_IS_OFF'				=> '[flash] <em>禁止</em>',
	'FLASH_IS_ON'				=> '[flash] <em>允许</em>',
	'FLOOD_ERROR'				=> '您不能在这么短的时间内再次发表文章。',
	'FONT_COLOR'				=> '文字颜色',
	'FONT_COLOR_HIDE'			=> '隐藏字体颜色',
	'FONT_HUGE'					=> '最大',
	'FONT_LARGE'				=> '大',
	'FONT_NORMAL'				=> '普通',
	'FONT_SIZE'					=> '文字大小',
	'FONT_SMALL'				=> '小',
	'FONT_TINY'					=> '最小',

	'GENERAL_UPLOAD_ERROR'		=> '无法上传附件到 %s',

	'IMAGES_ARE_OFF'			=> '[img] <em>禁止</em>',
	'IMAGES_ARE_ON'				=> '[img] <em>允许</em>',
	'INVALID_FILENAME'			=> '%s 是无效的文件名',

	'LOAD'						=> '导入',
	'LOAD_DRAFT'				=> '导入草稿',
	'LOAD_DRAFT_EXPLAIN'		=> '在这里，您可以选择草稿继续编辑。您现在编写的帖子将被取消，所有的当前内容将会丢失。您可以在用户控制面板内查看、编辑和删除草稿。',
	'LOGIN_EXPLAIN_BUMP'		=> '您需要登录后才能在这个版面推举话题。',
	'LOGIN_EXPLAIN_DELETE'		=> '您需要登录后才能在这个版面删除帖子。',
	'LOGIN_EXPLAIN_POST'		=> '您需要登录后才能在这个版面发表文章。',
	'LOGIN_EXPLAIN_QUOTE'		=> '您需要登录后才能在这个版面引用帖子。',
	'LOGIN_EXPLAIN_REPLY'		=> '您需要登录后才能在这个版面回复帖子。',

	'MAX_FONT_SIZE_EXCEEDED'	=> '您可以使用的最大字体是 %1$d。',
	'MAX_FLASH_HEIGHT_EXCEEDED'	=> 'Flash文件的高度不允许超过 %1$d 象素。',
	'MAX_FLASH_WIDTH_EXCEEDED'	=> 'Flash文件的宽度不允许超过 %1$d 象素。',
	'MAX_IMG_HEIGHT_EXCEEDED'	=> '图片的高度不允许超过 %1$d 象素。',
	'MAX_IMG_WIDTH_EXCEEDED'	=> '图片的宽度不允许超过 %1$d 象素。',

	'MESSAGE_BODY_EXPLAIN'		=> '请在这里输入正文，注意不允许超过 <strong>%d</strong> 个字符。',
	'MESSAGE_DELETED'			=> '文章已被成功删除',
	'MORE_SMILIES'				=> '查看更多表情',

	'NOTIFY_REPLY'				=> '有人回复时，给我发送一封Email',
	'NOT_UPLOADED'				=> '文件上传失败。',
	'NO_DELETE_POLL_OPTIONS'	=> '您无法删除现有的投票选项',
	'NO_PM_ICON'				=> '没有 PM 图标',
	'NO_POLL_TITLE'				=> '您必须设置投票标题',
	'NO_POST'					=> '指定的文章不存在',
	'NO_POST_MODE'				=> '没有指定文章类型',

	'PARTIAL_UPLOAD'			=> '要上传的文件只上传了一部分',
	'PHP_SIZE_NA'				=> '要上传的附件太大了。<br />无法获知 php.ini 中设定的最大允许上传文件大小。',
	'PHP_SIZE_OVERRUN'			=> '要上传的附件太大了，php.ini 中设定的最大允许上传文件大小为 %1$d %2$s。<br />请注意这个数值是在 php.ini 内设定的，无法被覆盖。',
	'PLACE_INLINE'				=> '置入文中',
	'POLL_DELETE'				=> '删除投票',
	'POLL_FOR'					=> '投票持续时间',
	'POLL_FOR_EXPLAIN'			=> '需要永久投票请输入 0 或者留空',
	'POLL_MAX_OPTIONS'			=> '每个用户的可选数',
	'POLL_MAX_OPTIONS_EXPLAIN'	=> '这是每个用户在投票时可以选择的选项数目。',
	'POLL_OPTIONS'				=> '投票选项',
	'POLL_OPTIONS_EXPLAIN'		=> '请分别在每行输入一个选项，您最多可以设置 <strong>%d</strong> 个选项',
	'POLL_OPTIONS_EDIT_EXPLAIN'	=> '请分别在每行输入一个选项. 您最多可以设置 <strong>%d</strong> 个选项. 如果您移除或添加选项, 所有前面的投票都将被重置.',
	'POLL_QUESTION'				=> '投票问题',
	'POLL_TITLE_TOO_LONG'		=> '投票的标题不能大于100字符.',
	'POLL_TITLE_COMP_TOO_LONG'	=> '解析后的投票标题过长, 请删除一些BBCode或表情图标.',
	'POLL_VOTE_CHANGE'			=> '允许重新投票',
	'POLL_VOTE_CHANGE_EXPLAIN'	=> '如果允许，用户将可以更改他们的选择。',
	'POSTED_ATTACHMENTS'		=> '发布的附件',
	'POST_APPROVAL_NOTIFY'		=> '当您的文章通过审核时会通知您.',
	'POST_CONFIRMATION'			=> '文章确认',
	'POST_CONFIRM_EXPLAIN'		=> '为防止恶意灌水，本论坛要求您输入一组确认码。确认码显示在下面的图片中，如果您无法正常浏览这个图片请联络 %s论坛管理员%s。',
	'POST_DELETED'				=> '这篇文章已被成功删除',
	'POST_EDITED'				=> '这篇文章已被成功修改',
	'POST_EDITED_MOD'			=> '这篇文章已经被修改但是需要等待批准',
	'POST_GLOBAL'				=> '全局公告',
	'POST_ICON'					=> '帖子图标',
	'POST_NORMAL'				=> '普通',
	'POST_REVIEW'				=> '帖子预览',
	'POST_REVIEW_EDIT'			=> '文章查看',
	'POST_REVIEW_EDIT_EXPLAIN'	=> '在您编辑的过程中这篇文章已经被其他用户更改. 您可能需要查看一下新的帖子内容以便调整自己的发文.',
	'POST_REVIEW_EXPLAIN'		=> '已有至少一篇新回复在这个主题下发表了，您或许希望重新审视您的回贴。',
	'POST_STORED'				=> '文章已经成功发表',
	'POST_STORED_MOD'			=> '文章已经保存但是需要等待批准发布',
	'POST_TOPIC_AS'				=> '发表新主题为',
	'PROGRESS_BAR'				=> '进度条',

	'QUOTE_DEPTH_EXCEEDED'		=> '最多允许嵌套使用 %1$d 层引用',

	'SAVE'						=> '保存',
	'SAVE_DATE'					=> '另存为',
	'SAVE_DRAFT'				=> '保存草稿',
	'SAVE_DRAFT_CONFIRM'		=> '请注意保存的草稿仅包含标题和正文，任何其他内容都将被忽略。您希望现在保存草稿吗?',
	'SMILIES'					=> '表情',
	'SMILIES_ARE_OFF'			=> '表情 <em>禁止</em>',
	'SMILIES_ARE_ON'			=> '表情 <em>允许</em>',
	'STICKY_ANNOUNCE_TIME_LIMIT'=> '置顶/公告时间限制',
	'STICK_TOPIC_FOR'			=> '主题置顶时间',
	'STICK_TOPIC_FOR_EXPLAIN'	=> '输入 0 或留空将使主题成为永久公告或置顶',
	'STYLES_TIP'				=> '提示：点击格式选项可以将格式直接应用到选中的文字上',

	'TOO_FEW_CHARS'				=> '您输入的正文过短',
	'TOO_FEW_CHARS_LIMIT'		=> '您的正文长度为 %1$d 字符. 您的输入不能少于 %2$d 个字符.',
	'TOO_FEW_POLL_OPTIONS'		=> '您必须输入至少两个投票选项',
	'TOO_MANY_ATTACHMENTS'		=> '不能再添加更多的附件了，允许的数量是 %d 个。',
	'TOO_MANY_CHARS'			=> '您输入的正文过长',
	'TOO_MANY_CHARS_POST'		=> '您的内容包含 %1$d 字节, 而允许的最大字节数为 %2$d.',
	'TOO_MANY_CHARS_SIG'		=> '您的签名档包含 %1$d 字节, 而允许的最大字节数为 %2$d.',
	'TOO_MANY_POLL_OPTIONS'		=> '您输入了过多的投票选项',
	'TOO_MANY_SMILIES'			=> '您的内容包含了过多的表情，允许的最大表情数量是 %d。',
	'TOO_MANY_URLS'				=> '您的内容包含了过多的链接，允许的最大链接数量是 %d。',
	'TOO_MANY_USER_OPTIONS'		=> '您不能指定比当前选项数还多的每个用户可选数',
	'TOPIC_BUMPED'				=> '主题推举成功',

	'UNAUTHORISED_BBCODE'		=> '您不能使用特定的 BBCodes：%s',
	'UNGLOBALISE_EXPLAIN'		=> '要将这个主题从全局公告转换为普通主题，您需要选择一个版面放置它。',
	'UPDATE_COMMENT'			=> '更新评论',
	'URL_INVALID'				=> '您指定的链接无效。',
	'URL_NOT_FOUND'				=> '指定的文件无法找到。',
	'URL_IS_OFF'				=> '[url] <em>禁止</em>',
	'URL_IS_ON'					=> '[url] <em>允许</em>',
	'USER_CANNOT_BUMP'			=> '您不能在这个版面推举文章',
	'USER_CANNOT_DELETE'		=> '您不能在这个版面删除文章',
	'USER_CANNOT_EDIT'			=> '您不能在这个版面编辑文章',
	'USER_CANNOT_REPLY'			=> '您不能在这个版面回复文章',
	'USER_CANNOT_FORUM_POST'	=> '您不能在这个版面进行发帖操作，因为这个版面类型不支持。',

	'VIEW_MESSAGE'				=> '%s查看您提交的内容%s',
	'VIEW_PRIVATE_MESSAGE'		=> '%s查看您发送的私人短信%s',

	'WRONG_FILESIZE'			=> '文件太大了, 最大允许文件尺寸为 %1d %2s',
	'WRONG_SIZE'				=> '图片必须最小宽 %1$d 象素，高 %2$d 象素；最大宽 %3$d 象素，高 %4$d 象素。您提交的图片为：宽 %5$d 象素，高 %6$d 象素。',
));

?>