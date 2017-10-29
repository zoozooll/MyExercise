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
$t_user_address = $tablePreStr."user_address";
$t_areas = $tablePreStr."areas";

//变量定义区
$address_id=intval(get_args('address_id'));

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$sql="select * from $t_user_address where user_id=$user_id";
//echo $sql;
$address_rs=$dbo->getRs($sql);

$user_info['to_user_name'] = '';
$user_info['full_address'] = '';
$user_info['email'] = '';
$user_info['mobile'] = '';
$user_info['telphone'] = '';
$user_info['zipcode'] = '';
$user_info['user_province'] = 0;
$user_info['user_city'] = 0;
//$user_info['user_country'] = 0;

if($address_id && $address_rs){
	foreach($address_rs as $value) {
		if($address_id == $value['address_id']) {
			$user_info = $value;
		}
	}
}
// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = isset($user_info['user_country']) ? $user_info['user_country'] : 1;

$areas_info = get_areas_info($dbo,$t_areas);

?>