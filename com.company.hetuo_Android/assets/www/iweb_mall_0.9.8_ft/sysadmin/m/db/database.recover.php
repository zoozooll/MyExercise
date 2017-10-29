<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("../foundation/module_admin_logs.php");
//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_admin_log = $tablePreStr."admin_log";

$filedb=array();
$files = scandir('../docs');
$i=0;
$file_array=array();
foreach($files as $key=>$val){
	if(substr($val,0,5)=='mall_'){
		$file_array[$i][0]=$val;

		$file = scandir('../docs/'.$val);

		foreach($file as $item=>$na){
			if($item>=2){
				$strlen=16;
				$fp=fopen("../docs/$val/$na",'rb');
				$bakinfo=fread($fp,200);
				fclose($fp);
				$detail=explode("\n",$bakinfo);
				$bk['name']=$na;
				$bk['version']=substr($detail[1],10);
				$bk['time']=substr($detail[2],7);
				$bk['pre']=substr($na,0,$strlen);
				$bk['num']=substr($na,$strlen,strrpos($na,'.')-$strlen);

				$bk['MB'] = round((filesize('../docs/'.$val.'/'.$na))/1024*100)/100;
				$bk['MB'] = ($bk['MB']>1024) ? (round($bk['MB']/1024*100)/100).'M' : $bk['MB'].'K';
				$filedb[$i][]=$bk;
			}
		}
		$i++;
	}
}

//导入备份文件
$operation	= get_args('operation');
if($operation=='bakin'){
	//权限管理
	$right=check_rights("data_recover");
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}
	$dbo = new dbex;
	dbtarget('w',$dbServs);

	$pre=get_args('pre');
	$dir='../docs/'.$pre;
	$file = scandir($dir);

	foreach($file as $key=>$val){
		if($key>=2){
			$key=$key-1;
			echo $d=$dir.'/'.$key.'.sql';
			bakindata($dbo,$dir.'/'.$key.'.sql');
		}
	}
	/** 添加log */
	$admin_log ="导入数据";
	admin_log($dbo,$t_admin_log,$admin_log);

	echo "<script language='javascript'> alert('".$a_langpackage->a_import_success."'); location.href='m.php?app=db_save'; </script>";
}

//删除备份文件
$fid=get_args('fid');
if($fid){
	$dir='../docs/'.$fid;
	if ($dp = opendir($dir)) {
		while (($file=readdir($dp)) != false) {
			@unlink('../docs/'.$dir.'/'.$file);
		}
	}
	closedir($dp);
	rmdir($dir);

	$dbo = new dbex;
	dbtarget('w',$dbServs);
	/** 添加log */
	$admin_log ="删除备份数据";
	admin_log($dbo,$t_admin_log,$admin_log);

	exit($a_langpackage->a_del_suc);
}

function delfile($filename,$check=1){
	@chmod ($filename, 0777);
	return @unlink($filename);
}
function bakindata($dbo,$filename) {
	$sql=file($filename);
	$query='';
	$num=0;
	foreach($sql as $key => $value){
		$value=trim($value);
		if(eregi("\;$",$value)){
			$query.=$value;
			$dbo->exeUpdate($query);
			$query='';
		} else{
			$query.=$value;
		}
	}
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
<script language='javascript'>
function create_xmlhttp()
{
	if(window.ActiveXObject){
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}else{
		xmlhttp=new XMLHttpRequest();
	}
}
function del_file(file_id)
{
	 create_xmlhttp();
	 xmlhttp.open("GET","m.php?app=db_recover&fid="+file_id,true);
	 xmlhttp.onreadystatechange=function(){
		 if(xmlhttp.readyState==4&&xmlhttp.status==200){
		 	var return_str=xmlhttp.responseText;
			var	return_text=return_str.replace(/[\s\n\r]/g,"");
		 	window.document.getElementById("operate_"+file_id).innerHTML=return_text;
		 	window.document.getElementById("d_operate_"+file_id).innerHTML='';
	 	 }
	 }
	 xmlhttp.send(null);
}
</script>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $a_langpackage->a_dbs_restore; ?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_dbs_restore; ?></h3>
    <div class="content2">
	  <form action="?operation=post" method="post">
	    <TABLE class="list_table" >
		<thead>
	      <tr style="text-align:center;">
	        <th style="width:40px;">ID </th>
	        <th align="left"><?php echo $a_langpackage->a_filename; ?></th>
			<th><?php echo $a_langpackage->a_impropriate_space; ?></th>
	        <th><?php echo $a_langpackage->a_version; ?></th>
	        <th><?php echo $a_langpackage->a_backup_time; ?></th>
	        <th width='40'><?php echo $a_langpackage->a_import; ?></th>
	        <th width='40'><?php echo $a_langpackage->a_delete; ?></th>
	      </tr>
	      </thead>
	      <tbody>

	      <?php foreach($file_array as $key=>$val){?>
	      <tr style="text-align:center;">
	        <td><?php echo $key+1;?></td>
	        <td align="left"><?php echo $val['0'];?></td>
	        <td></td>
	        <td></td>
	        <td></td>
	        <td>
	        	<div id=operate_<?php echo $val['0'];?>>
	        		<a href="?app=db_recover&operation=bakin&pre=<?php echo $val['0']?>" onclick='return confirm("<?php echo $a_langpackage->a_sure_import_dbs; ?>");'><?php echo $a_langpackage->a_import; ?></a>
	       		</div>
	        </td>
	        <td>
	        	<div id=d_operate_<?php echo $val['0'];?>>
		        	<a href='javascript:del_file(<?php echo "\"".$val['0']."\"";?>);' title="<?php echo $d_langpackage->d_del?>" alt="<?php echo $d_langpackage->d_del?>" onclick='return confirm("<?php echo $a_langpackage->a_sure_delete; ?>");'><img src='skin/images/del.png' /></a>
	        	</div>
	        </td>
	      </tr>
	      <div id="filedb">
	      <?php foreach($filedb[$key] as $n=>$file){?>
	      <tr style="text-align:center;">
	        <td></td>
	        <td align="left"><?php echo $file['name'];?></td>
			<td class="center"><?php echo $file['MB'];?></td>
	        <td class="center"><?php echo $file['version'];?></td>
	        <td class="center"><?php echo $file['time'];?></td>
	        <td></td>
	        <td></td>
	      </tr>
	      <?php }?>
	      </div>
	      <?php }?>
	      </tbody>
	    </table>
	  </form>
	  </div>
	 </div>
	</div>
</div>
</body>
</html>