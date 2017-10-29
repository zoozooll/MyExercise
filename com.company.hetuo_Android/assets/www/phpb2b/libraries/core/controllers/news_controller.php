<?php
class News extends PbController {
	var $name = "News";

	function rewrite($id, $title = null, $dt = null)
	{
		$url = null;
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($title)) {
				$url = "news/".rawurlencode($title)."/";
			}else{
				$url = "news/detail/".$id.".html";
			}
		}else{
			if ($rewrite_compatible && !empty($title)) {
				$url = "news/detail.php?title=".rawurlencode($title);
			}else{
				$url = "news/detail.php?id=".$id;
			}
		}
		return $url;
	}
}
?>