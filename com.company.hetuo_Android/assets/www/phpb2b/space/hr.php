<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision$
 */
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
uses("job","typeoption");
$job = new Jobs();
$typeoption = new Typeoption();
$conditions = "company_id=".$company->info['id'];
if (isset($_GET['id'])) {
	$id = intval(($_GET['id']));
	if ($id) {
		$info = $job->read("*", intval($_GET['id'], $conditions));
		if (empty($info)) {
			flash('data_not_exists');
		}
		$tpl_file = "hr_detail";
		setvar("Salary",$typeoption->get_cache_type("salary"));
        setvar("Worktype",$typeoption->get_cache_type("work_type"));
		setvar("Gender",$typeoption->get_cache_type("gender"));
		setvar("Education",$typeoption->get_cache_type("education"));
		setvar("item",$info);
		$space->render($tpl_file);
		exit;
	}
}
$result = $job->findAll("*", null, $conditions, "id DESC", 0, 10);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['url'] = $space->rewriteDetail("hr", $result[$i]['id']);
	}
	setvar("Items", $result);
}
$sql = "UPDATE {$tb_prefix}jobs SET clicked=clicked+1 WHERE status=1 AND company_id='".$company->info['id']."'";
$pdb->Execute($sql);

setvar("Items",$result);
$space->render("hr");
?>