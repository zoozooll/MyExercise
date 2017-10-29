<?php
/*
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class Valiimage {
	public static function validateCode($length) {
		$array="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		$authnum = "";
		for($i=0;$i<$length;$i++)	{
			$authnum .=substr($array,rand(0,25),1);
		}
		return $authnum; 
	}
	
	public static function validateMd5() {
		return md5(uniqid(rand(), true));
	}

	public static function generateValidationImage($string = NULL, $width = 72, $height = 30, $d = 0, $num = 10) {
		global $site_font_path;
		$len=strlen($string);
		$bordercolor = "#333333";
		$bgcolor = "#FFFFFF";

		$image = imagecreate($width, $height);
		$bordercolor = self::getcolor($image,$bordercolor);
		imagefilledrectangle($image,0,0,$width+1,$height+1,$bordercolor); 
		$back = self::getcolor($image,$bgcolor);
		imagefilledrectangle($image,1,1,$width-2,$height-2,$back);
		
		$image = self::setnoise($image,$width,$height,$num);
		$size = ceil($width / $len); 
		//取得字体
		if($site_font_path) {
		   $font = $site_font_path;
		} else {
		   $font = 5;
		}
		for($i=0;$i<$len;$i++){
			$TempText=substr($string,$i,1);
			$textColor = imagecolorallocate($image, rand(0, 100), rand(0, 100), rand(0, 100));
			$randsize =trim(rand($size-$size/6, $size+$size/6));
			$randAngle = rand(-15,15);
			$x=8+($width-$width/8)/$len*$i;
			$y=rand($height-3,$height-10);
			imagettftext($image,$randsize,$randAngle,$x,$y,$textColor,$font,$TempText);  
		}
		header("Expires: Mon, 23 Jul 1993 05:00:00 GMT");
		header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
		header("Cache-Control: no-store, no-cache, must-revalidate");
		header("Cache-Control: post-check=0, pre-check=0", false);
		header("Pragma: no-cache");
		header("Content-type: image/png");
		imagejpeg($image);
		imagedestroy($image);
	}
	
	public static function getcolor($image,$color){
		//$color = eregi_replace ("^#","",$color);
		$r = $color[1].$color[2];
		$r = hexdec ($r);
		$b = $color[3].$color[4];
		$b = hexdec ($b);
		$g = $color[5].$color[6];
		$g = hexdec ($g);
		$color = imagecolorallocate ($image, $r, $b, $g); 
		return $color;
	}
	public static function setnoise($image,$width,$height,$noisenum){
		for ($i=0; $i<$noisenum; $i++){
			$randColor = imagecolorallocate($image, rand(0, 255), rand(0, 255), rand(0, 255));
			imagesetpixel($image, rand(0, $width), rand(0, $height), $randColor);
			$line_w1 = rand(0, $width);
			$line_w2 = rand(0, $width);
			$line_h1 = rand(0, $height);
			$line_h2 = rand(0, $height);
			imageline($image, $line_w1, $line_h1, $line_w2, $line_h2, $randColor);
			imageline($image, $line_w1, $line_h1 + 1, $line_w2, $line_h2 + 1, $randColor);
		} 
		return $image;
	}
	
}
?>