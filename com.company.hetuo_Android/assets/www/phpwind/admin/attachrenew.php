<?php
!function_exists('adminmsg') && exit('Forbidden');

$basename="$admin_file?adminjob=attachrenew";
($attach_url || $db_ifftp) && adminmsg('attachrenew_forbidden');
InitGP(array('action'));
if (empty($action)) {
	include PrintEot('attachrenew');exit;
} else {
	InitGP(array('step'));
	$step	= $step ? (int) $step : 0;
	$prenum = 10000;
	$attachDB = L::loadDB('attachs');
	$attachs = $attachDB->groupByTidAndPid($step,$prenum);
	$tTables = $pTables = array();
	foreach ($attachs as $key=>$value) {
		if ($value['pid']) {
			$pTable = GetPtable('N',$value['tid']);
			$pTables[$pTable][] = $value['pid'];
		} else {
			$tTable = GetTtable($value['tid']);
			$tTables[$tTable][] = $value['tid'];
		}
	}
	foreach ($tTables as $table=>$value) {
		$db->update("UPDATE $table SET aid=1 WHERE tid IN (".pwImplode($value).")");
	}
	foreach ($pTables as $table=>$value) {
		$db->update("UPDATE $table SET aid=1 WHERE pid IN (".pwImplode($value).")");
	}

	$maxAid = $attachDB->getTableStructs('Auto_increment');
	if ($maxAid>($step+1)*$prenum) {
		$step++;
		adminmsg('attach_renew_wait',EncodeUrl("$basename&action=$action&step=$step"),1);
	} else {
		adminmsg('attach_renew');
	}
}
?>