<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户中心 - 完善/修改我的名片 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" src="js/main.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
<!--
function postChk(theForm){
  var pn=theForm.password.value;
  if(pn!='' && pn.match(/[\s\r\n]+/)){
    alert('密码不能有空格！请重输');
	theForm.password.focus();
    return false;
  }
  if(pn!='' && (pn.length>30 || pn.length<3)){
    alert('密码长度是3-30字符！');
	theForm.password.focus();
    return false;
  }
  if(pn!='' && theForm.password_again.value==''){
    alert('密码重输不能留空！');
	theForm.password_again.focus();
    return false;
  }
  if(pn!='' && pn!=theForm.password_again.value){
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
  if(theForm.imcode.value==''){
    alert('验证码不能留空！');
	theForm.imcode.focus();
    return false;
  }
  if(theForm.imcode.value!=getCookie('regimcode')){
    alert('验证码不符！');
	theForm.imcode.focus();
    return false;
  }
  if(theForm.handtel.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')!='' && !theForm.handtel.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'').match(/[\d-]{1,20}/)){
    alert('移动电话请添11位数字！');
	theForm.handtel.focus();
    return false;
  }
  if(theForm.hometel.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')!='' && !theForm.hometel.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'').match(/[\d-]{1,20}/)){
    alert('办公电话请添“区号-数字（20位以内）”格式！');
	theForm.hometel.focus();
    return false;
  }
  if(theForm.zip.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')!='' && !theForm.zip.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'').match(/[\d]{6}/)){
    alert('邮编请添数字（6位）！');
	theForm.zip.focus();
    return false;
  }
  if(theForm.qq.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'')!='' && !theForm.qq.value.replace(/^[\s\r\n]+|[\s\r\n]+$/,'').match(/[\d]{1,20}/)){
    alert('QQ请添数字（20位以内）！');
	theForm.qq.focus();
    return false;
  }
  return true;
}
-->
</script>

</head>
<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; <a href="user.php">用户中心</a></h5>


        <?php
require('inc/set_sql.php');
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
if(confirm_login()){
  require('inc/require/user_left_menu.txt');

  $yes='完善/修改我的名片';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php

//连接mysqkl数据库


?>        <?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';


  if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
    if(@mysql_select_db($sql['name'],$db)){
      mysql_query('SET NAMES '.$sql['char'].'');
      if($result=mysql_query("SELECT * FROM yzsoumember WHERE username='".$cookie[0]."'",$db)){ //结果集
	    $row=mysql_fetch_assoc($result);
        mysql_free_result($result);
	  }else{
	    $err.='<br /><img src="images/i.gif" align="absmiddle" /> 出错！数据库中查无此用户！';
	  }
    }else{
      $err.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接不成功！';
    }
    mysql_close();
  }else{
    $err.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接不成功！';
  }
  if(!isset($err)){
?>
<div class="list_title"><a class="list_title_in">我的名片</a></div><br />

<a href="user_card.php?username=<?php echo urlencode($row['username']); ?>" target="_blank">预览我当前的名片</a><br /><br />


        <form action="run.php?run=user_reg" method="post" enctype="multipart/form-data" name="regform" id="regform" onsubmit="return postChk(this)">
          <input type="hidden" name="act" value="regfilemodify" />
          <table cellspacing="0" border="0" cellpadding="0" width="100%">
            <tr>
              <td width="100" align="right">用户名：</td>
              <td><?php echo $row['username']; ?></td>
            </tr>
            <tr>
              <td width="100" align="right"><?php echo file_exists('data/upload/'.urlencode($cookie[0]).'.jpg')?'<a href="data/upload/'.urlencode(urlencode($cookie[0])).'.jpg" target="_blank"><img src="data/upload/'.urlencode(urlencode($cookie[0])).'.jpg" width="75" height="100" /></a>':'<img src="images/photo.jpg" width="75" height="100" />'; ?></td>
              <td>上传头像：<br /><input type="file" name="uploadfile" /></td>
            </tr>
            <tr>
              <td width="100" align="right">密码：</td>
              <td><input name="password" type="password" onkeydown="if(event.keyCode==32){alert('密码不能有空格');return false;}" style="width:200px" value="" maxlength="40" />
                  <font color="#FF6600">*</font>（不填则默认原密码）</td>
            </tr>
            <tr>
              <td width="100" align="right">重输密码：</td>
              <td><input name="password_again" type="password" style="width:200px" maxlength="40" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">密码保护问题：</td>
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
              <td width="100" align="right">答案：</td>
              <td><input name="password_answer" type="text" style="width:200px" value="<?php echo $row['password_answer']; ?>" maxlength="200" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">邮箱：</td>
              <td><input name="email" type="text" style="width:200px" value="<?php echo $row['email']; ?>" maxlength="200" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">真实姓名：</td>
              <td><input name="realname" type="text" style="width:200px" value="<?php echo $row['realname']; ?>" maxlength="200" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">性别：</td>
              <td><input name="sex" type="radio" value="先生" id="man"<?php echo $row['sex']=='先生'?' checked':''; ?> maxlength="200" />
                  <label for="man">先生</label>
                  <input name="sex" type="radio" value="女士" id="woman"<?php echo $row['sex']=='女士'?' checked':''; ?> maxlength="200" />
                  <label for="woman">女士</label></td>
            </tr>
            <tr>
              <td width="100" align="right">出生日期：</td>
              <td><input name="birthday" type="text" style="width:200px" value="<?php echo $row['birthday']; ?>" maxlength="200" /></td>
            </tr>
            <tr>
              <td width="100" align="right">移动电话：</td>
              <td><input name="handtel" type="text" style="width:200px" value="<?php echo $row['handtel']; ?>" maxlength="20" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">办公电话：</td>
              <td><input name="hometel" type="text" style="width:200px" value="<?php echo $row['hometel']; ?>" maxlength="20" />
                请添“区号-数字”格式</td>
            </tr>
            <tr>
              <td width="100" align="right">公司名称：</td>
              <td><input name="company" type="text" style="width:200px" value="<?php echo $row['company']; ?>" maxlength="200" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">地址：</td>
              <td><input name="address" type="text" style="width:200px" value="<?php echo $row['address']; ?>" maxlength="200" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">邮编：</td>
              <td><input name="zip" type="text" style="width:200px" value="<?php echo $row['zip']; ?>" maxlength="200" />
                  <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="100" align="right">QQ：</td>
              <td><input name="qq" type="text" style="width:200px" value="<?php echo $row['qq']; ?>" maxlength="20" /></td>
            </tr>
            <tr>
              <td width="100" align="right">网址：</td>
              <td><input name="sign" type="text" style="width:200px" value="<?php echo $row['sign']; ?>" maxlength="40" /></td>
            </tr>
            <tr>
              <td width="100" align="right">验证码：</td>
              <td><input name="imcode" type="text" style="width:100px;float:left" />
                  <iframe src="js/imcode.html" id="imFrame" name="imFrame" frameborder="0" width="100" height="24" scrolling="No"></iframe>
                <font color="#FF6600">*</font></td>
            </tr>
            <tr>
              <td width="200" align="right">&nbsp;</td>
              <td><input name="submit" type="submit" value="修改" />
                  <input name="reset" type="reset" value="重置" /></td>
            </tr>
          </table>
        </form>
      <?php
  }else{
    echo $err!=''?'<br /><img src="images/i.gif" align="absmiddle" /> 发现错误信息：'.$err:'';
  }
}else{
  echo '欢迎你：Guest匿名用户<br /><a href="user_reg.php?'.basename($_SERVER['REQUEST_URI']).'"><b>先去创建帐号（非常简单）</b></a>或<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录帐号</b></a>，以获得更多发表或管理权限';
}

?>
    </td>
  </tr>
</table>
<br />
<br />
<br />
<br />
<div id="foot"><?php require('inc/require/foot.txt'); ?></div>
</body>
</html>