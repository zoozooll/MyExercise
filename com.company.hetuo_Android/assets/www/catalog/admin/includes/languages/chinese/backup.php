<?php
/*
  $Id: backup.php,v 1.16 2002/03/16 21:30:02 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '���ݿⱸ�ݹ�����');

define('TABLE_HEADING_TITLE', '��������');
define('TABLE_HEADING_FILE_DATE', '����');
define('TABLE_HEADING_FILE_SIZE', '�ļ���С');
define('TABLE_HEADING_ACTION', '����');

define('TEXT_INFO_HEADING_NEW_BACKUP', '�±���');
define('TEXT_INFO_HEADING_RESTORE_LOCAL', '�ָ��������ݿ�');
define('TEXT_INFO_NEW_BACKUP', '���ݿⱸ�ݿ�����Ҫ�����ӣ������жϡ�');
define('TEXT_INFO_UNPACK', '<br><br>(���ļ��ӵ����н�ѹ��������)');
define('TEXT_INFO_RESTORE', '�ָ����ݿ�ʱ�����жϡ�<br><br>Ҫ�ָ������ݿ�Խ����Ҫʱ��Խ����<br><br>���ܵĻ�����ʹ��mysql �ͻ��˳���<br><br>���磺<br><br><b>mysql -h' . DB_SERVER . ' -u' . DB_SERVER_USERNAME . ' -p ' . DB_DATABASE . ' < %s </b> %s');
define('TEXT_INFO_RESTORE_LOCAL', '�ָ����ݿ�ʱ�����жϡ�<br><br>Ҫ�ָ������ݿ�Խ����Ҫʱ��Խ����');
define('TEXT_INFO_RESTORE_LOCAL_RAW_FILE', '�������ļ������Ǵ�sql���ı����ļ���');
define('TEXT_INFO_DATE', '���ڣ�');
define('TEXT_INFO_SIZE', '��С��');
define('TEXT_INFO_COMPRESSION', 'ѹ����');
define('TEXT_INFO_USE_GZIP', 'ʹ�� GZIP');
define('TEXT_INFO_USE_ZIP', 'ʹ�� ZIP');
define('TEXT_INFO_USE_NO_COMPRESSION', '��ѹ�� (�� SQL)');
define('TEXT_INFO_DOWNLOAD_ONLY', 'ֻ���� (�����ڷ�������)');
define('TEXT_INFO_BEST_THROUGH_HTTPS', '���ͨ��HTTPS����');
define('TEXT_DELETE_INTRO', '��ȷ��Ҫɾ���ñ��ݣ�');
define('TEXT_NO_EXTENSION', '��');
define('TEXT_BACKUP_DIRECTORY', '����Ŀ¼��');
define('TEXT_LAST_RESTORATION', '�ϴλָ�ʱ�䣺');
define('TEXT_FORGET', '(<u>����</u>)');

define('ERROR_BACKUP_DIRECTORY_DOES_NOT_EXIST', '���󣺱����ļ��в����ڡ�����configure.php���趨��');
define('ERROR_BACKUP_DIRECTORY_NOT_WRITEABLE', '���󣺱����ļ����޷�д�롣');
define('ERROR_DOWNLOAD_LINK_NOT_ACCEPTABLE', '�����������������⡣');

define('SUCCESS_LAST_RESTORE_CLEARED', '��ɣ��ϴλָ������ѱ������');
define('SUCCESS_DATABASE_SAVED', '��ɣ����ݿ��Ѵ��̡�');
define('SUCCESS_DATABASE_RESTORED', '��ɣ����ݿ��ѻָ���');
define('SUCCESS_BACKUP_DELETED', '��ɣ������������');
?>