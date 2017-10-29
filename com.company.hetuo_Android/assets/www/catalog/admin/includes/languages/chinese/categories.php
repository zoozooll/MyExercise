<?php
/*
  $Id: categories.php,v 1.24 2002/08/17 09:43:33 project3000 Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '��Ʒ���� / ��Ʒ');
define('HEADING_TITLE_SEARCH', '��Ѱ��');
define('HEADING_TITLE_GOTO', '����');

define('TABLE_HEADING_ID', '���');
define('TABLE_HEADING_CATEGORIES_PRODUCTS', '��Ʒ���� / ��Ʒ');
define('TABLE_HEADING_ACTION', '����');
define('TABLE_HEADING_STATUS', '״̬');

define('TEXT_NEW_PRODUCT', '����Ʒ��&quot;%s&quot;');
define('TEXT_CATEGORIES', '��Ʒ���ࣺ');
define('TEXT_SUBCATEGORIES', '����Ʒ���ࣺ');
define('TEXT_PRODUCTS', '��Ʒ��');
define('TEXT_PRODUCTS_PRICE_INFO', '�۸�');
define('TEXT_PRODUCTS_TAX_CLASS', '˰��');
define('TEXT_PRODUCTS_AVERAGE_RATING', 'ƽ���÷֣�');
define('TEXT_PRODUCTS_QUANTITY_INFO', '������');
define('TEXT_DATE_ADDED', '�������ڣ�');
define('TEXT_DATE_AVAILABLE', '�ϼ����ڣ�');
define('TEXT_LAST_MODIFIED', '�ϴ��޸�ʱ�䣺');
define('TEXT_IMAGE_NONEXISTENT', '��ͼƬ');
define('TEXT_NO_CHILD_CATEGORIES_OR_PRODUCTS', '���������Ʒ�������Ʒ��');
define('TEXT_PRODUCT_MORE_INFORMATION', 'Ҫ��ø�����Ϣ������ʸ���Ʒ<a href="http://%s" target="blank"><u>��ҳ</u></a>��');
define('TEXT_PRODUCT_DATE_ADDED', '��Ʒ�ϼ�����Ϊ%s��');
define('TEXT_PRODUCT_DATE_AVAILABLE', '��ƷԤ�Ƶ�������Ϊ%s��');

define('TEXT_EDIT_INTRO', '������Ҫ���޸�');
define('TEXT_EDIT_CATEGORIES_ID', '��Ʒ�����ţ�');
define('TEXT_EDIT_CATEGORIES_NAME', '��Ʒ�������ƣ�');
define('TEXT_EDIT_CATEGORIES_IMAGE', '��Ʒ����ͼƬ��');
define('TEXT_EDIT_SORT_ORDER', '����˳��');

define('TEXT_INFO_COPY_TO_INTRO', '��ѡ��һ������Ʒ�����Ը��Ƹ���Ʒ��');
define('TEXT_INFO_CURRENT_CATEGORIES', '��ǰ��Ʒ���ࣺ');

define('TEXT_INFO_HEADING_NEW_CATEGORY', '����Ʒ����');
define('TEXT_INFO_HEADING_EDIT_CATEGORY', '�޸���Ʒ����');
define('TEXT_INFO_HEADING_DELETE_CATEGORY', 'ɾ����Ʒ����');
define('TEXT_INFO_HEADING_MOVE_CATEGORY', '�ƶ���Ʒ����');
define('TEXT_INFO_HEADING_DELETE_PRODUCT', 'ɾ����Ʒ');
define('TEXT_INFO_HEADING_MOVE_PRODUCT', '�ƶ���Ʒ');
define('TEXT_INFO_HEADING_COPY_TO', '���Ƶ�');

define('TEXT_DELETE_CATEGORY_INTRO', '��ȷ��Ҫɾ�������Ʒ������');
define('TEXT_DELETE_PRODUCT_INTRO', '��ȷ��Ҫ����ɾ�������Ʒ��');

define('TEXT_DELETE_WARNING_CHILDS', '<b>���棺</b>�����Ʒ�������� %s ������Ʒ���࣡');
define('TEXT_DELETE_WARNING_PRODUCTS', '<b>���棺</b>�����Ʒ�������� %s ����Ʒ��');


define('TEXT_MOVE_PRODUCTS_INTRO', '��ѡ�� <b>%s</b> �����ĸ���Ʒ����');
define('TEXT_MOVE_CATEGORIES_INTRO', '��ѡ�� <b>%s</b> �����ĸ���Ʒ����');
define('TEXT_MOVE', '�� <b>%s</b> ����');

define('TEXT_NEW_CATEGORY_INTRO', '���ṩ����Ʒ�������������');
define('TEXT_CATEGORIES_NAME', '�������ƣ�');
define('TEXT_CATEGORIES_IMAGE', '����ͼƬ��');
define('TEXT_SORT_ORDER', '����˳��');

define('TEXT_PRODUCTS_STATUS', '��Ʒ״̬��');
define('TEXT_PRODUCTS_DATE_AVAILABLE', '�ϼ����ڣ�');
define('TEXT_PRODUCT_AVAILABLE', '�л�');
define('TEXT_PRODUCT_NOT_AVAILABLE', '�޻�');
define('TEXT_PRODUCTS_MANUFACTURER', '�����̣�');
define('TEXT_PRODUCTS_NAME', '��Ʒ���ƣ�');
define('TEXT_PRODUCTS_DESCRIPTION', '��Ʒ������');
define('TEXT_PRODUCTS_QUANTITY', '��Ʒ������');
define('TEXT_PRODUCTS_MODEL', '��Ʒ�ͺţ�');
define('TEXT_PRODUCTS_IMAGE', '��ƷͼƬ��');
define('TEXT_PRODUCTS_URL', '��ƷURL��');
define('TEXT_PRODUCTS_URL_WITHOUT_HTTP', '<small>( http://)</small>');
define('TEXT_PRODUCTS_PRICE_NET', '��Ʒ�۸�');
define('TEXT_PRODUCTS_PRICE_GROSS', '��Ʒ�۸�');
define('TEXT_PRODUCTS_WEIGHT', '��Ʒ������');

define('EMPTY_CATEGORY', '�հ���Ʒ����');

define('TEXT_HOW_TO_COPY', '���Ʒ�����');
define('TEXT_COPY_AS_LINK', '������Ʒ');
define('TEXT_COPY_AS_DUPLICATE', '������Ʒ');

define('ERROR_CANNOT_LINK_TO_SAME_CATEGORY', '������ͬһ�������в���������Ʒ��');
define('ERROR_CATALOG_IMAGE_DIRECTORY_NOT_WRITEABLE', '�������ڴ����Ʒ����ͼƬ���ļ�Ŀ¼�޷�д�룺 ' . DIR_FS_CATALOG_IMAGES);
define('ERROR_CATALOG_IMAGE_DIRECTORY_DOES_NOT_EXIST', '�������ڴ����Ʒ����ͼƬ���ļ�Ŀ¼�����ڣ� ' . DIR_FS_CATALOG_IMAGES);
define('ERROR_CANNOT_MOVE_CATEGORY_TO_PARENT', '������Ʒ���಻�������ӷ����С�');
?>
