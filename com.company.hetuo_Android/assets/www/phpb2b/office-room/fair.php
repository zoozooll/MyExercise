<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
require("../libraries/common.inc.php");
require("room.share.php");
if (empty($company_id)) {
	flash("no_company_perm");
}
uses("expo");
$fair = new Expoes();
if(isset($_POST['do']) && isset($_POST['id'])){
	if ($fair->checkExist($_POST['id'])) {
		$sql = "INSERT INTO {$tb_prefix}expomembers (expo_id,member_id,company_id,created,modified) VALUE (".$_POST['id'].",".$_SESSION['MemberID'].",".$company_id.",".$time_stamp.",".$time_stamp.")";
		$result = $pdb->Execute($sql);
	}
	if($result){
		echo "<script language='javascript'>window.close();</script>";
		exit;
	}else {
		flash("action_failed", '', 0);
	}
}
?>