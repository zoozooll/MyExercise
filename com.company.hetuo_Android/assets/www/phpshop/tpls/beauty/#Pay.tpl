<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<style type="text/css">
<!--
#ordersuccess{ color:#FF0000; border:1px solid #93D900; padding:5px; }
#ordersuccess{margin:0px 0px 5px 0px; border:1px solid #7ECEF4; background-color:#F2FAFE; font-size:14px; color:#FF0000; text-align:left;}
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
	<div id="locate"><span id="topnav"><a href="./">首页</a></span> </div>
{if tmpshop==NULL}
<div id="ordersuccess">
 &#8226; 未选购任何产品
</div>
<div id="ordersuccess">
 &#8226; <a href="./">点击这里返回首页</a>
</div>
{else}
	<div id="chanr">
	  <div id="divClass">柜台结帐</div> 
   </div>
   <div id="chanl" style="margin-top:5px;">
		<div id="ordersuccess">
		 &#8226; <b>您的详细结帐信息</b>（加＊号的为网上在线支付）&nbsp;&nbsp;&nbsp;&nbsp;<br>                                            
			&nbsp;&nbsp;(<font color="#ff0000">为提高服务品质本站大部分会在在每日15:00-16:00以电话进行确认
</font>)</font>
	 </div>{inc file=#Shop_Product_Detailed.tpl}
		<div id="orderpay">             
			<div id="ordermethod">
				<ul>
				<li id="pay">
				<font color="#FF0000">&#8226;请记住您的订单号：{$billnoinfo.billno}</font>
				</li>
				</ul>
				<div id="pay">＊ 您可以<font color="#FF0000">网上在线支付</font> 本网络代收机制受『128 bit SSL电子安全交易』保护，请放心购物
				</div>
				<ul id="webpay">
				   <li></li> 
				</ul>
				{if alipaylink!=''}<a href="{$alipaylink}" target="_blank"><img src="images/default/button_alipay1.gif" border="0" /></a><br /><br />{/if}
				{if tenpaylink!=''}<a href="{$tenpaylink}" target="_blank"><img src="images/default/button_pay.gif" /></a>{/if}
				<ul>
				<li id="pay">
				&#8226;您的邮寄信息：
				</li>
				<li><font color="#808080"> 
				领取人：{$billnoinfo.name}<br />付款方式：{$billnoinfo.paymethod}<br /> 邮寄地址：{$billnoinfo.address}<br /> 邮编：{$billnoinfo.postcode} <br />电话：{$billnoinfo.phone} {$billnoinfo.mobile} <br /> 邮寄方式：{$billnoinfo.freight}<br /> 备注：{$billnoinfo.ss} </font>
				</li>
				</ul>
			</div> 
			<div id="ordermethod">
				<ul>
				{$loop bank}
				{if bank.loop.type=='B'}
				{if bank.preve.type!='B'}<li id="pay">银行电汇</li>{/if}
				<li> 
				开户行：{$bank.loop.name}<br /><font color="#808080">收款人：{$bank.loop.payee}<br />账号：{$bank.loop.accounts} {$bank.loop.describes}</font>
				</li>
				{/if}
				{if bank.loop.type=='Y'}{if bank.preve.type!='Y'}<li id="pay">邮局汇款</li>{/if} 
				<li><font color="#808080"> 
				&#8226;收款人地址：{$bank.loop.name} 收款人：{$bank.loop.payee}邮编：{$bank.loop.accounts} {$bank.loop.describes}</font>
				</li>
				{/if}
				{/loop}
				</ul>
				<ul>
				<li>
				<font color="#808080">请在汇款附言中注明你的用户名、订单号以及订货内容，款到发货。本网站建议你在办理汇款后来E-MAIL告知，并附上汇款单的复印件，或者传真。</font>             
				</li>
				</ul>
				
			</div>
		</div>
			<div id="webinfo">
				<div id="payimg"></div>
				<div id="pay">
				  本网络代收机制受『128 bit SSL电子安全交易』保护，请放心购物        
				</div>
				  <a href="http://www.phpshop.cn/" target="_blank">phpshop.cn</a> 电子商务购物系统 您最放心的购物系统
			</div>
  </div>
{/if}
	<div class="cl"></div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>