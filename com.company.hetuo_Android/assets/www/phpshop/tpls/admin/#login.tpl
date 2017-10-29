<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="../css/admin/css.css" rel="stylesheet" type="text/css" />
</head>
<body style="background-color:#BAD1EB;">
<div style="width:100%; height:100%; background-color:#BAD1EB; margin:0px;" align="center">
	<div id="ligoin" style="FILTER:Dropshadow(color=#316AAA,offX=2,offY=2); width:100%;height:353px;line-height:15pt;">
		<div style="width:50%; height:180px; background-color:#73A2D6; border: 1px dashed #000000;">
			 <div style="height:25px;"></div>
			 <div style="height:25px;" class="f14w">【管理员登录】</div>
			  <form id="form1" name="form1" method="post" action="checklogin.php">
				<div style="height:30px; float:left; width:50%;" align="right">
				   用户名：<input name="name" type="text" class="txInput" id="name" size="15"/>
				</div>
				<div style="height:30px; float:left; width:60px;" align="right">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密 码：
				</div>
				<div style="height:30px; float:left;" align="right">
				<input name="pass" type="password" class="txInput" id="pass" size="15"/>
				</div>
				<br />
				<div style="height:30px; float:left; width:50%;" align="right">
				   验证码：<input name="validate" type="text" class="txInput" id="validate" size="4"/>
				</div>
				<div style="height:30px; float:left; width:60px;" align="right">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="height:30px; float:left;" align="right">
				<a href="#"><img onclick="this.src='validate.php?u=' + Math.random()" src="validate.php" id="validate" border="0"/></a>
				</div>
				<div style="height:30px; float:left; width:60px;" align="right">
				<span style="height:30px; float:left; width:50%;"></span>&nbsp;</div>
				<div style="height:30px; float:left;" align="right"></div>
				<br /><br />
				<label>
				<input type="submit" name="Submit" value="确 定"  class="txInput"/>
				</label>
				<label>
				<input type="reset" name="Submit2" value="清 除"  class="txInput"/>
				</label>
			  </form>
		</div>
	</div>
</div>
</body>
</html>