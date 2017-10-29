<?php
!defined('PW_UPLOAD') && exit('Forbidden');
function Pwloaddl($mod,$ckfunc='mysqli_get_client_info'){//20080714
	return extension_loaded($mod) && $ckfunc && function_exists($ckfunc) ? true : false;
}

function SitStrCode($string,$key,$action='ENCODE'){
	$string	= $action == 'ENCODE' ? $string : base64_decode($string);
	$len	= strlen($key);
	$code	= '';
	for($i=0; $i<strlen($string); $i++){
		$k		= $i % $len;
		$code  .= $string[$i] ^ $key[$k];
	}
	$code = $action == 'DECODE' ? $code : str_replace('=','',base64_encode($code));
	return $code;
}
function generatestr($len) {
	mt_srand((double)microtime() * 1000000);
    $keychars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWYXZ";
	$maxlen = strlen($keychars)-1;
	$str = '';
	for ($i=0;$i<$len;$i++){
		$str .= $keychars[mt_rand(0,$maxlen)];
	}
	return substr(md5($str.time().$_SERVER["HTTP_USER_AGENT"].$GLOBALS['db_hash']),0,$len);
}
function N_insertinto($array,$start,$records=null){
	global $db,$times,$record;
	$record || $record = 800;
	$records && $record = $records;
	$array = is_array($array) ? array_slice($array,$start,$record) : array();
	foreach ($array as $value) {
		$value[1] && strtolower(substr($value[0],0,6)) == 'insert' && $value[0] = 'REPLACE'.substr($value[0],6);
		$db->query($value[0]);
		$times++;
	}
}
function N_atindex($array,$start,$records=null){
	global $db,$times,$record;
	$record || $record = 800;
	$records && $record = $records;
	$array = is_array($array) ? array_slice($array,$start,$record) : array();
	foreach ($array as $value) {
		$unique = 0;
		if ($value[3]=='PRIMARY') {
			$add = $drop = 'PRIMARY KEY';
		} elseif ($value[3]=='UNIQUE') {
			$add  = "UNIQUE $value[1]"; $drop = "INDEX $value[1]";
		} else {
			$add = $drop = "INDEX $value[1]";
			$unique = 1;
		}
		$indexkey = array();
		$query = $db->query("SHOW KEYS FROM $value[0]");
		while ($rt = $db->fetch_array($query)) {
			$indexkey[$rt['Key_name']][$rt['Column_name']] = $unique;
		}
		if ($indexkey[$value[1]]) {
			if ($value[2]) {
				$ifdo = false;
				$column = explode(',',$value[2]);
				if (count($indexkey[$value[1]])!=count($column)) {
					$ifdo = true;
				} else {
					foreach ($column as $v) {
						if (!$indexkey[$value[1]][$v]) {
							$ifdo = true; break;
						}
					}
				}
				$ifdo && $db->query("ALTER TABLE $value[0] DROP $drop,ADD $add ($value[2])");
			} elseif (empty($value[4]) || isset($indexkey[$value[1]][$value[4]])) {
				$db->query("ALTER TABLE $value[0] DROP $drop");
			}
		} elseif ($value[2]) {
			$db->query("ALTER TABLE $value[0] ADD $add ($value[2])");
		}
		$times++;
	}
}
function N_pointstable($array){
	global $db,$PW;
	!is_array($array) && $array = array();
	$returnarray = $array;
	foreach ($array as $value) {
		if (in_array($value[0],array('pw_tmsgs','pw_posts'))) {
			$replace = str_replace(array('pw_','_'),array($PW,'\_'),$value[0]);
			$query = $db->query("SHOW TABLE STATUS LIKE '{$replace}%'");
			while ($rt = $db->fetch_array($query)) {
				if ($replace!=$rt['Name']) {
					$rt['OldName'] = str_replace($PW,'pw_',$rt['Name']);
					$returnarray[] = array($rt['OldName'],$value[1],str_replace(" $value[0] "," $rt[OldName] ",$value[2]));
				}
			}
		}
	}
	return $returnarray;
}
function N_atfield($array,$start,$records=null){
	global $db,$times,$record;
	$record || $record = 800;
	$records && $record = $records;
	$array = is_array($array) ? array_slice($array,$start,$record) : array();
	foreach ($array as $value) {
		$rt = $db->get_one("SHOW COLUMNS FROM $value[0] LIKE '$value[1]'");
		$lowersql = strtolower($value[2]);
		if ((strpos($lowersql,' add ')!==false && $rt['Field']!=$value[1]) || (str_replace(array(' drop ',' change '),'',$lowersql)!=$lowersql && $rt['Field']==$value[1])) {
			$db->query($value[2]);
		}
		$times++;
	}
}
function N_createtable($array,$start,$records=null){
	global $db,$charset,$times,$record;
	$record || $record = 800;
	$records && $record = $records;
	!is_array($array) && $array = array();
	$array = array_slice($array,$start,$record);
	foreach ($array as $key => $value) {
		!$value[1] && $value[1] = 'MyISAM';
		$value[0] = "CREATE TABLE IF NOT EXISTS $key ($value[0]) ";
		if ($db->server_info() > '4.1') {
			$value[0] .= "ENGINE=$value[1]".($charset ? " DEFAULT CHARSET=$charset" : '');
		} else {
			$value[0] .= "TYPE=$value[1]";
		}
		$db->query($value[0]);
		$times++;
	}
}

function N_droptable($array,$start,$records=null) {
	global $db,$times,$record;	
	$record || $record = 800;
	$records && $record = $records;
	!is_array($array) && $array = array();
	$array = array_slice($array,$start,$record);
	foreach ($array as $key => $value) {
		$db->query("DROP TABLE IF EXISTS $value");
		$times++;
	}
}

function Promptmsg($msg,$tostep=null,$fromstep=null){
	@extract($GLOBALS, EXTR_SKIP);
	require(R_P.'lang/install_lang.php');
	$lang['showmsg'] = $lang['showmsg_upto'];
	$lang['welcome_msg'] = $lang['welcome_msgupto'];
	if (empty($tostep)) {
		$url = 'javascript:history.go(-1);';
		$lang['last'] = $lang['back'];
	} else {
		$url = "window.location.replace('$basename?step=$tostep');";
		$lang['last'] = $lang['redirect'];
	}
	$lang[$msg] && $msg = $lang[$msg];
	if (!empty($fromstep) && $times==$record) {
		list($stepnum,$steptype) = explode('_',$start);
		$end = (int)$stepnum+$record;
		strlen($steptype) && $end .= "_$steptype";
		$url = "window.location.replace('$basename?step=$fromstep&start=$end');";
	}
	if ($limit < $max) {
		$url = "window.location.replace('$basename?step=$fromstep&start=$limit');";
	}
	$msg = preg_replace("/{#(.+?)}/eis",'$\\1',$msg);
	require(R_P.'lang/promptmsg.htm');footer();
}
function footer(){
	global $footer;
	require_once(R_P.'lang/footer.htm');
	$output = trim(str_replace(array('<!--<!---->','<!---->',"\r"),'',ob_get_contents()),"\n");
	ob_end_clean();
	ob_start();
	echo $output;unset($output);exit;
}

function GetCrlf(){
	return GetPlatform()=='win' ? "\r\n" : "\n";
}
function GetPlatform(){
	if (strpos($_SERVER['HTTP_USER_AGENT'],'Win')!==false) {
		return 'win';
	} elseif (strpos($_SERVER['HTTP_USER_AGENT'],'Mac')!==false) {
		return 'mac';
	} elseif (strpos($_SERVER['HTTP_USER_AGENT'],'Linux')!==false) {
		return 'linux';
	} elseif (strpos($_SERVER['HTTP_USER_AGENT'],'Unix')!==false) {
		return 'unix';
	} elseif (strpos($_SERVER['HTTP_USER_AGENT'],'OS/2')!==false) {
		return 'os2';
	} else {
		return '';
	}
}
function N_writable($pathfile) {
	//Copyright (c) 2003-09 PHPWind
	//fix windows acls bug
	$isDir = substr($pathfile,-1)=='/' ? true : false;
	if ($isDir) {
		if (is_dir($pathfile)) {
			mt_srand((double)microtime()*1000000);
			$pathfile = $pathfile.'pw_'.uniqid(mt_rand()).'.tmp';
		} elseif (@mkdir($pathfile)) {
			return N_writable($pathfile);
		} else {
			return false;
		}
	}
	@chmod($pathfile,0777);
	$fp = @fopen($pathfile,'ab');
	if ($fp===false) return false;
	fclose($fp);
	$isDir && @unlink($pathfile);
	return true;
}

function change_array($array) {
	$reset = array();
	if (is_array($array)) {
		foreach ($array as $key => $value) {
			foreach ($value as $k => $v) {
				$reset[$k][$key] = $v;
			}
		}
	}
	return addslashes(serialize($reset));
}

function checkuptoadmin($CK) {
	Add_S($CK);
	global $db,$manager;
	if (is_array($manager) && CkInArray($CK[1],$manager)) {
		global $manager_pwd;
		$v_key = array_search($CK[1],$manager);
		if (!SafeCheck($CK,PwdCode($manager_pwd[$v_key]))) {
			$rt = $db->get_one("SELECT uid,username,groupid,groups,password FROM pw_members WHERE username=".pwEscape($CK[1]));
			if (!SafeCheck($CK,PwdCode($rt['password']))) {
				return false;
			}
		}
		return true;
	} elseif ($CK[1] == $manager) {
	   	global $manager_pwd;
		if (!SafeCheck($CK,PwdCode($manager_pwd))) {
			$rt = $db->get_one("SELECT uid,username,groupid,groups,password FROM pw_members WHERE username=".pwEscape($CK[1]));
			if (!SafeCheck($CK,PwdCode($rt['password']))) {
				return false;
			}
		}
		return true;
	} else {
		return false;
	}
}

function admincheck($uid,$username,$groupid,$groups) {
	global $db;
	$rt = $db->get_one("SELECT username,groupid,groups FROM pw_administrators WHERE uid=".pwEscape($uid));
	if ($rt && $rt['username'] == $username && ($rt['groupid'] == $groupid || strpos($rt['groups'], ",$groupid,") !== false)) {
		return true;
	} else {
		return false;
	}
}

function F_L_count($filename,$offset){
	global $onlineip;
	$count=0;
	if($fp=@fopen($filename,"rb")){
		flock($fp,LOCK_SH);
		fseek($fp,-$offset,SEEK_END);
		$readb=fread($fp,$offset);
		fclose($fp);
		$readb=trim($readb);
		$readb=explode("\n",$readb);
		$count=count($readb);$count_F=0;
		for($i=$count-1;$i>0;$i--){
			if(strpos($readb[$i],"|Logging Failed|$onlineip|")===false){
				break;
			}
			$count_F++;
		}
	}
	return $count_F;
}
function pwSetVersion($do,$reason='') {//PHPWind history
	global $wind_version,$from_version,$wind_repair,$reason,$timestamp,$db;
	$wind_version = strtoupper($wind_version);
	$from_version = strtoupper($from_version);
	$PHPWind = $db->get_value("SELECT db_value FROM pw_config WHERE db_name='PHPWind'");
	$PHPWind = $PHPWind ? unserialize($PHPWind) : array();
	$PHPWind['version'] && $from_version = $PHPWind['version'];
	$reason || $reason = $from_version == $wind_version ? ($wind_repair ? 'Repair wind' : 'Re-do it again') : "";
	$PHPWind['history'][] = "$do\t$timestamp\t$from_version\t$wind_version,$wind_repair\t$reason";
	$PHPWind['version'] = $wind_version;
	$PHPWind['repair'] = $wind_repair;
	$db->update("REPLACE INTO pw_config (db_name, db_value, decrip) VALUES ('PHPWind',".pwEscape(serialize($PHPWind)).",'PHPWind')");
	//@unlink(D_P.'data/bbscache/version');
	return $PHPWind['version'];
}
function pwGetVersion() {
	global $db,$PW;
	$version = readover(D_P.'data/bbscache/version');
	if (!$version) {
		$PHPWind = $db->get_value("SELECT db_value FROM pw_config WHERE db_name='PHPWind'");
		$PHPWind = $PHPWind ? unserialize($PHPWind) : array();
		if ($PHPWind['version']) {
			$version = $PHPWind['version'];
		} else {
			$rt = $db->get_one("SHOW TABLE STATUS LIKE '".str_replace('_','\_',$PW)."permission'");
			$pw_table = $rt['Name'];
			if ($pw_table==$PW.'permission') {
				$version = '7.0rc';
			} else {
				$rt = $db->get_one("SHOW TABLE STATUS LIKE '".str_replace('_','\_',$PW)."cache'");
				$pw_table = $rt['Name'];
				if ($pw_table==$PW.'cache') {
					$version = '6.3.2';
				}
			}
		}
		writeover(D_P.'data/bbscache/version',$version);
	}
	return $version;
}

function arr_unique($array){
	if (is_array($array)) {
		$temp_array = array();
		foreach ($array as $key => $value) {
			$var_md5 = md5(is_array($value) ? serialize($value) : $value);
			if (in_array($var_md5,$temp_array)) {
				unset($array[$key]);
			} else {
				$temp_array[] = $var_md5;
			}
		}
	}
	return $array;
}
function updatemedal_list(){
	global $db;
	$query = $db->query("SELECT uid FROM pw_medaluser GROUP BY uid");
	$medaldb = '<?php die;?>0';
	while ($rt = $db->fetch_array($query)) {
		$medaldb .= ','.$rt['uid'];
	}
	writeover(D_P.'data/bbscache/medals_list.php',$medaldb);
}
function GetLang($lang,$EXT='php'){
	global $tplpath;
	if (file_exists(R_P."template/$tplpath/lang_$lang.$EXT")) {
		return R_P."template/$tplpath/lang_$lang.$EXT";
	} elseif (file_exists(R_P."template/wind/lang_$lang.$EXT")) {
		return R_P."template/wind/lang_$lang.$EXT";
	} else {
		exit("Can not find lang_$lang.$EXT file");
	}
}

function getfavor($tids) {
	$tids  = explode('|',$tids);
	$tiddb = array();
	foreach ($tids as $key => $t) {
		if ($t) {
			$v = explode(',',$t);
			foreach ($v as $k => $v1) {
				$tiddb[$key][$v1] = $v1;
			}
		}
	}
	return $tiddb;
}

function checkFields($tableName,$fieldName){
	global $db;
	$isField = $db->get_one("show columns from ".$tableName." like '".$fieldName."'");
	if(!$isField){
		$db->query("ALTER TABLE pw_argument add tpcid smallint(6) NOT NULL default '0'");
	}
}
?>