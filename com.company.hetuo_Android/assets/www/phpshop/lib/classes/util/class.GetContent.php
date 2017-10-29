<?php
/*
	class.GetContent.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class GetContent {

	public static function getCurl($servleturl)  {
		$cUrl = curl_init();
		curl_setopt($cUrl, CURLOPT_URL, $servleturl);
		curl_setopt($cUrl, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($cUrl, CURLOPT_TIMEOUT, 30);
		curl_setopt($cUrl, CURLOPT_USERAGENT, "Connection: Keep-Alive");
		$pageContent = trim(curl_exec($cUrl));
		curl_close($cUrl);
		return $pageContent;
	}
	
	public static function pregmatchContent($preg, $content)  {
		preg_match($preg, $content, $arrMatches);
		return $arrMatches;
	}
	
	public static function pregmatchallContent($preg, $content)  {
		preg_match_all($preg, $content, $arrMatches);
		return $arrMatches;
	}
	
	public static function getSocket($host, $body = NULL, $port = 80) {
		$fp = fsockopen($host, $port, $errno, $errstr, 30);
		if (!$fp) {
			//echo "$errstr ($errno)<br />\n";
			return "$errstr ($errno)<br />\n";
		} else {
			$out = "GET / HTTP/1.1\r\n";
			$out .= "Host: $host\r\n";
			$out .= "Connection: Close\r\n$body";
			fwrite($fp, $out);
			$str = '';
			while (!feof($fp)) {
				$str .= fgets($fp, 128);
			}
			fclose($fp);
			return $str;
		}	
	}
}
?>