<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户中心 - 管理已发信息 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
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
if(confirm_login()){
  require('inc/require/user_left_menu.txt');

  $yes='管理已发信息';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


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
    if(manageType=='edit')
      document.manageform.action='user_list_edit.php';
    else
      document.manageform.action='run.php?run=user_del&limit='+manageType+'';
    document.manageform.submit();
  }
  return false;
}
-->
</script>
<form method="post" name="manageform">
<?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';


   require('inc/set_sql.php');
  //连接mysqkl数据库
  if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
    if(@mysql_select_db($sql['name'],$db)){
      mysql_query('SET NAMES '.$sql['char'].'');
      if($result=mysql_query('SELECT * FROM yzsoulistdata WHERE author_ip="'.$cookie[0].'" ORDER BY topdate',$db)){ //结果集
        $n=mysql_num_rows($result); //总记录数
        require('inc/function/get_page.php');
        $p=get_page($n); //页数
        $seek=$n-$web['pagesize']*($p-1);
        $end=$seek-$web['pagesize']>0?$seek-$web['pagesize']:0;
        for($i=$seek-1;$i>=$end;$i--){
          if(mysql_data_seek($result,$i)){
            if($row=mysql_fetch_assoc($result)){
              $text.='<div class="li"><div style="float:right"><span title="评论或回复|阅览">'.$row['reply'].'|'.$row['views'].'</span> - '.$row['date'].'</div>·<input name="id[]" id="id[]" class="" type="checkbox" value="'.$row['id'].'" /><a href="article.php?id='.$row['id'].'" target="_blank">'.$row['title'].'</a></div>';
            }
          }
	    }
        $text.=get_page_foot($p,$n,'');
        $text.='
<input name="act" type="button" value="编辑" onclick="chk(this,\'edit\')" />
<input name="act" type="button" value="删除" onclick="chk(this,\'del\')" />
<input name="act" type="button" value="刷新" onclick="chk(this,\'refresh\')" />常来刷新让信息排名靠前
';
        mysql_free_result($result);
	  }else{
        $text.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库查无数据！';
	  }
    }else{
      $text.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接不成功！';
    }
    mysql_close();
  }else{
    $text.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['host'].']连接不成功！';
  }
?>

<div class="list_title"><a class="list_title_in">信息记录</a></div>

<?php


  if($text){
    echo $text;
  }else{
    echo '<br /><img src="images/i.gif" align="absmiddle" /> 暂无数据！';
  }



}else{
  echo '欢迎你：Guest匿名用户<br /><a href="user_reg.php?'.basename($_SERVER['REQUEST_URI']).'"><b>先去创建帐号（非常简单）</b></a>或<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录帐号</b></a>，以获得更多发表或管理权限';
}

?>
</form>    </td>
  </tr>
</table>
<br />
<br />
<br />
<br />

<div id="foot"><?php require('inc/require/foot.txt'); ?></div>
</body>
</html>
