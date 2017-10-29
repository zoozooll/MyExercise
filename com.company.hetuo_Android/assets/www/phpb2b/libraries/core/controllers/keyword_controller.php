<?php
class Keyword extends PbController {
	var $name = "Keyword";
	var $keywords;
	
	/**
	 * rewrite tag url
	 *
	 * @param mixed $titles
	 */
	function rewrite($id, $title = null)
	{
		$url = null;
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($title)) {
				$url = "tag/".rawurlencode($title)."/";
			}else{
				$url = "tag/detail/".$id.".html";
			}
		}elseif(!empty($title)){
			$url = "tag.php?name=".rawurlencode($title);
		}else{
			$url = "tag.php?id=".$id;
		}
		return $url;
	}	
}