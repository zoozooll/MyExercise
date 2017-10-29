<?php
/* 公共包含文件 */
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//数据库配置及连接文件
require($webRoot.$baseLibsPath."conf/dbconf.php");
require($webRoot.$baseLibsPath."fdbtarget.php");
require($webRoot.$baseLibsPath."libs_inc.php");

//库支持文件
//表操作类
require($webRoot.$baseLibsPath."cdbex.class.php");
//过滤函数
require($webRoot."foundation/freq_filter.php");

require($webRoot."foundation/fsqlitem_set.php");
require($webRoot."foundation/ctime.class.php");
require($webRoot."foundation/fsession.php");
?>