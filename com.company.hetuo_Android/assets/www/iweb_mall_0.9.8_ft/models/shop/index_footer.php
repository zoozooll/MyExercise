<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("foundation/asystem_info.php");

function show_back_lp($def_lp){
	echo "<select name='langpackage' onchange='selectlangpackage (this.value)'>";
	$res=opendir("langpackage");
	while($lp_dir=readdir($res)){
		if(!preg_match("/^\./",$lp_dir)){
			$l_selected='';
			if($lp_dir==$def_lp){$l_selected="selected";}
			$lp_tip=trim(file_get_contents("langpackage"."/".$lp_dir."/"."tip.php"));
			echo "<option value='".$lp_dir."' ".$l_selected.">".$lp_tip."</option>";
		}
	}
	echo "</select>";	
}
?>