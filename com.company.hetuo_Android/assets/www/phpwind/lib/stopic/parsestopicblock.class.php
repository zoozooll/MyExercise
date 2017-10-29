<?php
!defined('P_W') && exit('Forbidden');
class PW_ParseStopicBlock{
	var $config;
	var $replacetag;
	var $begin;
	var $loops;
	var $end;
	function PW_ParseStopicBlock() {

	}

	function getConfig() {
		return $this->config;
	}
	function getReplacetag() {
		return $this->replacetag;
	}
	function getBegin() {
		return $this->begin;
	}
	function getLoops() {
		return $this->loops;
	}
	function getEnd() {
		return $this->end;
	}

	function execut($str) {
		$this->config = array();
		$this->replacetag = array();
		if (preg_match('/([^\x00]*?)<loop>([^\x00]+?)<\/loop>([^\x00]*)/i',$str,$reg)) {
			$this->begin= $reg[1];
			$this->loops	= $reg[2];
			$this->confing	= $this->_parseParam($this->loops);
			$this->end	= $reg[3];
		}
	}

	function _parseParam($string){
		preg_match_all('/\{([\w]+?)\}/',$string,$mat);
		foreach ($mat[1] as $k=>$v) {
			if (!in_array($v,$this->config)) {
				$this->config[]	= $v;
				$this->replacetag[]= $mat[0][$k];
			}
		}
	}
}
?>