<?php
/*
	class.GetContent.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
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