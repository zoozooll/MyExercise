<?php
/*
  $Id: english.php,v 1.101 2002/11/11 13:30:16 project3000 Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

// look in your $PATH_LOCALE/locale directory for available locales..
// on RedHat6.0 I used 'en_US'
// on FreeBSD 4.0 I use 'en_US.ISO_8859-1'
// this may not work under win32 environments..
define('DATE_FORMAT_SHORT', '%Y/%m/%d');  // this is used for strftime()
define('DATE_FORMAT_LONG', '%Y/%m/%d'); // this is used for strftime()
define('DATE_FORMAT', 'Y/m/d'); // this is used for date()
define('DATE_TIME_FORMAT', DATE_FORMAT_SHORT . ' %H:%m:%s');

////
// Return date in raw format
// $date should be in format mm/dd/yyyy
// raw date is in format YYYYMMDD, or DDMMYYYY
function tep_date_raw($date, $reverse = false) {
  if ($reverse) {
    return substr($date, 0, 2) . substr($date, 3, 2) . substr($date, 6, 4);
  } else {
    return substr($date, 6, 4) . substr($date, 3, 2) . substr($date, 0, 2);
  }
}

// Global entries for the <html> tag
define('HTML_PARAMS','dir="ltr" lang="zh"');

// charset for web pages and emails
define('CHARSET', 'gb2312');

// page title
define('TITLE', STORE_NAME);

// header text in includes/header.php
define('HEADER_TITLE_TOP', 'ϵͳ����');
define('HEADER_TITLE_SUPPORT_SITE', '֧����վ');
define('HEADER_TITLE_ONLINE_CATALOG', '��ƷĿ¼');
define('HEADER_TITLE_ADMINISTRATION', 'ϵͳ����');

// text for gender
define('MALE', '��');
define('FEMALE', 'Ů');

// text for date of birth example
define('DOB_FORMAT_STRING', 'mm/dd/yyyy');

// configuration box text in includes/boxes/configuration.php
define('BOX_HEADING_CONFIGURATION', 'ϵͳ����');
define('BOX_CONFIGURATION_MYSTORE', '�ҵ��̵�');
define('BOX_CONFIGURATION_LOGGING', 'ϵͳ��¼');
define('BOX_CONFIGURATION_CACHE', 'ϵͳ������');

// modules box text in includes/boxes/modules.php
define('BOX_HEADING_MODULES', '���ģ��');
define('BOX_MODULES_PAYMENT', '���ʽ');
define('BOX_MODULES_SHIPPING', '�ͻ���ʽ');
define('BOX_MODULES_ORDER_TOTAL', '�����ܼ�');

// categories box text in includes/boxes/catalog.php
define('BOX_HEADING_CATALOG', '��ƷĿ¼');
define('BOX_CATALOG_CATEGORIES_PRODUCTS', '����/��Ʒ');
define('BOX_CATALOG_CATEGORIES_PRODUCTS_ATTRIBUTES', '��Ʒ����');
define('BOX_CATALOG_MANUFACTURERS', '������');
define('BOX_CATALOG_REVIEWS', '��Ʒ����');
define('BOX_CATALOG_SPECIALS', '�ؼ���Ʒ');
define('BOX_CATALOG_PRODUCTS_EXPECTED', '����������Ʒ');

// customers box text in includes/boxes/customers.php
define('BOX_HEADING_CUSTOMERS', '�˿�');
define('BOX_CUSTOMERS_CUSTOMERS', '�˿�');
define('BOX_CUSTOMERS_ORDERS', '����');

// taxes box text in includes/boxes/taxes.php
define('BOX_HEADING_LOCATION_AND_TAXES', '���� / ˰��');
define('BOX_TAXES_COUNTRIES', '����');
define('BOX_TAXES_ZONES', '����');
define('BOX_TAXES_GEO_ZONES', '˰��');
define('BOX_TAXES_TAX_CLASSES', '˰��');
define('BOX_TAXES_TAX_RATES', '˰��');

// reports box text in includes/boxes/reports.php
define('BOX_HEADING_REPORTS', '����');
define('BOX_REPORTS_PRODUCTS_VIEWED', '��Ʒ������');
define('BOX_REPORTS_PRODUCTS_PURCHASED', '��Ʒ�������');
define('BOX_REPORTS_ORDERS_TOTAL', '����ͳ��');

// tools text in includes/boxes/tools.php
define('BOX_HEADING_TOOLS', 'ϵͳ����');
define('BOX_TOOLS_BACKUP', '���ݿⱸ��');
define('BOX_TOOLS_BANNER_MANAGER', '��������');
define('BOX_TOOLS_CACHE', 'ϵͳ����������');
define('BOX_TOOLS_DEFINE_LANGUAGE', '��������');
define('BOX_TOOLS_FILE_MANAGER', '�ļ�������');
define('BOX_TOOLS_MAIL', '�����ʼ�');
define('BOX_TOOLS_NEWSLETTER_MANAGER', '�������Ź�����');
define('BOX_TOOLS_SERVER_INFO', '����������');
define('BOX_TOOLS_WHOS_ONLINE', '�����û�');

// localizaion box text in includes/boxes/localization.php
define('BOX_HEADING_LOCALIZATION', '����������');
define('BOX_LOCALIZATION_CURRENCIES', '����');
define('BOX_LOCALIZATION_LANGUAGES', '����');
define('BOX_LOCALIZATION_ORDERS_STATUS', '����״̬');

// javascript messages
define('JS_ERROR', '���ı���ڴ����з��ִ���\n���޸ģ�\n\n');

define('JS_OPTIONS_VALUE_PRICE', '* ��Ʒ����Ӧ�����۸�\n');
define('JS_OPTIONS_VALUE_PRICE_PREFIX', '* ��Ʒ����Ӧ�����۸�ǰ׺��\n');

define('JS_PRODUCTS_NAME', '* ����Ʒ��Ҫ��д����\n');
define('JS_PRODUCTS_DESCRIPTION', '* ����Ʒ��Ҫ��д��Ʒ˵��\n');
define('JS_PRODUCTS_PRICE', '* ����Ʒ��Ҫ��д�۸�\n');
define('JS_PRODUCTS_WEIGHT', '* ����Ʒ��Ҫ��д����\n');
define('JS_PRODUCTS_QUANTITY', '* ����Ʒ��Ҫ��д����\n');
define('JS_PRODUCTS_MODEL', '* ����Ʒ��Ҫ��д��Ʒ�ͺ�\n');
define('JS_PRODUCTS_IMAGE', '* ����Ʒ��Ҫ��ƷͼƬ\n');

define('JS_SPECIALS_PRODUCTS_PRICE', '* ���ṩ����Ʒ���¼۸�\n');

define('JS_GENDER', '* \'�Ա�\' ����ѡ.\n');
define('JS_FIRST_NAME', '* \'��\' ����Ϊ ' . ENTRY_FIRST_NAME_MIN_LENGTH . ' ���֡�\n');
define('JS_LAST_NAME', '* The \'��\' ����Ϊ ' . ENTRY_LAST_NAME_MIN_LENGTH . '���֡�\n');
define('JS_DOB', '* \'��������\' ��ʽΪ: xx/xx/xxxx (��/��/��)��\n');
define('JS_EMAIL_ADDRESS', '* \'�����ʼ�\' ����Ϊ ' . ENTRY_EMAIL_ADDRESS_MIN_LENGTH . ' ���ֳ���\n');
define('JS_ADDRESS', '* \'�ֵ����ƺ���\' ����Ϊ ' . ENTRY_STREET_ADDRESS_MIN_LENGTH . ' ���֡�\n');
define('JS_POST_CODE', '* \'��������\' ӦΪ ' . ENTRY_POSTCODE_MIN_LENGTH . ' λ����\n');
define('JS_CITY', '* \'�У��أ�\' ����Ϊ ' . ENTRY_CITY_MIN_LENGTH . ' ���֡�\n');
define('JS_STATE', '* \'ʡ��\' ����ѡ��\n');
define('JS_STATE_SELECT', '-- ѡ���ϸ��� --');
define('JS_ZONE', '* \'ʡ��\' Ӧ�Ӹù�����ʡ����ѡ����');
define('JS_COUNTRY', '* \'����\' ����ѡ��\n');
define('JS_TELEPHONE', '* \'�绰\'��������Ϊ ' . ENTRY_TELEPHONE_MIN_LENGTH . ' λ����\n');
define('JS_PASSWORD', '* \'����\'�� \'����ȷ��\'��������ͬ��������Ϊ ' . ENTRY_PASSWORD_MIN_LENGTH . ' ���֡�\n');

define('JS_ORDER_DOES_NOT_EXIST', '������ %s �����ڣ�');

define('CATEGORY_PERSONAL', '����');
define('CATEGORY_ADDRESS', '��ַ');
define('CATEGORY_CONTACT', '��ϵ��ʽ');
define('CATEGORY_COMPANY', '��˾');
/*
define('CATEGORY_PASSWORD', '����');
*/
define('CATEGORY_OPTIONS', 'ѡ��');
define('ENTRY_GENDER', '�Ա�');
define('ENTRY_GENDER_ERROR', '&nbsp;<span class="errorText">������</span>');
define('ENTRY_FIRST_NAME', '����');
define('ENTRY_FIRST_NAME_ERROR', '&nbsp;<span class="errorText">���� ' . ENTRY_FIRST_NAME_MIN_LENGTH . ' ����</span>');
define('ENTRY_LAST_NAME', '�գ�');
define('ENTRY_LAST_NAME_ERROR', '&nbsp;<span class="errorText">���� ' . ENTRY_LAST_NAME_MIN_LENGTH . ' ����</span>');
define('ENTRY_DATE_OF_BIRTH', '�������ڣ�');
define('ENTRY_DATE_OF_BIRTH_ERROR', '&nbsp;<span class="errorText">(���磺 05/21/1970)</span>');
define('ENTRY_EMAIL_ADDRESS', '�����ʼ���');
define('ENTRY_EMAIL_ADDRESS_ERROR', '&nbsp;<span class="errorText">���� ' . ENTRY_EMAIL_ADDRESS_MIN_LENGTH . ' ����</span>');
define('ENTRY_EMAIL_ADDRESS_CHECK_ERROR', '&nbsp;<span class="errorText">��������ʼ���ַ��Ч��</span>');
define('ENTRY_EMAIL_ADDRESS_ERROR_EXISTS', '&nbsp;<span class="errorText">��������ʼ���ַ�Ѵ��ڣ�</span>');
define('ENTRY_COMPANY', '��˾���ƣ�');
define('ENTRY_COMPANY_ERROR', '');
define('ENTRY_STREET_ADDRESS', '�ֵ����ƺţ�');
define('ENTRY_STREET_ADDRESS_ERROR', '&nbsp;<span class="errorText">���� ' . ENTRY_STREET_ADDRESS_MIN_LENGTH . ' ����</span>');
define('ENTRY_SUBURB', '�أ�������');
define('ENTRY_SUBURB_ERROR', '');
define('ENTRY_POST_CODE', '�������룺');
define('ENTRY_POST_CODE_ERROR', '&nbsp;<span class="errorText">���� ' . ENTRY_POSTCODE_MIN_LENGTH . ' λ</span>');
define('ENTRY_CITY', '�У��أ���');
define('ENTRY_CITY_ERROR', '&nbsp;<span class="errorText">���� ' . ENTRY_CITY_MIN_LENGTH . ' </span>');
define('ENTRY_STATE', 'ʡ�ݣ�');
define('ENTRY_STATE_ERROR', '&nbsp;<span class="errorText">������</span>');
define('ENTRY_COUNTRY', '���ң�');
define('ENTRY_COUNTRY_ERROR', '');
define('ENTRY_TELEPHONE_NUMBER', '�绰��');
define('ENTRY_TELEPHONE_NUMBER_ERROR', '&nbsp;<span class="errorText">���� ' . ENTRY_TELEPHONE_MIN_LENGTH . ' λ</span>');
define('ENTRY_FAX_NUMBER', '���棺');
define('ENTRY_FAX_NUMBER_ERROR', '');
define('ENTRY_NEWSLETTER', '�������ţ�');
define('ENTRY_NEWSLETTER_YES', '����');
define('ENTRY_NEWSLETTER_NO', '������');
define('ENTRY_NEWSLETTER_ERROR', '');
/*
define('ENTRY_PASSWORD', '���룺');
define('ENTRY_PASSWORD_CONFIRMATION', '����ȷ�ϣ�');
define('PASSWORD_HIDDEN', '--����--');
*/
// images
define('IMAGE_ANI_SEND_EMAIL', '���ڷ��͵����ʼ�');
define('IMAGE_BACK', '����');
define('IMAGE_BACKUP', '����');
define('IMAGE_CANCEL', 'ȡ��');
define('IMAGE_CONFIRM', 'ȷ��');
define('IMAGE_COPY', '����');
define('IMAGE_COPY_TO', '���Ƶ�');
define('IMAGE_DETAILS', '����');
define('IMAGE_DELETE', 'ɾ��');
define('IMAGE_EDIT', '�޸�');
define('IMAGE_EMAIL', '�����ʼ�');
define('IMAGE_FILE_MANAGER', '�ļ�������');
define('IMAGE_ICON_STATUS_GREEN', '�Ѽ���');
define('IMAGE_ICON_STATUS_GREEN_LIGHT', '�������');
define('IMAGE_ICON_STATUS_RED', 'δ����');
define('IMAGE_ICON_STATUS_RED_LIGHT', '����ر�');
define('IMAGE_ICON_INFO', '����');
define('IMAGE_INSERT', '����');
define('IMAGE_LOCK', '��ס');
define('IMAGE_MODULE_INSTALL', '��װģ��');
define('IMAGE_MODULE_REMOVE', 'ɾ��ģ��');

define('IMAGE_MOVE', '�ƶ�');
define('IMAGE_NEW_BANNER', '�¹��');
define('IMAGE_NEW_CATEGORY', '�·���');
define('IMAGE_NEW_COUNTRY', '�¹���');
define('IMAGE_NEW_CURRENCY', '�»���');
define('IMAGE_NEW_FILE', '���ļ�');
define('IMAGE_NEW_FOLDER', '���ļ���');
define('IMAGE_NEW_LANGUAGE', '������');
define('IMAGE_NEW_NEWSLETTER', '�µ�������');
define('IMAGE_NEW_PRODUCT', '����Ʒ');
define('IMAGE_NEW_TAX_CLASS', '��˰��');
define('IMAGE_NEW_TAX_RATE', '��˰��');
define('IMAGE_NEW_TAX_ZONE', '��˰��');
define('IMAGE_NEW_ZONE', '�µ���');
define('IMAGE_ORDERS', '����');
define('IMAGE_ORDERS_INVOICE', '��Ʊ');
define('IMAGE_ORDERS_PACKINGSLIP', 'װ�䵥');
define('IMAGE_PREVIEW', 'Ԥ��');
define('IMAGE_RESTORE', '�ָ�');
define('IMAGE_RESET', '��ԭ');
define('IMAGE_SAVE', '����');
define('IMAGE_SEARCH', '��Ѱ');
define('IMAGE_SELECT', 'ѡ��');
define('IMAGE_SEND', '����');
define('IMAGE_SEND_EMAIL', '�������ʼ�');
define('IMAGE_UNLOCK', '����');
define('IMAGE_UPDATE', '����');
define('IMAGE_UPDATE_CURRENCIES', '���»���');
define('IMAGE_UPLOAD', '����');

define('ICON_CROSS', '��');
define('ICON_CURRENT_FOLDER', '��ǰ�ļ���');
define('ICON_DELETE', 'ɾ��');
define('ICON_ERROR', '����');
define('ICON_FILE', '�ļ�');
define('ICON_FILE_DOWNLOAD', '����');
define('ICON_FOLDER', '�ļ���');
define('ICON_LOCKED', '����ס');
define('ICON_PREVIOUS_LEVEL', 'ǰ��һ��');
define('ICON_PREVIEW', 'Ԥ��');
define('ICON_STATISTICS', 'ͳ������');
define('ICON_SUCCESS', '���');
define('ICON_TICK', '��');
define('ICON_UNLOCKED', '���Ѵ�');
define('ICON_WARNING', '����');

// constants for use in tep_prev_next_display function
define('TEXT_RESULT_PAGE', '�� %s ҳ �� %d ҳ');
define('TEXT_DISPLAY_NUMBER_OF_BANNERS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> �����)');
define('TEXT_DISPLAY_NUMBER_OF_COUNTRIES', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ������)');
define('TEXT_DISPLAY_NUMBER_OF_CUSTOMERS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ���˿�)');
define('TEXT_DISPLAY_NUMBER_OF_CURRENCIES', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> �ֻ���)'); 
define('TEXT_DISPLAY_NUMBER_OF_LANGUAGES', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ������)');
define('TEXT_DISPLAY_NUMBER_OF_MANUFACTURERS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ��������)');
define('TEXT_DISPLAY_NUMBER_OF_NEWSLETTERS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ���������)');
define('TEXT_DISPLAY_NUMBER_OF_ORDERS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ������)');
define('TEXT_DISPLAY_NUMBER_OF_ORDERS_STATUS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> �ֶ���״̬)');
define('TEXT_DISPLAY_NUMBER_OF_PRODUCTS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ����Ʒ)');
define('TEXT_DISPLAY_NUMBER_OF_PRODUCTS_EXPECTED', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ����������Ʒ)');
define('TEXT_DISPLAY_NUMBER_OF_REVIEWS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ����Ʒ����)');
define('TEXT_DISPLAY_NUMBER_OF_SPECIALS', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ���ؼ���Ʒ)');
define('TEXT_DISPLAY_NUMBER_OF_TAX_CLASSES', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ��˰��)');
define('TEXT_DISPLAY_NUMBER_OF_TAX_ZONES', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ��˰��)');
define('TEXT_DISPLAY_NUMBER_OF_TAX_RATES', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ��˰��)');
define('TEXT_DISPLAY_NUMBER_OF_ZONES', '��ʾ <b>%d</b> �� <b>%d</b> (�� <b>%d</b> ������)');

define('PREVNEXT_BUTTON_PREV', '&lt;&lt;');
define('PREVNEXT_BUTTON_NEXT', '&gt;&gt;');

define('TEXT_DEFAULT', 'Ԥ��ֵ');
define('TEXT_SET_DEFAULT', '��ΪԤ��ֵ');
define('TEXT_FIELD_REQUIRED', '&nbsp;<span class="fieldRequired">* ������</span>');

define('ERROR_NO_DEFAULT_CURRENCY_DEFINED', '����: ��Ԥ�����. ��ȥ: ϵͳ����->������->����');

define('TEXT_CACHE_CATEGORIES', '��Ʒ������');
define('TEXT_CACHE_MANUFACTURERS', '��������');
define('TEXT_CACHE_ALSO_PURCHASED', '��ģ��');

define('TEXT_NONE', '--��--');
define('TEXT_TOP', '����');
define('ERROR_DESTINATION_DOES_NOT_EXIST', '��������Ŀ¼�����ڡ�');
define('ERROR_DESTINATION_NOT_WRITEABLE', '��������Ŀ¼����д��');
define('ERROR_FILE_NOT_SAVED', '���������ļ�δ���̡�');
define('ERROR_FILETYPE_NOT_ALLOWED', '���������ļ����ͱ��ܾ���');
define('SUCCESS_FILE_SAVED_SUCCESSFULLY', '�ɹ��������ļ��Ѵ��̡�');
define('WARNING_NO_FILE_UPLOADED', '���棺���ļ������ء�');
define('WARNING_FILE_UPLOADS_DISABLED', '���棺�� php.ini �����ļ��У��ļ����ع����ѱ������');

?>