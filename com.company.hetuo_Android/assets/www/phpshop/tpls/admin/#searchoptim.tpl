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
	<!-- 
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">启用 Archiver:</div>
		<div>是<input type="radio" name="value1" value="1"{if value.value1==1} checked{/if}>
			 否<input type="radio" name="value1" value="0"{if value.value1==0} checked{/if}>
		</div>
	</div>
	-->
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">标题附加字:</div>
		<div><input name="value2" type="text" class="txInput" id="value2" value="{$value.value2}" size="35"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Meta Keywords:</div>
		<div><input name="value3" type="text" class="txInput" id="value3" value="{$value.value3}" size="35"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Meta Description:</div>
		<div><input name="value4" type="text" class="txInput" id="value4" value="{$value.value4}" size="35"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">其他头部信息:</div>
		<div><input name="value5" type="text" class="txInput" id="value5" value="{$value.value5}" ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">静态页面:</div>
		<div><input name="value6" type="radio" value="0"{if value.value6==0} checked{/if}>不做任何静态处理<input name="value6" type="radio" value="1"{if value.value6==1} checked{/if}>仿静态处理<input name="value6" type="radio" value="2"{if value.value6==2} checked{/if}>静态处理(需服务器支持)</div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="{$lid}">
<input type="hidden" name="mid" value="{$mid}">
</div>
</form>
</body></html>