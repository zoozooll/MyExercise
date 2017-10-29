<?php

//判断登录
function confirm_login(){
  global $cookie;
  $cookie=@explode('|',$_COOKIE['usercookie']);
  if(count($cookie)==3){
    return true;
  }
  return false;
}

?>