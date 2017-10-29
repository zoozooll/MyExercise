<?php
error_reporting(0);
$IWEB_SHOP_IN = true;
require("../foundation/asession.php");
include_once("../configuration.php");
include_once("../foundation/fsession.php");
//随机字符串种子，可以换成字母或其他英文字符
$glbVerifySeed = "123456789ABCDEFGHIJKLMNPRSTUVWXYZ";
main();

function main() {
	$verifyCode = getRandomCode(4);
	set_session('verifyCode',$verifyCode);
	$imgWidth = isset($_REQUEST["width"])?$_REQUEST["width"]:"";
	$imgHeight = isset($_REQUEST["height"])?$_REQUEST["height"]:"";
	$imgFont = isset($_REQUEST["font"])?$_REQUEST["font"]:"";
	
	if($imgWidth == "") $imgWidth = 60;
	if($imgHeight == "") $imgHeight = 20;
	if($imgFont == "") $imgFont = 6;
	doOutputImg($verifyCode, $imgWidth, $imgHeight, $imgFont);
}

//获取随机数字字符串
function getRandomCode($length=5) {
	global $glbVerifySeed;
	
	$bgnIdx = 0;
	$endIdx = strlen($glbVerifySeed)-1;
	
	$code = "";
	for($i=0; $i<$length; $i++) {
		$curPos = rand($bgnIdx, $endIdx);
		$code .= substr($glbVerifySeed, $curPos, 1);
	}
	
	return $code;
}

//输出校验码图像
function doOutputImg($string, $imgWidth, $imgHeight, $imgFont,
	$imgFgColorArr=array(0,0,0), $imgBgColorArr=array(255,255,255)) {
	$image = imagecreatetruecolor($imgWidth, $imgHeight);

	//用白色背景加黑色边框画个方框
	$backColor = imagecolorallocate($image, 255, 255, 255);
	$borderColor = imagecolorallocate($image, 0, 0, 0);
	imagefilledrectangle($image, 0, 0, $imgWidth - 1, $imgHeight - 1, $backColor);
	imagerectangle($image, 0, 0, $imgWidth - 1, $imgHeight - 1, $borderColor);

	$imgFgColor = imagecolorallocate ($image, $imgFgColorArr[0], $imgFgColorArr[1], $imgFgColorArr[2]);
	doDrawStr($image, $string, $imgFgColor, $imgFont);
	doPollute($image, 64);
	header('Content-type: image/png');
	imagepng($image);
	imagedestroy($image);
}

//画出校验码
function doDrawStr($image, $string, $color, $imgFont) {
	$imgWidth = imagesx($image);
	$imgHeight = imagesy($image);
	
	$count = strlen($string);
	$xpace = ($imgWidth/$count);
	
	$x = ($xpace-6)/2;
	$y = ($imgHeight/2-8);
	for ($p = 0; $p<$count; $p ++) {
		$xoff = rand(-2, +2);
		$yoff = rand(-2, +2);
		$curChar = substr($string, $p, 1);
		imagestring($image, $imgFont, $x+$xoff, $y+$yoff, $curChar, $color);
		$x += $xpace;
	}

	return 0;
}

//画出一些杂点
function doPollute($image, $times) {   
	$imgWidth = imagesx($image);
	$imgHeight = imagesy($image);
	for($j=0; $j<$times; $j++) {
		$x = rand(0, $imgWidth);
		$y = rand(0, $imgHeight);
		
		$color = imagecolorallocate($image, rand(0,255), rand(0,255), rand(0,255));
		imagesetpixel($image, $x, $y, $color);
	}
}
?>