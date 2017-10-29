<?php
/**
*
* captcha_qa [Chinese Simplified]
*
* @package language
* @version $Id: captcha_qa.php 9966 2009-08-12 15:12:03Z mikovchain $
* @copyright (c) 2009 phpbbchina.com
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
	'CAPTCHA_QA'				=> 'Q&amp;A CAPTCHA',
	'CONFIRM_QUESTION_EXPLAIN'	=> '请回答此问题以证明您不是Spam机器人.',
	'CONFIRM_QUESTION_WRONG'	=> '您的回答不正确.',

	'QUESTION_ANSWERS'			=> '回答',
	'ANSWERS_EXPLAIN'			=> '请输入正确答案, 多个请分行.',
	'CONFIRM_QUESTION'			=> '问题',

	'ANSWER'					=> '答案',
	'EDIT_QUESTION'				=> '编辑问题',
	'QUESTIONS'					=> '问题',
	'QUESTIONS_EXPLAIN'			=> '在注册的过程中, 用户会被问及您设定的问题. 要使用这个插件, 您在论坛默认语言下要设置至少一个问题. 这个问题必须让您的访客易于理解和回答而机器人无法理解. 请注意, 机器人可能使用Google™搜索这类工具来搜索答案. 使用较多数目的问题并且经常变更题目可以很好的避免机器人注册. 您可以设置严格校验来检查答案的标点和大小写是否一致.',
	'QUESTION_DELETED'			=> '问题已删除',
	'QUESTION_LANG'				=> '语言',
	'QUESTION_LANG_EXPLAIN'		=> '这个问题和对应的答案使用的语言.',
	'QUESTION_STRICT'			=> '严格校验',
	'QUESTION_STRICT_EXPLAIN'	=> '启用后, 大小写和空格一致的答案才能通过检查.',

	'QUESTION_TEXT'				=> '问题内容',
	'QUESTION_TEXT_EXPLAIN'		=> '在注册时问及的问题内容.',

	'QA_ERROR_MSG'				=> '请填写所有空格并输入至少一个答案.',
	'QA_LAST_QUESTION'			=> '在插件还处于激活状态时不能删除所有问题.',
));

?>