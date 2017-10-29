<?php
/*
* 程序名称：162100简约论坛
* 作者：162100.com
* 邮箱：162100.com@163.com
* 网址：http://www.162100.com
* 演示：http://www.162100.com/bbs
* ＱＱ：184830962
* ＱＱ群：106319161 
* 声明：仅供代码爱好者学习交流，禁用于商业目的，请保留此版权信息
*/
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户中心 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<meta name="Description" content="<?php echo $web['description']; ?>" />
<meta name="keywords" content="<?php echo $web['keywords']; ?>" />
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; 用户中心</h5>


        <?php
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
if(confirm_login()){
  require('inc/require/user_left_menu.txt');

  $yes='现在可以操作了';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(isset($yes)){
  echo file_exists('data/upload/'.urlencode($cookie[0]).'.jpg')?'<a href="data/upload/'.urlencode(urlencode($cookie[0])).'.jpg" target="_blank"><img src="data/upload/'.urlencode(urlencode($cookie[0])).'.jpg" width="75" height="100" /></a><br />':'<img src="images/photo.jpg" width="75" height="100" /><br />';
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'<br />
	  <<建议完善自己的名片，以便让更多人关注';


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
