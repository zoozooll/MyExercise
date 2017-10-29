<?php
function stripslashes_deep($val) {
	 return is_array($val)?array_map('stripslashes_deep', $val):stripslashes($val);
}
function add_magic_quotes($val) {
	return is_array($val)?array_map('add_magic_quotes',$val):addslashes($val);
}
function openfile($filename,$check=1,$method="rb"){
	$check && strpos($filename,'..')!==false && exit('What are you doing?');
	if($handle=@fopen($filename,$method)){
		flock($handle,LOCK_SH);
		$filedata=@fread($handle,filesize($filename));
		fclose($handle);
	}
	return $filedata;
}
function writefile($filename,$data,$check=1,$method="rb+",$iflock=1,$chmod=1){
	$check && strpos($filename,'..')!==false && exit('What are you doing?');
	touch($filename);
	$handle=fopen($filename,$method);
	if($iflock){
		flock($handle,LOCK_EX);
	}
	fwrite($handle,$data);
	if($method=="rb+") ftruncate($handle,strlen($data));
	fclose($handle);
	$chmod && @chmod($filename,0777);
}
?>