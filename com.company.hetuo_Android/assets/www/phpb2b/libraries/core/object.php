<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, PHPB2B.com. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 117 $
 */
class PbObject{
	
	var $params;
	var $fontFace = 'incite.ttf';

	function Object() {
		$args = func_get_args();
		if (method_exists($this, '__destruct')) {
			register_shutdown_function (array(&$this, '__destruct'));
		}
		call_user_func_array(array(&$this, '__construct'), $args);
	}
	
	function __construct(){}	
	
	function toString() {
		$class = get_class($this);
		return $class;
	}
}
?>