<?php
function Root()
{
	$base=$_SERVER['DOCUMENT_ROOT'];
	$root=str_replace("\\","/",dirname(__file__));
	$website=str_replace($base,"",$root);
	return $website;
}
?>