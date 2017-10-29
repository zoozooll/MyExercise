<?php
class Dict extends PbController {
	var $name = "Dict";	
	
	function rewrite($id, $word = null)
	{
		global $rewrite_able, $rewrite_compatible;
		if ($rewrite_able) {
			if ($rewrite_compatible && $word) {
				$word = rawurlencode($word);
				$url = URL."dict/word-".$word."/";
			}else{
				$url = URL."dict/detail/".$id.".html";
			}
		}else{
			$url = "dict/detail.php?id=".$id;
		}
		return $url;
	}
}
?>