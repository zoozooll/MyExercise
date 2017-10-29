<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 在线修改文件 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
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
  $yes='在线修改文件';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
//遍历
function dirtree($path) {
  $text_d=$text_f=array();
  if($fp=@opendir($path)){
    while(false!==($file=readdir($fp))){
      $cf='';
      if($file=='.' || $file=='..' || $file=='images' || $file=='tmp') continue;
      if(is_dir($path.'/'.$file)){
        if($file=='css') $cf='<span style="color:#CCCCCC">（风格文件库）</span>';
		elseif($file=='data') $cf='<span style="color:#CCCCCC">（上传文件库）</span>';
        elseif($file=='run') $cf='<span style="color:#CCCCCC">（动态执行文件库）</span>';
        elseif($file=='img') $cf='<span style="color:#CCCCCC">（图片目录，不能编辑）</span>';
		elseif($file=='function') $cf='<span style="color:#CCCCCC">（函数文件库）</span>';
        elseif($file=='ad') $cf='<span style="color:#CCCCCC">（广告文件库）</span>';
		elseif($file=='power') $cf='<span style="color:#CCCCCC">（管理员密钥库）</span>';

        $text_d[]='<span style="cursor:hand;cursor:pointer;" onclick="javascript:toInput(\''.ltrim($path.'/'.$file,'./').'\');">目录：'.ltrim($path.'/'.$file,'./').''.$cf.'</span><br />';
      }
      if(is_file($path.'/'.$file)){
        if($file=='set.php') $cf='<span style="color:#CCCCCC">（系统参数文件）</span>';
		elseif($file=='set_area.php') $cf='<span style="color:#CCCCCC">（栏目参数文件）</span>';
		elseif($file=='set_sql.php') $cf='<span style="color:#CCCCCC">（数据库参数文件）</span>';
        elseif($file=='index.php') $cf='<span style="color:#CCCCCC">（首页）</span>';
        $text_f[]='<span style="cursor:hand;cursor:pointer;" onclick="javascript:toInput(\''.ltrim($path.'/'.$file,'./').'\');">文件：'.ltrim($path.'/'.$file,'./').''.$cf.'</span><br />';
      }
    }
    closedir($fp);
  }
  natcasesort($text_d);
  natcasesort($text_f);
  return implode('',$text_d).implode('',$text_f);
}

if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
<a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';
?>
<br />
<b>提示：</b>在此在线修改文件请务必谨慎，这将关系到网站能否正常运行；或本地手动修改该文件，然后上传
<?php
$text='';
$thefile=$_REQUEST['otherfile'];
if($thefile && is_file($thefile)):
  $file=@file_get_contents($thefile);
  $file=str_replace('&','&amp;',$file);
  $file=str_replace('<','&lt;',$file);
  $file=str_replace('>','&gt;',$file);
?>
<form name="Zmodifyform" method="post" action="run.php?run=admin_modify">
  <a href="?otherfile=<?php echo dirname($thefile); ?>">向上</a><br />
  <?php echo $thefile; ?> <b>代码：</b><?php echo preg_match('/\.js$/i',$thefile)?'<font color="#FF5500">此文件为JS文件，请用javascript语言编写</font>':''; ?><br />
  <textarea name="content" rows="30" style="width:98%;" wrap="off"><?php echo $file; ?></textarea><br />
  <input type="hidden" name="thefile" value="<?php echo $thefile; ?>">
  <input type="submit" value="修改完必，确定提交" onclick="javascript:return confirm('提交吗？请确定无误后再执行');">
  <input type="button" value=" ↓ " onclick="document.Zmodifyform.content.rows=document.Zmodifyform.content.rows+10;">
</form>
<?php
else:
?>
<form action="" method="post">
  <b>文件：</b><br />
  <input type="text" name="otherfile" id="otherfile" size="50"><br />
  <input type="submit" value="提交，进入该文件（目录）">
</form>
<script language="javascript" type="text/javascript">
<!--
function toInput(v){
  try{
    document.getElementById("otherfile").value=v;
    document.getElementById("otherfile").focus();
  }catch(err){
  }
}
//-->
</script>
<br />
<div class="title2"><b>请准确输入文件路径！！！或点击下面列表依次进行</b></div>
<?php
echo ($thefile && file_exists($thefile) && $thefile!='.')?'<a href="?otherfile='.dirname($thefile).'">向上</a><br />':'';
echo ''.dirtree($thefile?$thefile:'.').'';
endif;



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