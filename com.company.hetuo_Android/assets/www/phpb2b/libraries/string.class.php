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
class Strings extends PbObject
{
	var $name;
	var $string;
	
	function Strings()
	{
		
	}
	
	function txt2array($data)
	{
		$datas = explode("\r\n", $data);
		$tmp_str = array();
		if (!empty($datas)) {
			foreach ($datas as $val) {
				$tmp_str[] = $val;
			}
			return $tmp_str;
		}else{
			return false;
		}
	}
	
	function txt2file($data)
	{
		$datas = trim(preg_replace("/(\s*(\r\n|\n\r|\n|\r)\s*)/", "\r\n", $data));
		return $datas;
	}
}
?>