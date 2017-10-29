<?php 
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision$
 */
class Segments
{
    var
    $signChar = array("~","!","@","#","\$","%","^","&","*",",",".","?",";",":","/","'",'"',"[","]","{","}","！"," ￥","……","…","、","，","。","？","；","：","‘","“","”","’"," 【","】","～","！","＠","＃","＄","％","＾","＆","＊","，","．"," ＜","＞","；","：","＇","＂","［","］","｛","｝","／","＼","　");
    var $termStr;
    var $hilight_str;
    
    function clear_point($str)
    {
    	return str_replace($this->signChar, ' ', $str);
    }
    
    function formatStr($str)
    {
    	$searchkey = $this->clear_point($str);
    	$match = preg_replace('/\s+/',' ',$searchkey);
    	$this->termStr = explode(" ", $match);
    	foreach ($this->termStr as $key=>$val) {
    		$hilight_str[] = $val;
    	}
    	$this->hilight_str = implode("\",\"", $hilight_str);
    	return str_replace(' ', '%', $match);
    }
}
?>