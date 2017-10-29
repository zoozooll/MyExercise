<?
/**
 * class.RunXmlAPI.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
 */

/**
 * about runtime xml file load and store
 */
class XML {
	protected $localEncoding = "UTF-8";
	
	/**
	 * 装截运行时XML文件
	 * @param Int
	 * @return Array
	 */
	public function loadToArray($pathfile) {
		$doc = new DOMDocument();
		
		$doc->load($pathfile);
		$root = $doc->documentElement;
		$arr = $this->nodeToArray($root);
		
		return $arr;
	}
	
	/**
	 * 取得XML字符串到数组
	 */
	public function parseToArray($str) {
//		$doc = new DOMDocument();
		$doc = DOMDocument::loadXML($str);
		$root = $doc->documentElement;
		$arr = $this->nodeToArray($root);
		
		return $arr;
	}

	/**
	 * 保存运行时XML文件
	 * @param Int
	 * @return Array
	 */
	public function storeFromArray($pathfile, $data) {
		$doc = new DOMDocument("1.0", "UTF-8");
		$doc->formatOutput = true; 


		$rootElement = $doc->createElement('Root');
		$doc->appendChild($rootElement);
		//将数组转换XML节点		
		$this->arrayToNode($doc, $rootElement, $data);

		$xml = $doc->saveXML();
		
		//保存到文件
		if(($fp=fopen($pathfile, "w+")) == false) {
			throw new Exception("can not open [$pathfile](r).");
		}
		chmod($pathfile, 0666); //设置文件权限
		$len = fwrite($fp, $xml);
		if($len < strlen($xml)) {
			throw new Exception("fwrite string length is wrong [$len].");
			fclose($fp);
		}
		fclose($fp);
	}

	/**
	 * XML Node转换到Array
	 */
	public function nodeToArray($node) {
		$arr = array();

		if(is_object($node) == false) {
			return NULL;
		}
		//取得Attributes
		if ($node->hasAttributes()) {
			foreach ($node->attributes as $attribute) {
				$arr[$this->decode($attribute->name)] = 
					$this->decode($attribute->value);
			}
		}
		//取得nodeValue
		//无子节点,则返回(注:Text Node 也算一个节点)
		if($node->hasChildNodes() == false) {
			return $arr;
		}
		
		foreach($node->childNodes as $element) {
			$nodeName = $element->nodeName;
			if(empty($nodeName) || substr($nodeName, 0, 1) == "#") {
				continue;
			}
			
			if(is_object($element->nodeValue)) {
				echo "object.".$element->nodeValue;
				continue;
			}
			$arr[$element->nodeName][] = $this->nodeToArray($element);
		}
		return $arr;
	}

	/**
	 * Array转换到XML Node
	 */
	public function arrayToNode($doc, $parentNode, $arr) {
		foreach($arr as $key=>$value) {
			if(empty($key)) {
				continue;
			}
			
			$element = $doc->createElement($key);
			if(is_array($value)) {
				$this->arrayToNode($doc, $element, $value);
			} else {
				//编码
				$value = $this->encode($value);
				$element->appendChild($doc->createTextNode($value));
			}
			$parentNode->appendChild($element);
		}
	}

	protected function &encode(&$str) {
		if($this->localEncoding == NULL) {
			return $str;
		}
		$str = iconv($this->localEncoding, "UTF-8//IGNORE", $str);
		$str = str_replace($this->enclosure, $this->enclosure.$this->enclosure, $str);

		return $str;
	}

	protected function &decode(&$str) {
		if($this->localEncoding == NULL) {
			return $str;
		}
		$str = iconv("UTF-8", $this->localEncoding."//IGNORE", $str);
		$str = str_replace($this->enclosure.$this->enclosure, $this->enclosure, $str);

		return $str;
	}

}
?>