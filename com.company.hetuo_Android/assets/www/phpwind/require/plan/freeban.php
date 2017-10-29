<?php
!function_exists('readover') && exit('Forbidden');

$query = $db->query("SELECT id,uid,fid FROM pw_banuser WHERE type='1' AND startdate+days*86400<".pwEscape($timestamp));
$ids = $uids1 = $uids2 = array();
while ($rt = $db->fetch_array($query)) {
	$ids[] = $rt['id'];
	if ($rt['fid']) {
		$uids2[] = $rt['uid'];
	} else {
		$uids1[] = $rt['uid'];
	}
}
if ($ids) {
	$db->update("DELETE FROM pw_banuser WHERE id IN(".pwImplode($ids).")");
	$uids1 && $db->update("UPDATE pw_members SET groupid='-1' WHERE uid IN(".pwImplode($uids1).")");
	$uids2 && $db->update("UPDATE pw_members m LEFT JOIN pw_banuser b ON m.uid=b.uid AND b.fid>0 SET m.userstatus=m.userstatus&(~1) WHERE b.uid is NULL AND m.uid IN(".pwImplode($uids2).")");
}
?>