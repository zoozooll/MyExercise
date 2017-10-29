<?php

//图片水印处理函数
function run_img_watermark($img,$waterstring,$quality){
  if(!$waterstring) return;
  $img_info=@getimagesize($img);
  switch($img_info[2]){
    case 1:
    $water_img=@imagecreatefromgif($img);
    break;
    case 2:
    $water_img=@imagecreatefromjpeg($img);
    break;
    case 3:
    $water_img=@imagecreatefrompng($img);
    break;
  }
  if($water_img){
    //putenv('GDFONTPATH='.realpath('.'));
    //$font='simsun.ttc';//文件太大10M空间有限改Arial但仅限英文了
	$font='arial.ttf';
    $font_size=@function_exists("gd_info")?9:12;
    $black=@imagecolorallocate($water_img,0,0,0);
    $white=@imagecolorallocate($water_img,255,255,255);
    @imagefilledrectangle($water_img,3,$img_info[1]-15,cut_str($waterstring),$img_info[1],$white);  //80长度可改
    //imagestring($water_img,2,3,$img_info[1]-15,$waterstring,$black); //中文不行
    @imagettftext($water_img,$font_size,0,5,$img_info[1]-3,$black,$font,$waterstring);

    switch($img_info[2]){
      case 1:
      @imagegif($water_img,$img,$quality); //100质量最好，取默认75
      break;
      case 2:
      @imagejpeg($water_img,$img,$quality);
      break;
      case 3:
      @imagepng($water_img,$img,$quality);
      break;
    }
    @imagedestroy($water_img);
  }
}

//处理缩略图函数
function run_img_resize($img,$resize_img_name,$limit_width,$limit_height,$quality){
  $img_info=@getimagesize($img);
  $width=$img_info[0];
  $height=$img_info[1];
  if($width>$height){
    $resize_width=$limit_width;
    $resize_height=$limit_height;
  }elseif($width<$height){
    $resize_width=$limit_height;
    $resize_height=$limit_width;
  }else{
    $resize_width=$limit_width;
    $resize_height=$limit_height;
  }
  switch($img_info[2]){
    case 1:
    $img=@imagecreatefromgif($img);
    break;
    case 2:
    $img=@imagecreatefromjpeg($img);
    break;
    case 3:
    $img=@imagecreatefrompng($img);
    break;
  }
  if(!$img) return false;
  if(function_exists("imagecopyresampled")){
    $resize_img=@imagecreatetruecolor($resize_width,$resize_height);
    $white=@imagecolorallocate($resize_img,255,255,255);
    @imagefilledrectangle($resize_img,0,0,$resize_width,$resize_height,$white);// 填充背景色
    @imagecopyresampled($resize_img,$img,0,0,0,0,$resize_width,$resize_height,$width,$height);
  }else{
    $resize_img=@imagecreate($resize_width,$resize_height);
    $white=@imagecolorallocate($resize_img,255,255,255);
    @imagefilledrectangle($resize_img,0,0,$resize_width,$resize_height,$white);// 填充背景色
    @imagecopyresized($resize_img,$img,0,0,0,0,$resize_width,$resize_height,$width,$height);
  }
  //if(file_exists($resize_img_name)) unlink($resize_img_name);
  switch($img_info[2]){
    case 1:
    @imagegif($resize_img,$resize_img_name,$quality); //100质量最好，默认75
    break;
    case 2:
    @imagejpeg($resize_img,$resize_img_name,$quality);
    break;
    case 3:
    @imagepng($resize_img,$resize_img_name,$quality);
    break;
  }
  @imagedestroy($resize_img);
  return true;
}

//计算字符串物理长，生成图片用
function cut_str($str){
  $strlen=strlen($str);
  $n=6; //长一点
  for($i=0;$i<$strlen;$i++){
    if(ord(substr($str,$i,1))>'0xE0'){
      $tmpstr.=substr($str,$i,3);    //utf-8 code
      $i=$i+2;
      $n+=12;
    }else{
      $tmpstr.=substr($str,$i,1);
      $n+=6;
    }
  }
  return $n;
}

?>