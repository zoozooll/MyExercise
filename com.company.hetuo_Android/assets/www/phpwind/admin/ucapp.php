<?php
!function_exists('adminmsg') && exit('Forbidden');

if ($uc_server != 1) {
	$basename = "javascript:parent.Window.all['ucapp'].colse();";
	adminmsg('uc_server_set');
}

if (empty($action)) {
	
	require_once(R_P . 'uc_client/class_core.php');
	$uc = new UC();
	$ucApp = $uc->load('app');

	$apps  = array();
	$query = $db->query("SELECT * FROM pw_ucapp");
	while ($rt = $db->fetch_array($query)) {
		$status = $ucApp->ucfopen($rt['siteurl'], $rt['interface'], $rt['secretkey'], 'Site', 'connect');
		if (isset($status['errCode'])) {
			$rt['status'] = $status['errCode'];
			$rt['status'] > 2 && $rt['status'] = 3;
		} else {
			$rt['status'] = -2;
		}
		$apps[] = $rt;
	}
	include PrintEot('ucapp');exit;

} elseif ($action == 'del') {
	
	InitGP(array('selid'), 'P', 2);
	
	if ($selid) {
		$db->update("DELETE FROM pw_ucapp WHERE id IN (" . pwImplode($selid) . ')');
		require_once(R_P . 'uc_client/class_core.php');
		$uc = new UC();
		$myApp = $uc->load('app');
		$myApp->checkColumns();
	}
	adminmsg('operate_success');	

} elseif ($action == 'add') {

	if (empty($_POST['step'])) {
		
		$app = array();
		include PrintEot('ucapp');exit;

	} else {
		
		InitGP(array('name','siteurl','secretkey','interface'));
		$siteurl = rtrim($siteurl,'/');

		$db->update("INSERT INTO pw_ucapp SET " . pwSqlSingle(array(
			'name' => $name,
			'siteurl' => $siteurl,
			'secretkey' => $secretkey,
			'interface' => $interface
		)));

		require_once(R_P . 'uc_client/class_core.php');
		$uc = new UC();
		$myApp = $uc->load('app');
		$myApp->checkColumns();

		adminmsg('operate_success');
	}
} elseif ($action == 'edit') {

	InitGP(array('id'));
	$app = $db->get_one("SELECT * FROM pw_ucapp WHERE id=" . pwEscape($id));
	empty($app) && adminmsg('undefined_action');

	if (empty($_POST['step'])) {

		include PrintEot('ucapp');exit;

	} else {

		InitGP(array('name','siteurl','secretkey','interface'));
		$siteurl = rtrim($siteurl,'/');

		$db->update("UPDATE pw_ucapp SET " . pwSqlSingle(array(
			'name' => $name,
			'siteurl' => $siteurl,
			'secretkey' => $secretkey,
			'interface' => $interface
		)) . ' WHERE id=' . pwEscape($id));

		if ($app['uc'] && $secretkey != $uc_key) {
			setConfig('uc_appid', $app['id']);
			setConfig('uc_key', $secretkey);
			updatecache_c();
		}
		$basename .= "&action=edit&id=$id";

		adminmsg('operate_success');
	}
}
?>