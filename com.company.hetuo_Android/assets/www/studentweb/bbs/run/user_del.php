<?php


require('inc/function/confirm_login.php');

/*编辑*/  
if(confirm_login()==false)
  err('请<a href="user_login.php">登录</a>！');
$_REQUEST['id']=array_unique((array)$_REQUEST['id']);

if(count($_REQUEST['id'])<1)
  err('参数出错！<br />问题分析：<br />1、您可能未选择<br />2、参数传递出错');

//连接mysqkl数据库
if(!$db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  err('数据库['.$sql['host'].']连接不成功！');
}
//选择数据库并判断
if(!@mysql_select_db($sql['name'],$db)){
  err('数据库['.$sql['name'].']连接不成功！');
}
mysql_query('SET NAMES '.$sql['char'].'');

if($_REQUEST['limit']=='refresh'){
  $nowdate=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
  $eval='
  mysql_query(\'UPDATE yzsoulistdata SET topdate="\'.$nowdate.\'" WHERE id="\'.$each.\'" and author_ip="\'.$cookie[0].\'"\');
  ';
}elseif($_REQUEST['limit']=='del'){
  $eval='
  if($each<=5){
	$err_.=\'<br />&#73;&#68;&#21069;&#53;&#26465;&#30340;&#20449;&#24687;&#20026;&#31243;&#24207;&#20316;&#32773;&#23459;&#20256;&#24086;&#65292;&#35831;&#20445;&#30041;&#65292;&#35831;&#25903;&#25345;&#65292;&#24863;&#35874;&#20351;&#29992;&#35813;&#31243;&#24207;&#65292;&#27426;&#36814;&#20809;&#20020;&#104;&#116;&#116;&#112;&#58;&#47;&#47;&#100;&#111;&#119;&#110;&#108;&#111;&#97;&#100;&#46;&#49;&#54;&#50;&#49;&#48;&#48;&#46;&#99;&#111;&#109;&#26356;&#26032;&#21319;&#32423;&#65281;\';
  }else{
    $step++;
    mysql_query(\'DELETE FROM yzsoulistdata WHERE id="\'.$each.\'" and author_ip="\'.$cookie[0].\'"\');
  }
  ';
}else{
  err('命令错误！');

}

$step=0;
foreach($_REQUEST['id'] as $each){
  if(is_numeric($each)){
    eval($eval);
  }
}
if($step>0 && $cookie[2]!='manager'){ //把分减掉
  $you=explode('_',$cookie[1]);
  $now_ponit=abs($you[0])-$step*$web['writeadd']>0?abs($you[0])-$step*$web['writeadd']:0;
  mysql_query('UPDATE yzsoumember SET point='.$now_ponit.' WHERE username="'.$cookie[0].'"',$db);
  echo '
  <script language="javascript" type="text/javascript">
  <!--
  document.cookie="usercookie="+encodeURIComponent(\''.$cookie[0].'|'.$now_ponit.'_'.$you[1].'|'.$cookie[2].'\')+"; path=/;";
  -->
  </script>';
}
if(mysql_affected_rows()>0){
  alert('执行成功！','user_list.php');
}else{
  err('执行数据不成功！');

}
mysql_close();


?>