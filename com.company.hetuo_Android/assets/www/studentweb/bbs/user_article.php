<?php
if($_REQUEST['username']==''){
  die('&#29992;&#25143;&#21442;&#25968;&#32570;&#22833;&#25110;&#20986;&#38169;&#65292;&#35831;&#20174;<a href="./">&#39318;&#39029;</a>&#20174;&#26032;&#24320;&#22987;');
}
require('inc/set.php');
require('inc/set_sql.php');

if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  if(@mysql_select_db($sql['name'],$db)){
    mysql_query('SET NAMES '.$sql['char'].'');
    $result=mysql_query('SELECT username FROM yzsoumember WHERE username="'.$_REQUEST['username'].'"',$db); //结果集
    if($row=mysql_fetch_assoc($result)){
	  $yes='√';
      mysql_free_result($result);
      if($result=mysql_query('SELECT * FROM yzsoulistdata WHERE author_ip="'.$_REQUEST['username'].'" ORDER BY topdate',$db)){ //结果集
        $n=mysql_num_rows($result); //总记录数
        require('inc/function/get_page.php');
        $p=get_page($n); //页数
        $seek=$n-$web['pagesize']*($p-1);
        $end=$seek-$web['pagesize']>0?$seek-$web['pagesize']:0;
        $topdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
        for($i=$seek-1;$i>=$end;$i--){
          if(mysql_data_seek($result,$i)){
            if($row_=mysql_fetch_assoc($result)){
              $text_list.='
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="li">
  <tr>
    <td>·'.($row_['pic']!=''?'<img src="images/picye.gif" alt="文中含图" />':'').($row_['fil']!=''?'<img src="images/picfil.gif" alt="文中含影辑" />':'').($row_['enc']!=''?'<img src="images/picenc.gif" alt="文中含附件" />':'').'<a href="article.php?id='.$row_['id'].'" title="'.$row_['title'].'" target="_blank">'.$row_['title'].'</a>'.($row_['topdate']>$topdate?'[置顶]':'').'</td>
	<td width="50" align="right" title="评论或回复|阅览">'.$row_['reply'].'|'.$row_['views'].'</td>
	<td width="100" align="right">'.$row_['date'].'</td>
  </tr>
</table>
';
            }
          }
        }
        mysql_free_result($result);
	  }else{
        $err.='<img src="images/i.gif" align="absmiddle" /> 表连接不成功或尚未建立！';
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
<title>查看用户<?php echo $_REQUEST['username']; ?>发布的信息 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; 查阅用户<?php echo $_REQUEST['username']; ?>发布的信息</h5>


        <strong>查阅用户信息</strong>
        <ul>
          <li><a href="user_card.php?username=<?php echo urlencode($_REQUEST['username']); ?>">用户<?php echo $_REQUEST['username']; ?>的名片</a></li>
          <li><a href="#">用户<?php echo $_REQUEST['username']; ?>发布的信息</a></li>
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
?>
<div class="list_title"><a class="list_title_in">信息记录</a></div>
<?php
if(!isset($err)){
  if($text_list){
    $text.=$text_list;
    $text.=get_page_foot($p,$n,'&username='.urlencode($_REQUEST['username']).'');
  }else{
    $text.='<div class="li"><img src="images/i.gif" align="absmiddle" /> 暂无记录！</div>';
  }
  echo $text;
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