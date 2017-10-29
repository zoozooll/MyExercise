<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_order.php");

//语言包引入
$m_langpackage=new moduleslp;

//定义文件表
$t_order_info = $tablePreStr."order_info";

// 处理post变量
$order_id = intval(get_args('order_id'));

dbtarget('r',$dbServs);
$dbo=new dbex();

$order_info = get_order_info($dbo,$t_order_info,$order_id,0,$shop_id);
if(!$order_info) {
	action_return(0,$m_langpackage->m_noex_thisorder,'-1');
}
if($order_info['order_status']=='0') {
	action_return(0,$m_langpackage->m_order_cancel,'-1');
}

$price = floatval(get_args('price'));
if($price==$order_info['order_amount']) {
	action_return(1,'','modules.php?app=shop_order_view&order_id='.$order_id);
}

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$sql = "update `$t_order_info` set order_amount='$price' where order_id='$order_id' and shop_id='$shop_id'";

if($dbo->exeUpdate($sql)) {
	action_return(1,'','modules.php?app=shop_order_view&order_id='.$order_id);
} else {
	action_return(0,$m_langpackage->m_price_editfail,'-1');
}
?>