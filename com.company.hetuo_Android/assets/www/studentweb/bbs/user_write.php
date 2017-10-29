<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我要发表帖子 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<meta name="Description" content="<?php echo $web['description']; ?>" />
<meta name="keywords" content="<?php echo $web['keywords']; ?>" />
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/editor.css">
<script language="javascript" type="text/javascript" src="js/main.js"></script>
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>

<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; 我要发表帖子 &gt; 选择类目</h5>


        <?php
require('inc/set_area.php');
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');

function getclass($area_id){
  global $web;
  if(is_array($web['area'][$area_id]) && count($web['area'][$area_id])>1){
    $text.='<font color=#FF5500>细分栏目：</font>';
    foreach((array)$web['area'][$area_id] as $i=>$class){
      if($i!=0){
        $text.='<a href="?area_id='.$area_id.'_'.$i.'" class="class">'.$class.'</a> ';
	  }
    }
    $text.='<br />';
  }
  return $text;
}

function getarea(){
  global $web;
  foreach((array)$web['area'] as $i=>$area){
    $text.='<h6><a href="?area_id='.$i.'">'.$area[0].'</a></h6> ';
    foreach((array)$area as $j=>$class){
      if($j!=0){
        $text.='<a href="?area_id='.$i.'_'.$j.'" class="class">'.$class.'</a> ';
      }
    }
    $text.='<br />';
  }
  return $text;
}


if($_REQUEST['area_id']){
  if(preg_match('/^\d+\_\d+$/',$_REQUEST['area_id'])){
    list($area_id,$class_id)=@explode('_',$_REQUEST['area_id']);
	if($web['area'][$area_id][$class_id]==NULL){
	  echo '<h6>所有栏目</h6><br />';
	  echo getarea();
    }else{
      echo $type='<h6><a href="?">所有栏目</a></h6> &gt; <h6><a href="?area_id='.$area_id.'">'.$web['area'][$area_id][0].'</a></h6> &gt; '.$web['area'][$area_id][$class_id];
	  $yes='现在可以发表了';
    }
  }elseif(is_numeric($_REQUEST['area_id'])){
	if($web['area'][$_REQUEST['area_id']]==NULL){
	  echo '<h6>所有栏目</h6><br />';
	  echo getarea();
    }else{
	  echo '<h6><a href="?">所有栏目</a></h6> &gt; <h6><a href="?area_id='.$_REQUEST['area_id'].'">'.$web['area'][$_REQUEST['area_id']][0].'</a></h6><br />';
      echo getclass($_REQUEST['area_id']);
	}
  }else{
    echo getarea();
  }
}else{
  echo '<h6>所有栏目</a></h6><br />';
  echo getarea();
}


?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(confirm_login()){
  echo '
<style type="text/css">
<!--
#ok { display:none; }
-->
</style>';
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';
}else{
  echo '欢迎你：Guest匿名用户<br /><a href="user_reg.php?'.basename($_SERVER['REQUEST_URI']).'"><b>先去创建帐号（非常简单）</b></a>或<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录帐号</b></a>，以获得更多发表或管理权限';
  $imcode='<br /><br /><b><span style="color:#FF6600">*</span> 匿名发表请填验证码：</b><div style="clear:both"><input name="imcode" type="text" style="width:100px;float:left" /><iframe src="js/imcode.html" id="imFrame" name="imFrame" frameborder="0" width="100" height="24" scrolling="No"></iframe></div>';
}
?>
<?php
if($yes==true){
  if($web['guest_write']!=1){
    echo '<div style="position:relative">
          <div id="ok">&nbsp;</div>
        </div>';
  }
?>
        
<iframe id="lastFrame" name="lastFrame" frameborder="0" style="display:none"></iframe>
<script language="JavaScript" type="text/javascript">
<!--
var liMaxCount=<?php echo (is_numeric($web['list_wordcount']) && $web['list_wordcount']>0)?$web['list_wordcount']:50000; ?>;
var formU="run.php?run=user_write";
var formF='\
<br /><br /><b><u><span style="color:#FF6600">*</span> 类目：</u></b><br />\
<?php echo $type; ?>\
<input type="hidden" name="area_id" value="<?php echo $_REQUEST['area_id']; ?>" /><?php echo $imcode; ?>';
document.write('<'+'sc'+'ript language="javascript" src="js/editor.js" type="text/javascript"></'+'sc'+'ript>');
-->
</script>
<div align="right"><a href="http://www.162100.com/bbs/162100editor_help.php" target="_blank"><img src="images/tools/about.gif" />  如何使用编辑器</a></div>
<?php
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
