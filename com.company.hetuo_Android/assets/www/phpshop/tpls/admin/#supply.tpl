<html>
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<link href="../../css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div>
    <div>{$fun_name}</div>
</div>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家名称:</div>
		<div><input name="name" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家联系电话:</div>
		<div><input name="phone" class="txInput" type="text"> 移动电话:<input name="mobile" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家联系Email:</div>
		<div><input name="email" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家联系地址:</div>
		<div><input name="address" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">描述:</div>
		<div>
		  <textarea name="describes" class="txInput"></textarea>
		</div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
<input name="add" type="submit" class="txInput" value="添加" onClick="this.value='yes';">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="{$lid}">
<input type="hidden" name="mid" value="{$mid}">
</div>
</form>
<div id="divMainBodyContent">
	<div id="divMainBodyContentTitle">
	所有货源商家:
	</div>
	<div></div>
	<div class="cls"></div>
</div>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
		{$loop value}
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			货源商家名称:
			</div>
			<div><input name="name[]" type="text" class="txInput" value="{$value.loop.name}"> 
			
			
			    
				<input name="oid[]" type="hidden" value="{$value.loop.id}">
				<input name="id[]" type="checkbox" value="{$value.loop.id}"> 修改  <a href="admincp.php?mid={$mid}&lid={$lid}&act=del&sid={$value.loop.id}" target="adminMain">删除</a>
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			货源商家联系电话:
			</div>
			<div><input name="phone[]" class="txInput" type="text" value="{$value.loop.phone}"> 移动电话:<input name="mobile[]" type="text" class="txInput" value="{$value.loop.mobile}"> </div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">货源商家联系Email:</div>
			<div><input name="email[]" class="txInput" type="text" value="{$value.loop.email}"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">货源商家联系地址:</div>
			<div><input name="address[]" class="txInput" type="text" value="{$value.loop.address}"></div>
		</div>

		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 描述:</div>
			<div>
			  <textarea name="describes[]" class="txInput">{$value.loop.describes}</textarea> 
		  </div>
			<div class="cls"></div>
		</div>
		{/loop}
<div id="divMainBodyContentSumbit">
<input name="modify" type="submit" value="modify" class="txInput">
<input type="hidden" name="lid" value="{$lid}">
<input type="hidden" name="mid" value="{$mid}">
</div>
</form>
</body></html>