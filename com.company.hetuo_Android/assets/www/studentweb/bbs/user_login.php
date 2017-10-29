<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录<?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" type="text/javascript">
<!--
window.onload=function(){
  document.loginform.location.value=location.href.replace(/^[^\?]+(\?(.+))?/i,'$2');
}
function forPassword(){
  var str_n=prompt('寻找密码——请输入用户名：','');
  if(str_n){
    location.href='user_forpassword.php?username='+encodeURIComponent(str_n)+'';
  }
}

-->
</script>
<script language="javascript" type="text/javascript">
<!--
function postChk(theForm){
  if(theForm.username.value.replace(/^(\s+|　+)|(\s+|　+)$/,'')==''){
    alert('用户名不能留空！');
	theForm.username.focus();
    return false;
  }
  if(theForm.password.value.replace(/^(\s+|　+)|(\s+|　+)$/,'')==''){
    alert('密码不能留空！');
	theForm.password.focus();
    return false;
  }
}
-->
</script>

</head>
<body>
<div id="logo"><a href="./"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>
<form action="run.php?run=user_login" method="post" name="loginform" id="loginform" onsubmit="return postChk(this)">
  <table class="maintable" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; 登录</h5>
         欢迎回来！登录后您可以有更多发表权限，例如：
        <ol>
            <li>可以上传图片</li>
          <li>可以发布链接</li>
          <li>通过发表可以积攒积分，以获得更多其他权限，如置顶、得到广告宣传位等等</li>
          <li>有关会员权限详细说明，可参见<a href="user_power.php">user_power.php</a></li>
        </ol></td>
      <td width="100" valign="middle" align="right" class="pass"> 》</td>
      <td valign="top"><h5>填写</h5>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="100"><select name="logintype" id="logintype">
                  <option value="username" selected="selected">用户名登录</option>
                  <option value="email">用邮箱登录</option>
              </select></td>
              <td><input name="username" type="text" onkeydown="if(event.keyCode==32){alert('用户名不能有空格');return false;}" style="width:170px" /></td>
            </tr>
            <tr>
              <td width="100">密码</td>
              <td><input name="password" type="password" onkeydown="if(event.keyCode==32){alert('密码不能有空格');return false;}" style="width:170px" /></td>
            </tr>
            <tr>
              <td width="100">&nbsp;</td>
              <td><input name="save_cookie" type="checkbox" value="1" />
                两周不用再登录 <a href="#" onclick="forPassword();return false;">忘记密码</a></td>
            </tr>
            <tr>
              <td width="100">&nbsp;</td>
              <td><input name="submit" type="submit" value="登录" />
                  <input name="reset" type="reset" value="重置" /></td>
            </tr>
        </table></td>
    </tr>
  </table>
  <input name="act" type="hidden" value="login" />
  <input name="location" type="hidden" />
</form>
<br />
<br />
<br />
<br />
<div id="foot"><a href="list.php?area_id=96">留言交流</a> - 2008-2009 All Rights Reserved</div>
</body>
</html>