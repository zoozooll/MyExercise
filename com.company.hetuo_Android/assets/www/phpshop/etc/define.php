<?php
if( !defined('DEFINE_PHP') ){
define('DEFINE_PHP','YES');
/// physical path ///
define('__ROOT_PATH',substr(dirname(__FILE__), 0, -3));
//
define('__KEY','ebc7749a2dc1aba5cb4550a827bf63ff');
/// db connection ///
define('DB_TYPE','mysql');
/** database host */
define('DB_HOST','localhost');
/** database name */
define('DB_NAME','phpshop');
/** database db table*/
define('DB_TABLE', 'phpshop_');
/** database user name */
define('DB_USER','root');
/** database password */
define('DB_PASSWORD','123456');

//web style
define('__SETTING_STYLE','default/');
//serialnumber
define('__SERIAL_NUMBER','B07D2-0AD60-7DB6A-9E4CB-59F37-40CBA');
}
require_once(__ROOT_PATH . "/functions/func.Common.php");
?>