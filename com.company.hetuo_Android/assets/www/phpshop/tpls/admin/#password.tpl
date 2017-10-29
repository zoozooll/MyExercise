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
		<div id="divMainBodyContentTitle"> 用户名:</div>
		<div><input name="name" type="text" class="txInput" id="name" value="{$admin.name}" readonly >
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Email:</div>
	  <div><input name="email" type="text" class="txInput" id="email" value="{$admin.email}" >
		  <input type="checkbox" name="changeemail" value="1"> 更新Email
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">修改密码:</div>
		<div><input name="oldpass" type="password" class="txInput" id="oldpass" value="" > 
		旧密码</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">&nbsp;</div>
		<div><input name="password" type="password" class="txInput" id="password" value="" > 
		新密码</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">&nbsp;</div>
		<div><input name="pass" type="password" class="txInput" id="pass" value="" > 
		再输入一次新密码</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">最近登录:</div>
		<div>IP:{$admin.lastloginip} 时间:{$admin.lastlogintime} 登录次数:{$admin.logintimes}
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">上次登录:</div>
		<div>IP:{$admin.loginip} 时间:{$admin.logintime}
		</div>
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