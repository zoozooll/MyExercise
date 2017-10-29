<?php
/*
	*功能：付完款后跳转的页面
	*版本：2.0
	*日期：2008-08-01
	*修正日期：2010-4-22
	'说明：
	'以下代码只是方便商户测试，提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	'该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
*/

///////////页面功能说明///////////////
//该页面可在本机电脑测试
//该页面称作“返回页”，是同步被支付宝服务器所调用，可当作是支付完成后的提示信息页，如“您的某某某订单，多少金额已支付成功”。
//可放入HTML等美化页面的代码、订单交易完成后的数据库更新程序代码
//该页面调试工具可以使用PHP拥有的断点调试工具Zend Studio，也可以使用写文本函数log_result，该函数已被默认关闭，见alipay_notify.php中的函数return_verify
/////////////////////////////

require_once("alipay_notify.php");
require_once("alipay_config.php");
$alipay = new alipay_notify($partner,$security_code,$sign_type,$_input_charset,$transport);
$verify_result = $alipay->return_verify();

//获取支付宝的反馈参数
   $dingdan	= $_GET['out_trade_no'];	//获取支付宝传递过来的订单号
   $total	= $_GET['price'];			//获取支付宝传递过来的总价格  

if($verify_result) {    //认证合格
	if($_GET['trade_status'] == 'WAIT_SELLER_SEND_GOODS') //交易状态：买家已付款，等待卖家发货
	{
		//log_result("verify_success"); 
		
		//如果您申请了支付宝的购物卷功能，请在返回的信息里面不要做金额的判断，否则会出现校验通不过，出现订单无法更新。如果您需要获取买家所使用购物卷的金额,
		//请获取返回信息的这个字段discount的值，取绝对值，就是买家付款优惠的金额。即 原订单的总金额=买家付款返回的金额total_fee +|discount|.

		echo "支付成功<br>订单号是：".$dingdan."<br>订单金额是：".$total;
	}
	else
	{
		echo "fail";
	}
}
else {    //认证不合格
	echo "fail";
	//log_result ("verify_failed");
}
?>