<?php


  //页脚
require('inc/function/confirm_manager.php');
require('inc/function/filter.php');
require('inc/function/write_file.php');
  if(!(confirm_manager()==true && $cookie[0]==$web['manager'])){
    err('该命令必须以基本管理员'.$web['manager'].'身份登录！请重登录');
}

if($_POST['filter']=='yes'){
  $content=''.filter2($_POST['content']).'<?php require(\'inc/require/code_author.txt\'); ?>';
}else{
  if(!get_magic_quotes_gpc()){
    $_POST['content']=addslashes($_POST['content']);
  }else{
    $_POST['content']=$_POST['content'];
  }
  $content=''.stripslashes($_POST['content']).'<?php require(\'inc/require/code_author.txt\'); ?>';
}

@chmod('inc',0777);
@chmod('inc/require',0777);
write_file('inc/require/foot.txt',$content);
alert('发布成功！','admin_foot.php');


?>



