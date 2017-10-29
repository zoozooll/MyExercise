<?php
/*
  $Id: currencies.php,v 1.10 2002/01/12 17:20:32 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '货币');

define('TABLE_HEADING_CURRENCY_NAME', '货币');
define('TABLE_HEADING_CURRENCY_CODES', '代码');
define('TABLE_HEADING_CURRENCY_VALUE', '价值');
define('TABLE_HEADING_ACTION', '操作');

define('TEXT_INFO_EDIT_INTRO', '请作必要的修改');
define('TEXT_INFO_CURRENCY_TITLE', '名称：');
define('TEXT_INFO_CURRENCY_CODE', '代码：');
define('TEXT_INFO_CURRENCY_SYMBOL_LEFT', '左侧符号：');
define('TEXT_INFO_CURRENCY_SYMBOL_RIGHT', '右侧符号：');
define('TEXT_INFO_CURRENCY_DECIMAL_POINT', '小数点符号：');
define('TEXT_INFO_CURRENCY_THOUSANDS_POINT', '千位符号：');
define('TEXT_INFO_CURRENCY_DECIMAL_PLACES', '小数位数：');
define('TEXT_INFO_CURRENCY_LAST_UPDATED', '上次修改时间：');
define('TEXT_INFO_CURRENCY_VALUE', '币值：');
define('TEXT_INFO_CURRENCY_EXAMPLE', '样本：');
define('TEXT_INFO_INSERT_INTRO', '请输入新货币及相关数据');
define('TEXT_INFO_DELETE_INTRO', '你确定要删除这个货币吗？');
define('TEXT_INFO_HEADING_NEW_CURRENCY', '新货币');
define('TEXT_INFO_HEADING_EDIT_CURRENCY', '修改货币');
define('TEXT_INFO_HEADING_DELETE_CURRENCY', '删除货币');
define('TEXT_INFO_SET_AS_DEFAULT', TEXT_SET_DEFAULT . ' (需要人工更新币值)');
define('TEXT_INFO_CURRENCY_UPDATED', '%s (%s) 的汇率已经由 %s 刷新。');

define('ERROR_REMOVE_DEFAULT_CURRENCY', '错误：不能删除预设货币。请将别的货币定为预设，然后再试。');
define('ERROR_CURRENCY_INVALID', '错误：%s (%s) 的汇率无法通过 %s 刷新。这是一个有效的货币代码吗？');
define('WARNING_PRIMARY_SERVER_FAILED', '警告：主汇率服务器 (%s) 无法更新 %s (%s) 的汇率 - 试备用汇率服务器。');

?>
