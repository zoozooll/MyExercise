<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require("../../foundation/asession.php");
require_once("alipay_service.php");

$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

// 连接数据库 初始操作
$dbo = new dbex;
dbtarget('r',$dbServs);
$order_id = intval($_GET['id']);
if(!$order_id) {
	$order_id = intval($_POST['order_id']);
}

if(!$order_id) {exit("Error");}

// 获取订单信息
$orderinfo = get_order_info($dbo,$t_order_info,$order_id);
if(!$orderinfo) {exit("非法操作");}

// 获取支付配置信息
$sql = "SELECT * FROM $t_shop_payment WHERE shop_id='".$orderinfo['shop_id']."' and pay_id=".$orderinfo['pay_id'];
$row = $dbo->getRow($sql);
$payment_info = unserialize($row['pay_config']);
$partner        = $payment_info['partner'];
$security_code  = $payment_info['security_code'];
$seller_email   = $payment_info['seller_email'];
$_input_charset = "utf-8";
$sign_type      = "MD5";
$transport      = "http";
$notify_url     = $baseUrl."plugins/alipay/notify_url.php";
$return_url     = $baseUrl."plugins/alipay/return_url.php";
$show_url       = $baseUrl."goods.php?id=".$orderinfo['goods_id'];
unset($row);
unset($payment_info);

$shop_id = get_sess_shop_id();
if(!$shop_id) {exit("Error");}

if($_GET['case'] == "ok") {
	$parameter = array(
		"service"         => "send_goods_confirm_by_platform",  //接口类型
		"partner"         => $partner,           //合作商户号
		"_input_charset"  => $_input_charset,    //字符集，默认为GBK
		"trade_no"        => $_POST['TxtTrade_no'], //支付宝交易号
		"logistics_name"  => $_POST['TxtLogistics_name'], //物流公司名称
		"invoice_no"      => $_POST['TxtInvoice_no'], //物流单号
		"transport_type"  => $_POST['DDLTransport_type'], //发货类型，POST（平邮），EXPRESS（快递），EMS（EMS）
	);
	
	$shipping_time = $ctime->long_time();
	$order_id = intval($_POST['order_id']);
	$sql = "update `$t_order_info` set transport_status=1,shipping_time='$shipping_time',shipping_name='".$parameter['logistics_name']."',shipping_no='".$parameter['invoice_no']."',shipping_type='".$parameter['transport_type']."' where order_id='$order_id' and shop_id='$shop_id'";
	$dbo->exeUpdate($sql);
	
	$alipay = new alipay_service($parameter,$security_code,$sign_type);
	$link = $alipay->create_url();

	header("Location: $link ");
} else {
	if(!$orderinfo['pay_status']) {
		exit("对方未付款！");
	}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>确认发货</title>
	<meta content="" name="keywords" />
	<meta content="" name="description" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
body,td {font-size:12px;}
table { border-collapse:collapse; border:solid #999; border-width:1px 0 0 1px; margin:5px auto;} 
table th, table td { border:solid #999; border-width:0 1px 1px 0; padding:2px; line-height:24px;} 
th {background:#eee;}
</style>
</head>
<body>
<form action="comput.php?case=ok" method="post" name="myform" >
    <table width="400" align="center">
	<tr>
		<th colspan="2" align="left">&nbsp;&nbsp; 确认发货</th>
	</tr>
	 <tr>
        <td width="100" align="right" class=title>支付宝交易号：</td>
        <td width="" class=title><?php echo $orderinfo['trade_no']; ?><input name="TxtTrade_no" type="hidden" id="TxtTrade_no" value="<?php echo $orderinfo['trade_no']; ?>" /></td>
	 </tr>
	 <tr>
        <td align="right" class=title>物流公司名称：</td>
        <td class=title><input name="TxtLogistics_name" type="text" id="TxtLogistics_name" /></td>
	 </tr>
     <tr>
        <td align="right" class=title>物流发货单号：</td>
        <td class=title><input name="TxtInvoice_no" type="text" id="TxtInvoice_no" /></td>
     </tr>
     <tr>
        <td align="right" class=title>发货运输类型：</td>
        <td class=title><select name="DDLTransport_type" id="DDLTransport_type">
          <option value="POST" selected="selected">POST</option>
          <option value="EXPRESS">EXPRESS</option>
          <option value="EMS">EMS</option>
        </select></td>
     </tr>
     <tr>
        <td colspan="2" align="center">
		<input type="hidden" name="order_id" value="<?php echo $order_id; ?>" />
		<input type="submit" name="Submit" value="确认发货" /></td>
	 </tr>
    </table>
</form>
</body>
</html>
<?php
}
?>