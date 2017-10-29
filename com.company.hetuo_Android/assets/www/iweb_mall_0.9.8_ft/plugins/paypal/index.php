<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
//require_once("alipay_service.php");

// 连接数据库 初始操作
$dbo = new dbex;
dbtarget('r',$dbServs);
$order_id = intval($_GET['id']);
$pay_id = intval($_GET['pay_id']);

if(!$order_id && !$pay_id) {exit("Error");}
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

// 获取订单信息
$orderinfo = get_order_info($dbo,$t_order_info,$order_id);
if(!$orderinfo) {exit("非法操作");}

// 获取支付配置信息
$sql = "SELECT * FROM $t_shop_payment WHERE shop_id='".$orderinfo['shop_id']."' and pay_id=$pay_id";
$row = $dbo->getRow($sql);
$payment_info = unserialize($row['pay_config']);

$paypal_id        = $payment_info['paypal_id'];
$paypal_money  = $payment_info['paypal_money'];
$pay_fee   = $payment_info['pay_fee'];

$order_amount=$orderinfo['order_amount'];
$payid=$orderinfo['payid'];

//$notify_url     = $baseUrl."plugins/paypal/notify_url.php";
$return_url     = $baseUrl."plugins/paypal/return_url.php";

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"> 
</head> 
<script language=javascript> 
setTimeout("document.form1.submit()",0) 
</script> 

<body>
<form name="form1" style="text-align:center;" action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type='hidden' name='cmd' value='_xclick'>
<input type='hidden' name='business' value='<?php echo $paypal_id?>'>
<input type='hidden' name='item_name' value='<?php echo $payid?>'>
<input type='hidden' name='amount' value='<?php echo $order_amount?>'>
<input type='hidden' name='currency_code' value='<?php echo $paypal_money?>'>
<input type='hidden' name='return' value='<?php echo $return_url?>'>
<input type='hidden' name='charset' value='utf-8'>
</form>
</body>
</html>