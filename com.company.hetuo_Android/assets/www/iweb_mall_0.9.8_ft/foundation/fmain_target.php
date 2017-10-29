<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function getAppId(){
	if(isset($_GET['app'])){
		return $_GET['app'];
	} else {
		return 'start';
	}
}

function getActId(){
	if(isset($_GET['act'])){
		return $_GET['act'];
	} else {
		echo 'para err';
		exit;
	}
}

function getUrlPara(){
	$urlTmp=$_SERVER["REQUEST_URI"];
	if(substr_count($urlTmp,'?')==1){
		$urlArr=explode("?",$urlTmp);
		return '?'.$urlArr[1];
	} else {
		return '';
	}
}
?>