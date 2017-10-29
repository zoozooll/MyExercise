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

require('inc/set.php');
require('inc/set_sql.php');
require('inc/set_area.php');

function getclass($area_id){
  global $web,$option,$manage;
  if(is_array($web['area'][$area_id]) && count($web['area'][$area_id])>1){
    $text.='<font color=#FF5500>细分栏目：</font>';
    foreach((array)$web['area'][$area_id] as $i=>$class){
      if($i!=0){
        $option.='<option value="'.$area_id.'_'.$i.'" >　　'.$class.'</option>';
        $text.='<a href="?area_id='.$area_id.'_'.$i.''.$manage.'" class="class">'.$class.'</a> ';
	  }
    }
    $text.='<br />';
  }
  return $text;
}

if($_REQUEST['manage']=='yes'){
  require('inc/function/confirm_manager.php');
  if(confirm_manager())
    $manage='&manage=yes';
}
if($_REQUEST['list_type']!='ess'){
  $t1=' class="list_title_in"';
  $t2='';
}else{
  $t2=' class="list_title_in"';
  $t1='';
  $sqlt=' AND good="good"';
}


if($_REQUEST['area_id']){
  if(preg_match('/^\d+\_\d+$/',$_REQUEST['area_id'])){
    list($area_id,$class_id)=@explode('_',$_REQUEST['area_id']);
	if($web['area'][$area_id][$class_id]==NULL){
	  die('&#26631;&#31614;&#38169;&#35823;&#65281;&#35831;&#20174;<a href="./">&#39318;&#39029;</a>&#37325;&#26032;&#24320;&#22987;');
    }
	$option='<option value="'.$area_id.'" selected="selected">　'.$web['area'][$area_id][0].'</option><option value="'.$_REQUEST['area_id'].'" selected="selected">　　'.$web['area'][$area_id][$class_id].'</option>';
    $title1=$web['area'][$area_id][0].' &gt; '.$web['area'][$area_id][$class_id];
    $title2='<a href="?area_id='.$area_id.'&list_type='.$_REQUEST['list_type'].''.$manage.'">'.$web['area'][$area_id][0].'</a> &gt; '.$web['area'][$area_id][$class_id];
    $artext='';
	$arspit='';
  }elseif(is_numeric($_REQUEST['area_id'])){
	if($web['area'][$_REQUEST['area_id']]==NULL){
	  die('&#26631;&#31614;&#38169;&#35823;&#65281;&#35831;&#20174;<a href="./">&#39318;&#39029;</a>&#37325;&#26032;&#24320;&#22987;');
    }
	$option='<option value="'.$_REQUEST['area_id'].'" selected="selected">　'.$web['area'][$_REQUEST['area_id']][0].'</option>';
    $title1=$web['area'][$_REQUEST['area_id']][0];
    $title2=$web['area'][$_REQUEST['area_id']][0];
    $artext=getclass($_REQUEST['area_id']);
    $arspit='list($area_id,$class_id)=@explode("_",$row["area_id"]);$area_name=" <span class=\"ar\">[<a href=\"?area_id=".$area_id."_".$class_id."\">".$web["area"][$area_id][$class_id]."</a>]</span>";';
  }else{
    die('&#26631;&#31614;&#38169;&#35823;&#65281;&#35831;&#20174;<a href="./">&#39318;&#39029;</a>&#37325;&#26032;&#24320;&#22987;');
  }
}else{
  $title1='首页';
  $title2='所有栏目';
  require('inc/function/getarea.php');
  $artext=getarea();
  $arspit='list($area_id,$class_id)=@explode("_",$row["area_id"]);$area_name=" <span class=\"ar\">[<a href=\"?area_id=".$area_id."_".$class_id."\">".$web["area"][$area_id][$class_id]."</a>]</span>";';
  //$arspit='$area_name=" <span class=\"ar\">[<a href=\"?area_id=".$area_id."_".$class_id."\">".$web["area"][abs($row["area_id"])][0]."</a>]</span>";';
}

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><?php echo $title1; ?> - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<meta name="Description" content="<?php echo $web['description']; ?>" />
<meta name="keywords" content="<?php echo $web['keywords']; ?>" />
<link rel="stylesheet" type="text/css" href="css/style.css">
<?php
if(isset($manage)){
  echo '
<style type="text/css">
<!--
.mm { display:inline; }
-->
</style>
';
}
?>
<style type="text/css">
<!--
.ar a { color:#999999; text-decoration:none; }
-->
</style>
<script language="javascript" type="text/javascript">
<!--
var youVersion=4.2;
-->
</script>
<script language="javascript" src="js/main.js" type="text/javascript"></script>

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
    <form action="run.php?run=admin_del&dataname=yzsoulistdata" method="post" name="manageform" id="manageform">
      <h5><a href="index.php?<?php echo '&list_type='.$_REQUEST['list_type'].''.$manage.''; ?>">论坛首页</a> &gt; <?php echo $title2; ?></h5>
      <div class="area"><?php echo $artext; ?></div>
      <div class="list_title"><?php echo '<a href="?area_id='.$_REQUEST['area_id'].''.$manage.'"'.$t1.'>最新记录</a><a href="?area_id='.$_REQUEST['area_id'].'&list_type=ess'.$manage.'"'.$t2.'>精华记录</a>';
if(isset($manage)){
  require('inc/function/get_manager_key.php');
}
?>

      </div>
      <?php

if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  if(@mysql_select_db($sql['name'],$db)){
    mysql_query('SET NAMES '.$sql['char'].'');
    if($result=mysql_query('SELECT * FROM yzsoulistdata WHERE area_id LIKE "'.$_REQUEST['area_id'].'%"'.$sqlt.' ORDER BY topdate',$db)){ //结果集
      $n=mysql_num_rows($result); //总记录数
      require('inc/function/get_page.php');
	  require('inc/function/get_date.php');
      $p=get_page($n); //页数
      $seek=$n-$web['pagesize']*($p-1);
      $end=$seek-$web['pagesize']>0?$seek-$web['pagesize']:0;
      $topdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
	  $todayli=get_date($topdate,10);
	  $step=0;
      for($i=$seek-1;$i>=$end;$i--){
        if(mysql_data_seek($result,$i)){
          if($row=mysql_fetch_assoc($result)){
		    $step++;
		    eval($arspit);
            $text_list.='
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="li step'.($step%2).'">
  <tr>
    <td>·<input name="id[]" id="id[]" class="mm" type="checkbox" value="'.$row['id'].'" />'.($row['pic']!=''?'<img src="images/picye.gif" alt="文中含图" />':'').($row['fil']!=''?'<img src="images/picfil.gif" alt="文中含影辑" />':'').($row['enc']!=''?'<img src="images/picenc.gif" alt="文中含附件" />':'').'<a href="article.php?id='.$row['id'].'" title="'.$row['title'].'" target="_blank">'.$row['title'].'</a>'.($row['topdate']>$topdate?'[置顶]':'').''.($row['good']=='good'?'[精]':'').''.$area_name.'</td>
	<td width="110" align="right">'.(strstr($row['author_ip'],'.')?'<span title="匿名用户">('.$row['author_ip'].')</span>':'<a href="user_card.php?username='.urlencode($row['author_ip']).'" target="_blank" title="点击查看发布人名片">'.$row['author_ip'].'<img src="images/card.gif" /></a>').'</td>
	<td width="50" align="right" title="评论或回复|阅览">'.$row['reply'].'|'.$row['views'].'</td>
	<td width="80" align="right" class="todaydate'.($todayli==$row['date']?' todayli':'').'">'.$row['date'].'</td>
  </tr>
</table>
';
          }
        }
      }
      mysql_free_result($result);
	}else{
      $text.='<div class="li"><img src="images/i.gif" align="absmiddle" /> 表连接不成功或尚未建立！</div>';
	}
  }else{
    $text.='<div class="li"><img src="images/i.gif" align="absmiddle" /> 指定的数据库连接不成功！</div>';
  }
  mysql_close();
}else{
  $text.='<div class="li"><img src="images/i.gif" align="absmiddle" /> 数据库连接失败！</div>';
}

if($text_list){
  $text.=$text_list;
  $text.=get_page_foot($p,$n,'&area_id='.$_REQUEST['area_id'].'&list_type='.$_REQUEST['list_type'].''.$manage.'');
}else{
  $text.='<div class="li"><img src="images/i.gif" align="absmiddle" /> 暂无记录！</div>';
}




echo $text;


?>
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
      <div class="title">站内搜索</div>
      <form action="search.php?action=search" method="get" target="_blank">
        <input type="text" id="keyword" name="keyword" style="width:178px;" onfocus="if(this.value=='关键词'){this.value=''}" value="关键词" />
        <select name="area_id">
          <option value="all">所有类目</option>
          <?php echo $option; ?>
        </select>
        <input class="submit" type="submit" name="submit" value="搜索" />
        <input type="hidden" name="action" value="search" />
      </form>
    </div>
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
  <?php if(filesize('inc/ad/ad_index2.txt')>0){echo '<div class="ad_index2">';require('inc/ad/ad_index2.txt');echo '</div>';} ?>
  <?php require('inc/require/foot.txt'); ?>
</div>
</body>
</html>
