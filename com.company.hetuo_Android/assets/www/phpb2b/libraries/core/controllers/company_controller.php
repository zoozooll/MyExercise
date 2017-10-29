<?php
class Company extends PbController {
	var $name = "Company";
	
	function rewrite($userid, $id = 0, $name = null)
	{
		global $subdomain_support, $topleveldomain_support, $rewrite_able;
		$userid = rawurlencode($userid);
		$url = ($rewrite_able)? URL."space/".$userid."/":URL."space.php?userid=".$userid;
		if($subdomain_support){
			$url = "http://".$userid.$subdomain_support."/";
		}
		return $url;
	}
}
?>