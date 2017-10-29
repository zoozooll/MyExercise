<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_users.php");
require("foundation/module_areas.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_users = $tablePreStr."users";


$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$user_info = get_user_info($dbo,$t_users,$user_id);
if($user_info['user_name']) {
	echo "<script>location.href='modules.php?app=user_profile'</script>";
	exit();
}

?>