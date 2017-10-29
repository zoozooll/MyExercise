<?php
/*
	*功能：设置商品有关信息及物流信息
	*版本：2.0
	*日期：2008-08-01
	*修改日期：2010-04-22
	'说明：
	'以下代码只是方便商户测试，提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	'该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
*/

require_once("alipay_service.php");
require_once("alipay_config.php");

////////////页面功能说明///////////
//该页面是接口入口页面，生成支付时的URL
//该页面测试时出现“调试错误”请参考：http://club.alipay.com/read-htm-tid-8681712.html
//要传递的参数要么不允许为空，要么就不要出现在数组与隐藏控件或URL链接里
///////////////////////////////////

///////以下参数是需要通过下单时的订单数据传入进来获得//////////
$out_trade_no = date('Ymdhms');
$subject = "订单名称";
$body = "订单描述备注";
$price = "0.01";

$receive_name = "测试";
$receive_address = "测试收货地址";
$receive_zip = "123456";
$receive_phone = "0571-81234567";
$receive_mobile = "13312341234";

$logistics_fee_1 = "0.00";
$logistics_payment_1 = "BUYER_PAY";
$logistics_type_1 = "EMS";
	
$logistics_fee_2 = "0.00";
$logistics_payment_2 = "BUYER_PAY";
$logistics_type_2 = "POST";

$buyer_email = "abc@126.com";
////////////////////////////////////////////////////////////

$parameter = array(
/////////////////不会更改的必填参数/////////////////////////////
	"service"        => "create_partner_trade_by_buyer",  //交易类型 实物标准双接口服务名称：trade_create_by_buyer ； 中介担保交易（纯担保交易）服务名称：create_partner_trade_by_buyer
	"payment_type"   => "1",                      //默认为1,不需要修改
	"_input_charset" => $_input_charset,          //字符集，默认为GBK
	
/////////////////必填的参数////////////////////////////////////
	"partner"        => $partner,                 //合作身份者ID
	"seller_email"   => $seller_email,            //收款支付宝账号，一般是签约支付宝账号
	"out_trade_no"   => $out_trade_no,            //商家网站唯一订单号（保证商家网站订单系统的里的订单号唯一）
	"subject"        => $subject,                 //商品名称，订单主题
	"price"          => $price,                   //商品单价，订单总价（价格不能为0）
	"quantity"       => "1",                      //商品数量（用于商品单价时的数量）
	
	"logistics_fee"      => '0.00',               //物流配送费用
	"logistics_payment"  => 'BUYER_PAY',          //物流费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
	"logistics_type"     => 'EXPRESS',            //物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
	
/////////////////建议填写的重要的参数/////////////////////////
	"return_url"     => $return_url,              //同步返回,返回页路径地址
	"notify_url"     => $notify_url,              //异步返回，通知页路径地址
	"body"           => $body,                    //商品描述，订单备注

	"show_url"       => $show_url,                 //商品相关网站

////////////////其他有用的选填参数///////////////////////////
	//收货人信息，当至少含有这些参数中的receive_name、receive_address、receive_zip时，到达支付宝收银台以后的操作步骤中会减少设置收货人信息的一步
	"receive_name"	 => $receive_name,		   //收货人姓名
	"receive_address"=> $receive_address,	   //收货人地址
	"receive_zip"	 => $receive_zip,		   //收货人邮编
	"receive_phone"  => $receive_phone,		   //收货人联系电话
	"receive_mobile" => $receive_mobile,	   //收货人手机
	
	"buyer_email"	 => $buyer_email		   //买家支付宝账号
/*
	"logistics_fee_1"    => $logistics_fee_1,             //物流配送费用
	"logistics_payment_1"=> $logistics_payment_1,        //物流费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
	"logistics_type_1"   => $logistics_type_1,              //物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
	
	"logistics_fee_2"    => $logistics_fee_2,             //物流配送费用
	"logistics_payment_2"=> $logistics_payment_2,        //物流费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
	"logistics_type_2"   => $logistics_type_2             //物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
*/
);
$alipay = new alipay_service($parameter,$security_code,$sign_type);


//POST方式传递，得到加密结果字符串
$sign = $alipay->Get_Sign();


//若改成GET方式传递，请取消下面的两行注释
$link=$alipay->create_url();
/*echo "<script>window.location =\"$link\";</script>"; 
*/
?>
<html>
<head>
<title>支付宝担保接口</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body>
			<form name="alipaysubmit" method="post" action="https://www.alipay.com/cooperate/gateway.do?_input_charset=<?php echo $_input_charset?>">
				<input type=hidden name="service" value="create_partner_trade_by_buyer">
				<input type=hidden name="partner" value="<?php echo $partner?>">
				<input type=hidden name="return_url" value="<?php echo $return_url?>"> 
				<input type=hidden name="notify_url" value="<?php echo $notify_url?>">  
				<input type=hidden name="subject" value="<?php echo $subject?>"> 
				<input type=hidden name="body" value="<?php echo $body?>">
				<input type=hidden name="out_trade_no" value="<?php echo $out_trade_no?>">
				<input type=hidden name="price" value="<?php echo $price?>">
				<input type=hidden name="quantity" value="1">
				<input type=hidden name="payment_type" value="1">
				<input type=hidden name="show_url" value="<?php echo $show_url?>">
				<input type=hidden name="seller_email" value="<?php echo $seller_email?>">
				<input type=hidden name="logistics_fee" value="0.00">
				<input type=hidden name="logistics_payment" value="BUYER_PAY">
				<input type=hidden name="logistics_type" value="EXPRESS">
				<input type=hidden name="sign" value="<?php echo $sign?>">
				<input type=hidden name="sign_type" value="MD5">
				
				<input type=hidden name="receive_name" value="<?php echo $receive_name?>">
				<input type=hidden name="receive_address" value="<?php echo $receive_address?>">
				<input type=hidden name="receive_zip" value="<?php echo $receive_zip?>">
				<input type=hidden name="receive_phone" value="<?php echo $receive_phone?>">
				<input type=hidden name="receive_mobile" value="<?php echo $receive_mobile?>">
				
				<input type=hidden name="buyer_email" value="<?php echo $buyer_email?>">
				<!--
				<input type=hidden name="logistics_fee_1" value="<?php echo $logistics_fee_1?>">
				<input type=hidden name="logistics_payment_1" value="<?php echo $logistics_payment_1?>">
				<input type=hidden name="logistics_type_1" value="<?php echo $logistics_type_1?>">
				<input type=hidden name="logistics_fee_2" value="<?php echo $logistics_fee_2?>">
				<input type=hidden name="logistics_payment_2" value="<?php echo $logistics_payment_2?>">
				<input type=hidden name="logistics_type_2" value="<?php echo $logistics_type_2?>">
				 -->
			</form>
	<table>
		<tr><td>
			<input type="button" name="v_action" value="支付宝网上安全即时支付平台" onClick="alipaysubmit.submit()">
		</td></tr>
	</table>
</body>
</html>
