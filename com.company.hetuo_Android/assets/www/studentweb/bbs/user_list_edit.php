<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户中心 - 修改已发信息 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
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
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; <a href="user.php">用户中心</a> &gt; 修改已发信息</h5>


        <?php
require('inc/set.php');
require('inc/set_sql.php');
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
require('inc/function/confirm_manager.php');

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
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';


  $_REQUEST['id']=array_unique((array)$_REQUEST['id']);
  $_REQUEST['id']=$_REQUEST['id'][0];
  if($_REQUEST['id'] && is_numeric($_REQUEST['id'])){

    //连接mysqkl数据库
    if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
      //选择数据库并判断
      if(@mysql_select_db($sql['name'],$db)){
        mysql_query('SET NAMES '.$sql['char'].'');
        if($result=mysql_query("SELECT * FROM yzsoulistdata WHERE id='".$_REQUEST["id"]."'",$db)){ //结果集
          //mysql_data_seek($result,$_REQUEST['id']);
          $row=mysql_fetch_assoc($result);
          mysql_free_result($result);
          if(!confirm_manager()){
		    if($row['author_ip']!=$cookie[0]){
              $err.='<br /><img src="images/i.gif" align="absmiddle" /> 经查你与该文作者不符！';
		    }
          }
		}else{
          $err.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库查无此记录！';
		}
	  }else{
        $err.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接不成功！';
      }
      mysql_close();
	}else{
      $err.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['host'].']连接不成功！';
    }
  }else{
    $err.='<br /><img src="images/i.gif" align="absmiddle" /> 文章参数出错！问题分析：1、您可能未选择文章；2、参数传递出错';
  }
  if(!isset($err)){
?>

<?php
  require('inc/set_area.php');
  if(preg_match('/^\d+\_\d+$/',$row['area_id'])){
    list($area_id,$class_id)=@explode('_',$row['area_id']);
    if($web['area'][$area_id][$class_id]!=NULL){
	  $type='<h6>所有栏目</h6> &gt; <h6>'.$web['area'][$area_id][0].'</h6> &gt; '.$web['area'][$area_id][$class_id];
	}
  }
  $type=$type?$type:'<font color=#FF5500>找不到相应栏目，可能程序出错，请重新尝试</font>';
?>
<iframe id="lastFrame" name="lastFrame" frameborder="0" style="display:none"></iframe>
<script language="javascript" type="text/javascript">
<!--
subjT='<?php echo trim($row['title']); ?>';
contT='<?php echo $row['text']; ?>';
var liMaxCount=<?php echo (is_numeric($web['list_wordcount']) && $web['list_wordcount']>0)?$web['list_wordcount']:50000; ?>;
var formU="run.php?run=user_edit&id=<?php echo $_REQUEST['id']; ?>";
var formF='\
<br /><br /><b><u><span style="color:#FF6600">*</span> 类目：</u></b><br />\
<?php echo $type; ?> <a href="user_list_edit2.php?id=<?php echo $_REQUEST['id']; ?>"> [ 改变类目 ]</a>\
<input type="hidden" name="area_id" value="<?php echo $row['area_id']; ?>" />';
document.write('<'+'sc'+'ript language="javascript" src="js/editor.js" type="text/javascript"></'+'sc'+'ript>');
-->
</script>
<div align="right"><a href="http://www.162100.com/bbs/162100editor_help.php" target="_blank"><img src="images/tools/about.gif" />  如何使用编辑器</a></div>
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
