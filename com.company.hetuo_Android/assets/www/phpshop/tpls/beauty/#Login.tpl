<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
    <!--head -->
    {inc file=inc/#body_top.tpl}
   <div id="locate"><span id="topnav"><a href="./">首页</a> &raquo; 用户登录</span> </div>
    <div id="chanl" style="border:#9AD5F2 1px solid; border-top:#FFF 1px solid;">
	  <div id="s_nav" class="register">用户登陆</div> 
      <div id="chantop">
	  	<form action="register.php?switch=checklogin" method="post">
			 <div id="toplf" style="padding:25px; padding-left:250px;">
				<div>
				  <div id="retinput" class="f">请输入您的E-mail：</div>
				  <div><input id="email" size="25" name="email" /></div>
				  <div class="cl"></div>
				</div>
				<br />
				<div>
				  <div id="retinput" class="f">输入您的登录密码：</div>
				  <div> <input name="password" type="password" id="password" size="25" /></div>
				  <div class="cl"></div>
				</div>
				<div>
				  <div id="retinput" class="f"></div>
				  <div></div>
				  <div class="cl"></div>
				</div>
				<br />
				<div>
				  <input name="remember" type="checkbox" value="yes" />记住我的帐号！
						<input name="" type="submit" value=" 登 录 " /> <input name="" type="reset" value=" 重 置 " /> <a href="register.php">还不是会员，请按 这里 免费注册！</a> <!--<a href="register.php">忘记密码，请按这里</a>-->
				  <div class="cl"></div>
				</div>
				<div>
				  <div></div>
				</div>
			</div>
		</form>
	  </div>
	  <div class="cl"></div>
	</div>
    <div class="cl"></div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>
