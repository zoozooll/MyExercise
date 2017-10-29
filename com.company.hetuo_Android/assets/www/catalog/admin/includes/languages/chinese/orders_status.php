<?php
/*
  $Id: orders_status.php,v 1.5 2002/01/29 14:43:00 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '订单状态');

define('TABLE_HEADING_ORDERS_STATUS', '订单状态');
define('TABLE_HEADING_ACTION', '操作');

define('TEXT_INFO_EDIT_INTRO', '请作必要的修改');
define('TEXT_INFO_ORDERS_STATUS_NAME', '订单状态：');
define('TEXT_INFO_INSERT_INTRO', '请输入新订单状态及相关数据');
define('TEXT_INFO_DELETE_INTRO', '你确定要删除这个订单状态吗？');
define('TEXT_INFO_HEADING_NEW_ORDERS_STATUS', '新订单状态');
define('TEXT_INFO_HEADING_EDIT_ORDERS_STATUS', '修改订单状态');
define('TEXT_INFO_HEADING_DELETE_ORDERS_STATUS', '删除订单状态');

define('ERROR_REMOVE_DEFAULT_ORDER_STATUS', '错误：不能删除预设订单状态。请将别的订单状态定为预设，然后再试。');
define('ERROR_STATUS_USED_IN_ORDERS', '错误：这个订单状态正用于订单中。');
define('ERROR_STATUS_USED_IN_HISTORY', '错误：这个订单状态正用于订单状态纪录中。');
?>