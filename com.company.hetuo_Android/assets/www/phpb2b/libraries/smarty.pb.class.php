<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
require(LIB_PATH . "smarty/Smarty.class.php");
class MySmarty extends Smarty {
	var $flash_layout = 'flash';
	var $tpl_ext = '.html';

	function MySmarty()
	{
		global $debug;
		$this->Smarty();
		if (isset($debug)) {
			switch ($debug) {
				case 1:
					error_reporting(E_ALL & ~E_DEPRECATED);
					if(function_exists('ini_set')) {
						ini_set('display_errors', 1);
					}
					break;
				case 2:
					error_reporting(E_ERROR | E_WARNING | E_PARSE | E_NOTICE);
					break;
				case 3:
					error_reporting(E_ALL);
					break;
				case 4:
					error_reporting(E_ALL);
					$GLOBALS['pdb']->debug = true;
					break;
				case 5:
					error_reporting(E_ALL);
					$GLOBALS['pdb']->debug = true;
					$this->debugging   = true;
					break;
				default:
					error_reporting(0);
					if(function_exists('ini_set')) {
						ini_set('display_errors', 0);
					}
					break;
			}
		}
		$this->plugins_dir[] = APP_PATH."slug".DS;
		$this->template_dir = PHPB2B_ROOT ."templates".DS;
		$this->compile_dir = DATA_PATH."templates_c".DS;
		$this->cache_dir = DATA_PATH."templates_cache".DS;
		$this->left_delimiter = "<{";
		$this->right_delimiter = "}>";
		$this->caching = false;
	}
	
	function setCompileDir($compile_dir = '')
	{
		if(!empty($compile_dir)){
			$comp_d = $this->compile_dir.$compile_dir;
			if(!file_exists($comp_d)) pb_create_folder($comp_d);
			$this->compile_dir =$comp_d;
		}
	}
	
	function flash($message_code, $url, $pause = 1, $extra = '') {
		global $theme_name;
		$images = array("failed.png", "success.png", "notice.png");
		$styles = array("error", "true");
		if (empty($message_code) || !$message_code || $message_code=="failed") {
			$image = $images[0];
			$message = L('action_failed', "msg", $extra);
			$style = $styles[0];
		}elseif($message_code=="success" or true===$message_code or strstr("success", $message_code)){
			$image = $images[1];
			$style = $styles[1];
			$message = L("success", "msg", $extra);
		}else{
			$image = $images[2];
			$style = null;
			$message = L($message_code, "msg", $extra);
		}
		$this->assign('action_img', $image);
		$this->assign('action_style', $style);
		$this->assign('url', $url);
		$this->assign('message', $message);
		$this->assign('title', strip_tags($message));
		if($pause!=0){
			$this->assign('redirect', $this->redirect($url, $pause));
		}
		$this->assign('page_title', strip_tags($message));
		if(strstr($this->flash_layout, "/")){
			if (!$this->template_exists($this->flash_layout.$this->tpl_ext)) {
				$this->assign('ThemeName', 'default');
				$this->flash_layout = 'default/flash';
			}
		}
		if (!$this->template_exists($this->flash_layout.$this->tpl_ext)) {
			die($message);
		}
		template($this->flash_layout);
		exit();
	}
	
	function redirect($url, $pause) {
	
		return "<script>\n".
		"function redirect() {\nwindow.location.replace('$url');\n}\n".
		"setTimeout('redirect();', ".($pause*1000).");\n".
		"</script>";
	}
}
?>