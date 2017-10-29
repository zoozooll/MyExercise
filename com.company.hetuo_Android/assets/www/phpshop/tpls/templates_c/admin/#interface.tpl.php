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
		<div id="divMainBodyContentTitle"> 邮件发送方式:</div>
		<div> 使用sendmail <input type="radio" name="value1"<?php if($this->__muant["value"]["value1"]==1) { ?> checked<?php } ?> value="1">  
		      使用其他SMTP 服务器发送  <input type="radio" name="value1"<?php if($this->__muant["value"]["value1"]==0) { ?> checked<?php } ?> value="0">
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"><b>使用其他SMTP 服务器设置:</b></div>
	    <div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">SMTP 服务器的地址:</div>
		<div><input name="value2" type="text" class="txInput" id="value2" value="<?php echo $this->__muant["value"]["value2"] ?>" > 
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">SMTP 服务器的端口:</div>
		<div><input name="value3" type="text" class="txInput" id="value3" value="<?php echo $this->__muant["value"]["value3"] ?>" >(默认为25)</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">发信人邮件地址:</div>
		<div><input name="value4" type="text" class="txInput" id="value4" value="<?php echo $this->__muant["value"]["value4"] ?>" ></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">SMTP 身份验证用户名:</div>
		<div><input name="value5" type="text" class="txInput" id="value5" value="<?php echo $this->__muant["value"]["value5"] ?>" ></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">SMTP 身份验证密码:</div>
		<div><input name="value6" type="password" class="txInput" id="value6" value="<?php echo $this->__muant["value"]["value6"] ?>" ></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContentSumbit">
			  <input type="hidden" name="aid" value="<?php echo $this->__muant["aid"] ?>">
			  <input name="ok" type="submit" value="  o  k  " class="txInput">
			  <input name="reset" type="reset" value="reset" class="txInput">
			  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
			  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
	</div>
</div>
</form>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"><b>发送邮件测试:</b></div>
		<div> 测试发件人: <input type="text" name="fromemail" value="" class="txInput">  
		      测试收件人(多个收件人用 ; 隔开): <input type="text" name="toemail" value="" class="txInput"> <input name="sendemail" type="submit" value="开始测试" class="txInput">
		</div>
	</div>
</div>
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</form>
</body></html>