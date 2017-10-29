<?php
/* 公共包含文件 */
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//数据库配置及连接文件
require_once($webRoot.$baseLibsPath."conf/dbconf.php");
require_once($webRoot.$baseLibsPath."fdbtarget.php");
require_once($webRoot.$baseLibsPath."libs_inc.php");

//库支持文件
//表操作类
require_once($webRoot.$baseLibsPath."cdbex.class.php");
//过滤函数
require_once($webRoot."foundation/freq_filter.php");

require_once($webRoot."foundation/fsqlitem_set.php");
?>