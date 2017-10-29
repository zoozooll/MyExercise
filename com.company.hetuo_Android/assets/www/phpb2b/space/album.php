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
uses("album");
$album = new Albums();
$joins[] = "LEFT JOIN {$tb_prefix}attachments a ON a.id=Album.attachment_id";
$result = $album->findAll("a.title,a.description,Album.id,a.attachment as thumb", $joins, "Album.member_id='".$member->info['id']."'", "Album.id desc");
if (!empty($result)) {
	$count = count($result);
	for($i=0; $i<$count; $i++){
		$result[$i]['image'] = URL. pb_get_attachmenturl($result[$i]['thumb'], '', 'small');
		$result[$i]['middleimage'] = URL. pb_get_attachmenturl($result[$i]['thumb']);
	}
}
setvar("Items", $result);
$space->render("album");
?>