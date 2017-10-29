<?php 
/*
	class.Template.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/
class Template {
	public $__muant = array();//template value
	public $templatefile = NULL;//templatefile  �ļ���
	public $templatedir = NULL;//templatedir ·��
	public $templatecachedir = NULL;//���� ·��
	
	public function diapaly($compile) {
		$style = Common::getSiteInfo('value11');
		$style = empty($style) ? __SETTING_STYLE : $style . '/';
		if(!is_bool($compile)) {
			$compile =  Common::getSiteInfo('value12');
		}
		//$compiledfile = $this -> getDir($this->templatefile).$this -> getDirfile($this->templatefile) .'.php';
		$compiledfile = $this->templatefile . '.php';
		$compileddirfile = __ROOT_TPLS_TPATH."templates_c/".$style.$compiledfile;
		if(!empty($compile)) {
			$this -> compilerfile($compiledfile, $this -> templatefile, $style);
		}
		//$__muant = $this -> tvars;
		if(file_exists($compileddirfile)) {
			error_reporting(E_ALL ^ E_NOTICE);
			include($compileddirfile);
		} else {
			throw new Exception("$compileddirfile not exist.");
		}
		return;
	}
	
	public function compilerfile($compiledfile, $templatefile, $style) {
		$createdTime = File::getFileCreatedTime($compiledfile, __ROOT_TPLS_TPATH.$style."templates_c/");
		if(File::getFileCreatedTime($templatefile, __ROOT_TEMPLATES_TPATH.$style) > $createdTime) {
			$content = File::readContents($templatefile, __ROOT_TEMPLATES_TPATH.$style);
			$content = TemplateCompiler::compiler($content, $style);
			File::creatFile($compiledfile, $content, __ROOT_TPLS_TPATH."templates_c/".$style);
		}
	}
	
	public function getDirfile($templatefile) {
		$tmp = explode('/', $templatefile);
		$num = count($tmp) - 1;
		return $tmp[$num];
	}
	
	public function getDir($templatefile) {
		$tmp = explode('/', $templatefile);
		$num = count($tmp) - 1;
		$str = NULL;
		for($i = 0;$i < $num; $i++) {
			$str .= $tmp[$i].'/';
		}
		return $str;
	}
	
	public function setTpl($name) {
		$this -> templatefile = $name;
	}
	
	public function setVar($key, $value ='') {
		if(is_array($key)) {
			foreach($key as $k => $v) {
				if($k != '') {
					$this->__muant[$k] = $v;
				}
			}
		} else {
			if($key != '') {
				$this->__muant[$key] = $value;
			}
		}
	}
}
?>