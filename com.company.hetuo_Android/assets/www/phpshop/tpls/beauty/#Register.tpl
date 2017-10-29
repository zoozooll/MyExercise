<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<style type="text/css">
#user_info{ border-bottom: dotted 1px #CCC; font-size:16px; margin-top:10px; font-weight:300; width:600px; color: #666666;}
</style>
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
	<div id="locate"><span id="topnav"><a href="./">首页</a> &raquo; 注册成为会员</span> </div>
	<div id="register" style="border:#9AD5F2 1px solid; border-top:#FFF 1px solid;">
      <div id="regcomd">
		<div class="boxcoml">
			<div id="s_nav" class="register"><h3>注册成为会员</h3></div>
			<div style="padding-left:150px; padding-bottom:20px;">{if reg_success=='yes'}{inc file=#reg_success.tpl}{else}{inc file=#reg_info.tpl}{/if}</div>
		</div>
		<div class="cl"></div>
	  </div>
	</div>
    <div class="cl"></div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>
