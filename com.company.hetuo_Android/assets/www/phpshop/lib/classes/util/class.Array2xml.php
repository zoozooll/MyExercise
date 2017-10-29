<?php
/*
	class.Array2xml.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class Array2xml {
	public $xml;
	public function Array2xml($array = NULL,$encoding='UTF-8') {
		if($array == NULL) {
			return NULL;
		}
		$this->xml='<?xml version="1.0" encoding="'.$encoding.'"?>';
		$this->xml.=$this->_array2xml($array);
	}
	public function getXml() {
			return $this->xml;
	}
	public function _array2xml($array) {
			foreach($array as $key=>$val) {
					is_numeric($key)&&$key="item id=\"$key\"";
					$xml.="<$key>";
					$xml.=is_array($val)?$this->_array2xml($val):$val;
					list($key,)=explode(' ',$key);
					$xml.="</$key>";
			}
			return $xml;
	}
	
	public function storeFromArray($pathfile, $data) {
		if(($fp=fopen($pathfile, "w+")) == false) {
			throw new Exception("can not open [$pathfile](r).");
		}
		chmod($pathfile, 0666); //
		$len = fwrite($fp, $data);
		if($len < strlen($data)) {
			throw new Exception("fwrite string length is wrong [$len].");
			fclose($fp);
		}
		fclose($fp);
	}
	
}
?>