<?php

if(!isset($_POST['x']) || !isset($_POST['y'])||!isset($_POST['src'])){
	die();
}
$x = $_POST['x'];
$y = $_POST['y'];

$path_prestr='../../';
$img_path=$path_prestr.$_POST['src'];
$img_ext=get_ext($img_path);
$img_size=@getimagesize($img_path);
$new_width=trim($_POST['w']);
$new_weight=trim($_POST['h']);

$size_ischanged=0;//是否对图片进行了缩放

if($img_size[0]!=$new_width){
	//echo '<br>size changed!<br>';
	$tmp_path=str_replace('.'.$img_ext, '_tmp.'.$img_ext, $img_path);
	if(create_thumb($img_path,$tmp_path,$new_width,$new_weight)){
		$img_path=$tmp_path;
		$size_ischanged=1;
	}else{
		exit;
	}
}


$ico_path=str_replace('.'.$img_ext, '_ico.'.$img_ext, $img_path);

$temp_img='';
if($img_ext=='jpg'||$img_ext=='jpeg'){
	$temp_img = imagecreatefromjpeg($img_path);
}
if($img_ext=='gif'){
	$temp_img = imagecreatefromgif($img_path);
}
if($img_ext=='png'){
	$temp_img = imagecreatefrompng($img_path);
}

$new_img = imagecreatetruecolor(100,100);
$old_width  = imagesx($temp_img);
$old_height = imagesy($temp_img);
imagecopyresampled($new_img,$temp_img,0,0,$x,$y,100,100,100,100);

if($size_ischanged==1){
	$ico_path=str_replace('_tmp','',$ico_path);
}

if($img_ext=='jpg'||$img_ext=='jpeg'){
	imagejpeg($new_img , $ico_path);
}
if($img_ext=='gif'){
	imagegif($new_img , $ico_path);
}
if($img_ext=='png'){
  imagepng($new_img , $ico_path);
}

imagedestroy($new_img);

if($size_ischanged==1){
	@unlink($img_path);
}

$ico_path=str_replace($path_prestr, '', $ico_path);

echo '<img id="user_ico" src="'.$ico_path.'?a='.rand(0,99999999).'">';
echo '<input type="hidden" name="u_ico_url" value="'.$ico_path.'">';



	function get_ext($file_name) 
	{ 
		$extend=explode("." , $file_name); 
		$va=count($extend)-1;
		$ext_name=trim($extend[$va]);
		if($ext_name=='jpeg'){
			 $ext_name='jpg';
		}
		return $ext_name; 
	} 



  function create_thumb ($src_file,$thumb_file,$t_width,$t_height) {    	  
 
      if (!file_exists($src_file)) return false;    
 
      $src_info = getImageSize($src_file);    
 
      //如果来源图像小于或等于缩略图则拷贝源图像作为缩略图    
      if ($src_info[0] <= $t_width && $src_info[1] <= $t_height) {    
          if (!copy($src_file,$thumb_file)) {    
              return false;    
          }    
          return true;    
      }    
 
      //按比例计算缩略图大小    
      if ($src_info[0] - $t_width > $src_info[1] - $t_height) {    
          $t_height = ($t_width / $src_info[0]) * $src_info[1];    
      } else {    
          $t_width = ($t_height / $src_info[1]) * $src_info[0];    
      }    
 
      //取得文件扩展名    
      $fileext=get_ext($src_file);    
 
      switch ($fileext) {    
          case 'jpg' :    
              $src_img = ImageCreateFromJPEG($src_file); break;    
          case 'png' :    
              $src_img = ImageCreateFromPNG($src_file); break;    
          case 'gif' :    
              $src_img = ImageCreateFromGIF($src_file); break;    
      }    
 
      //创建一个真彩色的缩略图像    
      $thumb_img = @ImageCreateTrueColor($t_width,$t_height);    
 
      //ImageCopyResampled函数拷贝的图像平滑度较好，优先考虑    
      if (function_exists('imagecopyresampled')) {    
          @ImageCopyResampled($thumb_img,$src_img,0,0,0,0,$t_width,$t_height,$src_info[0],$src_info[1]);    
      } else {    
          @ImageCopyResized($thumb_img,$src_img,0,0,0,0,$t_width,$t_height,$src_info[0],$src_info[1]);    
      }    
 
      //生成缩略图    
      switch ($fileext) {    
          case 'jpg' :    
              ImageJPEG($thumb_img,$thumb_file); break;    
          case 'gif' :    
              ImageGIF($thumb_img,$thumb_file); break;    
          case 'png' :    
              ImagePNG($thumb_img,$thumb_file); break;    
      }    
 
      //销毁临时图像    
      @ImageDestroy($src_img);    
      @ImageDestroy($thumb_img);    
 
      return true;    
 
  }
?>