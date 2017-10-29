<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=setuser";

if (empty($action)) {

	$groupselect = "<option value='-1'>".getLangInfo('all','reg_member')."</option>";
	$g_sel = '';
	$query = $db->query("SELECT gid,gptype,grouptitle FROM pw_usergroups WHERE gid>2 AND gptype<>'member' ORDER BY gid");
	while ($group = $db->fetch_array($query)) {
		$groupselect .= "<option value=\"$group[gid]\">$group[grouptitle]</option>";
		if ($group['gptype'] != 'default') {
			$g_sel .= "<option value=\"$group[gid]\">$group[grouptitle]</option>";
		}
	}
	include PrintEot('setuser');exit;

} elseif ($_POST['action'] == 'addnew') {

	InitGP(array('username','password','email','groupid'),'P');
	if (!$username || !$password || !$email) {
		adminmsg('setuser_empty');
	}
	!$groupid && $groupid = '-1';

	$username = trim($username);
	$S_key = array("\\",'&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#');
	foreach ($S_key as $value) {
		if (strpos($username,$value)!==false) {
			adminmsg('illegal_username');
		}
		if (strpos($password,$value)!==false) {
			adminmsg('illegal_password');
		}
	}
	if (strlen($username)>14 || strrpos($username,"|")!==false || strrpos($username,'.')!==false || strrpos($username,' ')!==false || strrpos($username,"'")!==false || strrpos($username,'/')!==false || strrpos($username,'*')!==false || strrpos($username,";")!==false || strrpos($username,",")!==false || strrpos($username,"<")!==false || strrpos($username,">")!==false) {
		adminmsg('illegal_username');
	}
	if (strrpos($password,"\r")!==false || strrpos($password,"\t")!==false || strrpos($password,"|")!==false || strrpos($password,"<")!==false || strrpos($password,">")!==false) {
		adminmsg('illegal_password');
	} else {
		$password = md5($password);
	}
	if ($email&&!ereg("^[-a-zA-Z0-9_\.]+\@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,3}$",$email)) {
		adminmsg('illegal_email');
	}
	if ($groupid == '3' && !If_manager) {
		adminmsg('manager_right');
	}
	$register = L::loadClass('Register');
	$register->setField('username', $username);
	$register->setField('password', $password);
	$register->setField('email', $email);
	$register->setField('groupid', $groupid);
	$register->setField('yz', 1);
	$register->execute();
	
	if ($groupid <> '-1') {
		admincheck($register->uid,$username,$groupid,'','update');
	}
	adminmsg('operate_success');

} elseif ($action == 'search') {

	InitGP(array('groupid','schname','schname_s','schemail','userip','regdate','schlastvisit','orderway','asc','lines','page'));
	InitGP(array('vaguename','vagueemail'));
	if (!$groups && !$schname && !$schemail && !$groupid && !$userip && $regdate=='all' && $schlastvisit=='all') {
		adminmsg('noenough_condition');
	}
	$sql     = is_numeric($groupid) ? "m.groupid=".pwEscape($groupid) : 1;
	$order   = '';
	$schname = trim($schname);
	if ($schname!='') {
		if (strpos($schname,'*') !== false) {
			$t_schname = addslashes(str_replace('*','%',$schname));
			$sql .= " AND (m.username LIKE ".pwEscape($t_schname).')';
		} else {
			$schname = addslashes($schname);
			$sql .= " AND m.username = ".pwEscape($schname);
		}
	}
	if ($schemail!='') {
		if (strpos($schemail,'*') !== false) {
			$t_schemail = str_replace('*','%',$schemail);
			$sql .= " AND (m.email LIKE ".pwEscape($t_schemail).")";
		}else{
			$schemail = addslashes($schemail);
			$sql .= " AND m.email = ".pwEscape($schemail);
		}
	}
	if ($userip!='') {
		$userip = str_replace('*','%',$userip);
		$sql .= " AND (md.onlineip LIKE ".pwEscape("$userip%").")";
	}
	if ($regdate!='all' && is_numeric($regdate)) {
		$schtime = $timestamp-$regdate;
		$sql .= " AND m.regdate<".pwEscape($schtime);
	}
	if ($schlastvisit!='all' && is_numeric($schlastvisit)) {
		$schtime = $timestamp-$schlastvisit;
		$sql .= " AND md.thisvisit<".pwEscape($schtime);
	}
	if ($orderway) {
		$order = "ORDER BY ".pwEscape($orderway);
		$asc=='DESC' && $order .= " $asc";
	}
	$rs = $db->get_one("SELECT COUNT(*) AS count FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE $sql");
	$count = $rs['count'];

	!is_numeric($lines) && $lines=100;
	(!is_numeric($page) || $page < 1) && $page=1;
	$numofpage = ceil($count/$lines);
	if ($numofpage && $page>$numofpage) {
		$page = $numofpage;
	}
	$pages = numofpage($count,$page,$numofpage, "$admin_file?adminjob=setuser&action=$action&schname=".rawurlencode($schname)."&groupid=$groupid&schemail=$schemail&regdate=$regdate&schlastvisit=$schlastvisit&orderway=$orderway&lines=$lines&asc=$asc&");
	$start = ($page-1)*$lines;
	$limit = pwLimit($start,$lines);
	$groupselect = "<option value='-1'>".getLangInfo('all','reg_member')."</option>";
	$query = $db->query("SELECT gid,gptype,grouptitle FROM pw_usergroups WHERE gid>2 AND gptype<>'member' ORDER BY gid");
	while ($group = $db->fetch_array($query)) {
		$gid = $group['gid'];
		$groupselect .= "<option value='$gid'>$group[grouptitle]</option>";
	}
	$schdb = array();
	$query = $db->query("SELECT m.uid,m.username,m.email,m.groupid,m.memberid,m.regdate,md.postnum,md.onlineip FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE $sql $order $limit");
	while ($sch = $db->fetch_array($query)) {
		$sch['regdate']= get_date($sch['regdate']);
		strpos($sch['onlineip'],'|') && $sch['onlineip']=substr($sch['onlineip'],0,strpos($sch['onlineip'],'|'));
		if ($sch['groupid']=='-1') {
			$sch['groupselect'] = str_replace("<option value='-1'>".getLangInfo('all','reg_member')."</option>","<option value='-1' selected>".getLangInfo('all','reg_member')."</option>",$groupselect);
		} else {
			$sch['groupselect'] = str_replace("<option value='$sch[groupid]'>".$ltitle[$sch['groupid']]."</option>","<option value='$sch[groupid]' selected>".$ltitle[$sch['groupid']]."</option>",$groupselect);
		}
		$schdb[] = $sch;
	}
	if (empty($schdb) && $schname){
		$errorname = $schname;
		Showmsg('user_not_exists');
	}
	include PrintEot('setuser');exit;

} elseif ($action == 'groups') {

	InitGP(array('groupid','schname'),'P');
	$sql = is_numeric($groupid) ? "a.groups LIKE '%,$groupid,%'" : "a.groups!=''";
	$schname = trim($schname);
	if ($schname!='') {
		if (strpos($schname,'*') !== false) {
			$schname = addslashes(str_replace('*','%',$schname));
			$sql .= " AND (a.username LIKE ".pwEscape($schname).")";

		} else {
			$schname = addslashes($schname);
			$sql .= " AND a.username=".pwEscape($schname);
		}
	}
	$query = $db->query("SELECT a.uid,a.username,a.groupid,a.groups,m.memberid FROM pw_administrators a LEFT JOIN pw_members m USING(uid) WHERE $sql LIMIT 200");
	while ($rt = $db->fetch_array($query)) {
		$rt['system'] = $rt['groupid']=='-1' ? $ltitle[$rt['memberid']] : $ltitle[$rt['groupid']];
		$groupds = explode(',',$rt['groups']);
		foreach ($groupds as $key => $value) {
			if ($value) {
				$rt['gtitle'] .= $ltitle[$value].' ';
			}
		}
		$schdb[] = $rt;
	}
	include PrintEot('setuser');exit;

} elseif ($action == 'editgroup') {

	InitGP(array('gid'),'P');
	if (!$gid) adminmsg('operate_error');
	$_cache = getDatastore();
	foreach ($gid as $uid => $groupid) {
		if ($uid) {
			$rt = $db->get_one("SELECT username,groupid,groups FROM pw_members WHERE uid=".pwEscape($uid));
			if ($rt['groupid']==3 && $groupid!=3 && !If_manager) {
				adminmsg('manager_right');
			} elseif ($rt['groupid']!=3 && $groupid==3 && !If_manager) {
				adminmsg('manager_right');
			} elseif ($rt['groupid']==5 && $groupid==-1 || $rt['groupid']!=5 && $groupid==5) {
				adminmsg('setuser_forumadmin');
			}
			$groups = $rt['groups'];
			if ($groups && strpos($groups,','.$groupid.',')!==false) {
				$groups = str_replace(','.$groupid.',',',',$groups);
				$groups == ',' && $groups = '';
			}
			$db->update('UPDATE pw_members SET groupid='.pwEscape($groupid).',groups='.pwEscape($groups).' WHERE uid='.pwEscape($uid));
			if ($groupid == 6) {
				$db->update("REPLACE INTO pw_banuser"
					. " SET " .pwSqlSingle(array(
						'uid'		=> $uid,
						'fid'		=> 0,
						'type'		=> 2,
						'startdate'	=> $timestamp,
						'days'		=> 0,
						'admin'		=> $admin_name,
						'reason'	=> ''
				),false));
			}
			$_cache->delete('UID_'.$uid);

			if ($groupid <> '-1' || $groups) {
				admincheck($uid,$rt['username'],$groupid,$groups,'update');
			} elseif ($rt['groupid'] <> '-1' || $rt['groups']) {
				admincheck($uid,$rt['username'],$groupid,$groups,'delete');
			}
		}
	}
	adminmsg('operate_success');

} elseif ($action == 'edit') {

	InitGP(array('uid'),'GP',2);
	include_once(D_P.'data/bbscache/customfield.php');
	require_once(R_P.'require/showimg.php');
	$fieldadd = '';
	foreach ($customfield as $key => $val) {
		$val['id'] = (int)$val['id'];
		$fieldadd .= ",mb.field_$val[id]";
	}
	if (empty($_POST['step'])) {

		@extract($db->get_one("SELECT m.*,md.onlinetime,md.postnum,md.rvrc,md.money,md.credit,md.currency,md.lastvisit,md.thisvisit,md.lastpost,md.todaypost,md.onlineip,md.uploadtime,md.uploadnum,mb.deposit,mb.ddeposit $fieldadd FROM pw_members m LEFT JOIN pw_memberinfo mb ON m.uid=mb.uid LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE m.uid=".pwEscape($uid)));
		$rvrc = floor($rvrc/10);
		if (strpos($onlineip,'|')) {
			$onlineip = substr($onlineip,0,strpos($onlineip,'|'));
		}
		$regdate = get_date($regdate);
		$ifpublicmail = getstatus($userstatus,7) ? 'checked' : '';
		getstatus($userstatus,8) ? $email_open = 'checked' : $email_close = 'checked';
		$sexselect[$gender] = 'selected';
		$selected[$groupid] = 'selected';
		$getbirthday = explode("-",$bday);
		$yearslect[(int)$getbirthday[0]]="selected";
        $monthslect[(int)$getbirthday[1]]="selected";
		$dayslect[(int)$getbirthday[2]]="selected";

		$groups = explode(',',$groups);
		foreach ($groups as $key => $value) {
			${'check_'.$value} = 'checked';
		}
		$usergroup = "<ul class=\"list_A list_160\">";
		$groupselect = "<option value='-1' $selected[member]>".getLangInfo('all','reg_member')."</option>";

		$query = $db->query("SELECT gid,gptype,grouptitle FROM pw_usergroups WHERE gid>2 AND gptype<>'member' ORDER BY gid");
		while ($rt = $db->fetch_array($query)) {
			$gid = $rt['gid'];
			$groupselect.="<option value='$gid' $selected[$gid]>$rt[grouptitle]</option>";

			if ($rt['gid'] != $groupid) {
				$num++;
				$htm_tr=$num%3==0 ? '' : '';
				$ifchecked=${'check_'.$rt['gid']};
				$usergroup.="<li><input type='checkbox' name='groups[]' value='$rt[gid]' $ifchecked>$rt[grouptitle]</li>$htm_tr";
			}
		}
		$usergroup.="</ul>";

		list($iconurl,$icontype,$iconwidth,$iconheight,$iconfile,$iconpig) = showfacedesign($icon,true,'m');
		$iconsize = $httpurl = '';
		$disabled = 'disabled';
		$width2 = $width3 = $iconwidth;
		$height2 = $height3 = $iconheight;
		$iconwidth && $iconsize = " width=\"$iconwidth\"";
		$iconheight && $iconsize .= " height=\"$iconheight\"";
		if ($icontype == 2) {
			$httpurl = $iconurl;
			$width3 = $height3 = '';
		} elseif ($icontype == 3) {
			$width2 = $height2 = $disabled = '';
		}
		$ifselected = false;
		$fp = opendir($imgdir.'/face/');
		while ($facefile = readdir($fp)) {
			if (preg_match('/\.(gif|png|jpg|jepg)$/i',$facefile)) {
				if ($facefile==$iconfile) {
					$ifselected = true;
					$faces .= "<option value=\"$facefile\" selected>$facefile</option>";
				} else {
					$fselected = (!$ifselected && $facefile=='none.gif') ? 'selected' : '';
					$faces .= "<option value=\"$facefile\" $fselected>$facefile</option>";
				}
			}
		}
		closedir($fp);
		$mdcredit = $credit;
		//custom credits
		if ($_CREDITDB) {
			require_once(R_P.'require/credit.php');
			$custom_credits = $credit->get($uid,'CUSTOM');
		}
		include PrintEot('setuser');exit;

	} elseif ($_POST['step'] == 2) {

		InitGP(array('groupid','groups','username','password','check_pwd','email','publicmail','receivemail','regdate','yz','userip','facetype','proicon','delupload','postnum','rvrc','money','deposit','ddeposit','credit','currency','onlinetime','site','location','oicq','icq','msn','yahoo','honor','gender','year','month','day','signature','introduce','banpm','question','customquest','answer','creditdb'),'P');
		$basename .= "&action=edit&uid=$uid";
		$upmembers = $uc_edit = array();

		$oldinfo = $db->get_one('SELECT username,groupid,groups,icon,email FROM pw_members WHERE uid=' . pwEscape($uid));
		if ($password != '') {
			$password != $check_pwd && adminmsg('password_confirm');
			$upmembers['password'] = md5($password);
			$uc_edit['password'] = md5($password);
		}
		if ($email && $email != $oldinfo['email']) {
			$uc_edit['email'] = $email;
		}
		if ($question != '-2') {
			$upmembers['safecv'] = questcode($question,$customquest,$answer);
		}
		$newgroups = $groups ? ','.implode(',',$groups).',' : '';
		$newgroups = str_replace(','.$groupid.',',',',$newgroups);
		if (($oldinfo['groupid'] == '3' || strpos($oldinfo['groups'],',3,') !== false) && !If_manager) {
			adminmsg('manager_right');
		} elseif ($oldinfo['groupid'] != '3' && ($groupid == '3' || strpos($newgroups,',3,') !== false) && !If_manager) {
			adminmsg('manager_right');
		}
		if (ifadmin($oldinfo['username']) && $groupid != '5' && strpos($newgroups,',5,') === false) {
			if (strpos($oldinfo['groups'],',5,') !== false) {
				adminmsg('setuser_forumadmin');
			} else {
				$newgroups .= $newgroups ? '5,' : ',5,';
			}
		} elseif (!ifadmin($oldinfo['username']) && ($groupid == '5' || strpos($newgroups,',5,') !== false)) {
			adminmsg('setuser_forumadmin');
		}
		$newgroups == ',' && $newgroups = '';
		if ($groupid <> '-1' || $newgroups) {
			admincheck($uid,$username,$groupid,$newgroups,'update');
		} elseif ($oldinfo['groupid'] <> '-1' || $oldinfo['groups']) {
			admincheck($uid,$username,$groupid,$newgroups,'delete');
		}
		$newgroups != $oldinfo['groups'] && $upmembers['groups'] = $newgroups;

		/*
		list($iconurl,$icontype,$iconwidth,$iconheight,$iconfile,$iconpig,$ifhavasmallicon) = showfacedesign(addslashes($oldinfo['icon']),true);
		if ($facetype == 2) {
			if (substr($_POST['i_http'],0,4) != 'http' || strrpos($_POST['i_http'],'|') !== false) {
				adminmsg('illegal_customimg');
			}
			$icontype == 3 && DelIcon($iconfile);
			$i_w = (int)$_POST['i_w'];
			$i_h = (int)$_POST['i_h'];
			$iconfile = $_POST['i_http'];
			list($iconwidth,$iconheight) = getfacelen($i_w,$i_h);
		} elseif ($facetype == 3 && $delupload) {
			DelIcon($delupload);
			$facetype = $icontype = 1;
			$proicon = 'none.gif';
		}
		$facetype != 1 && $facetype != 2 && $facetype != 3 && $facetype = $icontype;
		if ($facetype == 1) {
			if ($icontype != 1) {
				$icontype == 3 && DelIcon($iconfile);
				if (!file_exists("$imgdir/face/$proicon")) {
					$proicon = 'none.gif';
				}
			}
			if (!empty($proicon)) {
				if (strlen($proicon)>20 || !preg_match('/^[0-9A-Za-z]+\.[A-Za-z]{2,5}$/',$proicon)) {
					adminmsg('undefined_action');
				}
				$iconfile = $proicon;
			}
			$iconwidth = $iconheight = 0;
		}
		$iconwidth < 1 && $iconwidth = '';
		$iconheight < 1 && $iconheight = '';
		$icon = "$iconfile|$facetype|$iconwidth|$iconheight";
		if ($iconpig) {
			$icon .= "|$iconpig";
		} else {
			$icon .= "|";
		}
		if ($facetype == 3 && $ifhavasmallicon == 1) {
			$icon .= "|1";
		}
		strlen($icon)>100 && adminmsg('illegal_customimg');
		pwFtpClose($ftp);
		*/
		$user_a = explode('|',$oldinfo['icon']);
		$usericon = '';
		if ($facetype == 3 && $delupload) {
			$facetype = 1;
			$proicon = 'none.gif';
		}
		if ($facetype == 1) {
			$usericon = setIcon($proicon, $facetype, $user_a);
		} elseif ($facetype == 2) {
			$httpurl = $_POST['httpurl'];
			if (strncmp($httpurl[0],'http',4) != 0 || strrpos($httpurl[0],'|') !== false) {
				Showmsg('illegal_customimg');
			}
			$proicon = $httpurl[0];
			$httpurl[1] = (int)$httpurl[1];
			$httpurl[2] = (int)$httpurl[2];
			$httpurl[3] = (int)$httpurl[3];
			$httpurl[4] = (int)$httpurl[4];
			list($user_a[2], $user_a[3]) = flexlen($httpurl[1], $httpurl[2], $httpurl[3], $httpurl[4]);
			$usericon = setIcon($proicon, $facetype, $user_a);
			unset($httpurl);
		}
		pwFtpClose($ftp);
		$usericon && $upmembers['icon'] = $usericon;

		$bday = $year."-".$month."-".$day;
		//$rvrc*=10;
		$regdate = PwStrtoTime($regdate);

		if ($oldinfo['username'] != stripcslashes($username)) {
			$rs = $db->get_one('SELECT COUNT(*) AS count FROM pw_members WHERE username='.pwEscape($username));
			if ($rs['count'] > 0) {
				adminmsg('username_exists');
			}
			$uc_edit['username'] = $username;
		}
		if ($uc_edit) {
			$ucuser = L::loadClass('Ucuser');
			list($ucstatus, $errmsg) = $ucuser->edit($uid, $oldinfo['username'], $uc_edit);
			if ($ucstatus < 0) {
				Showmsg($errmsg);
			}
		}
		$ustatus = ',userstatus=userstatus'.($publicmail ? '|' : '&~') .'(1<<6)';
		$ustatus.= ',userstatus=userstatus'.($receivemail ? '|' : '&~') .'(1<<7)';

		require_once(R_P.'require/bbscode.php');
		$cksign = convert($signature,$db_windpic,2);
		$signstatus = $cksign != $signature ? 1 : 0;

		if ($signstatus != getstatus($userstatus,9)) {
			$ustatus .= ',userstatus=userstatus'.($signstatus ? '|' : '&~') .'(1<<8)';
		}
		if ($groupid == 6) {
			$db->update("REPLACE INTO pw_banuser"
				. " SET " .pwSqlSingle(array(
					'uid'		=> $uid,
					'fid'		=> 0,
					'type'		=> 2,
					'startdate'	=> $timestamp,
					'days'		=> 0,
					'admin'		=> $admin_name,
					'reason'	=> ''
			),false));
		}
		if ($groupid == 6 || $groupid != $oldinfo['groupid']) {
			$_cache = getDatastore();
			$_cache->delete('UID_'.$uid);
		}

		$db->update("UPDATE pw_members"
			. " SET " . pwSqlSingle(array_merge($upmembers,array(
				'username'		=> $username,
				'gender'		=> $gender,
				'email'			=> $email,
				'regdate'		=> $regdate,
				'groupid'		=> $groupid,
				'site'			=> $site,
				'oicq'			=> $oicq,
				'icq'			=> $icq,
				'msn'			=> $msn,
				'yahoo'			=> $yahoo,
				'location'		=> $location,
				'bday'			=> $bday,
				'honor'			=> $honor,
				'yz'			=> $yz,
				'signature'		=> $signature,
				'introduce'		=> $introduce,
				'banpm'			=> $banpm
				))) . $ustatus
			. " WHERE uid=" . pwEscape($uid));

		$db->update("UPDATE pw_memberdata"
			. " SET " . pwSqlSingle(array(
					//'rvrc'		=> $rvrc,
					//'money'		=> $money,
					//'credit'	=> $credit,
					//'currency'	=> $currency,
					'postnum'	=> $postnum,
					'onlinetime'=> $onlinetime,
					'onlineip'	=> $userip
				))
			. " WHERE uid=" . pwEscape($uid)
		);

		$setCredit = array(
			'rvrc'		=> $rvrc,
			'money'		=> $money,
			'credit'	=> $credit,
			'currency'	=> $currency
		);
		if ($_CREDITDB && !empty($creditdb)) {
			foreach ($creditdb as $key => $value) {
				if (is_numeric($key) && is_numeric($value)) {
					$setCredit[$key] = $value;
				}
			}
		}
		require_once(R_P.'require/credit.php');
		$credit->runsql(array($uid => $setCredit), false);

		$mi = $db->get_one("SELECT uid,deposit,ddeposit $fieldadd FROM pw_memberinfo mb WHERE uid=".pwEscape($uid));
		if (!$mi) {
			if ($deposit || $ddeposit) {
				$db->update("INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$uid,'deposit'=>$deposit,'ddeposit'=>$ddeposit)));
			}
		} elseif ($deposit != $mi['deposit'] || $ddeposit != $mi['ddeposit']) {
			$db->update("UPDATE pw_memberinfo SET".pwSqlSingle(array('deposit'=>$deposit,'ddeposit'=>$ddeposit))."WHERE uid=".pwEscape($uid));
		}
		if ($customfield) {
			$fieldadd = '';
			foreach ($customfield as $key => $val) {
				$field = "field_".(int)$val['id'];
				InitGP(array($field),'P');
				if ($mi[$field] != $$field) {
					$fieldadd .= $fieldadd ? ",$field='{$$field}'" : "$field='{$$field}'";
				}
			}
			if ($fieldadd) {
				$db->pw_update(
					"SELECT uid FROM pw_memberinfo WHERE uid=".pwEscape($uid),
					"UPDATE pw_memberinfo SET $fieldadd WHERE uid=".pwEscape($uid),
					"INSERT INTO pw_memberinfo SET uid='$uid',$fieldadd"
				);
			}
		}
		/*
		if ($_CREDITDB && !empty($creditdb)) {
			foreach ($creditdb as $key => $value) {
				if (is_numeric($key) && is_numeric($value)) {
					$db->pw_update(
						"SELECT uid FROM pw_membercredit WHERE uid=".pwEscape($uid)."AND cid=".pwEscape($key),
						"UPDATE pw_membercredit SET value=".pwEscape($value)."WHERE uid=".pwEscape($uid)."AND cid=".pwEscape($key),
						"INSERT INTO pw_membercredit SET ".pwSqlSingle(array('uid'=>$uid,'cid'=>$key,'value'=>$value))
					);
				}
			}
		}
		*/
		adminmsg('operate_success');
	}
}
?>