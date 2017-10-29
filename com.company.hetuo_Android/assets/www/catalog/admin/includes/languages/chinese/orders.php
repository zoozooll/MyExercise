<?php
/*
  $Id: orders.php,v 1.24 2003/02/09 13:15:22 thomasamoulton Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '订单');
define('HEADING_TITLE_SEARCH', '订单编号：');
define('HEADING_TITLE_STATUS', '状态：');

define('TABLE_HEADING_COMMENTS', '附言');
define('TABLE_HEADING_CUSTOMERS', '顾客');
define('TABLE_HEADING_ORDER_TOTAL', '订单总额');
define('TABLE_HEADING_DATE_PURCHASED', '订购日期');
/*
define('TABLE_HEADING_STATUS', '状态');
*/
define('TABLE_HEADING_ACTION', '操作');
define('TABLE_HEADING_QUANTITY', '数量');
define('TABLE_HEADING_PRODUCTS_MODEL', '型号');
define('TABLE_HEADING_PRODUCTS', '品名');
define('TABLE_HEADING_TAX', '税');
define('TABLE_HEADING_TOTAL', '总计');
/*
define('TABLE_HEADING_STATUS', '状态');
*/
define('TABLE_HEADING_PRICE_EXCLUDING_TAX', '价格 (税前)');
define('TABLE_HEADING_PRICE_INCLUDING_TAX', '价格 (税后)');
define('TABLE_HEADING_TOTAL_EXCLUDING_TAX', '总计 (税前)');
define('TABLE_HEADING_TOTAL_INCLUDING_TAX', '总计 (税后)');

define('TABLE_HEADING_STATUS', '状态');
define('TABLE_HEADING_CUSTOMER_NOTIFIED', '已通知顾客');
define('TABLE_HEADING_DATE_ADDED', '创建日期');

define('ENTRY_CUSTOMER', '顾客：');
define('ENTRY_SOLD_TO', '售给：');
/*
define('ENTRY_STREET_ADDRESS', '街道门牌号码：');
define('ENTRY_SUBURB', '县（区）：');
define('ENTRY_CITY', '市（县）：');
define('ENTRY_POST_CODE', '邮政编码：');
define('ENTRY_STATE', '省份：');
define('ENTRY_COUNTRY', '国家：');
define('ENTRY_TELEPHONE', '电话：');
define('ENTRY_EMAIL_ADDRESS', '电子邮件：');
*/
define('ENTRY_DELIVERY_TO', '发货给：');
define('ENTRY_SHIP_TO', '发货给：');
define('ENTRY_SHIPPING_ADDRESS', '发货地址：');
define('ENTRY_BILLING_ADDRESS', '账单地址：');
define('ENTRY_PAYMENT_METHOD', '付款方式：');
define('ENTRY_CREDIT_CARD_TYPE', '信用卡种类：');
define('ENTRY_CREDIT_CARD_OWNER', '信用卡持有人：');
define('ENTRY_CREDIT_CARD_NUMBER', '信用卡号：');
define('ENTRY_CREDIT_CARD_EXPIRES', '信用卡有效日期：');
define('ENTRY_SUB_TOTAL', '小计：');
define('ENTRY_TAX', '税：');
define('ENTRY_SHIPPING', '送货费：');
define('ENTRY_TOTAL', '总计：');
define('ENTRY_DATE_PURCHASED', '订购日期：');
define('ENTRY_STATUS', '状态：');
define('ENTRY_DATE_LAST_UPDATED', '上次更新日期：');
define('ENTRY_NOTIFY_CUSTOMER', '通知顾客：');
define('ENTRY_NOTIFY_COMMENTS', '附言：');
define('ENTRY_PRINTABLE', '打印发票：');

define('TEXT_INFO_HEADING_DELETE_ORDER', '删除订单');
define('TEXT_INFO_DELETE_INTRO', '你确定要删除这个订单？');
define('TEXT_INFO_RESTOCK_PRODUCT_QUANTITY', '增补商品数量');
define('TEXT_DATE_ORDER_CREATED', '创建日期：');
define('TEXT_DATE_ORDER_LAST_MODIFIED', '上次修改时间：');
define('TEXT_INFO_PAYMENT_METHOD', '付款方式：');

define('TEXT_ALL_ORDERS', '所有订单');
define('TEXT_NO_ORDER_HISTORY', '无订单记录');

define('EMAIL_SEPARATOR', '------------------------------------------------------');
define('EMAIL_TEXT_SUBJECT', '订单状态更新');
define('EMAIL_TEXT_ORDER_NUMBER', '订单编号：');
define('EMAIL_TEXT_INVOICE_URL', '详细发票：');
define('EMAIL_TEXT_DATE_ORDERED', '订购日期：');
define('EMAIL_TEXT_STATUS_UPDATE', '您的订单更新后的状态如下：' . "\n\n" . '%s' . "\n\n" . '如有问题请速与我们联系。' . "\n");
define('EMAIL_TEXT_COMMENTS_UPDATE', '您的订单有以下附言：' . "\n\n%s\n\n");

define('ERROR_ORDER_DOES_NOT_EXIST', '错误：订单不存在。');
define('SUCCESS_ORDER_UPDATED', '完成：订单已更新。');
define('WARNING_ORDER_NOT_UPDATED', '警告：没有改动。 定单未更新。');
?>
