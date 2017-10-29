<?php

/*在线修改文件*/

  require('inc/function/confirm_manager.php');
  if(!(confirm_manager()==true && $cookie[0]==$web['manager'])){
    err('该命令必须以基本管理员'.$web['manager'].'身份登录！请重登录');
  }
  if(!$_POST['thefile'] || !file_exists($_POST['thefile'])) //strlen($_POST['content'])==0 || 
    err('参数不全！');
  if(!get_magic_quotes_gpc()){
    $_POST['content']=addslashes($_POST['content']);
  }else{
    $_POST['content']=$_POST['content'];
  }
  require('inc/function/write_file.php');
  @chmod(dirname($_POST['thefile']),0777);
  write_file($_POST['thefile'],stripslashes($_POST['content']));
  err('在线修改文件完成！');


?>



