<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户中心 - 我要求广告位 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<meta name="Description" content="<?php echo $web['description']; ?>" />
<meta name="keywords" content="<?php echo $web['keywords']; ?>" />
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; <a href="user.php">用户中心</a></h5>

        <?php
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
require('inc/function/get_date.php');
if(confirm_login()){
  require('inc/require/user_left_menu.txt');

  $yes='我要求广告位';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';
?>
<div class="list_title"><a class="list_title_in">广告位列表</a></div>
当前暂无广告出售
<?php
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
