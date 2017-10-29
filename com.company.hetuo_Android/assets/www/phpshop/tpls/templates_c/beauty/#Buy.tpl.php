<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
<script language="javascript">
function PageValidator() {
	document.CustomerInfo.orderinfo.value = document.getElementById('order_info').innerHTML;
	var CustomerInfo = document.CustomerInfo;
	var isfreight = false;
	if(CustomerInfo.freight.length==undefined) {
		if(CustomerInfo.freight.value != undefined && CustomerInfo.freight.checked == true) {
			isfreight = true;
		}
	} else {
		for(i=0;i<CustomerInfo.freight.length;i++) { 
			if(CustomerInfo.freight[i].checked) {
				isfreight = true;
				break;
			}   
		} 
	}
	if(isfreight == false) {
		alert('配送方式没有选择，请选择配送方式！');
		if(CustomerInfo.freight.length==undefined) {
			CustomerInfo.freight.focus();
		} else {
			if(CustomerInfo.freight.length > 0) CustomerInfo.freight[0].focus();
		}
		return false;
	}
	var ispaymethod = false;
	var w = false;
	if(CustomerInfo.paymethod.length==undefined) {
		if(CustomerInfo.paymethod.value != undefined && CustomerInfo.paymethod.checked == true) {
			ispaymethod = true;
			if(CustomerInfo.paymethod.value=='W') w = true;
		}
	} else {
		for(i=0;i<CustomerInfo.paymethod.length;i++) { 
			if(CustomerInfo.paymethod[i].checked) {
				ispaymethod = true;
				if(CustomerInfo.paymethod[i].value=='W') w = true;
				break;
			}   
		} 
	}
	if(ispaymethod == false) {
		alert('付款方式没有选择，请选择付款方式！');
		if(CustomerInfo.paymethod.length > 0) CustomerInfo.paymethod[0].focus();
		return false;
	}
	if(w == true) {
		w = false;
		if(CustomerInfo.bank_type.length!=undefined) {
			for(i=0;i<CustomerInfo.bank_type.length;i++) { 
				if(CustomerInfo.bank_type[i].checked) {
					w = true;
					break;
				}   
			}
		} else {
			w = CustomerInfo.bank_type.checked;
		}
		if(w == false) {
			alert('支付方式没有选择，请选择支付方式！');
			if(CustomerInfo.bank_type.length!=undefined) {
				CustomerInfo.bank_type[0].focus();
			} else {
				CustomerInfo.bank_type.focus();
			}
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
	if(CustomerInfo.phone.value == '') {
		alert('联系电话没有填写！');
		CustomerInfo.phone.focus();
		return false;
	}
	if(CustomerInfo.phone.value != '') {
		var patrn=/^([0-9\-\.\s\(\)]+){5,22}$/i; 
		if (!patrn.exec(CustomerInfo.phone.value)) {
			alert('填入的联系电话不正确！');
			CustomerInfo.phone.focus();
			return false;
		}
	} 
	if(CustomerInfo.email.value == '') {
		alert('E-Mail没有填写！');
		CustomerInfo.email.focus();
		return false;
	}
	if(CustomerInfo.email.value != '') {
		var patrn=/^(.+?)@([a-z0-9\.]+)\.([a-z]){2,5}$/i; 
		if (!patrn.exec(CustomerInfo.email.value)) {
			alert('填入的E-Mail不正确！');
			CustomerInfo.email.focus();
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
    <?php include("inc/#body_top.tpl.php") ?>
	<div id="locate"><span id="topnav"><a href="./">首页</a> &raquo; 柜台结帐</span> </div>
<?php if($this->__muant["tmpshop"]==NULL) { ?>
<div id="ordersuccess">
 &#8226; 未选购任何产品
</div>
<div id="ordersuccess">
 &#8226; <a href="./">点击这里返回首页</a>
</div>
<?php } else { ?>
	<div id="chanr">
	  <div class="mallLogin"><span class="welcome">柜台结帐</span></div> 
   </div>
   <div id="chanl" style="margin-top:5px;">
		<div id="order_info">
		<div id="ordersuccess">
		 &#8226; 订购清单如下:
		</div> 
		<?php include("#Shop_Product_Detailed.tpl.php") ?>  
		</div>
		<form method="post" action="checkout.php"  onsubmit="return PageValidator()" name="CustomerInfo" >			
		<div id="orderpay">             
				<div id="paymethod">配送方式</div>
				<div id="ordermethod">
				<ul>
				<?php $freightinum = count($this->__muant["freight"]); for($freighti = 0; $freighti<$freightinum; $freighti++) { ?>
				<li id="pay">
				<input type="radio" value="<?php echo $this->__muant["freight"]["$freighti"]["id"] ?>" id="freight" name="freight" > <?php echo $this->__muant["freight"]["$freighti"]["name"] ?> <?php if($this->__muant["freight"]["$freighti"]["freight"]>0) { ?><?php if($this->__muant["freight"]["$freighti"]["shop_price"]>0) { ?><?php if($this->__muant["allprice"]>=$this->__muant["freight"]["$freighti"]["shop_price"]) { ?>免运费<?php } else { ?>价格：<?php echo $this->__muant["freight"]["$freighti"]["freight"] ?>元<?php } ?><?php } else { ?>价格：<?php echo $this->__muant["freight"]["$freighti"]["freight"] ?>元<?php } ?><?php } else { ?>免运费<?php } ?> <?php echo $this->__muant["freight"]["$freighti"]["describes"] ?>
				</li>   
				<?php } ?> 
				</ul>
				<!--<input type="radio" value="国际航空包裹配送" id=num name="num" onClick="javascript:document.updownform.submit();" >国际航空包裹配送(7-15个工作日)
																				 
				-->
			</div>
			<div id="paymethod">付款方式</div>
			<div id="ordermethod">
				<ul>
				<?php $bankinum = count($this->__muant["bank"]); for($banki = 0; $banki<$bankinum; $banki++) { ?>
				<?php if($this->__muant["bank"]["$banki"]["type"]=='B') { ?>
				<?php if($this->__muant["bank"]["$banki"-1]["type"]!='B') { ?><li id="pay">
				<input type="radio" value="B" id="B" name="paymethod" >银行电汇
				</li><?php } ?>
				<li> 
				开户行：<?php echo $this->__muant["bank"]["$banki"]["name"] ?><br /><font color="#808080">收款人：<?php echo $this->__muant["bank"]["$banki"]["payee"] ?><br />账号：<?php echo $this->__muant["bank"]["$banki"]["accounts"] ?> <?php echo $this->__muant["bank"]["$banki"]["describes"] ?></font>
				</li>
				<?php } ?>
				<?php if($this->__muant["bank"]["$banki"]["type"]=='Y') { ?><?php if($this->__muant["bank"]["$banki"-1]["type"]!='Y') { ?>
				<li id="pay">
				<input type="radio" value="Y" id="Y" name="paymethod" >邮局汇款
				</li><?php } ?> 
				<li><font color="#808080"> 
				&#8226;收款人地址：<?php echo $this->__muant["bank"]["$banki"]["name"] ?> 收款人：<?php echo $this->__muant["bank"]["$banki"]["payee"] ?>邮编：<?php echo $this->__muant["bank"]["$banki"]["accounts"] ?> <?php echo $this->__muant["bank"]["$banki"]["describes"] ?></font>
				</li>
				<?php } ?>
				<?php } ?>
				</ul>
				<?php if($this->__muant["isusepay"]=='1') { ?>
				<div id="pay"><input type="radio" value="W" id="W" name="paymethod" onClick="displayDiv('webpay');" ><font color="#FF0000">网上在线支付</font> 本网络代收机制受『128 bit SSL电子安全交易』保护，请放心购物
				</div>
				
				  <ul id="webpay" style="display:none;">
				  <li id="pay">
				  <font color="#FF0000">请选择支付方式：</font>
				  </li>
					<!--li>1) 人民币支付 </li-->
				   <?php if($this->__muant["usealipay"]=='1') { ?>
				   <li><input name="bank_type" type="radio" value="alipay" ><img src="images/default/icon_alipay_16x16_v2.gif" />支付宝支付(alipy.com 可选择银行)</li> 
				   <?php } ?><br />
				   <?php if($this->__muant["usetenpay"]=='1') { ?>
				   <li><input name="bank_type" type="radio" value="0" ><img src="images/default/tenpay.gif" width="16" />财付通通用支付页面(可选择银行)</li>
				   <?php } ?>
				</ul>
				<?php } ?>
				
				<ul>
				   
				<!--li id="pay">  
					<input disabled type="radio" value="货到付款" id=num name="num2" onClick="javascript:document.updownform.submit();" 
					><font color=#FF0000>( 您订购未滿500元所以无法使用货到付款 )</font>货到付款
				
				</li-->
				
				<!--li id="pay">                                                                                                   
				<input type="radio" value="邮局汇款" id=num name="num2" onClick="javascript:document.updownform.submit();" >邮局汇款  
				</li>
				<li>
				<font color="#808080">请在汇款附言中注明你的用户名、订单号以及订货内容，款到发货。本网站建议你在办理汇款后来E-MAIL告知，并附上汇款单的复印件，或者传真至（）。&nbsp;<br>
				收款人： <br>
				地址： <br>
				邮政编码： </font>             
				</li>
				<li id="pay">
				<input type="radio" value="银行电汇" id=num name="num2" onClick="javascript:document.updownform.submit();" >银行电汇  
				</li>
				<li>	 
				<font color="#808080">             
				户名：马碧华<br>
				开户行： 工商银行<br>
				帐号： 9558 8021 0211 0966373</font>
				</li-->
				</ul>
				
			</div> 
		</div>
		<div id="ordersuccess">
		 &#8226; <b>请填写您的详细信息</b>（加＊号的为必填项）&nbsp;&nbsp;&nbsp;&nbsp;<br>                                            
			&nbsp;&nbsp;(<font color="#ff0000">请确实留下您的联络电话，本站以电话进行订单确认</font>)</font>
		</div>    
		<table border="1" style="border:1px solid #7ECEF4; padding:0px;" width="100%" cellspacing="0" cellpadding="0" bordercolordark="#ecf5ff" bordercolorlight="#9BBDE3" height="258">
		  <tr>
			<td width="100%" height="256">                     
			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="261">
			  <tr> 
				<td width="153" height="30" align="right" valign="middle" bgcolor="#ecf5ff" style="text-align:right; font-size:14px;"> * 收件人姓名:&nbsp;&nbsp;</td>
				<td width="443" bgcolor="#ecf5ff" height="31">
				  <input size="27" name="name" value="<?php echo $this->__muant["userinfo"]["name"] ?>" maxlength="20" >
				  <font color="red">*</font></td>                                          
			  </tr>
			  <tr> 
				<td width="153" height="30" align="right" valign="middle" bgcolor="#ecf5ff" style="text-align:right; font-size:14px;">
				  * 邮寄地址:&nbsp;&nbsp;</td>
				<td width="443" bgcolor="#ecf5ff" height="30">
				  <input size="41" name="address" value="<?php echo $this->__muant["userinfo"]["address"] ?>" maxlength="50">
				  <font color="red">*</font></td>                                          
			  </tr>
			   <tr> 
				<td width="153" height="30" align="right" valign="middle" bgcolor="#ecf5ff" style="text-align:right; font-size:14px;">
				  邮编:&nbsp;&nbsp;</td>
				<td width="443" bgcolor="#ecf5ff" height="30">
				  <input size="20" name="postcode" value="<?php echo $this->__muant["userinfo"]["postcode"] ?>" maxlength="50">
				 </td>                                          
			  </tr>
			  <tr> 
				<td width="153" height="30" align="right" valign="middle" bgcolor="#ecf5ff" style="text-align:right; font-size:14px;">
				  *联系电话:&nbsp;&nbsp;</td>
				<td bgcolor="#ecf5ff" height="32" width="443"><font size="2"> 
				  <input size="27" name="phone" value="<?php echo $this->__muant["userinfo"]["phone"] ?>" maxlength="20">
				  <font color="red">*</font></font> </td>                                          
			  </tr>
			  <tr> 
				<td width="153" height="30" align="right" valign="middle" bgcolor="#ecf5ff" style="text-align:right; font-size:14px;">移动电话:&nbsp;&nbsp;</td>
				<td width="443" bgcolor="#ecf5ff" height="31"> 
				  <input name="mobile" size="27" maxlength="15" value="<?php echo $this->__muant["userinfo"]["mobile"] ?>">
				</td>
			  </tr>
			  <tr> 
				<td width="153" height="30" align="right" valign="middle" bgcolor="#ecf5ff" style="text-align:right;">
				  * E-Mail:&nbsp;&nbsp;</td>                     
				<td width="443" bgcolor="#ecf5ff" height="30">
				  <input size="30" name="email" value="<?php echo $this->__muant["userinfo"]["email"] ?>" maxlength="50">
				  <font color="red">*</font></td> 
			  </tr>
			  <tr> 
				<td width="153" height="30" align="right" valign="middle" bgcolor="#ecf5ff" style="text-align:right; font-size:14px;">用户备注:&nbsp;&nbsp;</td>
				<td width="443" bgcolor="#ecf5ff" height="24">(请写上您的特殊要求）
				</td>
			  </tr>
			  <tr> 
				<td width="153" bgcolor="#ecf5ff" align="right" height="83">　</td>
				<td width="443" bgcolor="#ecf5ff" height="83"><TEXTAREA name="ss" cols=52 rows="3"></TEXTAREA>
				</td>
			  </tr>
			</table>
			</td>
		  </tr>
		</table><br />
			<input style="FONT-SIZE: 9pt; COLOR: blue" type="button" value="回上页" onClick=" history.back(1);return false" id=button1 name=button1>
			<input style="FONT-SIZE: 9pt; COLOR: blue" onClick="history.back(1);return false;" type="button" value="继续采购" id=button2 name=button2>
			<INPUT style="FONT-SIZE: 9pt; COLOR: blue" onclick="setUserShopFrame('');myshow('usershop');callLoad('loadingiframe');" type="button" width="55" value="修改订单" >
			<INPUT style="FONT-SIZE: 9pt; COLOR: blue" type="submit" width="55" value="结帐送出" >
			<input name="orderinfo" type="hidden" value="" />
			</form>                       
			
		
			<div id="webinfo">
				<div id="payimg"></div>
				<div id="pay">
				  本网络代收机制受『128 bit SSL电子安全交易』保护，请放心购物        
				</div>
				  <a href="http://www.phpshop.cn/" target="_blank">phpshop.cn</a> 电子商务购物系统 您最放心的购物系统
			</div>
		</div>
<?php } ?>
	<div class="cl"></div>
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body>
</html>