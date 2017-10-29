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
require("../libraries/common.inc.php");
uses("news", "typeoption","newstype");
require("session_cp.inc.php");
require(CACHE_PATH. 'cache_type.php');
require(LIB_PATH. 'snoopy.class.php');
$news = new Newses();
$newstype = new Newstypes();
$snoopy = new Snoopy();
$typeoption = new Typeoption();
$conditions = array();
$tpl_file = "news.gather";
if (isset($_POST['gather']) && !empty($_POST['rules'])) {
	$rules = stripslashes($_POST['rules']);
	$tmp_rules = explode("\r\n", $rules);
	if (!empty($tmp_rules) && count($tmp_rules)==4) {
		list($remote_url, $remote_url_match, $remote_title_match, $remote_content_match) = $tmp_rules;
	}else{
		flash();
	}
	$snoopy->fetchlinks($remote_url);
	$urls = array();
	$urls = $snoopy->results;
	if (empty($urls)) {
		flash();
	}
	foreach ($urls as $key=>$value)
	{
		//fetched url
		if (!preg_match($remote_url_match,$value))
		{
			unset($urls[$key]);
		}
	}
	$urls = array_unique($urls);
	$u = 0;
	$sql[] = $title = $content = array();
	foreach ($urls as $key=>$value)
	{
		$snoopy = new Snoopy;
		$snoopy->fetch($value);
		$temp = $snoopy->results;
		//title
		if (preg_match($remote_title_match,$temp,$match))
		{
			$title = addslashes(trim($match[1]));
		}
		unset($match);
		if ($_POST['throw_similar']) {
			$sim = $news->findByTitle($title);
			if(empty($sim)){
				//content
				if (preg_match($remote_content_match,$temp,$match))
				{
					$content = addslashes(trim($match[1]));
				}
				$u++;
				$sql[] = "('".$title."','".$content."','".$_POST['data']['type_id']."',".$time_stamp.")";
			}
		}else{
			//content
			if (preg_match($remote_content_match,$temp,$match))
			{
				$content = addslashes(trim($match[1]));
			}
			$u++;
			$sql[] = "('".$title."','".$content."','".$_POST['data']['type_id']."',".$time_stamp.")";
		}
	}
	$sql = array_filter($sql);
	if (!empty($sql)) {
		$ins_str = "INSERT INTO {$tb_prefix}newses (title,content,type_id,created) VALUES ".implode(",", $sql);
		$result = $pdb->Execute($ins_str);
	}
	if ($result) {
		flash("success");
	}else{
		flash();
	}
}
setvar("NewstypeOptions", $newstype->getTypeOptions());
setvar("AskAction", $typeoption->get_cache_type("common_option"));
template($tpl_file);
?>