<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 修改页脚代码 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/editor.css">
<script language="javascript" type="text/javascript" src="js/main.js"></script>
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top">
<?php
require('inc/set_area.php');
require('inc/function/confirm_manager.php');
require('inc/function/user_class.php');
require('inc/function/getarea.php');

if(confirm_manager()==true && $cookie[0]==$web['manager']){
  $manage='&manage=yes';
  require('inc/require/admin_left_menu.txt');
  $yes='修改页脚代码';
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


$editstr=file_get_contents('inc/require/foot.txt');
$editstr=preg_replace('/<\?(php)?.*\?>/iU','',$editstr);
$editstr=preg_replace('/[\r\n]+/',"\\n",$editstr);
$editstr=preg_replace("/\'/","\\\'",$editstr);
$editstr=preg_replace('/<script/i','<sc\'+\'ri\'+\'pt',$editstr);
$editstr=preg_replace('/<\/script/i','</sc\'+\'ri\'+\'pt',$editstr);
//$editstr=str_replace('&','&amp;',$editstr);
//$editstr=str_replace('<','&lt;',$editstr);
//$editstr=str_replace('>','&gt;',$editstr);


?>
<iframe id="lastFrame" name="lastFrame" frameborder="0" style="display:none"></iframe>
<script language="javascript" type="text/javascript">
<!--
subjT='在下面编辑器中填写页脚代码吧！如备案号或统计代码等';
contT='<?php echo $editstr; ?>';
var liMaxCount=<?php echo (is_numeric($web['list_wordcount']) && $web['list_wordcount']>0)?$web['list_wordcount']:50000; ?>;
var formU="run.php?run=admin_foot";
var formF='<br /><br /><input type="radio" name="filter" value="yes" checked />请系统自动过滤代码<br /><input type="radio" name="filter" value="no" />不用过滤，我相信自己所写的代码规范(填写JS等代码如统计代码时请选此项)。<b>注意此项每次提交前都要仔细校对代码</b>';
document.write('<'+'sc'+'ript language="javascript" src="js/editor.js" type="text/javascript"></'+'sc'+'ript>');
-->
</script>
<div align="right"><a href="http://www.162100.com/bbs/162100editor_help.php" target="_blank"><img src="images/tools/about.gif" />  如何使用编辑器</a></div>
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

<div id="foot">2008-2010 162100.com All Rights Reserved</div>
</body>
</html>
