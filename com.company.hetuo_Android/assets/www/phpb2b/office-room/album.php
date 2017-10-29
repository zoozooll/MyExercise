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
require(LIB_PATH. 'page.class.php');
require(CACHE_PATH. "cache_type.php");
uses("attachment", "album");
check_permission("album");
$attachment_controller = new Attachment('pic');
$attachment = new Attachments();
$album = new Albums();
$tpl_file = "album";
$page = new Pages();
if (empty($companyinfo)) {
	flash("pls_complete_company_info", "company.php", 0);
}
if (isset($_POST['do'])) {
	pb_submit_check('album');
	$vals = $_POST['album'];
	$vals['title'] = $title = trim($vals['title']);
	$vals['description'] = $description = trim($vals['description']);
	$now_album_amount = $attachment->findCount(null, "created>".$today_start." AND member_id=".$_SESSION['MemberID']);
	if (!empty($_FILES['pic']['name'])) {
		$type_id = 1;
		$attach_id = (empty($id))?"album-".$_SESSION['MemberID']."-".($album->getMaxId()+1):"album-".$_SESSION['MemberID']."-".$id;
		$attachment_controller->title = $title;
		$attachment_controller->description = $description;
		$attachment_controller->rename_file = $attach_id;
		$attachment_controller->upload_process($type_id);
	}
	if (!empty($id)) {
		if (empty($attachment_controller->id)) {
			$attachment_id = $pdb->GetOne("SELECT attachment_id FROM {$tb_prefix}albums WHERE id=".$id);
		}else{
			$attachment_id = $attachment_controller->id;
		}
		$sql = "UPDATE {$tb_prefix}attachments a,{$tb_prefix}albums ab SET a.title='".$title."',a.description='".$description."',ab.attachment_id={$attachment_id},type_id='".$vals['type_id']."' WHERE ab.id={$id} AND a.id=".$attachment_id;
	}else{
		if ($g['max_album'] && $now_album_amount>=$g['max_album']) {
			flash('one_day_max');
		}
		if (empty($_FILES['pic']['name'])) {
			flash("failed");
		}
		$sql = "INSERT INTO {$tb_prefix}albums (member_id,attachment_id,type_id) VALUES (".$_SESSION['MemberID'].",'".$attachment_controller->id."','".$vals['type_id']."')";
	}
	$result = $pdb->Execute($sql);
	if (!$result) {
		flash();
	}else{
		pheader("Location:album.php");
	}
}
setvar("AlbumTypes", $_PB_CACHE['albumtype']);
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if($do=="del" && !empty($id)) {
		$result = $album->del(intval($id), "member_id=".$_SESSION['MemberID']);
	}
	if ($do=="edit") {
		if (!empty($id)) {
			$album_info = $pdb->GetRow("SELECT a.title,a.description,ab.id,a.attachment,ab.type_id FROM {$tb_prefix}albums ab LEFT JOIN {$tb_prefix}attachments a ON a.id=ab.attachment_id WHERE ab.member_id=".$_SESSION['MemberID']." AND ab.id={$id}");
			if (!empty($album_info['attachment'])) {
				$album_info['image'] = pb_get_attachmenturl($album_info['attachment'], "../");
			}
			setvar("item", $album_info);
		}
		$tpl_file = "album_edit";
		template($tpl_file);
		exit;
	}
}
$joins[] = "LEFT JOIN {$tb_prefix}albums a ON a.attachment_id=Attachment.id";
$conditions[] = "Attachment.member_id=".$_SESSION['MemberID']." AND Attachment.attachmenttype_id=1";
$amount = $attachment->findCount($joins, $conditions, "Attachment.id");
$page->setPagenav($amount);
$result = $attachment->findAll("Attachment.title,Attachment.description,Attachment.attachment,a.id", $joins, $conditions, "a.id DESC", $page->firstcount, $page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['image'] = pb_get_attachmenturl($result[$i]['attachment'], '../', "small");
	}
	setvar("Items", $result);
	setvar("ByPages", $page->pagenav);
}
template($tpl_file);
?>