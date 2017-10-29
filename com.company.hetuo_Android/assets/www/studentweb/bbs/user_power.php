<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户中心 - 了解我的权限  - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
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
require('inc/set_sql.php');
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
require('inc/function/get_date.php');
if(confirm_login()){
  require('inc/require/user_left_menu.txt');

  $yes='了解我的权限';
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
<div class="list_title"><a class="list_title_in">系统参数说明</a></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="150">上传权限起始分</td>
    <td><strong><?php echo $web['up_start']; ?></strong>&nbsp;</td>
  </tr>
  <tr>
    <td width="150">发布链接起始分</td>
    <td><strong><?php echo $web['link_start']; ?></strong>&nbsp;</td>
  </tr>
  <tr>
    <td width="150">上传图片限定尺寸</td>
    <td><strong><?php echo $web['max_file_size'][15]; ?></strong> KB</td>
  </tr>
  <tr>
    <td width="150">上传动画限定尺寸</td>
    <td><strong><?php echo $web['max_file_size'][16]; ?></strong> KB</td>
  </tr>
  <tr>
    <td width="150">上传影音文件限定尺寸</td>
    <td><strong><?php echo $web['max_file_size'][17]; ?></strong> KB</td>
  </tr>
  <tr>
    <td width="150">上传其它文件限定尺寸</td>
    <td><strong><?php echo $web['max_file_size'][18]; ?></strong> KB</td>
  </tr>
  <tr>
    <td width="150">每日上传数量</td>
    <td>=等级分÷20</td>
  </tr>
</table>

<div class="list_title"><a class="list_title_in">我的档案权限</a></div>

<?php
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

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="150">我的积分</td>
    <td><strong><?php echo $row['point']; ?></strong>&nbsp;</td>
  </tr>
  <tr>
    <td width="150">发布权限</td>
    <td><strong><?php echo ($row['power']!='manager' && (strstr($row['power'],'a') || strstr($row['power'],'t')))?'×（原因：被关闭）':'√'; ?></strong>&nbsp;</td>
  </tr>
  <tr>
    <td width="150">评论权限</td>
    <td><strong><?php echo ($row['power']!='manager' && (strstr($row['power'],'a') || strstr($row['power'],'r')))?'×（原因：被关闭）':'√'; ?></strong>&nbsp;</td>
  </tr>
  <tr>
    <td width="150">上传权限</td>
    <td><strong><?php
	 if($row['power']!='manager' && (strstr($row['power'],'a') || strstr($row['power'],'u'))){
	   echo '×（原因：被关闭）';
	 }else{
	   if($row['point']>=$web['up_start']){
	     echo '√'; 
	   }else{
	     echo '×（原因：积分未到）';
	   }
	 }
	 ?></strong>&nbsp;</td>
  </tr>
  <tr>
    <td width="150">每日上传数量</td>
    <td><strong><?php echo ceil($row['point']/20); ?></strong> 如果有发布、上传权限&nbsp;</td>
  </tr>
  <tr>
    <td width="150">发布链接</td>
    <td><strong><?php echo ($row['point']<$web['link_start'])?'×':'√'; ?></strong> 如果有发布权限&nbsp;</td>
  </tr>
  <tr>
    <td width="150">置顶权限</td>
    <td><strong><?php echo abs($row['topcount']); ?></strong>条&nbsp;</td>
  </tr>
  <tr>
    <td width="150">置顶限期</td>
    <td><strong><?php
	    $nowdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
 echo abs($row['topdate'])>$nowdate?get_date($row['topdate']):'（无效）'; ?></strong>&nbsp;</td>
  </tr>
</table>




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
