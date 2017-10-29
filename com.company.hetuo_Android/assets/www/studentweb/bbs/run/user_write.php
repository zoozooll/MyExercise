<?php

if($_REQUEST['area_id']=='' || !preg_match('/^[\d\_]+$/',$_REQUEST['area_id'])){
  err('标签参数为空或错误！请<a href="user_write.php">重新开始</a>');
}
require('inc/set_area.php');
eval('$mark=$web[\'area\']['.str_replace('_','][',$_REQUEST['area_id']).'];');
if($mark==NULL){
  err('无此类目，标签参数出错！请<a href="user_write.php">重新开始</a>');
}

require('inc/function/confirm_login.php');
if(confirm_login()==false){
  if($web['guest_write']!=1){
    err('过客发表权限已被管理员关闭，请<a href="user_reg.php">注册</a>或<a href="user_login.php">登录</a>发表！');
  }
  if(!$_COOKIE['regimcode'] || !$_POST['imcode'] || $_POST['imcode']!=$_COOKIE['regimcode']){
    err('验证码缺失或不符！');
  }
  require('inc/function/user_ip.php');
  $author=user_ip();
  $linkpower=false;
}else{
  if(abs($web['write_start'])>0 && abs($cookie[1])<abs($web['write_start']) && $cookie[2]!='manager'){
    err('提交被拒绝！当前设置为：积分低于'.abs($web['write_start']).'不允许发表');
  }
  if($cookie[2]!='manager' && (strstr($cookie[2],'a') || strstr($cookie[2],'t'))){ //a所有，t主题
    err('提交被拒绝！您已经被限制发表，具体请询问管理员，以获得发表权限');
  }
  $author=$cookie[0];
  $linkpower=abs($cookie[1])<$web['link_start']?false:true;
  $write_other=true;
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
if($linkpower==false){
  $content=preg_replace('/<(a [^>]*)(http:\/\/[^\>\"\'\s]+)([^>]*)>/i','<${1}javascript:void(0)${3} onclick="window.open(\'$2\');return false;">',$content);
}

$topdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
require('inc/function/get_date.php');
$date=get_date($topdate,10);


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

$result=@mysql_query("INSERT INTO `yzsoulistdata`(`title`,`text`,`pic`,`fil`,`enc`,`date`,`reply`,`views`,`area_id`,`author_ip`,`topdate`) VALUES('".$subject."','".$content."','".$pic."','".$fil."','".$enc."','".$date."',0,0,'".$_REQUEST['area_id']."','".$author."','".$topdate."')");
$now_id=mysql_insert_id();

if(mysql_affected_rows()>0){
  //更新热标签
  if($result=mysql_query('SELECT * FROM yzsoulistdata WHERE area_id="'.$_REQUEST['area_id'].'"',$db)){
    $n=mysql_num_rows($result)+1; //总记录数
    mysql_free_result($result);
  }
  if($n>0){
    add_top_mark($n,$_REQUEST['area_id'],$mark);
  }
  if($write_other==true && $cookie[2]!='manager'){
    //更新发布数量
    mysql_query('UPDATE yzsoumember SET point=point+'.$web['writeadd'].',writecount=writecount+1 WHERE username="'.$cookie[0].'"',$db);
    $you=@explode('_',$cookie[1]);
    echo '
    <script language="javascript" type="text/javascript">
    <!--
    document.cookie="usercookie="+encodeURIComponent(\''.$cookie[0].'|'.(abs($you[0])+$web['writeadd']).'_'.$you[1].'|'.$cookie[2].'\')+"; path=/;";
    -->
    </script>';
  }
  mysql_close();
  alert('发布成功！','article.php?id='.$now_id.'');
}else{
  mysql_close();
  err('添加数据不成功！');
}


function add_top_mark($n,$area_id,$name){
  $mark=15; //定义最热标签数
  $tops=@file('inc/require/hot_mark.txt');
  if(preg_grep('/^\d+\|'.$area_id.'\|/',$tops)){
    $tops=preg_replace('/^\d+\|'.$area_id.'.+/',$n.'|'.$area_id.'|'.$name,$tops);
  }else{
	if(abs(end($tops))>$n){
	  return;
    }else{
	  $tops[]=$n.'|'.$area_id.'|'.$name.'
';
	}
  }
  rsort($tops);
  $tops=array_slice($tops,0,$mark);
  require('inc/function/write_file.php');
  @chmod('inc',0777);
  @chmod('inc/require',0777);
  write_file('inc/require/hot_mark.txt',@implode('',$tops));
}


?>



