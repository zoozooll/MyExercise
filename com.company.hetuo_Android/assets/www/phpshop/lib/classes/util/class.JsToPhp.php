<?php 
/*
	class.JsToPhp.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/
class JsToPhp {
	public static function unescape($str, $charcode = 'gb2312'){
		$text = preg_replace_callback("/%u[0-9A-Za-z]{4}/", toUtf8, $str);
		if($charcode == NULL) {
			return $text;
		} else {
			return mb_convert_encoding($text, $charcode, 'utf-8');
		}
	}

	public static function escape($str, $charcode = NULL) {
		if($charcode != NULL) {
			$str = iconv($charcode,"UTF-8", $str);
		}
		preg_match_all("/[\xc2-\xdf][\x80-\xbf]+|[\xe0-\xef][\x80-\xbf]{2}|[\xf0-\xff][\x80-\xbf]{3}|[\x01-\x7f]+/e", $str, $r);
		$str = $r[0];
		$l = count($str);
		for($i=0; $i<$l; $i++){
			$value = ord($str[$i][0]);
			if($value < 223){
				$str[$i] = rawurlencode(utf8_decode($str[$i]));
			}else{
				$str[$i] = "%u".strtoupper(bin2hex(iconv("UTF-8","UCS-2",$str[$i])));
			}
		}
		return join("",$str);
	}
}

?>