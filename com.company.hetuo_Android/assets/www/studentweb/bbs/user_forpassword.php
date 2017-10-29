<?php
require('inc/set.php');
require('inc/set_sql.php');
$_REQUEST['username']=strtolower(trim($_REQUEST['username']));
//连接mysqkl数据库
if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  if(@mysql_select_db($sql['name'],$db)){
    mysql_query('SET NAMES '.$sql['char'].'');
    $result=mysql_query("SELECT * FROM yzsoumember WHERE username='".$_REQUEST['username']."'",$db); //结果集
    if(!$row=mysql_fetch_assoc($result)){
	  die('&#20986;&#38169;&#65281;&#25968;&#25454;&#24211;&#20013;&#26597;&#26080;&#27492;&#29992;&#25143;&#65281;&#28857;&#27492;&#21487;<a href="javascript:window.history.back();"><u>&#36820;&#22238;</u></a>');
	}
  }else{
    die('&#25968;&#25454;&#24211;['.$sql['name'].']&#36830;&#25509;&#19981;&#25104;&#21151;&#65281;&#28857;&#27492;&#21487;<a href="javascript:window.history.back();"><u>&#36820;&#22238;</u></a>');
  }
}else{
  die('&#25968;&#25454;&#24211;['.$sql['host'].']&#36830;&#25509;&#19981;&#25104;&#21151;&#65281;&#28857;&#27492;&#21487;<a href="javascript:window.history.back();"><u>&#36820;&#22238;</u></a>');
}
mysql_free_result($result);
mysql_close();


?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>找回密码<?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" src="js/main.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
<!--
function postChk(theForm){
  if(theForm.password_answer.value.replace(/^(\s+|　+)|(\s+|　+)$/,'')==''){
    alert('密码答案不能留空！');
	theForm.password_answer.focus();
    return false;
  }
  if(theForm.email.value.replace(/^(\s+|　+)|(\s+|　+)$/,'')==''){
    alert('email不能留空！');
	theForm.email.focus();
    return false;
  }
  if(!theForm.email.value.match(/[\w\.]+@[\w\.]+\.[\w\.]/)){
    alert('email填：xxx@xxx.xxx(.xx) 格式');
	theForm.email.focus();
    return false;
  }
  if(theForm.imcode.value.replace(/^(\s+|　+)|(\s+|　+)$/,'')==''){
    alert('验证码不能留空！');
	theForm.imcode.focus();
    return false;
  }
  if(theForm.imcode.value.replace(/^(\s+|　+)|(\s+|　+)$/,'')!=getCookie('regimcode')){
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
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> &nbsp;</div>


<form name="regform" method="post" action="run.php?run=user_login" onsubmit="return postChk(this)">
<input type="hidden" name="act" value="foundpassword">
<input name="username" value="<?php echo $_REQUEST['username']; ?>" type="hidden">
<table class="maintable" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; 找回密码</h5>
      </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5>填写</h5>
        <table cellspacing="0" border="0" cellpadding="0" width="100%">
          <tr>
            <td width="200" align="right">用户名：</td>
            <td><?php echo $_REQUEST['username']; ?></td>
          </tr>
          <tr>
            <td width="200" align="right">密码保护问题：</td>
            <td><select name="password_question" id="password_question" style="width:205px">
                <option value="">请选择</option>
                <option value="1" <?php echo ($row['password_question']==1)?' selected':'';?>>&#24744;&#30340;&#23456;&#29289;&#30340;&#21517;&#23383;</option>
                <option value="2" <?php echo ($row['password_question']==2)?' selected':'';?>>&#24744;&#25152;&#23601;&#35835;&#30340;&#31532;&#19968;&#25152;&#23398;&#26657;&#30340;&#21517;&#23383;</option>
                <option value="3" <?php echo ($row['password_question']==3)?' selected':'';?>>&#24744;&#26368;&#21916;&#27426;&#30340;&#20241;&#38386;&#36816;&#21160;&#26159;&#20160;&#20040;</option>
                <option value="4" <?php echo ($row['password_question']==4)?' selected':'';?>>&#24744;&#26368;&#21916;&#27426;&#30340;&#36816;&#21160;&#21592;&#26159;&#35841;</option>
                <option value="5" <?php echo ($row['password_question']==5)?' selected':'';?>>&#24744;&#30340;&#20307;&#37325;&#26159;&#22810;&#23569;&#20844;&#26020;</option>
                <option value="6" <?php echo ($row['password_question']==6)?' selected':'';?>>&#24744;&#20854;&#20013;&#19968;&#20301;&#32769;&#24072;&#30340;&#21517;&#23383;</option>
                <option value="7" <?php echo ($row['password_question']==7)?' selected':'';?>>&#24744;&#30340;&#36523;&#39640;&#26159;&#22810;&#23569;&#67;&#77;</option>
              </select>
                <font color="#FF6600">*</font></td>
          </tr>
          <tr>
            <td width="200" align="right">答案：</td>
            <td><input name="password_answer" type="text" style="width:200px" maxlength="200" />
                <font color="#FF6600">*</font></td>
          </tr>
          <tr>
            <td width="200" align="right">邮箱：</td>
            <td><input name="email" type="text" style="width:200px" maxlength="200" />
                <font color="#FF6600">*</font></td>
          </tr>
          <tr>
            <td align="right">验证码：</td>
            <td><input name="imcode" type="text" style="width:100px;float:left" />
                <iframe src="js/imcode.html" id="imFrame" name="imFrame" frameborder="0" width="100" height="24" scrolling="No"></iframe>
              <font color="#FF6600">*</font></td>
          </tr>
          <tr>
            <td width="200" align="right">&nbsp;</td>
            <td><input name="submit" type="submit" value="提交" />
                <input name="reset" type="reset" value="重置" /></td>
          </tr>
        </table></td>
  </tr>
</table>
</form>
<br />
<br />
<br />
<br />

<div id="foot"><?php require('inc/require/foot.txt'); ?></div>
</body>
</html>