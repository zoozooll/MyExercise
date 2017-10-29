<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 676 $
 */
function smarty_function_im($params){
	$output = null;
	if(isset($params['img'])){
		$linkimage = URL.$params['img'];
	}
	$val = $params['id'];
	switch ($type = strtolower($params['type'])) {
		case "qq":
			$code = 'href="http://wpa.qq.com/msgrd?V=1&Uin='.$val.'&Site='.URL.'&Menu=yes"';
			break;
		case "skype":
			$code = 'href="skype:'.$val.'?call" onclick="return skypeCheck();"';
			break;
		case "msn":
			$code = 'href="msnim:chat?contact='.$val.'"';
			break;
		case "icq":
			$code = 'href="http://wwp.icq.com/scripts/search.dll?to='.$val.'"';
			break;
		case "yahoo":
			$code = 'href="http://edit.yahoo.com/config/send_webmesg?.target='.$val.'&.src=pg"';
			break;
		default:
			$code = $val;
			break;
	}
	$output = '<a '.$code.'><span class="im_'.$type.'">'.strtoupper($type).'</span></a>';
	echo $output;
}
?>