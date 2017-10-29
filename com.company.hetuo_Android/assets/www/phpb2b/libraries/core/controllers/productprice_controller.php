<?php
class Productprice extends PbController {
	var $name = "Productprice";
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
				$url = "product/price.php?title=".rawurlencode($title);
			}else{
				$url = "product/price.php?id=".$id;
			}
		}
		return $url;
	}
}
?>