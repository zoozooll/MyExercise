<html>
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script language="javascript">
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid={$mid}&lid={$lid}&type="+val;
}</script>
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">
	<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 选择付款方式:</div>
		<div id="hiddenselect">
		<select name="tpye"  class="txInput" id="tpye" onChange="MP_jumpMenu(this.value)">
			<option value="B"{if type=='B'} selected{/if}>银行电汇</option>
			<option value="W"{if type=='W'} selected{/if}>在线付款</option>
			<option value="Y"{if type=='Y'} selected{/if}>邮局汇款</option>
			<option value="H"{if type=='H'} selected{/if}>货到付款</option>
		</select>
		</div>
		<div class="cls"></div>
    </div>
	{if type!='W'}
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			{if type=='B'}银行电汇填写{/if}
			{if type=='H'}货到付款填写{/if}
			{if type=='Y'}邮局汇款填写{/if}:
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			{if type=='B'}开户行{/if}
			{if type=='H'}货到付款名称{/if}
			{if type=='Y'}收款人地址{/if}:
			</div>
			<div><input name="name" type="text"> {if type=='H'}开放货到付款地区{else}收款人{/if}:<input name="payee" type="text"> 
			{if type=='B'}账号{/if}
			{if type=='H'}货到付款须购物多少元以上{/if}
			{if type=='Y'}邮编{/if}:
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
			  <input type="hidden" name="lid" value="{$lid}">
			  <input type="hidden" name="mid" value="{$mid}">
			</div>
			<div class="cls"></div>
		</div>
		</form>
		<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
		<input type="hidden" name="tpye" value="{$type}">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			{if type=='B'}所有银行电汇{/if}
			{if type=='H'}所有货到付款{/if}
			{if type=='Y'}所有邮局付款{/if}:
			</div>
			<div></div>
			<div class="cls"></div>
		</div>
		{$loop value}
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			{if type=='B'}开户行{/if}
			{if type=='H'}货到付款名称{/if}
			{if type=='Y'}收款人地址{/if}:
			</div>
			<div><input name="name[]" type="text" value="{$value.loop.name}"> 
			{if type=='H'}开放货到付款地区{else}收款人{/if}：<input name="payee[]" type="text" value="{$value.loop.payee}"> 
			{if type=='B'}账号{/if}
			{if type=='H'}货到付款须购物多少元以上{/if}
			{if type=='Y'}邮编{/if}:
			    <input name="accounts[]" type="text" value="{$value.loop.accounts}"> 
				<input name="oid[]" type="hidden" value="{$value.loop.id}">
				<input name="id[]" type="checkbox" value="{$value.loop.id}"> 修改  <a href="admincp.php?mid={$mid}&lid={$lid}&act=del&bid={$value.loop.id}" target="adminMain">删除</a>
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 描述:</div>
			<div><input name="describes[]" type="text" value="{$value.loop.describes}"> </div>
			<div class="cls"></div>
		</div>
		{/loop}
	{else}
	<div id="alipay">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle" class="fb">
			是否使用网上在线支付
			</div>
			<div><input type="radio" name="isusepay" value="1"{if isusepay=='1'} checked{/if}>使用 <input type="radio" name="isusepay" value="0"{if isusepay=='0'} checked{/if}>不使用</div>
			<div class="cls"></div>
		</div>		
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle" class="fb">支付宝(alipay):</div>
			<div><input type="checkbox" name="usealipay" value="1"{if usealipay=='1'} checked{/if}>使用</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			合作者身份(partnerID):
			</div>
			<div><input name="partner" type="text" value="{$partner}"></div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			交易安全校验码(key)(security_code):
			</div>
			<div><input name="security_code" type="text" value="{$security_code}"></div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			支付宝帐号(email):
			</div>
			<div><input name="seller_email" type="text" value="{$seller_email}"></div>
			<div class="cls"></div>
		</div>
	</div>
	<div id="tenpay">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle" class="fb">财富通(tenpay):</div>
			<div><input type="checkbox" name="usetenpay" value="1"{if usetenpay=='1'} checked{/if}>
			使用</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			商户号(strSpid):
			</div>
			<div><input name="strSpid" type="text" value="{$strspid}"></div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			32位商户密钥(strSpkey):
			</div>
			<div><input name="strSpkey" type="text" value="{$strspkey}"></div>
			<div class="cls"></div>
		</div>
	</div>
	{/if}
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div><input name="modify" type="submit" value="modify" class="txInput">&nbsp;
		  <input type="hidden" name="lid" value="{$lid}">
		  <input type="hidden" name="mid" value="{$mid}">
        </div>
		<div class="cls"></div>
    </div>
	</form>
</div>	
<div id="divMainBodyContentSumbit"></div>
</body></html>