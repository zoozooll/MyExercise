<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_order.php");
require("foundation/module_credit.php");

//语言包引入
$m_langpackage=new moduleslp;

//定义文件表
$t_order_info = $tablePreStr."order_info";
$t_credit = $tablePreStr."credit";

// 处理post变量
$order_id = intval(get_args('id'));


dbtarget('r',$dbServs);
$dbo=new dbex();

$order_info = get_order_info($dbo,$t_order_info,$order_id,$user_id);
if(!$order_info) {
	action_return(0,$m_langpackage->m_noex_thisorder);
}
if($order_info['order_status']=='0') {
	action_return(0,$m_langpackage->m_order_cancel);
}

$receive_time = $ctime->long_time();


//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$post['order_id']=$order_id;
$post['goods_id']=$order_info['goods_id'];
$post['buyer']=$order_info['user_id'];
$post['seller']=$order_info['shop_id'];

$suc=insert_credit($dbo,$t_credit,$post);

$sql = "update `$t_order_info` set order_status=3,receive_time='$receive_time' where order_id='$order_id' and user_id='$user_id'";

if($dbo->exeUpdate($sql) && $suc) {
	action_return(1,$m_langpackage->m_sureyou_receive);
} else {
	action_return(0,$m_langpackage->m_sureyou_receivefail,'-1');
}
?>