<?php
!defined('M_P') && exit('Forbidden');

!$winduid && Showmsg('not_login');
if (!$db_appo || !$db_appifopen) Showmsg('app_close');

/*** userapp **/
if ($db_siteappkey && $db_appo) {
	$app_array = array();
	$appclient = L::loadClass('appclient');
	$app_array = $appclient->userApplist($winduid);
}
/*** userapp **/

$param = array();

$pw_query = base64_decode(urldecode(str_replace('&#61;','=',$_GET['pw_query'])));

if ($pw_query) {
	$param['pw_query'] = base64_encode($pw_query);
}
$param['pw_appId']		= $_GET['id'];
$param['pw_uid']		= $winduid;
$param['pw_siteurl']	= $db_bbsurl;
$param['pw_sitehash']	= $db_sitehash;
$param['pw_t']			= $timestamp;

$url = $db_server_url . '/apps.php?';
foreach ($param as $key => $value) {
	$url .= "$key=" . urlencode($value) . '&';
}
$hash = $param['pw_appId'] . '|' . $param['pw_uid'] . '|' . $param['pw_siteurl'] . '|' . $param['pw_sitehash'] . '|' . $param['pw_t'];
$url .= 'pw_sig=' . md5($hash . $db_siteownerid);

require_once(M_P.'require/header.php');
require_once PrintEot('m_app');footer();
?>