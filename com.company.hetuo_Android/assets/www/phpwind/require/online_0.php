<?php
!function_exists('readover') && exit('Forbidden');

$index_whosonline = '<div><table align="center" cellspacing="0" cellpadding="0" width="99%"><tr>';
$flag = -1;
$onlinearray = openfile(D_P."data/bbscache/online.php");
$count_ol = count($onlinearray);
if ($onlinearray[0] == '') $count_ol = 0;

for ($i = 1; $i < $count_ol; $i++) {
	if (strpos($onlinearray[$i],"\t") !== false) {
		$onlinedb = explode("\t",$onlinearray[$i]);
		$inread = '';
		if ($onlinedb[4]) $inread = '(Read)';
		if (strpos($db_showgroup,",".$onlinedb[5].",") !== false) {
			$img = $onlinedb[5];
		} else {
			$img = '6';
		}
		if ($onlinedb[9] == '<>') {
			if ($groupid == 3) $adminonly = "&#38544;&#36523;:$onlinedb[0]\n";
			$img = '6';
			$onlinedb[0] = "&#38544;&#36523;&#20250;&#21592;";
			$onlinedb[8] = 0;
		} else {
			$adminonly = '';
		}
		if ($groupid == '3') {
			$adminonly = "{$adminonly}I P : $onlinedb[2]\n";
		}
		$onlineinfo = "$adminonly&#35770;&#22363;: $onlinedb[6]{$inread}\n&#26102;&#38388;: $onlinedb[7]";
		$flag++;
		if ($flag % 7 == 0) $index_whosonline .= '</tr><tr>';
		$index_whosonline .= "<td style=\"border:0;width:14%\"><img src='$imgpath/$stylepath/group/$img.gif' align='bottom'> <a href=u.php?action=show&uid=$onlinedb[8] title='$onlineinfo'>$onlinedb[0]</a></td>";
	}
}
unset($onlinearray);

if ($db_showguest == 1) {
	$guestarray = openfile(D_P."data/bbscache/guest.php");
	$unregcount = count($guestarray);
	if ($guestarray[0] == '') $userunreg = 0;
	for ($i = 1; $i < $unregcount; $i++) {
		if (strpos($guestarray[$i],"\t") !== false) {
			$guestdb = explode("\t",$guestarray[$i]);
			$inread = '';
			if ($guestdb[3]) $inread = '(Read)';
			if ($groupid == '3') {
				$ipinfo = "I P : {$guestdb[0]}\n";
			}
			$onlineinfo = "$ipinfo&#35770;&#22363;: $guestdb[4]{$inread}\n&#26102;&#38388;: $guestdb[5]";
			$flag++;
			if ($flag % 7 == 0) $index_whosonline .= '</tr><tr>';
			$index_whosonline .= "<td style=\"border:0;width:14%\"><img src='$imgpath/$stylepath/group/2.gif' align='bottom'> <a title='$onlineinfo'>guest</a></td>";
		}
	}
	unset($guestarray);
}
$index_whosonline .= '</tr></table></div>';
?>