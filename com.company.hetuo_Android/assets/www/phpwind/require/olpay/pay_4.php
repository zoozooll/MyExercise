<?php
!function_exists('readover') && exit('Forbidden');

$pwSQL = array();
$invcodes = $fistinvcode = '';
for ($i = 0; $i < $rt['number']; $i++) {
	$invcode = randstr(16);
	$i == 0 && $fistinvcode = $invcode;
	$invcodes .= ($invcodes ? "\n" : '') . $invcode;
	$pwSQL[] = array(
		'invcode'	=> $invcode,
		'uid'		=> 0,
		'createtime'=> $timestamp
	);
}
if ($pwSQL) {
	$db->update("INSERT INTO pw_invitecode (invcode,uid,createtime) VALUES " . pwSqlMulti($pwSQL));
	$inv_id = $db->insert_id();
	$db->update("UPDATE pw_clientorder SET paycredit=" . pwEscape($inv_id) . ' WHERE id=' . pwEscape($rt['id']));
}
require_once(R_P.'require/sendemail.php');
$sendinfo = sendemail($rt['payemail'],'email_invite_subject','email_invite_content','email_additional');

$ret_url = '$regurl?invcode=' . $fistinvcode;
?>