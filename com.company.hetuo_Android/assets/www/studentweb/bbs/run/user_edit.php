<?php


require('inc/function/confirm_login.php');
require('inc/function/confirm_manager.php');
require('inc/set_area.php');

if(confirm_login()==false){
  err('提交被拒绝！<a href="user_reg.php"><b>先去注册（非常简单）</b></a>或<a href="user_login.php"><b>登录</b></a>，以获得发表权限');
}

if(!$_REQUEST['id'] || !is_numeric($_REQUEST['id'])){
  err('文章参数出错！<br />问题分析：1、您可能未选择文章；2、参数传递出错');
}

if($_REQUEST['area_id']=='' || !preg_match('/^[\d\_]+$/',$_REQUEST['area_id'])){
  err('标签参数为空或错误！请<a href="user_write.php">重新开始</a>');
}
eval('$mark=$web["area"]['.str_replace('_','][',$_REQUEST['area_id']).'];');
if($mark==NULL){
  err('无此类目，标签参数出错！请<a href="user_write.php">重新开始</a>');
}



/* 发表*/

require('inc/function/filter.php');
$subject=filter1($_POST['subject']);
server_sbj($subject); //主题检测
$content=filter2($_POST['content']);
$content=preg_replace('/(<(img|a) [^>]+)'.preg_quote($web['path'],'/').'((data\/upload|images)\/[^>]+>)/i','${1}${3}',$content);
server_chk($content,$web['list_wordcount']); //内容检测
//图
if(preg_match('/<img [^>]+data\/upload\/([^>]+\.(jpg|gif))[^>]*>/i',$content,$matches)){
  /*
  @chmod('data/upload',0777);
  if(@extension_loaded("gd")){
    //处理缩略图及水印
    run_img_resize('data/upload/'.$matches[1],'data/upload/s-'.$matches[1],$web['spic_w'],$web['spic_h'],$web['pic_quality']);
    $pic='data/upload/s-'.$matches[1];
  }
  */
  $pic='images/picye.gif';
}
if(!isset($pic)){
  if(preg_match('/<img [^>]+http:\/\/[^>]+\.(jpg|gif)[^>]*>/i',$content,$matches)){
    $pic='images/picye.gif';
  }else{
    $pic=''; //$pic='images/picno.gif';
  }
}

if(preg_match('/<embed[^>]+src[\s\r\n]*=[^>]+/isU',$_POST['content'])){
  $fil='images/picfil.gif';
}
if(preg_match('/<img[^>]+src[\s\r\n]*=[^>]*images\/picenc.gif[^>]+><a[^>]+href[\s\r\n]*=/isU',$_POST['content'])){
  $enc='images/picenc.gif';
}



//过滤链接
if(abs($cookie[1])<$web['link_start']){
  $content=preg_replace('/<(a [^>]*)(http:\/\/[^\>\"\'\s]+)([^>]*)>/i','<${1}javascript:void(0)${3} onclick="window.open(\'$2\');return false;">',$content);
}
$topdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
//$date=get_date($topdate,10);






//题目|内容|图|影片|附件|日期|浏览|类目|会员或IP|GUEST密码|置顶截止日期|精华

//连接mysqkl数据库
if(!$db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  err('数据库['.$sql['host'].']连接不成功！');
}
//选择数据库并判断
if(!@mysql_select_db($sql['name'],$db)){
  err('数据库['.$sql['name'].']连接不成功！');
}
mysql_query('SET NAMES '.$sql['char'].'');

if($result=mysql_query("SELECT * FROM yzsoulistdata WHERE id='".$_REQUEST["id"]."'",$db)){ //结果集
  //mysql_data_seek($result,$_REQUEST['id']);
  $row=mysql_fetch_assoc($result);
  if(!confirm_manager()){
    if($row['author_ip']!=$cookie[0]){
      err('经查你与该文作者不符！');
    }
  }
  mysql_query("UPDATE yzsoulistdata SET title='".$subject."',`text`='".$content."',pic='".$pic."',fil='".$fil."',enc='".$enc."',topdate='".$topdate."',area_id='".$_REQUEST['area_id']."' WHERE id='".$_REQUEST["id"]."'");
}

if(mysql_affected_rows()>0){
  mysql_close();
  alert('编辑成功！','article.php?id='.$_REQUEST["id"].'');
}else{
  err('添加数据不成功！');
}


?>



