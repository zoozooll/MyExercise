<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_users.php");

//语言包引入
//$u_langpackage=new userslp;

//定义文件表
$t_users = $tablePreStr."users";

// 处理post变量
$user_name = short_check(get_args('v'));

if(empty($user_name)) {
	echo 0;
	exit;
}

//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();
$sql = "select user_id from `$t_users` where user_name='$user_name'";
$result = $dbo->getRow($sql);
if($result) {
	echo 0;
} else {
	echo 1;
}
exit;
?>