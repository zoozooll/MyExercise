<?php
/*
  $Id: categories.php,v 1.24 2002/08/17 09:43:33 project3000 Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '商品分类 / 商品');
define('HEADING_TITLE_SEARCH', '搜寻：');
define('HEADING_TITLE_GOTO', '到：');

define('TABLE_HEADING_ID', '编号');
define('TABLE_HEADING_CATEGORIES_PRODUCTS', '商品分类 / 商品');
define('TABLE_HEADING_ACTION', '操作');
define('TABLE_HEADING_STATUS', '状态');

define('TEXT_NEW_PRODUCT', '新商品在&quot;%s&quot;');
define('TEXT_CATEGORIES', '商品分类：');
define('TEXT_SUBCATEGORIES', '子商品分类：');
define('TEXT_PRODUCTS', '商品：');
define('TEXT_PRODUCTS_PRICE_INFO', '价格：');
define('TEXT_PRODUCTS_TAX_CLASS', '税别：');
define('TEXT_PRODUCTS_AVERAGE_RATING', '平均得分：');
define('TEXT_PRODUCTS_QUANTITY_INFO', '数量：');
define('TEXT_DATE_ADDED', '创建日期：');
define('TEXT_DATE_AVAILABLE', '上架日期：');
define('TEXT_LAST_MODIFIED', '上次修改时间：');
define('TEXT_IMAGE_NONEXISTENT', '无图片');
define('TEXT_NO_CHILD_CATEGORIES_OR_PRODUCTS', '请插入新商品分类或商品。');
define('TEXT_PRODUCT_MORE_INFORMATION', '要获得更多信息，请访问该商品<a href="http://%s" target="blank"><u>网页</u></a>。');
define('TEXT_PRODUCT_DATE_ADDED', '商品上架日期为%s。');
define('TEXT_PRODUCT_DATE_AVAILABLE', '商品预计到货日期为%s。');

define('TEXT_EDIT_INTRO', '请作必要的修改');
define('TEXT_EDIT_CATEGORIES_ID', '商品分类编号：');
define('TEXT_EDIT_CATEGORIES_NAME', '商品分类名称：');
define('TEXT_EDIT_CATEGORIES_IMAGE', '商品分类图片：');
define('TEXT_EDIT_SORT_ORDER', '排序顺序：');

define('TEXT_INFO_COPY_TO_INTRO', '请选择一个新商品分类以复制该商品。');
define('TEXT_INFO_CURRENT_CATEGORIES', '当前商品分类：');

define('TEXT_INFO_HEADING_NEW_CATEGORY', '新商品分类');
define('TEXT_INFO_HEADING_EDIT_CATEGORY', '修改商品分类');
define('TEXT_INFO_HEADING_DELETE_CATEGORY', '删除商品分类');
define('TEXT_INFO_HEADING_MOVE_CATEGORY', '移动商品分类');
define('TEXT_INFO_HEADING_DELETE_PRODUCT', '删除商品');
define('TEXT_INFO_HEADING_MOVE_PRODUCT', '移动商品');
define('TEXT_INFO_HEADING_COPY_TO', '复制到');

define('TEXT_DELETE_CATEGORY_INTRO', '你确定要删除这个商品分类吗？');
define('TEXT_DELETE_PRODUCT_INTRO', '你确定要永久删除这个商品吗？');

define('TEXT_DELETE_WARNING_CHILDS', '<b>警告：</b>这个商品分类仍有 %s 个子商品分类！');
define('TEXT_DELETE_WARNING_PRODUCTS', '<b>警告：</b>这个商品分类仍有 %s 个商品！');


define('TEXT_MOVE_PRODUCTS_INTRO', '请选择将 <b>%s</b> 放在哪个商品分类');
define('TEXT_MOVE_CATEGORIES_INTRO', '请选择将 <b>%s</b> 放在哪个商品分类');
define('TEXT_MOVE', '将 <b>%s</b> 移至');

define('TEXT_NEW_CATEGORY_INTRO', '请提供新商品分类的以下资料');
define('TEXT_CATEGORIES_NAME', '分类名称：');
define('TEXT_CATEGORIES_IMAGE', '分类图片：');
define('TEXT_SORT_ORDER', '排序顺序：');

define('TEXT_PRODUCTS_STATUS', '商品状态：');
define('TEXT_PRODUCTS_DATE_AVAILABLE', '上架日期：');
define('TEXT_PRODUCT_AVAILABLE', '有货');
define('TEXT_PRODUCT_NOT_AVAILABLE', '无货');
define('TEXT_PRODUCTS_MANUFACTURER', '制造商：');
define('TEXT_PRODUCTS_NAME', '商品名称：');
define('TEXT_PRODUCTS_DESCRIPTION', '商品描述：');
define('TEXT_PRODUCTS_QUANTITY', '商品数量：');
define('TEXT_PRODUCTS_MODEL', '商品型号：');
define('TEXT_PRODUCTS_IMAGE', '商品图片：');
define('TEXT_PRODUCTS_URL', '商品URL：');
define('TEXT_PRODUCTS_URL_WITHOUT_HTTP', '<small>( http://)</small>');
define('TEXT_PRODUCTS_PRICE_NET', '商品价格：');
define('TEXT_PRODUCTS_PRICE_GROSS', '商品价格：');
define('TEXT_PRODUCTS_WEIGHT', '商品重量：');

define('EMPTY_CATEGORY', '空白商品分类');

define('TEXT_HOW_TO_COPY', '复制方法：');
define('TEXT_COPY_AS_LINK', '链接商品');
define('TEXT_COPY_AS_DUPLICATE', '复制商品');

define('ERROR_CANNOT_LINK_TO_SAME_CATEGORY', '错误：在同一个分类中不能链接商品。');
define('ERROR_CATALOG_IMAGE_DIRECTORY_NOT_WRITEABLE', '错误：用于存放商品分类图片的文件目录无法写入： ' . DIR_FS_CATALOG_IMAGES);
define('ERROR_CATALOG_IMAGE_DIRECTORY_DOES_NOT_EXIST', '错误：用于存放商品分类图片的文件目录不存在： ' . DIR_FS_CATALOG_IMAGES);
define('ERROR_CANNOT_MOVE_CATEGORY_TO_PARENT', '错误：商品分类不能移入子分类中。');
?>
