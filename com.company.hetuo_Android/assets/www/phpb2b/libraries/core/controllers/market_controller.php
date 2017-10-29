<?php
class Market extends PbController {
	var $name = "Market";

	function rewrite($id, $name = null)
	{
		$url = null;
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($name)) {
				$url = "market/".rawurlencode($name)."/";
			}else{
				$url = "market/detail/".$id.".html";
			}
		}else{
			$url = "market/detail.php?id=".$id;
		}
		return $url;
	}
}
?>