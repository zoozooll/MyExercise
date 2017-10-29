<?php
/*
  $Id: file_manager.php,v 1.13 2002/08/19 01:45:58 hpdl Exp $

  osCommerce, Open Source E-Commerce Solutions
  http://www.oscommerce.com

  Copyright (c) 2002 osCommerce

  Released under the GNU General Public License
*/

define('HEADING_TITLE', '文件管理器');

define('TABLE_HEADING_FILENAME', '文件名');
define('TABLE_HEADING_SIZE', '文件大小');
define('TABLE_HEADING_PERMISSIONS', '访问权限');
define('TABLE_HEADING_USER', '用户');
define('TABLE_HEADING_GROUP', '群组');
define('TABLE_HEADING_LAST_MODIFIED', '上次修改时间');
define('TABLE_HEADING_ACTION', '操作');

define('TEXT_INFO_HEADING_UPLOAD', '上载');
define('TEXT_FILE_NAME', '文件名：');
define('TEXT_FILE_SIZE', '文件大小：');
define('TEXT_FILE_CONTENTS', '内容：');
define('TEXT_LAST_MODIFIED', '上次修改时间：');
define('TEXT_NEW_FOLDER', '新文件夹');
define('TEXT_NEW_FOLDER_INTRO', '输入新文件夹名：');
define('TEXT_DELETE_INTRO', '你确定要删除该文件吗？');
define('TEXT_UPLOAD_INTRO', '请选择要上载的文件。');

define('ERROR_DIRECTORY_NOT_WRITEABLE', '错误：文件夹无法写入。 请给 %s 设定正确的用户访问权限');
define('ERROR_FILE_NOT_WRITEABLE', '错误：文件无法写入。 请给 %s 设定正确的用户访问权限');
define('ERROR_DIRECTORY_NOT_REMOVEABLE', '错误：文件夹无法删除。 请给 %s 设定正确的用户访问权限');
define('ERROR_FILE_NOT_REMOVEABLE', '错误：文件无法删除。 请给 %s 设定正确的用户访问权限');

define('ERROR_DIRECTORY_DOES_NOT_EXIST', '错误：文件夹 %s 不存在');
?>
