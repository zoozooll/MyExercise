<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//post,get对象过滤通用函数

function login_check($post) {
	$MaxSlen=30;//限制登陆验证输入项最多20个字符

	if (!get_magic_quotes_gpc())    // 判断magic_quotes_gpc是否为打开
	{
		$post=addslashes($post);    // 进行magic_quotes_gpc没有打开的情况对提交数据的过滤
	}

	$post = LenLimit($post,$MaxSlen);

	$post=trim(str_replace(" ","",$post));

	$post=cleanHex($post);

	if (strpos($post,"=")||strpos($post,"'")||strpos($post,"\\")||strpos($post,"*")||strpos($post,"#")) {
		return false;
	}else{
		return true;
	}

}



function long_check($post) {
	$MaxSlen=3000;//限制较长输入项最多3000个字符
	if (!get_magic_quotes_gpc())    // 判断magic_quotes_gpc是否为打开
	{
		$post = addslashes($post);    // 进行magic_quotes_gpc没有打开的情况对提交数据的过滤
	}

	$post = LenLimit($post,$MaxSlen);

	$post = str_replace("\'", "’", $post);
	//$post= htmlspecialchars($post);    // 将html标记转换为可以显示在网页上的html
	$post = nl2br($post);    // 回车

	return $post;
}



function big_check($post) {
	$MaxSlen=30000;//限制大输入项最多30000个字符

	if (!get_magic_quotes_gpc())    // 判断magic_quotes_gpc是否为打开
	{
		$post = addslashes($post);    // 进行magic_quotes_gpc没有打开的情况对提交数据的过滤
	}

	$post = LenLimit($post,$MaxSlen);

	$post = str_replace("\'", "’", $post);

	return $post;
}


function short_check($str,$is_hex="") {
	$MaxSlen=300;//限制短输入项最多300个字符

	if (!get_magic_quotes_gpc())    // 判断magic_quotes_gpc是否打开
	{
		$str = addslashes($str);    // 进行过滤
	}

	$str = LenLimit($str,$MaxSlen);

	$str = str_replace("\'", "", $str);

	$str = str_replace("\\", "", $str);

	$str = str_replace("#", "", $str);

	$str = str_replace("=", "", $str);

	$str = htmlspecialchars($str);

	//$str = str_replace(" ", "", $str);

	if (empty($is_hex)) {
		$str = cleanHex($str);
	}
	return trim($str);
}


//过滤16进制
function cleanHex($input){
	$clean = preg_replace("![\][xX]([A-Fa-f0-9]{1,3})!", "",$input);
	return $clean;
}


//限制输入字符长度，防止缓冲区溢出攻击
function LenLimit($Str,$MaxSlen) {
	if(isset($Str{$MaxSlen})){
		return " ";
	}else{
		return $Str;
	}
}

function filt_fields($select_items){
	$sql_part = '';
	if($select_items!='*'){
		if(is_array($select_items)) {
			$sql_part = '`'.implode("`,`",$select_items).'`';
		} else {
			$sql_part = "$select_items";
		}
	} else {
		$sql_part = '*';
	}
	return $sql_part;
}

function filt_num_array($id_str){
	$sql_part = '';
	if($id_str!='*'){
		if(is_array($id_str)) {
			$sql_part = implode(",",$id_str);
		} else {
			$sql_part = "$id_str";
		}
	} else {
		$sql_part = '*';
	}
	return $sql_part;
}
?>