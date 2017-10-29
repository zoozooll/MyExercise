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
</head>
<body>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 用户名:</div>
		<div><input name="name" type="text" class="txInput" id="name" value="<?php echo $this->__muant["admin"]["name"] ?>" readonly >
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Email:</div>
	  <div><input name="email" type="text" class="txInput" id="email" value="<?php echo $this->__muant["admin"]["email"] ?>" >
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
		<div>IP:<?php echo $this->__muant["admin"]["lastloginip"] ?> 时间:<?php echo $this->__muant["admin"]["lastlogintime"] ?> 登录次数:<?php echo $this->__muant["admin"]["logintimes"] ?>
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">上次登录:</div>
		<div>IP:<?php echo $this->__muant["admin"]["loginip"] ?> 时间:<?php echo $this->__muant["admin"]["logintime"] ?>
		</div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
</body></html>