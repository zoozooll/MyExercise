<?php
!function_exists('readover') && exit('Forbidden');

$get_today = get_date($timestamp,'Y-m-d');
$todayyear = substr($get_today,0,strpos($get_today,'-'));
$gettoday = substr($get_today,strpos($get_today,'-')+1);
$query = $db->query("SELECT uid,username,bday,gender,newpm FROM pw_members WHERE RIGHT(bday,5)=".pwEscape($gettoday)."LIMIT 100");
$msg_a = array();

$msg_title = getLangInfo('other','send_title');
while ($rt = $db->fetch_array($query)) {
	$birthday = substr($rt['bday'],strpos($rt['bday'],'-') + 1);
	if ($gettoday == $birthday) {
		$birthyear = substr($rt['bday'],0,strpos($rt['bday'],'-'));
		$age = $todayyear - $birthyear;
		$writemsg = $rt['username'];
		if ($rt['gender'] == 1) {
			$writemsg .= getLangInfo('other','men');
		} elseif ($rt['gender'] == 2) {
			$writemsg .= getLangInfo('other','women');
		}
		$writemsg .= getLangInfo('other','send_content',array('age'=>$age));
		$msg_a[] = array($rt['uid'],'0','System','rebox','1',$timestamp,$msg_title,$writemsg);
	}
}
if ($msg_a) {
	require_once(R_P.'require/msg.php');
	send_msgc($msg_a);
}
?>