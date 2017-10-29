<?php
class Tag extends PbController {
	var $name = "Tag";

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
			$url = "tag/".rawurlencode($title)."/";
		}else{
			$url = "offer/list.php?do=search&q=".rawurlencode($title);
		}
		return $url;
	}	
}
?>