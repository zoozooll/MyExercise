<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 管理友情链接 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
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
  $yes='管理友情链接';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
function get_links($matches){
  if($matches[1] && $matches[2]){
    return '<div>名称<input type="text" name="linkname[]" value="'.htmlspecialchars($matches[2]).'" style="width:180px;" /> 网址<input type="text" name="linkhttp[]" value="'.htmlspecialchars($matches[1]).'" style="width:200px;" /><input type="button" value="删" onclick="dellink(this)" /></div>';
  }
}

if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
<a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';

  $text='';
  if($content_i=@file_get_contents('inc/require/frienlink.txt')){
    $arr_link=@preg_replace("/[\r\n]+/","",$content_i);
    $arr_link=@explode("　",$arr_link);
    $arr_link=@array_filter($arr_link);
    if(count($arr_link)>0){
      foreach($arr_link as $link){
        $text.=preg_replace_callback('/<a[^>]+href="([^>"]+)"[^>]*>(.+)<\/a>/i','get_links',$link);
      }
    }
  }
  unset($content_i);
?>
<br />
提示：友情链接请以代码书写，必须注意书写规范，如：<br />
网址写成：http://www.162100.com<br />
否则系统将自动过滤掉。
<script language="javascript" type="text/javascript">
<!--
//添加链接
function addlink(){
  var par=document.getElementById('links');
  par.innerHTML+='<div>名称<input type="text" name="linkname[]" style="width:180px;" /> 网址<input type="text" name="linkhttp[]"  style="width:200px;" /><input type="button" value="删" onclick="dellink(this)" /></div>';
}

//删除链接
function dellink(obj){
  var tar=obj.parentNode;
  var par=tar.parentNode;
  if(confirm('确定删除此链接吗？！')){
    try{
      par.removeChild(tar);
    }catch(err){
    }
  }
}
-->
</script>
<form name="setfriendlinkform" method="post" action="run.php?run=admin_friendlink">
  <div id="links">
  <?php echo $text; ?>  </div><br /><br />
  <input type="button" value="点此添加" onClick="addlink();"><br />
  <input type="submit" value="提交" onclick="javascript:return confirm(\'提交吗？请确定无误后再执行\');">
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
