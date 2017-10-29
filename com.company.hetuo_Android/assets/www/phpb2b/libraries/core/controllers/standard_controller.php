<?php
class Standard extends PbController {
	var $name = "Standard";
	function rewrite($id, $title = null)
	{
		$url = null;
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($title)) {
				$url = "standard/".rawurlencode($title)."/";
			}else{
				$url = "standard/detail/".$id.".html";
			}
		}else{
			if ($rewrite_compatible && !empty($title)) {
				$url = "standard.php?title=".rawurlencode($title);
			}else{
				$url = "standard.php?id=".$id;
			}
		}
		return $url;
	}
}
?>