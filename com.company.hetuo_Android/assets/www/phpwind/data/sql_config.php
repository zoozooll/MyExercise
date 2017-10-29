<?php
/**
* 数据库相关信息设置
* 具体数值，请联系您的主机商,询问具体的数据库相关信息
*/
	// 数据库 主机名 或 IP 地址，如数据库端口不是3306，请在 主机名 或 IP 地址后添加“:具体端口”，如您的主机是localhost，端口是3307，则更改为“localhost:3307”
$dbhost = 'localhost';

	// 数据库用户名和密码，连接和访问 MySQL 数据库时所需的用户名和密码，不推荐使用空的数据库密码。
$dbuser = 'root';
$dbpw = '123456';

	// 数据库名，论坛程序所使用的数据库名。
$dbname = 'phpwind';

	// 数据库类型，有效选项有 mysql 和 mysqli，自pwforums v6.3.2起，引入了mysqli的支持，兼容性更好，效率性能更稳定，与mysql连接更稳定
	// 若服务器的配置是 PHP5.1.0或更高版本 和 MySQL4.1.3或更高版本，可以尝试使用 mysqli。
$database = 'mysqli';

	// 表区分符，用于区分每一套程序的符号
$PW = 'pw_';

	// 是否持久连接，暂不支持mysqli
$pconnect = '0';

/**
* Mysql编码设置(常用编码：gbk、big5、utf8、latin1)
* 如果您的论坛出现乱码现象，需要设置此项来修复
* 请不要随意更改此项，否则将可能导致论坛出现乱码现象
*/
$charset = 'utf8';

/**
* 创始人将拥有论坛的所有权限，自pwforums v6.3起支持多重创始人，用户名密码更改方法：
* 方法1、将./data/sql_config.php的文件属性设置为777（非NT服务器）或取消只读并将用户权限设置为Full Control（完全控制，NT服务器），
* 然后用原始创始人帐号登录后台，在更改论坛创始人处进行相关添加修改操作，
* 操作完毕后，再将./data/sql_config.php的文件属性设置为644（非NT服务器）或只读（NT服务器）。（推荐）
* 方法2、用记事本打开./data/sql_config.php文件，在“创始人用户名数组”中加入新的用户名，
* 如“$manager = array('admin');”更改为“$manager = array('admin','phpwind');”，在“创始人密码数组”中加入新的密码，
* 如“$manager_pwd = array('21232f297a57a5a743894a0e4a801fc3');”
* 更改为“$manager_pwd = array('21232f297a57a5a743894a0e4a801fc3','21232f297a57a5a743894a0e4a801fc3');”，
* 其中“21232f297a57a5a743894a0e4a801fc3”是密码为admin的md5的加密串，您可以创建一个新的文件在根目录（test.php），
* 文件内容为 "<?php echo md5('您的密码');?>" ，在地址栏输入http://你的论坛/test.php获得md5加密后的密码，用完记得删除文件test.php。
*/
	// 创始人用户名数组
$manager = array('kangkang');

	// 创始人密码数组
$manager_pwd = array('5f23af2b29d27c3d65f30ce17da673c7');

/**
* 镜像站点设置，默认为1，代表是主站点
*/
$db_hostweb = '1';

/**
* 附件url地址，以http:// 开头的绝对地址，为空使用默认
*/
$attach_url = array();
?>