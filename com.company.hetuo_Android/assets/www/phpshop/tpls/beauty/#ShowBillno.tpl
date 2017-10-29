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
	<div id="locate"><span id="topnav"><a href="./">首页</a></span> &raquo; <span id="navuser">{if userinfo==''}<a href="register.php">注册</a> &raquo; <a href="login.php">登陆</a> &raquo; {/if}<a href="myhome.php">用户中心</a> &raquo; <a href="buy.php">柜台结帐</a></span></div>
{if billnoinfo==NULL}
<div id="ordersuccess">
 &#8226; 未找到任何订单
</div>
<div id="ordersuccess">
 &#8226; <a href="./">点击这里返回首页</a>
</div>
{else}
	<div id="chanr">
	  <div id="divClass">查看订单</div>
   </div>
   <div id="chanl" style="margin-top:5px;">
		<div id="ordersuccess">
		 &#8226; <b>您的详细订单信息</b>&nbsp;&nbsp;&nbsp;&nbsp;                                          
			&nbsp;&nbsp;<!--(<font color="#ff0000">为提高服务品质本站大部分会在在每日15:00-16:00以电话进行确认
</font>)</font>-->
	 </div>
		<div id="orderpay">             
			<div id="ordermethod">
				<div id="order_info">
					<div id="ordersuccess">
						&#8226; 您的订单号：{$billnoinfo.billno} &nbsp; &nbsp; &#8226; 订购清单如下：
					</div> 
					<table border="1" width="100%" bordercolor="#CEE3D0" cellspacing="0" cellpadding="0" style="border-collapse:collapse; margin:0px; padding:0px;">
					  <tr>
						<td width="100%" align="center">    
					<table  width="100%" height="92" cellspacing="1" cellpadding="1" >
					  <TR BGCOLOR=#CEE3D0 > 
						<TD WIDTH=600 height="28" align="center"></td>
						<TD WIDTH=600 height="28" align="center"><font size="2">产&nbsp; 
						  品&nbsp; 名&nbsp; 称</font></td>
						<TD WIDTH=600 height="28" align="center"><font size="2">编&nbsp; 
						  号</font></TD>
						<TD WIDTH=600 height="28" align="center"><font size="2">数量</font></TD>
						<TD WIDTH=600 height="28" align="center"><font size="2">单价</font></TD>
						<TD WIDTH=600 height="28" align="center"><font size="2">总价</font></TD>
						<!--td width=600 height="28" align="center"><font size="2">删除</font></td-->
					  </TR>
					  {$loop tmpshop}
					  <TR bgcolor="#EAF7EC" align="center"> 
						<TD width="600" height="1" ><a href="{$weburl}{$tmpshop.loop.url}" target="_blank"><img src="{$weburl}{$tmpshop.loop.m_pic}" width="40" onerror="this.src='{$weburl}product/nopic.gif'" border="0" ></a></td>
						<TD width="600" height="1" align="center"><a href="{$weburl}{$tmpshop.loop.url}" target="_blank">{$tmpshop.loop.name}</a></td>
						<TD width="600" height="1" >{$tmpshop.loop.number}</td>
						<TD width="600" height="1" > 
						  <input type="hidden" name="pid[]" value="{$tmpshop.loop.pid}">
						  <input type="text" name="num[]" value="{$tmpshop.loop.num}" size="3" maxlength="5">						</td>
						<TD width="600" height="1" >{$tmpshop.loop.price}</td>
						<TD width="600" height="1" >{$tmpshop.loop.allprice}</td>
						<!--td width="600" height="1" ><a href="shop.php?switch=iframeshop&pid={$tmpshop.loop.pid}&act=del" target="UserShop_iframe">删除</a></td-->
					  </TR>
					  {/loop}
					  <tr bgcolor="#EAF7EC"> 
						<td colspan="8" width="600" height="28" align="right">
						  
					合计:<span style="background-color: #FFFF00">总价￥<font color=red>{$allprice}</font>(元)</span>&nbsp;&nbsp;&nbsp;	</td>
					  </tr>
					  <tr bgcolor="#EAF7EC">
					    <td colspan="8" height="28" align="right" width="600"><span style="background-color: #FFFF00">运费：￥<font color=red>{$billnoinfo.freight_price}</font> &nbsp;总支付金额:￥<font color=red>{$billnoinfo.pay_price}</font>(元)</span>&nbsp;&nbsp;&nbsp;	</td>
				      </tr>
					</table>      
					</td> 
					  </tr>
					</table>  
				</div>
				<li id="pay">
				&#8226;您的付款方式：{$billnoinfo.paymethod} {if billnoinfo.paymethod=='网上在线支付'}{if billnoinfo.pay_success=='1'}(支付成功){else}(未支付成功, 您可以再次进行网上在线支付)<br /><br /> 
                {if alipaylink!=''}<a href="{$alipaylink}" target="_blank"><img src="images/default/button_alipay1.gif" border="0" /></a><br /><br />{/if}
				{if tenpaylink!=''}<a href="{$tenpaylink}" target="_blank"><img src="images/default/button_pay.gif" /></a>{/if}
                {/if}{/if}
				</li>
				<li id="pay">
				&#8226;您的邮寄信息：
				</li>
				<li><font color="#808080"> 
				领取人：{$billnoinfo.name}</li><li>付款方式：{$billnoinfo.paymethod}</li><li> 邮寄地址：{$billnoinfo.address}</li><li> 邮编：{$billnoinfo.postcode} </li><li>电话：{$billnoinfo.phone}</li><li>手机：{$billnoinfo.mobile} </li><li> 邮寄方式：{$billnoinfo.freight}</li><li> 备注：{$billnoinfo.ss} </font>
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