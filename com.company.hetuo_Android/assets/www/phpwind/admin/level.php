<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=level";

$levelImg = array();
if ($fp = opendir("$imgdir/$stylepath/level")) {
	while (($file = readdir($fp))) {
		if (!is_dir($file) && substr($file,-4) == '.gif') {
			$imgId = substr($file,0,-4);
			$levelImg[$imgId] = $file;
		}
	}
	closedir($fp);
}
ksort($levelImg);

if (empty($action)) {
	$gdb = array();

	$query = $db->query("SELECT gid,gptype,grouptitle,groupimg,grouppost FROM pw_usergroups ORDER BY grouppost,gid");
	while ($level = $db->fetch_array($query)) {
		$gdb[$level['gptype']][] = $level;
	}

	include PrintEot('level');exit;

} elseif ($_POST['action'] == "menedit") {

	InitGP(array('memtitle','mempic'),'P',0);
	InitGP(array('mempost'),'P',2);

	$gid = saveGroup('member',array('title'=>$memtitle['add'],'img'=>$mempic['add'],'num'=>$mempost['add']));
	unset($memtitle['add'],$mempic['add'],$mempost['add']);

	@asort($mempost);
	foreach ($mempost as $key => $value) {
		if (!is_numeric($value)) {
			$value = 20 * pow(2,$key);
			$mempost[$key] = $value;
		}
		$db->update("UPDATE pw_usergroups SET " . pwSqlSingle(array(
			'grouptitle'	=> $memtitle[$key],
			'groupimg'		=> $mempic[$key],
			'grouppost'		=> $mempost[$key]
		)) . " WHERE gptype='member' AND gid=".pwEscape($key));
		updatecache_g($key);/*update cache*/
	}
	updatecache_l();
	adminmsg('operate_success',"$basename#1");

} elseif ($_POST['action'] == "defedit") {

	InitGP(array('deftitle','defpic'),'P');
	foreach ($deftitle as $key => $value) {
		$db->update("UPDATE pw_usergroups SET " . pwSqlSingle(array(
			'grouptitle'	=> $value,
			'groupimg'		=> $defpic[$key]
		)) . " WHERE gptype='default' AND gid=".pwEscape($key));
	}
	updatecache_l();
	adminmsg('operate_success',"$basename#4");

} elseif ($_POST['action'] == "sysedit") {

	InitGP(array('systitle','syspic'),'P',0);

	$gid = saveGroup('system',array('title'=>$systitle['add'],'img'=>$syspic['add'],'num'=>0));
	unset($systitle['add'],$syspic['add']);

	foreach ($systitle as $key => $value) {
		$db->update("UPDATE pw_usergroups SET " . pwSqlSingle(array(
			'grouptitle'	=> $value,
			'groupimg'		=> $syspic[$key]
		)) . " WHERE gptype='system' AND gid=".pwEscape($key));
	}
	updatecache_l();
	adminmsg('operate_success',"$basename#2");

} elseif ($_POST['action'] == "vipedit") {

	InitGP(array('viptitle','vippic'),'P',0);

	$gid = saveGroup('special',array('title'=>$viptitle['add'],'img'=>$vippic['add'],'num'=>0));
	unset($viptitle['add'],$vippic['add']);

	foreach ($viptitle as $key => $value) {
		$db->update("UPDATE pw_usergroups SET ".pwSqlSingle(array(
			'grouptitle'	=> $value,
			'groupimg'		=> $vippic[$key]
		)) . " WHERE gptype='special' AND gid=".pwEscape($key));
	}
	updatecache_l();
	adminmsg('operate_success',"$basename#3");

} elseif ($action == 'addgroup') {

	InitGP(array('newtitle','newpic','gtype'),'GP',0);
	InitGP(array('newpost'),'GP',2);
	$gid = saveGroup($gtype,array('title'=>array($newtitle),'img'=>array($newpic),'num'=>array($newpost)));
	$gid && $basename = "$admin_file?adminjob=level&action=editgroup&gid=$gid";
	adminmsg('operate_success',$basename);

} elseif ($action == "delgroup") {

	PostCheck($verify);
	InitGP(array('delid'));
	if ($delid < 7) {
		adminmsg('level_del');
	}
	$db->update("DELETE FROM pw_usergroups WHERE gid=".pwEscape($delid));
	P_unlink(D_P."data/groupdb/group_$delid.php");

	updatecache_l();
	adminmsg('operate_success');

} elseif ($action == "editgroup") {

	InitGP(array('gid','step'),'GP',2);

	if (empty($step)) {

		if (file_exists(D_P."data/groupdb/group_$gid.php") && $gid != 1) {
			include_once Pcv(D_P."data/groupdb/group_$gid.php");
			$default = 0;
		} else {
			include_once(D_P."data/groupdb/group_1.php");
			$default = 1;
		}
		@extract($SYSTEM);
		@extract($_G);
		$groupselect = $credit_type = $special_type = $upload_type = '';
		$selected_g = array($gid => 'selected');
		foreach ($ltitle as $key => $value) {
			$groupselect .= "<option value=$key $selected_g[$key]>$value</option>";
		}
		$uploadtype = unserialize($uploadtype);
		foreach ($uploadtype as $key => $value) {
			$upload_type .= "<tr>
			<td><input class=\"input input_wc\" name=\"filetype[]\" value=\"$key\"></td>
			<td><input class=\"input input_wc\" name=\"maxsize[]\" value=\"$value\"></td><td><a href=\"javascript:;\" onclick=\"removecols(this);\">[删除]</a></td>
			</tr>";
		}
		require_once(R_P.'require/credit.php');

		$_G['markset'] = unserialize($_G['markset']);

		$credit_type = $credit->cType;

		$pergroup_sel = $media_sel = $allowpcid_sel = array();
		foreach (explode(',',$pergroup) as $value) {
			$pergroup_sel[$value] = 'checked';
		}
		foreach (explode(',',$allowpcid) as $value) {
			$allowpcid_sel[$value] = 'checked';
		}
		foreach (explode(',',$media) as $value) {
			$media_sel[$value] = 'checked';
		}
		foreach ($credit->cType as $key => $value) {
			$special_type .= "<option value=\"$key\"".($selltype == $key ? ' selected' : '').">$value</option>";
		}
		!$banmax && $banmax!=0 && $banmax = 1;

		/*
		* 基本权限
		*/
		ifcheck($allowread,'allowread');

		ifcheck($allowmember,'allowmember');
		ifcheck($allowprofile,'allowprofile');
		ifcheck($allowreport,'allowreport');
		ifcheck($allowmessege,'allowmessege');
		ifcheck($allowsort,'allowsort');
		ifcheck($alloworder,'alloworder');
		ifcheck($allowpost,'allowpost');
		ifcheck($allowrp,'allowrp');
		ifcheck($allownewvote,'allownewvote');
		ifcheck($allowvote,'allowvote');
		ifcheck($allowactive,'allowactive');
		ifcheck($allowgoods,'allowgoods');
		ifcheck($allowdebate,'allowdebate');
		ifcheck($allowmodelid,'allowmodelid');
		ifcheck($htmlcode,'htmlcode');
		//ifcheck($wysiwyg,'wysiwyg');
		ifcheck($allowhidden,'allowhidden');
		ifcheck($allowsell,'allowsell');
		ifcheck($allowloadrvrc,'allowloadrvrc');
		ifcheck($allowhide,'allowhide');
		ifcheck($userbinding,'userbinding');
		ifcheck($upload,'upload');
		ifcheck($allowportait,'allowportait');
		ifcheck($allowhonor,'allowhonor');
		ifcheck($allowdelatc,'allowdelatc');
		${'allowsearch_'.(int)$allowsearch}		= 'checked';
		${'allowupload_'.(int)$allowupload}		= 'checked';
		${'allowdownload_'.(int)$allowdownload}	= 'checked';

		ifcheck($msggroup,'msggroup');
		ifcheck($ifmemo,'ifmemo');
		ifcheck($previewvote,'previewvote');
		ifcheck($allowreward,'allowreward');
		ifcheck($viewvote,'viewvote');
		ifcheck($pmodifyvote,'pmodifyvote');
		ifcheck($modifyvote,'modifyvote');
		ifcheck($viewipfrom,'viewipfrom');
		ifcheck($atclog,'atclog');
		ifcheck($show,'show');
		ifcheck($atccheck,'atccheck');
		ifcheck($dig,'dig');
		ifcheck($leaveword,'leaveword');
		ifcheck($allowencode,'allowencode');


		ifcheck($diaryallow,'diaryallow');
		/*
		* 管理权限
		*/
		ifcheck($allowbuy,'allowbuy');
		ifcheck($allowadmincp,'allowadmincp');
		ifcheck($visithide,'visithide');
		ifcheck($rzforum,'rzforum');
		ifcheck($tpctype,'tpctype');
		ifcheck($tpccheck,'tpccheck');
		ifcheck($delatc,'delatc');
		ifcheck($moveatc,'moveatc');
		ifcheck($copyatc,'copyatc');
		ifcheck($digestadmin,'digestadmin');
		ifcheck($lockadmin,'lockadmin');
		ifcheck($pushadmin,'pushadmin');
		ifcheck($coloradmin,'coloradmin');
		ifcheck($downadmin,'downadmin');
		ifcheck($viewcheck,'viewcheck');
		ifcheck($viewclose,'viewclose');
		ifcheck($attachper,'attachper');
		ifcheck($delattach,'delattach');
		ifcheck($shield,'shield');
		ifcheck($unite,'unite');
		ifcheck($remind,'remind');
		ifcheck($pingcp,'pingcp');
		ifcheck($inspect,'inspect');

		ifcheck($superright,'superright');
		ifcheck($viewip,'viewip');
		${'topped_'.(int)$topped}		= 'checked';
		${'markable_'.(int)$markable}	= 'checked';
		${'banuser_'.(int)$banuser}		= 'checked';
		ifcheck($bantype,'bantype');
		ifcheck($posthide,'posthide');
		ifcheck($sellhide,'sellhide');
		ifcheck($encodehide,'encodehide');
		ifcheck($anonyhide,'anonyhide');
		ifcheck($postpers,'postpers');
		ifcheck($anonymous,'anonymous');
		ifcheck($replylock,'replylock');
		ifcheck($modother,'modother');
		ifcheck($deltpcs,'deltpcs');
		ifcheck($allowtime,'allowtime');
		ifcheck($areapush,'areapush');		
		ifcheck($overprint,'overprint');
		ifcheck($replaytopped,'replaytopped');
		ifcheck($replayorder,'replayorder');
		
		$schtime != 'all' && !is_numeric($schtime) && $schtime = 7776000;
		${'schtime_'.$schtime} = 'selected';
		$sellinfo && $sellinfo = str_replace('<br />',"\n",$sellinfo);

		require GetLang('right');
		$lang['right']['system'] = array_merge($lang['right']['system'],$lang['right']['systemforum']);
		unset($lang['right']['systemforum']);

		include PrintEot('level');exit;

	} elseif ($_POST['step'] == 2) {

		InitGP(array('othergroup','group','othergid','filetype','grouptitle'),'P');
		InitGP(array('maxsize'),'P',2);

		if (file_exists(D_P."data/groupdb/group_$gid.php")) {
			include_once Pcv(D_P."data/groupdb/group_$gid.php");
			$_M = array_merge($_G, $SYSTEM);
		} else {
			$_M = array();
		}
		!isset($group['maxmsg'])		&& $group['maxmsg']		= '10';
		!isset($group['allownum'])	    && $group['allownum']	= '5';
		!isset($group['edittime'])		&& $group['edittime']	= '0';
		!isset($group['postpertime'])	&& $group['postpertime']= '0';
		!isset($group['searchtime'])	&& $group['searchtime']	= '0';
		!isset($group['signnum'])		&& $group['signnum']	= '0';

		foreach ($group['markset'] as $key => $value) {
			if ($value['markctype']) {
				if ($value['marklimit'][0] > $value['marklimit'][1] || max(abs($value['marklimit'][0]),$value['marklimit'][1]) > $value['maxcredit']) adminmsg('level_credit_error');
			}
		}

		$db->update("UPDATE pw_usergroups SET grouptitle=".pwEscape($grouptitle)."WHERE gid=".pwEscape($gid));
		updatecache_l();

		$gptype = $db->get_value("SELECT gptype FROM pw_usergroups WHERE gid=" . pwEscape($gid));

		$group['markset'] = addslashes(serialize($group['markset']));
		$uploadtype = array();
		foreach ($filetype as $key => $value) {
			if ($value) {
				$uploadtype[$value] = $maxsize[$key];
			}
		}
		$group['uploadtype'] = $uploadtype ? serialize($uploadtype) : '';
		$group['pergroup']	 = implode(',',$group['pergroup']);
		$group['media']		 = implode(',',$group['media']);
		$group['allowpcid'] = implode(',',$group['allowpcid']);
		$group['schtime']	!= 'all' && !is_numeric($group['schtime']) && $group['schtime'] = 7776000;

		require GetLang('right');
		$basicdb = array_merge($lang['right']['basic'],$lang['right']['read'],$lang['right']['att']);
		$vipdb	 = $lang['right']['special'];
		$sysdb	 = $lang['right']['system'];
		$sysfdb	 = $lang['right']['systemforum'];
		unset($lang['right']);

		isset($group['sellinfo']) && $group['sellinfo'] = str_replace("\n", '<br />', $group['sellinfo']);

		$pwSQL = array();
		foreach ($group as $key => $value) {
			if (isset($basicdb[$key])) {
				$keytype = 'basic';
			} elseif (isset($sysfdb[$key]) && in_array($gptype,array('system','special'))) {
				$keytype = 'systemforum';
			} elseif (isset($sysdb[$key]) && in_array($gptype,array('system','special'))) {
				$keytype = 'system';
			} elseif (isset($vipdb[$key]) && $gptype == 'special') {
				$keytype = 'special';
			} elseif($key == 'pushtime') {
				//帖子提前时间
				$keytype = 'systemforum';
			} else {
				continue;
			}
			if ($value <> $_M[$key]) {
				$pwSQL[] = array(0, 0, $gid, $key, $keytype, $value);
			}
		}

		$upgid = array();
		if ($pwSQL) {
			$upgid[] = $gid;
		}
		if ($othergroup) {
			if ($othergid = array_diff($othergid,array($gid))) {
				$query = $db->query("SELECT gid,gptype FROM pw_usergroups WHERE gid IN(" . pwImplode($othergid). ')');
				while ($rt = $db->fetch_array($query)) {
					if ($rt['gid'] <> $gid) {
						$ifup = 0;
						foreach ($othergroup as $key => $value) {
							if (isset($basicdb[$value])) {
								$keytype = 'basic';
							} elseif (isset($sysfdb[$value]) && in_array($rt['gptype'],array('system','special'))) {
								$keytype = 'systemforum';
							} elseif (isset($sysdb[$value]) && in_array($rt['gptype'],array('system','special'))) {
								$keytype = 'system';
							} elseif (isset($vipdb[$value]) && $rt['gptype'] == 'special') {
								$keytype = 'special';
							} else {
								continue;
							}
							$pwSQL[] = array(0, 0, $rt['gid'], $value, $keytype, $group[$value]);
							$ifup = 1;
						}
						if ($ifup) {
							$upgid[] = $rt['gid'];
						}
					}
				}
			}
		}
		if ($pwSQL) {
			$db->update("REPLACE INTO pw_permission (uid,fid,gid,rkey,type,rvalue) VALUES " . pwSqlMulti($pwSQL));
			updatecache_g($upgid);
			updatecache_gr();
		}
		$basename = "$admin_file?adminjob=level&action=editgroup&gid=$gid";
		adminmsg('operate_success');

	} elseif ($step == 3) {

		$db->update("UPDATE pw_usergroups SET ifdefault='1' WHERE gid=".pwEscape($gid));
		P_unlink(D_P."data/groupdb/group_$gid.php");
		adminmsg('operate_success');

	}
} elseif ($action == 'setright') {

	InitGP(array('rkey','gid'));
	$gid = intval($gid);
	require GetLang('right');

	$keytype = $setdb = '';
	foreach ($lang['right'] as $key => $value) {
		if (isset($value[$rkey])) {
			$keytype = in_array($key,array('read','att')) ? 'basic' : $key;
			$setdb = $value[$rkey];
			break;
		}
	}
	empty($keytype) && adminmsg('undefined_action');

	if (empty($_POST['step'])) {

		$rdb = $gdb = array();

		$query = $db->query("SELECT gid,rvalue FROM pw_permission WHERE uid='0' AND fid='0' AND rkey=" . pwEscape($rkey));
		while ($rt = $db->fetch_array($query)) {
			$rdb[$rt['gid']] = $rt['rvalue'];
		}
		$query = $db->query("SELECT gid,gptype,grouptitle FROM pw_usergroups ORDER BY gptype,grouppost,gid");
		while ($rt = $db->fetch_array($query)) {
			if ($gid == $rt['gid']) $gp_gptype = $rt['gptype'];
			if (in_array($rt['gptype'],array('default','member')) && $keytype <> 'basic' || $keytype == 'special' && $rt['gptype'] <> 'special') {
				continue;
			}
			$html = str_replace("group[$rkey]","group[{$rt[gid]}]",$setdb['html']);
			switch ($rkey) {
				case 'allowsearch':
				case 'markable':
				case 'allowupload':
				case 'allowdownload':
				case 'topped':
				case 'banuser':
					${$rkey.'_0'} = ${$rkey.'_1'} = ${$rkey.'_2'} = ${$rkey.'_3'} = '';
					${$rkey.'_'.(int)$rdb[$rt['gid']]} = 'checked';break;
				case 'searchtime':
				case 'signnum':
				case 'imgwidth':
				case 'imgheight':
				case 'fontsize':
				case 'maxmsg':
				case 'maxsendmsg':
				case 'maxfavor':
				case 'maxgraft':
				case 'pwdlimitime':
				case 'maxcstyles':
				case 'postlimit':
				case 'postpertime':
				case 'edittime':
				case 'allownum':
				case 'banmax':
				case 'sellprice':
				case 'rmbprice':
				case 'selllimit':
				case 'sellinfo':
				case 'pushtime':
					$$rkey = $rdb[$rt['gid']];break;
				case 'schtime':
					$schtime_all = $schtime_86400 = $schtime_172800 = $schtime_604800 = $schtime_2592000 = $schtime_5184000 = $schtime_7776000 = $schtime_15552000 = $schtime_31536000 = '';
					${$rkey.'_'.$rdb[$rt['gid']]} = 'selected';break;
				case 'pergroup':
				case 'allowpcid':
				case 'media':
					${$rkey.'_sel'} = array();
					foreach (explode(',',$rdb[$rt['gid']]) as $value) {
						${$rkey.'_sel'}[$value] = 'checked';
					}
					break;
				case 'markset':
					isset($credit) || require_once(R_P.'require/credit.php');
					$credit_type = '';

					$credit_type = $credit->cType;
					$marksetdb[$rt['gid']] = unserialize($rdb[$rt['gid']]);

					break;
				case 'uploadtype':
					$html = str_replace(array('filetype[]', 'maxsize[]', 'mode', 'ft'), array("group[{$rt[gid]}][]", "maxsize_{$rt[gid]}[]", "mode_{$rt[gid]}", "ft_{$rt[gid]}"), $html);
					$upload_type = '';
					$uploadtype = unserialize($rdb[$rt['gid']]);
					foreach ($uploadtype as $key => $value) {
						$upload_type .= "<tr class=\"tr3\">
						<td><input class=\"input\" size=\"10\" name=\"group[{$rt[gid]}][]\" value=\"$key\"></td>
						<td><input class=\"input\" size=\"10\" name=\"maxsize_{$rt[gid]}[]\" value=\"$value\"> <a style=\"cursor:pointer;color:#FA891B\" onclick=\"removecols(this);\">[DELETE]</a></td>
						</tr>";
					}
					break;
				case 'selltype':
					isset($credit) || require_once(R_P.'require/credit.php');
					$special_type = '';
					foreach ($credit->cType as $key => $value) {
						$special_type .= "<option value=\"$key\"".($rdb[$rt['gid']] == $key ? ' selected' : '').">$value</option>";
					}
					break;
				default :
					ifcheck($rdb[$rt['gid']],$rkey);
			}
			if ($rkey != 'markset') {
				eval("\$html = \"".addcslashes($html,'"')."\";");
			} else {
				$html = $credit_type;
			}
			$gdb[$rt['gptype']][$rt['gid']] = array($rt['grouptitle'],$html);
		}
		include PrintEot('level');exit;
	} else {

		InitGP(array('group'));
		$rdb = $gdb = $pwSQL = $upgid = array();
		$query = $db->query("SELECT gid,rvalue FROM pw_permission WHERE uid='0' AND fid='0' AND rkey=" . pwEscape($rkey));
		while ($rt = $db->fetch_array($query)) {
			if ($rkey == 'markset') {
				$rdb[$rt['gid']]['markset'] = $rt['rvalue'];
			} else {
				$rdb[$rt['gid']]['markset'] = $rt['rvalue'];
			}
		}

		if ($keytype == 'special') {
			$sql = "gptype='special'";
		} elseif ($keytype <> 'basic') {
			$sql = "gptype>2";
		} else {
			$sql = '';
		}
		$query = $db->query("SELECT gid,gptype FROM pw_usergroups ".($sql ? "WHERE $sql" : ''));
		while ($rt = $db->fetch_array($query)) {
			$gdb[] = $rt['gid'];
		}


		foreach ($gdb as $key => $gid) {
			$value = $group[$gid];
			switch ($rkey) {
				case 'markset':
					foreach ($value['markset'] as $k => $val) {
						if ($val['markctype']) {

							if ($val['marklimit'][0] > $val['marklimit'][1] || max(abs($val['marklimit'][0]),$val['marklimit'][1]) > $val['maxcredit']) adminmsg('level_credit_error');
						}
					}
					$value = is_array($value['markset']) ? addslashes(serialize($value['markset'])) : '';break;

				case 'pergroup':
				case 'allowpcid':
				case 'media':
					$value = implode(',',$value);break;
				case 'schtime':
					$value != 'all' && !is_numeric($value) && $value = 7776000;
					break;
				case 'uploadtype':
					InitGP(array('maxsize_'.$gid),'',2);
					$uploadtype = array();
					foreach ($value as $k => $v) {
						if ($v) {
							$uploadtype[$v] = ${'maxsize_'.$gid}[$k];
						}
					}
					$value = $uploadtype ? serialize($uploadtype) : '';
					break;
			}
			if ($value <> $rdb[$gid]) {
				$pwSQL[] = array(0, 0, $gid, $rkey, $keytype, $value);
				$upgid[] = $gid;
			}
		}

		if ($pwSQL) {
			$db->update("REPLACE INTO pw_permission (uid,fid,gid,rkey,type,rvalue) VALUES " . pwSqlMulti($pwSQL));
			updatecache_g($upgid);
			if (in_array($rkey,array('imgwidth','imgheight','fontsize'))) {
				updatecache_gr();
			}
		}
		$basename .= "&action=$action&rkey=$rkey";
		adminmsg('operate_success');

	}
} elseif ($action == 'batch') {

	if (empty($_POST['step'])) {

		$group = array();
		$query = $db->query("SELECT gid,gptype,grouptitle FROM pw_usergroups WHERE gptype<>'default' ORDER BY gptype,gid");
		while ($rt = $db->fetch_array($query)) {
			$group[] = $rt;
		}
		include PrintEot('level');exit;

	} elseif ($_POST['step'] == 3) {

		$upload = $_FILES['upload'];

		if (is_array($upload)) {
			$upload_name = $upload['name'];
			$upload_size = $upload['size'];
			$upload = $upload['tmp_name'];
		}
		$gids = $titles = array();

		if ($upload && $upload != 'none') {
			require_once(R_P.'require/postfunc.php');
			$attach_ext = strtolower(substr(strrchr($upload_name,'.'),1));
			if (!if_uploaded_file($upload)) {
				adminmsg('upload_error');
			} elseif ($attach_ext != 'txt') {
				adminmsg('upload_type_error');
			}
			$source = D_P."data/tmp/group.txt";
			if (postupload($upload,$source)) {
				include_once(D_P."data/bbscache/wordsfb.php");
				$content = explode("\n",readover($source));
				foreach ($content as $key => $value) {
					list($gid,$title) = explode("=>",$value);
					if (is_numeric($gid)) {
						$gids[] = $gid;
						$titles[$gid] = Char_cv(trim($title));
					}
				}
			} else {
				adminmsg('upload_error');
			}
			P_unlink($source);
		} else {
			adminmsg('upload_error');
		}
		if ($gids) {
			$gids = pwImplode($gids);
			$query = $db->query("SELECT gid,grouptitle FROM pw_usergroups WHERE gid IN($gids)");
			while (@extract($db->fetch_array($query))) {
				if ($grouptitle != $titles[$gid]) {
					$db->update("UPDATE pw_usergroups SET grouptitle=".pwEscape($titles[$gid])." WHERE gid=".pwEscape($gid));
				}
			}
			updatecache_l();
		}
		adminmsg('operate_success');

	} else {

		InitGP(array('selid'),'P');
		$filename = 'group_'.get_date($timestamp,'Ymd');
		if (!$selid = checkselid($selid)) {
			adminmsg('operate_error');
		}
		$writeinfo = '';
		$query = $db->query("SELECT gid,grouptitle FROM pw_usergroups WHERE gid IN($selid)");
		while ($rt = $db->fetch_array($query)) {
			$writeinfo .= $rt['gid']."=>".$rt['grouptitle']."\r\n";
		}
		ob_end_clean();
		header('Last-Modified: '.gmdate('D, d M Y H:i:s',$timestamp+86400).' GMT');
		header('Cache-control: no-cache');
		header('Content-Encoding: none');
		header('Content-Disposition: attachment; filename='.$filename.".txt");
		header('Content-type: txt');
		header("Content-Transfer-Encoding: binary");
		header('Content-Length: '.strlen($writeinfo));
		echo $writeinfo;exit;
	}
} elseif ($action == 'help') {
	include PrintEot('level');exit;
}

function saveGroup($gtype,$info) {
	if (in_array($gtype,array('member','system','special'))) {
		$pwSQL = array();
		foreach ($info['title'] as $key=>$value) {
			if (empty($value)) {
				continue;
			}
			$info['img'][$key] || $info['img'][$key] = 6;
			if ($gtype == 'member') {
				$pwSQL[] = array($gtype,$value,$info['img'][$key],$info['num'][$key]);
			} else {
				$pwSQL[] = array($gtype,$value,$info['img'][$key],0);
			}
		}
		if ($pwSQL) {
			
			$GLOBALS['db']->update('INSERT INTO pw_usergroups (gptype,grouptitle,groupimg,grouppost) VALUES' . pwSqlMulti($pwSQL));
			$gid = $GLOBALS['db']->insert_id();
			if ($gtype != 'member') {
				for ($i = 0;$i<count($pwSQL);$i++) {
					$gids[] = $gid+$i;
				}
				updatecache_g($gids);
			}
			updatecache_l();
			return $gid;
		}
	}
	return false;
}
?>