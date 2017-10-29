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
</head>
<body>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 客服即时聊天工具:</div>
		<div><input type="text" name="value1" value="{$value.value1}">QQ(多个号用; 隔开)
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> </div>
		<div><input type="text" name="value2" value="{$value.value2}">MSN(多个号用; 隔开)
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input type="text" name="value3" value="{$value.value3}">taobao(多个号用; 隔开)
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">联系电话及地址等:</div>
	    <div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value4" type="text" class="txInput" id="value4" value="{$value.value4}" >地址
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value5" type="text" class="txInput" id="value5" value="{$value.value5}" >
		邮编</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value6" type="text" class="txInput" id="value6" value="{$value.value6}" >
		电话</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value7" type="text" class="txInput" id="value7" value="{$value.value7}" >
		移动电话（24H客服专线）</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value8" type="text" class="txInput" id="value8" value="{$value.value8}" >
		传真</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value9" type="text" class="txInput" id="value9" value="{$value.value9}" >
		Email</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value10" type="text" class="txInput" id="value10" value="{$value.value10}" >
		800电话</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><input name="value11" type="text" class="txInput" id="value11" value="{$value.value11}" >
		400电话</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContentSumbit">
			  <input type="hidden" name="aid" value="{$aid}">
			  <input name="ok" type="submit" value="  o  k  " class="txInput">
			  <input name="reset" type="reset" value="reset" class="txInput">
			  <input type="hidden" name="lid" value="{$lid}">
			  <input type="hidden" name="mid" value="{$mid}">
	</div>
</div>
</form>
</body></html>