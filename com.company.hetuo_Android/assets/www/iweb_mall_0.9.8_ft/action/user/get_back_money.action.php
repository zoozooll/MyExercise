<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require 'foundation/module_users.php';

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_order_info = $tablePreStr."order_info";

// 处理post变量

//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();
$order_id = intval(get_args("order_id"));
$sql = "SELECT pay_id,pay_name,order_status,pay_status,transport_status,pay_time FROM $t_order_info WHERE order_id='$order_id'";
$order_info = $dbo->getRow($sql);
if (is_array($order_info)) {
	//判断订单状态
	if ($order_info['pay_status']==1&&$order_info['order_status']>0&&$order_info['order_status']<3&&time()>(strtotime($order_info['pay_time'])+24*3600)) {
		dbtarget('r',$dbServs);
		$dbo=new dbex();
		$now = date("Y-m-d H:i:s");
		$sql="UPDATE $t_order_info SET get_back_time='$now' WHERE order_id='$order_id'";
		if ($dbo->exeUpdate($sql)) {
			if ($order_info['pay_name']=='支付宝纯担保') {
				echo "<script language=\"JavaScript\" type=\"text/javascript\">window.open ('http://www.alipay.com');</script>";
			}elseif ($order_info['pay_name']=='财付通即时到帐'||$order_info['pay_name']=='财付通中介担保') {
				echo "<script language=\"JavaScript\" type=\"text/javascript\">window.open ('http://www.tenpay.com');</script>";
			}else{
				action_return(0,"不符合退款要求！","modules.php?app=user_my_order");
				exit;
			}
			action_return(1,"退款成功！","modules.php?app=user_my_order");
		}
	}else{
		action_return(0,"不符合退款要求！","modules.php?app=user_my_order");
	}
}
?>
