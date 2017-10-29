<?php
if($_REQUEST['username']==''){
  die('&#29992;&#25143;&#21442;&#25968;&#32570;&#22833;&#25110;&#20986;&#38169;&#65292;&#35831;&#20174;<a href="./">&#39318;&#39029;</a>&#20174;&#26032;&#24320;&#22987;');
}
require('inc/set.php');
require('inc/set_sql.php');

if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  if(@mysql_select_db($sql['name'],$db)){
    mysql_query('SET NAMES '.$sql['char'].'');
    if($result=mysql_query('SELECT * FROM yzsoumember WHERE username="'.$_REQUEST['username'].'"',$db)){ //结果集
      if($row=mysql_fetch_assoc($result)){
	    $yes='√';
        mysql_free_result($result);
	  }else{
        $err.='<img src="images/i.gif" align="absmiddle" /> 查不到用户'.$_REQUEST['username'].'的信息！';
	  }
    }else{
      $err.='<img src="images/i.gif" align="absmiddle" /> 查不到用户'.$_REQUEST['username'].'的信息！';
	}
    mysql_close();
  }else{
    $err.='<img src="images/i.gif" align="absmiddle" /> 指定的数据库连接不成功！';
  }
}else{
  $err.='<img src="images/i.gif" align="absmiddle" /> 数据库连接失败！';
}

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看用户<?php echo $_REQUEST['username']; ?>名片 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" src="js/main.js" type="text/javascript"></script>

<style type="text/css">
<!--
#card { width:454px; height:254px; position:relative; }
#card_bottom { width:446px; height:246px; border-right:4px #CCCCCC solid; border-bottom:4px #CCCCCC solid; position:absolute; top:4px; left:4px; z-index:1;  }
#card_top { width:448px; height:248px; border:1px #666666 solid; position:absolute; top:0; left:0; z-index:2; background:#FFFFFF url(images/about_card.png) 1px 1px no-repeat; }
h4 { font-size:150%; font-family:"黑体"; }
-->
</style></head>
<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; 查阅用户<?php echo $_REQUEST['username']; ?>名片</h5>


        <strong>查阅用户信息</strong>
        <ul>
          <li><a href="#">用户<?php echo $_REQUEST['username']; ?>的名片</a></li>
          <li><a href="user_article.php?username=<?php echo urlencode($_REQUEST['username']); ?>">用户<?php echo $_REQUEST['username']; ?>发布的信息</a></li>
        </ul></td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


        <?php
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
if(confirm_login()){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';
}else{
  echo '欢迎你：Guest匿名用户<br /><a href="user_reg.php?'.basename($_SERVER['REQUEST_URI']).'"><b>先去创建帐号（非常简单）</b></a>或<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录帐号</b></a>，以获得更多发表或管理权限';
}

if(!isset($err)){
?>
 <br />
<br />
<br />
<br />
       <center>
          <div id="card">
            <div id="card_bottom">&nbsp;</div>
            <div id="card_top">
            <table width="90%" height="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="100" align="left">&nbsp;</td>
                <td align="right"><h4><?php echo $row['company']!=''?$row['company']:'未留下公司名'; ?></h4></td>
              </tr>
              <tr>
                <td width="100" align="left">&nbsp;</td>
                <td align="right"><h4><?php echo $row['realname']!=''?$row['realname']:'未留下姓名'; ?> <?php echo '（'.($row['sex']!=''?$row['sex']:'未留下性别').'）'; ?></h4></td>
              </tr>
              <tr>
                <td width="100" align="left"><?php echo file_exists('data/upload/'.urlencode($cookie[0]).'.jpg')?'<a href="data/upload/'.urlencode(urlencode($cookie[0])).'.jpg" target="_blank"><img src="data/upload/'.urlencode(urlencode($cookie[0])).'.jpg" width="75" height="100" /></a>':'<img src="images/photo.jpg" width="75" height="100" />'; ?></td>
                <td align="left">邮箱：<?php echo $row['email']; ?><br />
                  地址：<?php echo $row['address']; ?><br />
                  邮编：<?php echo $row['zip']; ?> QQ：<?php echo $row['qq']; ?><br />
                  办公电话：<?php echo $row['hometel']; ?><br />
                  移动电话：<?php echo $row['handtel']; ?><br />
                  网址：<?php echo $row['sign']; ?></td>
              </tr>
            </table>
            </div>
          </div>
        </center>
      <?php
}else{
  echo $err!=''?'<br /><img src="images/i.gif" align="absmiddle" /> 发现错误信息：'.$err:'';
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