<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<script language="javascript">
function PageValidator() {
	var CustomerInfo = document.CustomerInfo;
	var isfreight = false;
	for(i=0;i<CustomerInfo.freight.length;i++) { 
		if(CustomerInfo.freight[i].checked) {
			isfreight = true;
			break;
		}   
	} 
	if(isfreight == false) {
		alert('配送方式没有选择，请选择配送方式！');
		if(CustomerInfo.freight.length > 0) CustomerInfo.freight[0].focus();
		return false;
	}
	var ispaymethod = false;
	var w = false;
	for(i=0;i<CustomerInfo.paymethod.length;i++) { 
		if(CustomerInfo.paymethod[i].checked) {
			ispaymethod = true;
			if(CustomerInfo.paymethod[i].value=='W') w = true;
			break;
		}   
	} 
	if(ispaymethod == false) {
		alert('付款方式没有选择，请选择付款方式！');
		if(CustomerInfo.paymethod.length > 0) CustomerInfo.paymethod[0].focus();
		return false;
	}
	if(w == true) {
		w = false;
		for(i=0;i<CustomerInfo.bank_type.length;i++) { 
			if(CustomerInfo.bank_type[i].checked) {
				w = true;
				break;
			}   
		} 
		if(w == false) {
			alert('支付方式没有选择，请选择支付方式！');
			if(CustomerInfo.bank_type.length > 0) CustomerInfo.bank_type[0].focus();
			return false;
		}
	}
	if(CustomerInfo.name.value == '') {
		alert('收件人姓名没有填写！');
		CustomerInfo.name.focus();
		return false;
	}
	if(CustomerInfo.address.value == '') {
		alert('邮寄地址没有填写！');
		CustomerInfo.address.focus();
		return false;
	}
	if(CustomerInfo.tel.value == '') {
		alert('联系电话没有填写！');
		CustomerInfo.tel.focus();
		return false;
	}
	if(CustomerInfo.tel.value != '') {
		var patrn=/^([0-9\-\.\s\(\)]+){5,22}$/i; 
		if (!patrn.exec(CustomerInfo.tel.value)) {
			alert('填入的联系电话不正确！');
			CustomerInfo.tel.focus();
			return false;
		}
	} 
	if(CustomerInfo.mail.value == '') {
		alert('E-Mail没有填写！');
		CustomerInfo.mail.focus();
		return false;
	}
	if(CustomerInfo.mail.value != '') {
		var patrn=/^(.+?)@([a-z0-9\.]+)\.([a-z]){2,5}$/i; 
		if (!patrn.exec(CustomerInfo.mail.value)) {
			alert('填入的E-Mail不正确！');
			CustomerInfo.mail.focus();
			return false;
		}
	} 
}
</script>
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
<div id="ps"><span id="topnav"><a href="./">首页</a></span> <span id="navuser">{if userinfo==''}<a href="register.php">注册</a> <a href="login.php">登陆</a> {/if}<a href="myhome.php">用户中心</a> <a href="buy.php">柜台结帐</a></span></div>
{if tmpshop==NULL}
<div id="ordersuccess">
 &#8226; 未选购任何产品
</div>
<div id="ordersuccess">
 &#8226; <a href="#close" onClick="window.close();return false;">点击这里关闭窗口</a>
</div>
{else}
	<div id="chanr">
	  <div id="divClass">{$channelinfo.name}商品分类</div> 
	  <div id="divClassUB"><a href="{$channel.loop.url}" title="{$channel.loop.name}">{$channel.loop.name}</a></div>
			 <div id="chanb"> 
				    <div id="divClassul" class="chanp">
						<ul><li><a href="{$channel.loop.url}" title="{$channel.loop.name}">{$channel.loop.name}</a></li></ul>
						<div class="cl"></div>
			   		</div>
			 </div>
   </div>
   <div id="chanl">
		<div id="ordersuccess">
		 &#8226; 订购清单如下:
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
			<td width=600 height="28" align="center"><font size="2">删除</font></td>
		  </TR>
		  {$loop tmpshop}
		  <TR bgcolor="#EAF7EC" align="center"> 
			<TD width="600" height="1" ><img src="{$tmpshop.loop.m_pic}" width="40" onerror="this.src='product/nopic.gif'" ></td>
			<TD width="600" height="1" align="center">{$tmpshop.loop.name}</td>
			<TD width="600" height="1" >{$tmpshop.loop.number}</td>
			<TD width="600" height="1" > 
			  <input type="hidden" name="pid[]" value="{$tmpshop.loop.pid}">
			  <input type="text" name="num[]" value="{$tmpshop.loop.num}" size="3" maxlength="5">	</td>
			<TD width="600" height="1" >{$tmpshop.loop.price}</td>
			<TD width="600" height="1" >{$tmpshop.loop.allprice}</td>
			<td width="600" height="1" ><a href="shop.php?switch=iframeshop&pid={$tmpshop.loop.pid}&act=del" target="UserShop_iframe">删除</a></td>
		  </TR>
		  {/loop}
		  <tr bgcolor="#EAF7EC"> 
			<td colspan="8" width="600" height="28" align="right">
			  
		合计:{$allprice}+{$freight}(消费未满100元加运费{$freight}元)=<span style="background-color: #FFFF00">总价￥<font color=red>{$sellprice}</font>(元)</span>&nbsp;&nbsp;&nbsp;	</td>
		  </tr>
		</table>      
		</td>
		  </tr>
		</table>  
		<form method="post" action="shop.php?switch=sum"  onsubmit="return PageValidator()" NAME="CustomerInfo" >			
		<div id="orderpay">             
				<div id="paymethod">配送方式</div>
				<div id="ordermethod">
				<ul>
				{$loop freight}
				<li id="pay">
				<input type="radio" value="{$freight.loop.id}" id="freight" name="freight" > {$freight.loop.name} 价格：{$freight.loop.freight}元 {$freight.loop.describes}
				</li>   
				{/loop} 
				</ul>
				<!--<input type="radio" value="国际航空包裹配送" id=num name="num" onClick="javascript:document.updownform.submit();" >国际航空包裹配送(7-15个工作日)
																				 
				-->
			</div>
			<div id="paymethod">付款方式</div>
			<div id="ordermethod">
				<ul>
				{$loop bank}
				{if bank.loop.type=='B'}
				{if bank.preve.type!='B'}<li id="pay">
				<input type="radio" value="B" id="B" name="paymethod" >银行电汇
				</li>{/if}
				<li> 
				开户行：{$bank.loop.name}<br /><font color="#808080">收款人：{$bank.loop.payee}<br />账号：{$bank.loop.accounts} {$bank.loop.describes}</font>
				</li>
				{/if}
				{if bank.loop.type=='Y'}{if bank.preve.type!='Y'}
				<li id="pay">
				<input type="radio" value="Y" id="Y" name="paymethod" >邮局汇款
				</li>{/if} 
				<li><font color="#808080"> 
				&#8226;收款人地址：{$bank.loop.name} 收款人：{$bank.loop.payee}邮编：{$bank.loop.accounts} {$bank.loop.describes}</font>
				</li>
				{/if}
				{/loop}
				</ul>
				{if isusepay=='1'}
				<div id="pay"><input type="radio" value="W" id="W" name="paymethod" onClick="displayDiv('webpay');" ><font color="#FF0000">网上在线支付</font> 本网络代收机制受『128 bit SSL电子安全交易』保护，请放心购物
				</div>
				
				  <ul id="webpay" style="display:none;">
				  <li id="pay">
				  <font color="#FF0000">请选择支付方式：</font>
				  </li>
					<!--li>1) 人民币支付 </li-->
				   {if usealipay=='1'}
				   <li><input name="bank_type" type="radio" value="alipay" >支付宝支付(alipy.com)</li> 
				   {/if}  
				   {if usetenpay=='1'}
				   <li><input name="bank_type" type="radio" value="1001" >招商银行</li>   
				   <li><input name="bank_type" type="radio" value="1002" >中国工商银行</li>  
				   <li><input name="bank_type" type="radio" value="1003" >中国建设银行</li>  
				   <li><input name="bank_type" type="radio" value="1004" >上海浦东发展银行</li>   
				   <li><input name="bank_type" type="radio" value="1005" >中国农业银行</li>  
				   <li><input name="bank_type" type="radio" value="1006" >中国民生银行</li>  
				   <li><input name="bank_type" type="radio" value="1008" >深圳发展银行</li>   
				   <li><input name="bank_type" type="radio" value="1009" >兴业银行</li>  
				   <li><input name="bank_type" type="radio" value="1028" >广州银联(支持所有在中国的银行)</li>
				   <li><input name="bank_type" type="radio" value="0" >财付通通用支付页面(可选择银行)</li>
				   <li><input name="bank_type" type="radio" value="9999" >财付通帐户支付页面)</li>
				   {/if}
				</ul>
				{/if}
			</div> 
		</div>
		<div id="ordersuccess">
		 &#8226; <b>请填写您的详细信息</b>（加＊号的为必填项）&nbsp;&nbsp;&nbsp;&nbsp;<br>                                            
			&nbsp;&nbsp;(<font color="#ff0000">请确实留下您的联络电话，本站以电话进行订单确认</font>)</font>
		</div>    
		<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolordark="#ecf5ff" bordercolorlight="#9BBDE3" height="258">
		  <tr>
			<td width="100%" height="256">                     
			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="261">
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="31"><font size="2">&nbsp;&nbsp;                                                                                                                                                                                                                                      
				  * 收件人姓名:&nbsp;&nbsp;</font></td>                                                                                                                                                                                                                                    
				<td width="443" bgcolor="#ecf5ff" height="31"><font size="2"> 
				  <input size="27" name="name" value="" maxlength="20" >
				  <font color="red">*</font></font></td>                                          
			  </tr>
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="30"><font size="2">&nbsp;&nbsp;                                                                                                                                                                                                                                      
				  *邮寄地址:&nbsp;&nbsp;</font></td>
				<td width="443" bgcolor="#ecf5ff" height="30"><font size="2"> 
				  <input size="41" name="address" value="" maxlength="50">
				  <font color="red">*</font></font></td>                                          
			  </tr>
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="32"><font size="2">&nbsp;&nbsp;                                                                                                                                                                                                                                      
				  *联系电话:&nbsp;&nbsp;</font></td>
				<td bgcolor="#ecf5ff" height="32" width="443"><font size="2"> 
				  <input size="27" name="tel" value="" maxlength="20">
				  <font color="red">*</font></font> </td>                                          
			  </tr>
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="31"><font size="2">移动电话</font><font size="2">:&nbsp;&nbsp;</font></td>
				<td width="443" bgcolor="#ecf5ff" height="31"> 
				  <input name="mobile" size="27" maxlength="15" value="">
				</td>
			  </tr>
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="30"><font size="2"> 
				  * E-Mail:&nbsp;&nbsp;</font></td>                                                                                                                                                                                                                                    
				<td width="443" bgcolor="#ecf5ff" height="30"><font size="2"> 
				  <input size="30" name="mail" value="" maxlength="50">
				  </font><font color="red">*</font></td> 
			  </tr>
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="24"><font size="2">用户备注:&nbsp;&nbsp;</font></td>
				<td width="443" bgcolor="#ecf5ff" height="24"><font size="2">(请写上您的特殊要求）</font>
				</td>
			  </tr>
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="83">　</td>
				<td width="443" bgcolor="#ecf5ff" height="83"><TEXTAREA name="SS" cols=52 rows="3"></TEXTAREA>
				</td>
			  </tr>
			</table>
			</td>
		  </tr>
		</table>
			<INPUT style="FONT-SIZE: 9pt; COLOR: blue" type="submit" width="55" value="结帐送出" >
			<input style="FONT-SIZE: 9pt; COLOR: blue" type="button" value="回上页" onClick=" history.back(1);return false" id=button1 name=button1>
			<input style="FONT-SIZE: 9pt; COLOR: blue" onClick="history.back(1);return false;" type="button" value="继续采购" id=button2 name=button2>
			<input id="closewindow" name="closewindow" onClick="parent.TB_remove();" type="button" value="关闭窗口">
			</form>                       
			
		
			<div id="webinfo">
				<div id="payimg"></div>
				<div id="pay">
				  本网络代收机制受『128 bit SSL电子安全交易』保护，请放心购物        
				</div>
				  夜乐 Shoping电子商务购物网站联盟
			</div>
		</div>
{/if}
	<div class="cl"></div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>