<?php
class Expo extends PbController {
	var $name = "Expo";
	
	function rewrite($id, $name = null)
	{
		$url = null;
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($name)) {
				$url = "fair/".rawurlencode($name)."/";
			}else{
				$url = "fair/detail/".$id.".html";
			}
		}else{
			if ($rewrite_compatible && !empty($name)) {
				$url = "fair/detail.php?title=".rawurlencode($name);
			}else{
				$url = "fair/detail.php?id=".$id;
			}
		}
		return $url;
	}
}
?>