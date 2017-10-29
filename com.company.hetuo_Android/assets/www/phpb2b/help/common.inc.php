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
uses("helptype","help");
$helptype = new Helptypes();
$help = new Helps();
$conditions = array();
$helptype_result = $helptype->findAll("id,title",null, "parent_id=0","display_order ASC,id DESC");
if (!empty($helptype_result)) {
	foreach ($helptype_result as $key=>$val) {
		$helptype_result[$val['id']]['id'] = $val['id'];
		$helptype_result[$val['id']]['name'] = $val['title'];
		$sub_result = $pdb->GetArray("SELECT id,title FROM {$tb_prefix}helptypes WHERE parent_id='".$val['id']."'");
		if (!empty($sub_result)) {
			foreach ($sub_result as $key1=>$val1) {
				$helptype_result[$val['id']]['sub'][$val1['id']]['id'] = $val1['id'];
				$helptype_result[$val['id']]['sub'][$val1['id']]['name'] = $val1['title'];
			}
		}
	}
	setvar("Helptypes", $helptype_result);
}
?>