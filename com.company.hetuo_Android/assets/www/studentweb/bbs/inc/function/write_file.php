<?php

//写文件
function write_file($file,$text){
  if(!file_exists($file)){
    if(!@touch($file)){
      err('操作失败！原因分析：文件'.$file.'不存在或不可创建或读写，可能是当前运行环境权限不足');
    }
  }
  @chmod($file,0777);
  if(is_writable($file) && ($fp=@fopen($file,'rb+'))){
    f_lock($fp);
    @ftruncate($fp,0);
    if(strlen($text)>0 && !@fwrite($fp,$text)){
      err('操作失败！原因分析：文件'.$file.'不存在或不可创建或读写，可能是权限不足！');
	}
    @flock($fp,LOCK_UN);
    fclose($fp);
  }else{
    err('操作失败！原因分析：文件'.$file.'不存在或不可读写');
  }
}

//锁定文件
function f_lock($fp){
  if($fp){
    if(!flock($fp,LOCK_EX)){
      sleep(1);
      f_lock($fp);
    }
  }
}

?>