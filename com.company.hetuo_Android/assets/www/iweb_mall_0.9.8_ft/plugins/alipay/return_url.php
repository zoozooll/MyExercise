<?php
/*
	*功能：付完款后跳转的页面
	*版本：2.0
	*日期：2008-08-01
	'说明：
	'以下代码只是方便商户测试，提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	'该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
*/
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require_once("../../foundation/ctime.class.php");
require_once("alipay_notify.php");

$dbo = new dbex;
dbtarget('r',$dbServs);
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

//获取支付宝的反馈参数
$dingdan    = $_GET['out_trade_no'];   //获取订单号
$total_fee  = $_GET['total_fee'];      //获取总价格
$receive_name    = $_GET['receive_name'];    //获取收货人姓名
$receive_address = $_GET['receive_address']; //获取收货人地址
$receive_zip     = $_GET['receive_zip'];     //获取收货人邮编
$receive_phone   = $_GET['receive_phone'];   //获取收货人电话
$receive_mobile  = $_GET['receive_mobile'];  //获取收货人手机


// 获取订单信息
$orderinfo = get_order_info_bypayid($dbo,$t_order_info,$dingdan);

if(!$orderinfo) {exit("非法操作");}

// 获取支付配置信息
$sql = "SELECT * FROM $t_shop_payment WHERE shop_id='".$orderinfo['shop_id']."' and pay_id=1";

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

// 返回信息验证
$alipay = new alipay_notify($partner,$security_code,$sign_type,$_input_charset,$transport);
$verify_result = $alipay->return_verify();

if($verify_result) {						//认证合格
	$pay_time = $ctime->long_time();
	
	dbtarget('w',$dbServs);
	if($orderinfo) {
		$sql = "update $t_order_info set pay_id=1,pay_status=1,pay_time='$pay_time',trade_no='$trade_no' where payid='$dingdan'";
		$dbo->exeUpdate($sql);
	}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>支付成功</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<table style="border:1px solid #ccc; width:770px; margin:20px auto;">
	<tr class="center">
		<th height="75" colspan="6" style="background:#fcfded; font-size:12px;"><h1 style="font-family: '黑体'; margin:auto; width:70%; height:53px; font-size:26px; background:url(../../skin/default/images/payok.gif) 0 0px no-repeat; line-height:53px;">恭喜你，您此次的支付操作已成功</h1>
		<ul style="width:65%; text-align:left; margin:auto; ">
		<li>· 点击<a style="color:#900" href="../../modules.php?app=user_my_order">我的订单</a>，进入会员中心查看您的订单信息</li>
		<li>· 点击<a style="color:#900" href="../../modules.php?app=user_profile">个人信息</a>，进入会员中心查看您的个人资料</li>
		<li>· 如有疑问，请致电客服：<?php echo $SYSINFO['sys_kftelphone'];?></li>
		</ul>
		<br />
		</th>
	</tr>
</table>
</body>
</html>
<?php
} else {
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>支付成功</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<table style="border:1px solid #ccc; width:770px; margin:20px auto;">
	<tr class=center>
		<th height="75" colspan="6" style="background:#fcfded; font-size:12px;"><h1 style="font-family: '黑体'; margin:auto; width:70%; height:53px; font-size:26px; background:url(../../skin/default/images/payerror.gif) 0 0px no-repeat; line-height:53px;">很抱歉，您此次的支付操作失败</h1>
		<ul style="width:65%; text-align:left; margin:auto; "><li>· 点击<a style="color:#900" href="../../modules.php?app=user_my_order">我的订单</a>，进入会员中心查看您的订单信息</li>
		<li>· 点击<a style="color:#900" href="../../modules.php?app=user_profile">个人信息</a>，进入会员中心查看或修改您的个人资料</li> </li>
		<li>· 如有疑问，请致电客服：<?php echo $SYSINFO['sys_kftelphone'];?></li>
		</ul>
		<br />
		</th>
	</tr>
</table>
</body>
</html>
<?php
}

/* 日志消息,把支付宝反馈的参数记录下来 */
function  log_result($word) { 
	/*
	$fp = fopen("../../cache/log.txt","a");	
	flock($fp, LOCK_EX) ;
	fwrite($fp,$word."：执行日期：".strftime("%Y%m%d%H%I%S",time())."\t\n");
	flock($fp, LOCK_UN); 
	fclose($fp);
	*/
}

?>