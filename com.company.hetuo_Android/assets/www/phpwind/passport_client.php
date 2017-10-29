<?php
require_once('global.php');
require_once(R_P.'require/checkpass.php');

InitGP(array('action','forward','verify'));
InitGP(array('userdb'),'GP',0);
if(!$db_pptifopen || $db_ppttype!='client'){
	Showmsg('passport_close');
}
if(empty($db_pptkey) || md5($action.$userdb.$forward.$db_pptkey) != $verify){
	Showmsg('passport_safe');
}
$_db_hash=$db_hash;

$db_hash=$db_pptkey;
parse_str(StrCode($userdb,'DECODE'),$userdb);

if($action=='login'){
	$userdb = Char_cv($userdb);
	if(!$userdb['time'] || !$userdb['username'] || !$userdb['password']){
		Showmsg('passport_data');
	}
	if($timestamp-$userdb['time']>3600){
		Showmsg('passport_error');
	}

	$member_field = array('username','password','email');
	$memberdata_field = array('rvrc','money','credit','currency');

	$sql='';
	foreach($member_field as $key=>$val){
		$sql .= ','.$val;
	}
	$rt=$db->get_one("SELECT uid $sql FROM pw_members WHERE username=".pwEscape($userdb['username']));
	if($rt){
		$sql=$sql2=array();
		foreach($userdb as $key=>$val){
			if(in_array($key,$member_field) && $rt[$key] != $val){
				$sql[$key] = $val;
			}elseif(in_array($key,$memberdata_field) && strpos(",$db_pptcredit,",",$key,")!==false){
				$sql2[$key] = $val;
			}
		}
		if ($sql) {
			$sql = pwSqlSingle($sql);
			$db->update("UPDATE pw_members SET $sql WHERE uid=".pwEscape($rt['uid']));
		}
		if ($sql2) {
			$sql2 = pwSqlSingle($sql2);
			$db->update("UPDATE pw_memberdata SET $sql2 WHERE uid=".pwEscape($rt['uid']));
		}

		$winduid = $rt['uid'];
	} else{
		$sql1 = $sql2 = array();
		foreach($userdb as $key=>$val){
			if(in_array($key,$member_field)){
				$sql1[$key] = $val;
			}elseif(in_array($key,$memberdata_field) && strpos(",$db_pptcredit,",",$key,")!==false){
				$sql2[$key] = (int)$val;
			}
		}
		$sql1 += array(
			'groupid'	=> -1,
			'memberid'	=> 8,
			'gender'	=> 0,
			'regdate'	=> $timestamp
		);
		$db->update("REPLACE INTO pw_members SET".pwSqlSingle($sql1));
		$winduid = $db->insert_id();
		$sql2 += array(
			'uid'		=> $winduid,
			'postnum'	=> 0,
			'lastvisit'	=> $timestamp,
			'thisvisit'	=> $timestamp,
			'onlineip'	=> $onlineip
		);
		$db->update("REPLACE INTO pw_memberdata SET".pwSqlSingle($sql2));
		$db->update("UPDATE pw_bbsinfo SET newmember=".pwEscape($userdb['username']).",totalmember=totalmember+1 WHERE id='1'");
	}
	$db_hash=$_db_hash;
	$windpwd = PwdCode($userdb['password']);
	Cookie("winduser",StrCode($winduid."\t".$windpwd),$userdb['cktime']);
	Cookie('lastvisit','',0);
	Loginipwrite();
	ObHeader($forward ? $forward : $db_bbsurl);
} elseif($action=='quit'){
	$db_hash=$_db_hash;
	Loginout();
	ObHeader($forward ? $forward : $db_bbsurl);
}
?>