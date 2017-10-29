<?php
!function_exists('readover') && exit('Forbidden');
$query = $db->query("SELECT id,awardee,level FROM pw_medalslogs WHERE action='1' AND state='0' AND timelimit>0 AND awardtime+timelimit*2592000<".pwEscape($timestamp));
$medaldb = $namedb = array();
while ($rt = $db->fetch_array($query)) {
	$medaldb[$rt['awardee']][] = array($rt['id'],$rt['level']);
	$namedb[] = $rt['awardee'];
}
if ($namedb) {
	include_once(R_P.'require/msg.php');
	include_once(D_P.'data/bbscache/medaldb.php');
	$pwSQL = $ids = $medaluser = $message = array();
	$reason = Char_cv(getLangInfo('other','medal_reason'));
	$usernames = pwImplode(array_unique($namedb));
	$query = $db->query("SELECT uid,username,medals FROM pw_members WHERE username IN($usernames)");
	while ($rt = $db->fetch_array($query)) {
		$medals = ",".$rt['medals'].",";
		$medalname = '';
		foreach ($medaldb[$rt['username']] as $key => $value) {
			$ids[] = $value[0];
			$medal = $value[1];
			$pwSQL[] = array($rt['username'],'SYSTEM',$timestamp,$medal,2,$reason);
			$medals = str_replace(",$medal,",',',$medals);
			$medaluser[] = '(uid='.pwEscape($rt['uid']).' AND mid='.pwEscape($medal).')';
			$medalname .= $medalname ? ','.$_MEDALDB[$medal]['name'] : $_MEDALDB[$medal]['name'];
		}
		$metal_cancel = Char_cv(getLangInfo('other','metal_cancel'));
		$metal_cancel_text = Char_cv(getLangInfo('other','metal_cancel_text',array('medalname'=>$medalname)));
		$message[] = array($rt['uid'],'0','SYSTEM','rebox','1',$timestamp,$metal_cancel,$metal_cancel_text);
		$medals = substr($medals,1,-1);
		$db->update("UPDATE pw_members SET medals=".pwEscape($medals,false)."WHERE uid=".pwEscape($rt['uid'],false));
	}
	$pwSQL		&& $db->update("INSERT INTO pw_medalslogs (awardee,awarder,awardtime,level,action,why) VALUES".pwSqlMulti($pwSQL,false));
	$ids		&& $db->update("UPDATE pw_medalslogs SET state='1' WHERE id IN(".pwImplode($ids,false).")");
	$medaluser	&& $db->update("DELETE FROM pw_medaluser WHERE ".implode(' OR ',$medaluser));
	$message	&& send_msgc($message);
	updatemedal_list();
}

function updatemedal_list(){
	global $db;
	$query = $db->query("SELECT uid FROM pw_medaluser GROUP BY uid");
	$medaldb = '<?php die;?>0';
	while($rt=$db->fetch_array($query)){
		$medaldb .= ','.$rt['uid'];
	}
	writeover(D_P.'data/bbscache/medals_list.php',$medaldb);
}
?>