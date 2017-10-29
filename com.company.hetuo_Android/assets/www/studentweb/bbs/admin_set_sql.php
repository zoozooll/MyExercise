<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 修改数据库参数 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top">

<?php
require('inc/set_sql.php');
require('inc/function/confirm_manager.php');
require('inc/function/user_class.php');
require('inc/function/getarea.php');

if(confirm_manager()==true && $cookie[0]==$web['manager']){
  $manage='&manage=yes';
  require('inc/require/admin_left_menu.txt');
  $yes='修改数据库参数';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
<a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'<br />
<br />
';
?>

安装数据库流程：先在下面建数据库 > 然后建表 > 完成！
<?php
  if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
	$sub='重新';
    $err.='<br /><img src="images/ok.gif" align="absmiddle" /> 数据库['.$sql['host'].']连接成功！';
    //选择数据库并判断
    if(@mysql_select_db($sql['name'],$db)){
	  $sub='重新';
      $err.='<br /><img src="images/ok.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接成功！';
    }else{
      $err.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接不成功！请重新设置';
	}
  }else{
    $err.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['host'].']连接不成功！请重新设置';
  }
  if($err){
    echo $err;
  }
  echo '<br />
<br />
数据库配置————————————————————————
<form method="post" action="run.php?run=sql_set">
　　服务器类型：<input name="dbtype" type="text" value="'.$sql['type'].'" /> 一般是 mysql<br />
　　服务器地址：<input name="dbhost" type="text" value="'.$sql['host'].'" /> 一般是 localhost<br />
　　　数据库名：<input name="dbname" type="text" value="'.$sql['name'].'" /> <input name="create" type="checkbox" value="1"/>创建新数据库<br />
　数据库用户名：<input name="dbuser" type="text" value="'.$sql['user'].'" /><br />
数据库用户密码：<input name="dbpsw" type="password" value="'.$sql['pass'].'" /><br />
　　数据表编码：<select name="dbchar">
          <option value="utf8" selected="selected">UTF-8</option>
        </select>
  <input type="submit" value="'.$sub.'设置" />
</form>


';
?>



<?php
}else{
  echo '<img src="images/i.gif" align="absmiddle" /> 请以基本管理员'.$web['manager'].'<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录</b></a>，以获得管理权限';
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
