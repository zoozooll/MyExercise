<?php
/*
  $Id: banner_manager.php,v 1.17 2002/08/18 18:54:47 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '��������');

define('TABLE_HEADING_BANNERS', '���');
define('TABLE_HEADING_GROUPS', 'Ⱥ��');
define('TABLE_HEADING_STATISTICS', '��ʾ���� / �������');
define('TABLE_HEADING_STATUS', '״̬');
define('TABLE_HEADING_ACTION', '����');

define('TEXT_BANNERS_TITLE', '�������');
define('TEXT_BANNERS_URL', '���URL��');
define('TEXT_BANNERS_GROUP', '���Ⱥ�飺');
define('TEXT_BANNERS_NEW_GROUP', '��������������һ���µĹ��Ⱥ��');
define('TEXT_BANNERS_IMAGE', 'ͼƬ��');
define('TEXT_BANNERS_IMAGE_LOCAL', '�������������뱾���ļ�');
define('TEXT_BANNERS_IMAGE_TARGET', '��ͼƬ������');
define('TEXT_BANNERS_HTML_TEXT', 'HTML �ı���');
define('TEXT_BANNERS_EXPIRES_ON', '�������ڣ�');
define('TEXT_BANNERS_OR_AT', '������');
define('TEXT_BANNERS_IMPRESSIONS', '���');
define('TEXT_BANNERS_SCHEDULED_AT', '��ʼ���ڣ�');
define('TEXT_BANNERS_BANNER_NOTE', '<b>���˵����</b><ul><li>ͼƬ��HTML�ı�����ͬʱʹ�á�</li><li>HTML�ı���ͼƬ������Ȩ</li></ul>');
define('TEXT_BANNERS_INSERT_NOTE', '<b>ͼƬ˵����</b><ul><li>�����ļ�Ŀ¼���������û�д�룡</li><li>������轫ͼƬ���ص�������������ʹ�ñ��أ��������ˣ�ͼƬ����Ҫ�� \'��ͼƬ����\' ����</li><li>\'��ͼƬ����\' �����ݱ�����һ���Ѵ����ļ�Ŀ¼������б�ܽ�β�����磬banners/��</li></ul>');
define('TEXT_BANNERS_EXPIRCY_NOTE', '<b>��������˵����</b><ul><li>���ں��������ֻ��һ��</li><li>���治���Զ�ֹͣ���������</li></ul>');
define('TEXT_BANNERS_SCHEDULE_NOTE', '<b>��ʼ����˵����</b><ul><li>���趨�˿�ʼ���ڣ����潫�ڸ����Զ���ʼ��ʾ��</li><li>������趨�Ŀ�ʼ����ǰ����ʾ��֮�󽫱���Ϊ��ʾ�С�</li></ul>');

define('TEXT_BANNERS_DATE_ADDED', '�������ڣ�');
define('TEXT_BANNERS_SCHEDULED_AT_DATE', '��ʼ�ڣ�<b>%s</b>');
define('TEXT_BANNERS_EXPIRES_AT_DATE', '�����ڣ�<b>%s</b>');
define('TEXT_BANNERS_EXPIRES_AT_IMPRESSIONS', '�����ڣ�<b>%s</b> ���');
define('TEXT_BANNERS_STATUS_CHANGE', '״̬�仯��%s');

define('TEXT_BANNERS_DATA', '��<br>��');
define('TEXT_BANNERS_LAST_3_DAYS', 'ǰ3��');
define('TEXT_BANNERS_BANNER_VIEWS', '������');
define('TEXT_BANNERS_BANNER_CLICKS', '�����');

define('TEXT_INFO_DELETE_INTRO', '��ȷ��Ҫɾ����������');
define('TEXT_INFO_DELETE_IMAGE', 'ɾ�����ͼƬ');

define('SUCCESS_BANNER_INSERTED', '��ɣ�����Ѳ��롣');
define('SUCCESS_BANNER_UPDATED', '��ɣ�����Ѹ��¡�');
define('SUCCESS_BANNER_REMOVED', '��ɣ������ɾ����');
define('SUCCESS_BANNER_STATUS_UPDATED', '��ɣ����״̬�Ѹ��¡�');

define('ERROR_BANNER_TITLE_REQUIRED', '���󣺹���������');
define('ERROR_BANNER_GROUP_REQUIRED', '���󣺹��Ⱥ������');
define('ERROR_IMAGE_DIRECTORY_DOES_NOT_EXIST', '����Ŀ���ļ�Ŀ¼�����ڣ�%s');
define('ERROR_IMAGE_DIRECTORY_NOT_WRITEABLE', '����Ŀ���ļ�Ŀ¼�޷�д�룺%s');
define('ERROR_IMAGE_DOES_NOT_EXIST', '����ͼƬ������');
define('ERROR_IMAGE_IS_NOT_WRITEABLE', '����ͼƬ�޷�ɾ��');
define('ERROR_UNKNOWN_STATUS_FLAG', '���󣺲���״̬��־');

define('ERROR_GRAPHS_DIRECTORY_DOES_NOT_EXIST', '����Graphs Ŀ¼�����ڡ����� \'images\' Ŀ¼�´���һ�� \'graphs\' Ŀ¼��');
define('ERROR_GRAPHS_DIRECTORY_NOT_WRITEABLE', '����Graphs Ŀ¼�޷�д�롣');
?>