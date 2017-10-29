<?php
/*
  $Id: banner_manager.php,v 1.17 2002/08/18 18:54:47 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '广告管理器');

define('TABLE_HEADING_BANNERS', '广告');
define('TABLE_HEADING_GROUPS', '群组');
define('TABLE_HEADING_STATISTICS', '显示次数 / 点击次数');
define('TABLE_HEADING_STATUS', '状态');
define('TABLE_HEADING_ACTION', '操作');

define('TEXT_BANNERS_TITLE', '广告名：');
define('TEXT_BANNERS_URL', '广告URL：');
define('TEXT_BANNERS_GROUP', '广告群组：');
define('TEXT_BANNERS_NEW_GROUP', '，或在下面输入一个新的广告群组');
define('TEXT_BANNERS_IMAGE', '图片：');
define('TEXT_BANNERS_IMAGE_LOCAL', '，或在下面输入本地文件');
define('TEXT_BANNERS_IMAGE_TARGET', '将图片存至：');
define('TEXT_BANNERS_HTML_TEXT', 'HTML 文本：');
define('TEXT_BANNERS_EXPIRES_ON', '结束日期：');
define('TEXT_BANNERS_OR_AT', '，或者');
define('TEXT_BANNERS_IMPRESSIONS', '浏览');
define('TEXT_BANNERS_SCHEDULED_AT', '开始日期：');
define('TEXT_BANNERS_BANNER_NOTE', '<b>广告说明：</b><ul><li>图片和HTML文本不能同时使用。</li><li>HTML文本比图片有优先权</li></ul>');
define('TEXT_BANNERS_INSERT_NOTE', '<b>图片说明：</b><ul><li>上载文件目录必须允许用户写入！</li><li>如果不需将图片上载到服务器，比如使用本地（服务器端）图片，则不要填 \'将图片存至\' 栏。</li><li>\'将图片存至\' 栏内容必须是一个已存在文件目录，并以斜杠结尾。例如，banners/。</li></ul>');
define('TEXT_BANNERS_EXPIRCY_NOTE', '<b>结束日期说明：</b><ul><li>日期和浏览次数只填一项</li><li>如广告不需自动停止，则不填此栏</li></ul>');
define('TEXT_BANNERS_SCHEDULE_NOTE', '<b>开始日期说明：</b><ul><li>如设定了开始日期，则广告将在该日自动开始显示。</li><li>广告在设定的开始日期前不显示，之后将被标为显示中。</li></ul>');

define('TEXT_BANNERS_DATE_ADDED', '创建日期：');
define('TEXT_BANNERS_SCHEDULED_AT_DATE', '开始于：<b>%s</b>');
define('TEXT_BANNERS_EXPIRES_AT_DATE', '结束于：<b>%s</b>');
define('TEXT_BANNERS_EXPIRES_AT_IMPRESSIONS', '结束于：<b>%s</b> 浏览');
define('TEXT_BANNERS_STATUS_CHANGE', '状态变化：%s');

define('TEXT_BANNERS_DATA', '数<br>据');
define('TEXT_BANNERS_LAST_3_DAYS', '前3天');
define('TEXT_BANNERS_BANNER_VIEWS', '广告浏览');
define('TEXT_BANNERS_BANNER_CLICKS', '广告点击');

define('TEXT_INFO_DELETE_INTRO', '你确定要删除这个广告吗？');
define('TEXT_INFO_DELETE_IMAGE', '删除广告图片');

define('SUCCESS_BANNER_INSERTED', '完成：广告已插入。');
define('SUCCESS_BANNER_UPDATED', '完成：广告已更新。');
define('SUCCESS_BANNER_REMOVED', '完成：广告已删除。');
define('SUCCESS_BANNER_STATUS_UPDATED', '完成：广告状态已更新。');

define('ERROR_BANNER_TITLE_REQUIRED', '错误：广告名必须填。');
define('ERROR_BANNER_GROUP_REQUIRED', '错误：广告群组必须填。');
define('ERROR_IMAGE_DIRECTORY_DOES_NOT_EXIST', '错误：目标文件目录不存在：%s');
define('ERROR_IMAGE_DIRECTORY_NOT_WRITEABLE', '错误：目标文件目录无法写入：%s');
define('ERROR_IMAGE_DOES_NOT_EXIST', '错误：图片不存在');
define('ERROR_IMAGE_IS_NOT_WRITEABLE', '错误：图片无法删除');
define('ERROR_UNKNOWN_STATUS_FLAG', '错误：不明状态标志');

define('ERROR_GRAPHS_DIRECTORY_DOES_NOT_EXIST', '错误：Graphs 目录不存在。请在 \'images\' 目录下创建一个 \'graphs\' 目录。');
define('ERROR_GRAPHS_DIRECTORY_NOT_WRITEABLE', '错误：Graphs 目录无法写入。');
?>