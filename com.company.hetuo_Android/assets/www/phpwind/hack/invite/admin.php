<?php
!function_exists('adminmsg') && exit('Forbidden');
@include_once(D_P.'data/bbscache/inv_config.php');

if (!$action) {

	ifcheck($inv_open,'open');
	ifcheck($inv_onlinesell,'onlinesell');
	$usergroup	= "";
	$num		= 0;
	foreach ($ltitle as $key => $value) {
		if ($key != 1 && $key != 2) {
			$checked = '';
			if (strpos($inv_groups,','.$key.',') !== false) {
				$checked = 'checked';
			}
			$num++;
			$htm_tr = $num%4 == 0 ?  '' : '';
			$usergroup .=" <li><input type='checkbox' name='groups[]' value='$key' $checked>$value</li>$htm_tr";
		}
	}
	$rvrc_st = $money_st = $credit_st = $currency_st = '';
	${$inv_credit.'_st'} = 'selected';
	include PrintHack('admin');exit;

} elseif ($_POST['action'] == 'unsubmit') {

	InitGP(array('config','groups'),'P');
	if(!is_numeric($config['open'])) $config['open']=1;
	if(!is_numeric($config['days'])) $config['days']=30;
	if(!is_numeric($config['limitdays'])) $config['limitdays']=0;
	if(!is_numeric($config['costs'])) $config['costs']=100;
	if(is_array($groups)){
		$config['groups'] = ','.implode(',',$groups).',';
	} else {
		$config['groups'] = '';
	}
	foreach($config as $key=>$value){
		$db->pw_update(
			"SELECT hk_name FROM pw_hack WHERE hk_name=".pwEscape("inv_$key"),
			"UPDATE pw_hack SET hk_value=".pwEscape($value)."WHERE hk_name=".pwEscape("inv_$key"),
			"INSERT INTO pw_hack SET hk_name=".pwEscape("inv_$key").",hk_value=".pwEscape($value)
		);
	}
	updatecache_inv();
	adminmsg('operate_success');

} elseif ($action == 'manager') {

	InitGP(array('page'));
	!$type && $type = '1';
	$sql = $sel_1 = $sel_2 = $sel_3 = $sel_4 = '';
	$inv_days *= 86400;
	if ($type == '1') {
		$sql	= "WHERE i.ifused='0' AND i.createtime>".pwEscape($timestamp-$inv_days);
		$sel_1	= 'selected';
	} elseif ($type == '2') {
		$sql	= "WHERE i.ifused='1'";
		$sel_2	= 'selected';
	} elseif ($type == '3') {
		$sql	= "WHERE i.ifused<'2' AND i.createtime<".pwEscape($timestamp-$inv_days);
		$sel_3	= 'selected';
	} else {
		$sql	= "WHERE i.ifused='2'";
		$sel_4	= 'selected';
	}
	$db_showperpage = 20;
	(!is_numeric($page) || $page<1) && $page = 1;
	$limit = pwLimit(($page-1)*$db_showperpage,$db_showperpage);
	$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_invitecode i $sql");
	$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_showperpage),"$basename&action=manager&type=$type&");

	$query = $db->query("SELECT i.*,m.username FROM pw_invitecode i LEFT JOIN pw_members m USING(uid) $sql $limit");
	$invdb = array();
	$i = 1;
	while ($rt = $db->fetch_array($query)) {
		$rt['num']=($page-1)*$db_showperpage+$i++;
		$rt['createtime']=get_date($rt['createtime'],'Y-m-d H:i:s');
		$invdb[]=$rt;
	}
	include PrintHack('admin');exit;

} elseif ($_POST['action'] == 'delete') {

	InitGP(array('selid'),'P');
	if (!$selid = checkselid($selid)) {
		adminmsg('operate_error');
	}
	$selid && $db->update("DELETE FROM pw_invitecode WHERE id IN ($selid)");
	adminmsg('operate_success');
}
?>