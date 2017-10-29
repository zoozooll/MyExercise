<?php

//判断管理员
function confirm_manager(){
  global $cookie;
  $cookie=@explode('|',$_COOKIE['usercookie']);
  if($cookie[2]=='manager' && !@file_exists('power/'.urlencode($cookie[0]).'.php')){
    die('<b>发现风险！</b>存放管理员密钥的目录或文件丢失，请检查power目录，保证其存在并可写权限，然后重新以管理员身份<a href="user_login.php">登录</a>。');
  }
  //$cookie=array_filter($cookie);
  if(count($cookie)==3 && $cookie[1]==substr(@file_get_contents('power/'.urlencode($cookie[0]).'.php'),15) && $cookie[2]=='manager'){
    return true;
  }
  return false;
}

?>