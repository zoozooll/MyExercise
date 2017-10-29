<?php
$IWEB_SHOP_IN = true;

require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require("../../cache/setting.php");

// 连接数据库 初始操作
$dbo = new dbex;
dbtarget('r',$dbServs);
$order_id = intval($_GET['id']);
$pay_id = intval($_GET['pay_id']);

$t_order_info = $tablePreStr."order_info";
$t_payment = $tablePreStr."payment";

$sql = "SELECT * FROM $t_payment WHERE pay_id=$pay_id";
$shop_pay = $dbo->getRow($sql);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>银行支付</title>
<link rel="stylesheet" type="text/css" href="../../skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
</head>
<body>
<div class="main">
	<div class="right_operate">
		<form action="../../do.php?act=user_pay_message" method="post">
		<table width="98%">
		<tr><td>支付简介：</td><td><?php echo $shop_pay['pay_desc']?></td></tr>
		<tr><td>支付信息</td><td><textarea name="pay_message" rows="7" cols="50" wrap="off"></textarea>
		<input type="hidden" name="id" value="<?php echo $order_id;?>"/></td></tr>
		<tr><td></td><td><input type="submit" name="name" value="提交"/></td></tr>
		</table>
		<input type="hidden" name="pay_id" value="<?php echo $pay_id?>"/>
		<input type="hidden" name="pay_name" value="<?php echo $shop_pay['pay_name']?>"/>
		</form>
	</div>
	<div class="clear"></div>
</div>
</body>
</html>