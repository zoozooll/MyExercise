<?php
/*
* 程序名称：162100简约论坛
* 作者：162100.com
* 邮箱：162100.com@163.com
* 网址：http://www.162100.com
* 演示：http://www.162100.com/bbs
* ＱＱ：184830962
* ＱＱ群：106319161 
* 声明：仅供代码爱好者学习交流，禁用于商业目的，请保留此版权信息
*/
if($_REQUEST['id']=='' || !is_numeric($_REQUEST['id'])){
  die('&#73;&#68;&#21442;&#25968;&#32570;&#22833;&#25110;&#20986;&#38169;&#65292;&#35831;&#20174;<a href="./">&#39318;&#39029;</a>&#20174;&#26032;&#24320;&#22987;');
}
require('inc/set.php');
require('inc/set_area.php');
require('inc/set_sql.php');

if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  if(@mysql_select_db($sql['name'],$db)){
    mysql_query('SET NAMES '.$sql['char'].'');
    if($result=mysql_query('SELECT * FROM yzsoulistdata WHERE id="'.$_REQUEST['id'].'"',$db)){ //结果集
      $row=mysql_fetch_assoc($result);
      mysql_query('UPDATE yzsoulistdata SET views='.(abs($row['views'])+1).' WHERE id="'.$_REQUEST['id'].'"',$db);
      mysql_free_result($result);
    }
    if($row['area_id']!=''){
      if($result=mysql_query('SELECT * FROM yzsoulistdata WHERE area_id="'.$row['area_id'].'" ORDER BY topdate DESC LIMIT 10',$db)){ //结果集
        while($row_=mysql_fetch_assoc($result)){
          $text_.='·<a href="article.php?id='.$row_['id'].'" target="_blank">'.$row_['title'].'</a><br />';
        }
        mysql_free_result($result);
      }
    }
    if($row['id']!=''){
	  if($result=mysql_query('SELECT * FROM yzsoureply WHERE r_id="'.$_REQUEST['id'].'"',$db)){ //结果集
        while($row__=mysql_fetch_assoc($result)){
          $text__.='
		  <div class="author" id="reply_'.$row__['id'].'"><a href="run.php?run=admin_del&id='.$row__['id'].'&dataname=yzsoureply" class="del_re">×</a>'.$row__['author_ip'].' - '.$row__['date'].'</div>
		  <div class="area">'.$row__['text'].'</div>';
        }
		mysql_free_result($result);
      }
    }
    mysql_close();
  }/*else{
    $text='指定的数据库连接不成功！';
  }*/
}/*else{
  $text='数据库连接失败！';
}*/

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><?php echo $row['title']; ?><?php echo $web['code_author']; ?></title>
<meta name="Description" content="<?php echo $row['title']; ?> - <?php echo $web['description']; ?>" />
<meta name="keywords" content="<?php echo $web['keywords']; ?>" />
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" src="js/main.js" type="text/javascript"></script>
<style type="text/css">
<!--
-->
</style>

</head>

<body>
<table border="0" cellpadding="0" cellspacing="0" id="logo">
  <tr valign="top">
    <td width="200"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></td>
    <td><div class="ad_top"><?php require('inc/ad/ad_top.txt'); ?></div></td>
    <td width="250"><div class="top_r"><a onclick="SetHome(this,window.location);return(false);" href="#">设为主页</a><a href="#" onclick="addFavor();return(false);">加入收藏</a><a href="http://www.162100.com/" target="_blank">网址导航</a><span onclick="cCss('blue')" style="background-color:#6690C7;" title="兰色风格">换</span><span onclick="cCss('green')" style="background-color:#00CC00;" title="绿色风格">风</span><span onclick="cCss('yellow')" style="background-color:#FF9900;" title="黄色风格">格</span></div></td>
  </tr>
</table>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>

<div id="body">
  <div id="area">
  <?php if(filesize('inc/ad/ad_index.txt')>0){echo '<div class="ad_index">';require('inc/ad/ad_index.txt');echo '</div>';} ?>
<?php
list($area_id,$class_id)=@explode('_',$row['area_id']);
echo '<h5><a href="./">论坛首页</a> &gt; <a href="index.php?area_id='.$area_id.'">'.$web['area'][$area_id][0].'</a> &gt; '.$web['area'][$area_id][$class_id].'</h5>';
echo '<div><div style="float:right">[<a href="user_list_edit.php?id='.$_REQUEST['id'].'">编辑</a>]</div><h4>'.$row['title'].'</h4></div>';
echo '<div class="area">'.$row['text'].'</div>';

?>
    <?php require('inc/ad/ad_bottom.txt'); ?>
    <div id="reply"><a href="#foot" onclick="document.getElementById('postform').style.display='block'">评论或回复&gt;&gt;</a><br />
        <br />
        <?php
echo $text__;
?>
    </div>
    <script language="JavaScript" type="text/javascript">
<!--
function postChk(theForm){
  var con=theForm.content.value.replace(/^\s+|\s+$/g,'');
  if(con==''){
    alert('留言内容不能为空白！');
	theForm.content.focus();
    return false;
  }
  if(con.length > 400){
    alert('请缩短你的留言内容至400字符以下。现在为'+con.length+'字符，大约得减'+(con.length-400)+'字符');
	theForm.content.focus();
    return false;
  }
  var im=theForm.imcode.value;
  if(imC=getCookie("regimcode")){
    if(imC!=im){
      alert('请准确填写验证码！');
      return false;
    }
  }
  return true;
}
-->
</script>
    <form id="postform" name="postform" method="post" action="run.php?run=user_reply" onsubmit="return postChk(this)">
      <textarea name="content" id="content" rows="6" style="width:500px"></textarea> <font color="#B8B8B8">限字符数：<?php echo $web['list_wordcount']; ?></font>
      <table width="350" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="195">输入验证码：
              <input name="imcode" id="imcode" type="text" style="width:100px" /></td>
          <td><iframe src="js/imcode.html" id="imFrame" name="imFrame" frameborder="0" width="100" height="24" scrolling="No"></iframe></td>
        </tr>
      </table>
      <input type="hidden" name="id" value="<?php echo $_REQUEST['id']; ?>" />
      <input id="go" type="submit" name="go" value="提交评论回复" />
      <input name="reset" type="reset" value="重置" />
    </form>
  </div>
  <div id="right">
    <div class="power">
        <?php
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
if(confirm_login()){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).' <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout&location='.basename($_SERVER['REQUEST_URI']).'">退出</a><br />您上次访问是'.$you[1].'';
  $writepower='发表帖子';
}else{
  echo '欢迎你：Guest匿名用户<br /><a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'" style="color:#FF5500">登录您的帐号</a> <a href="user_reg.php?'.basename($_SERVER['REQUEST_URI']).'">快速注册帐号</a>';
  $writepower='匿名发表';
}
?>
    <a href="user_write.php" class="send">我要<?php echo $writepower; ?></a>
    </div>
    <div class="search">
      <?php
	  require('inc/function/get_date.php');
echo '发布日期：'.$row['date'].'<br />
	  刷新日期：'.get_date($row['topdate']).'<br />
      发布人：'.(strstr($row['author_ip'],'.')?'匿名用户('.$row['author_ip'].')':'<a href="user_card.php?username='.urlencode($row['author_ip']).'" target="_blank" title="点击查看发布人名片">'.$row['author_ip'].'<img src="images/card.gif" /></a>').'<br />
      回复或评论：'.$row['reply'].'<br />
      阅览：'.$row['views'].'';

?>
    </div>
    <div class="hot">
      <div class="title"><a href="index.php?area_id=<?php echo $row['area_id']; ?>" style="float:right">more</a>本栏目更多信息</div>
      <?php echo $text_!=''?$text_:'暂无'; ?> </div>
    <div class="hot">
      <div class="title">热门栏目</div>
      <?php
      require('inc/function/get_hot_mark.php');
	   echo get_hot_mark(); ?>
	</div>
    <div class="frienlink">
      <div class="title">友情链接</div>
      <?php require('inc/require/frienlink.txt'); ?>
	  <?php require('inc/ad/ad_frienlink.txt'); ?>
    </div>
    <div class="ad_right">
      <?php require('inc/ad/ad_right.txt');; ?>
    </div>
  </div>
</div>
<div id="foot">
  <?php require('inc/require/foot.txt'); ?></div>
</body>
</html>
