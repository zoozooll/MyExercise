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
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">
	<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 品牌名称:</div>
	    <div>中文：<input name="name" type="text"> 英文：<input name="enname" type="text"></div>
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
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 所有品牌:</div>
	    <div></div>
		<div class="cls"></div>
    </div>
	{$loop value}
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div>中文：<input name="name[]" type="text" value="{$value.loop.name}"> 英文：<input name="enname[]" type="text" value="{$value.loop.enname}">
				   <input name="oid[]" type="hidden" value="{$value.loop.id}">
				   <input name="id[]" type="checkbox" value="{$value.loop.id}"> 修改 <a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&delbid={$value.loop.id}&act=del" target="_self">删除</a></div>
		<div class="cls"></div>
    </div>
	{/loop}
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div><input name="modify" type="submit" value="modify" class="txInput">
		<input type="hidden" name="lid" value="{$lid}">
		<input type="hidden" name="mid" value="{$mid}">
		</div>
		<div class="cls"></div>
    </div>
	</form>
</div>
<div id="divMainBodyContentSumbit">
</div>
</body></html>