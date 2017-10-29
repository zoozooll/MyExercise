<?php
!function_exists('adminmsg') && exit('Forbidden');

@set_time_limit(0);
!$admintype && $admintype = 'bakout';
$basename = $admintype ? "$admin_file?adminjob=bakup&admintype=$admintype" : "$admin_file?adminjob=bakup";

if ($admintype == 'bakout') {
	if (empty($action)) {
		require_once (R_P.'admin/table.php');
		list($pwdb, $otherdb) = N_getTabledb(true);

		$existTables = array();
		$query = $db->query("SHOW TABLES");
		while ($rt = $db->fetch_array($query, MYSQL_NUM)) {
			$existTables[$rt[0]] = true;
		}

		include PrintEot('bakup');exit;
	} else{
		$pwServer['REQUEST_METHOD']!='POST' && PostCheck($verify);
		InitGP(array('tabledb','sizelimit','start','tableid','step','pre','rows'));

		$bak = "#\n# PHPWind bakfile\n# Version:".$wind_version."\n# Time: ".get_date($timestamp,'Y-m-d H:i')."\n# Type: \n# PHPWind: http://www.phpwind.net\n# --------------------------------------------------------\n\n\n";
		$db->query("SET SQL_QUOTE_SHOW_CREATE = 0");

		$start = intval($start);
		if (!$tabledb && $pre) {
			$tablesel = readover(Pcv(D_P.'data/'.$pre.'table.tmp'),'rb');
			$tabledb = explode("|", $tablesel);
		}
		!$tabledb && adminmsg('operate_error');
		!$step && $sizelimit /= 2;
		$bakupdata  = bakupdata($tabledb, $start);
		$bakuptable = '';
		if (!$step) {
			$step  = 1;
			$start = 0;
			$pre = 'pw_'.get_date($timestamp,'md').'_'.randstr(10).'_';
			$bakuptable = bakuptable($tabledb);
			writeover(Pcv(D_P.'data/'.$pre.'table.tmp'), implode("|", $tabledb), 'wb');
		}
		$f_num = ceil($step/2);
		$filename = $pre.$f_num.'.sql';
		$step++;
		$writedata = $bakuptable ? $bakuptable.$bakupdata : $bakupdata;

		$t_name = $tabledb[$tableid-1];
		$c_n = $start;
		if ($stop == 1) {
			$files = $step-1;
			trim($writedata) && writeover(D_P.'data/'.$filename, $bak.$writedata, 'ab');
			$j_url = "$basename&action=$action&start=$start&tableid=$tableid&sizelimit=$sizelimit&step=$step&pre=$pre&rows=$rows";
			adminmsg('bakup_step', EncodeUrl($j_url), 2);
		} else {
			trim($writedata) && writeover(D_P.'data/'.$filename, $bak.$writedata, 'ab');
			if ($step > 1) {
				for ($i=1; $i<=$f_num; $i++) {
					$bakfile .= '<a href="data/'.$pre.$i.'.sql">'.$pre.$i.'.sql</a><br>';
				}
			}
			@unlink(Pcv(D_P.'data/'.$pre.'table.tmp'));
			adminmsg('bakup_out');
		}
	}
} elseif($admintype == 'bakin') {

	if (empty($action)) {
		$filedb = array();
		$handle = opendir(D_P.'data');
		while($file = readdir($handle)){
			if((!$PW || eregi("^pw_",$file) || eregi("^$PW",$file)) && eregi("\.sql$",$file)){
				$strlen=eregi("^$PW",$file) ? 16 + strlen($PW) : 19;
				$fp=fopen(D_P."data/$file",'rb');
				$bakinfo=fread($fp,200);
				fclose($fp);
				$detail=explode("\n",$bakinfo);
				$bk['name']=$file;
				$bk['version']=substr($detail[2],10);
				$bk['time']=substr($detail[3],8);
				$bk['pre']=substr($file,0,$strlen);
				$bk['num']=substr($file,$strlen,strrpos($file,'.')-$strlen);
				$filedb[]=$bk;
			}
		}
		include PrintEot('bakup');exit;
	} elseif($action=='bakincheck'){
		InitGP(array('pre'));
		include PrintEot('bakup');exit;
	} elseif($action=='bakin'){
		$pwServer['REQUEST_METHOD']!='POST' && PostCheck($verify);
		InitGP(array('step','count','pre'));
		if(!$count){
			$count=0;
			$handle=opendir(D_P.'data');
			while($file = readdir($handle)){
				if(eregi("^$pre",$file) && eregi("\.sql$",$file)){
					$count++;
				}
			}
		}
		!$step && $step=1;
		/*
		$sql=readover(D_P.'data/'.$pre.$step.'.sql');
		bakindata($sql);
		*/
		bakindata(D_P.'data/'.$pre.$step.'.sql');
		$i=$step;
		$step++;
		if($count > 1 && $step <= $count){
			$j_url="$basename&action=bakin&step=$step&count=$count&pre=$pre";
			adminmsg('bakup_in',EncodeUrl($j_url),2);
		}
		updatecache();
		adminmsg('operate_success');
	} elseif($action=='del'){
		InitGP(array('delfile'),'P');
		if(!$delfile) adminmsg('operate_error');
		foreach($delfile as $key => $value){
			if(eregi("\.sql$",$value)){
				P_unlink(D_P."data/$value");
			}
		}
		adminmsg('operate_success');
	}
}

function bakupdata($tabledb,$start=0){
	global $db,$sizelimit,$tableid,$start,$stop,$rows;
	$tableid=$tableid?$tableid-1:0;
	$stop=0;
	$t_count=count($tabledb);
	for($i=$tableid;$i<$t_count;$i++){
		$ts=$db->get_one("SHOW TABLE STATUS LIKE ".pwEscape($tabledb[$i]));
		$rows=$ts['Rows'];
		$flag = true;
		while($flag){
			$limitadd = pwLimit($start,100000);
			$query = $db->query("SELECT * FROM $tabledb[$i] $limitadd");
			$num_F = $db->num_fields($query);

			while ($datadb = $db->fetch_array($query,MYSQL_NUM)){
				$start++;
				$bakupdata .= "INSERT INTO $tabledb[$i] VALUES("."'".$db->escape_string($datadb[0])."'";
				$tempdb='';
				for($j=1;$j<$num_F;$j++){
					$tempdb.=",'".$db->escape_string($datadb[$j])."'";
				}
				$bakupdata .=$tempdb. ");\n";
				if($sizelimit && strlen($bakupdata)>$sizelimit*1000){
					$flag = false;
					break;
				}
			}
			$db->free_result($query);
			if($start>=$rows){
				$flag = false;
				$start=0;
			}
		}

		$bakupdata .="\n";
		if($sizelimit && strlen($bakupdata)>$sizelimit*1000){
			$stop=1;
			break;
		}
	}
	if($stop==1){
		$tableid= ++$i;
	}
	return $bakupdata;
}

function bakuptable($tabledb){
	global $db;
	foreach($tabledb as $key=>$table){
		$creattable.= "DROP TABLE IF EXISTS $table;\n";
		$CreatTable = $db->get_one("SHOW CREATE TABLE $table");
		$CreatTable['Create Table']=str_replace($CreatTable['Table'],$table,$CreatTable['Create Table']);
		$creattable.=$CreatTable['Create Table'].";\n\n";
	}
	return $creattable;
}

function bakindata($filename) {
	global $db,$charset;
	$sql=file($filename);
	$query='';
	$num=0;
	foreach($sql as $key => $value){
		$value=trim($value);
		if(!$value || $value[0]=='#') continue;
		if(eregi("\;$",$value)){
			$query.=$value;
			if(eregi("^CREATE",$query)){
				$extra = substr(strrchr($query,')'),1);
				$tabtype = substr(strchr($extra,'='),1);
				$tabtype = substr($tabtype, 0, strpos($tabtype,strpos($tabtype,' ') ? ' ' : ';'));
				$query = str_replace($extra,'',$query);
				if($db->server_info() > '4.1'){
					$extra = $charset ? "ENGINE=$tabtype DEFAULT CHARSET=$charset;" : "ENGINE=$tabtype;";
				}else{
					$extra = "TYPE=$tabtype;";
				}
				$query .= $extra;
			}elseif(eregi("^INSERT",$query)){
				$query='REPLACE '.substr($query,6);
			}
			$db->query($query);
			$query='';
		} else{
			$query.=$value;
		}

	}
}
?>