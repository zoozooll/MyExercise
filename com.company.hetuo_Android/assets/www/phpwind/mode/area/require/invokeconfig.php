<?php
!defined('P_W') && exit('Forbidden');
function getSubjectByTid($tid,$param) {
	global $db;
	$tid	= (int) $tid;
	if (!$tid) return false;
	$thread	= $db->get_one("SELECT tid,fid,author,authorid,subject,type,postdate,hits,replies FROM pw_threads WHERE tid=".pwEscape($tid));
	$thread['url'] 	= 'read.php?tid='.$thread['tid'];
	$thread['title'] 	= $thread['subject'];
	$thread['titlealt'] 	= $thread['subject'];
	$thread['image']	= '';
	$thread['forumname']= getForumName($thread['fid']);
	$thread['forumurl']	= getForumUrl($thread['fid']);
	$temp	= array();
	foreach ($param as $key=>$value) {
		$temp_2	= '';
		if ($key == 'descrip') {
			$temp_2 = getDescripByTid($tid);
		} elseif ($key == 'image') {
			$temp[$key]	= getImagesByTid($tid);
			continue;
		} elseif (array_key_exists($key,$thread)) {
			$temp_2 = $thread[$key];
		} else {
			$temp_2 = '';
		}
		$temp[$key]	= PW_tplGetData::_analyseResultByParameter($temp_2,$value);
	}
	return $temp;
}
function getImagesByTid($tid) {
	global $db;
	$temp	= array();
	$query	= $db->query("SELECT attachurl FROM pw_attachs WHERE tid=".pwEscape($tid,false)." AND type='img' LIMIT 5");
	while($rt = $db->fetch_array($query)){
		$a_url	= geturl($rt['attachurl'],'show');
		$temp[] = is_array($a_url) ? $a_url[0] : $a_url;
	}
	return $temp;
}

function getParamName($type,$stamp='subject') {
	if ($type=='title') {
		if ($stamp=='forum') {
			return getLangInfo('other','element_title_forum');
		} elseif($stamp=='user') {
			return getLangInfo('other','element_title_user');
		} elseif($stamp=='tag') {
			return getLangInfo('other','element_title_tag');
		} else {
			return getLangInfo('other','element_title');
		}
	}
	return getLangInfo('other','element_'.$type);
}

function getParamDiscrip($type,$stamp='subject') {
	if ($type=='title') {
		$temp = getParamName($type,$stamp);
		return $title.$temp;
	} elseif ($type=='descrip') {
		return getLangInfo('other','element_descrip').getLangInfo('other','element_length');
	} elseif ($type=='image') {
		return getLangInfo('other','element_image_size');
	} else {
		return getLangInfo('other','element_'.$type).getLangInfo('other','set_param_type');
	}
}

function getForumOption($cateid) {
	global $db;
	$query	= $db->query("SELECT f.fid,f.fup,f.ifsub,f.childid,f.type,f.name,f.style,f.f_type,f.cms,f.ifhide,fd.aid,fd.aids,fd.aidcache FROM pw_forums f LEFT JOIN pw_forumdata fd ON f.fid=fd.fid ORDER BY f.vieworder,f.fid");
	$catedb = $forumdb = $subdb1 = $subdb2 =  $fname= array();
	while ($forum = $db->fetch_array($query)) {
		$fname[$forum['fid']] = str_replace(array("\\","'",'<','>'),array("\\\\","\'",'&lt;','&gt;'), strip_tags($forum['name']));
		if ($forum['type'] == 'category') {
			$catedb[] = $forum;
		} elseif ($forum['type'] == 'forum') {
			$forumdb[$forum['fup']] || $forumdb[$forum['fup']] = array();
			$forumdb[$forum['fup']][] = $forum;
		} elseif ($forum['type'] == 'sub') {
			$subdb1[$forum['fup']] || $subdb1[$forum['fup']] = array();
			$subdb1[$forum['fup']][] = $forum;
		} else {
			$subdb2[$forum['fup']] || $subdb2[$forum['fup']] = array();
			$subdb2[$forum['fup']][] = $forum;
		}
	}
	$forumcache = '';
	foreach ($catedb as $cate) {
		if (!$cate||$cate['fid']!=$cateid) continue;
		if (!$cate['cms']) {
			$forumcache .= "<option value=\"$cate[fid]\">>> {$fname[$cate[fid]]}</option>\r\n";
		}
		if (!$forumdb[$cate['fid']]) continue;
		foreach ($forumdb[$cate['fid']] as $forum) {
			if (!$forum['cms']) {
				$forumcache .= "<option value=\"$forum[fid]\"> &nbsp;|- {$fname[$forum[fid]]}</option>\r\n";
			}
			if (!$subdb1[$forum['fid']]) continue;
			foreach ($subdb1[$forum['fid']] as $sub1) {
				if (!$sub1['cms']) {
					$forumcache .= "<option value=\"$sub1[fid]\"> &nbsp; &nbsp;|-  {$fname[$sub1[fid]]}</option>\r\n";
				}
				if (!$subdb2[$sub1['fid']]) continue;

				foreach ($subdb2[$sub1['fid']] as $sub2) {
					if (!$sub2['cms']) {
						$forumcache .= "<option value=\"$sub2[fid]\">&nbsp;&nbsp; &nbsp; &nbsp;|-  {$fname[$sub2[fid]]}</option>\r\n";
					}
				}
			}
		}
	}
	return $forumcache;
}
function passOverKeys($key) {
	$temp = array(
		'tagrelate',
		'titlealt',
		'descrip',
	);
	if (in_array($key,$temp)) {
		return true;
	}
	return false;
}
?>