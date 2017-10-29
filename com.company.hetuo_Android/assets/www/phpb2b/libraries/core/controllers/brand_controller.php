<?php
class Brand extends PbController {
	var $name = "Brand";
	function rewrite($id, $title = null)
	{
		$url = null;
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($title)) {
				$url = "brand/".rawurlencode($title)."/";
			}else{
				$url = "brand/detail/".$id.".html";
			}
		}else{
			if ($rewrite_compatible && !empty($title)) {
				$url = "brand/detail.php?title=".rawurlencode($title);
			}else{
				$url = "brand/detail.php?id=".$id;
			}
		}
		return $url;
	}
}
?>