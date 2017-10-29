<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script language="javascript">
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&type="+val;
}</script>
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 选择付款方式:</div>
		<div id="hiddenselect">
		<select name="tpye"  class="txInput" id="tpye" onChange="MP_jumpMenu(this.value)">
			<option value="B"<?php if($this->__muant["type"]=='B') { ?> selected<?php } ?>>银行电汇</option>
			<option value="W"<?php if($this->__muant["type"]=='W') { ?> selected<?php } ?>>在线付款</option>
			<option value="Y"<?php if($this->__muant["type"]=='Y') { ?> selected<?php } ?>>邮局汇款</option>
			<option value="H"<?php if($this->__muant["type"]=='H') { ?> selected<?php } ?>>货到付款</option>
		</select>
		</div>
		<div class="cls"></div>
    </div>
	<?php if($this->__muant["type"]!='W') { ?>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			<?php if($this->__muant["type"]=='B') { ?>银行电汇填写<?php } ?>
			<?php if($this->__muant["type"]=='H') { ?>货到付款填写<?php } ?>
			<?php if($this->__muant["type"]=='Y') { ?>邮局汇款填写<?php } ?>:
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			<?php if($this->__muant["type"]=='B') { ?>开户行<?php } ?>
			<?php if($this->__muant["type"]=='H') { ?>货到付款名称<?php } ?>
			<?php if($this->__muant["type"]=='Y') { ?>收款人地址<?php } ?>:
			</div>
			<div><input name="name" type="text"> <?php if($this->__muant["type"]=='H') { ?>开放货到付款地区<?php } else { ?>收款人<?php } ?>:<input name="payee" type="text"> 
			<?php if($this->__muant["type"]=='B') { ?>账号<?php } ?>
			<?php if($this->__muant["type"]=='H') { ?>货到付款须购物多少元以上<?php } ?>
			<?php if($this->__muant["type"]=='Y') { ?>邮编<?php } ?>:
			<input name="accounts" type="text"></div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 描述:</div>
			<div><input name="describes" type="text"> </div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"></div>
			<div><input name="add" type="submit" class="txInput" value="添加" onClick="this.value='yes';"> <input name="reset" type="reset" value="reset" class="txInput">
			  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
			  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
			</div>
			<div class="cls"></div>
		</div>
		</form>
		<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
		<input type="hidden" name="tpye" value="<?php echo $this->__muant["type"] ?>">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			<?php if($this->__muant["type"]=='B') { ?>所有银行电汇<?php } ?>
			<?php if($this->__muant["type"]=='H') { ?>所有货到付款<?php } ?>
			<?php if($this->__muant["type"]=='Y') { ?>所有邮局付款<?php } ?>:
			</div>
			<div></div>
			<div class="cls"></div>
		</div>
		<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			<?php if($this->__muant["type"]=='B') { ?>开户行<?php } ?>
			<?php if($this->__muant["type"]=='H') { ?>货到付款名称<?php } ?>
			<?php if($this->__muant["type"]=='Y') { ?>收款人地址<?php } ?>:
			</div>
			<div><input name="name[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["name"] ?>"> 
			<?php if($this->__muant["type"]=='H') { ?>开放货到付款地区<?php } else { ?>收款人<?php } ?>：<input name="payee[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["payee"] ?>"> 
			<?php if($this->__muant["type"]=='B') { ?>账号<?php } ?>
			<?php if($this->__muant["type"]=='H') { ?>货到付款须购物多少元以上<?php } ?>
			<?php if($this->__muant["type"]=='Y') { ?>邮编<?php } ?>:
			    <input name="accounts[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["accounts"] ?>"> 
				<input name="oid[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>">
				<input name="id[]" type="checkbox" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"> 修改  <a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&act=del&bid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>" target="adminMain">删除</a>
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 描述:</div>
			<div><input name="describes[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["describes"] ?>"> </div>
			<div class="cls"></div>
		</div>
		<?php } ?>
	<?php } else { ?>
	<div id="alipay">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle" class="fb">
			是否使用网上在线支付
			</div>
			<div><input type="radio" name="isusepay" value="1"<?php if($this->__muant["isusepay"]=='1') { ?> checked<?php } ?>>使用 <input type="radio" name="isusepay" value="0"<?php if($this->__muant["isusepay"]=='0') { ?> checked<?php } ?>>不使用</div>
			<div class="cls"></div>
		</div>		
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle" class="fb">支付宝(alipay):</div>
			<div><input type="checkbox" name="usealipay" value="1"<?php if($this->__muant["usealipay"]=='1') { ?> checked<?php } ?>>使用</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			合作者身份(partnerID):
			</div>
			<div><input name="partner" type="text" value="<?php echo $this->__muant["partner"] ?>"></div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			交易安全校验码(key)(security_code):
			</div>
			<div><input name="security_code" type="text" value="<?php echo $this->__muant["security_code"] ?>"></div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			支付宝帐号(email):
			</div>
			<div><input name="seller_email" type="text" value="<?php echo $this->__muant["seller_email"] ?>"></div>
			<div class="cls"></div>
		</div>
	</div>
	<div id="tenpay">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle" class="fb">财富通(tenpay):</div>
			<div><input type="checkbox" name="usetenpay" value="1"<?php if($this->__muant["usetenpay"]=='1') { ?> checked<?php } ?>>
			使用</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			商户号(strSpid):
			</div>
			<div><input name="strSpid" type="text" value="<?php echo $this->__muant["strspid"] ?>"></div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			32位商户密钥(strSpkey):
			</div>
			<div><input name="strSpkey" type="text" value="<?php echo $this->__muant["strspkey"] ?>"></div>
			<div class="cls"></div>
		</div>
	</div>
	<?php } ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div><input name="modify" type="submit" value="modify" class="txInput">&nbsp;
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
        </div>
		<div class="cls"></div>
    </div>
	</form>
</div>	
<div id="divMainBodyContentSumbit"></div>
</body></html>