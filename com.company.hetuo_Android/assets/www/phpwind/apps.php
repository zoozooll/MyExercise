<?php
define ( 'SCR', 'app' );
require_once ('global.php');
if (isset($_GET['ajax'])) {
	define('AJAX','1');
}
InitGP ( array ('q') );

if (in_array($q,array('ajax','article','diary','galbum','group','groups','hot','photos','sharelink','share','stopic','write'))) {
	#基础APP接口
	require_once R_P . 'require/app_route.php';
} elseif ($q == 'blooming') {

	InitGP(array('tid'),'G',2);
	!$db_siteappkey && Showmsg('app_not_register');
	@include_once(D_P.'data/bbscache/info_class.php');

	if (array_key_exists('opened_class',$db_threadconfig)) {
		$opened_class = unserialize($db_threadconfig['opened_class']);
	}

	$openclass = array();

	if (is_array($opened_class)) {
		foreach($opened_class as $v) {
			$openclass[$v] = $info_class[$v];
			unset($info_class[$v]);
		}
	} else {
		Showmsg('app_not_blooming_class');
	}

	require_once PrintEot ( 'apps' );
	ajax_footer();

} elseif ($q == 'updata') {
	require_once(R_P.'require/posthost.php');
	include_once(D_P.'data/bbscache/level.php');
	InitGP(array('tid','cid'),'P',2);

	$pw_tmsgs = GetTtable($tid);
	$rt = $db->get_one("SELECT t.tid,subject,content FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON t.tid=tm.tid WHERE t.tid=".pwEscape($tid));

	$systitle = $winddb['groupid'] == '-1' ? '' : $ltitle[$winddb['groupid']];
	$memtitle = $ltitle[$winddb['memberid']];
	$uptitle = $systitle ? $systitle : $memtitle;

	if (!$cid || !$tid) Showmsg('Please select class');
	
	$partner	= md5($db_siteid.$db_siteownerid);
	$content	= pwConvert($rt['content'],'gbk',$db_charset);
	$subject	= pwConvert($rt['subject'],'gbk',$db_charset);
	$windid		= pwConvert($windid,'gbk',$db_charset);
	$uptitle	= pwConvert($uptitle,'gbk',$db_charset);

	$para = array(
		'tid'		=> $rt['tid'],
		'cid'		=> $cid,
		'upposter'	=> $windid,
		'uptitle'	=> $uptitle,
		'subject'	=> $subject,
		'rf'		=> $pwServer['HTTP_REFERER'],
		'sitehash'	=> $db_sitehash,
		'action'	=> 'updata',
	);

	ksort($para);
	reset($para);

	$arg = '';
	foreach ($para as $key => $value) {
		$arg .= "$key=".urlencode($value)."&";
	}

	$verify = md5(substr($arg,0,-1).$partner);

	if (strpos($content,'[attachment=') !== false) {
		preg_replace("/\[attachment=([0-9]+)\]/eis","upload('\\1')",$content,$db_cvtimes);
	}

	$data =  PostHost("http://app.phpwind.net/pw_app.php?","action=updata&tid=$rt[tid]&cid=$cid&upposter=$windid&uptitle=$uptitle&sitehash=$db_sitehash&subject=".urlencode($subject)."&content=".urlencode($content)."&verify=$verify&rf=".urlencode($pwServer['HTTP_REFERER']),"POST");

	$backdata = substr($data,strpos($data,'$backdata=')+10);
	$backdata = pwConvert($backdata,$db_charset,'gbk');

	Showmsg($backdata);

} elseif ($q == 'survey') {
	@include_once(D_P."data/bbscache/survey_cache.php");
	require_once(R_P.'require/header.php');
	InitGP(array('itemid'),'G',2);
	if (!$itemid) {
		foreach ($survey_cache as $itemdb) {
			$itemid = $itemdb['itemid'] > $itemid ? $itemdb['itemid'] : $itemid;
		}
	}
	$survey = $survey_cache[$itemid];

	require_once PrintEot('apps');
	footer();
}  elseif ($q == 'appthread') {#新增app帖子交换的弹出框
	InitGP(array('do'),'G');
	InitGP(array('forumid'),'G',2);
	!$db_siteappkey && Showmsg ( 'app_siteappkey_notexist' );
    $appclient = L::loadClass('appclient');
	$app_url = $appclient->getThreadsUrl('index','main',$do,$forumid);
	require_once PrintEot('apps');
	ajax_footer();

}  elseif ($q == 'music') {
	InitGP(array('page'),GP,2);
	InitGP(array('keyword'));

	if (!$db_xiami_music_open || $db_apps_list['11']['status'] != 1) {
		echo "close\t";
		ajax_footer();
	}
	$numofpage = GetCookie('numofpage');
	$page > $numofpage && $page = $numofpage;

	!$db_siteappkey && Showmsg ( 'app_siteappkey_notexist' );
	
	$datadb = array();
    $appclient = L::loadClass('appclient');
	$datadb = $appclient->getMusic($page,$keyword);

	if ($datadb == 'close') {
		echo "close\t";
		ajax_footer();
	}

	$musicdb	= $datadb['music'];
	$numofpage	= $datadb['page'];
	$total		= $datadb['total'];
	Cookie('numofpage',$numofpage);

	(!is_numeric($page) || $page < 1) && $page = 1;
	$db_perpage = 8;
	$page > $numofpage && $page = $numofpage;
	$pages = numofpage_music($total,$page,$numofpage,"apps.php?q=music&ajax=1&keyword=$keyword&",null,1);
	
	$music_list_html = '<div class="musicshow">';
	if (is_array($musicdb)) {
		$music_list_html .= '<ul>';
		foreach ($musicdb as $value) {
			$value['song_name_s'] = substrs($value['song_name'],30);
			$music_list_html .= "<li><span>[<a onclick=\"insert_xiami_music('$value[song_id]')\" href=\"javascript:;\">".getLangInfo('other','music_insert')."</a>]</span><p><a title=\"$value[song_name]\" href=\"javascript:;\" onclick=\"insert_xiami_music('$value[song_id]')\">$value[song_name_s] -- $value[artist_name]</a></p><input type=\"hidden\" id=\"$value[song_id]\" value=\"$value[song_info]\"/></li>";
		}
		$music_list_html .= "</ul>$pages";
	} else {
		$music_list_html .= "<p align=\"center\" style=\"height:70px; line-height:50px\"><span class=\"musicresult\" style=\"background:url($imgpath/post/c_editor/music_none.gif) no-repeat 0 0; display:inline-block; padding-left:55px\">".getLangInfo('other','music_none1')."<span class=\"musicred\"><a href=\"javascript:;\">$keyword</a></span>".getLangInfo('other','music_none2')."</span></p>";
		
	}
	$music_list_html .= "</div>";

	echo "success\t$music_list_html";

	ajax_footer();
} else {
	InitGP(array('id'),'G',2);
	! $winduid && Showmsg ( 'not_login' );
	if (!$db_appbbs || !$db_appifopen) Showmsg ('app_close');

	$param = array ();

	$pw_query = base64_decode ( urldecode ( str_replace ( '&#61;', '=', $_GET ['pw_query'] ) ) );

	if ($pw_query) {
		$param ['pw_query'] = base64_encode ( $pw_query );
	}
	$param ['pw_appId']		= $id;
	$param ['pw_uid']		= $winduid;
	$param ['pw_siteurl']	= $db_bbsurl;
	$param ['pw_sitehash']	= $db_sitehash;
	$param ['pw_t']			= $timestamp;
	$param ['pw_bbsapp']	= 1;

	$url = $db_server_url . '/apps.php?';

	foreach ( $param as $key => $value ) {
		$url .= "$key=" . urlencode ( $value ) . '&';
	}
	$hash = $param ['pw_appId'] . '|' . $param ['pw_uid'] . '|' . $param ['pw_siteurl'] . '|' . $param ['pw_sitehash'] . '|' . $param ['pw_t'];
	$url .= 'pw_sig=' . md5 ( $hash . $db_siteownerid );

	require_once (R_P . 'require/header.php');
	require_once PrintEot ( 'apps' );
	footer ();
}
function upload($aid) {
	global $db,$content,$db_bbsurl,$attachpath;
	$rt = $db->get_one("SELECT attachurl,type FROM pw_attachs WHERE aid='$aid'");
	if ($rt['attachurl']) {
		if ($rt['type'] == 'img') {
			$img = "[img]$db_bbsurl/$attachpath/".$rt['attachurl']."[/img]";
			$content = addslashes(str_replace("[attachment=$aid]",$img,$content));
		} else {
			$content = addslashes(str_replace("[attachment=$aid]",'',$content));
		}
	}
}

//xiami_music 分页
function numofpage_music($count,$page,$numofpage,$url,$max=null,$ajaxurl='') {
	global $tablecolor;
	$total = $numofpage;
	if (!empty($max)) {
		$max = (int)$max;
		$numofpage > $max && $numofpage = $max;
	}
	if ($numofpage <= 1 || !is_numeric($page)) {
		return '';
	} else {
		list($url,$mao) = explode('#',$url);
		$mao && $mao = '#'.$mao;
		$pages = "<div class=\"pages\"><a style=\"cursor:pointer;\" class=\"b\"".($ajaxurl ? " onclick=\"return getMusic('1')\"" : '').">&laquo;</a>";
		for ($i = $page-3; $i <= $page-1; $i++) {
			if($i<1) continue;
			$pages .= "<a style=\"cursor:pointer;\"".($ajaxurl ? " onclick=\"return getMusic('$i')\"" : '').">$i</a>";
		}
		$pages .= "<b>$page</b>";
		if ($page < $numofpage) {
			$flag = 0;
			for ($i = $page+1; $i <= $numofpage; $i++) {
				$pages .= "<a style=\"cursor:pointer;\"".($ajaxurl ? " onclick=\"return getMusic('$i')\"" : '').">$i</a>";
				$flag++;
				if ($flag == 4) break;
			}
		}
		$pages .= "<a style=\"cursor:pointer;\" class=\"b\"".($ajaxurl ? " onclick=\"return getMusic('$numofpage')\"" : '').">&raquo;</a><span class=\"pagesone\">Pages: $page/$total&nbsp; &nbsp; &nbsp;Go <input type=\"text\" size=\"3\" onkeydown=\"javascript: if(event.keyCode==13){".($ajaxurl ? "getMusic(this.value);" : " location='{$url}page='+this.value+'{$mao}';")."return false;}\"></span></div>";
		return $pages;
	}
}
?>