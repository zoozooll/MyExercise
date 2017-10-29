<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册<?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" type="text/javascript">
<!--
window.onload=function(){
  document.regform.location.value=location.href.replace(/^[^\?]+(\?(.+))?/i,'$2');
}
-->
</script>
<script language="javascript" type="text/javascript">
<!--
function postChk(theForm){
  var un=theForm.username.value;
  if(un.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')==''){
	theForm.username.focus();
    alert('用户名不能空！');
    return false;
  }
  if(un.match(/[\?\+\%\"\'\|\\]+/)){
    alert('提交被拒绝！用户名——长度请控制在3-45个字符之内（汉字为3字符），请尽量用汉字、数字、英文及下划线组成，不能含?+%"\'|\ ');
	theForm.username.focus();
    return false;
  }
  var pn=theForm.password.value;
  if(pn.match(/[\s\r\n]+/)){
    alert('密码不能有空格！请重输');
	theForm.password.focus();
    return false;
  }
  if(pn.length>30 || pn.length<3){
    alert('密码长度是3-30字符！');
	theForm.password.focus();
    return false;
  }
  if(theForm.password_again.value==''){
    alert('密码重输不能留空！');
	theForm.password_again.focus();
    return false;
  }
  if(pn!=theForm.password_again.value){
    alert('两次密码输得不一样！');
	theForm.password_again.focus();
    return false;
  }
  if(theForm.password_question.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')==''){
    alert('密码问题不能留空！');
    return false;
  }
  if(theForm.password_answer.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')==''){
    alert('密码答案不能留空！');
	theForm.password_answer.focus();
    return false;
  }
  if(theForm.email.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')==''){
    alert('email不能留空！');
	theForm.email.focus();
    return false;
  }
  if(!theForm.email.value.match(/[\w\.]+@[\w\.]+\.[\w\.]/)){
    alert('email填：xxx@xxx.xxx(.xx) 格式');
	theForm.email.focus();
    return false;
  }
  if(theForm.qq.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')!='' && !theForm.qq.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'').match(/[\d]{1,20}/)){
    alert('QQ请添数字（20位以内）！');
	theForm.qq.focus();
    return false;
  }
  if(theForm.imcode.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')==''){
    alert('验证码不能留空！');
	theForm.imcode.focus();
    return false;
  }
  if(theForm.imcode.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')!=getCookie('regimcode')){
    alert('验证码不符！');
	theForm.imcode.focus();
    return false;
  }
  return true;
}
-->
</script>

</head>
<body>
<div id="logo"><a href="./"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>
<form action="run.php?run=user_reg" method="post" name="regform" id="regform" onsubmit="return postChk(this)">
  <table class="maintable" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; 注册帮助</h5>
          <ol>
            <li>用户名——长度请控制在3-45个字符之内（汉字为3字符），请尽量用汉字、数字、英文及下划线组成，不能含?+%"\'|\</li>
            <li>密码——请设定在3-30位，不能有空格，应尽量设置繁杂点以保证安全</li>
            <li>密码问题便于您密码找回时使用</li>
            <li>邮箱——请真实有效地填写，这将非常重要</li>
            <li>不能输入空白字符以及<font color="#FF6600">*</font> 为必填项</li>
          </ol></td>
      <td width="100" valign="middle" align="right" class="pass"> 》</td>
      <td valign="top"><h5>填写</h5>
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="100">用户名：</td>
              <td><input name="username" type="text" onkeydown="if(event.keyCode==32){alert('用户名不能有空格');return false;}" style="width:200px" maxlength="40" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100">密码：</td>
              <td><input name="password" type="password" onkeydown="if(event.keyCode==32){alert('密码不能有空格');return false;}" style="width:200px" maxlength="40" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100">重输密码：</td>
              <td><input name="password_again" type="password" style="width:200px" maxlength="40" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100">密码找回问题：</td>
              <td><select name="password_question" style="width:205px">
                  <option value="">请选择</option>
                  <option value="1" >&#24744;&#30340;&#23456;&#29289;&#30340;&#21517;&#23383;</option>
                  <option value="2" >&#24744;&#25152;&#23601;&#35835;&#30340;&#31532;&#19968;&#25152;&#23398;&#26657;&#30340;&#21517;&#23383;</option>
                  <option value="3" >&#24744;&#26368;&#21916;&#27426;&#30340;&#20241;&#38386;&#36816;&#21160;&#26159;&#20160;&#20040;</option>
                  <option value="4" >&#24744;&#26368;&#21916;&#27426;&#30340;&#36816;&#21160;&#21592;&#26159;&#35841;</option>
                  <option value="5" >&#24744;&#30340;&#20307;&#37325;&#26159;&#22810;&#23569;&#20844;&#26020;</option>
                  <option value="6" >&#24744;&#20854;&#20013;&#19968;&#20301;&#32769;&#24072;&#30340;&#21517;&#23383;</option>
                  <option value="7" >&#24744;&#30340;&#36523;&#39640;&#26159;&#22810;&#23569;&#67;&#77;</option>
                </select>
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100">答案：</td>
              <td><input name="password_answer" type="text" style="width:200px" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100">邮箱：</td>
              <td><input name="email" type="text" style="width:200px" maxlength="200" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100">QQ：</td>
              <td><input name="qq" type="text" style="width:200px" maxlength="200" />
                  </td>
            </tr>
            <tr>
              <td width="100">验证码：</td>
              <td><input name="imcode" type="text" style="width:100px;float:left" />
                  <iframe src="js/imcode.html" id="imFrame" name="imFrame" frameborder="0" width="100" height="24" scrolling="No"></iframe>
                <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100"><input name="location" type="hidden" />
              &nbsp;</td>
              <td><input name="submit" type="submit" value="注册" />
                  <input name="reset" type="reset" value="重置" /></td>
            </tr>
        </table></td>
    </tr>
  </table>
  <input type="hidden" name="act" value="reg" />
  <br />
</form>
<br />
<br />
<br />
<br />
<div id="foot"><a href="list.php?area_id=96">留言交流</a> - 2008-2009 All Rights Reserved</div>
</body>
</html>