<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');

/*
* 用户组分享权限
*/
if ($shareGM == 1) {
	Showmsg('share_group_right');
}

InitGP(array('link','descrip','ifhidden','type'),'P',1);
$share = array();

$link	= str_replace('&#61;','=',$link);
$ifhidden = (int)$ifhidden;

if ($type && in_array($type,array('user','photo','album','group','diary','topic','reply'))) {
	$type_tmp = $type;
	define('AJAX','1');
	$id	= (int)GetGP('id');
	!$id && Showmsg('data_error');
	if ($type=='user') {
		if ($id==$winduid) showmsg('data_error');
		$userdb = getOneInfo($id);
		empty($userdb) && Showmsg('data_error');
		$link	= $db_bbsurl.'/u.php?uid='.$id;
		$type	= 'user';
		$share['user']['username']	= $userdb['username'];
		$share['user']['image']	= $userdb['face'];
	} elseif ($type=='photo') {
		$photo = $db->get_one("SELECT p.pid,p.aid,p.path,p.uploader,p.ifthumb,a.aname,a.private,a.ownerid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid WHERE p.pid=" . pwEscape($id) . " AND a.atype='0'");
		empty($photo) && Showmsg('data_error');
		$link	= $db_bbsurl.'/{#APPS_BASEURL#}q=photos&space=1&a=view&pid='.$id;
		$type	= 'photo';
		$share['photo']['uid']	= $photo['ownerid'];
		$share['photo']['username']	= $photo['uploader'];
		$share['photo']['image']	= getphotourl($photo['path'],$photo['ifthumb']);
	} elseif ($type=='album') {
		$album 	= $db->get_one("SELECT aname,ownerid,owner,lastphoto FROM pw_cnalbum WHERE atype='0' AND aid=" . pwEscape($id));
		empty($album) && Showmsg('data_error');
		$link	= $db_bbsurl.'/{#APPS_BASEURL#}q=photos&space=1&a=album&aid='.$id;
		$type	= 'album';
		$share['album']['uid']	= $album['ownerid'];
		$share['album']['username']	= $album['owner'];
		$share['album']['image']	= getphotourl($album['lastphoto']);
	} elseif ($type=='group') {
		$group 	= $db->get_one("SELECT id,cname,cnimg FROM pw_colonys WHERE id=" . pwEscape($id));
		empty($group) && Showmsg('data_error');
		if ($group['cnimg']) {
			list($cnimg) = geturl("cn_img/$group[cnimg]",'lf');
		} else {
			$cnimg = $pwModeImg.'/nophoto.gif';
		}
		$link	= $db_bbsurl.'/{#APPS_BASEURL#}q=group&cyid='.$id;
		$type	= 'group';
		$share['group']['name']	= $group['cname'];
		$share['group']['image']	= $cnimg;
	} elseif ($type=='diary') {
		$diary 	= $db->get_one("SELECT did,subject,uid FROM pw_diary WHERE did=" . pwEscape($id));
		empty($diary) && Showmsg('data_error');

		$link	= $db_bbsurl.'/{#APPS_BASEURL#}q=diary&space=1&u='.$diary['uid'].'&did='.$id;
		$type	= 'diary';
		$share['diary']['subject'] = $diary['subject'];
	} elseif ($type == 'topic') {
		$pw_tmsgs = GetTtable($id);
		$topicdb = $db->get_one("SELECT t.tid,t.authorid,t.subject,t.author,tm.content FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON t.tid=tm.tid WHERE t.tid=".pwEscape($id));
		empty($topicdb) && Showmsg('data_error');

		$link	= $db_bbsurl.'/read.php?tid='.$id;
		$type	= 'topic';
		$share['topic']['subject'] = $topicdb['subject'];

		require_once(R_P.'require/bbscode.php');
		$topicdb['content'] = strip_tags(convert($topicdb['content'],$db_windpost));
		$topicdb['content'] = substrs($topicdb['content'],100,'N');

		$attimages = array();
		$query = $db->query("SELECT attachurl,ifthumb FROM pw_attachs WHERE tid=".pwEscape($topicdb['tid'],false)." AND pid=0 AND type='img' LIMIT 4");
		while ($rt = $db->fetch_array($query)) {
			$a_url = geturl($rt['attachurl'],'show',$rt['ifthumb']);
			if ($a_url != 'nopic') {
				$attimages[$rt['attachurl']] = is_array($a_url) ? $a_url[0] : $a_url;
			}
		}
		$attimages = serialize($attimages);
		$share['topic']['abstract'] = $topicdb['content'];
		$share['topic']['imgs'] = $attimages;
	} elseif ($type == 'reply') {
		InitGP(array('tid'));
		$pw_posts = GetPtable('N',$tid);

		$replydb = $db->get_one("SELECT p.pid,p.tid,p.subject as psubject,p.author,p.authorid,p.postdate,p.content,t.subject as tsubject FROM $pw_posts p LEFT JOIN pw_threads t ON p.tid=t.tid WHERE p.pid=".pwEscape($id));
		empty($replydb) && Showmsg('data_error');

		$link = $db_bbsurl.'/job.php?action=topost&tid='.$tid.'&pid='.$id;
		$type = 'topic';
		$share['topic']['subject'] = $replydb['psubject'] ? $replydb['psubject'] : 'Re:'.$replydb['tsubject'];
		require_once(R_P.'require/bbscode.php');
		$replydb['content'] = strip_tags(convert($replydb['content'],$db_windpost));
		$replydb['content'] = substrs($replydb['content'],100,'N');

		$attimages = array();
		$query = $db->query("SELECT attachurl FROM pw_attachs WHERE uid=".pwEscape($replydb['authorid'],false)." AND pid=".pwEscape($id,false)." AND type='img' LIMIT 5");
		while ($rt = $db->fetch_array($query)) {
			$a_url = geturl($rt['attachurl'],'show');
			if ($a_url != 'nopic') {
				$attimages[$rt['attachurl']] = is_array($a_url) ? $a_url[0] : $a_url;
			}
		}
		$attimages = serialize($attimages);
		$share['topic']['abstract'] = $replydb['content'];
		$share['topic']['imgs'] = $attimages;

	}
}
PostCheck(1,$o_share_gdcheck,$o_share_qcheck);
/**
* 禁止受限制用户发言
*/
banUser();

$ifhidden!=0 && $ifhidden!=1 && $ifhidden = 0;
if (!$link) {
	ObHeader('mode.php?m=$m&q=share');
}
!preg_match("/^https?\:\/\/.{4,255}$/i", $link) && Showmsg('mode_share_link_error');

if (strlen($descrip)>300) Showmsg('mode_share_descrip_toolang');
require_once(R_P.'require/postfunc.php');

require_once(R_P.'require/bbscode.php');
$wordsfb = L::loadClass('FilterUtil');
if (($banword = $wordsfb->comprise($descrip)) !== false) {
	Showmsg('post_wordsfb');
}

$share['link'] = $link;
$share['descrip'] = $descrip;

$parselink = parse_url($link);
if (!$type) {
	if(preg_match("/(youku.com|youtube.com|sohu.com|sina.com.cn)$/i",$parselink['host'],$hosts)) {
		$hash = getVideo($link,$hosts[1]);
		if(!empty($hash)) {
			$type = $share['type'] = 'video';
			$share['video']['hash'] = $f_hash = $hash;
			$share['video']['host'] = $hosts[1];
		} else {
			$type = $share['type'] = 'web';
		}
	} elseif (preg_match("/\.(mp3|wma)$/i",$link)) {
		$type = $share['type'] = 'music';
		$f_hash = $share['link'];
	} elseif (preg_match("/\.swf$/i",$link)) {
		$type = $share['type'] = 'flash';
		$f_hash = $share['link'];
	} else {
		$type = $share['type'] = 'web';
	}
}
$content = serialize($share);
$arr = array($type,$winduid,$windid,$timestamp,$content,$ifhidden);
$db->update("INSERT INTO pw_share(type,uid,username,postdate,content,ifhidden) VALUES(".pwImplode($arr).")");
if ($type == 'topic') {
	$db->update("UPDATE pw_threads SET shares=shares+1 WHERE tid=".pwEscape($id));
}
if (!$ifhidden) {
	$f_id = $db->insert_id();

	if ($type != 'web' && $f_hash) {
		$share_code = '[share]'.($type=='video'?$share['video']['host']:$type).','.$f_hash.','.$f_id.'[/share]';
	} elseif ($type == 'user') {
		$share_code = '[url='.$share['link'].'][img]'.$share['user']['image'].'[/img][/url]';
		$title	= $share['user']['username'];
	} elseif ($type == 'photo') {
		$belong	= getLangInfo('app','photo_belong');
		$image_link	= $link;
		$share['link']	= $db_bbsurl.'/u.php?uid='.$photo['ownerid'];
		$title	= $share['photo']['username'];
		$share_code = '[url='.$image_link.'][img]'.$share['photo']['image'].'[/img][/url]'.$belong;
	} elseif ($type == 'album') {
		$belong	= getLangInfo('app','photo_belong');
		$image_link	= $link;
		$share['link']	= $db_bbsurl.'/u.php?uid='.$album['ownerid'];
		$title	= $share['album']['username'];
		$share_code = '[url='.$image_link.'][img]'.$share['album']['image'].'[/img][/url]'.$belong;
	} elseif ($type == 'group') {
		$title	= $share['group']['name'];
		$share_code = '[url='.$share['link'].'][img]'.$share['group']['image'].'[/img][/url]'.$belong;
	} elseif ($type == 'diary') {
		$title	= $share['diary']['subject'];
		$share_code = '';
	} elseif ($type == 'topic') {
		$title	= $share['topic']['subject'];
		$abstract= $share['topic']['abstract'];
		$attimages = unserialize($share['topic']['imgs']);
		$imgs = '';
		foreach ($attimages as $k => $v) {
			$imgs .= "[img]".$v."[/img]";
		}
		$share_code = '';
	} else {
		$share_code = '';
	}
	$type_name	= getLangInfo('app',$type);

	!$title && $title = substrs($share['link'],40);

	pwAddFeed($winduid,'share',$f_id,array(
								'lang'=>'share_view',
								'link'=>$share['link'],
								'title'=>$title,
								'descrip'=>$descrip,
								'abstract'=>$abstract,
								'imgs'=>$imgs,
								'share_code'=>$share_code,
								'uid'=>$winduid,
								'username'=>$windid,
								'type_name'=>$type_name)
			);
	$a = 'self';
} else {
	$a = 'my';
}

countPosts('+1');

//积分变动
require_once(R_P.'require/credit.php');
$o_share_creditset = unserialize($o_share_creditset);
$creditset = getCreditset($o_share_creditset['Post']);
$creditset = array_diff($creditset,array(0));
if (!empty($creditset)) {
	$credit->sets($winduid,$creditset,true);
	updateMemberid($winduid);
}

if ($creditlog = unserialize($o_share_creditlog)) {
	addLog($creditlog['Post'],$windid,$winduid,'share_Post');
}
updateUserAppNum($winduid,'share');

$memberShare = array(
	'reply'	=> "memberShareThread",
	'topic'	=> "memberShareThread",
	'diary'	=> "memberShareDiary",
	'album'	=> "memberShareAlbum",
	'user'	=> "memberShareUser",
	'group'	=> "memberShareGroup",
	'photo'	=> "memberSharePic",
	'web'	=> "memberShareLink",
	'video'	=> "memberShareVideo",
	'music'	=> "memberShareMusic"
);
$threadShare = array(
	'topic'	=> "threadShare",
	'diary'	=> "diaryShare",
	'photo'	=> "picShare"
);
$threadFav = array(
	'diary'	=> "diaryFav",
	'photo'	=> "picFav"
);
if (isset($memberShare[$type])) {
	updateDatanalyse($winduid,$memberShare[$type],1);
}
if (isset($threadShare[$type_tmp]) && $ifhidden != 1) {
	updateDatanalyse($id,$threadShare[$type_tmp],1);
} elseif (isset($threadFav[$type])) {
	updateDatanalyse($id,$threadFav[$type],1);
}
if (defined('AJAX')) {
	Showmsg('operate_success');
} else {
	refreshto("{$baseUrl}q=share&a=$a",'operate_success');
}


function getVideo($link, $host) {
	$matches = array();
	switch ($host) {
		case 'youku.com':
			preg_match("/sid\/(\w+)\//",$link,$matches);
			break;
		case 'youtube.com':
			preg_match("/v\=([\w\-]+)/",$link,$matches);
			break;
		case 'sina.com.cn':
			preg_match("/\/(\d+)-(\d+)\.html/",$link,$matches);
			break;
		case 'sohu.com':
			preg_match("/\/(\d+)\/*$/",$link,$matches);
			break;
	}
	if(!empty($matches[1])) {
		$return = $matches[1];
	} else {
		$return = '';
	}
	return $return;
}
?>