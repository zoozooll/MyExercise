<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* post 数据处理 */
$v = intval(get_args('v'));

if(!$v) {
	exit("-1");
}

require("foundation/module_attr.php");

//数据表定义区
$t_attribute = $tablePreStr."attribute";

//定义写操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$attribute_info = get_attribute_info($dbo,$t_attribute,$v);
if($attribute_info) {
	echo json_encode($attribute_info);
} else {
	exit("-1");
}
?>