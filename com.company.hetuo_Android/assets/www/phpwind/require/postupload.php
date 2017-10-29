<?php
!function_exists('readover') && exit('Forbidden');

InitGP(array('flashatt'),'P');

$attachs = $aids = $elementpic = array();
$ifupload = 0;

foreach ($_FILES as $key => $val) {
	if (!$val['tmp_name'] || $val['tmp_name'] == 'none') {
		unset($_FILES[$key]);
	}
}
$filenum = count($_FILES);

if ($filenum > 0 && $filenum <= $db_attachnum || $flashatt && is_array($flashatt)) {
	if (!$db_allowupload) {
		Showmsg('upload_close');
	} elseif ($foruminfo['allowupload'] && !allowcheck($foruminfo['allowupload'],$groupid,$winddb['groups'])) {
		Showmsg('upload_forum_right');
	} elseif (!$foruminfo['allowupload'] && $_G['allowupload'] == 0) {
		Showmsg('upload_group_right');
	}
	if ($winddb['uploadtime'] < $tdtime) {
		$winddb['uploadnum'] = 0;
	}
	if (is_array($flashatt)) {
		$filenum += count($flashatt);
	}
	if (($winddb['uploadnum'] + $filenum) >= $_G['allownum']) {
		Showmsg('upload_num_error');
	}
	$uploaddb = UploadFile($winduid);

	if ($flashatt && is_array($flashatt)) {
		$flattids = array_keys($flashatt);
		$query = $db->query("SELECT * FROM pw_attachs WHERE tid='0' AND pid='0' AND uid=" . pwEscape($winduid) . " AND aid IN(" . pwImplode($flattids) . ')');
		while ($rt = $db->fetch_array($query)) {
			$value = $flashatt[$rt['aid']];
			$attach_ext = strtolower(substr(strrchr($rt['name'],'.'),1));
			$savedir = '';
			if ($db_attachdir) {
				if ($db_attachdir == 2) {
					$savedir = "Type_$attach_ext";
				} elseif ($db_attachdir == 3) {
					$savedir = 'Mon_'.date('ym');
				} elseif ($db_attachdir == 4) {
					$savedir = 'Day_'.date('ymd');
				} else {
					$savedir = "Fid_$fid";
				}
			}
			$newattname = $fileuplodeurl = preg_replace('/^0_/', "{$fid}_", $rt['attachurl']);
			$savedir && $fileuplodeurl = $savedir.'/'.$fileuplodeurl;
			$dstfile	= "$attachdir/$fileuplodeurl";
			$srcfile	= "$attachdir/mutiupload/$rt[attachurl]";
			$ifthumb	= 0;

			if (in_array($attach_ext,array('gif','jpg','jpeg','png','bmp'))) {
				require_once(R_P.'require/imgfunc.php');
				if (!$img_size = GetImgSize($srcfile,$attach_ext)) {
					Showmsg('upload_content_error');
				}
				$img_size[0] = $img_size['width'];
				$img_size[1] = $img_size['height'];
				unset($img_size['width'],$img_size['height']);
				if ($db_watermark && $forumset['watermark'] && $img_size[2]<'4' && $img_size[0]>$db_waterwidth && $img_size[1]>$db_waterheight && function_exists('imagecreatefromgif') && function_exists('imagealphablending') && ($attach_ext!='gif' || function_exists('imagegif') && ($db_ifgif==2 || $db_ifgif==1 && (PHP_VERSION > '4.4.2' && PHP_VERSION < '5' || PHP_VERSION > '5.1.4'))) && ($db_waterimg && function_exists('imagecopymerge') || !$db_waterimg && function_exists('imagettfbbox'))) {
					ImgWaterMark($srcfile,$db_waterpos,$db_waterimg,$db_watertext,$db_waterfont,$db_watercolor, $db_waterpct,$db_jpgquality);
				}
				if ($db_ifathumb) {
					$thumbdir = "thumb/$fileuplodeurl";
					$thumburl = $db_ifftp ? D_P."data/tmp/thumb_$newattname" : "$attachdir/$thumbdir";
					list($db_thumbw,$db_thumbh) = explode("\t",$db_athumbsize);
					createFolder(dirname($thumburl));
					if ($thumbsize = MakeThumb($srcfile,$thumburl,$db_thumbw,$db_thumbh)) {
						$img_size[0] = $thumbsize[0];
						$img_size[1] = $thumbsize[1];
						$ifthumb = 1;
					}
				}
				//Start elementupdate
				if (($db_ifpwcache & 512) && !$value['needrvrc'] && !$elementpic) {
					$elementpic = array('aid' => $rt['aid'], 'attachurl' => $fileuplodeurl,'ifthumb'=>$ifthumb);
				}
				//End elementupdate
			}
			if (pwFtpNew($ftp,$db_ifftp)) {
				if (!$ftp->upload($srcfile,$fileuplodeurl)) {
					continue;
				}
				P_unlink($srcfile);
				if ($ifthumb == 1) {
					$ftp->mkdir("thumb/$savedir");
					$ftp->upload($thumburl,$thumbdir) && P_unlink($thumburl);
				}
			} elseif (!pwMovefile($dstfile,$srcfile)) {
				continue;
			}
			$ifupload = ($rt['type'] == 'img' ? 1 : ($rt['type'] == 'txt' ? 2 : 3));
			$attachs[$rt['aid']] = array(
				'aid'       => $rt['aid'],
				'name'      => $rt['name'],
				'type'      => $rt['type'],
				'attachurl' => $fileuplodeurl,
				'needrvrc'  => 0,
				'special'	=> 0,
				'ctype'		=> '',
				'size'      => $rt['size'],
				'hits'      => $rt['hits'],
				'desc'		=> str_replace('\\','',$value['desc']),
				'ifthumb'	=> $ifthumb
			);
			$pwSQL = array('fid' => $fid, 'attachurl' => $fileuplodeurl, 'descrip' => $value['desc'], 'ifthumb' => $ifthumb);
			if ($value['needrvrc'] > 0 && ($value['special'] == 1 && $htmlhide == '' && in_array($value['ctype'],$db_enhideset['type']) || $value['special'] == 2 && $htmlsell == '' && in_array($value['ctype'],$db_sellset['type']))) {
				$attachs[$rt['aid']]['needrvrc']	= $pwSQL['needrvrc']	= $value['needrvrc'];
				$attachs[$rt['aid']]['special']		= $pwSQL['special']		= $value['special'];
				$attachs[$rt['aid']]['ctype']		= $pwSQL['ctype']		= $value['ctype'];
			}
			$winddb['uploadtime'] = $timestamp;
			$winddb['uploadnum']++;
			$db->update("UPDATE pw_attachs SET " . pwSqlSingle($pwSQL) . ' WHERE aid=' . pwEscape($rt['aid']));
		}
	}
	foreach ($uploaddb as $value) {
		$value['name'] = addslashes($value['name']);
		if ($special == 4 && $value['id'] == 0) {//商品图标
			$goodsicon = $value['attachurl'];
		}
		if (!$value['ifreplace']) {
			InitGP(array('att_ctype'.$value['id'],'atc_desc'.$value['id']), 'P');
			InitGP(array('atc_needrvrc'.$value['id'], 'att_special'.$value['id']), 'P', 2);
			$value['descrip']	= ${'atc_desc'.$value['id']};
			$value['needrvrc']	= ${'atc_needrvrc'.$value['id']};
			$value['special']	= ${'att_special'.$value['id']};
			$value['ctype']		= ${'att_ctype'.$value['id']};
			if ($value['needrvrc'] > 0 && ($value['special'] == 1 && $htmlhide == '' && in_array($value['ctype'],$db_enhideset['type']) || $value['special'] == 2 && $htmlsell == '' && in_array($value['ctype'],$db_sellset['type']))) {

			} else {
				$value['needrvrc'] = $value['special'] = 0;
				$value['ctype'] = '';
			}
			$db->update("INSERT INTO pw_attachs SET ".pwSqlSingle(array(
				'fid'		=> $fid,				'uid'		=> $winduid,
				'hits'		=> 0,					'name'		=> $value['name'],
				'type'		=> $value['type'],		'size'		=> $value['size'],
				'attachurl'	=> $value['attachurl'],	'needrvrc'	=> $value['needrvrc'],
				'special'	=> $value['special'],	'ctype'		=> $value['ctype'],
				'uploadtime'=> $timestamp,			'descrip'	=> $value['descrip'],
				'ifthumb'	=> $value['ifthumb']
			)));
			$aid = $db->insert_id();
			$attachs[$aid] = array(
				'aid'       => $aid,
				'name'      => stripslashes($value['name']),
				'type'      => $value['type'],
				'attachurl' => $value['attachurl'],
				'needrvrc'  => $value['needrvrc'],
				'special'	=> $value['special'],
				'ctype'		=> $value['ctype'],
				'size'      => $value['size'],
				'hits'      => 0,
				'desc'		=> str_replace('\\','',$value['descrip']),
				'ifthumb'	=> $value['ifthumb']
			);
			$atc_content = str_replace("[upload=$value[id]]","[attachment=$aid]",$atc_content);
		} else {
			$value['needrvrc']	= $replacedb[$value['id']]['needrvrc'];
			$value['special']	= $replacedb[$value['id']]['special'];
			$value['ctype']		= $replacedb[$value['id']]['ctype'];
			$value['descrip']	= $replacedb[$value['id']]['desc'];
			$aid = $replacedb[$value['id']]['aid'];
			$db->update("UPDATE pw_attachs SET ".pwSqlSingle(array(
				'name'		=> $value['name'],			'type'		=> $value['type'],
				'size'		=> $value['size'],			'attachurl'	=> $value['attachurl'],
				'needrvrc'	=> $value['needrvrc'],		'special'	=> $value['special'],
				'ctype'		=> $value['ctype'],			'uploadtime'=> $timestamp,
				'descrip'	=> $value['descrip'],		'ifthumb'	=> $value['ifthumb']
			)) . " WHERE aid=".pwEscape($aid));
			$oldattach[$aid]['name'] = $value['name'];
			$oldattach[$aid]['type'] = $value['type'];
			$oldattach[$aid]['size'] = $value['size'];
			$oldattach[$aid]['ifthumb'] = $value['ifthumb'];
		}
		//Start elementupdate
		if (($db_ifpwcache & 512) && $value['type'] == 'img' && !$value['needrvrc'] && !$elementpic) {
			$elementpic = array('aid' => $aid, 'attachurl' => $value['attachurl'],'ifthumb'=>$value['ifthumb']);
		}
		//End elementupdate
	}
}
unset($_FILES);

/*
* 上传附件积分控制
*/
if ($ifupload && $_G['allowupload'] == 1 && $uploadmoney) {
	require_once(R_P.'require/credit.php');

	if ($uploadmoney < 0 && $credit->get($winduid,$uploadcredit) < abs($uploadmoney)) {
		require_once(R_P.'require/updateforum.php');
		delete_att(array($attachs));
		$creditname = $credit->cType[$uploadcredit];
		Showmsg('upload_money_limit');
	}
	$credit->addLog('topic_upload',array($uploadcredit => $uploadmoney),array(
		'uid'		=> $winduid,
		'username'	=> $windid,
		'ip'		=> $onlineip,
		'fname'		=> $forum[$fid]['name']
	));
	if (!$credit->set($winduid,$uploadcredit,$uploadmoney,false)) {
		require_once(R_P.'require/updateforum.php');
		delete_att(array($attachs));
		Showmsg('undefined_action');
	}
}
foreach ($attachs as $key => $value) {
	$aids[] = $key;
}
$aids && $aids = pwImplode($aids);
$attachs = !empty($attachs) ? addslashes(serialize($attachs)) : '';

pwFtpClose($ftp);
?>