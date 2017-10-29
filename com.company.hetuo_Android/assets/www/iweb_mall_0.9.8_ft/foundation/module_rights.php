<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function check_rights($code){
	if($_SESSION['rights']=='all_priviilege') {
		return 1;
	}
	if($_SESSION['rights']){
		$local_rights = $_SESSION['rights'];
		if(stripos(",,{$local_rights},",",{$code},")){
			return 1;
		}else{
			return 0;
		}
	} else {
		return 0;
	}
}
?>