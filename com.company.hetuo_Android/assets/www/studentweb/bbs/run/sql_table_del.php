<?php

/*在线修改文件*/

require('inc/function/confirm_manager.php');
if(!(confirm_manager()==true && $cookie[0]==$web['manager'])){
  err('该命令必须以基本管理员'.$web['manager'].'身份登录！请重登录');
}
//连接mysqkl数据库
if(!$db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
  err('数据库['.$sql['host'].']连接不成功！');
}
//选择数据库并判断
if(!@mysql_select_db($sql['name'],$db)){
  err('数据库['.$sql['name'].']连接不成功！');
}

if(@mysql_query('DROP TABLE IF EXISTS '.$_REQUEST['table'].'')){
  $out.='数据库表格['.$_REQUEST['table'].']删除成功！';
}else{
  $out.='数据库表格['.$_REQUEST['table'].']删除失败！';
}


err($out.'<a href="admin.php">进入admin.php</a>');


?>



