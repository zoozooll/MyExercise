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
uses("industry");
$industry = new Industries();
$tpl_file = "card";
if (empty($companyinfo)) {
	flash("pls_complete_company_info", "company.php", 0);
}
if (isset($_POST['save'])) {
	pb_submit_check("company");
	$vals = array();
	$vals['link_man'] = $_POST['company']['link_man'];
	$vals['tel'] = $company->getPhone($_POST['data']['telcode'],$_POST['data']['telzone'],$_POST['data']['tel']);
	$vals['fax'] = $company->getPhone($_POST['data']['faxcode'],$_POST['data']['faxzone'],$_POST['data']['fax']);
	$vals['name'] = strip_tags($_POST['company']['name']);
	$vals['mobile'] = strip_tags($_POST['company']['mobile']);
	$vals['email'] = $_POST['company']['email'];
	$company->primaryKey = "id";
	$result = $company->save($vals, "update", $companyinfo['id']);
	if($result){
		flash("success");
	}else{
		flash("action_failed");
	}
}
if(!empty($companyinfo)){
	list(,$companyinfo['telcode'], $companyinfo['telzone'], $companyinfo['tel']) = $company->splitPhone($companyinfo['tel']);
	list(,$companyinfo['faxcode'], $companyinfo['faxzone'], $companyinfo['fax']) = $company->splitPhone($companyinfo['fax']);
}
setvar("item",$companyinfo);
template($tpl_file);
?>