<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("../foundation/module_admin_logs.php");

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_admin_log = $tablePreStr."admin_log";

$operation=get_args('operation');

if($operation=='savebackup'){
	//权限管理
	$right=check_rights("data_backup");
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}
	$dbo = new dbex;
	dbtarget('w',$dbServs);

	$tabledb	= get_args('tabledb');

	if(!$tabledb){
		echo "<script language='javascript'> alert('".$a_langpackage->a_need_select_one."'); history.go(-1);</script>";
		exit;
	}

	if(!is_array($tabledb)){
		$tabledb = str_replace('\"','"',$tabledb);
		$tabledb	= unserialize($tabledb);
	}

	$sizelimit	= get_args('sizelimit');
	$start		= get_args('start');
	$step		= get_args('step');
	$tableid	= get_args('tableid');
	$dataid		= get_args('dataid');
	$data_num	= get_args('data_num');
	$aaa		= get_args('aaa');
	$k			= get_args('k');

	$time=new time_class();
	$time_long=$time->long_time();
//	$format='m_d';
	$time_custom=$time->custom('m_d_H_i');

	$bak="/* iweb_mall Backup SQL File \n Version: ".$SYSINFO['version']." \n Time: ".$time_long."\n iweb_mall: http://www.jooyea.net*/\n\n\n\n";

	$writedata=bakupdata($dbo,$tabledb,$start);

	if($aaa==""){
		$aaa=num_rand(6);
	}
	$filedir='mall_'.$time_custom.'_'.$aaa;

	$filename=$step.'.sql';
	$step++;

	trim($writedata) && writefile($filedir,$filename,$bak.$writedata,true,'ab');

	if($stop==1){
		$tabledb=serialize($tabledb);

		echo "<script language='javascript'>location.href='m.php?app=db_save&operation=savebackup&start=".$start."&step=".$step."&tableid=".$tableid."&tabledb=".$tabledb."&dataid=".$dataid."&sizelimit=".$sizelimit."&data_num=".$data_num."&aaa=".$aaa."&k=".$k."';</script>";
		exit;
	}
	/** 添加log */
	$admin_log ="备份数据库";
	admin_log($dbo,$t_admin_log,$admin_log);

	echo "<script language='javascript'> alert('".$a_langpackage->a_backup_success."'); location.href='m.php?app=db_recover'; </script>";
}
else{
	$step=1;
	$start=0;
	$dbo = new dbex;
	dbtarget('r',$dbServs);
	$tabledb=mysql_query("show table status from $db;");
	while($row[]=mysql_fetch_array($tabledb));
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<title></title>
</head>
<script language="JavaScript" type="text/JavaScript">
function checkAll(form, name) {
	for(var i = 0; i < form.elements.length; i++) {
		var e = form.elements[i];
		if(e.name.match(name)) {
			e.checked = form.elements['chkall'].checked;
		}
	}
}
</script>
<body>

<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $a_langpackage->a_dbs_backup; ?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_dbs_backup; ?></h3>
    <div class="content2">
	  <form action="?app=db_save&operation=savebackup&start=<?php echo $start;?>&step=<?php echo $step;?>" method="post">
	    <TABLE class="list_table" >
		<thead>
	      <tr style="text-align: center;">
	        <th width="50px"><?php echo $a_langpackage->a_select; ?></th>
	        <th width="100px">ID</th>
	        <th align="left" width=""><?php echo $a_langpackage->a_dbs_table; ?></th>
	        <th align="left" width=""><?php echo $a_langpackage->a_dbs_valus; ?></th>
	        <th width="165px"><?php echo $a_langpackage->a_last_update_time; ?></th>
	      </tr>
	      </thead>
	      <?php
	      foreach($row as $n=>$table){
	      	if($table[0]!=""){
	      ?>
	      <tbody>
	      <tr style="text-align:center;">
	        <td width="50px"><input type="checkbox" class="checkbox" name="tabledb[]" value="<?php echo $table[0];?>" /></td>
	        <td width="100px"><?php echo $n+1;?></td>
	        <td align="left"><?php echo $table[0];?></td>
	        <td align="left"><?php echo $table[4];?><?php echo $a_langpackage->a_tiao; ?></td>
	        <td width="165px"><?php echo $table['Update_time'];?></td>
	      </tr>
	      <?php
	      	}
	      }
	      ?>
	      <tr class="nobg">
	        <td align="center"><input type="checkbox" name="chkall" id="chkall" class="checkbox" onclick="checkAll(this.form, 'tabledb')" /></td>
	        <td colspan="2"><?php echo $a_langpackage->a_dbs_size; ?>：<input type="text" class="small-text" name="sizelimit" value="1024" size="8"/>kb
	          <div class="fixsel"><span class="button-container"><input class="regular-button" type="submit" class="btn" name="forumlinksubmit" value="<?php echo $a_langpackage->a_backup; ?>"  /></span> </div></td>
	      </tr>
	      </tbody>
	    </table>
	  </form>
	  </div>
	 </div>
	</div>
</div>

</body>
</html>
<?php
function writefile($filedir,$filename,$data,$check=1,$method="rb+",$iflock=1,$chmod=1){
	if(!file_exists($filedir)){
		@mkdir('../docs/'.$filedir);
	}
	$filename='../docs/'.$filedir.'/'.$filename;
	$check && strpos($filename,'..')!==false;
	touch($filename);
	$handle=fopen($filename,$method);
	if($iflock){
		flock($handle,LOCK_EX);
	}
	fwrite($handle,$data);
	if($method=="rb+") ftruncate($handle,strlen($data));
	fclose($handle);
	$chmod && @chmod($filename,0777);
}
function num_rand($lenth){
	mt_srand((double)microtime() * 1000000);
	$randval="";
	for($i=0;$i<$lenth;$i++){
		$randval.= mt_rand(0,9);
	}
	$randval=substr(md5($randval),mt_rand(0,32-$lenth),$lenth);
	return $randval;
}
function bakupdata($dbo,$tabledb,$start=0){
	global $sizelimit,$tableid,$stop,$t_count,$dataid,$data_num,$k;
	$tableid=$tableid?$tableid:0;
	$dataid=$dataid?$dataid:0;
	$data_num=$data_num?$data_num:0;
	$k=$k?$k:0;
	$stop=0;
	$t_count=count($tabledb);
	$bakupdata="";

	for($i=$tableid;$i<$t_count;$i++){

		if($dataid>=$data_num){
			$dataid=0;
			$k=0;
			$bakupdata.= "DROP TABLE IF EXISTS $tabledb[$i];\n";
			$CreatTable = $dbo->create("SHOW CREATE TABLE $tabledb[$i]");
			$bakupdata.=$CreatTable['Create Table'].";\n\n";
		}

		$sql="SELECT * FROM $tabledb[$i]";
		$rs = $dbo->getRs($sql);
		$data_num=count($rs);

		foreach($rs as $key=>$val){
			if($key>=$dataid){
				$num_F = count($val)/2;
				$k++;
				$table=$tabledb[$i];
				$bakupdata .= "INSERT INTO $table VALUES("."'".addslashes($val[0])."'";
				$tempdb='';
				for($j=1;$j<$num_F;$j++){
					$tempdb.=",'".str_replace("\n",'\n',addslashes($val[$j]))."'";
				}
				$bakupdata .=$tempdb. ");\n";

				if($sizelimit && strlen($bakupdata)>$sizelimit*1000){
					$stop=1;
					break;
				}
			}
		}

		$bakupdata .="\n";
		if($sizelimit && strlen($bakupdata)>$sizelimit*1000){
			$stop=1;
			break;
		}
		$tableid=$t_count;
		$dataid=$data_num;
	}

	if($stop==1){
		$tableid=$i;
		$dataid=$k;
	}
	return $bakupdata;
}
?>