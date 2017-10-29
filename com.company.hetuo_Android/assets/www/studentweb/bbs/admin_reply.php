<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 管理跟帖评论 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<style type="text/css">
<!--
.shelf { border-collapse:collapse; }
.shelf td { font-size:12px; border-left:1px #CCCCCC solid; border-bottom:1px #CCCCCC solid; }
-->
</style>
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top">
<?php
require('inc/set_sql.php');
require('inc/set_area.php');
require('inc/function/confirm_manager.php');
require('inc/function/user_class.php');
require('inc/function/getarea.php');

if(confirm_manager()){
  $manage='&manage=yes';
  require('inc/require/admin_left_menu.txt');
  $yes='管理跟帖评论';
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
<form action="run.php?run=admin_del&dataname=yzsoureply" method="post" name="manageform" id="manageform">
<table class="shelf" style="border-top:1px #CCCCCC solid;border-right:1px #CCCCCC solid;" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="20" align="center">&nbsp;</td>
    <td width="*" align="center"><strong>评论内容</strong></td>
    <td width="110" align="center"><strong>评论作者</strong></td>
    <td width="110" align="center"><strong>评论时间</strong></td>
    <td width="55" align="center"><strong>主文ID</strong></td>
  </tr>
</table>

<?php


  require('inc/function/get_page.php');
  if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
    if(@mysql_select_db($sql['name'],$db)){
      mysql_query('SET NAMES '.$sql['char'].'');
	  if($result=mysql_query('SELECT * FROM yzsoureply ORDER BY id DESC',$db)){ //结果集
        $n=mysql_num_rows($result); //总记录数
        $p=get_page($n); //页数
        $text='';
        $seek=$n-$web['pagesize']*($p-1);
        $end=$seek-$web['pagesize']>0?$seek-$web['pagesize']:0;
        for($i=$seek-1;$i>=$end;$i--){
          if(mysql_data_seek($result,$i)){//<a href="run.php?run=admin_del&id='.$row['id'].'&dataname=yzsoureply">×</a>
            $row=mysql_fetch_assoc($result);
            $text.='<table class="shelf" style="border-right:1px #CCCCCC solid;" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="top">
    <td width="20" align="center"><input name="id[]" id="id[]" class="" type="checkbox" value="'.$row['id'].'" /></td>
    <td width="*"><a href="article.php?id='.$row['r_id'].'#reply_'.$row['id'].'" target="_blank">'.$row['text'].'</a></td>
    <td width="110" align="center">'.$row['author_ip'].'</td>
    <td width="110" align="center">'.$row['date'].'</td>
    <td width="55" align="center"><a href="article.php?id='.$row['r_id'].'" target="_blank">'.$row['r_id'].'</a></td>
  </tr>
</table>';
          }
        }
	    mysql_free_result($result);
      }
    }
    mysql_close();
  }

  if(!empty($text)){
    echo $text;
    echo get_page_foot($p,$n,'');
	echo '<input type="submit" name="act" value="删除" />';
  }else{
    echo '<img src="images/i.gif" align="absmiddle" /> 评论数据为空或数据库连接未成功！';
  }
?>
    </form>


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
