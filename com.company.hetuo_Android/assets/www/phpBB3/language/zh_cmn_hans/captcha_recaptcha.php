<?php
/**
*
* recaptcha [Chinese Simplified]
*
* @package language
* @version $Id: captcha_recaptcha.php 9933 2009-08-06 09:12:21Z mikovchain $
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
	'RECAPTCHA_LANG'				=> '简体中文',
	'RECAPTCHA_NOT_AVAILABLE'		=> '要使用 reCaptcha, 您需要先在<a href="http://recaptcha.net">reCaptcha.net</a>上注册一个账号.',
	'CAPTCHA_RECAPTCHA'				=> 'reCaptcha',
	'RECAPTCHA_INCORRECT'			=> '您输入的验证码不正确',

	'RECAPTCHA_PUBLIC'				=> 'reCaptcha 公键',
	'RECAPTCHA_PUBLIC_EXPLAIN'		=> '您的 reCaptcha 公键. 键值可以从 <a href="http://recaptcha.net">reCaptcha.net</a> 上获得.',
	'RECAPTCHA_PRIVATE'				=> 'reCaptcha 私键',
	'RECAPTCHA_PRIVATE_EXPLAIN'		=> '您的 reCaptcha 私键. 键值可以从 <a href="http://recaptcha.net">reCaptcha.net</a> 上获得.',

	'RECAPTCHA_EXPLAIN'				=> '为了证明您不是Spam机器人, 请输入下面显示的两个词.',
));

?>