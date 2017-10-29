<?php
!defined ('P_W') && exit('Forbidden');
class PW_ThemeConfig{
	var $config;
	function PW_ThemeConfig () {
		$this->_initConfig();
	}

	function getThemes() {
		$tplPath = $this->config['dir'];
		$temp = array();
		if ($fp1 = opendir($tplPath)) {
			while ($tpldir = readdir($fp1)) {
				if (strpos($tpldir,'.')!==false) continue;
				$ifconfig = $this->getThemeConfigFile($tpldir) ? 1 : 0;
				$temp[] = array('dir'=>$tpldir,'ifconfig'=>$ifconfig);
			}
		}
		return $temp;
	}

	function getPages($theme) {
		if (strpos($theme,'.')!==false) return array();
		$tplPath = $this->config['dir'].'/'.$theme;
		$temp = array();
		if ($fp1 = opendir($tplPath)) {
			while ($tpldir = readdir($fp1)) {
				if (strpos($tpldir,'.htm')===false || in_array($tpldir,array('header.htm','footer.htm'))) continue;
				$temp[] = $tplPath.'/'.$tpldir;
			}
		}
		return $temp;
	}

	function getThemeConfigFile($theme) {
		$filedir = Pcv($this->config['dir'].'/'.$theme.'/'.$this->config['configfile']);
		if (file_exists($filedir)) {
			return $filedir;
		}
		return false;
	}

	function _initConfig() {
		$this->config = array(
			'dir'=>R_P.'mode/area/themes',
			'configfile'=>'config.php',
		);
	}
}
?>