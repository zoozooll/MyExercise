<?php
defined('P_W') || exit('Forbidden');
define('WIND_VERSION','7.5 SP3,20100303');


/**
 * 获取客户端IP
 *
 * @return string
 */
function pwGetIp() {
	global $pwServer,$db_xforwardip;
	if ($db_xforwardip) {
		if ($pwServer['HTTP_X_FORWARDED_FOR'] && $pwServer['REMOTE_ADDR']) {
			if (strstr($pwServer['HTTP_X_FORWARDED_FOR'], ',')) {
				$x = explode(',', $pwServer['HTTP_X_FORWARDED_FOR']);
				$pwServer['HTTP_X_FORWARDED_FOR'] = trim(end($x));
			}
			if (preg_match('/^([0-9]{1,3}\.){3}[0-9]{1,3}$/',$pwServer['HTTP_X_FORWARDED_FOR'])) {
				return $pwServer['HTTP_X_FORWARDED_FOR'];
			}
		} elseif ($pwServer['HTTP_CLIENT_IP'] && preg_match('/^([0-9]{1,3}\.){3}[0-9]{1,3}$/',$pwServer['HTTP_CLIENT_IP'])) {
			return $pwServer['HTTP_CLIENT_IP'];
		}
	}
	$db_xforwardip = 0;
	if (preg_match('/^([0-9]{1,3}\.){3}[0-9]{1,3}$/',$pwServer['REMOTE_ADDR'])) {
		return $pwServer['REMOTE_ADDR'];
	}
	return 'Unknown';
}

/**
 * 分析服务器负载
 *
 * 只针对*unix服务器有效
 *
 * @param int $loadavg 负载最大值
 * @return boolean 是否超过最大负载
 */
function pwLoadAvg($loadavg) {
	$avgstats = 0;
	if (file_exists('/proc/loadavg')) {
		if ($fp = @fopen('/proc/loadavg','r')) {
			$avgdata = @fread($fp,6);
			@fclose($fp);
			list($avgstats) = explode(' ',$avgdata);
		}
	}
	/*非*NIX系统负载分析,适合独立服务器
	elseif (!file_exists(D_P.'data/avgstats.txt') || $timestamp-pwFilemtime(D_P.'data/avgstats.txt')>1800) {
		if (strpos(strtolower(PHP_OS),'win')!==false && function_exists('shell_exec')) {
			if (!($sstats = @shell_exec("typeperf \"Processor(_Total)\% Processor Time\" -sc 1"))) return 0;
			$sreply = explode("\n",str_replace("\r",'',$sstats));
			list($sstats) = array_slice($sreply,2,1);
			list(,$statline) = explode(',',str_replace('"','',$sstats));
			$avgstats = round($statline,2);
		} elseif (function_exists('exec') && ($sstats = @exec("uptime")) && preg_match("|(?:averages)?\: ([0-9\.]+)[^0-9\.]+([0-9\.]+)[^0-9\.]+([0-9\.]+)\s*|",$sstats,$statline)) {
			$avgstats = $statline[1];
		} else {
			$avgstats = 0;
		}
		writeover(D_P.'data/avgstats.txt',$avgstats);
	} else {
		$avgstats = readover(D_P.'data/avgstats.txt');
	}
	*/
	if ($avgstats > $loadavg) {
		return true;
	} else {
		return false;
	}
}

/**
 * CC攻击处理
 *
 * CC攻击会导致服务器负载过大,对相关客户端请求进行处理并日志
 *
 * @param int $cc 服务器负载参数
 * @return void
 */
function pwDefendCc($cc) {
	global $timestamp,$onlineip,$pwServer,$db_xforwardip;
	if ($cc==2 && !empty($pwServer['HTTP_USER_AGENT'])) {
		$useragent = strtolower($pwServer['HTTP_USER_AGENT']);
		if (str_replace(array('spider','google','msn','yodao','yahoo','http:'),'',$useragent)!=$useragent) {
			$cc = 1;
		}
	}
	Cookie('c_stamp',$timestamp,0);
	$c_stamp = GetCookie('c_stamp');
	$c_crc32 = substr(md5($c_stamp.$pwServer['HTTP_REFERER']),0,10);
	$c_banedip = readover(D_P.'data/ccbanip.txt');
	if ($c_banedip && $c_ipoffset = strpos("$c_banedip\n","\t$onlineip\n")) {
		$c_ltt = substr($c_banedip,$c_ipoffset-10,10);
		$c_crc32==$c_ltt && exit('Forbidden, Please turn off CC');
		writeover(D_P.'data/ccbanip.txt',str_replace("\n$c_ltt\t$onlineip",'',$c_banedip));
	}
	if (($db_xforwardip || $cc==2) && ($timestamp-$c_stamp>3 || $timestamp<$c_stamp)) {
		$c_on = false;
		if ($c_fp = @fopen(D_P.'data/ccip.txt','rb')) {
			flock($c_fp,LOCK_SH);
			$c_size = 27*800;
			fseek($c_fp,-$c_size,SEEK_END);
			while (!feof($c_fp)) {
				$c_value = explode("\t",fgets($c_fp,29));
				if (trim($c_value[1])==$onlineip && $c_crc32==$c_value[0]) {
					$c_on = true; break;
				}
			}
			fclose($c_fp);
		}
		if ($c_on) {
			echo 'Forbidden, Please Refresh';
			$ccbanip = '';
			$c_banedip && $ccbanip .= implode("\n",array_slice(explode("\n",$c_banedip),-999));
			$ccbanip .= "\n".$c_crc32."\t".$onlineip;
			writeover(D_P.'data/ccbanip.txt',$ccbanip); exit;
		}
		@filesize(D_P.'data/ccip.txt')>27*1000 && P_unlink(D_P.'data/ccip.txt');
		writeover(D_P.'data/ccip.txt',"$c_crc32\t$onlineip\n",'ab');
	}
}

/**
 * 删除多余全局变量
 *
 * 多余的全局变量,会对站点安全构成威胁.需要保留的变量在$allowed中说明
 *
 */
function pwInitGlobals() {
	$allowed = array('GLOBALS'=>1, '_GET'=>1, '_POST'=>1, '_COOKIE'=>1, '_FILES'=>1, '_SERVER'=>1, 'P_S_T'=>1);
	foreach ($GLOBALS as $key => $value) {
		if (!isset($allowed[$key])) {
			$GLOBALS[$key] = null;
			unset($GLOBALS[$key]);
		}
	}
	if (!get_magic_quotes_gpc()) {
		Add_S($_POST);
		Add_S($_GET);
		Add_S($_COOKIE);
	}
	Add_S($_FILES);
	$GLOBALS['pwServer'] = GetServer(array('HTTP_REFERER','HTTP_HOST','HTTP_X_FORWARDED_FOR','HTTP_USER_AGENT','HTTP_CLIENT_IP', 'HTTP_SCHEME','HTTPS','PHP_SELF','REQUEST_URI','REQUEST_METHOD','REMOTE_ADDR','QUERY_STRING'));
	!$GLOBALS['pwServer']['PHP_SELF'] && $GLOBALS['pwServer']['PHP_SELF'] = GetServer('SCRIPT_NAME');
}

function Cookie($ck_Var,$ck_Value,$ck_Time='F',$p=true){
	global $db_ckpath,$db_ckdomain,$timestamp,$pwServer;

//	if (!$pwServer['REQUEST_URI'] || ($https = @parse_url($pwServer['REQUEST_URI']))===false) {
//		$https = array();
//	}
//	if ($https['scheme']=='https' || (empty($https['scheme']) && ($pwServer['HTTP_SCHEME']=='https' || $pwServer['HTTPS'] && strtolower($pwServer['HTTPS'])!='off'))) {
//		$ck_Secure = true;
//	} else {
//		$ck_Secure = false;
//	}
	/* performance 2010-3-12 */
	static $ck_Secure = null;
	if($ck_Secure === null ){
		if (!$pwServer['REQUEST_URI'] || ($https = @parse_url($pwServer['REQUEST_URI']))===false) {
			$https = array();
		}
		if ($https['scheme']=='https' || (empty($https['scheme']) && ($pwServer['HTTP_SCHEME']=='https' || $pwServer['HTTPS'] && strtolower($pwServer['HTTPS'])!='off'))) {
			$ck_Secure = true;
		} else {
			$ck_Secure = false;
		}
	}

	if (P_W!='admincp') {
		$ckpath = !$db_ckpath ? '/' : $db_ckpath;
		$ckdomain = $db_ckdomain;
	} else {
		$ckpath = '/';
		$ckdomain = '';
	}
	$ck_Httponly = false;
	if ($ck_Var=='AdminUser' || $ck_Var=='winduser') {
		$agent = strtolower($pwServer['HTTP_USER_AGENT']);
		if (!($agent && preg_match('/msie ([0-9]\.[0-9]{1,2})/i', $agent) && strstr($agent, 'mac'))) {
			$ck_Httponly = true;
		}
	}

	strlen($ck_Value) > 512 && $ck_Value = substr($ck_Value,0,512);
	$p && $ck_Var = CookiePre().'_'.$ck_Var;
	if ($ck_Time=='F') {
		$ck_Time = $timestamp+31536000;
	} elseif ($ck_Value=='' && $ck_Time==0) {
		return setcookie($ck_Var,'',$timestamp-31536000,$ckpath,$ckdomain,$ck_Secure);
	}

	if (PHP_VERSION < 5.2) {
		return setcookie($ck_Var,$ck_Value,$ck_Time,$ckpath.($ck_Httponly ? '; HttpOnly' : ''),$ckdomain,$ck_Secure);
	} else {
		return setcookie($ck_Var,$ck_Value,$ck_Time,$ckpath,$ckdomain,$ck_Secure,$ck_Httponly);
	}
}
function GetCookie($Var){
	return $_COOKIE[CookiePre().'_'.$Var];
}
function CookiePre(){
	return ($GLOBALS['db_cookiepre']) ? $GLOBALS['db_cookiepre'] : substr(md5($GLOBALS['db_sitehash']),0,5);
}
function P_unlink($filename){
	return @unlink(Pcv($filename));
}
function openfile($filename){
	$filedb = explode('<:wind:>',str_replace("\n","\n<:wind:>",readover($filename)));
	$count = count($filedb)-1;
	if ($count > -1 && (!$filedb[$count] || $filedb[$count]=="\r")) {
		unset($filedb[$count]);
	}
	empty($filedb) && $filedb[0] = '';
	return $filedb;
}
function readover($filename,$method='rb'){
	$filename = Pcv($filename);
	$filedata = '';
	if ($handle = @fopen($filename,$method)) {
		flock($handle,LOCK_SH);
		$filedata = @fread($handle,filesize($filename));
		fclose($handle);
	}
	return $filedata;
}
function writeover($filename,$data,$method='rb+',$iflock=1,$check=1,$chmod=1){
	//Copyright (c) 2003-09 PHPWind
	$filename = Pcv($filename,$check);
	touch($filename);
	$handle = fopen($filename,$method);
	$iflock && flock($handle,LOCK_EX);
	fwrite($handle,$data);
	$method=='rb+' && ftruncate($handle,strlen($data));
	fclose($handle);
	$chmod && @chmod($filename,0777);
}
function PwdCode($pwd){
	return md5($GLOBALS['pwServer']['HTTP_USER_AGENT'].$pwd.$GLOBALS['db_hash']);
}
function SafeCheck($CK,$PwdCode,$var='AdminUser',$expire=1800){
	global $timestamp;
	if ($timestamp-$CK[0]>$expire || $CK[2]!=md5($PwdCode.$CK[0])) {
		Cookie($var,'',0);
		return false;
	}
	$CK[0] = $timestamp;
	$CK[2] = md5($PwdCode.$CK[0]);
	Cookie($var,StrCode(implode("\t",$CK)));
	return true;
}
function StrCode($string,$action='ENCODE'){
	$action != 'ENCODE' && $string = base64_decode($string);
	$code = '';
	$key  = substr(md5($GLOBALS['pwServer']['HTTP_USER_AGENT'].$GLOBALS['db_hash']),8,18);
	$keylen = strlen($key); $strlen = strlen($string);
	for ($i=0;$i<$strlen;$i++) {
		$k		= $i % $keylen;
		$code  .= $string[$i] ^ $key[$k];
	}
	return ($action!='DECODE' ? base64_encode($code) : $code);
}
function substrs($content,$length,$add='Y'){
	if (strlen($content)>$length) {
		if ($GLOBALS['db_charset']!='utf-8') {
			$retstr = '';
			for ($i=0;$i<$length-2;$i++) {
				$retstr .= ord($content[$i]) > 127 ? $content[$i].$content[++$i] : $content[$i];
			}
			return $retstr.($add=='Y' ? ' ..' : '');
		}
		return utf8_trim(substr($content,0,$length)).($add=='Y' ? ' ..' : '');
	}
	return $content;
}
function utf8_trim($str) {
	$hex = '';
	$len = strlen($str)-1;
	for ($i=$len;$i>=0;$i-=1) {
		$ch = ord($str[$i]);
		$hex .= " $ch";
		if (($ch & 128)==0 || ($ch & 192)==192) {
			return substr($str,0,$i);
		}
	}
	return $str.$hex;
}

/**
 * Format a GMT/UTC date/time
 *
 * @param int $timestamp
 * @param string $timeformat
 * @return string
 */
function get_date($timestamp,$timeformat=null){
	static $format=null,$time=null;
	if (!isset($time)) {
		global $db_datefm,$db_timedf,$_datefm,$_timedf;
		$format = $_datefm ? $_datefm : $db_datefm;
		if ($_timedf && $_timedf!='111') {
			$time = $_timedf*3600;
		} elseif ($db_timedf && $db_timedf!='111') {
			$time = $db_timedf*3600;
		} else {
			$time = 0;
		}
	}
	empty($timeformat) && $timeformat = $format;
	return gmdate($timeformat,$timestamp+$time);
}
function geturl($attachurl,$type = null,$thumb = null) {
	global $attachdir,$attachpath,$db_ftpweb,$attach_url;
	if ($thumb) {
		if (file_exists($attachdir.'/thumb/'.$attachurl)) {
			return array($attachpath.'/thumb/'.$attachurl,'Local');
		} elseif (file_exists($attachdir.'/'.$attachurl)) {
			return array($attachpath.'/'.$attachurl,'Local');
		} elseif ($db_ftpweb) {
			$attachurl = 'thumb/'.$attachurl;
		}
	}
	if (file_exists($attachdir.'/'.$attachurl)) {
		return array($attachpath.'/'.$attachurl,'Local');
	}
	if ($db_ftpweb && !$attach_url || $type == 'lf') {
		return array($db_ftpweb.'/'.$attachurl,'Ftp');
	}
	if (!$db_ftpweb && !is_array($attach_url)) {
		return array($attach_url.'/'.$attachurl,'att');
	}
	if (!$db_ftpweb && count($attach_url) == 1) {
		return array($attach_url[0].'/'.$attachurl,'att');
	}
	if ($type == 'show') {
		return ($db_ftpweb || $attach_url) ? 'imgurl' : 'nopic';
	}
	if ($db_ftpweb && $fp = @fopen($db_ftpweb.'/'.$attachurl,'rb')) {
		@fclose($fp);
		return array($db_ftpweb.'/'.$attachurl,'Ftp');
	}
	if (!empty($attach_url)) {
		foreach ($attach_url as $value) {
			if ($value != $db_ftpweb && ($fp = @fopen($value.'/'.$attachurl,'rb'))) {
				@fclose($fp);
				return array($value.'/'.$attachurl,'att');
			}
		}
	}
	return false;
}

function randstr($lenth) {
	return substr(md5(num_rand($lenth)),mt_rand(0,32-$lenth),$lenth);
}
function num_rand($lenth) {
	mt_srand((double)microtime()*1000000);
	$randval = '';
	for ($i = 0; $i<$lenth; $i++) {
		$randval .= ("" == $randval) ? mt_rand(1,9) : mt_rand(0,9);
	}
	return $randval;
}
function PwStrtoTime($time){
	global $db_timedf;
	return function_exists('date_default_timezone_set') ? strtotime($time) - $db_timedf*3600 : strtotime($time);
}
function Pcv($filename,$ifcheck=1){
	$tmpname = strtolower($filename);
	$tmparray = array('://',"\0");
	$ifcheck && $tmparray[] = '..';
	if (str_replace($tmparray,'',$tmpname)!=$tmpname) {
		exit('Forbidden');
	}
	return $filename;
}
function pwDirCv($dir){
	//Copyright (c) 2003-09 PHPWind
	$dir = str_replace(array("'",'#','=','`','$','%','&',';'),'',$dir);
	return trim(preg_replace('/(\/){2,}|(\\\){1,}/','/',$dir),'/');
}
function GetTtable($tid) {
	global $db_tlist;
	if ($db_tlist && is_array($db_tlist)) {
		foreach ($db_tlist as $key => $value) {
			if ($key>0 && $tid>$value[1]) {
				return 'pw_tmsgs'.(int)$key;
			}
		}
	}
	return 'pw_tmsgs';
}
function getDescripByTid($tid){
	global $db;
	$tid = (int)$tid;
	if (!$tid) return '';
	$table	= GetTtable($tid);
	$content= $db->get_value("SELECT content FROM $table WHERE tid=".pwEscape($tid));
	$content= stripWindCode($content);
	$content= strip_tags($content);
	return substrs($content,200);
}
function GetPtable($tbid,$tid=null){
	if ($GLOBALS['db_plist'] && is_array($plistdb = $GLOBALS['db_plist'])) {
		if ($tbid=='N' && !empty($tid)) {
			$tbid = $GLOBALS['db']->get_value('SELECT ptable FROM pw_threads WHERE tid='.pwEscape($tid,false));
		}
		if ((int)$tbid>0 && array_key_exists($tbid,$plistdb)) {
			return 'pw_posts'.$tbid;
		}
	}
	return 'pw_posts';
}

function GetPcatetable($pcid){//获取团购活动表
	global $db_pcids;
	$pcid = (int)$pcid;
	if ($pcid > 0 && trim($db_pcids,',')) {
		if (strpos(",".$db_pcids.",",",".$pcid.",") !== false) {
			return 'pw_pcvalue'.$pcid;
		}
	}
	Showmsg('undefined_action');
}

function GetTopcitable($modelid){//获取分类信息表
	global $db_modelids;
	$modelid = (int)$modelid;
	if ($modelid > 0 && trim($db_modelids,',')) {
		if (strpos(",".$db_modelids.",",",".$modelid.",") !== false) {
			return 'pw_topicvalue'.$modelid;
		}
	}
	Showmsg('undefined_action');
}

function Sql_cv($var){
	global $db;
	$db->update('INSERT INTO pw_sqlcv SET var='.pwEscape($var),0);
	$id = $db->insert_id();
	$rt = $db->get_one('SELECT var FROM pw_sqlcv WHERE id='.pwEscape($id));
	$db->update('DELETE FROM pw_sqlcv WHERE id='.pwEscape($id));
	return $rt['var'];
}
function CkInArray($needle,$haystack) {
	if (!$needle || empty($haystack) || !in_array($needle,$haystack)) {
		return false;
	}
	return true;
}

function pw_var_export($input,$t = null) {
	switch (gettype($input)) {
		case 'string':
			return "'".str_replace(array("\\","'"),array("\\\\","\'"),$input)."'";
		case 'array':
			$output = "array(\r\n";
			foreach ($input as $key => $value) {
				$output .= $t."\t".pw_var_export($key,$t."\t").' => '.pw_var_export($value,$t."\t");
				$output .= ",\r\n";
			}
			$output .= $t.')';
			return $output;
		case 'boolean':
			return $input ? 'true' : 'false';
		case 'NULL':
			return 'NULL';
		case 'integer':
		case 'double':
		case 'float':
			return "'".(string)$input."'";
	 }
	 return 'NULL';
}


/**
 * 针对SQL语句的变量进行反斜线过滤,并两边添加单引号
 *
 * @param mixed $var 过滤前变量
 * @param boolean $strip 数据是否经过stripslashes处理
 * @param boolean $is_array 变量是否为数组
 * @return mixed 过滤后变量
 */
function pwEscape($var,$strip = true,$is_array=false) {
	if (is_array($var)) {
		if (!$is_array) return " '' ";
		foreach ($var as $key => $value) {
			$var[$key] = trim(pwEscape($value,$strip));
		}
		return $var;
	} elseif (is_numeric($var)) {
		return " '".$var."' ";
	} else {
		return " '".addslashes($strip ? stripslashes($var) : $var)."' ";
	}
}
/**
 * 过滤数组每个元素值,并进行单引号合并
 *
 * @param array $array 源数组
 * @param boolean $strip 数据是否经过stripslashes处理
 * @return string 合并后字符串
 */
function pwImplode($array,$strip=true) {
	return implode(',',pwEscape($array,$strip,true));
}
/**
 * 构造单记录数据更新SQL语句
 *  格式: field='value',field='value'
 *
 * @param array $array 更新的数据,格式: array(field1=>'value1',field2=>'value2',field3=>'value3')
 * @param boolean $strip 数据是否经过stripslashes处理
 * @return string SQL语句
 */
function pwSqlSingle($array,$strip=true) {
	//Copyright (c) 2003-09 PHPWind
	$array = pwEscape($array,$strip,true);
	$str = '';
	foreach ($array as $key => $val) {
		$str .= ($str ? ', ' : ' ').$key.'='.$val;
	}
	return $str;
}
/**
 * 构造批量数据更新SQL语句
 *  格式: ('value1[1]','value1[2]','value1[3]'),('value2[1]','value2[2]','value2[3]')
 *
 * @param array $array 更新的数据,格式: array(array(value1[1],value1[2],value1[3]),array(value2[1],value2[2],value2[3]))
 * @param boolean $strip 数据是否经过stripslashes处理
 * @return string SQL语句
 */
function pwSqlMulti($array,$strip=true) {
	//Copyright (c) 2003-09 PHPWind
	$str = '';
	foreach ($array as $val) {
		if (!empty($val)) {
			$str .= ($str ? ', ' : ' ') . '(' . pwImplode($val,$strip) .') ';
		}
	}
	return $str;
}
/**
 * SQL查询中,构造LIMIT语句
 *
 * @param integer $start 开始记录位置
 * @param integer $num 读取记录数目
 * @return string SQL语句
 */
function pwLimit($start,$num=false) {
	return ' LIMIT '.($start <= 0 ? 0 : (int)$start).($num ? ','.abs($num) : '');
}
function getstatus($status,$b,$getv = 1) {
	return $status >> --$b & $getv;
}
/**
 * 获取指定语言包里的某一内容信息
 *
 * @param string $T 语言包文件名
 * @param string $I 指定语言信息
 * @param array $L 额外变量
 * @param array $M 是否调用模式下的语言文件
 * @return string
 */
function getLangInfo($T,$I,$L=false,$M=false) {
	static $lang;
	if (!isset($lang[$T])) {
		if ($M==false) {
			require Pcv(GetLang($T));
		} else {
			require Pcv(getModeLang($T));
		}
	}
	if (isset($lang[$T][$I])) {
		eval('$I="'.addcslashes($lang[$T][$I],'"').'";');
	}
	return $I;
}

/*获取积分操作*/
function GetCreditLang($T,$logtype) {
	static $lang;
	if (!isset($lang[$T])) {
		require Pcv(GetLang($T));
	}
	$pop = '';
	if (isset($lang[$T][$logtype])) {
		eval('$pop="'.addcslashes($lang[$T][$logtype],'"').'";');
	}
	return $pop;
}

function getModeLang($lang,$EXT='php'){
	if (defined('M_P') && file_exists(M_P."lang/lang_$lang.$EXT")) {
		return M_P."lang/lang_$lang.$EXT";
	} else {
		return GetLang($lang);
	}
}

function InitGP($keys,$method=null,$cvtype=1){//0=null,1=Char_cv,2=int
	//Copyright (c) 2003-09 PHPWind
	!is_array($keys) && $keys = array($keys);
	foreach ($keys as $key) {
		if ($key == 'GLOBALS') continue;
		$GLOBALS[$key] = NULL;
		if ($method != 'P' && isset($_GET[$key])) {
			$GLOBALS[$key] = $_GET[$key];
		} elseif ($method != 'G' && isset($_POST[$key])) {
			$GLOBALS[$key] = $_POST[$key];
		}
		if (isset($GLOBALS[$key]) && !empty($cvtype) || $cvtype==2) {
			$GLOBALS[$key] = Char_cv($GLOBALS[$key],$cvtype==2,true);
		}
	}
}
function GetGP($key,$method=null){
	//Copyright (c) 2003-09 PHPWind
	if ($method == 'G' || $method != 'P' && isset($_GET[$key])) {
		return $_GET[$key];
	}
	return $_POST[$key];
}
/**
 * 读取指定的全局环境变量值
 *
 * @param mixed $keys 环境变量名，可数组或单值
 * @return mixed 根据参数个数返回指定环境变量值
 */
function GetServer($keys){
	//Copyright (c) 2003-09 PHPWind
	$server = array();
	$array = (array)$keys;
	foreach ($array as $key) {
		$server[$key] = NULL;
		if (isset($_SERVER[$key])) {
			$server[$key] = str_replace(array('<','>','"',"'",'%3C','%3E','%22','%27','%3c','%3e'),'',$_SERVER[$key]);
		}
	}
	return is_array($keys) ? $server : $server[$keys];
}
function Char_cv($mixed,$isint=false,$istrim=false) {
	//Copyright (c) 2003-09 PHPWind
	if (is_array($mixed)) {
		foreach ($mixed as $key => $value) {
			$mixed[$key] = Char_cv($value,$isint,$istrim);
		}
	} elseif ($isint) {
		$mixed = (int)$mixed;
	} elseif (!is_numeric($mixed) && ($istrim ? $mixed = trim($mixed) : $mixed) && $mixed) {
		$mixed = str_replace(array("\0","%00","\r"),'',$mixed);
		$mixed = preg_replace(
			array('/[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]/','/&(?!(#[0-9]+|[a-z]+);)/is'),
			array('','&amp;'),
			$mixed
		);
		$mixed = str_replace(array("%3C",'<'),'&lt;',$mixed);
		$mixed = str_replace(array("%3E",'>'),'&gt;',$mixed);
		$mixed = str_replace(array('"',"'","\t",'  '),array('&quot;','&#39;','    ','&nbsp;&nbsp;'),$mixed);
	}
	return $mixed;
}
function CheckVar(&$var) {
	if (is_array($var)) {
		foreach ($var as $key => $value) {
			CheckVar($var[$key]);
		}
	} elseif (P_W != 'admincp') {
		$var = str_replace(array('..',')','<','='),array('&#46;&#46;','&#41;','&#60;','&#61;'),$var);
	} elseif (str_replace(array('<iframe','<meta','<script'),'',$var)!=$var) {
		global $basename;
		$basename = 'javascript:history.go(-1);';
		adminmsg('word_error');
	}
}
function Add_S(&$array){
	if (is_array($array)) {
		foreach ($array as $key => $value) {
			if (is_array($value)) {
				Add_S($array[$key]);
			} else {
				$array[$key] = addslashes($value);
			}
		}
	}
}
function pwWritable($pathfile) {
	//Copyright (c) 2003-09 PHPWind
	//fix windows acls bug noizy
	substr($pathfile,-1)=='/' && $pathfile = substr($pathfile,0,-1);
	if (is_dir($pathfile)) {
		mt_srand((double)microtime()*1000000);
		$pathfile = $pathfile.'/pw_'.uniqid(mt_rand()).'.tmp';
	}
	$unlink = file_exists($pathfile) ? false : true;
	$fp = @fopen($pathfile,'ab');
	if ($fp===false) return false;
	fclose($fp);
	if ($unlink) @unlink($pathfile);
	return true;
}
/*
 * 获取论坛的普通版块id
 */
function getCommonFid() {
	static $fids = null;

	if (!isset($fids)) {
		if (pwFilemtime(D_P.'data/bbscache/commonforum.php') < pwFilemtime(D_P.'data/bbscache/forum_cache.php')) {
			global $db;
			$query = $db->query("SELECT fid FROM pw_forums WHERE type<>'category' AND cms<>1 AND password='' AND forumsell='' AND f_type<>'hidden' AND allowvisit=''");
			while ($rt = $db->fetch_array($query)) {
				$fids .= ",'$rt[fid]'";
			}
			$fids && $fids = substr($fids,1);
			writeover(D_P.'data/bbscache/commonforum.php',"<?php\r\n\$fids = \"$fids\";\r\n?>");
		} else {
			include (D_P.'data/bbscache/commonforum.php');
		}
	}
	return $fids;
}
/*
 * 获取论坛的特殊版块id
 */
function getSpecialFid() {
	static $fids = null;

	if (!isset($fids)) {
		if (pwFilemtime(D_P.'data/bbscache/specialforum.php') < pwFilemtime(D_P.'data/bbscache/forum_cache.php')) {
			global $db;
			$query = $db->query("SELECT fid FROM pw_forums WHERE type<>'category' AND (cms=1 OR password!='' OR forumsell!='' OR f_type='hidden' OR allowvisit!='')");
			while ($rt = $db->fetch_array($query)) {
				$fids .= ",'$rt[fid]'";
			}
			$fids && $fids = substr($fids,1);
			writeover(D_P.'data/bbscache/specialforum.php',"<?php\r\n\$fids = \"$fids\";\r\n?>");
		} else {
			include (D_P.'data/bbscache/specialforum.php');
		}
	}
	return $fids;
}
/**
 * 当前登录用户版块管理权限
 *
 * @param string $isBM 用户是否为版主
 * @param string $rkey 指定要获取的权限名
 * @param integer $fid 版块FID
 * @return mixed 返回指定权限值
 */
function pwRights($isBM=false,$rkey='',$fid=false) {
	//Copyright (c) 2003-09 PHPWind
	static $_SYSTEM = null;

	if ($GLOBALS['gp_gptype']!='system' && $GLOBALS['gp_gptype']!='special') return false;

	$uid = (int)$GLOBALS['winduid'];
	$gid = (int)$GLOBALS['groupid'];
	$fid===false && $fid = (int)$GLOBALS['fid'];

	if (empty($uid) || empty($gid) || empty($fid)) return false;

	if (!isset($_SYSTEM[$fid])) {
		$_SYSTEM[$fid] = $BMSystem = array();
		$isUser = false;

		$pwSQL = 'uid='.pwEscape($uid,false).'AND fid='.pwEscape($fid,false)."AND gid='0'";
		if ($isBM && $gid != 5) {//获取版主权限
			$pwSQL .= " OR uid='0' AND fid=".pwEscape($fid,false)."AND gid IN ('5',".pwEscape($gid,false).") OR uid='0' AND fid='0' AND gid='5'";
		} else {
			$pwSQL .= " OR uid='0' AND fid=".pwEscape($fid,false)."AND gid=".pwEscape($gid,false);
		}
		$query = $GLOBALS['db']->query("SELECT uid,fid,gid,rkey,rvalue FROM pw_permission WHERE ($pwSQL) AND type='systemforum' ORDER BY uid DESC,fid");
		while ($rt = $GLOBALS['db']->fetch_array($query)) {
			if ($rt['uid'] == $uid) {//用户个人权限
				$_SYSTEM[$fid][$rt['rkey']] = $rt['rvalue'];
				$isUser = true;
			} elseif ($isUser) {//取得个人权限,结束
				break;
			} elseif ($isBM && $rt['gid'] && $gid != $rt['gid']) {//版主权限
				$BMSystem[$rt['rkey']] = $rt['rvalue'];
			} else {
				$_SYSTEM[$fid][$rt['rkey']] = $rt['rvalue'];
			}
		}
		if (!$isUser) {
			empty($_SYSTEM[$fid]) && ($GLOBALS['SYSTEM']['superright'] || $isBM && $gid == 5) && $_SYSTEM[$fid] = $GLOBALS['SYSTEM'];
			if ($BMSystem) {//版主权限加权
				foreach ($BMSystem as $key=>$value) {
					$_SYSTEM[$fid][$key] < $value && $_SYSTEM[$fid][$key] = $value;
				}
			}
		}
	}
	return empty($rkey) ? $_SYSTEM[$fid] : $_SYSTEM[$fid][$rkey];
}
function modeEot($template,$EXT='htm'){
	global $db_mode,$db_tplpath;
	if ($db_mode == 'area') {
		$srcTpl = getAreaSrcTpl($template,$EXT);
		$tarTpl = D_P."data/tplcache/".$db_tplpath.$template.'.'.$EXT;
	} else {

		$srcTpl = M_P."template/$template.$EXT";
		$tarTpl = D_P."data/tplcache/".$db_mode.'_'.$template.'.'.$EXT;
	}
	if (!file_exists($srcTpl)) {
		return false;
	}
	if (pwFilemtime($tarTpl)>pwFilemtime($srcTpl)) {
		return $tarTpl;
	} else {
		return modeTemplate($template,$srcTpl,$tarTpl);
	}
}

function getAreaSrcTpl($template,$EXT='htm'){
	global $db_tplstyle,$area_indextpl;
	if ($template=='header' || $template=='footer') {
		$srcTpl = M_P."themes/$area_indextpl/$template.$EXT";
	} else {
		$srcTpl = M_P."themes/$db_tplstyle/$template.$EXT";
	}
	if (!file_exists($srcTpl)) {
		global $area_indextpl;
		if ($area_indextpl!='default' && file_exists(M_P."themes/$area_indextpl/$template.$EXT")) {
			$srcTpl = M_P."themes/$area_indextpl/$template.$EXT";
		} else {
			$srcTpl = M_P."themes/default/$template.$EXT";
		}
	}
	return $srcTpl;
}

function modeTemplate($tplname,$srcTpl,$tarTpl){
	global $db_modepages;
	$file_str	= readover($srcTpl);
	$parsepw	= L::loadClass('parsepw');
	$file_str	= $parsepw->execute($tplname,$file_str);
	writeover($tarTpl,$file_str);
	return $tarTpl;
}

function getCateid($fid) {
	global $forum;
	if (in_array($forum[$fid]['type'],array('sub2','sub','forum'))) {
		return getCateid($forum[$fid]['fup']);
	} elseif ($forum[$fid]['type'] == 'category') {
		return $fid;
	} else {
		return false;
	}
}

function pwConvert($str,$to_encoding,$from_encoding,$ifmb=true) {
	if (strtolower($to_encoding) == strtolower($from_encoding)) {
		return $str;
	}
	if (is_array($str)) {
		foreach ($str as $key=>$value) {
			$str[$key] = pwConvert($value,$to_encoding,$from_encoding,$ifmb);
		}
		return $str;
	} else {
		if (function_exists('mb_convert_encoding') && $ifmb) {
			return mb_convert_encoding($str,$to_encoding,$from_encoding);
		} else {
			static $pwconvert = null;
			!$to_encoding && $to_encoding = 'GBK';
			!$from_encoding && $from_encoding = 'GBK';
			if (!isset($pwconvert) && !is_object($pwconvert)) {
				require_once(R_P.'m/chinese.php');
				$pwconvert = new Chinese();
			}
			return $pwconvert->Convert($str,$from_encoding,$to_encoding,!$ifmb);
		}
	}
}
function pwCreditNames($ctype = null) {
	static $creditnames = null;
	if (!isset($creditnames)) {
		$creditnames = array('money' => $GLOBALS['db_moneyname'], 'rvrc' => $GLOBALS['db_rvrcname'],'credit' => $GLOBALS['db_creditname'], 'currency' => $GLOBALS['db_currencyname']);
		foreach ($GLOBALS['_CREDITDB'] as $key => $value) {
			$creditnames[$key] = $value[0];
		}
	}
	return isset($ctype) ? $creditnames[$ctype] : $creditnames;
}
function pwCreditUnits($ctype = null) {
	static $creditunits = null;
	if (!isset($creditunits)) {
		$creditunits = array('money' => $GLOBALS['db_moneyunit'], 'rvrc' => $GLOBALS['db_rvrcunit'],'credit' => $GLOBALS['db_creditunit'], 'currency' => $GLOBALS['db_currencyunit']);
		foreach ($GLOBALS['_CREDITDB'] as $key => $value) {
			$creditunits[$key] = $value[1];
		}
	}
	return isset($ctype) ? $creditunits[$ctype] : $creditunits;
}
function minImage($sourceImg,$width,$height){
	static $mini = 0;
	global $db_bbsurl,$attachdir,$db_attachname;
	if (strpos($sourceImg,'://')) {
		return $sourceImg;
	}
	if ($mini == 0) {
		if (file_exists($attachdir."/mini")) {
			$mini = 1;
		} else {
			if (mkdir($attachdir."/mini")) {
				@chmod($attachdir."/mini",0777);
				$mini = 1;
			} else {
				$mini = 2;
			}
		}
	}
	if ($mini == 1) {
		$width = (int)$width;
		$height = (int)$height;
		if (!$width || !$height) {
			Showmsg('minimage_wh_error');
		}
		$file_ext	= end(explode('.',$sourceImg));
		$imgname	= substr(md5($sourceImg.$width.$height),10,10).'.'.$file_ext;
		if(file_exists($attachdir."/mini/".$imgname)) {
			return $db_bbsurl.'/'.$db_attachname."/mini/".$imgname;
		}
		$srcfile	= ((strpos($sourceImg,$db_attachname)===0 || strpos($sourceImg,'images')===0 )? R_P : $attachdir).$sourceImg;
		$targtImg	= $attachdir."/mini/".$imgname;
		require_once(R_P.'require/imgfunc.php');
		$thumbsize = modeImageThumb($srcfile,$targtImg,$width,$height);

		if ($thumbsize) {
			$fileurl = $db_bbsurl.'/'.$db_attachname."/mini/".$imgname;
			return $fileurl;
		} else {
			return $sourceImg;
		}
	} else {
		return $sourceImg;
	}
}
function ObContents($output){
	//Copyright (c) 2003-09 PHPWind
	ob_end_clean();
	$getHAE = GetServer('HTTP_ACCEPT_ENCODING');
	if (!headers_sent() && $GLOBALS['db_obstart'] && $getHAE && N_output_zip()!='ob_gzhandler') {
		$encoding = '';
		if (strpos($getHAE,'x-gzip') !== false) {
			$encoding = 'x-gzip';
		} elseif (strpos($getHAE,'gzip') !== false) {
			$encoding = 'gzip';
		}
		if ($encoding && function_exists('crc32') && function_exists('gzcompress')) {
			header('Content-Encoding: '.$encoding);
			$outputlen  = strlen($output);
			$outputzip  = "\x1f\x8b\x08\x00\x00\x00\x00\x00";
			$outputzip .= substr(gzcompress($output,$GLOBALS['db_obstart']),0,-4);
			$outputzip .= @pack('V',crc32($output));
			$output = $outputzip.@pack('V',$outputlen);
		} else {
			ObStart();
		}
	} else {
		ObStart();
	}
	return $output;
}
function ObStart(){
	//Copyright (c) 2003-09 PHPWind
	ObGetMode() == 1 ? ob_start('ob_gzhandler') : ob_start();
}
function ObGetMode(){
	//Copyright (c) 2003-09 PHPWind
	static $mode = null;
	if ($mode!==null) {
		return $mode;
	}
	$mode = 0;
	if ($GLOBALS['db_obstart'] && function_exists('ob_gzhandler') && N_output_zip()!='ob_gzhandler' && (!function_exists('ob_get_level') || ob_get_level()<1)) {
		$mode = 1;
	}
	return $mode;
}
function N_flush($ob=null){
	//Copyright (c) 2003-09 PHPWind
	if (php_sapi_name()!='apache2handler' && php_sapi_name()!='apache2filter') {
		if (N_output_zip() == 'ob_gzhandler') {
			return;
		}
		if ($ob && ob_get_length()!==false && ob_get_status() && !ObGetMode($GLOBALS['db_obstart'])) {
			@ob_flush();
		}
		flush();
	}
}
function N_output_zip(){
	//Copyright (c) 2003-09 PHPWind
	static $output_handler = null;
	if ($output_handler === null) {
		if (@ini_get('zlib.output_compression')) {
			$output_handler = 'ob_gzhandler';
		} else {
			$output_handler = @ini_get('output_handler');
		}
	}
	return $output_handler;
}
function ajax_footer(){
	global $db_charset;
	$output = str_replace(array('<!--<!---->','<!---->'),'',ob_get_contents());
	header("Content-Type: text/xml;charset=$db_charset");
	echo ObContents("<?xml version=\"1.0\" encoding=\"$db_charset\"?><ajax><![CDATA[".$output."]]></ajax>");exit;
}

function numofpage($count,$page,$numofpage,$url,$max=null,$ajaxurl='') {
	global $tablecolor;
	$total = $numofpage;
	if (!empty($max)) {
		$max = (int)$max;
		$numofpage > $max && $numofpage = $max;
	}
	if ($numofpage <= 1 || !is_numeric($page)) {
		return '';
	} else {
		list($url,$mao) = explode('#',$url);
		$mao && $mao = '#'.$mao;
		$pages = "<div class=\"pages\"><a href=\"{$url}page=1$mao\" class=\"b\"".($ajaxurl ? " onclick=\"return ajaxview('{$ajaxurl}page=1')\"" : '').">&laquo;</a>";
		for ($i = $page-3; $i <= $page-1; $i++) {
			if($i<1) continue;
			$pages .= "<a href=\"{$url}page=$i$mao\"".($ajaxurl ? " onclick=\"return ajaxview('{$ajaxurl}page=$i')\"" : '').">$i</a>";
		}
		$pages .= "<b>$page</b>";
		if ($page < $numofpage) {
			$flag = 0;
			for ($i = $page+1; $i <= $numofpage; $i++) {
				$pages .= "<a href=\"{$url}page=$i$mao\"".($ajaxurl ? " onclick=\"return ajaxview('{$ajaxurl}page=$i')\"" : '').">$i</a>";
				$flag++;
				if ($flag == 4) break;
			}
		}
		$pages .= "<a href=\"{$url}page=$numofpage$mao\" class=\"b\"".($ajaxurl ? " onclick=\"return ajaxview('{$ajaxurl}page=$numofpage')\"" : '').">&raquo;</a><span class=\"pagesone\"><span>Pages: $page/$total</span><input type=\"text\" size=\"3\" onkeydown=\"javascript: if(event.keyCode==13){".($ajaxurl ? "ajaxview('{$ajaxurl}page='+this.value);" : " location='{$url}page='+this.value+'{$mao}';")."return false;}\"><button onclick=\"javascript: ".($ajaxurl ? "ajaxview('{$ajaxurl}page='+this.previousSibling.value);" : " location='{$url}page='+this.previousSibling.value+'{$mao}';")."return false;\">Go</button></span></div>";
		return $pages;
	}
}
/**
 * 服务器时间校正后的文件修改时间
 *
 * @param string $file 文件路径
 * @return int 返回修改时间
 */
function pwFilemtime($file) {
	return file_exists($file) ? intval(filemtime($file) + $GLOBALS['db_cvtime']*60) : 0;
}
function pwDelatt($path,$ifftp) {
	if (strpos($path,'..') !== false) {
		return false;
	}
	if (!file_exists("$GLOBALS[attachdir]/$path")) {
		if (pwFtpNew($GLOBALS['ftp'],$ifftp)) {
			$GLOBALS['ftp']->delete($path);
			$GLOBALS['ftp']->delete('thumb/'.$path);
		}
	} else {
		P_unlink("$GLOBALS[attachdir]/$path");
		if (file_exists("$GLOBALS[attachdir]/thumb/$path")) {
			P_unlink("$GLOBALS[attachdir]/thumb/$path");
		}
	}
	return true;
}
function pwFtpNew(&$ftp,$ifftp) {
	if (!$ifftp) return false;
	static $ftp_server,$ftp_port,$ftp_user,$ftp_pass,$ftp_dir;
	if (!is_object($ftp)) {
		require_once(R_P.'lib/ftp.class.php');
		$ftp = new FTP($ftp_server,$ftp_port,$ftp_user,$ftp_pass,$ftp_dir);
	}
	return true;
}
function pwFtpClose(&$ftp) {
	if (is_object($ftp) && method_exists($ftp,'close')) {
		$ftp->close();
		$ftp = null;
	}
}
function descriplog($message) {
	$message = str_replace(array("\n",'[b]','[/b]'),array('<br />','<b>','</b>'),$message);
	if (strpos($message,'[/URL]')!==false || strpos($message,'[/url]')!==false) {
		$message = preg_replace("/\[url=([^\[]+?)\](.*?)\[\/url\]/is","<a href=\"\\1\" target=\"_blank\">\\2</a>",$message);
	}
	return $message;
}

/**
 * 将数组格式化成json格式
 *
 * @param  $type
 * @return string
 */
function pwJsonEncode($var){
	switch (gettype($var)) {
		case 'boolean':
			return $var ? 'true' : 'false';
		case 'NULL':
			return 'null';
		case 'integer':
			return (int) $var;
		case 'double':
		case 'float':
			return (float) $var;
		case 'string':
			return '"'.str_replace("\'","'",addslashes(str_replace(array("\n","\r","\t"),'',$var))).'"';
		case 'array':
			if (count($var) && (array_keys($var) !== range(0, sizeof($var) - 1))) {
				$properties = array();
				foreach ($var as $name=>$value) {
					$properties[] = pwJsonEncode(strval($name)) . ':' . pwJsonEncode($value);
				}
				return '{' . join(',', $properties) . '}';
			}
			$elements = array_map('pwJsonEncode', $var);
			return '[' . join(',', $elements) . ']';
	}
	return false;
}

/**
 * 获取好友列表
 *
 * @param int $uid		需要查找的uid;
 * @param int $start	limit条件
 * @param int $num		limit条件
 * @param int $ftype	好友分组
 * @param int $show		是否需要详细数据
 * @return array
 */
function getFriends($uid,$start=0,$num=0,$ftype=false,$show=false,$imgtype='m'){
	global $db,$db_onlinetime,$timestamp,$winduid;
	$fild	= 'm.uid,m.username,f.ftid,f.iffeed';
	$order  = $where = '';
	if ($show) {
		$fild .= ',m.icon as face,m.honor,md.f_num,md.thisvisit,md.lastvisit';
		$left = 'LEFT JOIN pw_memberdata md ON f.friendid=md.uid';
		$order = 'md.thisvisit';
	} else {
		$left = '';
		$order = 'f.joindate';
	}
	if ($ftype !== false && $ftype !== '') {
		$ftype	= (int)$ftype;
		$where = ' AND f.ftid='.pwEscape($ftype);
	}
	$start	= (int) $start;
	$num	= (int) $num;
	if ($start || $num) {
		!$num && $num = 8;
		$limit = pwLimit($start,$num);
	} else {
		$limit = '';
	}
	$rs = $db->query("SELECT $fild FROM pw_friends f LEFT JOIN pw_members m ON f.friendid=m.uid $left WHERE f.uid=".pwEscape($uid)." AND f.status=0 $where ORDER BY $order DESC $limit");
	$result = array();
	if ($show) {
		require_once(R_P.'require/showimg.php');
		while ($one = $db->fetch_array($rs)) {
			list($one['face']) = showfacedesign($one['face'],1,$imgtype);
			$one['honor'] = substrs($one['honor'],90);
			$one['lastvisit']	= get_date($one['lastvisit']);
			$result[$one['uid']] = $one;
		}
	} else {
		while ($one = $db->fetch_array($rs)) {
			$result[$one['uid']] = $one;
		}
	}
	count($result) == 0 && $result = false;
	return $result;
}

/**
 * 添加会员最新动作
 *
 * @param int $uid		动作会员UID
 * @param string $type	动作类型
 * @param mixed $log	动作描述
 */
function pwAddFeed($uid,$type,$typeid,$log) {
	global $db,$timestamp;

	//判断该用户的该操作是否要要生成动态
	if (in_array($type,array('post','write','diary','share','photo'))){
		$fieldname = $type == 'post' ? 'article_isfeed' : ($type == 'photo' ? 'photos_isfeed' : $type.'_isfeed');
		$isfeed = $db->get_value("SELECT $fieldname FROM pw_ouserdata WHERE uid=".pwEscape($uid));
		if(!$isfeed) return false;
	}
	if (is_array($log)) {
		empty($log['lang']) && $log['lang'] = $type;
		$descrip = Char_cv(getLangInfo('feed',$log['lang'],$log));
	} else {
		$descrip = Char_cv($log);
	}
	$typeid = (int)$typeid;
	$db->update("INSERT INTO pw_feed SET " . pwSqlSingle(array(
		'uid'		=> $uid,
		'type'		=> $type,
		'typeid'	=> $typeid,
		'descrip'	=> $descrip,
		'timestamp'	=> $timestamp
	),false));
	return true;
}

function checkOnline($time){
	global $db_onlinetime,$timestamp;
	if ($time+$db_onlinetime*1.5>$timestamp) {
		return true;
	}
	return false;
}
/**
 * 用户未读短消息更新
 * @param (int|array) $uids 需要更新的用户uid
 * @param string $type recount:重新统计,add:指定用户未读短消息加1,minus:指定用户未读短消息减1
 */
function updateNewpm($uids,$type='add'){
	global $db;
	if (!is_array($uids)) {
		$userdb = $db->get_one("SELECT uid,groupid,regdate FROM pw_members WHERE uid=".pwEscape($uids));
		empty($userdb) && Showmsg('undefined_action');
		$sqladd = 'uid='.pwEscape($userdb['uid']);
	} else {
		$sqladd = 'uid IN ('.pwImplode($uids).')';
	}
	if ($type == 'recount') {
		if (is_array($uids)) {
			$query = $db->query("SELECT uid,groupid,regdate FROM pw_members WHERE uid IN (".pwImplode($uids).')');
			while ($userdb = $db->fetch_array($query)) {
				$private_newmsg_num = getPrivateNewmsgNum($userdb['uid']);
				$public_msg_record = getUserPublicMsgRecord($userdb['uid']);
				$public_newmsg_num = getPublicNewmsgNum($userdb['groupid'],$userdb['regdate'],$public_msg_record);
				$all_newmsg_num = $private_newmsg_num+$public_newmsg_num;
				$db->update("UPDATE pw_members SET newpm=".pwEscape($all_newmsg_num)." WHERE uid=".pwEscape($userdb['uid']));
			}
		} else {
			$private_newmsg_num = getPrivateNewmsgNum($userdb['uid']);
			$public_msg_record = getUserPublicMsgRecord($userdb['uid']);
			$public_newmsg_num = getPublicNewmsgNum($userdb['groupid'],$userdb['regdate'],$public_msg_record);
			$all_newmsg_num = $private_newmsg_num+$public_newmsg_num;
			$db->update("UPDATE pw_members SET newpm=".pwEscape($all_newmsg_num)." WHERE uid=".pwEscape($userdb['uid']));
		}
	} elseif ($type == 'add') {
		$db->update("UPDATE pw_members SET newpm=newpm+1 WHERE $sqladd");
	} elseif ($type == 'minus') {
		newpmMinusOne($uids);
	}
}

function newpmMinusOne($uids) {
	global $db;
	if (!is_array($uids)){
		if (getNewpm($uids) > 0 ){
			$db->update("UPDATE pw_members SET newpm=newpm-1 WHERE uid=".pwEscape($uids));
		}
	} else {
		foreach ($uids as $key => $uid) {
			if (getNewpm($uid) > 0) {
				$db->update("UPDATE pw_members SET newpm=newpm-1 WHERE uid=".pwEscape($uid));
			}
		}
	}
}

function getNewpm($uid) {
	global $db;
	$newpm = $db->get_value("SELECT newpm FROM pw_members WHERE uid=".pwEscape($uid));
	return $newpm;
}

function getPrivateNewmsgNum($uid){
	global $db;
	$count = $db->get_value("SELECT COUNT(*) FROM pw_msg WHERE touid=".pwEscape($uid)." AND ifnew='1' AND type='rebox'");
	return $count;
}

function getPublicNewmsgNum($groupid,$regdate,$public_msg_record){
	global $db,$winddb;
	$msg_gid = $winddb['groupid'];
	$checkmsg = $public_msg_record ? implode(',',$public_msg_record) : '';
	$query = $db->query("SELECT mid FROM pw_msg WHERE type='public' AND togroups LIKE ".pwEscape("%,$msg_gid,%")." AND mdate>".pwEscape($regdate)." ORDER BY mdate DESC LIMIT 20");
	$pub_count = 0;
	while ($rt = $db->fetch_array($query)) {
		if (strpos(",$checkmsg,",",$rt[mid],") === false) {
			$pub_count++;
		}
	}
	return $pub_count;
}
/**
 * 获取群发短消息在用户表中已读和删除短消息的记录
 *
 * @param int $uid
 */
function getUserPublicMsgRecord($uid){
	global $db;
	return $db->get_one('SELECT readmsg,delmsg FROM pw_memberinfo WHERE uid='.pwEscape($uid));
}
function getForumName($fid){
	$temp_forum = getForumCache();
	if (isset($temp_forum[$fid])) {
		return strip_tags($temp_forum[$fid]['name']);
	}
	return '';
}
function getForumCache(){
	static $temp_forum = array();

	if (!$temp_forum) {
		global $forum;
		if (!$forum) {
			include(D_P.'data/bbscache/forum_cache.php');
		}
		$temp_forum = $forum;
	}
	return $temp_forum;
}
function getForumUrl($fid){
	$fid = (int) $fid;
	if ($fid) {
		return 'thread.php?fid='.$fid;
	}
	return '';
}
function pwTplGetData($invokename,$title,$loopid=false){
	$tplgetdata = L::loadClass('tplgetdata');
	return $tplgetdata->getData($invokename,$title,$loopid);
}
function pwCycleLoops($invokename){
	global $SCR,$fid;
	$pw_invoke = L::loadDB('invoke');
	$config	= $pw_invoke->getDataByName($invokename);
	if ($config['loops']) {
		return $config['loops'];
	}
	return array();
}

/**
 * 获取用户的唯一字符串
 */
function appkey($uid,$app=false,$add=false){
	global $db_hash;
	return substr(md5($uid.$db_hash.($add ? $add : '').($app ? $app : '')),8,18);
}

function getmemberid($nums){
	global $lneed;
	$lneed || $lneed = L::config('lneed', 'level');
	arsort($lneed); reset($lneed);
	foreach ($lneed as $key => $lowneed) {
		$gid = $key;
		if ($nums >= $lowneed) {
			break;
		}
	}
	return $gid;
}

function CalculateCredit($creditdb,$upgradeset) {
	$credit = 0;
	foreach ($upgradeset as $key => $val) {
		if ($creditdb[$key] && $val) {
			if ($key == 'rvrc') {
				$creditdb[$key] = round($creditdb[$key]/10,1);
			} elseif ($key == 'onlinetime') {
				$creditdb[$key] = (int)($creditdb[$key]/3600);
			}
			$credit += (int)$creditdb[$key]*$val;
		}
	}
	return (int)$credit;
}
/**
 * Strip WindCode from a string
 */
function stripWindCode($text) {
	$pattern = array();
	if (strpos($text,"[post]")!==false && strpos($text,"[/post]")!==false) {
		$pattern[] = "/\[post\].+?\[\/post\]/is";
	}
	if (strpos($text,"[hide=")!==false && strpos($text,"[/hide]")!==false) {
		$pattern[] = "/\[hide=.+?\].+?\[\/hide\]/is";
	}
	if (strpos($text,"[sell")!==false && strpos($text,"[/sell]")!==false) {
		$pattern[] = "/\[sell=.+?\].+?\[\/sell\]/is";
	}
	$pattern[] = "/\[[a-zA-Z]+[^]]*?\]/is";
	$pattern[] = "/\[\/[a-zA-Z]*[^]]\]/is";

	$text = preg_replace($pattern,'',$text);
	return trim($text);
}

function isGM($name) {
	global $manager;
	return CkInArray($name,$manager);
}

/**
 * hash缓存实例化工厂
 */
function getDatastore($datastore = null) {
	global $db_datastore;
	$datastore || $datastore = $db_datastore;
	switch (strtolower($datastore)) {
		case 'memcache' :
			$_cache = L::loadClass('Memcache');
			break;
		case 'dbcache' :
			$_cache = L::loadClass('DBCache');
			break;
		default :
			$_cache = L::loadClass('DBCache');
			break;
	}
	return $_cache;
}

/**
 * 更新数据缓存库
 *
 */
function updateDatanalyse($tag, $action, $num) {
	global $db,$tdtime;
	$tag = (int)$tag; $num = (int)$num;
	$history = mktime ( 0, 0, 0, 0, 0, 0);
	if (!empty($tag) && !empty($action)) {
//		$db->pw_update(
//			"SELECT num FROM pw_datanalyse WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false)."AND timeunit=".pwEscape($tdtime,false),
//			"UPDATE pw_datanalyse SET num=num+".pwEscape($num,false) ." WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false)."AND timeunit=".pwEscape($tdtime,false),
//			"INSERT INTO pw_datanalyse SET tag=".pwEscape($tag,false).",action=".pwEscape($action,false).",timeunit=".pwEscape($tdtime,false).",num=".pwEscape($num,false)
//		);
//		$db->pw_update(
//			"SELECT num FROM pw_datanalyse WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false)."AND timeunit=".pwEscape($history,false),
//			"UPDATE pw_datanalyse SET num=num+".pwEscape($num,false) ." WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false)."AND timeunit=".pwEscape($history,false),
//			"INSERT INTO pw_datanalyse SET tag=".pwEscape($tag,false).",action=".pwEscape($action,false).",timeunit=".pwEscape($history,false).",num=".pwEscape($num,false)
//		);
		//sql optimize
		$isTdtime = $isHistory = 0;
		$timeuints = array($tdtime,$history);
		$query = $db->query("SELECT * FROM pw_datanalyse WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false));
		while($rs = $db->fetch_array($query)){
			if($rs['timeunit'] == $tdtime){
				$isTdtime = 1;
			}
			if($rs['timeunit'] == $history){
				$isHistory = 1;
			}
		}
		if($isTdtime && $isHistory){
			return $db->query("UPDATE pw_datanalyse SET num=num+".pwEscape($num,false) ." WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false)."AND timeunit IN (".pwImplode($timeuints).")");
		}
		if($isTdtime == 0 && $isHistory == 0){
			return $db->query("INSERT INTO pw_datanalyse (tag,action,timeunit,num) VALUES (".pwEscape($tag,false).",".pwEscape($action,false).",".pwEscape($tdtime,false).",".pwEscape($num,false)."),(".pwEscape($tag,false).",".pwEscape($action,false).",".pwEscape($history,false).",".pwEscape($num,false).")");
		}
		if($isTdtime){
			$db->query("UPDATE pw_datanalyse SET num=num+".pwEscape($num,false) ." WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false)."AND timeunit=".pwEscape($tdtime,false));
		}else{
			$db->query("INSERT INTO pw_datanalyse SET tag=".pwEscape($tag,false).",action=".pwEscape($action,false).",timeunit=".pwEscape($tdtime,false).",num=".pwEscape($num,false));
		}
		if($isHistory){
			$db->query("UPDATE pw_datanalyse SET num=num+".pwEscape($num,false) ." WHERE tag=".pwEscape($tag,false)."AND action=".pwEscape($action,false)."AND timeunit=".pwEscape($history,false));
		}else{
			$db->query("INSERT INTO pw_datanalyse SET tag=".pwEscape($tag,false).",action=".pwEscape($action,false).",timeunit=".pwEscape($history,false).",num=".pwEscape($num,false));
		}
	}
}
/**
 * 加载类(包括通用类和通用配置文件的加载)
 */
class L {

	function loadClass($className,$dir ='',$check=false) {
		static $classes = array();
		$suffix = '.class.php';
		if ($dir) {
			$dir = strtolower(trim($dir));
			if ($dir == 'db') {
				if (!class_exists('BaseDB')) require(R_P.'lib/base/basedb.php');
				$fileDir = R_P.'lib/'.$dir.'/'.strtolower($className).'.db.php';
				$className .= 'DB';
			} else {
				$fileDir = R_P.'lib/'.$dir.'/'.strtolower($className).$suffix;
			}
		} else {
			$fileDir = R_P.'lib/'.strtolower($className).$suffix;
		}
		if (isset($classes[$className])) {
			return $classes[$className];
		} elseif ($check == true) {
			return false;
		}
		if (file_exists($fileDir)) {
			$class = 'PW_'.$className;
			if (!class_exists($class)) require Pcv($fileDir);
			$classes[$className] = &new $class();
		} else {
			Showmsg('load_class_error');
		}
		return $classes[$className];
	}

	function loadDB($dbName) {
		return L::loadClass($dbName,'db');
	}

	function config($var = null, $file = 'config', $dir = 'bbscache', $isStatic = true) {
		static $conf = array();
		if (!isset($conf[$file])) {
			if (file_exists(D_P . "data/$dir/{$file}.php")) {
				include Pcv(D_P . "data/$dir/{$file}.php");
				$arr = get_defined_vars();
				unset($arr['dir'], $arr['file'], $arr['var'], $arr['conf'], $arr['isStatic']);
				if ($isStatic !== true) {
					return $var ? $arr[$var] : $arr;
				}
				$conf[$file] = $arr;
			} else {
				$conf[$file] = array();
			}
		}
		return $var ? $conf[$file][$var] : $conf[$file];
	}

	function reg($var = null) {
		return L::config($var, 'dbreg');
	}

	function style($var = null, $skinco = null, $ispath = false) {
		global $skin,$db_styledb,$db_defaultstyle;
		$skinco && isset($db_styledb[$skinco]) && $skin = $skinco;

		if (strpos($skin, '..')===false && file_exists(D_P . "data/style/$skin.php") && is_array($db_styledb[$skin]) &&$db_styledb[$skin][1] == '1') {

		} elseif (strpos($db_defaultstyle, '..') === false && file_exists(D_P . "data/style/$db_defaultstyle.php")) {
			$skin = $db_defaultstyle;
		} else {
			$skin = 'wind';
		}
		return !$ispath ? L::config($var, $skin, 'style') : Pcv(D_P . 'data/style/'.$skin.'.php');
	}

	function forum($fid) {
		return L::config('foruminfo', 'fid_'.intval($fid), 'forums', false);
	}
}

function initJob($userId,$jobName,$factor=array()){
	global $db_job_isopen;
	if(!$db_job_isopen){
		return;
	}
	$jobService = L::loadclass("job");
	$jobService->jobController($userId,$jobName,$factor);
}

/*主题印戳*/
function overPrint($overprint,$tid,$operate='',$oid=''){
	if(!in_array($overprint,array(1,2))){
		return false;
	}
	$overPrintService = L::loadclass("overprint");
	/*过滤*/
	if($overPrintService->checkThreadRelated($overprint,$operate,$tid)){
		return false;
	}
	if($overprint == 2){
		$oid = 0;$operate='';
	}
	$overPrintService->suckThread($tid,$operate,$oid);
}
?>