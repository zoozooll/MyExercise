<?php
$res=opendir("langpackage");
$lp_dir=readdir($res);
$language=array();
while($lp_dir=readdir($res)){
	if(!preg_match("/^\./",$lp_dir)){
		$language[]=$lp_dir;
	}
}
$language_array=array(
	'zh-cn'=>'zh',
	'zh-tw'=>'ft',
	'zh-hk'=>'ft',
	'zh-mo'=>'ft',
	'zh-sg'=>'ft',
);
$system_array=explode(',',$_SERVER["HTTP_ACCEPT_LANGUAGE"]);
$system_language=$system_array[0];

$langpackage='';
if(isset($_COOKIE['language']) && in_array($_COOKIE['language'],$language)){
	$langpackage=$_COOKIE['language'];
}elseif(isset($language_array[$system_language])){
	$langpackage=$language_array[$system_language];
}
if($langpackage){
	$langPackagePara = $langpackage;
	$langPackageBasePath = 'langpackage/'.$langPackagePara.'/';	
} else {
	$langpackage = $langPackagePara;
}
?>