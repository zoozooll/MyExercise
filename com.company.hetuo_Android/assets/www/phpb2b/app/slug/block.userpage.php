<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 123 $
 */
function smarty_block_userpage($params, $content, &$smarty) {
	if ($content === null) return;
	global $rewrite_able;
	$conditions = array();
	if (!class_exists("Userpages")) {
		uses("userpage");
		$userpage = new Userpages();
		$userpage_controller = new Userpage();
	}else{
	    $userpage = new Userpages();
		$userpage_controller = new Userpage();
	}
	require(CACHE_PATH. "cache_userpage.php");
	$result = $_PB_CACHE['userpage'];
	if (isset($params['exclude'])) {
		if (strpos($params['exclude'], ",")>0) {
			$tmp_str = explode(",", $params['exclude']);
			if (!empty($tmp_str)) {
				foreach ($tmp_str as $id_val) {
					unset($result[$id_val]);
				}
			}
		}else{
			unset($params['exclude']);
		}
	}
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			if (!empty($result[$i]['url'])) {
				$url = $result[$i]['url'];
			}else{
				if ($rewrite_able) {
					$url = "page/".$result[$i]['name'].".html";
				}else{
					$url = "page.php?name=".$result[$i]['name'];
				}
			}
			$return.= str_replace(array("[link:title]", "[field:title]", "[field:tip]"), array($url, $result[$i]['title'], $result[$i]['digest']), $content);
		}
	}
	return $return;
}
?>