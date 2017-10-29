<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<style type="text/css">
<!--
#ordersuccess{ color:#FF0000; border:1px solid #93D900; padding:5px; }
#ordersuccess{margin:0px 2px 5px 0px; border:1px solid #7ECEF4; background-color:#F2FAFE; font-size:14px; color:#FF0000; text-align:left;}
<!-- orderform -->
#ordertitle{ margin: 20px auto;}
#orderform {text-align:center;HEIGHT: 470px; margin:23px;}
#ordermethod{padding:20px;}
#paymethod{ background: url(images/default/gif_47_106.gif) no-repeat 0px 50%; height:50px; width:120px; padding-left:50px; padding-top:30px; font-size:14px; margin-left:0px; font-weight:bold;}
#orderpay {padding: 10px; margin:10px auto;border:1px solid #7ECEF4; background-color:#F2FAFE; line-height:20px; font-size:12px;text-align:left; }
#orderpay li{ margin-left:45px;}
#orderpay #pay{ font-size:12px; font-weight:bold;list-style-type: none; margin-top:10px; margin-left:5px; border:1px solid solid #CEECFB; padding:5px;}
#webinfo {padding: 10px; margin:25px auto; height:30px;border:1px solid #7ECEF4; background-color:#F2FAFE; line-height:20px; font-size:14px;}
-->
</style>
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
<div id="locate"><span id="topnav"><a href="./">首页</a> {if userinfo==''}&raquo; <a href="register.php">注册</a> &raquo; <a href="login.php">登陆</a> {/if}&raquo; <a href="myhome.php">用户中心</a> &raquo; <a href="buy.php">柜台结帐</a></span></div>
<div id="ordersuccess">
 &#8226; <a href="#close">订单号：{$billno} (支付状态)</a>
</div>
<div id="ordersuccess">
 &#8226; {if billnosuccess=='0'}抱歉，支付失败！{else}恭喜您，支付成功！{/if}
</div>

	<div class="cl"></div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>