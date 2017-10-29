<?php
!defined('P_W') && exit('Forbidden');
class PW_ParsePW{
	var $string;
	var $charset;
	var $loops = array();

	function PW_ParsePW(){
		global $db_charset;
		$this->charset	= strtolower(substr($db_charset,0,2));
	}
	function setString(&$string){
		$this->string	=& $string;
	}

	function execute($tplname,$string){
		global $db_modepages;
		$this->pw_invoke= L::loadDB('invoke');
		$this->pw_invokepiece = L::loadDB('invokepiece');
		$this->setString($string);
		$this->initLoops();
		if (array_key_exists($tplname,$db_modepages)) {
			$this->tplParseLoop();
			$this->tplParsePW();
		}
		$this->tplParsePrint();
		return $this->string;
	}

	function tplParseLoop(){
		preg_match_all('/<loop id="(['.$this->getChinesePreg().'\w]*?)">([^\x00]+?)<\/loop>/'.$this->getPregDecorate(),$this->string,$reg);
		$replace = array();
		foreach ($reg[1] as $key=>$value) {
			$temp = "\nEOT;\n\$pwloops = pwCycleLoops('".$value."');\nprint <<<EOT\nEOT;\nforeach(\$pwloops as \$loopid){print <<<EOT\n";
			$temp.= $reg[2][$key];
			$temp.= "\nEOT;\n}print <<<EOT\n";
			$replace[$key]	= $temp;
			$this->addtoLoops($value);
		}
		$this->string	= str_replace($reg[0],$replace,$this->string);
	}

	function tplParsePW(){
		//preg_match_all('/<pw id="(['.$this->getChinesePreg().'\w]*?)" tplid="(\d+)" \/>/'.$this->getPregDecorate(),$this->string,$reg);
		$reg = $this->_pregPW();
		$replace = $config = $invokes = array();
		foreach ($reg[1] as $id=>$val) {
			$val = trim($val);
			$temp = $this->pw_invoke->getDataByName($val);
			if (!$temp) {
				$tplid	= (int)$reg[2][$id];
				$ifloop	= in_array($val,$this->getLoops()) ? 1 : 0;
				$replace[$id] = $this->_creatInvoke($tplid,$val,$ifloop);
			} else {
				$replace[$id] = $temp['parsecode'];
			}
			$replace[$id]	= $this->_adminDiv($replace[$id],$val);
			$config	= $this->_setPageConfig($val,$config);
			$invokes[] = $val;
		}
		$this->_updatePageConfig($invokes,$config);
		$this->string = str_replace($reg[0],$replace,$this->string);
	}

	function getInvokes($string) {
		//preg_match_all('/<pw id="(['.$this->getChinesePreg().'\w]*?)" tplid="(\d+)" \/>/'.$this->getPregDecorate(),$this->string,$reg);
		$this->setString($string);
		$reg = $this->_pregPW();
		$invokes = array();
		foreach ($reg[1] as $id=>$val) {
			$invokes[] = $val;
		}
		return $invokes;
	}

	function _pregPW() {
		preg_match_all('/<pw id="(['.$this->getChinesePreg().'\w]*?)" tplid="(\d+)" \/>/'.$this->getPregDecorate(),$this->string,$reg);
		return $reg;
	}

	function tplParsePrint(){
		$s = array('/<!--#\s*/','/\s*#-->/','/\s*print <<<EOT\s*EOT;\s*/','/print <<<EOT\s*/','/\s*EOT;/');
		$e = array("\r\nEOT;\r\n","\r\nprint <<<EOT\r\n","\r\n","print <<<EOT\r\n","\r\nEOT;");
		$this->string = preg_replace($s,$e,$this->string);
	}

	function getChinesePreg(){
		if ($this->charset=='ut') {
			return '\x{4e00}-\x{9fa5}';
		} elseif ($this->charset=='bi') {
			return chr(0x40).'-'.chr(0xff);
		} else {
			return chr(0xa1).'-'.chr(0xff);
		}
	}

	function getPregDecorate(){
		if ($this->charset=='ut') {
			return 'ui';
		} else {
			return 'i';
		}
	}

	function _adminDiv($string,$id) {
		$temp	= '<div class="view-hover">';
		if (L::config('area_static_ifon','area_config') == 1) {
			$temp	.= $this->_getEditLink($id)."\n";
		} else {
			$temp	.= "\nEOT;\nif(\$ifEditAdmin){\nprint <<<EOT\n".$this->_getEditLink($id)."\n";
			$temp	.= "\nEOT;\n}print <<<EOT\n";
		}
		$temp	.= $string;
		$temp	.= '</div>';
		return $temp;
	}

	function _setPageConfig($invokename,$config){
		$pieces = $this->pw_invokepiece->getDatasByInvokeName($invokename);
		foreach ($pieces as $value) {
			$key = $value['id'];
			$config[$key] = $value['rang'] == 'fid' ? 1:0;
		}
		return $config;
	}

	function _creatInvoke($tplid,$invokename,$ifloop=0){
		$tpl	= L::loadDB('tpl');
		$tplData= $tpl->getData($tplid);

		$parsetpl	= L::loadClass('ParseTagCode');
		$parsetpl->init($invokename,$tplData['tagcode'],$ifloop);

		$parsecode	= $parsetpl->getParseCode();
		$tagCode	= $parsetpl->getTagCode();
		if ($parsecode) {
			$this->pw_invoke->insertData(array('name'=>$invokename,'tplid'=>$tplid,'tagcode'=>$tagCode,'parsecode'=>$parsecode,'ifloop'=>$ifloop));
		} else {
			Showmsg('invoke_parse_error');
		}
		$invokepiece = $parsetpl->getConditoin();
		if ($invokepiece) {
			$this->pw_invokepiece->insertDatas($invokepiece);
		}
		return $parsecode;
	}



	function initLoops(){
		$this->loops = array();
	}

	function getLoops(){
		return $this->loops;
	}

	function addtoLoops($invokename){
		$this->loops[] = $invokename;
	}

	function _updatePageConfig($invokes,$config){
		global $db_mode,$SCR;
		$temp_fid = 0;
		if ($db_mode=='area' && $SCR=='cate') {
			global $area_cateinfo,$fid;
			if ($fid && isset($area_cateinfo[$fid]) && isset($area_cateinfo[$fid]['tpl'])) {
				$temp_fid = $fid;
			}
		}
		$pw_mpageconfig = L::loadDB('mpageconfig');
		$pw_mpageconfig->insertData(array('mode'=>$db_mode,'scr'=>$SCR,'fid'=>$temp_fid,'invokes'=>serialize($invokes),'config'=>serialize($config)));
	}

	function _getEditLink($id) {
		return '<span class="open-none" id="'.$id.'">管理</span>';
	}
}
?>