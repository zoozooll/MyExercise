<?php
/*
  $Id: backup.php,v 1.16 2002/03/16 21:30:02 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '数据库备份管理器');

define('TABLE_HEADING_TITLE', '备份名称');
define('TABLE_HEADING_FILE_DATE', '日期');
define('TABLE_HEADING_FILE_SIZE', '文件大小');
define('TABLE_HEADING_ACTION', '操作');

define('TEXT_INFO_HEADING_NEW_BACKUP', '新备份');
define('TEXT_INFO_HEADING_RESTORE_LOCAL', '恢复本地数据库');
define('TEXT_INFO_NEW_BACKUP', '数据库备份可能需要数分钟，请勿中断。');
define('TEXT_INFO_UNPACK', '<br><br>(将文件从档案中解压缩出来后)');
define('TEXT_INFO_RESTORE', '恢复数据库时请勿中断。<br><br>要恢复的数据库越大，需要时间越长！<br><br>可能的话，请使用mysql 客户端程序<br><br>例如：<br><br><b>mysql -h' . DB_SERVER . ' -u' . DB_SERVER_USERNAME . ' -p ' . DB_DATABASE . ' < %s </b> %s');
define('TEXT_INFO_RESTORE_LOCAL', '恢复数据库时请勿中断。<br><br>要恢复的数据库越大，需要时间越长！');
define('TEXT_INFO_RESTORE_LOCAL_RAW_FILE', '。上载文件必须是纯sql（文本）文件。');
define('TEXT_INFO_DATE', '日期：');
define('TEXT_INFO_SIZE', '大小：');
define('TEXT_INFO_COMPRESSION', '压缩：');
define('TEXT_INFO_USE_GZIP', '使用 GZIP');
define('TEXT_INFO_USE_ZIP', '使用 ZIP');
define('TEXT_INFO_USE_NO_COMPRESSION', '不压缩 (纯 SQL)');
define('TEXT_INFO_DOWNLOAD_ONLY', '只下载 (不存于服务器端)');
define('TEXT_INFO_BEST_THROUGH_HTTPS', '最好通过HTTPS传送');
define('TEXT_DELETE_INTRO', '你确定要删除该备份？');
define('TEXT_NO_EXTENSION', '无');
define('TEXT_BACKUP_DIRECTORY', '备份目录：');
define('TEXT_LAST_RESTORATION', '上次恢复时间：');
define('TEXT_FORGET', '(<u>忘记</u>)');

define('ERROR_BACKUP_DIRECTORY_DOES_NOT_EXIST', '错误：备份文件夹不存在。请在configure.php中设定。');
define('ERROR_BACKUP_DIRECTORY_NOT_WRITEABLE', '错误：备份文件夹无法写入。');
define('ERROR_DOWNLOAD_LINK_NOT_ACCEPTABLE', '错误：下载链接有问题。');

define('SUCCESS_LAST_RESTORE_CLEARED', '完成：上次恢复日期已被清除。');
define('SUCCESS_DATABASE_SAVED', '完成：数据库已存盘。');
define('SUCCESS_DATABASE_RESTORED', '完成：数据库已恢复。');
define('SUCCESS_BACKUP_DELETED', '完成：备份已清除。');
?>