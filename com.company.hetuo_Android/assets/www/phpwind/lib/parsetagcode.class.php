<?php
!defined ('P_W') && exit('Forbidden');
class PW_ParseTagCode{
	var $ifloop;
	var $tagCode;
	var $parsecode;
	var $condition = array();
	var $index;

	function init($name,$string,$ifloop) {
		$this->index	= 1;
		$this->condition= array();
		$this->parsecode= '';

		$this->tagCode	= $string;
		$this->setName($name);
		$this->setIfloop($ifloop);
		$this->parseTagCode($string);
	}
	function setName($name) {
		if (!$name) showmsg('invoke name must required');
		$this->name	= $name;
	}

	function setIfloop($ifloop) {
		$this->ifloop	= $ifloop;
	}
	function getParseCode() {
		return $this->parsecode;
	}

	function getConditoin() {
		return $this->condition;
	}

	function getTagCode() {
		return $this->tagCode;
	}

	function initCondition($array) {
		if (isset($array['action'])) {
			$array['action'] = strtolower($array['action']);
		}
		if (!isset($array['title'])) {
			$array['title'] = $this->name."_".$this->index;
			$this->index++;
		}
		foreach ($this->condition as $value) {
			if ($value['title']==$array['title']) {
				showmsg('invoke_title_have_exist');
			}
		}
		$array['num'] = (int)$array['num'] ? (int)$array['num']:10;
		$array['invokename'] = $this->name;
		return $array;
	}

	function parseTagCode($string) {
		preg_match_all('/<list(.+?)\/>([^\x00]+?<loop>)([^\x00]+?)(<\/loop>)/i',$string,$reg);
		$replace = array();
		foreach ($reg[1] as $id=>$val) {
			$this->condition[$id] = $this->parsePWPiece($val);
			$replace[$id]	= $this->parsePwStart($id);
			$reg[2][$id]	= $this->parseLoopStart($reg[2][$id]);
			$replace[$id]	.= $reg[2][$id];
			$reg[3][$id]	= $this->parseParam($reg[3][$id],$id);
			$replace[$id]	.= $reg[3][$id];
			$reg[4][$id]	= $this->parseLoopEnd($reg[4][$id]);
			$replace[$id]	.= $reg[4][$id];
		}
		$string	= str_replace($reg[0],$replace,$string);
		$this->parsecode = $string;
	}

	function parsePwStart($id) {
		if ($this->ifloop) {
			return "\r\nEOT;\r\n\$pwresult = pwTplGetData('".$this->name."','".$this->condition[$id]['title']."',\$loopid);\r\nprint <<<EOT\r\n";
		}
		return "\r\nEOT;\r\n\$pwresult = pwTplGetData('".$this->name."','".$this->condition[$id]['title']."');\r\nprint <<<EOT\r\n";
	}

	function parseCycle($string) {
		if ($this->ifloop) {
			$start	= "\r\nEOT;\r\n\$pwloops = pwCycleLoops('".$this->name."','".$this->condition[$id]['title']."');\r\nprint <<<EOT\r\n";
			$start .= "\r\nEOT;\r\nforeach(\$pwloops as \$loop){print <<<EOT\r\n";
			$end	= "\r\nEOT;\r\n}print <<<EOT\r\n";
			$string	= $start.$string.$end;
		}
		return $string;
	}

	function parseLoopStart($string) {
		return str_replace("<loop>","\r\nEOT;\r\nforeach(\$pwresult as \$key=>\$val){print <<<EOT\r\n",$string);
	}
	function parseLoopEnd($string) {
		return str_replace("</loop>","\r\nEOT;\r\n}print <<<EOT\r\n",$string);
	}

	function parsePWPiece($string) {
		$temp = array();
		preg_match_all("/[a-zA-Z\-_]+\s?=\s?(['|\"]?).*?(\\1)/",$string,$match);
		foreach ($match[0] as $pwReg){
			$pos = strpos($pwReg,"=");
			$key = trim(strtolower(substr($pwReg,0,$pos)));
			$value = trim(substr($pwReg,$pos+1));
			if (preg_match("/^('|\")(.*?)(\\1)$/",$value,$newValue)) {
				$value = trim($newValue[2]);
			}
			$temp[$key] = $value;
		}
		$temp = $this->initCondition($temp);
		return $temp;
	}

	function parseParam($string,$id) {
		$temp_conditon	= array();
		preg_match_all('/\{([\w\,\-:\s]+?)\}/',$string,$mat);
		$replace_3 = array();
		foreach ($mat[1] as $k=>$v) {
			if (strpos($v,',') === false) {
				$key = trim($v);
				$fomat = 'default';
			} else {
				$pos = strpos($v,",");
				$key = trim(strtolower(substr($v,0,$pos)));
				$fomat = trim(substr($v,$pos+1));
			}
			if (array_key_exists($key,$temp_conditon)) {
				if ($temp_conditon[$key] == $fomat) {
					unset($mat[0][$k]);
					continue;
				} elseif ($fomat != 'default') {
					$temp_conditon[$key] = $fomat;
				}
			} else {
				$temp_conditon[$key] = $fomat;
			}
			$replace_3[]	= $this->_getParamCodeByKey($key);
		}
		$string = str_replace($mat[0],$replace_3,$string);
		$this->condition[$id]['param'] = $temp_conditon;
		return $string;
	}
	function _getParamCodeByKey($key) {
		if ($key == 'tagrelate') {
			$temp = "\r\nEOT;\r\nif(\$val[".$key."] && is_array(\$val[".$key."])){\r\nforeach(\$val[".$key."] as \$key_1=>\$val_1){print <<<EOT\r\n";
			$temp .= "<li><a href=\"\$val_1[url]\" target=\"_blank\">\$val_1[title]</a></li>";
			$temp .= "\r\nEOT;\r\n}}print <<<EOT\r\n";
			return $temp;
		} else {
			return '$val['.$key.']';
		}
	}
}
?>