<?php



require('inc/function/user_ip.php');
if($_REQUEST['id']=='' || !is_numeric($_REQUEST['id'])){
  err('ID参数为空或错误！');
}

require('inc/function/confirm_login.php');
if(confirm_login()==true){
  if($cookie[2]!='manager' && (strstr($cookie[2],'a') || strstr($cookie[2],'r'))){ //a所有，t主题
    err('提交被拒绝！您已经被限制评论或回复，具体请询问管理员，以获得权限');
  }
  $author=$cookie[0];
}else{
  $author=user_ip();
}


/* 发表*/
require('inc/function/cutstr.php');
require('inc/function/filter.php');
$web['re_wordcount']=(is_numeric($web['re_wordcount']) && $web['re_wordcount']>0)?$web['re_wordcount']:5000;
$content=filter1(cutstr($_POST['content'],$web['re_wordcount']));
if($content==''){
  err('您提交的内容不能留空！');
}
$date=gmdate('Y/m/d H:i:s',time()+(floatval($web['time_pos'])*3600));

//题目ID|内容|日期|会员或IP
//连接mysqkl数据库
if(!$db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  err('数据库['.$sql['host'].']连接不成功！');
}
//选择数据库并判断
if(!@mysql_select_db($sql['name'],$db)){
  err('数据库['.$sql['name'].']连接不成功！');
}
mysql_query('SET NAMES '.$sql['char'].'');
if(abs($web['re_update'])==1){
  $topdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
  mysql_query('UPDATE yzsoulistdata SET reply=reply+1,topdate="'.$topdate.'" WHERE id="'.$_REQUEST['id'].'"',$db);
}else{
  mysql_query('UPDATE yzsoulistdata SET reply=reply+1 WHERE id="'.$_REQUEST['id'].'"',$db);
}
if(mysql_affected_rows()>0){
  mysql_query('INSERT INTO `yzsoureply`(`r_id`,`text`,`date`,`author_ip`) values('.$_REQUEST['id'].',"'.$content.'","'.$date.'","'.$author.'")');
  mysql_close();
  alert('评论回复成功！','article.php?id='.$_REQUEST['id'].'');
}else{
  mysql_close();
  err('评论回复不成功！可能是查不到此ID数据！可能传递出错');
}


?>



