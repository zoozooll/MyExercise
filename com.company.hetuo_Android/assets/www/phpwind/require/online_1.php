<?php
!function_exists('readover') && exit('Forbidden');

isset($forum) || include(D_P.'data/bbscache/forum_cache.php');

$onlinedb = $gusetdb = array();

$query = $db->query("SELECT username,lastvisit,ip,fid,tid,groupid,action,ifhide,uid FROM pw_online" . (empty($db_showguest) ? ' WHERE uid!=0' : ''));

while ($rt = $db->fetch_array($query)) {
	if ($rt['uid']) {
		$inread = $rt['tid'] ? '(Read)' : '';
		if (strpos($db_showgroup,",".$rt['groupid'].",") !== false) {
			$rt['img'] = $rt['groupid'];
		} else {
			$rt['img'] = '6';
		}
		if ($rt['ifhide']) {
			if ($groupid == 3) {
				$adminonly  = "&#38544;&#36523;:$rt[username]\n";
			}
			$rt['img']		= '6';
			$rt['username'] = '&#38544;&#36523;&#20250;&#21592;';
			$rt['uid']		= 0;
		} else {
			$adminonly = '';
		}
		if ($groupid == '3') {
			$adminonly = "{$adminonly}I P : $rt[ip]\n";
		}
		$fname  = $forum[$rt['fid']]['name'];
		$action = $fname ? substrs(strip_tags($fname),13) : getLangInfo('action',$rt['action']);
		$rt['lastvisit']  = get_date($rt['lastvisit'],'m-d H:i');
		$rt['onlineinfo'] = "$adminonly&#35770;&#22363;: $action{$inread}\n&#26102;&#38388;: $rt[lastvisit]";
		$onlinedb[] = $rt;
	} else {
		$inread = $rt['tid'] ? '(Read)' : '';
		$rt['img'] = '2';
		$rt['username'] = 'guest';

		if ($groupid == '3') {
			$ipinfo = "I P : {$rt[ip]}\n";
		}
		$fname  = $forum[$rt['fid']]['name'];
		$action = $fname ? substrs(strip_tags($fname),13) : getLangInfo('action',$rt['action']);
		$rt['lastvisit']  = get_date($rt[lastvisit],'m-d H:i');
		$rt['onlineinfo'] = "$ipinfo&#35770;&#22363;: $action{$inread}\n&#26102;&#38388;: $rt[lastvisit]";
		$gusetdb[] = $rt;
	}
}
if ($db_showguest) {
	$onlinedb = array_merge($onlinedb,$gusetdb);
}
$index_whosonline = '<div><table align="center" cellspacing="0" cellpadding="0" width="99%"><tr>';
$flag = -1;
foreach ($onlinedb as $key => $val) {
	$flag++;
	if ($flag % 7 == 0) $index_whosonline .= '</tr><tr>';
	$index_whosonline .= "<td style=\"border:0;width:14%\"><img src=\"$imgpath/$stylepath/group/$val[img].gif\" align=\"bottom\"> <a href=\"u.php?action=show&uid=$val[uid]\" title=\"$val[onlineinfo]\">$val[username]</a></td>";
}
$index_whosonline .= '</tr></table></div>';
?>