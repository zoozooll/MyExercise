<?php

function cutstr($str,$len){
  $str=strip_tags(trim($str));
  $num=strlen($str);
  for($i=0;$i<$len;$i++){
    $temp_str.=ord(substr($str,$i,1))>127?substr($str,$i,1).substr($str,++$i,1).substr($str,++$i,1):substr($str,$i,1);
    if($i>$num){
      break;
    }
  }
  if($num>$len){
    $temp_str.='...';
  }
  return $temp_str;
}

?>