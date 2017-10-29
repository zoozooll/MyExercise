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
		<div id="divMainBodyContentTitle">为商品图片打上水印:</div>
		<div class="fLeft">
		<input type="radio" name="value1" value="-1"{if value.value1=='-1'} checked{/if}>不打<br />
		<input type="radio" name="value1" value="0"{if value.value1=='0'} checked{/if}>水印随机
		<input type="radio" name="value1" value="5"{if value.value1=='5'} checked{/if}>水印中部居中<br />
		<input type="radio" name="value1" value="1"{if value.value1=='1'} checked{/if}>水印顶端居左 
		<input type="radio" name="value1" value="2"{if value.value1=='2'} checked{/if}>水印顶端居中 
		<input type="radio" name="value1" value="3"{if value.value1=='3'} checked{/if}>水印顶端居右<br>
		<input type="radio" name="value1" value="7"{if value.value1=='7'} checked{/if}>水印底端居左 
		<input type="radio" name="value1" value="8"{if value.value1=='8'} checked{/if}>水印底端居中 
		<input type="radio" name="value1" value="9"{if value.value1=='9'} checked{/if}>水印底端居右 
		
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">水印图片(png格式 200X200px):</div>
	  <div class="fLeft">&nbsp;<input name="value2" type="text" class="txInput" id="value2" value="{$value.value2}" >
		</div>
		<div><iframe src="admincp.php?switch=uploadwaterimg" width="380" frameborder="0" height="25" allowTransparency='true' name="uploadpic"></iframe></div>
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