<?php
class Product extends PbController {
	var $name = "Product";
	
	function rewrite($id, $title = null)
	{
		$url = null;
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($title)) {
				$url = "product/".rawurlencode($title)."/";
			}else{
				$url = "product/detail/".$id.".html";
			}
		}else{
			if ($rewrite_compatible && !empty($title)) {
				$url = "product/content.php?title=".rawurlencode($title);
			}else{
				$url = "product/content.php?id=".$id;
			}
		}
		return $url;
	}
}
?>