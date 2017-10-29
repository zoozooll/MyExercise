<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 管理会员 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<style type="text/css">
<!--
.shelf { border-collapse:collapse; }
.shelf td { font-size:12px; border-left:1px #CCCCCC solid; border-bottom:1px #CCCCCC solid; }
-->
</style>
<script language="javascript" type="text/javascript">
<!--
function get_checkbox(){
  var allCheckBox=document.getElementsByName("id[]");
  var article='';
  if(allCheckBox!=null && allCheckBox.length>0){
    for(var i=0;i<allCheckBox.length;i++){
      if(allCheckBox[i].checked==true && allCheckBox[i].disabled==false){
        article=allCheckBox[i].value;
        break;
      }
    }
  }
  return article;
}

//管理
function chk(obj,manageType){
  if(get_checkbox()==''){
    alert('数据为空或尚未点选！');
    return false;
  }
  if(confirm('确定'+obj.value+'吗？')){
	document.manageform.action='run.php?run=admin_del&dataname=yzsoumember&limit='+manageType+'';
    document.manageform.submit();
  }
  return false;
}
-->
</script>



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
require('inc/function/get_page.php');
require('inc/function/getarea.php');

if(confirm_manager()){
  $manage='&manage=yes';
  require('inc/require/admin_left_menu.txt');
  $yes='管理会员';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
unset($text);
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
<a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';
?>

<div class="list_title"><a class="list_title_in">用户列表</a></div>
<form method="post" name="manageform" action="run.php?run=admin_del&dataname=yzsoumember" onsubmit="if(get_checkbox()==''){alert ('请点选！');return false;}" style="width:100%;overflow-x:auto;">

直接抵达用户：<input name="username" type="text" size="20" value="<?php echo $_REQUEST['username']; ?>" />
<input type="button" onclick="location.href='?username='+encodeURIComponent(this.form.username.value)+''" value="找" />
<table class="shelf" style="border-top:1px #CCCCCC solid;border-right:1px #CCCCCC solid;" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="20">&nbsp;</td>
    <td width="100">用户名</td>
    <td width="120">邮箱</td>
    <td width="80">密码问题</td>
    <td width="80">答案</td>
    <td width="80">权限</td>
    <td width="80">积分</td>
    <td width="80">发表数量</td>
    <td width="80">注册时间</td>
    <td width="80">最后访问</td>
	<td width="80">置顶条数</td>
    <td width="80">置顶限期</td>
	<td width="80">QQ</td>
	<td width="80">IP</td>
  </tr>
</table>
<?php
  require_once('inc/set_sql.php');
  //连接mysqkl数据库
  if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
    //选择数据库并判断
    if(@mysql_select_db($sql['name'],$db)){
      mysql_query('SET NAMES '.$sql['char'].'');
	  if($_REQUEST['username']){
	    $result=mysql_query('SELECT * FROM yzsoumember WHERE username="'.$_REQUEST['username'].'"',$db); //结果集
	    if($row=mysql_fetch_assoc($result)){
		  $text='<table class="shelf" style="border-right:1px #CCCCCC solid;" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="20"><input name="id[]" id="id[]" class="" type="checkbox" value="'.$row['id'].'" /></td>
    <td width="100">'.$row['username'].'&nbsp;</td>
    <td width="120">'.$row['email'].'&nbsp;</td>
    <td width="80">'.$row['password_question'].'&nbsp;</td>
    <td width="80">'.$row['password_answer'].'&nbsp;</td>
    <td width="80">'.$row['power'].'&nbsp;</td>
    <td width="80">'.$row['point'].'&nbsp;</td>
    <td width="80">'.$row['writecount'].'&nbsp;</td>
    <td width="80">'.$row['regdate'].'&nbsp;</td>
    <td width="80">'.$row['thisdate'].'&nbsp;</td>
	<td width="80">'.$row['topcount'].'&nbsp;</td>
	<td width="80">'.$row['topdate'].'&nbsp;</td>
	<td width="80">'.$row['qq'].'&nbsp;</td>
	<td width="80">'.$row['other1'].'&nbsp;</td>
  </tr>
</table>';
		}else{
		  $text='<br /><img src="images/i.gif" align="absmiddle" /> 查不到此用户！<a href="javascript:window.history.back()">返回</a>';
		}
	  }else{
        $result=mysql_query('SELECT * FROM yzsoumember ORDER BY id DESC',$db); //结果集
        $n=mysql_num_rows($result); //总记录数
        $p=get_page($n); //页数
        $text='';
        $seek=$n-$web['pagesize']*($p-1);
        $end=$seek-$web['pagesize']>0?$seek-$web['pagesize']:0;
        for($i=$seek-1;$i>=$end;$i--){
          if(mysql_data_seek($result,$i)){
            $row=mysql_fetch_assoc($result);
            $text.='<table class="shelf" style="border-right:1px #CCCCCC solid;" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="20"><input name="id[]" id="id[]" class="" type="checkbox" value="'.$row['id'].'" /></td>
    <td width="100">'.$row['username'].'&nbsp;</td>
    <td width="120">'.$row['email'].'&nbsp;</td>
    <td width="80">'.$row['password_question'].'&nbsp;</td>
    <td width="80">'.$row['password_answer'].'&nbsp;</td>
    <td width="80">'.$row['power'].'&nbsp;</td>
    <td width="80">'.$row['point'].'&nbsp;</td>
    <td width="80">'.$row['writecount'].'&nbsp;</td>
    <td width="80">'.$row['regdate'].'&nbsp;</td>
    <td width="80">'.$row['thisdate'].'&nbsp;</td>
	<td width="80">'.$row['topcount'].'&nbsp;</td>
	<td width="80">'.$row['topdate'].'&nbsp;</td>
    <td width="80">'.$row['qq'].'&nbsp;</td>
	<td width="80">'.$row['other1'].'&nbsp;</td>
  </tr>
</table>';
          }
        }
	  }
      mysql_free_result($result);
	}else{
      $text.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接不成功！';
	}
    mysql_close();
  }else{
    $text.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['host'].']连接不成功！';
  }
  echo $text;
  echo get_page_foot($p,$n,'');
  echo '
<input name="act" type="button" value="群发邮件" onclick="alert(\'此功能暂未制作\')" />
<input name="act" type="button" value="删除" onclick="chk(this,\'del\')" />
<input name="act" type="button" value="设为管理员" onclick="chk(this,\'addadmin\')" />
<input name="act" type="button" value="取消管理员" onclick="chk(this,\'deladmin\')" /><br />

<input name="act" type="button" value="限制发表" onclick="chk(this,\'t\')" />
<input name="act" type="button" value="限制评论" onclick="chk(this,\'r\')" />
<input name="act" type="button" value="限制上传" onclick="chk(this,\'u\')" />
<input name="act" type="button" value="限制所有" onclick="chk(this,\'a\')" /><br />

<input name="act" type="button" value="取消限制发表" onclick="chk(this,\'tt\')" />
<input name="act" type="button" value="取消限制评论" onclick="chk(this,\'rr\')" />
<input name="act" type="button" value="取消限制上传" onclick="chk(this,\'uu\')" />
<input name="act" type="button" value="取消限制所有" onclick="chk(this,\'aa\')" /><br />

批准：置顶<input name="topcount" type="text" size="2" value="'.$row['topcount'].'" />条，置顶<input name="topdate" type="text" size="2" value="'.$row['topdate'].'" />天
<input type="submit" value="提交" />
';


}else{
  echo '<img src="images/i.gif" align="absmiddle" /> 请以基本管理员'.$web['manager'].'<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录</b></a>，以获得管理权限';
}

?>
<br />
<br />
<br />
<br />
	</form>
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
