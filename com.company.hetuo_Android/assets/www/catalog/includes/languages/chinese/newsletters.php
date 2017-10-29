<?php
/*
  $Id: newsletters.php,v 1.5 2002/03/08 22:10:08 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '电子新闻管理器');

define('TABLE_HEADING_NEWSLETTERS', '电子新闻');
define('TABLE_HEADING_SIZE', '文件大小');
define('TABLE_HEADING_MODULE', '模版');
define('TABLE_HEADING_SENT', '已发出');
define('TABLE_HEADING_STATUS', '状态');
define('TABLE_HEADING_ACTION', '操作');

define('TEXT_NEWSLETTER_MODULE', '模版：');
define('TEXT_NEWSLETTER_TITLE', '电子新闻标题：');
define('TEXT_NEWSLETTER_CONTENT', '内容：');

define('TEXT_NEWSLETTER_DATE_ADDED', '创建日期：');
define('TEXT_NEWSLETTER_DATE_SENT', '发布日期：');

define('TEXT_INFO_DELETE_INTRO', '你确定要删除这条电子新闻吗？');

define('TEXT_PLEASE_WAIT', '请稍等 .. 正在发送电子邮件 ..<br><br>请勿中断！');
define('TEXT_FINISHED_SENDING_EMAILS', '电子邮件已发出！');

define('ERROR_NEWSLETTER_TITLE', '错误：电子新闻无标题');
define('ERROR_NEWSLETTER_MODULE', '错误：请指定电子新闻模版');
define('ERROR_REMOVE_UNLOCKED_NEWSLETTER', '错误：请在删除前锁住该条电子新闻。');
define('ERROR_EDIT_UNLOCKED_NEWSLETTER', '错误：请在修改前锁住该条电子新闻。');
define('ERROR_SEND_UNLOCKED_NEWSLETTER', '错误：请在发布前锁住该条电子新闻。');
?>